package com.koenbro.android.dbtest;

import android.provider.BaseColumns;

/**
 * The contract class explicitly specifies the layout of the db schema in a systematic and self-
 * documenting way. Will be called by {@link com.koenbro.android.dbtest.DBAdapter#DBHelper
 * DBAdapter.DatabaseHelper} which extends SQLiteHelper. In addition, each table will have its own DBAdapter,
 * that will likewise contain a DBHelper; see, for example
 * {@link com.koenbro.android.dbtest.ProjectDBAdapter#mDbHelper}. The question is: can these
 * DBHelpers be refactored?
 * @see com.koenbro.android.dbtest.DBAdapter#DBHelper
 * @see com.koenbro.android.dbtest.ProjectDBAdapter#mDbHelper
 * @see com.koenbro.android.dbtest.TaskDBAdapter#mDbHelper
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
    private DBContract() {}

    /**
     * inner class for each table that enumerates its columns
     */
    public static abstract class TableProject implements BaseColumns{
        public static final String TABLE_NAME = "projects";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_1 = "name";
        public static final String COLUMN_2 = "completed ";

        public static final String[] COLUMNS = {COLUMN_ID, COLUMN_1, COLUMN_2};
        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                        COMMA_SEP + COLUMN_1 + TEXT_TYPE +
                        COMMA_SEP + COLUMN_2 + INT_TYPE +
                        " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static final String TAG = "ProjectDB.tag";
    }

    /**
     * inner class for each table that enumerates its columns
     */
    public static abstract class TableTask implements  BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_1 = "name";
        public static final String COLUMN_2 = "priority";
        public static final String COLUMN_3 = "taskproject";

        public static final String[] COLUMNS = {COLUMN_ID, COLUMN_1, COLUMN_2};
        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
                        COMMA_SEP + COLUMN_1 + TEXT_TYPE +
                        COMMA_SEP + COLUMN_2 + INT_TYPE +
                        COMMA_SEP + COLUMN_3 + TEXT_TYPE +
                        COMMA_SEP + "FOREIGN KEY (" + COLUMN_3 + ") REFERENCES " +
                        TableProject.TABLE_NAME + " ("+ COLUMN_ID + ")" +
                        " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static final String TAG = "TaskDB.tag";
    }

}
