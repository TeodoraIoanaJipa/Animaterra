package com.example.animaterra;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    private static int VIDEO_REQUEST = 101;
    private static int PERMISSION_REQUEST_CODE = 12;
    private Uri videoUri = null;
    private static String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
    private Button recordButton;

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, PERMISSIONS,
                PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean shouldRequestPermissions = false;
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                shouldRequestPermissions = true;
            }
        }
        if (shouldRequestPermissions) {
            requestPermission();
        }

        recordButton = (Button) findViewById(R.id.recordButton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureVideo(v);
            }
        });

    }

    public void captureVideo(View view) {
        Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(videoCaptureIntent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(videoCaptureIntent, VIDEO_REQUEST);
        }
    }

    public void playVideo(View view) {
        Intent playIntent = new Intent(this, VideoPlayActivity.class);
        playIntent.putExtra("video", videoUri.toString());
        startActivity(playIntent);
    }

    public void displayAR(View view) {
        Intent playIntent = new Intent(this, ShowAnimalActivity.class);
        startActivity(playIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == VIDEO_REQUEST && resultCode == RESULT_OK) {
            videoUri = data.getData();
            playVideo(null);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
