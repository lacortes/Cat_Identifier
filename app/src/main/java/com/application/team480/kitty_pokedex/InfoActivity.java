package com.application.team480.kitty_pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * This class is information activity. It displays the information
 * about the selected breed.
 */
public class InfoActivity extends AppCompatActivity {
    private String breed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        breed = intent.getStringExtra("breed");
    }
}
