package com.example.android.inventoryapp.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.data.InventoryContract.ProductsEntry;
import static com.example.android.inventoryapp.R.id.product_price;
import static com.example.android.inventoryapp.R.id.qty_available;

/**
 * Adapter to display a Cursor in the listview item
 */

public class InventoryCursorAdapter extends CursorAdapter {
    public InventoryCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
    }

    /**
     * Set the views of the list item to values from the cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Get the textviews
        TextView txtName = (TextView) view.findViewById(R.id.product_name_text);
        TextView txtDescription = (TextView) view.findViewById(R.id.product_description_text);
        TextView txtPrice = (TextView) view.findViewById(product_price);
        TextView txtQuantity = (TextView) view.findViewById(qty_available);

        // get the values
        String name = cursor.getString(cursor.getColumnIndex(ProductsEntry.COLUMN_NAME));
        String description = cursor.getString(cursor.getColumnIndex(ProductsEntry.COLUMN_DESCRIPTION));
        String price = String.valueOf(cursor.getDouble(cursor.getColumnIndex(ProductsEntry.COLUMN_PRICE)));
        String quantity = String.valueOf(cursor.getInt(cursor.getColumnIndex(ProductsEntry.COLUMN_QUANTITY)));

        // set the text
        txtName.setText(name);
        txtDescription.setText(description);
        txtPrice.setText(price);
        txtQuantity.setText(quantity);


    }
}
