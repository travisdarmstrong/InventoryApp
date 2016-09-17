package com.example.android.inventoryapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.inventoryapp.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity {

    private InventoryDbHelper dbHelper;
    private static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new InventoryDbHelper(this);
        dbHelper.deleteAllEntries();
        CreateDummyData();

        Cursor sCursor = dbHelper.getAllSuppliers();
        Cursor pCursor = dbHelper.getAllProducts();

        Log.d(TAG, "Suppliers: " + sCursor.getCount());
        Log.d(TAG, "Products: " + pCursor.getCount());
    }

    private void CreateDummyData(){
        long acmeId = dbHelper.saveNewSupplier(getString(R.string.supplier1_name), getString(R.string.supplier1_phone), getString(R.string.supplier1_email), getString(R.string.supplier1_address));
        long apertureScienceId = dbHelper.saveNewSupplier(getString(R.string.supplier2_name), getString(R.string.supplier2_phone), getString(R.string.supplier2_email), getString(R.string.supplier2_address));
        long cyberdyneId = dbHelper.saveNewSupplier(getString(R.string.supplier3_name), getString(R.string.supplier3_phone), getString(R.string.supplier3_email), getString(R.string.supplier2_address));

        // Create some products from Acme
        dbHelper.saveNewProduct(getString(R.string.anvil_name), 17, getString(R.string.anvil_name), 0, 4, acmeId);
        dbHelper.saveNewProduct(getString(R.string.birdseed_name), 4.50, getString(R.string.anvil_name), 0, 8, acmeId);
        dbHelper.saveNewProduct(getString(R.string.skates_name), 1200, getString(R.string.skates_desc), 0, 0, acmeId);

        // Create some products from Aperture Science
        dbHelper.saveNewProduct(getString(R.string.portal_name), 2499.99, getString(R.string.portal_desc), 0, 1, apertureScienceId);
        dbHelper.saveNewProduct(getString(R.string.scube_name), 115, getString(R.string.scube_desc), 0, 80, apertureScienceId);
        dbHelper.saveNewProduct(getString(R.string.ccube_name), 116, getString(R.string.ccube_desc), 0, 1, apertureScienceId);
        dbHelper.saveNewProduct(getString(R.string.turret_name), 11999.99, getString(R.string.turret_desc), 0, 12, apertureScienceId);

        // Create some products from Cyberdyne Systems
        dbHelper.saveNewProduct(getString(R.string.t70_name), 60500, getString(R.string.t70_desc), 0, 4, cyberdyneId);
        dbHelper.saveNewProduct(getString(R.string.t800_name), 450000, getString(R.string.t800_desc), 0, 1, cyberdyneId);
    }
}
