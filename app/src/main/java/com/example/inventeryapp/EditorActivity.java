package com.example.inventeryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.inventeryapp.data.DBContract.DBEntry;
import com.example.inventeryapp.data.DBHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditorActivity extends AppCompatActivity {

    //Creating variables
    private EditText itemName, itemQuantity, itemPrice;
    private FloatingActionButton fb2;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //Initializing all our variables
        itemName = findViewById(R.id.itemNameEdit);
        itemQuantity = findViewById(R.id.itemQuantityEdit);
        itemPrice = findViewById(R.id.itemPriceEdit);
        fb2 = findViewById(R.id.floatingActionButton2);

        //Creating a new dbHandler class
        //and passing our context to it
        dbHandler = new DBHandler(EditorActivity.this);

        //Set onClickListener to fb2
        fb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               insertPet();
            }
        });
    }

    /**
     * Get user input from editor and save new pet into database.
     */
    private void insertPet() {
        //Extract data from all text field
        String name = itemName.getText().toString();
        int quantity = Integer.parseInt(itemQuantity.getText().toString());
        int price = Integer.parseInt(itemPrice.getText().toString());
        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(DBEntry.COLUMN_ITEM_NAME, name);
        values.put(DBEntry.COLUMN_ITEM_QUANTITY, quantity);
        values.put(DBEntry.COLUMN_ITEM_PRICE, price);
        // Insert a new pet into the provider, returning the content URI for the new pet.
        Uri newUri = getContentResolver().insert(DBEntry.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }
}