package com.example.spencer_zachariah_cs360_project.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class for managing application SQLite database.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    // User table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Item table
    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ITEM_ID = "id";
    public static final String COLUMN_ITEM_NAME = "name";
    public static final String COLUMN_ITEM_QTY = "quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        String createItems = "CREATE TABLE " + TABLE_ITEMS + " (" +
                COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM_NAME + " TEXT, " +
                COLUMN_ITEM_QTY + " INTEGER)";
        db.execSQL(createUsers);
        db.execSQL(createItems);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    /**
     * Add user credentials to the database.
     */
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long id = db.insert(TABLE_USERS, null, values);
        return id != -1;
    }

    /**
     * Verify username and password.
     */
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    /**
     * Insert an item.
     */
    public long addItem(String name, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, name);
        values.put(COLUMN_ITEM_QTY, quantity);
        return db.insert(TABLE_ITEMS, null, values);
    }

    /**
     * Retrieve all items.
     */
    public Cursor getAllItems() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_ITEMS, null, null, null, null, null, null);
    }

    /**
     * Update item quantity.
     */
    public boolean updateItem(int id, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_QTY, quantity);
        int rows = db.update(TABLE_ITEMS, values, COLUMN_ITEM_ID + "=?",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }

    /**
     * Delete item.
     */
    public boolean deleteItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_ITEMS, COLUMN_ITEM_ID + "=?",
                new String[]{String.valueOf(id)});
        return rows > 0;
    }
}
