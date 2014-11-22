package com.koenbro.android.dbtest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;

/**
 * This class manages the CRUD (<b>create-read-update-delete</b>) into <b>one table</b> of the database.
 */
public class ProjectCRUD extends DBAdapter {
    public ProjectCRUD(Context ctx) {
        super(ctx);
    }

    public void addRecord(Project project) {
        ContentValues values = DBContract.TableProject.objectToCV(project);
        this.db.insert(DBContract.TableProject.TABLE_NAME, null, values);
    }

    public Project getRecord(long id) throws SQLException {
        Cursor cursor = this.db.query(
                DBContract.TableProject.TABLE_NAME,
                DBContract.TableProject.COLUMNS,
                " id = ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor != null) cursor.moveToFirst();
        Project project = DBContract.TableProject.cursorToObject(cursor);
        assert cursor != null;
        cursor.close();
        return (project);
    }

    public ArrayList<Project> getAllRecords() {
        ArrayList<Project> projects = new ArrayList<Project>();
        String query = "SELECT * FROM " + DBContract.TableProject.TABLE_NAME;
        Cursor cursor = super.db.rawQuery(query, null);
        Project project;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    project = DBContract.TableProject.cursorToObject(cursor);
                    projects.add(project);
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
        return (projects);
    }

    public boolean updateRecord(Project project) {
        ContentValues values = DBContract.TableProject.objectToCV(project);
        return this.db.update(DBContract.TableProject.TABLE_NAME,
                values,
                DBContract.TableProject.COLUMN_ID + "=?",
                new String[]{String.valueOf(project.getId())}) > 0;
    }

    public boolean deleteRecord(Project project) {
        return this.db.delete(DBContract.TableProject.TABLE_NAME,
                DBContract.TableProject.COLUMN_ID + " = ?",
                new String[]{String.valueOf(project.getId())}) > 0;
    }
}