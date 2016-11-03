package com.example.android.inventoryapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract.ProductsEntry;
import com.example.android.inventoryapp.data.InventoryCursorAdapter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "Main Activity";
    private InventoryCursorAdapter adapter;
    private static final int LOADER_REFERENCE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_main_new_product);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newProductIntent = new Intent(getApplicationContext(), EditProductActivity.class);
                startActivity(newProductIntent);
            }
        });
        adapter = new InventoryCursorAdapter(this, null, 0);
        ListView productListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);
        productListView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(LOADER_REFERENCE, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.main_menu_insert_dummy_data):
                CreateDummyData();
                return true;
            case (R.id.main_menu_delete_all):
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Confirm the user wants to delete all products in the database
     */
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(getString(R.string.delete_all_conf_message));
        alertBuilder.setPositiveButton(getString(R.string.delete_all_conf_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllProducts();
            }
        });
        alertBuilder.setNegativeButton(getString(R.string.delete_all_conf_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    /**
     * Delete all products in the database and display a message with the result
     */
    private void deleteAllProducts() {
        int numRowsDeleted = getContentResolver().delete(ProductsEntry.CONTENT_URI, null, null);
        if (numRowsDeleted > 0) {
            Toast.makeText(this, getString(R.string.delete_all_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.delete_all_fail), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Create some data in the table
     */
    private void CreateDummyData() {

        ContentValues values = new ContentValues();

        // create some products from Aperture Science
        values.put(ProductsEntry.COLUMN_NAME, getString(R.string.portal_name));
        values.put(ProductsEntry.COLUMN_PRICE, 2499.99);
        values.put(ProductsEntry.COLUMN_DESCRIPTION, getString(R.string.portal_desc));
        values.put(ProductsEntry.COLUMN_IMAGE_URI, getDrawableResourceUri(R.drawable.aperture_science_handheld_portal_device).toString());
        values.put(ProductsEntry.COLUMN_QUANTITY, 1);
        values.put(ProductsEntry.COLUMN_SUPPLIER_NAME, getString(R.string.supplier2_name));
        values.put(ProductsEntry.COLUMN_SUPPLIER_EMAIL, getString(R.string.supplier2_email));
        Uri newProductUri = getContentResolver().insert(ProductsEntry.CONTENT_URI, values);

        values.put(ProductsEntry.COLUMN_NAME, getString(R.string.scube_name));
        values.put(ProductsEntry.COLUMN_PRICE, 115);
        values.put(ProductsEntry.COLUMN_DESCRIPTION, getString(R.string.scube_desc));
        values.put(ProductsEntry.COLUMN_IMAGE_URI, getDrawableResourceUri(R.drawable.aperture_science_weighted_storage_cube).toString());
        values.put(ProductsEntry.COLUMN_QUANTITY, 80);
        values.put(ProductsEntry.COLUMN_SUPPLIER_NAME, getString(R.string.supplier2_name));
        values.put(ProductsEntry.COLUMN_SUPPLIER_EMAIL, getString(R.string.supplier2_email));
        newProductUri = getContentResolver().insert(ProductsEntry.CONTENT_URI, values);

        values.put(ProductsEntry.COLUMN_NAME, getString(R.string.ccube_name));
        values.put(ProductsEntry.COLUMN_PRICE, 116);
        values.put(ProductsEntry.COLUMN_DESCRIPTION, getString(R.string.ccube_desc));
        values.put(ProductsEntry.COLUMN_IMAGE_URI, getDrawableResourceUri(R.drawable.aperture_science_weighted_companion_cube).toString());
        values.put(ProductsEntry.COLUMN_QUANTITY, 1);
        values.put(ProductsEntry.COLUMN_SUPPLIER_NAME, getString(R.string.supplier2_name));
        values.put(ProductsEntry.COLUMN_SUPPLIER_EMAIL, getString(R.string.supplier2_email));
        newProductUri = getContentResolver().insert(ProductsEntry.CONTENT_URI, values);

        values.put(ProductsEntry.COLUMN_NAME, getString(R.string.turret_name));
        values.put(ProductsEntry.COLUMN_PRICE, 11999.99);
        values.put(ProductsEntry.COLUMN_DESCRIPTION, getString(R.string.turret_desc));
        values.put(ProductsEntry.COLUMN_IMAGE_URI, getDrawableResourceUri(R.drawable.aperture_science_sentry_turret).toString());
        values.put(ProductsEntry.COLUMN_QUANTITY, 12);
        values.put(ProductsEntry.COLUMN_SUPPLIER_NAME, getString(R.string.supplier2_name));
        values.put(ProductsEntry.COLUMN_SUPPLIER_EMAIL, getString(R.string.supplier2_email));
        newProductUri = getContentResolver().insert(ProductsEntry.CONTENT_URI, values);

        // create some products from Cyberdyne Systems
        values.put(ProductsEntry.COLUMN_NAME, getString(R.string.t70_name));
        values.put(ProductsEntry.COLUMN_PRICE, 60500);
        values.put(ProductsEntry.COLUMN_DESCRIPTION, getString(R.string.t70_desc));
        values.put(ProductsEntry.COLUMN_IMAGE_URI, getDrawableResourceUri(R.drawable.cyberdyne_systems_t70).toString());
        values.put(ProductsEntry.COLUMN_QUANTITY, 4);
        values.put(ProductsEntry.COLUMN_SUPPLIER_NAME, getString(R.string.supplier3_name));
        values.put(ProductsEntry.COLUMN_SUPPLIER_EMAIL, getString(R.string.supplier3_email));
        newProductUri = getContentResolver().insert(ProductsEntry.CONTENT_URI, values);

        values.put(ProductsEntry.COLUMN_NAME, getString(R.string.t800_name));
        values.put(ProductsEntry.COLUMN_PRICE, 450000);
        values.put(ProductsEntry.COLUMN_DESCRIPTION, getString(R.string.t800_desc));
        values.put(ProductsEntry.COLUMN_IMAGE_URI, getDrawableResourceUri(R.drawable.cyberdyne_systems_t800).toString());
        values.put(ProductsEntry.COLUMN_QUANTITY, 1);
        values.put(ProductsEntry.COLUMN_SUPPLIER_NAME, getString(R.string.supplier3_name));
        values.put(ProductsEntry.COLUMN_SUPPLIER_EMAIL, getString(R.string.supplier3_email));
        newProductUri = getContentResolver().insert(ProductsEntry.CONTENT_URI, values);

        // Create some products from Acme
        values.put(ProductsEntry.COLUMN_NAME, getString(R.string.anvil_name));
        values.put(ProductsEntry.COLUMN_PRICE, 17);
        values.put(ProductsEntry.COLUMN_DESCRIPTION, getString(R.string.anvil_desc));
        values.put(ProductsEntry.COLUMN_IMAGE_URI, getDrawableResourceUri(R.drawable.acme_anvil).toString());
        values.put(ProductsEntry.COLUMN_QUANTITY, 4);
        values.put(ProductsEntry.COLUMN_SUPPLIER_NAME, getString(R.string.supplier1_name));
        values.put(ProductsEntry.COLUMN_SUPPLIER_EMAIL, getString(R.string.supplier1_email));
        newProductUri = getContentResolver().insert(ProductsEntry.CONTENT_URI, values);

        values.put(ProductsEntry.COLUMN_NAME, getString(R.string.skates_name));
        values.put(ProductsEntry.COLUMN_PRICE, 1200);
        values.put(ProductsEntry.COLUMN_DESCRIPTION, getString(R.string.skates_desc));
        values.put(ProductsEntry.COLUMN_IMAGE_URI, getDrawableResourceUri(R.drawable.acme_rocket_powered_roller_skates).toString());
        values.put(ProductsEntry.COLUMN_QUANTITY, 0);
        values.put(ProductsEntry.COLUMN_SUPPLIER_NAME, getString(R.string.supplier1_name));
        values.put(ProductsEntry.COLUMN_SUPPLIER_EMAIL, getString(R.string.supplier1_email));
        newProductUri = getContentResolver().insert(ProductsEntry.CONTENT_URI, values);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_REFERENCE) {

            Uri uri = ProductsEntry.CONTENT_URI;
            String[] columns = new String[]{
                    ProductsEntry.COLUMN_ID,
                    ProductsEntry.COLUMN_NAME,
                    ProductsEntry.COLUMN_PRICE,
                    ProductsEntry.COLUMN_DESCRIPTION,
                    ProductsEntry.COLUMN_IMAGE_URI,
                    ProductsEntry.COLUMN_QUANTITY};
            return new CursorLoader(this,
                    uri, columns,
                    null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (adapter != null) {
            adapter.swapCursor(null);
        }
    }

    private Uri getDrawableResourceUri(int resource) {
        Uri result = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        getResources().getResourcePackageName(resource) + "/" +
                        getResources().getResourceTypeName(resource) + "/" +
                        getResources().getResourceEntryName(resource));
        return result;
    }
}
