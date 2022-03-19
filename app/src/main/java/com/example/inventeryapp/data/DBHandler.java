package com.example.inventeryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.inventeryapp.data.DBContract.DBEntry;

public class DBHandler extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Inventory.db";

    //Create an SQLite Query
//    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DBEntry.TABLE_NAME + " ("
//            + DBEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//            + DBEntry.COLUMN_ITEM_NAME + " TEXT,"
//            + DBEntry.COLUMN_ITEM_QUANTITY + " TEXT,"
//            + DBEntry.COLUMN_ITEM_PRICE + " TEXT)";
    //Delete Query
//    private static final String SQL_DELETE_ENTRIES =
//            "DROP TABLE IF EXISTS " + DBEntry.TABLE_NAME;

    //Constructor
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_INVENTORY_TABLE =  "CREATE TABLE " + DBEntry.TABLE_NAME + " ("
                + DBEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DBEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + DBEntry.COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL, "
                + DBEntry.COLUMN_ITEM_PRICE + " INTEGER NOT NULL);";
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
    }
}
