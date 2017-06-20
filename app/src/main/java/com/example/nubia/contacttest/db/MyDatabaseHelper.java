package com.example.nubia.contacttest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sun on 2017/6/12.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_CONTACT = "create table Contact ("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "number text)";
    private Context mContext;
    public MyDatabaseHelper(Context context){
        this(context, "ContactBook.db", null, 1);
    }
    private MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
