package com.application.team480.kitty_pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * This class is information activity. It displays the information
 * about the selected breed.
 */
public class InfoActivity extends AppCompatActivity {
    private ArrayList<Result> topFive;
    private String breed;
    private String filePath;
    private final int INFO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Log.i("InfoActivity", "in Info");
        Intent intent = getIntent();
        breed = intent.getStringExtra("breed");
        filePath = intent.getStringExtra("filePath");
        topFive = intent.getParcelableArrayListExtra("topFive");
        for (int i = 0; i < topFive.size(); ++i) {
            Log.i("Breed", topFive.get(i).getBreed());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("Item ID", item.getItemId() + "");
        Log.i("Home ID", android.R.id.home + "");
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                upIntent.putExtra("filePath", filePath);
                upIntent.putExtra("from", INFO);
                upIntent.putParcelableArrayListExtra("topFive", topFive);
                NavUtils.navigateUpTo(this, upIntent);
                //NavUtils.
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
