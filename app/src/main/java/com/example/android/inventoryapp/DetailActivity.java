package com.example.android.inventoryapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.TextView;

import com.example.android.inventoryapp.data.InventoryDbHelper;
import com.example.android.inventoryapp.data.InventoryProduct;
import com.example.android.inventoryapp.data.InventorySupplier;

/**
 * Created by travis on 9/11/16.
 */
public class DetailActivity extends AppCompatActivity{

    private InventoryDbHelper dbHelper;
    private long productId;
    private InventoryProduct product;
    private InventorySupplier supplier;
    public static final String EXTRA_PRODUCTID = "productid";
    public static final String EXTRA_DBHELPER = "dbhelper";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productId = getIntent().getLongExtra(EXTRA_PRODUCTID, -1);
        dbHelper = new InventoryDbHelper(this);
        setContentView(R.layout.detail);
        getProductInfo(productId);
        displayData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the detail menu
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    private void getProductInfo(long productId){
        Cursor c = dbHelper.getProductById(productId);
        product = new InventoryProduct(c);
        getSupplierInfo(product.getSupplierId());
    }

    private void getSupplierInfo(long supplierId){
        Cursor c = dbHelper.getSupplierById(supplierId);
        supplier = new InventorySupplier(c);
    }

    private void displayData(){
        TextView productNameText = (TextView) findViewById(R.id.detail_name_text);
        TextView productDescText = (TextView) findViewById(R.id.detail_description);
        TextView productQtyText = (TextView) findViewById(R.id.detail_quantity);
        TextView supplierNameText = (TextView) findViewById(R.id.detail_supplier_name);

        productNameText.setText(product.getName());
        productDescText.setText(product.getDescription());
        productQtyText.setText(String.valueOf(product.getQuantity()));
        supplierNameText.setText(supplier.getName());
    }
}
