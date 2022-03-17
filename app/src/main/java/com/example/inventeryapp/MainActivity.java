package com.example.inventeryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.service.controls.actions.FloatAction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHandler = new DBHandler(this);

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
        SQLiteDatabase db = mDbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBEntry.COLUMN_ITEM_NAME, "DUMMY");
        values.put(DBEntry.COLUMN_ITEM_QUANTITY, 1);
        values.put(DBEntry.COLUMN_ITEM_PRICE, 1);

        long newRowId = db.insert(DBEntry.TABLE_NAME, null, values);

        if(newRowId != -1) {
            //Displaying a Confirmation Toast Message
            Toast.makeText(MainActivity.this, "Added Successfully.",
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "Adding Unsuccessful.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}