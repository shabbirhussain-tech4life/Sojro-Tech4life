package com.mdcbeta.patient.PersonalCare.DialogBox;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.base.ImageUploadFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.cases.adapter.ImagePreviewAdapter;
import com.mdcbeta.patient.PersonalCare.LifeStyleMain.BMIMainFragment;
import com.mdcbeta.patient.PersonalCare.dateshow;
import com.mdcbeta.patient.PersonalCare.timeshow;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.VerticalSpaceItemDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

/**
 * Created by MahaRani on 29/06/2018.
 */

public class RadiologyDialog extends BaseDialogFragment implements ImagePickerCallback {


    @BindView(R.id.radiologydate)
    Button radiologydate;

    @BindView(R.id.radiologytime)
    Button radiologytime;

    @BindView(R.id.radiologyok)
    Button radiologyok;

    @BindView(R.id.radiologycancel)
    Button radiologycancel;

    @BindView(R.id.radiologycomment)
    EditText radiologycomment;

    @BindView(R.id.weight_unit)
    Spinner weight_unit;

    @BindView(R.id.lvResults)
    RecyclerView lvResults;

    @Inject
    ApiFactory apiFactory;

    dateshow d;
    timeshow t;

    String res, time;
    String weightunit;

    private ImagePreviewAdapter adapter;
    private ImageUploadFragment.PermisionGrantedListener permisionGrantedListener;
    private ImageUploadFragment.CameraPermisionGrantedListener cameraPermisionGrantedListener;
    private static final int REQUEST_STORAGE = 0;
    private static final int REQUEST_CAMERA = 1;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private String pickerPath;
    int listviewSpace;


    private String shareOption;
    int feet,inches,weight;


    public static RadiologyDialog newInstance() {
        RadiologyDialog frag = new RadiologyDialog();
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        initList();
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.radiologydailog;
    }


    @Override
    public void onValidationSucceeded() {

        showProgress("Loading");

        apiFactory.addRadiologiesRecord(getUser().getId(), res,
                time, weightunit,
                "dummy", radiologycomment.getText().toString(),
                com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()),
                com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()))
                .compose(RxUtil.applySchedulers())
                .subscribe(model -> {
                    if(model.status) {
                        hideProgress();
                        //   callback.onYesClick();
                        //   dismiss();

                    }else {
                        showError(model.getMessage());
                    }

                },throwable -> showError(throwable.getMessage()));

    }



    @OnClick({R.id.radiologydate, R.id.radiologytime,R.id.radiologyok,R.id.radiologycancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.radiologydate:
               // d = new dateshow(radiologydate, getActivity());
                String a = radiologydate.getText().toString();
                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to.format(from.parse(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.radiologytime:
             //   t = new timeshow(radiologytime, getActivity());
                String b = radiologytime.getText().toString();
                String[] parts = b.split(" ");
                time = parts[0];
                break;
            case R.id.radiologyok:

                String d = radiologydate.getText().toString();
                SimpleDateFormat from1 = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                try {
                    res = to1.format(from1.parse(d));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String g = radiologytime.getText().toString();
                String[] parts1 = g.split(" ");
                time = parts1[0];

                showProgress("Loading");

                apiFactory.addRadiologiesRecord(getUser().getId(), res,
                        time, weightunit,
                        "dummy", radiologycomment.getText().toString(),
                        com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()),
                        com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()))
                        .compose(RxUtil.applySchedulers())
                        .subscribe(model -> {
                            if(model.status) {
                                hideProgress();
                                //   callback.onYesClick();
                                //   dismiss();

                            }else {
                                showError(model.getMessage());
                            }

                        },throwable -> showError(throwable.getMessage()));

                getFragmentHandlingActivity().replaceFragment(BMIMainFragment.newInstance());

                break;

            case R.id.radiologycancel:
                dismiss();
                break;
        }
    }


    @OnItemSelected(R.id.weight_unit) void onRealationselected(AdapterView<?> parent, int position) {
        weightunit = (String) parent.getItemAtPosition(position);
    }

    @OnClick(R.id.camera_fab)
    public void onViewClicked() {
        showImageDialog();
    }


    private void initList() {
        adapter = new ImagePreviewAdapter(getActivity());
        lvResults.setAdapter(adapter);
        lvResults.setHasFixedSize(true);
        lvResults.setItemAnimator(new DefaultItemAnimator());
        lvResults.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        lvResults.setNestedScrollingEnabled(false);
        lvResults.setLayoutManager(new GridLayoutManager(getContext(), 2));

    }

    @Override
    public void onImagesChosen(List<ChosenImage> list) {
        adapter.addImage(list);
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


    public void requestStoragePermission(ImageUploadFragment.PermisionGrantedListener permisionGrantedListener) {
        this.permisionGrantedListener = permisionGrantedListener;
        if(checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        }else {
            if( this.permisionGrantedListener!=null)
                this.permisionGrantedListener.onPermissionGranted(true);
        }
    }

    public void requestCameraPermission(ImageUploadFragment.CameraPermisionGrantedListener cameraPermisionGrantedListener) {

        this.cameraPermisionGrantedListener = cameraPermisionGrantedListener;

        if(checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        }else {

            if(this.cameraPermisionGrantedListener!=null)
                this.cameraPermisionGrantedListener.onPermissionGranted(true);
        }
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
    public void onError(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}


