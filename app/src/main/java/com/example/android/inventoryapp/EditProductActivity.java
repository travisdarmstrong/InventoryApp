package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract.ProductsEntry;

/**
 * Edit a product's details, or add a new product
 */

public class EditProductActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Uri uri;
    private long id;
    private int LOADER_REF = 1111;
    private EditText txtProductName;
    private EditText txtProductDescription;
    private EditText txtProductPrice;
    private EditText txtProductQuantity;
    private EditText txtSupplierName;
    private EditText txtSupplierEmail;
    private ImageView productImageView;
    private boolean itemChanged = false;
    Button btnSelectImage;
    Uri imageUri;
    private static final int GET_IMAGE_ACTIVITY = 7777;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_layout);

        txtProductName = (EditText) findViewById(R.id.editProductName);
        txtProductDescription = (EditText) findViewById(R.id.editProductDescription);
        txtProductPrice = (EditText) findViewById(R.id.editProductPrice);
        txtProductQuantity = (EditText) findViewById(R.id.editProductQuantity);
        txtSupplierName = (EditText) findViewById(R.id.editSupplierName);
        txtSupplierEmail = (EditText) findViewById(R.id.editSupplierEmail);
        productImageView = (ImageView) findViewById(R.id.editor_image);
        btnSelectImage = (Button) findViewById(R.id.editor_selectImage);

        txtProductName.setOnTouchListener(onEntryTouched);
        txtProductDescription.setOnTouchListener(onEntryTouched);
        txtProductPrice.setOnTouchListener(onEntryTouched);
        txtProductQuantity.setOnTouchListener(onEntryTouched);
        txtSupplierName.setOnTouchListener(onEntryTouched);
        txtSupplierEmail.setOnTouchListener(onEntryTouched);
        btnSelectImage.setOnTouchListener(onEntryTouched);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getImageIntent.setType("image/*");
                if (getImageIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(getImageIntent, GET_IMAGE_ACTIVITY);
                }
            }
        });

        uri = getIntent().getData();
        if (uri == null) {
            // adding a new product
            this.setTitle(getString(R.string.editor_title_new));
        } else {
            // editing an existing product
            id = ContentUris.parseId(uri);
            this.setTitle(getString(R.string.editor_title_edit));
            getSupportLoaderManager().initLoader(LOADER_REF, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit_action_save) {
            // save the item
            saveProduct();
            // exit the activity
            finish();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            if (!itemChanged) {
                NavUtils.navigateUpFromSameTask(this);
                return true;
            }
            exitConfirmation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public View.OnTouchListener onEntryTouched = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            itemChanged = true;
            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_IMAGE_ACTIVITY && resultCode == RESULT_OK) {
            Bitmap bmp = data.getParcelableExtra("data");
            Uri imageUri = data.getData();
            productImageView.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveProduct() {
        // Make sure there is something to save
        if (TextUtils.isEmpty(txtProductName.getText()) &&
                TextUtils.isEmpty(txtProductDescription.getText()) &&
                TextUtils.isEmpty(txtProductPrice.getText()) &&
                TextUtils.isEmpty(txtProductQuantity.getText()) &&
                TextUtils.isEmpty(txtSupplierName.getText()) &&
                TextUtils.isEmpty(txtSupplierEmail.getText())) {
            Toast.makeText(this, getString(R.string.editor_save_nothing), Toast.LENGTH_SHORT).show();
            return;
        }

        String productName = txtProductName.getText().toString();
        // product name cannot be null
        if (productName.isEmpty()) {
            Toast.makeText(this, getString(R.string.editor_save_error_productname), Toast.LENGTH_SHORT).show();
            return;
        }
        String productDescription = txtProductDescription.getText().toString();
        // product price must be numeric and greater than zero
        double productPrice = -1;
        try {
            String priceString = txtProductPrice.getText().toString().replace("$", "");
            productPrice = Double.parseDouble(priceString);
            if (productPrice < 0) {
                Toast.makeText(this, R.string.editor_save_error_price, Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.editor_save_error_priceinvalid, Toast.LENGTH_SHORT).show();
            return;
        }
        // product quantity must be numeric and greater than zero
        int productQuantity;
        try {
            String quantityString = txtProductQuantity.getText().toString();
            productQuantity = Integer.parseInt(quantityString);
            if (productQuantity < 0) {
                Toast.makeText(this, getString(R.string.editor_save_error_productquantity), Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e){
            Toast.makeText(this, R.string.editor_save_error_quantityinvalid, Toast.LENGTH_SHORT).show();
            return;
        }

        String supplierName = txtSupplierName.getText().toString();
        // supplier name cannot be null
        if (supplierName.isEmpty()){
            Toast.makeText(this, R.string.editor_save_error_supplier, Toast.LENGTH_SHORT).show();
            return;
        }
        String supplierEmail = txtSupplierEmail.getText().toString();
        // supplier email cannot be null
        if (supplierEmail.isEmpty()){
            Toast.makeText(this, R.string.editor_save_error_supplieremail, Toast.LENGTH_SHORT).show();
            return;
        }

        // image uri can be null
        String imageUriString = "";
        if (imageUri !=null){
            imageUriString = imageUri.toString();
        }

        ContentValues values = new ContentValues();
        values.put(ProductsEntry.COLUMN_NAME, productName);
        values.put(ProductsEntry.COLUMN_DESCRIPTION, productDescription);
        values.put(ProductsEntry.COLUMN_PRICE, productPrice);
        values.put(ProductsEntry.COLUMN_QUANTITY, productQuantity);
        values.put(ProductsEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(ProductsEntry.COLUMN_SUPPLIER_EMAIL, supplierEmail);
        values.put(ProductsEntry.COLUMN_IMAGE_URI, imageUriString);

        if (uri == null) {
            // save a new item
            Uri newProductUri = getContentResolver().insert(ProductsEntry.CONTENT_URI, values);
            if (newProductUri == null) {
                Toast.makeText(this, getString(R.string.save_update_fail), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.save_new_success), Toast.LENGTH_SHORT).show();
                itemChanged = false;
            }
        } else {
            // update an existing one
            int numEntriesUpdated = getContentResolver().update(uri, values, null, null);
            if (numEntriesUpdated >= 1) {
                Toast.makeText(this, getString(R.string.save_update_success), Toast.LENGTH_SHORT).show();
                itemChanged = false;
            } else {
                Toast.makeText(this, getString(R.string.save_update_fail), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // if the data hasn't changed, proceed with the action
        if (!itemChanged) {
            super.onBackPressed();
        }
        // There are unsaved changes
        exitConfirmation();
    }

    private void exitConfirmation() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(getString(R.string.exit_confirmation_message));
        alertBuilder.setPositiveButton(getString(R.string.exit_confirmation_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // discard changes and exit
                finish();
            }
        });
        alertBuilder.setNegativeButton(getString(R.string.exit_confirmation_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dismiss the dialog
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_REF) {
            String[] columns = new String[]{
                    ProductsEntry.COLUMN_ID,
                    ProductsEntry.COLUMN_NAME,
                    ProductsEntry.COLUMN_PRICE,
                    ProductsEntry.COLUMN_DESCRIPTION,
                    ProductsEntry.COLUMN_IMAGE_URI,
                    ProductsEntry.COLUMN_QUANTITY,
                    ProductsEntry.COLUMN_SUPPLIER_NAME,
                    ProductsEntry.COLUMN_SUPPLIER_EMAIL};
            return new CursorLoader(this, uri, columns, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            String name = data.getString(data.getColumnIndex(ProductsEntry.COLUMN_NAME));
            String description = data.getString(data.getColumnIndex(ProductsEntry.COLUMN_DESCRIPTION));
            double price = data.getDouble(data.getColumnIndex(ProductsEntry.COLUMN_PRICE));
            int quantity = data.getInt(data.getColumnIndex(ProductsEntry.COLUMN_QUANTITY));
            String supplierName = data.getString(data.getColumnIndex(ProductsEntry.COLUMN_SUPPLIER_NAME));
            String supplierEmail = data.getString(data.getColumnIndex(ProductsEntry.COLUMN_SUPPLIER_EMAIL));
            String imageUriString = data.getString(data.getColumnIndex(ProductsEntry.COLUMN_IMAGE_URI));
            imageUri = Uri.parse(imageUriString);

            txtProductName.setText(name);
            txtProductDescription.setText(description);
            txtProductPrice.setText(String.format("$ %.2f", price));
            txtProductQuantity.setText(String.valueOf(quantity));
            txtSupplierName.setText(supplierName);
            txtSupplierEmail.setText(supplierEmail);
            productImageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        txtProductName.setText("");
        txtProductDescription.setText("");
        txtProductPrice.setText("");
        txtProductQuantity.setText("");
        txtSupplierName.setText("");
        txtSupplierEmail.setText("");
        productImageView.setImageURI(null);
    }
}
