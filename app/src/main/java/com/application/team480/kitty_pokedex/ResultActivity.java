package com.application.team480.kitty_pokedex;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImage;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is Result activity. It receives the file path from the main activity.
 * Then, creates a file using the path and passes it to Watson to get the result.
 */
public class ResultActivity extends AppCompatActivity {
    private String filePath;
    private VisualRecognition vrClient;
    private ImageView imageView;
    private ListView listView;
    private List<Result> topFive;
    private final int NUM_OF_CLASSIFIERS = 2;
    private ClassifierResult defaultList;
    private ClassifierResult catList;
    private List<Result> resultList;
    private ResultAdapter resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = findViewById(R.id.imageView);
        listView = findViewById(R.id.listView);
        topFive = new ArrayList<>();
        resultList = new ArrayList<>();
        // Initialize Visual Recognition client
        vrClient = new VisualRecognition(
                VisualRecognition.VERSION_DATE_2016_05_20,
                getString(R.string.api_key)
        );
        // Receives extras from the main activity
        Intent intent = getIntent();
        filePath = intent.getStringExtra("filePath");
        Log.d("Debug", "Path: " + filePath);
        // Creates an image using the file path and sets it as the image
        Bitmap photo = BitmapFactory.decodeFile(filePath);
        imageView.setImageBitmap(photo);
        // Initializes the list view adapter
        resultAdapter = new ResultAdapter(this, R.layout.listview_result, topFive);
        // Creates a file using the file path and compress it so that Watson can take it.
        final File photoFile = saveBitmapToFile(new File(filePath));
        // Creates another thread to run Watson
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Parameters are to use custom classifiers
                    ClassifyOptions options = new ClassifyOptions.Builder()
                            .imagesFile(photoFile).parameters("{\"classifier_ids\": [\"Cat_977315332\", "
                                    + "\"default\"]}")
                            .build();
                    final ClassifiedImages response = vrClient.classify(options).execute();
                    ClassifiedImage classifiedImage = response.getImages().get(0);
                    // Gets the number of classes of the classifier
                    int numOfClassifiers = classifiedImage.getClassifiers().size();
                    setLists(classifiedImage);
                    createResultList();
                    createTopFive();
                    // Only update when the picture is a cat and the number of classifiers = 2
                    if (checkValidity() && numOfClassifiers == NUM_OF_CLASSIFIERS) {
                    // Update the list view
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resultAdapter.setData(topFive);
                            listView.setAdapter(resultAdapter);
                        }
                    });
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        // When an item of the list view is selected
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Log.i("ResultActivity", "an item is selected");
                Intent intent = new Intent(ResultActivity.this, InfoActivity.class);
                intent.putExtra("breed",resultAdapter.getItem(position).getBreed());
                startActivity(intent);
            }
        });
    }

    /**
     * This method creates a list of top five by taking top five from the result list.
     */
    private void createTopFive() {
        // If the size of the result list is less than 5, then take everything from the list.
        if (resultList.size() < 5) {
            for (int i = 0; i < resultList.size(); ++i) {
                topFive.add(resultList.get(i));
            }
            // Else, take only first 5 results.
        } else {
            for (int i = 0; i < 5; ++i) {
                topFive.add(resultList.get(i));
            }
        }
    }

    /**
     * This method create the result list by copying each element of Cat list, the response from Wastson.
     * Then, sort the list by percentage.
     */
    private void createResultList() {
        int size = catList.getClasses().size();
        // Copy each element over
        for (int i = 0; i < size; ++i) {
            ClassResult classResult = catList.getClasses().get(i);
            resultList.add(new Result(classResult.getClassName(), classResult.getScore()));
        }
        // Sort the list
        Collections.sort(resultList, new SortByPercentage());
    }

    /**
     * This method is to print out a list.
     * @param list
     *              List of Results
     */
    private void printList(List<Result> list) {
        for (int i = 0; i < list.size(); ++i) {
            Log.d("Name", list.get(i).getBreed());
            Log.d("Percentage", list.get(i).getPercentage() + "");
        }
    }

    /**
     * Use the default list to check if the picture is a cat.
     * @return
     *          True if it's a cat. Otherwise, false.
     */
    private boolean checkValidity() {
        Log.d("Debug", "Checking validity");
        int numOfClasses = defaultList.getClasses().size();
        boolean isCat = false;
        for (int i = 0; i < numOfClasses; ++i){
            ClassResult classResult = defaultList.getClasses().get(i);
            // Check if one of its classes is 'cat'
            if (classResult.getClassName().equals("cat")) {
                isCat = true;
                break;
            }
        }
        return isCat;
    }

    /**
     * This method sets the default and cat lists.
     * @param classifiedImage
     *                          classified image from Watson
     */
    private void setLists(ClassifiedImage classifiedImage) {
        int numOfClassifiers = classifiedImage.getClassifiers().size();
        for (int i = 0; i < numOfClassifiers; ++i) {
            ClassifierResult classifierResult = classifiedImage.getClassifiers().get(i);
            Log.d("Debug", "Name: " + classifierResult.getName());
            setDefaultList(classifierResult);
            setCatList(classifierResult);
        }
    }

    /**
     * This method sets the default list.
     * @param classifierResult
     *                          classifier result from Watson
     */
    private void setDefaultList(ClassifierResult classifierResult) {
        if (classifierResult.getName().equals("default")) {
            defaultList = classifierResult;
        }
    }

    /**
     * This method sets the cat list.
     * @param classifierResult
     *                          classifier result from Watson
     */
    private void setCatList(ClassifierResult classifierResult) {
        if (classifierResult.getName().equals("Cat")) {
            catList = classifierResult;
        }
    }

    /**
     * This method is to compress the size of the file.
     * @param file
     *              file to compress
     * @return
     *          compressed file
     */
    public File saveBitmapToFile(File file){
        try {
            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image
            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();
            // The new size we want to scale to
            final int REQUIRED_SIZE=75;
            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);
            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();
            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);
            return file;
        } catch (Exception e) {
            return null;
        }
    }
}
