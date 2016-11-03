package com.example.android.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.inventoryapp.data.InventoryContract.ProductsEntry;

/**
 * ContentProvider for the Inventory database
 */

public class InventoryContentProvider extends ContentProvider {
    private static final String TAG = "InventoryProvider";

    // URI Matcher
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int InventoryTable = 100;
    private static final int InventoryItem = 101;
    static{
        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_PRODUCTS, InventoryTable);
        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_PRODUCTS + "/#", InventoryItem);
    }

    /**
     * Database helper object
     */
    private InventoryDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new InventoryDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor result;
        int uriMatch = uriMatcher.match(uri);
        switch(uriMatch) {
            case InventoryTable:
                // query the entire table
                result = db.query(ProductsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case InventoryItem:
                // query a single item from the table
                selection = ProductsEntry.COLUMN_ID+"=?";
                // get the ID of the entry
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                result = db.query(ProductsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                // no match found
                throw new IllegalArgumentException("Cannot query for Uri: " + uri);
        }
        result.setNotificationUri(getContext().getContentResolver(), uri);
        return result;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        switch(match){
            case InventoryTable:
                return ProductsEntry.CONTENT_DIR_TYPE;
            case InventoryItem:
                return ProductsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Cannot parse uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = uriMatcher.match(uri);
        switch (match){
            case InventoryTable:
                return insertProduct(uri, values);
            default:
                // any other match is invalid, so is a failed match
                throw new IllegalArgumentException("Insertion not supported for uri: " + uri);
        }
    }

    /**
     * Insert a new product into the table
     * @param uri uri of the table
     * @param values data to insert
     * @return the ID of the new entry
     */
    private Uri insertProduct(Uri uri, ContentValues values){
        // Validate the Product Name
        String name = values.getAsString(ProductsEntry.COLUMN_NAME);
        if (name==null){
            throw new IllegalArgumentException("Product Name cannot be empty");
        }
        if (name.isEmpty()){
            throw new IllegalArgumentException("Product Name cannot be empty");
        }

        // Validate the Product Price
        Double price = values.getAsDouble(ProductsEntry.COLUMN_PRICE);
        if (price==null){
            throw new IllegalArgumentException("Product Price cannot be empty");
        }
        if (price<0){
            throw new IllegalArgumentException("Product Price cannot be a negative value");
        }

        // Validate the Quantity
        Integer quantity = values.getAsInteger(ProductsEntry.COLUMN_QUANTITY);
        if (quantity==null){
            throw new IllegalArgumentException("Product Quantity cannot be empty");
        }
        if (quantity<0){
            throw new IllegalArgumentException("Product Quantity cannot be a negative value");
        }

        // Validate the Supplier Name
        String supplierName = values.getAsString(ProductsEntry.COLUMN_SUPPLIER_NAME);
        if (supplierName==null) {
            throw new IllegalArgumentException("Supplier name cannot be empty");
        }
        if (supplierName.isEmpty()){
            throw new IllegalArgumentException("Supplier name cannot be empty");
        }

        // Supplier email address
        String supplierEmail = values.getAsString(ProductsEntry.COLUMN_SUPPLIER_EMAIL);
        if (supplierEmail==null) {
            throw new IllegalArgumentException("Supplier email cannot be empty");
        }
        if (supplierEmail.isEmpty()){
            throw new IllegalArgumentException("Supplier email cannot be empty");
        }

        // Insert the new product in the table
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(ProductsEntry.TABLE_NAME, null, values);
        if (id==-1){
            Log.e(TAG, "Error when inserting new product. Uri: " + uri);
            return null;
        }

        // notify the ContentResolver that a change has occurred
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch(match){
            case InventoryTable:
                // delete based on selection
                return deleteProduct(uri, selection, selectionArgs);
            case InventoryItem:
                // delete a specific entry
                selection = ProductsEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return deleteProduct(uri, selection, selectionArgs);
            default:
                // no match
                throw new IllegalArgumentException("Cannot delete item with Uri: " + uri);
        }
    }

    /**
     * Delete one or more products from the table based on the selection and arguments
     * @param uri the uri of the table
     * @param selection columns to filter
     * @param selectionArgs filter arguments
     * @return number of entries deleted
     */
    private int deleteProduct(Uri uri, String selection, String[] selectionArgs){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(ProductsEntry.TABLE_NAME, selection, selectionArgs);
        if (rowsDeleted>0) {
            // notify the ContentResolver that a change occurred
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch(match){
            case InventoryTable:
                // update based on selection
                return updateProduct(uri, values, selection, selectionArgs);
            case InventoryItem:
                // update a specific entry
                selection = ProductsEntry.COLUMN_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, values, selection, selectionArgs);
            default:
                // no match
                throw new IllegalArgumentException("Cannot update item with Uri: " + uri);
        }
    }

    /**
     * Update one or more products from the table based on the selection and selectionArgs
     * @param uri the uri of the table
     * @param values the new values to update
     * @param selection columns to filter
     * @param selectionArgs filter arguments
     * @return number of entries updated
     */
    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        // if ContentValues is empty, nothing to update
        if (values.size()==0){
            return 0;
        }

        // If provided, validate the Product Name
        if (values.containsKey(ProductsEntry.COLUMN_NAME)) {
            String name = values.getAsString(ProductsEntry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product Name cannot be empty");
            }
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Product Name cannot be empty");
            }
        }

        // If provided, validate the Product Price
        if (values.containsKey(ProductsEntry.COLUMN_PRICE)) {
            Double price = values.getAsDouble(ProductsEntry.COLUMN_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Product Price cannot be empty");
            }
            if (price < 0) {
                throw new IllegalArgumentException("Product Price cannot be a negative value");
            }
        }

        // If provided, validate the Quantity
        if (values.containsKey(ProductsEntry.COLUMN_QUANTITY)) {
            Integer quantity = values.getAsInteger(ProductsEntry.COLUMN_QUANTITY);
            if (quantity == null) {
                throw new IllegalArgumentException("Product Quantity cannot be empty");
            }
            if (quantity < 0) {
                throw new IllegalArgumentException("Product Quantity cannot be a negative value");
            }
        }

        // If provided, validate the Supplier Name
        if (values.containsKey(ProductsEntry.COLUMN_SUPPLIER_NAME)) {
            String supplierName = values.getAsString(ProductsEntry.COLUMN_SUPPLIER_NAME);
            if (supplierName == null) {
                throw new IllegalArgumentException("Supplier name cannot be empty");
            }
            if (supplierName.isEmpty()) {
                throw new IllegalArgumentException("Supplier name cannot be empty");
            }
        }

        // If provided, validate the supplier email address
        if (values.containsKey(ProductsEntry.COLUMN_SUPPLIER_EMAIL)) {
            String supplierEmail = values.getAsString(ProductsEntry.COLUMN_SUPPLIER_EMAIL);
            if (supplierEmail == null) {
                throw new IllegalArgumentException("Supplier email cannot be empty");
            }
            if (supplierEmail.isEmpty()) {
                throw new IllegalArgumentException("Supplier email cannot be empty");
            }
        }

        // Update the table
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated = db.update(ProductsEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated>0){
            // notify the ContentResolver that a change occurred
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
