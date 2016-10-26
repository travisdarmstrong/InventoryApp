package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract.ProductsEntry;


/**
 * Show detailed product information
 */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "DetailActivity";
    private Uri uri;
    long id;
    TextView productNameText;
    TextView productDescText;
    TextView productQtyText;
    TextView supplierNameText;

    String productName;
    String supplierName;
    String supplierEmail;
    int quantity;

    private int DETAIL_LOADER = 4567;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // load the layout
        setContentView(R.layout.detail);

        // get product information from the intent
        uri = getIntent().getData();
        id = ContentUris.parseId(uri);

        // set the references to the display elements
        productNameText = (TextView) findViewById(R.id.detail_name_text);
        productDescText = (TextView) findViewById(R.id.detail_description);
        productQtyText = (TextView) findViewById(R.id.detail_quantity);
        supplierNameText = (TextView) findViewById(R.id.detail_supplier_name);

        // set listeners for the button to place an order with the supplier
        Button btnOrder = (Button) findViewById(R.id.btn_detail_order);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceOrder();
            }
        });

        // set listeners for the -1 and +1 buttons
        Button minusOne = (Button) findViewById(R.id.btn_detail_minusone);
        Button plusOne = (Button) findViewById(R.id.btn_detail_addone);
        minusOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdjustQuantity(-1);
            }
        });
        plusOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdjustQuantity(1);
            }
        });

        // set listener for the button to add/subtract a custom amount
        Button addAmount = (Button) findViewById(R.id.btn_detail_cutom_add);
        // TODO: set the listener

        // Initialize the content loader
        getSupportLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }


    private void AdjustQuantity(int byVal){
        int newQty = quantity + byVal;
        if (newQty<0){
            Log.e(TAG, "User tried to reduce quantity below zero");
            Toast.makeText(this, getString(R.string.qty_cannot_go_negative), Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(ProductsEntry.COLUMN_QUANTITY, newQty);
        getContentResolver().update(uri, values, null, null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the detail menu
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.detail_menu_delete) {
            int numDeleted = getContentResolver().delete(uri, null, null);
            if (numDeleted>0){
                Toast.makeText(this, R.string.item_delete_success, Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(this, R.string.item_delete_error, Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Place an order with the supplier
     */
    private void PlaceOrder(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{supplierEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Order: " + productName);
        StringBuilder emailText = new StringBuilder();
        emailText.append(getString(R.string.order_email_text)).append("\r\n");
        emailText.append(productName).append("\r\n");
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailText.toString());
        startActivity(emailIntent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == DETAIL_LOADER) {
            String[] columns = new String[]{
                    ProductsEntry.COLUMN_ID,
                    ProductsEntry.COLUMN_NAME,
                    ProductsEntry.COLUMN_DESCRIPTION,
                    ProductsEntry.COLUMN_QUANTITY,
                    ProductsEntry.COLUMN_SUPPLIER_NAME,
                    ProductsEntry.COLUMN_SUPPLIER_EMAIL};
            return new CursorLoader(this, uri, columns, null, null, null);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            // get the data
            productName = data.getString(data.getColumnIndex(ProductsEntry.COLUMN_NAME));
            String productDesc = data.getString(data.getColumnIndex(ProductsEntry.COLUMN_DESCRIPTION));
            quantity = data.getInt(data.getColumnIndex(ProductsEntry.COLUMN_QUANTITY));
            String qtyString = String.valueOf(quantity);
            supplierName = data.getString(data.getColumnIndex(ProductsEntry.COLUMN_SUPPLIER_NAME));
            supplierEmail = data.getString(data.getColumnIndex(ProductsEntry.COLUMN_SUPPLIER_EMAIL));

            // set the displays
            productNameText.setText(productName);
            productDescText.setText(productDesc);
            productQtyText.setText(qtyString);
            supplierNameText.setText(supplierName);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // loader reset. clear the display data
        productNameText.setText("");
        productDescText.setText("");
        productQtyText.setText("");
        supplierNameText.setText("");
    }
}
