package com.koenbro.android.dbtest;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * The contract class explicitly specifies the layout of the db schema in a systematic and self-
 * documenting way. Will be called by {@link com.koenbro.android.dbtest.DBAdapter#DBHelper
 * DBAdapter.DatabaseHelper} which extends SQLiteHelper. In addition, each table will have its own DBAdapter,
 * that will likewise contain a DBHelper; see, for example
 * {@link ProjectCRUD}. The question is: can these
 * DBHelpers be refactored?
 *
 * @see com.koenbro.android.dbtest.DBAdapter#DBHelper
 * @see ProjectCRUD
 * @see TaskCRUD
 */
public final class DBContract {
    public static final String DB_NAME = "db_refactor.sqlite";
    public static final int DB_VERSION = 4;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";

    /**
     * empty constructor to prevent someone from accidentally instantiating the contract class
     */
    private DBContract() {
    }

    /**
     * inner class for each table that enumerates its columns
     */
    public abstract static class TableProject implements BaseColumns {
        public static final String TABLE_NAME = "projects";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_1 = "name";
        public static final String COLUMN_2 = "completed ";

        public static final String[] COLUMNS = {COLUMN_ID, COLUMN_1, COLUMN_2};
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                        COMMA_SEP + COLUMN_1 + TEXT_TYPE +
                        COMMA_SEP + COLUMN_2 + INT_TYPE +
                        " )";
        public static final String TAG = "ProjectDB.tag";

        /**
         * Method manages <b>read from</b> the database into a cursor. This is one of two methods that
         * are specific to the given data object (in this case, {@code Project}), the other being
         * {@code objectToCV(Project)}. The other methods handle the CRUD (create, read, update,
         * delete) functionality.
         *
         * @param cursor
         * @return a new data object (Project) from the database
         */
        static Project cursorToObject(Cursor cursor) {
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
         * {@code cursorToObject(Cursor)}. The other methods handle the CRUD (create, read, update,
         * delete) functionality.
         *
         * @param project
         * @return ContentValues, that can be written into the database
         */
        static ContentValues objectToCV(Project project) {
            ContentValues values = new ContentValues();
            values.put(DBContract.TableProject.COLUMN_1, project.getName());
            values.put(DBContract.TableProject.COLUMN_2, String.valueOf(project.getCompleted()));
            return (values);
        }


    }

    /**
     * inner class for each table that enumerates its columns
     */
    public static abstract class TableTask implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_1 = "name";
        public static final String COLUMN_2 = "priority";
        public static final String COLUMN_3 = "taskproject";

        public static final String[] COLUMNS = {COLUMN_ID, COLUMN_1, COLUMN_2};
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                        COMMA_SEP + COLUMN_1 + TEXT_TYPE +
                        COMMA_SEP + COLUMN_2 + INT_TYPE +
                        COMMA_SEP + COLUMN_3 + TEXT_TYPE +
                        COMMA_SEP + "FOREIGN KEY (" + COLUMN_3 + ") REFERENCES " +
                        TableProject.TABLE_NAME + " (" + COLUMN_ID + ")" +
                        " )";
        public static final String TAG = "TaskDB.tag";

        /**
         * Method manages <b>read from</b> the database into a cursor. This is one of two methods that
         * are specific to the given data object (in this case, {@code Project}), the other being
         * {@code objectToCV(Task)}. The other methods handle the CRUD (create, read, update,
         * delete) functionality.
         *
         * @param cursor
         * @return a new data object (Task) from the database
         */
        static Task cursorToObject(Cursor cursor) {
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
         * {@code cursorToObject(Cursor)}. The other methods handle the CRUD (create, read, update,
         * delete) functionality.
         *
         * @param task
         * @return ContentValues, that can be written into the database
         */
        static ContentValues objectToCV(Task task) {
            ContentValues values = new ContentValues();
            values.put(DBContract.TableTask.COLUMN_1, task.getName());
            values.put(DBContract.TableTask.COLUMN_1, String.valueOf(task.getTaskProject()));
            return (values);
        }

    }

}
