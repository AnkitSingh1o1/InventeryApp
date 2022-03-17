package com.example.inventeryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
                //Extract data from all text field
                String name = itemName.getText().toString();
                int quantity = Integer.parseInt(itemQuantity.getText().toString());
                int price = Integer.parseInt(itemPrice.getText().toString());

                //Calling a self-declared method to add data to our database
                long index = dbHandler.addNewItem(name, quantity, price);

                if(index != -1) {
                    //Displaying a Confirmation Toast Message
                    Toast.makeText(EditorActivity.this, "Added Successfully.",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditorActivity.this, "Adding Unsuccessful.",
                            Toast.LENGTH_SHORT).show();
                }
                itemName.setText("");
                itemQuantity.setText("");
                itemPrice.setText("");
            }
        });
    }
}