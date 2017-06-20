package com.example.nubia.contacttest.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.nubia.contacttest.db.MyDatabaseHelper;

public class MyContentProvider extends ContentProvider {
    public static final int TABLE_DIR = 0;
    public static final int TABLE_ITEM = 1;
    public static final String AUTORITY = "com.example.nubia.contacttest.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTORITY, "contact", TABLE_DIR);
        uriMatcher.addURI(AUTORITY, "contact/#", TABLE_ITEM);
    }
    public MyContentProvider() {

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri)){
            case TABLE_DIR:
                deleteRows = db.delete("contact", selection, selectionArgs);
                break;
            case TABLE_ITEM:
                String contactId = uri.getPathSegments().get(1);
                deleteRows = db.delete("contact", "id = ?", new String[]{contactId});
                break;
            default:
                break;
        }
        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)){
            case TABLE_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.nubia.contacttest.provider.contact";
            case TABLE_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.nubia.contacttest.provider.contact";
            default:
                break;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case TABLE_DIR:
            case TABLE_ITEM:
                long newContactId = db.insert("contact", null, values);
                uriReturn = Uri.parse("content://" + AUTORITY + "/contact/" + newContactId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper = new MyDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        //查询数据
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case TABLE_DIR:
                cursor = db.query("contact", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TABLE_ITEM:
                String contactId = uri.getPathSegments().get(1);
                cursor = db.query("contact", projection, "id = ?", new String[]{contactId}, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)){
            case TABLE_DIR:
                updateRows = db.update("contact", values, selection, selectionArgs);
                break;
            case TABLE_ITEM:
                String contactId = uri.getPathSegments().get(1);
                updateRows = db.update("contact", values, "id = ?", new String[]{contactId});
                break;
            default:
                break;
        }
        return updateRows;
    }
}
