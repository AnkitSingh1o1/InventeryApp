package com.example.inventeryapp.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.content.ContentResolver;

public final class DBContract {

   private DBContract(){};

   /**
    * The "Content authority" is a name for the entire content provider, similar to the
    * relationship between a domain name and its website.  A convenient string to use for the
    * content authority is the package name for the app, which is guaranteed to be unique on the
    * device.
    */
   public static final String CONTENT_AUTHORITY = "com.example.android.inventory";
   /**
    * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    * the content provider.
    */
   public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
   /**
    * Possible path (appended to base content URI for possible URI's)
    * For instance, content://com.example.android.pets/pets/ is a valid path for
    * looking at pet data. content://com.example.android.pets/staff/ will fail,
    * as the ContentProvider hasn't been given any information on what to do with "staff".
    */
   public static final String PATH_INVENTORY = "inventory";
   /**
    * The MIME type of the {@link #} for a list of pets.
    */
   public static final String CONTENT_LIST_TYPE =
           ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

   /**
    * The MIME type of the {@link #} for a single pet.
    */
   public static final String CONTENT_ITEM_TYPE =
           ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

   public static final class DBEntry implements BaseColumns{
      /** The content URI to access the pet data in the provider */
      public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);
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
