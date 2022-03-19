package com.example.inventeryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.CursorLoader;

import com.example.inventeryapp.data.DBContract.DBEntry;
import com.example.inventeryapp.data.DBHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int INVENTORY_LOADER = 0;
    InventoryCursorAdapter mCursorAdapter;

    /** Database helper that will provide us access to the database */
    private DBHandler mDbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // DBHandler dbHandler = new DBHandler(getApplicationContext());
        //SQLiteDatabase db = dbHandler.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
      fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(MainActivity.this, EditorActivity.class);
              startActivity(intent);
          }
      });

        // Find the ListView which will be populated with the pet data
        ListView inventoryListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        inventoryListView.setEmptyView(emptyView);

        //Setup an adapter to create a list item for each row of data in the cursor
        //there is no pet data yet so pass in null for the cursor
        mCursorAdapter  = new InventoryCursorAdapter(this, null);
        inventoryListView.setAdapter(mCursorAdapter);

        //Setup item click listener
        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(DBEntry.CONTENT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });

        //Kick off the loader
        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);


    }

//------------------------------------------<MENU>--------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                //Insert Dummy Data
                insertDummyItem();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                //Delete all pets
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Helper method to delete all pets in the database.
     */
    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(DBEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from Inventory database");
    }
//----------------------------------------</MENU>---------------------------------------------------

    private void insertDummyItem(){
        ContentValues values = new ContentValues();
        values.put(DBEntry.COLUMN_ITEM_NAME, "DUMMY");
        values.put(DBEntry.COLUMN_ITEM_QUANTITY, 1);
        values.put(DBEntry.COLUMN_ITEM_PRICE, 1);

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(DBEntry.CONTENT_URI, values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DBEntry._ID,
                DBEntry.COLUMN_ITEM_NAME,
                DBEntry.COLUMN_ITEM_QUANTITY,
                DBEntry.COLUMN_ITEM_PRICE
        };
        return new CursorLoader(this,
                DBEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished( Loader<Cursor> loader, Cursor data) {
        //update with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset( Loader<Cursor> loader) {
        //Callbacks called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}