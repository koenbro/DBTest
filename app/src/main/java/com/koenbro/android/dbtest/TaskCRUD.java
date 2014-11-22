package com.koenbro.android.dbtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;

/**
 * This class manages the CRUD (<b>create-read-update-delete</b>) into <b>one table</b> of the database.
 */
public class TaskCRUD extends DBAdapter {
    public TaskCRUD(Context ctx) {
        super(ctx);
    }

    public void addTask(Task task) {
        ContentValues values = DBContract.TableTask.objectToCV(task);
        this.db.insert(DBContract.TableTask.TABLE_NAME, null, values);
    }

    public Task getTask(long id) throws SQLException {
        Cursor cursor = this.db.query(
                DBContract.TableTask.TABLE_NAME,
                DBContract.TableTask.COLUMNS,
                " id = ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor != null) cursor.moveToFirst();
        Task task = DBContract.TableTask.cursorToObject(cursor);
        assert cursor != null;
        cursor.close();
        return (task);
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        String query = "SELECT * FROM " + DBContract.TableTask.TABLE_NAME;
        Cursor cursor = this.db.rawQuery(query, null);
        Task task;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    task = DBContract.TableTask.cursorToObject(cursor);
                    tasks.add(task);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return (tasks);
    }

    public boolean updateTask(Task task) {
        ContentValues values = DBContract.TableTask.objectToCV(task);
        return this.db.update(DBContract.TableTask.TABLE_NAME,
                values,
                DBContract.TableTask.COLUMN_ID + "=?",
                new String[]{String.valueOf(task.getId())}) > 0;
    }

    public boolean deleteTask(Task task) {
        return this.db.delete(DBContract.TableTask.TABLE_NAME,
                DBContract.TableTask.COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())}) > 0;
    }
}