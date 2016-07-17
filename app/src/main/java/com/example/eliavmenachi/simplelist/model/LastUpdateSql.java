package com.example.eliavmenachi.simplelist.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by eliav.menachi on 08/06/2016.
 */
public class LastUpdateSql {
    final static String TABLE = "last_update";
    final static String TABLE_NAME = "table_name";
    final static String TABLE_DATE = "date";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE + " (" +
                TABLE_NAME + " TEXT PRIMARY KEY," +
                TABLE_DATE + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TABLE + ";");
    }

    public static String getLastUpdate(SQLiteDatabase db, String tableName) {
        String[] args = {tableName};
        Cursor cursor = db.query(TABLE, null, TABLE_NAME + " = ?", args, null, null, null);

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(TABLE_DATE));
        }
        return null;
    }

    public static void setLastUpdate(SQLiteDatabase db, String table, String date) {
        ContentValues values = new ContentValues();
        values.put(TABLE_NAME, table);
        values.put(TABLE_DATE, date);

        db.insertWithOnConflict(TABLE, TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
