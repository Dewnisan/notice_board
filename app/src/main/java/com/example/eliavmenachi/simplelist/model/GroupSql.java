package com.example.eliavmenachi.simplelist.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class GroupSql {
    private final static String TABLE = "groups";
    private final static String TABLE_ID = "_id";
    private final static String TABLE_NAME = "name";
    private final static String TABLE_MEMBERS = "members";
    private final static String TABLE_IMAGE_NAME = "image_name";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE + " (" +
                TABLE_ID + " TEXT PRIMARY KEY," +
                TABLE_NAME + " TEXT," +
                TABLE_MEMBERS + " TEXT," +
                TABLE_IMAGE_NAME + " TEXT);");
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

            do {
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);

                String membersStr = cursor.getString(membersIndex);
                List<String> members = new LinkedList<String>();
                parseCollectionElements(membersStr, members);

                String imageName = cursor.getString(imageNameIndex);

                Group group = new Group(id, name, imageName);
                group.addMembers(members);

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

            String _id = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);

            String membersStr = cursor.getString(membersIndex);
            List<String> members = new LinkedList<String>();
            parseCollectionElements(membersStr, members);

            String imageName = cursor.getString(imageNameIndex);

            Group group = new Group(_id, name, imageName);
            group.addMembers(members);

            return group;
        }

        return null;
    }

    public static List<Group> getAllByUser(SQLiteDatabase db, String userId) {
        String selection = TABLE_MEMBERS + " LIKE ?";
        String[] args = {"%" + userId + "%"};
        Cursor cursor = db.query(TABLE, null, selection, args, null, null, null);

        List<Group> groups = new LinkedList<Group>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TABLE_ID);
            int nameIndex = cursor.getColumnIndex(TABLE_NAME);
            int membersIndex = cursor.getColumnIndex(TABLE_MEMBERS);
            int imageNameIndex = cursor.getColumnIndex(TABLE_IMAGE_NAME);

            do {
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);

                String membersStr = cursor.getString(membersIndex);
                List<String> members = new LinkedList<String>();
                parseCollectionElements(membersStr, members);

                String imageName = cursor.getString(imageNameIndex);

                Group group = new Group(id, name, imageName);
                group.addMembers(members);

                groups.add(group);
            } while (cursor.moveToNext());
        }

        return groups;
    }

    public static void add(SQLiteDatabase db, Group group) {
        ContentValues values = new ContentValues();

        values.put(TABLE_ID, group.getId());
        values.put(TABLE_NAME, group.getName());

        String membersStr = concatCollectionElements(new LinkedList(group.getMembers().values()));
        values.put(TABLE_MEMBERS, membersStr);

        values.put(TABLE_IMAGE_NAME, group.getImageName());

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
