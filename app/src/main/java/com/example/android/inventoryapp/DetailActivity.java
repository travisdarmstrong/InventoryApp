package com.example.android.inventoryapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

/**
 * Created by travis on 9/11/16.
 */
public class DetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the detail menu
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }
}
