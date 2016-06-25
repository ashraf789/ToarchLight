package com.example.syed.torchlight;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {

    ImageButton imageButton;
    private static Camera camera = null;
    private static Parameters parameters = null;
    private static boolean torchStatus;
    private static boolean hasFlushLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageButton = (ImageButton) findViewById(R.id.image_button);

        checkTorchFeature();
        openCamera();
        buttonImageChange();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (torchStatus == false) turnOnTorch();
                else if (torchStatus == true) turnOffTorch();
            }
        });
    }

    public void checkTorchFeature(){
        if (!getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            hasFlushLight = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("!! Your Device Not Support Flash Light !!");
            builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    toast("Torch Light Closed");
                    finish();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            return;
        }else{
            torchStatus = true;
            hasFlushLight = true;
        }

    }

    public void openCamera(){
        try {
            camera = Camera.open();
            parameters = camera.getParameters();
        }catch (Exception e){

        }

    }
    public void turnOnTorch(){
        if (camera == null || parameters == null) return;

        parameters = camera.getParameters();
        parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();

        torchStatus = true;
        buttonImageChange();
    }
    public void turnOffTorch(){
        if (camera == null || parameters == null) return;

        parameters = camera.getParameters();
        parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
        camera.stopPreview();

        torchStatus = false;
        buttonImageChange();
    }


    //change imageButton image
    public void buttonImageChange(){
        if (torchStatus == true){
            imageButton.setImageResource(R.drawable.on);
        }else
            imageButton.setImageResource(R.drawable.off);
    }

    // minimizing default method
    public void toast(String message){
        Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
    }


    // -----------------

    @Override
    protected void onPause() {
        super.onPause();
        turnOffTorch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasFlushLight){
            turnOnTorch();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        openCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null){
            camera.release();
            camera = null;
        }
    }
}