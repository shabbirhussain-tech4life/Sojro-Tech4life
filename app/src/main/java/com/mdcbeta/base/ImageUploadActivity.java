package com.mdcbeta.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;

import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.mdcbeta.R;

/**
 * Created by Shakil Karim on 5/6/17.
 */

public abstract class ImageUploadActivity extends BaseActivity implements ImagePickerCallback {

    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private static final int REQUEST_STORAGE = 0;
    private static final int REQUEST_CAMERA = 1;
    private String pickerPath;
    private PermisionGrantedListener permisionGrantedListener;
    private CameraPermisionGrantedListener cameraPermisionGrantedListener;

    public void showImageDialog(){
        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Option");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {

                requestCameraPermission(isGranted -> {
                    if (isGranted) {
                        dialog.dismiss();
                        takePicture();
                    } else {
                        Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                        showError("Camera Permission Denied");


                    }
                });


            } else if (options[item].equals("Choose From Gallery")) {


                requestStoragePermission(isGranted -> {
                    if (isGranted) {
                        dialog.dismiss();
                        pickImage();
                    } else {
                        Toast.makeText(this, "Gallery Permission Denied", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();

    }


    public void pickImage() {
        imagePicker = new ImagePicker(this);
        imagePicker.shouldGenerateMetadata(true);
        imagePicker.shouldGenerateThumbnails(true);
        imagePicker.setImagePickerCallback(this);
        imagePicker.pickImage();
    }


    public void takePicture() {
        cameraPicker = new CameraImagePicker(this);
        cameraPicker.shouldGenerateMetadata(true);
        cameraPicker.shouldGenerateThumbnails(true);
        cameraPicker.setImagePickerCallback(this);
        pickerPath = cameraPicker.pickImage();
    }



    public void requestStoragePermission(PermisionGrantedListener permisionGrantedListener) {
        this.permisionGrantedListener = permisionGrantedListener;
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
          != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
              REQUEST_STORAGE);
        }else {
            if( this.permisionGrantedListener!=null)
                this.permisionGrantedListener.onPermissionGranted(true);
        }
//by kanwal


    }

    public void requestCameraPermission(CameraPermisionGrantedListener cameraPermisionGrantedListener) {

        this.cameraPermisionGrantedListener = cameraPermisionGrantedListener;

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }else {

            if(this.cameraPermisionGrantedListener!=null)
                this.cameraPermisionGrantedListener.onPermissionGranted(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_STORAGE)
        {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if(this.permisionGrantedListener !=null){
                    this.permisionGrantedListener.onPermissionGranted(true);
                }
            } else {

                if(this.permisionGrantedListener !=null){
                    this.permisionGrantedListener.onPermissionGranted(false);
                }
            }
        }
        else if (requestCode == REQUEST_CAMERA)
        {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if(this.cameraPermisionGrantedListener != null){
                    this.cameraPermisionGrantedListener.onPermissionGranted(true);
                }
            } else {
                if(this.cameraPermisionGrantedListener !=null){
                    this.cameraPermisionGrantedListener.onPermissionGranted(false);
                }
                // Permission denied
                Toast.makeText(this, "Sheet is useless without access to external storage :/", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(this);
                }
                imagePicker.submit(data);
            } else if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                if (cameraPicker == null) {
                    cameraPicker = new CameraImagePicker(this);
                    cameraPicker.setImagePickerCallback(this);
                    cameraPicker.reinitialize(pickerPath);
                }
                cameraPicker.submit(data);
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


    public interface PermisionGrantedListener{
        public void onPermissionGranted(boolean isGranted);
    }
    public interface CameraPermisionGrantedListener{
        public void onPermissionGranted(boolean isGranted);
    }

//    @Override
//    public void onBackPressed() {
//
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            new android.app.AlertDialog.Builder(this)
//                    .setIcon(R.drawable.ic_dialog_alert)
//                    .setTitle("You want to exit ?")
//                    .setPositiveButton("Yes", (dialog, which) -> {
//                        super.onBackPressed();
//
//
//                    })
//                    .setNegativeButton("No", null)
//                    .show();
//            //additional code
//        } else {
//            getSupportFragmentManager().popBackStack();
//        }
//
//    }
}
