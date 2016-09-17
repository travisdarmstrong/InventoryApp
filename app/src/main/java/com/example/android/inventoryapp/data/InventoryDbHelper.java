package com.example.android.inventoryapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventoryapp.data.InventoryContract.ProductsEntry;
import com.example.android.inventoryapp.data.InventoryContract.SuppliersEntry;

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
    private static final int DB_VERSION = 1;
    /**
     * SQL code to create products table
     */
    private static final String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
            ProductsEntry.TABLE_NAME + "(" +
            ProductsEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ProductsEntry.COLUMN_NAME + " TEXT NOT NULL, " +
            ProductsEntry.COLUMN_PRICE + " REAL NOT NULL, " +
            ProductsEntry.COLUMN_DESCRIPTION + " TEXT, " +
            ProductsEntry.COLUMN_IMAGE_ID + " INTEGER NOT NULL, " +
            ProductsEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, " +
            ProductsEntry.COLUMN_SUPPLIER_ID + " INTEGER NOT NULL" + ");";
    /**
     * SQL code to create suppliers table
     */
    private static final String SQL_CREATE_SUPPLIERS_TABLE = "CREATE TABLE " +
            SuppliersEntry.TABLE_NAME + "(" +
            SuppliersEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SuppliersEntry.COLUMN_NAME + " TEXT NOT NULL, " +
            SuppliersEntry.COLUMN_PHONE + " REAL NOT NULL, " +
            SuppliersEntry.COLUMN_EMAIL + " TEXT, " +
            SuppliersEntry.COLUMN_ADDRESS + " TEXT " + ");";
    /**
     * SQL code to delete products table
     */
    private static final String SQL_DELETE_PRODUCTS_TABLE =
            "DROP TABLE IF EXISTS " + ProductsEntry.TABLE_NAME + ";";
    /**
     * SQL code to delete suppliers table
     */
    private static final String SQL_DELETE_SUPPLIERS_TABLE =
            "DROP TABLE IF EXISTS " + SuppliersEntry.TABLE_NAME + ";";

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
        db.execSQL(SQL_CREATE_SUPPLIERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PRODUCTS_TABLE);
        db.execSQL(SQL_DELETE_SUPPLIERS_TABLE);
        onCreate(db);
    }

    /**
     * Save a new entry in the Suppliers Table
     *
     * @param name            - name of the supplier
     * @param phoneNumber     - phone number with area code
     * @param emailAddress    - email address to place a new order
     * @param physicalAddress - address of the supplier
     * @return - id of the new table entry
     */
    public long saveNewSupplier(String name, String phoneNumber, String emailAddress, String physicalAddress) {
        // Get a writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Set up the ContentValues object to store the values for each column
        ContentValues cv = new ContentValues();
        cv.put(SuppliersEntry.COLUMN_NAME, name);
        cv.put(SuppliersEntry.COLUMN_PHONE, phoneNumber);
        cv.put(SuppliersEntry.COLUMN_EMAIL, emailAddress);
        cv.put(SuppliersEntry.COLUMN_ADDRESS, physicalAddress);
        // Insert the row in the database table and return the row ID
        return db.insert(SuppliersEntry.TABLE_NAME, null, cv);
    }

    /**
     * Delete a Supplier from the suppliers table given the ID
     *
     * @param supplierId the ID of the supplier to delete
     * @return the number of rows modified
     */
    public int deleteSupplier(long supplierId) {
        // Get a writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Set up the query to find the target row
        String selection = SuppliersEntry.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(supplierId)};
        // Delete the row and return the number of rows modified
        return db.delete(SuppliersEntry.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Get all entries from the Suppliers table
     *
     * @return Cursor with the results
     */
    public Cursor getAllSuppliers() {
        // Get a readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // Perform the query and return the cursor
        return db.query(SuppliersEntry.TABLE_NAME,
                new String[]{SuppliersEntry.COLUMN_ID,
                        SuppliersEntry.COLUMN_NAME,
                        SuppliersEntry.COLUMN_PHONE,
                        SuppliersEntry.COLUMN_EMAIL,
                        SuppliersEntry.COLUMN_ADDRESS},
                null, null, null, null, null);
    }

    /**
     * Delete all entries in the Suppliers Table
     * But leave the database file
     *
     * @return
     */
    public int deleteAllSuppliers() {
        // Get a writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Perform the deletion and return the number of rows deleted
        return db.delete(SuppliersEntry.TABLE_NAME, null, null);
    }

    /**
     * Delete all entries from the Suppliers and Products Tables
     * But leave the database file
     *
     * @return the number of entries deleted
     */
    public int deleteAllEntries() {
        // Delete all entries in each table
        return deleteAllSuppliers() + deleteAllProducts();
    }

    /**
     * Save a new entry in the Products Table
     *
     * @param name name of the item
     * @param price price of the item
     * @param description text description of the item
     * @param imageId resource id of the image
     * @param quantity number currently in stock
     * @param supplierId ID of the supplier in the suppliers table
     * @return the ID of the new entry
     */
    public long saveNewProduct(String name, double price, String description, int imageId, int quantity, long supplierId) {
        // Get a writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Set up the ContentValues object to store the values for each column
        ContentValues cv = new ContentValues();
        cv.put(ProductsEntry.COLUMN_NAME, name);
        cv.put(ProductsEntry.COLUMN_PRICE, price);
        cv.put(ProductsEntry.COLUMN_DESCRIPTION, description);
        cv.put(ProductsEntry.COLUMN_IMAGE_ID, imageId);
        cv.put(ProductsEntry.COLUMN_QUANTITY, quantity);
        cv.put(ProductsEntry.COLUMN_SUPPLIER_ID, supplierId);
        // Insert the row in the database table and return the row ID
        return db.insert(ProductsEntry.TABLE_NAME, null, cv);
    }

    /**
     * Delete a product from the Products Table given the ID
     * @param productId the ID of the entry to delete
     * @return the number of entries deleted
     */
    public int deleteProduct(long productId) {
        // Get a writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Set up the query to find the target row
        String selection = ProductsEntry.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(productId)};
        // Delete the row and return the number of rows modified
        return db.delete(ProductsEntry.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Get all entries from the Products Table
     * @return Cursor with the results
     */
    public Cursor getAllProducts() {
        // Get a readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // Perform the query and return the cursor
        return db.query(ProductsEntry.TABLE_NAME,
                new String[]{ProductsEntry.COLUMN_ID,
                        ProductsEntry.COLUMN_NAME,
                        ProductsEntry.COLUMN_PRICE,
                        ProductsEntry.COLUMN_DESCRIPTION,
                        ProductsEntry.COLUMN_IMAGE_ID,
                        ProductsEntry.COLUMN_QUANTITY,
                        ProductsEntry.COLUMN_SUPPLIER_ID},
                null, null, null, null, null);
    }

    /**
     * Delete all entries in the Products table
     * @return the number of entries deleted
     */
    public int deleteAllProducts() {
        // Get a writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Perform the deletion and return the number of rows deleted
        return db.delete(ProductsEntry.TABLE_NAME, null, null);
    }


}
