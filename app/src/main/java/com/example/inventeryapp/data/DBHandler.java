package com.example.inventeryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.inventeryapp.data.DBContract.DBEntry;
import androidx.annotation.Nullable;

import java.util.Locale;

public class DBHandler extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Inventory.db";

    //Create an SQLite Query
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + DBEntry.TABLE_NAME + " ("
            + DBEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBEntry.COLUMN_ITEM_NAME + " TEXT,"
            + DBEntry.COLUMN_ITEM_QUANTITY + " TEXT,"
            + DBEntry.COLUMN_ITEM_PRICE + " TEXT)";
    //Delete Query
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBEntry.TABLE_NAME;

    //Constructor
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // this method is called to check if the table exists already.
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public long addNewItem(String itemName, int itemQuantity,
                           int itemPrice){
        //Creating a var of our SQLite database and calling writable
        //method so we can write with it in our database
        SQLiteDatabase db = this.getWritableDatabase();

        //Creating a var for content values
        ContentValues values = new ContentValues();

        //Passing all values along with its key
        values.put(DBEntry.COLUMN_ITEM_NAME, itemName);
        values.put(DBEntry.COLUMN_ITEM_QUANTITY, itemQuantity);
        values.put(DBEntry.COLUMN_ITEM_PRICE, itemPrice);

        //After adding all values we are passing content values
        //to our table
        long idx = db.insert(DBEntry.TABLE_NAME, null, values);

        db.close();

        return idx;
    }

}
