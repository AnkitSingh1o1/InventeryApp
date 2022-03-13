package com.example.inventeryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {

    //Database Name
    private static final String DB_NAME = "inventory";

    //Database version
    private static final int DB_VERSION = 1;

    //Table Name
    private static final String  TABLE_NAME = "inventoryTable";

    //ID Column
    private static final String ID_COL = "id";

    //Item Name Column
    private static final String NAME_COL = "name";

    //Item Quantity Column
    private static final String QUANTITY_COL = "quantity";

    //Item Price Column
    private static final String PRICE_COL = "price";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create an SQLite Query
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + QUANTITY_COL + " TEXT,"
                + PRICE_COL + " TEXT)";

        sqLiteDatabase.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addNewItem(String itemName, int itemQuantity,
                           int itemPrice){
        //Creating a var of our SQLite database and calling writable
        //method so we can write with it in our database
        SQLiteDatabase db = this.getWritableDatabase();

        //Creating a var for content values
        ContentValues values = new ContentValues();

        //Passing all values along with its key
        values.put(NAME_COL, itemName);
        values.put(QUANTITY_COL, itemQuantity);
        values.put(PRICE_COL, itemPrice);

        //After adding all values we are passing content values
        //to our table
        db.insert(TABLE_NAME, null, values);

        db.close();

    }
}
