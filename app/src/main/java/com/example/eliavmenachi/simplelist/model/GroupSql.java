package com.example.eliavmenachi.simplelist.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by eliav.menachi on 08/06/2016.
 */
public class GroupSql {
    final static String TABLE = "groups";
    final static String TABLE_ID = "_id";
    final static String TABLE_NAME = "name";
    final static String TABLE_MEMBERS = "members";
    final static String TABLE_IMAGE_NAME = "image_name";
    final static String TABLE_MESSAGES = "messages";
    //final static String TABLE_DELETED = "deleted";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE + " (" +
                TABLE_ID + " TEXT PRIMARY KEY," +
                TABLE_NAME + " TEXT," +
                TABLE_MEMBERS + " TEXT," +
                TABLE_IMAGE_NAME + " TEXT," +
                TABLE_MESSAGES + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TABLE + ";");
    }

    public static List<Group> getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        List<Group> groups = new LinkedList<Group>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TABLE_ID);
            int nameIndex = cursor.getColumnIndex(TABLE_NAME);
            int membersIndex = cursor.getColumnIndex(TABLE_MEMBERS);
            int imageNameIndex = cursor.getColumnIndex(TABLE_IMAGE_NAME);
            int messagesIndex = cursor.getColumnIndex(TABLE_MESSAGES);

            do {
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);

                String membersStr = cursor.getString(membersIndex);
                List<String> members = new LinkedList<String>();
                parseCollectionElements(membersStr, members);

                String imageName = cursor.getString(imageNameIndex);

                String messagesStr = cursor.getString(messagesIndex);
                List<String> messages = new LinkedList<String>();
                parseCollectionElements(messagesStr, messages);

                Group group = new Group(id, name, members, imageName, messages);
                groups.add(group);
            } while (cursor.moveToNext());
        }
        return groups;
    }

    public static Group getById(SQLiteDatabase db, String id) {
        String where = TABLE_ID + " = ?";
        String[] args = {id};
        Cursor cursor = db.query(TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TABLE_ID);
            int nameIndex = cursor.getColumnIndex(TABLE_NAME);
            int membersIndex = cursor.getColumnIndex(TABLE_MEMBERS);
            int imageNameIndex = cursor.getColumnIndex(TABLE_IMAGE_NAME);
            int messagesIndex = cursor.getColumnIndex(TABLE_MESSAGES);

            String _id = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);

            String membersStr = cursor.getString(membersIndex);
            List<String> members = new LinkedList<String>();
            parseCollectionElements(membersStr, members);

            String imageName = cursor.getString(imageNameIndex);

            String messagesStr = cursor.getString(messagesIndex);
            List<String> messages = new LinkedList<String>();
            parseCollectionElements(messagesStr, messages);

            Group group = new Group(_id, name, members, imageName, messages);

            return group;
        }

        return null;
    }

    public static void add(SQLiteDatabase db, Group group) {
        ContentValues values = new ContentValues();

        values.put(TABLE_ID, group.getId());
        values.put(TABLE_NAME, group.getName());

        String membersStr = concatCollectionElements(group.getMembers());
        values.put(TABLE_MEMBERS, membersStr);

        values.put(TABLE_IMAGE_NAME, group.getImageName());

        String messagesStr = concatCollectionElements(group.getMessages());
        values.put(TABLE_MESSAGES, messagesStr);

        db.insertWithOnConflict(TABLE, TABLE_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static String getLastUpdateDate(SQLiteDatabase db) {
        return LastUpdateSql.getLastUpdate(db, TABLE);
    }

    public static void setLastUpdateDate(SQLiteDatabase db, String date) {
        LastUpdateSql.setLastUpdate(db, TABLE, date);
    }

    private static String concatCollectionElements(Collection<String> elements) {
        StringBuilder result = new StringBuilder();

        String sep = ",";
        for (String element : elements) {
            result.append(sep).append(element);
        }

        return result.toString();
    }

    private static void parseCollectionElements(String elementsStr, Collection<String> output) {
        String[] elements = elementsStr.split(",");
        for (String element : elements) {
            output.add(element);
        }
    }
}
