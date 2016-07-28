package com.example.eliavmenachi.simplelist.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by eliav.menachi on 08/06/2016.
 */
public class UserSql {
    private final static String TABLE = "users";
    private final static String TABLE_ID = "_id";
    private final static String TABLE_NAME = "name";
    private final static String TABLE_IMAGE_NAME = "image_name";
    //final static String TABLE_DELETED = "deleted";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE + " (" +
                TABLE_ID + " TEXT PRIMARY KEY," +
                TABLE_NAME + " TEXT," +
                TABLE_IMAGE_NAME + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TABLE + ";");
    }

    public static User getById(SQLiteDatabase db, String id) {
        String where = TABLE_ID + " = ?";
        String[] args = {id};
        Cursor cursor = db.query(TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TABLE_ID);
            int nameIndex = cursor.getColumnIndex(TABLE_NAME);
            int imageNameIndex = cursor.getColumnIndex(TABLE_IMAGE_NAME);

            String _id = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);
            String imageName = cursor.getString(imageNameIndex);

            User user = new User(_id, name, imageName);

            return user;
        }

        return null;
    }

    public static User getByName(SQLiteDatabase db, String name) {
        String where = TABLE_NAME + " = ?";
        String[] args = {name};
        Cursor cursor = db.query(TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TABLE_ID);
            int nameIndex = cursor.getColumnIndex(TABLE_NAME);
            int imageNameIndex = cursor.getColumnIndex(TABLE_IMAGE_NAME);

            String id = cursor.getString(idIndex);
            String _name = cursor.getString(nameIndex);
            String imageName = cursor.getString(imageNameIndex);

            User user = new User(id, _name, imageName);

            return user;
        }

        return null;
    }

    public static List<User> getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        List<User> users = new LinkedList<User>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TABLE_ID);
            int nameIndex = cursor.getColumnIndex(TABLE_NAME);
            int imageNameIndex = cursor.getColumnIndex(TABLE_IMAGE_NAME);

            do {
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                String imageName = cursor.getString(imageNameIndex);

                User user = new User(id, name, imageName);
                users.add(user);
            } while (cursor.moveToNext());
        }

        return users;
    }

    public static void add(SQLiteDatabase db, User user) {
        ContentValues values = new ContentValues();

        values.put(TABLE_ID, user.getId());
        values.put(TABLE_NAME, user.getName());
        values.put(TABLE_IMAGE_NAME, user.getImageName());

        db.insertWithOnConflict(TABLE, TABLE_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void remove(SQLiteDatabase db, String id) {
        db.delete(TABLE, TABLE_ID + " = ?", new String[]{id});
    }

    public static String getLastUpdateDate(SQLiteDatabase db) {
        return LastUpdateSql.getLastUpdate(db, TABLE);
    }

    public static void setLastUpdateDate(SQLiteDatabase db, String date) {
        LastUpdateSql.setLastUpdate(db, TABLE, date);
    }
}
