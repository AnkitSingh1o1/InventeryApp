package com.example.inventeryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.service.controls.actions.FloatAction;
import android.view.View;

import com.example.inventeryapp.data.DBHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHandler dbHandler = new DBHandler(getApplicationContext());
        SQLiteDatabase db = dbHandler.getReadableDatabase();
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

    }
}