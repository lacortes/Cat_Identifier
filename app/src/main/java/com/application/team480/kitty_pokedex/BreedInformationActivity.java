package com.application.team480.kitty_pokedex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BreedInformationActivity extends AppCompatActivity {
    private static final String EXTRA_BREED_TYPE = "com.application.team480.kitty_pokedex.breed_type";
    private static String TAG = "BreedInformationActivity";
    private FirebaseDatabase db;
    private DatabaseReference mDatabase;
    private String extraBreedType;
    private TextView breedNameTextView;
    private TextView breedInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breed_information);

        // Get extra passed in through intent
        extraBreedType = getIntent().getStringExtra(EXTRA_BREED_TYPE);

        breedNameTextView = (TextView) findViewById(R.id.breed_name_textview);
        breedInfoTextView = (TextView) findViewById(R.id.breed_info_textview);

        // Access database and get reference to path of breed
        db = FirebaseDatabase.getInstance();  // Access database
        mDatabase = db.getReference("breeds/"+extraBreedType);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KittyBreed kitty = dataSnapshot.getValue(KittyBreed.class);
                Log.i(TAG, kitty.getBreedType());

                // Set Text for views
                breedNameTextView.setText(capitalFirstLetter( kitty.getBreedType() ));
                breedInfoTextView.setText(kitty.getInfo());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Takes in a word and returns it with the first letter capitalized.
     * @param text Word to process.
     * @return String
     */
    public String capitalFirstLetter(String text) {
        String firstLetter = text.substring(0, 1).toUpperCase();
        String restOfText = text.substring(1, text.length());

        return firstLetter + restOfText;
    }

    /**
     *
     * @param packageContext Context from which the
     * @param breed Name of breed to request information from Firebasedatabase.
     * @return Intent
     */
    public static Intent newIntent(Context packageContext, String breed) {
        Intent i = new Intent(packageContext, BreedInformationActivity.class);
        i.putExtra(EXTRA_BREED_TYPE, breed);
        return i;
    }

}
