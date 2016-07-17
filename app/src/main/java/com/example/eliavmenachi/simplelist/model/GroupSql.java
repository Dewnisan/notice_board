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
    final static String TABLE_OWNER = "owner";
    final static String TABLE_MEMBERS = "members";
    final static String TABLE_IMAGE_NAME = "image_name";
    //final static String TABLE_DELETED = "deleted";

    static public void create(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE + " (" +
                TABLE_ID + " TEXT PRIMARY KEY," +
                TABLE_NAME + " TEXT," +
                TABLE_OWNER + " TEXT," +
                TABLE_MEMBERS + " TEXT," +
                TABLE_IMAGE_NAME + " TEXT);");
    }

    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + TABLE + ";");
    }

    public static List<Group> getAllGroups(SQLiteDatabase db) {
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        List<Group> groups = new LinkedList<Group>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TABLE_ID);
            int nameIndex = cursor.getColumnIndex(TABLE_NAME);
            int ownerIndex = cursor.getColumnIndex(TABLE_OWNER);
            int membersIndex = cursor.getColumnIndex(TABLE_MEMBERS);
            int imageNameIndex = cursor.getColumnIndex(TABLE_IMAGE_NAME);

            do {
                String id = cursor.getString(idIndex);
                String name = cursor.getString(nameIndex);
                String owner = cursor.getString(ownerIndex);

                String membersStr = cursor.getString(membersIndex);
                List<String> members = new LinkedList<String>();
                parseMembers(membersStr, members);

                String imageName = cursor.getString(imageNameIndex);

                Group group = new Group(id, name, owner, members, imageName);
                groups.add(group);
            } while (cursor.moveToNext());
        }
        return groups;
    }

    public static Group getGroupById(SQLiteDatabase db, String id) {
        String where = TABLE_ID + " = ?";
        String[] args = {id};
        Cursor cursor = db.query(TABLE, null, where, args, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TABLE_ID);
            int nameIndex = cursor.getColumnIndex(TABLE_NAME);
            int ownerIndex = cursor.getColumnIndex(TABLE_OWNER);
            int membersIndex = cursor.getColumnIndex(TABLE_MEMBERS);
            int imageNameIndex = cursor.getColumnIndex(TABLE_IMAGE_NAME);

            String _id = cursor.getString(idIndex);
            String name = cursor.getString(nameIndex);
            String owner = cursor.getString(ownerIndex);

            String membersStr = cursor.getString(membersIndex);
            List<String> members = new LinkedList<String>();
            parseMembers(membersStr, members);

            String imageName = cursor.getString(imageNameIndex);

            Group group = new Group(_id, name, owner, members, imageName);

            return group;
        }

        return null;
    }

    public static void add(SQLiteDatabase db, Group group) {
        ContentValues values = new ContentValues();

        values.put(TABLE_ID, group.getId());
        values.put(TABLE_NAME, group.getName());
        values.put(TABLE_OWNER, group.getOwner());

        String membersStr = concatMembers(group.getMembers());
        values.put(TABLE_MEMBERS, membersStr);

        values.put(TABLE_IMAGE_NAME, group.getImageName());

        db.insertWithOnConflict(TABLE, TABLE_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static String getLastUpdateDate(SQLiteDatabase db) {
        return LastUpdateSql.getLastUpdate(db, TABLE);
    }

    public static void setLastUpdateDate(SQLiteDatabase db, String date) {
        LastUpdateSql.setLastUpdate(db, TABLE, date);
    }

    private static String concatMembers(Collection<String> members) {
        StringBuilder result = new StringBuilder();

        String sep = ",";
        for (String member : members) {
            result.append(sep).append(member);
        }

        return result.toString();
    }

    private static void parseMembers(String membersStr, Collection<String> output) {
        String[] members = membersStr.split(",");
        for (String member : members) {
            output.add(member);
        }
    }
}
