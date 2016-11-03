package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventoryapp.data.InventoryContract.ProductsEntry;

/**
 * Provides an interface to the Inventory database with Products and Suppliers tables
 */
public class InventoryDbHelper extends SQLiteOpenHelper {

    /**
     * Inventory Database name
     */
    private static final String DB_NAME = "inventory.db";
    /**
     * Inventory Database version
     */
    private static final int DB_VERSION = 3;
    /**
     * SQL code to create products table
     */
    private static final String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
            ProductsEntry.TABLE_NAME + "(" +
            ProductsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ProductsEntry.COLUMN_NAME + " TEXT NOT NULL, " +
            ProductsEntry.COLUMN_PRICE + " REAL NOT NULL, " +
            ProductsEntry.COLUMN_DESCRIPTION + " TEXT, " +
            ProductsEntry.COLUMN_IMAGE_URI + " TEXT, " +
            ProductsEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, " +
            ProductsEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL," +
            ProductsEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL);";
    /**
     * SQL code to delete products table
     */
    private static final String SQL_DELETE_PRODUCTS_TABLE =
            "DROP TABLE IF EXISTS " + ProductsEntry.TABLE_NAME + ";";
    /**
     * Create a new instance of the {@link InventoryDbHelper}
     *
     * @param context application context
     */
    public InventoryDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PRODUCTS_TABLE);
        onCreate(db);
    }

}
