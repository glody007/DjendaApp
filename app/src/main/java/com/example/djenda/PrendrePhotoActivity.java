package com.example.djenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.camerakit.CameraKitView;
import com.example.djenda.reseau.Repository;
import com.example.djenda.ui.ajouterdetailsarticle.AjouterDetailsArticleActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class PrendrePhotoActivity extends AppCompatActivity {

    private CameraKitView cameraKitView;
    private FloatingActionButton buttonPrendrePhoto;
    private String mPhotoName = "default.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prendre_photo);
        cameraKitView = findViewById(R.id.camera);
        buttonPrendrePhoto = findViewById(R.id.imgCapture);
        buttonPrendrePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { prendrePhoto(); }
        });
    }

    private void prendrePhoto() {
        cameraKitView.captureImage(new CameraKitView.ImageCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onImage(CameraKitView cameraKitView, final byte[] capturedImage) {
                Log.d("prendre photo", "debut");
                if(hasPermissionToWriteInExternalStorage()) {
                    Log.d("prendre photo", "debut");
                    saveImage(capturedImage);
                }
                else { requestPermissionToWriteInExternalStorage(); }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermissionToWriteInExternalStorage() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


    private void requestPermissionToWriteInExternalStorage() {
            ActivityCompat.requestPermissions(PrendrePhotoActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 42);
    }

    private void saveImage(byte[] capturedImage) {
        Repository.getInstance().setPhotArticle(capturedImage);
        File savedPhoto = createFileToSave();
        try {
            Log.d("prendre photo", "debut");
            FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
            outputStream.write(capturedImage);
            outputStream.close();
            Log.d("prendre photo", "fin");
            startAjouterDetailsActivity(savedPhoto.getPath());
        } catch (java.io.IOException e) {
            Log.d("prendre photo", "erreur");
            e.printStackTrace();
        }
    }

    private void startAjouterDetailsActivity(String pathPhoto) {
        Intent intent = new Intent(this, AjouterDetailsArticleActivity.class);
        intent.putExtra("photo_uri", pathPhoto);
        intent.putExtra("photo_name", mPhotoName);
        startActivity(intent);
    }

    @NotNull
    private File createFileToSave() {
        File dossier =  new File(Environment.getExternalStorageDirectory() + "/djenda");
        if (!dossier.exists()) { dossier.mkdirs(); }
        mPhotoName = generatePhotoName();
        return new File(dossier, mPhotoName);
    }

    private String generatePhotoName() {
        Date d = new Date();
        CharSequence s  = DateFormat.format("MM-dd-yy_hh-mm-ss", d.getTime());
        return s.toString() + ".jpg";
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}