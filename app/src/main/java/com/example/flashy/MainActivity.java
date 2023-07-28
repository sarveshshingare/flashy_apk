package com.example.flashy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.flashy.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private Boolean isTorchOn;  //to save the state of flashlight

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        isTorchOn = false;

        //check if flashLight is available
        Boolean isFlashAvailable = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvailable) {
            AlertDialogBox();
            return;
        }

        //onClick listener for switch button
        binding.button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (binding.button.isChecked()) {
                        binding.imageView.setImageResource(R.drawable.on);
                        changeLightState(true);
                        isTorchOn = true;
                        binding.textView.setText("Flashlight :  ON");

                    } else {
                        binding.imageView.setImageResource(R.drawable.off);
                        changeLightState(false);
                        isTorchOn = false;
                        binding.textView.setText("Flashlight : OFF");

                    }
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("App not supported");
                    binding.button.setChecked(false);
                    binding.imageView.setImageResource(R.drawable.off);
                    AlertDialogBox();
                }
            }
        });
    }

    private void AlertDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Device does not have Flashlight");
        builder.setTitle("Alert !");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //  method to turn on/off flashlight
    private void changeLightState(boolean state) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
            String camId;
            try {
                camId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(camId, state);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

    }

    //    turn off flashlight when app is paused
    @Override
    protected void onPause() {
        super.onPause();
        if (isTorchOn) {
            changeLightState(false);
            binding.textView.setText("Flashlight : OFF");

        }
    }

//    turn on flashlight when app is resumed

    @Override
    protected void onResume() {
        super.onResume();
        if (isTorchOn) {
            changeLightState(true);
            binding.textView.setText("Flashlight :  ON");

        }
    }

}
//done
