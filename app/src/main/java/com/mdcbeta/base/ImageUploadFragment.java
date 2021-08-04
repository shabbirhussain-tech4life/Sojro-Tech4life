package com.mdcbeta.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;

import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.core.content.PermissionChecker.checkSelfPermission;


/**
 * Created by Shakil Karim on 4/2/17.
 */

public abstract class ImageUploadFragment extends BaseFragment implements ImagePickerCallback {


    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private static final int REQUEST_STORAGE = 0;
    private static final int REQUEST_CAMERA = 1;
    private String pickerPath;
    private PermisionGrantedListener permisionGrantedListener;
    private CameraPermisionGrantedListener cameraPermisionGrantedListener;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
    }

    public void showImageDialog(){
        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Option");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {

                requestCameraPermission(isGranted -> {
                    if (isGranted) {
                        dialog.dismiss();
                        takePicture();
                    } else {
                        Toast.makeText(getActivity(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                        getBaseActivity().showError("Camera Permission Denied");


                    }
                });


            } else if (options[item].equals("Choose From Gallery")) {


                requestStoragePermission(isGranted -> {
                    if (isGranted) {
                        dialog.dismiss();
                        pickImageMultiple();
                    } else {
                        Toast.makeText(getActivity(), "Gallery Permission Denied", Toast.LENGTH_SHORT).show();

                    }
                });

            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();

    }


    public void pickImageMultiple() {
        imagePicker = new ImagePicker(this);
        imagePicker.allowMultiple();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                }
                imagePicker.submit(data);
            } else if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                if (cameraPicker == null) {
                    cameraPicker = new CameraImagePicker(this);
                    cameraPicker.reinitialize(pickerPath);
                }
                cameraPicker.submit(data);
            }
        }
    }


    @SuppressLint("WrongConstant")
    public void requestStoragePermission(PermisionGrantedListener permisionGrantedListener) {
        this.permisionGrantedListener = permisionGrantedListener;
        if(checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
          != PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        }else {
            if( this.permisionGrantedListener!=null)
                this.permisionGrantedListener.onPermissionGranted(true);
        }
    }

    @SuppressLint("WrongConstant")
    public void requestCameraPermission(CameraPermisionGrantedListener cameraPermisionGrantedListener) {

        this.cameraPermisionGrantedListener = cameraPermisionGrantedListener;

        if(checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }else {

            if(this.cameraPermisionGrantedListener!=null)
                this.cameraPermisionGrantedListener.onPermissionGranted(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_STORAGE)
        {
            if (grantResults.length == 1 && grantResults[0] == PERMISSION_GRANTED) {

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
            if (grantResults.length == 1 && grantResults[0] == PERMISSION_GRANTED) {

                if(this.cameraPermisionGrantedListener != null){
                    this.cameraPermisionGrantedListener.onPermissionGranted(true);
                }
            } else {
                if(this.cameraPermisionGrantedListener !=null){
                    this.cameraPermisionGrantedListener.onPermissionGranted(false);
                }
                // Permission denied
                Toast.makeText(getActivity(), "Sheet is useless without access to external storage :/", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }


    }


    @Override
    public void onError(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }




    public interface PermisionGrantedListener{
        public void onPermissionGranted(boolean isGranted);
    }
    public interface CameraPermisionGrantedListener{
        public void onPermissionGranted(boolean isGranted);
    }




}
