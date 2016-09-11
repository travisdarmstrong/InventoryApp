package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.inventoryapp.data.InventoryContract.ProductsEntry;
import com.example.android.inventoryapp.data.InventoryContract.SuppliersEntry;

/**
 * Created by travis on 9/11/16.
 */
public class InventoryDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "inventory.db";
    private static final int DB_VERSION = 1;
    private static final String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
            ProductsEntry.TABLE_NAME + "(" +
            ProductsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
            ProductsEntry.COLUMN_NAME + " TEXT NOT NULL " +
            ProductsEntry.COLUMN_PRICE + " REAL NOT NULL " +
            ProductsEntry.COLUMN_DESCRIPTION + " TEXT " +
            ProductsEntry.COLUMN_IMAGE_ID + " INTEGER NOT NULL " +
            ProductsEntry.COLUMN_QUANTITY + " INTEGER NOT NULL " +
            ProductsEntry.COLUMN_SUPPLIER_ID + " INTEGER NOT NULL" + ");";
    private static final String SQL_CREATE_SUPPLIERS_TABLE = "CREATE TABLE " +
            SuppliersEntry.TABLE_NAME + "(" +
            SuppliersEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
            SuppliersEntry.COLUMN_NAME + " TEXT NOT NULL " +
            SuppliersEntry.COLUMN_PHONE + " REAL NOT NULL " +
            SuppliersEntry.COLUMN_EMAIL + " TEXT " +
            SuppliersEntry.COLUMN_ADDRESS + " TEXT " + ");";
    private static final String SQL_DELETE_PRODUCTS_TABLE =
            "DROP TABLE IF EXISTS " + ProductsEntry.TABLE_NAME + ";";
    private static final String SQL_DELETE_SUPPLIERS_TABLE =
            "DROP TABLE IF EXISTS " + SuppliersEntry.TABLE_NAME + ";";

    public InventoryDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
        db.execSQL(SQL_CREATE_SUPPLIERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PRODUCTS_TABLE);
        db.execSQL(SQL_DELETE_SUPPLIERS_TABLE);
        onCreate(db);
    }

    
}