package com.example.android.inventoryapp.data;

import android.database.Cursor;
import com.example.android.inventoryapp.data.InventoryContract.SuppliersEntry;

/**
 * Created by travis on 9/18/16.
 */
public class InventorySupplier {

    private String _supplierName;
    private String _supplierPhone;
    private String _supplierEmail;
    private String _supplierAddress;

    public InventorySupplier(String name, String phone, String email, String address){
        this._supplierName = name;
        this._supplierPhone = phone;
        this._supplierEmail = email;
        this._supplierAddress = address;
    }

    public InventorySupplier(Cursor c){
        if (c.moveToFirst()){
            this._supplierName = c.getString(c.getColumnIndex(SuppliersEntry.COLUMN_NAME));
            this._supplierPhone = c.getString(c.getColumnIndex(SuppliersEntry.COLUMN_PHONE));
            this._supplierEmail = c.getString(c.getColumnIndex(SuppliersEntry.COLUMN_EMAIL));
            this._supplierAddress = c.getString(c.getColumnIndex(SuppliersEntry.COLUMN_ADDRESS));
        }
    }

    public String getName(){
        return _supplierName;
    }

    public String getPhone(){
        return _supplierPhone;
    }

    public String getEmail(){
        return _supplierEmail;
    }

    public String getAddress(){
        return _supplierAddress;
    }
}
