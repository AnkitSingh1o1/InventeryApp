package com.example.inventeryapp.data;

import android.provider.BaseColumns;

public final class DBContract {

   private DBContract(){};

   public static final class DBEntry implements BaseColumns{
      /**
       * Name of database Table
       *
       */
      public final static String TABLE_NAME = "inventory";
      /**
       * Unique ID number for the items (only for use in the database table).
       *
       * Type: INTEGER
       */
      public final static String _ID = BaseColumns._ID;
      /**
       * Name of the items
       *
       * Type: TEXT
       */
      public final static String COLUMN_ITEM_NAME ="Name";
      /**
       * Quantity of Item
       *
       * Type: INTEGER
       */
      public final static String COLUMN_ITEM_QUANTITY = "Quantity";
      /**
       * Price of each item.
       *
       * Type: INTEGER
       */
      public final static String COLUMN_ITEM_PRICE = "Price";
   }
}
