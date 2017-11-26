package com.application.team480.kitty_pokedex;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.android.library.camera.CameraHelper;
import java.io.File;

/**
 * This class is the main activity. It has two buttons: Camera and Gallery.
 * Camera button lets the user to take a picture and save it to their phone.
 * Gallery button lets the user to pick a picture from gallery.
 */
public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 999;
    private static final int PICK_IMAGE = 100;
    private final int MAIN = 1;
    private CameraHelper cameraHelper;
    private Button btnCamera;
    private Button btnGallery;
    private Uri imageUri;
    private String filePath;

    private Button fakeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCamera = findViewById(R.id.Camera);
        btnGallery = findViewById(R.id.Gallery);
        // Initialize camera cameraHelper
        cameraHelper = new CameraHelper(this);
        // When Camera button is clicked
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Camera", "Clicked");
                cameraHelper.dispatchTakePictureIntent();
                Log.d("Camera", "Ended");
            }
        });
        // When Gallery button is clicked
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check whether your app has access to the WRITE permission
                if (checkPermission()) {
                    // If your app has access to the device’s storage, then print the following message to Android Studio’s Logcat
                    Log.e("permission", "Permission already granted.");
                    Log.d("Gallery", "Clicked");
                    openGallery();
                    Log.d("Gallery", "Ended");
                } else {
                    // If your app doesn’t have permission to access external storage, then call requestPermission
                    requestPermission();
                }
            }
        });
    }

    /**
     * This method checks if the user granted to access gallery.
     * @return
     *          true, if the permission is granted; otherwise, false.
     */
    private boolean checkPermission() {
        //Check for READ_EXTERNAL_STORAGE access, using ContextCompat.checkSelfPermission()//
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //If the app does have this permission, then return true//
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
        //If the app doesn’t have this permission, then return false//
            return false;
        }
    }

    /**
     * This method requests the user for a permission to access gallery.
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    /**
     * This method is to open gallery.
     */
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(MainActivity.this,
                      //      "Permission accepted", Toast.LENGTH_LONG).show();
                    openGallery();
                } else {
                    //Toast.makeText(MainActivity.this,
                      //      "Permission denied", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Debug", "RequestCode: " + requestCode);
        Log.d("Debug", "ResultCode: " + resultCode);
        // When Camera is selected
        if (resultCode == RESULT_OK && requestCode == CameraHelper.REQUEST_IMAGE_CAPTURE) {
            final File picFile = cameraHelper.getFile(resultCode);
            filePath = picFile.getAbsolutePath();
            Log.d("Debug", "Path: " + filePath);
            moveToResultActivity(filePath);
            // When Gallery is selected
        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imageUri, projection,null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
            Log.d("Debug", "Path: " + filePath);
            moveToResultActivity(filePath);
        }
    }

    /**
     * This method is to move to Result activity after taking or picking a picture.
     * @param filePath file path to the selected picture
     */
    private void moveToResultActivity(String filePath) {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("filePath", filePath);
        intent.putExtra("from", MAIN);
        startActivity(intent);
    }

}

