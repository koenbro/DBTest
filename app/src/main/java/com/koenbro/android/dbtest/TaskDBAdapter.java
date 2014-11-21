package com.koenbro.android.dbtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * This class manages the <b>read-write</b> into <b>one table</b> of the database. It includes a
 * DatabaseHelper which may possibly
 * abstracted out.
 */
public class TaskDBAdapter  {
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;
    /**
     * DatabaseHelper class. Probably a candidate for refactoring, as it replicates
     * {@link com.koenbro.android.dbtest.DBAdapter#DBHelper}. Each table has a class like this so
     * this DatabaseHelper is repeated for each table and corresponding data object.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DBContract.DB_NAME, null, DBContract.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
    /**
     * constructor
     * @param ctx context
     */
    public TaskDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    public TaskDBAdapter open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getReadableDatabase();
        return (this);
    }
    public void close() {this.mDbHelper.close();}
    /**
     * Method manages <b>read from</b> the database into a cursor. This is one of two methods that
     * are specific to the given data object (in this case, {@code Project}), the other being
     * {@code projectToContentValues(Task)}. The other methods handle the CRUD (create, read, update,
     * delete) functionality.
     * @param cursor
     * @return a new data object (Task) from the database
     */
    Task cursorToTask(Cursor cursor){
        Task task = new Task();
        task.setId(Long.parseLong(cursor.getString(0)));
        task.setName(cursor.getString(1));
        task.setTaskProject(Integer.parseInt(cursor.getString(3)));
        return task;
    }
    /**
     * Method puts (the content of) an object into a {@code ContentValues} so that it can be
     * <b>written into</b> the database. This is one of two methods that
     * are specific to the given data object (in this case, {@code Task}), the other being
     * {@code cursorToProject(Cursor)}. The other methods handle the CRUD (create, read, update,
     * delete) functionality.
     * @param task
     * @return ContentValues, that can be written into the database
     */
    ContentValues taskToContentValues(Task task){
        ContentValues values = new ContentValues();
        values.put(DBContract.TableTask.COLUMN_1, task.getName());
        values.put(DBContract.TableTask.COLUMN_1, String.valueOf(task.getTaskProject()));
        return (values);
    }

    public void addTask(Task task) {
        ContentValues values = taskToContentValues(task);
        this.mDb.insert(DBContract.TableTask.TABLE_NAME, null, values);
//        Log.d(DBContract.TableTask.TAG, "add task " + task.toString());
    }
    public Task getTask(long id) throws SQLException {
        Cursor cursor = this.mDb.query(
                DBContract.TableTask.TABLE_NAME,
                DBContract.TableTask.COLUMNS,
                " id = ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        Task task;
        if (cursor != null)  cursor.moveToFirst();
        task = cursorToTask(cursor);
//            Log.d(DBContract.TableTask.TAG, "getTask(" + id + ") " + task.toString());
        assert cursor != null;
        cursor.close();
        return(task);
    }
    public ArrayList<Task> getAllTasks(){
        ArrayList<Task> tasks = new ArrayList<Task>();
        String query = "SELECT * FROM " + DBContract.TableTask.TABLE_NAME;
        Cursor cursor = this.mDb.rawQuery( query, null);
        Task task;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    task = cursorToTask(cursor);
                    //Log.d(DBContract.TableTask.TAG, "id: " + cursor.getString(0));
                    tasks.add(task);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
//        Log.d(DBContract.TableTask.TAG, "get all tasks() " + tasks.toString());
        return (tasks);
    }
    public boolean updateTask(Task task){
        ContentValues values = taskToContentValues(task);
        return this.mDb.update(DBContract.TableTask.TABLE_NAME,
                values,
                DBContract.TableTask.COLUMN_ID+"=?",
                new String [] {String.valueOf(task.getId())}) > 0;
        //Log.d(DBContract.TableTask.TAG, "updated task " + task.toString());
    }
    public boolean deleteTask(Task task){
        return this.mDb.delete(DBContract.TableTask.TABLE_NAME,
                DBContract.TableTask.COLUMN_ID+" = ?",
                new String[] {String.valueOf(task.getId())}) > 0;
        //Log.d(DBContract.TableTask.TAG, "delete task " + task.toString());
    }

}
