package com.koenbro.android.dbtest;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database adapter that uses an inner class to create (if needed) or upgrade the database. This is
 * inspired by the answer to
 * <a href="http://stackoverflow.com/questions/4063510/multiple-table-sqlite-db-adapters-in-android">
 *     this question</a> on Stack Overflow and a <a
 *     href="http://stackoverflow.com/questions/3684678/best-practices-of-working-with-multiple-sqlite-db-tables-in-android">
 *     related question</a>, tackling "best practices."
 */
public class DBAdapter {
    private DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.DBHelper = new DatabaseHelper(ctx);
    }
    /**
     * This inner class creates the database if it is not already available and manages the
     * upgrade should the schema change. Supposedly only runs once in the beginning, and then only
     * runs again when upgraded.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper{
        DatabaseHelper(Context context){
            super(context, DBContract.DB_NAME, null, DBContract.DB_VERSION);
        }
        @Override
        /**
         * As new tables are added to the database, they need to be registered in this method, and the
         * version number needs to be incremented in {@link com.koenbro.android.dbtest.DBContract}
         */
        public void onCreate(SQLiteDatabase db){
            db.execSQL(DBContract.TableProject.CREATE_TABLE);
            db.execSQL(DBContract.TableTask.CREATE_TABLE);
        }
        @Override
        /**
         * As new tables are added to the database, they need to be registered in this method, and the
         * version number needs to be incremented in {@link com.koenbro.android.dbtest.DBContract}
         */
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            Log.w(DatabaseHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL(DBContract.TableProject.DELETE_TABLE);
            db.execSQL(DBContract.TableTask.DELETE_TABLE);
            onCreate(db);
        }
    }

    public  DBAdapter open() throws SQLException{
        this.db = this.DBHelper.getWritableDatabase();
        return this;
    }
    public void close(){
        this.DBHelper.close();
    }
}