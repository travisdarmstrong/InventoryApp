package com.example.android.inventoryapp.data;

/**
 * Created by travis on 9/11/16.
 */
public final class InventoryContract {

    private InventoryContract(){}

    public static final class ProductsEntry{
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE_ID = "image_id";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER_ID = "supplier_id";
    }

    public static final class SuppliersEntry{
        public static final String TABLE_NAME = "suppliers";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone_number";
        public static final String COLUMN_EMAIL = "address_email";
        public static final String COLUMN_ADDRESS = "address_physical";
    }
}
