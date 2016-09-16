package com.example.android.inventoryapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.inventoryapp.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity {

    private InventoryDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new InventoryDbHelper(this);
        CreateDummyData();

    }

    private void CreateDummyData(){
        dbHelper.saveNewSupplier("Acme Corporation", "(800) 555-1234", "sales@acmecorporation.com", "1 Acme St. Fairfield, NJ 07004");
        dbHelper.saveNewSupplier("Sears, Roebuck & Co", "", "sales@searsroebuck.com", "925 S. Homan Avenue, Chicago, IL 60624");
        dbHelper.saveNewSupplier("Aperture Science", "", "sales@aperturescience.com", "");
        dbHelper.saveNewSupplier("Cyberdyne Systems", "(800) 555-1001", "sales@cyberdynesystems.com", "");
    }
}
