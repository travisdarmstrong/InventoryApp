package com.example.android.inventoryapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Show detailed product information
 */
public class DetailActivity extends AppCompatActivity{

    private Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        uri = getIntent().getData();

        displayData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the detail menu
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.detail_menu_delete){
            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayData(){
        TextView productNameText = (TextView) findViewById(R.id.detail_name_text);
        TextView productDescText = (TextView) findViewById(R.id.detail_description);
        TextView productQtyText = (TextView) findViewById(R.id.detail_quantity);
        TextView supplierNameText = (TextView) findViewById(R.id.detail_supplier_name);

        productNameText.setText("name");
        productDescText.setText("description");
        productQtyText.setText("2");
        supplierNameText.setText("supplier");
    }
}
