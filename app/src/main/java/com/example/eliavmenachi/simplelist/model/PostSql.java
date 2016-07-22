package com.example.eliavmenachi.simplelist.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by eliav.menachi on 08/06/2016.
 */
public class PostSql {
    final static String TABLE = "posts";
    final static String TABLE_ID = "_id";
    final static String TABLE_OWNER = "owner";
    final static String TABLE_GROUP = "_group";
    final static String TABLE_MESSAGE = "message";
    final static String TABLE_IMAGE_NAME = "image_name";
    //final static String TABLE_DELETED = "deleted";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE + " (" +
                TABLE_ID + " TEXT PRIMARY KEY," +
                TABLE_OWNER + " TEXT," +
                TABLE_GROUP + " TEXT," +
                TABLE_MESSAGE + " TEXT," +
                TABLE_IMAGE_NAME + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TABLE + ";");
    }

    public static List<Post> getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        List<Post> posts = new LinkedList<Post>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TABLE_ID);
            int ownerIndex = cursor.getColumnIndex(TABLE_OWNER);
            int groupIndex = cursor.getColumnIndex(TABLE_GROUP);
            int messageIndex = cursor.getColumnIndex(TABLE_MESSAGE);
            int imageNameIndex = cursor.getColumnIndex(TABLE_IMAGE_NAME);

            do {
                String id = cursor.getString(idIndex);
                String owner = cursor.getString(ownerIndex);
                String group = cursor.getString(groupIndex);
                String message = cursor.getString(messageIndex);
                String imageName = cursor.getString(imageNameIndex);

                Post post = new Post(id, owner, group, message, imageName);
                posts.add(post);
            } while (cursor.moveToNext());
        }

        return posts;
    }

    public static Post getById(SQLiteDatabase db, String id) {
        String where = TABLE_ID + " = ?";
        String[] args = {id};
        Cursor cursor = db.query(TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TABLE_ID);
            int ownerIndex = cursor.getColumnIndex(TABLE_OWNER);
            int groupIndex = cursor.getColumnIndex(TABLE_GROUP);
            int messageIndex = cursor.getColumnIndex(TABLE_MESSAGE);
            int imageNameIndex = cursor.getColumnIndex(TABLE_IMAGE_NAME);

            String _id = cursor.getString(idIndex);
            String owner = cursor.getString(ownerIndex);
            String group = cursor.getString(groupIndex);
            String message = cursor.getString(messageIndex);
            String imageName = cursor.getString(imageNameIndex);

            Post post = new Post(_id, owner, group, message, imageName);

            return post;
        }

        return null;
    }

    public static void add(SQLiteDatabase db, Post post) {
        ContentValues values = new ContentValues();

        values.put(TABLE_ID, post.getId());
        values.put(TABLE_OWNER, post.getOwner());
        values.put(TABLE_GROUP, post.getGroup());
        values.put(TABLE_MESSAGE, post.getMessage());
        values.put(TABLE_IMAGE_NAME, post.getImageName());

        db.insertWithOnConflict(TABLE, TABLE_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static String getLastUpdateDate(SQLiteDatabase db) {
        return LastUpdateSql.getLastUpdate(db, TABLE);
    }

    public static void setLastUpdateDate(SQLiteDatabase db, String date) {
        LastUpdateSql.setLastUpdate(db, TABLE, date);
    }
}
