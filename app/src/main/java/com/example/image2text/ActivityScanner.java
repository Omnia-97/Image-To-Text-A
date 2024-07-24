package com.example.image2text;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityScanner extends AppCompatActivity {
    private ImageView captureIV;
    private TextView resultTV;
    private Button snapBtn, detectBtn;
    private Bitmap imageBitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        captureIV=findViewById(R.id.idIVCaptureImage);
        resultTV=findViewById(R.id.idTVDetectedText);
        snapBtn=findViewById(R.id.idButtonSnap);
        detectBtn=findViewById(R.id.idButtonDetect);
        detectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                detectText();
            }
        });

        snapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()){
                    captureImage();
                }else{
                    requestPermission();
                }
            }
        });
    }
    private boolean checkPermission(){
        int cameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA_SERVICE);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length >0){

            boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if(cameraPermission){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                captureImage();
            }else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            captureIV.setImageBitmap(imageBitmap);
        }
    }

    private void detectText(){

    }
    private void captureImage(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager()) != null){
            startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE);
        }

    }
    private void requestPermission(){
        int permissionCode = 200;
        ActivityCompat.requestPermissions(this , new String[]{
                Manifest.permission.CAMERA
        },
                permissionCode);

    }
}