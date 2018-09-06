package com.example.surfermate.visionapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.SparseArray;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    /** activity for the ocr detecting app
     * It detects the image through the rear facing camera and then translates to text
     * Implementing an internal db to store all the content captured. Converts info catptured into db
     * @param savedInstanceState
     */
    SharedPreferences sharePref;
    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    Button saveBtn;




    private static final String TAG = "MainActivity";
    private static final int requestPermissionID = 101;


    // Helper objects for detecting taps and pinches.
    //private ScaleGestureDetector scaleGestureDetector;
    //private GestureDetector gestureDetector;

    //for shared preference
    public static final String MY_PREFS_NAME = "SavedTextFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView = findViewById(R.id.surfaceView);
        textView = findViewById(R.id.text_view);
        saveBtn = findViewById(R.id.saveBtn);

        try {
            //Doing this helps remove nullpointer error
            //create the TextRecognizer - which processes images and determines what appears within them
            TextRecognizer tr = new TextRecognizer.Builder(getApplicationContext()).build();
            //calls createCameraSource function once app starts
            startCameraSource(tr);
            //calls createDetector function once app starts
            createDetector(tr);



        }catch(NullPointerException nullPointer){
            throw nullPointer;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != requestPermissionID) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                cameraSource.start(cameraView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startCameraSource(TextRecognizer tr){
        if(!tr.isOperational()){
            Log.w(TAG, "Detector dependencies not loaded yet");
        }
        else{
            //initialize camerasourse create settings
            cameraSource = new CameraSource.Builder(getApplicationContext(), tr)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    //.setFlashMode(useFlash? Camera.Parameters.FLASH_MODE_TORCH : null)
                    .build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height){

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {

                    cameraSource.stop();
                }
            });


        }
    }

    public void createDetector(TextRecognizer tr){
        //maybe separate this into a different function?
        tr.setProcessor(new Detector.Processor<TextBlock>() {
            //when we call release, it will release camera source and that in turn will release the TextRecognizer
            @Override
            public void release() {

            }


            //have to clean this up and order it up nice boii
            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                final SparseArray<TextBlock> items = detections.getDetectedItems();
                if(items.size() != 0){
                    textView.post(new Runnable(){
                        @Override
                        public void run(){
                            final StringBuilder stringBuilder = new StringBuilder();
                            for(int i=0; i<items.size(); i++){
                                TextBlock item = items.valueAt(i);
                                stringBuilder.append(item.getValue());
                                stringBuilder.append("\n");
                            }
                            textView.setText(stringBuilder.toString());
                            //submit button that stores data of text from strinbuilder.
                            //sends over to info to db which sends to another activity.
                            saveBtn.setOnClickListener(new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {

                                    //best way to practice this data transfer
                                    Intent i = new Intent(getApplicationContext(), ReviewActivity.class);
                                    i.putExtra("EXTRA_INFO", stringBuilder.toString());
                                    startActivity(i);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

}
