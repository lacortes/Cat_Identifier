package com.application.team480.kitty_pokedex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 *  This Activity accesses the Firebase Database to retrieve information about a cat breed and displays the info.
 *  This Activity accepts the breed info through a static method, newIntent(Context, String) (see below).
 *
 *  Ex: Pass the name of breed as an extra to this Activity (BreedInformationActivity) by button click, from MainActivity.
 *      The breed found in this case is a Sphynx.
 *      ...
 *      fakeButton.setOnClickListener(new View.OnClickListener() {
 *          public void onClick(View view) {
 *              Intent i = BreedInformationActivity.newIntent(MainActivity.this, "Sphynx");
 *              startActivity(i);
 *      });
 *      ...
 */
public class BreedInformationActivity extends AppCompatActivity {
    private static final String EXTRA_BREED_TYPE = "com.application.team480.kitty_pokedex.breed_type";
    private static String TAG = "BreedInformationActivity";
    private ListView characteristicListView;
    private CharacteristicListAdapter characterListAdapter;
    private FirebaseDatabase db;
    private DatabaseReference mDatabase;
    private String extraBreedType;
    private TextView breedNameTextView;
    private TextView breedInfoTextView;
    private ArrayList<Result> topFive;
    private String filePath;
    private ImageView breedImageView;
    private final int INFO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breed_information);
        setTitle("Cat Info");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Get extra passed in through intent
        Intent intent = getIntent();
        extraBreedType = intent.getStringExtra("breed").toLowerCase();
        Log.i(TAG, "Breed passed in: " + extraBreedType);
        filePath = intent.getStringExtra("filePath");
        topFive = intent.getParcelableArrayListExtra("topFive");
        for (int i = 0; i < topFive.size(); ++i) {
            Log.i("Breed", topFive.get(i).getBreed());
        }

        // Wire up TextView widgets
        breedNameTextView = (TextView) findViewById(R.id.breed_name_textview);
        breedInfoTextView = (TextView) findViewById(R.id.breed_info_textview);
        characteristicListView = (ListView) findViewById(R.id.breed_characteristic_list);

        breedImageView = (ImageView) findViewById(R.id.breed_imageview);
        breedImageView.setImageBitmap(getRoundedCornerBitmap(drawableToBitmap(determineBreedResource(extraBreedType))));


        // Access database and get reference to path of breed
        db = FirebaseDatabase.getInstance();  // Access database
        mDatabase = db.getReference("breeds/"+extraBreedType);

        // Query the database once, and retrieve the required breed info
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                KittyBreed kitty = dataSnapshot.getValue(KittyBreed.class);
                Log.i(TAG, "Retrieving info from Firebase Datbase ...");
                Log.i(TAG, kitty.getBreedType());

                breedNameTextView.setText(capitalFirstLetter( kitty.getBreedType() ));
                breedInfoTextView.setText(kitty.getInfo());

                List<Characteristic> characteristics = new ArrayList<>();

                // Affection
                Characteristic characteristic = new Characteristic();
                characteristic.setType(CharacteristicType.AFFECTION);
                characteristic.setScale(kitty.getAffection());
                characteristics.add(characteristic);

                // Energy
                characteristic = new Characteristic();
                characteristic.setType(CharacteristicType.ENERGY);
                characteristic.setScale(kitty.getEnergy());
                characteristics.add(characteristic);

                // Friendliness
                characteristic = new Characteristic();
                characteristic.setType(CharacteristicType.FRIENDLINESS);
                characteristic.setScale(kitty.getFriendliness());
                characteristics.add(characteristic);

                // Fur Length
                characteristic = new Characteristic();
                characteristic.setType(CharacteristicType.FUR_LENGTH);
                characteristic.setScale(kitty.getLength());
                characteristics.add(characteristic);

                // Grooming
                characteristic = new Characteristic();
                characteristic.setType(CharacteristicType.GROOMING);
                characteristic.setScale(kitty.getGrooming());
                characteristics.add(characteristic);

                // Training
                characteristic = new Characteristic();
                characteristic.setType(CharacteristicType.TRAINING);
                characteristic.setScale(kitty.getTraining());
                characteristics.add(characteristic);

                Log.i(TAG, characteristics.toString());

                characterListAdapter = new CharacteristicListAdapter(
                        getApplicationContext(),
                        R.layout.breed_characteristic_item,
                        characteristics);
                characteristicListView.setAdapter(characterListAdapter);
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
    private String capitalFirstLetter(String text) {
        String firstLetter = text.substring(0, 1).toUpperCase();
        String restOfText = text.substring(1, text.length());

        return firstLetter + restOfText;
    }

    private Drawable determineBreedResource(String breed) {
        String filtered = breed.replace(" ", "");
        Log.i(TAG, filtered);

        return getResources().getDrawable(getResources().getIdentifier(filtered, "drawable", getPackageName()));
    }

    /**
     * This method creates an Intent, and name of breed to pass as an extra to this activity.
     * @param packageContext Context from which the Intent is being created from.
     * @param breed Name of breed to request information from.
     * @return Intent
     */
    public static Intent newIntent(Context packageContext, String breed) {
        Intent i = new Intent(packageContext, BreedInformationActivity.class);
        i.putExtra(EXTRA_BREED_TYPE, breed);
        return i;
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

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 100;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

}
