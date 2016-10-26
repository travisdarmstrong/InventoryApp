package com.example.android.inventoryapp.data;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.inventoryapp.DetailActivity;
import com.example.android.inventoryapp.R;
import com.example.android.inventoryapp.data.InventoryContract.ProductsEntry;

import static com.example.android.inventoryapp.R.id.product_price;
import static com.example.android.inventoryapp.R.id.qty_available;

/**
 * Adapter to display a Cursor in the listview item
 */

public class InventoryCursorAdapter extends CursorAdapter {
    private Context mContext;

    public InventoryCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.mContext = context;
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

        // get the id of the item
        final long id = cursor.getLong(cursor.getColumnIndex(ProductsEntry.COLUMN_ID));

        // get the button to sell an item
        Button btnSell = (Button) view.findViewById(R.id.qty_sell_button);
        // set the "Tag" to be the cursor position so we know which button got clicked
        btnSell.setTag(id);
        // set the listener that will decrease the quantity
        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CursorAdapter", "Button " + String.valueOf(id) + " clicked");
            }
        });

        // set the "Tag" to be the cursor position so we know which button got clicked
        txtName.setTag(id);
        txtDescription.setTag(id);

        // set the listener that will open the detail view
        txtName.setOnClickListener(detailClickListener);
        txtDescription.setOnClickListener(detailClickListener);
    }

    /**
     * When the user clicks on the text of the item, show the detailed view
     */
    private View.OnClickListener detailClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            long id = (long)v.getTag();
            Intent detailIntent = new Intent(mContext, DetailActivity.class);
            Uri detailUri = ContentUris.withAppendedId(ProductsEntry.CONTENT_URI, id);
            detailIntent.setData(detailUri);
            mContext.startActivity(detailIntent);
        }
    };

}
