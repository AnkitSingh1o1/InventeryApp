package com.example.inventeryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.inventeryapp.data.DBContract.DBEntry;


public class DBContentProvider extends ContentProvider {
    /** Tag for the log messages */
    public static final String LOG_TAG = DBContentProvider.class.getSimpleName();

//------------------------------------------<URI Matcher>-------------------------------------------
    private static final int INVENTORY = 100;
    private static final int INVENTORY_ID = 101;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(DBContract.CONTENT_AUTHORITY, DBContract.PATH_INVENTORY, INVENTORY);
        sURIMatcher.addURI(DBContract.CONTENT_AUTHORITY, DBContract.PATH_INVENTORY+"/#", INVENTORY_ID);
    }
//----------------------------------------</URI Matcher>--------------------------------------------

    private DBHandler mDBHandler;
    @Override
    public boolean onCreate() {
        mDBHandler = new DBHandler(getContext());
        return true;
    }

//------------------------------------------<QUERY>-------------------------------------------------
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDBHandler.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sURIMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(DBEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case INVENTORY_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = DBEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(DBEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
//--------------------------------------------</QUERY>----------------------------------------------

//-------------------------------------------<INSERT>-----------------------------------------------
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sURIMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPet(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(DBEntry.COLUMN_ITEM_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }
        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer quantity = values.getAsInteger(DBEntry.COLUMN_ITEM_QUANTITY);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }
        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer price = values.getAsInteger(DBEntry.COLUMN_ITEM_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }


        // Get writeable database
        SQLiteDatabase database = mDBHandler.getWritableDatabase();
        // Insert the new pet with the given values
        long id = database.insert(DBEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        // Notify all listeners that the data has changed for the pet content URI
        getContext().getContentResolver().notifyChange(uri, null);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }
//------------------------------------------</INSERT>-----------------------------------------------


//----------------------------------------<UPDATE>--------------------------------------------------
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sURIMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case INVENTORY_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = DBEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // check that the name value is not null.
        if (values.containsKey(DBEntry.COLUMN_ITEM_NAME)) {
            String name = values.getAsString(DBEntry.COLUMN_ITEM_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Item requires a name");
            }
        }
        // check that the quantity value is valid.
        if (values.containsKey(DBEntry.COLUMN_ITEM_QUANTITY)) {
            Integer quantity = values.getAsInteger(DBEntry.COLUMN_ITEM_QUANTITY);
            if (quantity == null) {
                throw new IllegalArgumentException("Item requires valid quantity");
            }
        }
        // check that the price value is valid.
        if (values.containsKey(DBEntry.COLUMN_ITEM_PRICE)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer price = values.getAsInteger(DBEntry.COLUMN_ITEM_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Item requires valid price");
            }
        }
        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        } // Otherwise, get writeable database to update the data

        SQLiteDatabase database = mDBHandler.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(DBEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows updated
        return rowsUpdated;
    }
//-------------------------------------</UPDATE>----------------------------------------------------

//-------------------------------------<DELETE>-----------------------------------------------------
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDBHandler.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sURIMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                rowsDeleted = database.delete(DBEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case INVENTORY_ID:
                // Delete a single row given by the ID in the URI
                selection = DBEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(DBEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }
//-----------------------------------------</DELETE>------------------------------------------------

    @Override
    public String getType(Uri uri) {
        final int match = sURIMatcher.match(uri);
        switch (match) {
            case INVENTORY:
                return DBEntry.CONTENT_LIST_TYPE;
            case INVENTORY_ID:
                return DBEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
