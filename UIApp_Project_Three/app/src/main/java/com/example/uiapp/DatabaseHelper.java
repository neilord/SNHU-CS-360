package com.example.uiapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Central SQLite helper. Handles user accounts and inventory items.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "ui_app.db";
    private static final int    DB_VERSION = 1;

    /* ----------  table & column names  ---------- */
    public static final String T_USER  = "users";
    public static final String T_ITEM  = "inventory";

    public static final String C_ID       = "_id";
    public static final String C_USERNAME = "username";
    public static final String C_PASSWORD = "password";

    public static final String C_TITLE   = "title";
    public static final String C_DETAILS = "details";
    public static final String C_QTY     = "quantity";

    private static final String CREATE_USER =
            "CREATE TABLE " + T_USER + " (" +
                    C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    C_USERNAME + " TEXT UNIQUE," +
                    C_PASSWORD + " TEXT)";

    private static final String CREATE_ITEM =
            "CREATE TABLE " + T_ITEM + " (" +
                    C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    C_TITLE   + " TEXT," +
                    C_DETAILS + " TEXT," +
                    C_QTY     + " INTEGER)";

    public DatabaseHelper(@Nullable Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_ITEM);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + T_USER);
        db.execSQL("DROP TABLE IF EXISTS " + T_ITEM);
        onCreate(db);
    }

    /* ----------  USER helpers  ---------- */

    public boolean register(String user, String pass) {
        ContentValues cv = new ContentValues();
        cv.put(C_USERNAME, user);
        cv.put(C_PASSWORD, pass);
        return getWritableDatabase().insert(T_USER, null, cv) != -1;
    }

    public boolean login(String user, String pass) {
        Cursor c = getReadableDatabase().query(
                T_USER, new String[]{C_ID},
                C_USERNAME + "=? AND " + C_PASSWORD + "=?",
                new String[]{user, pass}, null, null, null);
        boolean ok = c.moveToFirst();
        c.close();
        return ok;
    }

    /* ----------  INVENTORY helpers  ---------- */

    public long addItem(String title, String details, int qty) {
        ContentValues cv = new ContentValues();
        cv.put(C_TITLE, title);
        cv.put(C_DETAILS, details);
        cv.put(C_QTY, qty);
        return getWritableDatabase().insert(T_ITEM, null, cv);
    }

    public int deleteItem(long id) {
        return getWritableDatabase().delete(T_ITEM, C_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int updateItem(long id, String title, String details, int qty) {
        ContentValues cv = new ContentValues();
        cv.put(C_TITLE, title);
        cv.put(C_DETAILS, details);
        cv.put(C_QTY, qty);
        return getWritableDatabase().update(T_ITEM, cv, C_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Cursor getAllItems() {
        return getReadableDatabase().query(
                T_ITEM, null, null, null, null, null, C_TITLE + " ASC");
    }

    public boolean isLowInventory() {
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT 1 FROM " + T_ITEM + " WHERE " + C_QTY + " <= 3 LIMIT 1", null);
        boolean low = c.moveToFirst();
        c.close();
        return low;
    }
}