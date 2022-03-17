package com.example.inventeryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



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
                cursor = database.query(DBContract.DBEntry.TABLE_NAME, projection, selection, selectionArgs,
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
                selection = DBContract.DBEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(DBContract.DBEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }
//--------------------------------------------</QUERY>----------------------------------------------
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

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
        // Get writeable database
        SQLiteDatabase database = mDBHandler.getWritableDatabase();
        // Insert the new pet with the given values
        long id = database.insert(DBContract.DBEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }
//------------------------------------------</INSERT>-----------------------------------------------

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
