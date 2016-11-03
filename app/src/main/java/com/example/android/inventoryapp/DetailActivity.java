package com.example.android.inventoryapp;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    ImageView productImageView;
    String productName;
    double productPrice;
    String productDesc;
    Uri productImageUri;
    int productQuantity;
    String supplierName;
    String supplierEmail;

    private int DETAIL_LOADER = 4567;

// TODO: show picture

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
        productImageView = (ImageView) findViewById(R.id.detail_image);

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
        Button addAmount = (Button) findViewById(R.id.btn_detail_custom_add);
        addAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCustomQuantityPicker(1);
            }
        });

        Button subtractAmount = (Button) findViewById(R.id.btn_detail_custom_sub);
        subtractAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCustomQuantityPicker(-1);
            }
        });

        // Initialize the content loader
        getSupportLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }


    private void AdjustQuantity(int byVal) {
        int newQty = productQuantity + byVal;
        if (newQty < 0) {
            Log.e(TAG, "User tried to reduce quantity below zero");
            Toast.makeText(this, getString(R.string.qty_cannot_go_negative), Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(ProductsEntry.COLUMN_QUANTITY, newQty);
        getContentResolver().update(uri, values, null, null);
    }

    /**
     * Display a dialog to let the user select a custom value to add/remove items
     */
    private void ShowCustomQuantityPicker(final int multiplier) {
        final Dialog pickerDialog = new Dialog(this);
        pickerDialog.setContentView(R.layout.number_picker);
        String addOrRemove = multiplier > 0 ? getString(R.string.detail_picker_add) : getString(R.string.detail_picker_remove);
        TextView txtInstructions = (TextView) pickerDialog.findViewById(R.id.txtInstructions);
        txtInstructions.setText(String.format(getString(R.string.detail_picker_instructions_placeholder), addOrRemove));
        final EditText txtQty = (EditText) pickerDialog.findViewById(R.id.txtCustomQuantity);
        Button btnOk = (Button) pickerDialog.findViewById(R.id.picker_btnOK);
        Button btnCancel = (Button) pickerDialog.findViewById(R.id.picker_btnCancel);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txtQty.getText())) {
                    Log.d(TAG, "No value entered");
                    pickerDialog.dismiss();
                    return;
                }
                int val = Integer.parseInt(txtQty.getText().toString());
                if (val < 0) {
                    Toast.makeText(DetailActivity.this, R.string.detail_picker_negative_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                int adjustment = val * multiplier;
                Log.d(TAG, "User wants to adjust quantity by " + String.valueOf(adjustment));
                AdjustQuantity(adjustment);
                pickerDialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerDialog.dismiss();
            }
        });

        pickerDialog.show();
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
            deleteProduct();
        }
        if (item.getItemId() == R.id.detail_menu_edit) {
            // open the EditProductActivity with this product as the URI
            Intent editIntent = new Intent(this, EditProductActivity.class);
            editIntent.setData(uri);
            startActivity(editIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteProduct() {
        // show confirmation dialog
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(getString(R.string.delete_conf_message));
        alertBuilder.setPositiveButton(getString(R.string.delete_positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the item
                delete();
            }});
        alertBuilder.setNegativeButton(getString(R.string.delete_negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // just close the dialog
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    private void delete() {
        int numDeleted = getContentResolver().delete(uri, null, null);
        if (numDeleted > 0) {
            Toast.makeText(this, R.string.item_delete_success, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.item_delete_error, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Place an order with the supplier
     */
    private void PlaceOrder() {
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
                    ProductsEntry.COLUMN_PRICE,
                    ProductsEntry.COLUMN_DESCRIPTION,
                    ProductsEntry.COLUMN_IMAGE_URI,
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
            productPrice = data.getDouble(data.getColumnIndex(ProductsEntry.COLUMN_PRICE));
            productDesc = data.getString(data.getColumnIndex(ProductsEntry.COLUMN_DESCRIPTION));
            String productImageUriString = data.getString(data.getColumnIndex(ProductsEntry.COLUMN_IMAGE_URI));
            productImageUri = Uri.parse(productImageUriString);
            productQuantity = data.getInt(data.getColumnIndex(ProductsEntry.COLUMN_QUANTITY));
            String qtyString = String.valueOf(productQuantity);
            supplierName = data.getString(data.getColumnIndex(ProductsEntry.COLUMN_SUPPLIER_NAME));
            supplierEmail = data.getString(data.getColumnIndex(ProductsEntry.COLUMN_SUPPLIER_EMAIL));

            // set the displays
            productNameText.setText(productName);
            productDescText.setText(productDesc);
            productQtyText.setText(qtyString);
            supplierNameText.setText(supplierName);
            productImageView.setImageURI(productImageUri);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // loader reset. clear the display data
        productNameText.setText("");
        productDescText.setText("");
        productQtyText.setText("");
        supplierNameText.setText("");
        productImageView.setImageURI(null);
    }
}
