package com.example.eliavmenachi.simplelist.model;

/**
 * Created by eliav.menachi on 08/06/2016.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by eliav.menachi on 13/05/2015.
 */
public class ModelSql {
    private final static int VERSION = 6;

    private Helper mSqlDb;

    public ModelSql(Context context) {
        mSqlDb = new Helper(context);
    }

    public SQLiteDatabase getWritableDB() {
        return mSqlDb.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDB() {
        return mSqlDb.getReadableDatabase();
    }

    class Helper extends SQLiteOpenHelper {
        public Helper(Context context) {
            super(context, "database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            UserSql.create(db);
            GroupSql.create(db);
            PostSql.create(db);
            LastUpdateSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            UserSql.drop(db);
            GroupSql.drop(db);
            PostSql.drop(db);
            LastUpdateSql.drop(db);
            onCreate(db);
        }
    }
}
