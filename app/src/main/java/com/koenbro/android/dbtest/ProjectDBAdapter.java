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
public class ProjectDBAdapter  {
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
    public ProjectDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    public ProjectDBAdapter open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getReadableDatabase();
        return (this);
    }
    public void close() {this.mDbHelper.close();}

    /**
     * Method manages <b>read from</b> the database into a cursor. This is one of two methods that
     * are specific to the given data object (in this case, {@code Project}), the other being
     * {@code projectToContentValues(Project)}. The other methods handle the CRUD (create, read, update,
     * delete) functionality.
     * @param cursor
     * @return a new data object (Project) from the database
     */
    Project cursorToProject(Cursor cursor){
        Project project = new Project();
        project.setId(Long.parseLong(cursor.getString(0)));
        project.setName(cursor.getString(1));
        project.setCompleted(Integer.parseInt(cursor.getString(2)));
        return project;
    }

    /**
     * Method puts (the content of) an object into a {@code ContentValues} so that it can be
     * <b>written into</b> the database. This is one of two methods that
     * are specific to the given data object (in this case, {@code Project}), the other being
     * {@code cursorToProject(Cursor)}. The other methods handle the CRUD (create, read, update,
     * delete) functionality.
     * @param project
     * @return ContentValues, that can be written into the database
     */
    ContentValues projectToContentValues(Project project){
        ContentValues values = new ContentValues();
        values.put(DBContract.TableProject.COLUMN_1, project.getName());
        values.put(DBContract.TableProject.COLUMN_2, String.valueOf(project.getCompleted()));
        return (values);
    }

    public void addProject(Project project) {
        ContentValues values = projectToContentValues(project);
        this.mDb.insert(DBContract.TableProject.TABLE_NAME, null, values);
//        Log.d(DBContract.TableProject.TAG, "add project " + project.toString());
    }
    public Project getProject(long id) throws SQLException {
        Cursor cursor = this.mDb.query(
                DBContract.TableProject.TABLE_NAME,
                DBContract.TableProject.COLUMNS,
                " id = ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        Project project;
        if (cursor != null)  cursor.moveToFirst();
        project = cursorToProject(cursor);
//            Log.d(DBContract.TableProject.TAG, "getProject(" + id + ") " + project.toString());
        assert cursor != null;
        cursor.close();
        return(project);
    }
    public ArrayList<Project> getAllProjects(){
        ArrayList<Project> projects = new ArrayList<Project>();
        String query = "SELECT * FROM " + DBContract.TableProject.TABLE_NAME;
        Cursor cursor = this.mDb.rawQuery( query, null);
        Project project;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    project = cursorToProject(cursor);
                    //Log.d(DBContract.TableProject.TAG, "id: " + cursor.getString(0));
                    projects.add(project);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
//        Log.d(DBContract.TableProject.TAG, "get all projects() " + projects.toString());
        return (projects);
    }
    public boolean updateProject(Project project){
        ContentValues values = projectToContentValues(project);
        return this.mDb.update(DBContract.TableProject.TABLE_NAME,
                values,
                DBContract.TableProject.COLUMN_ID+"=?",
                new String [] {String.valueOf(project.getId())}) > 0;
        //Log.d(DBContract.TableProject.TAG, "updated project " + project.toString());
    }
    public boolean deleteProject(Project project){
        return this.mDb.delete(DBContract.TableProject.TABLE_NAME,
                DBContract.TableProject.COLUMN_ID+" = ?",
                new String[] {String.valueOf(project.getId())}) > 0;
        //Log.d(DBContract.TableProject.TAG, "delete project " + project.toString());
    }

}
