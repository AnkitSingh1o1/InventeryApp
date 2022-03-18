package com.example.inventeryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.service.controls.actions.FloatAction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.inventeryapp.data.DBContract.DBEntry;
import com.example.inventeryapp.data.DBHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


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
        ListView petListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHandler = new DBHandler(this);

        displayDatabaseInfo();

    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHandler.getReadableDatabase();

        String[] projection = {
                DBEntry._ID,
                DBEntry.COLUMN_ITEM_NAME,
                DBEntry.COLUMN_ITEM_QUANTITY,
                DBEntry.COLUMN_ITEM_PRICE};


        Cursor cursor = getContentResolver().query(DBEntry.CONTENT_URI, projection, null,
                null, null);

        // Find the ListView which will be populated with the pet data
        ListView petListView = (ListView) findViewById(R.id.list);

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        InventoryCursorAdapter adapter = new InventoryCursorAdapter(this, cursor);
        // Attach the adapter to the ListView.
        petListView.setAdapter(adapter);
    }

//------------------------------------------<MENU>----------------------------------------------------
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//----------------------------------------</MENU>-----------------------------------------------------

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
}