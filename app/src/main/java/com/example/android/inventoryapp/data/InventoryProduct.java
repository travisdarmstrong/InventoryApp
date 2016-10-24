package com.example.android.inventoryapp.data;

import android.database.Cursor;
import com.example.android.inventoryapp.data.InventoryContract.ProductsEntry;

/**
 * Created by travis on 9/18/16.
 */
public class InventoryProduct {
    private String _productName;
    private String _productDescription;
    private int _productQuantity;
    private double _productPrice;
    private long _supplierId;

    public InventoryProduct(String name, String description, int quantity, double price, long supplierId){
        this._productName = name;
        this._productDescription = description;
        this._productQuantity = quantity;
        this._productPrice = price;
        this._supplierId = supplierId;
    }

    public InventoryProduct(Cursor c){
        if (c.moveToFirst()){
            this._productName = c.getString(c.getColumnIndex(ProductsEntry.COLUMN_NAME));
            this._productDescription = c.getString(c.getColumnIndex(ProductsEntry.COLUMN_DESCRIPTION));
            this._productQuantity = c.getInt(c.getColumnIndex(ProductsEntry.COLUMN_QUANTITY));
            this._productPrice = c.getDouble(c.getColumnIndex(ProductsEntry.COLUMN_PRICE));;
        }
    }

    public String getName(){
        return _productName;
    }

    public String getDescription(){
        return _productDescription;
    }

    public int getQuantity(){
        return _productQuantity;
    }

    public double getPrice(){
        return _productPrice;
    }

    public long getSupplierId(){
        return _supplierId;
    }
}
