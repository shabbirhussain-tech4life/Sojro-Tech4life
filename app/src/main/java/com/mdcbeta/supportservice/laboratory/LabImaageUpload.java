package com.mdcbeta.supportservice.laboratory;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.mdcbeta.R;
import com.mdcbeta.base.ImageUploadFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.healthprovider.cases.adapter.ImagePreviewAdapter;
import com.mdcbeta.util.RxBus;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.VerticalSpaceItemDecoration;
import com.mdcbeta.widget.ActionBar;
import com.trello.rxlifecycle2.android.FragmentEvent;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.xml.validation.Validator;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by MahaRani on 12/11/2018.
 */

public class LabImaageUpload extends ImageUploadFragment {

    private static final String TAG = "CreateCaseFragment";
    public static int scrollX = 0;
    public static int scrollY = -1;
    @BindView(R.id.lvResults)
    RecyclerView lvResults;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;

    static String mId;
    static String record_id;

    @BindView(R.id.btn_upload)
    Button btn_upload;

    @Inject
    ApiFactory apiFactory;

    @Inject
    RxBus rxBus;

    private ImagePreviewAdapter adapter;
    private Validator validator;

    public static LabImaageUpload newInstance(String id, String recordid) {
        LabImaageUpload fragment = new LabImaageUpload();
        mId = id;
        record_id = recordid;
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_radiology_upload;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }


    @Override
    public void onResume() {
        super.onResume();

    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated: ");


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();

        Log.d(TAG, "onViewCreated: ");


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

    @OnClick(R.id.camera_fab)
    public void onViewClicked() {
        showImageDialog();
    }

    @Override
    public void onImagesChosen(List<ChosenImage> list) {
        adapter.addImage(list);
    }



    public void onValidation() {

        showProgress("Creating case..");

        File file;
        String uploadId = UUID.randomUUID().toString();
        List<ChosenImage> images_path = adapter.getFiles();

        List<MultipartBody.Part> parts = new ArrayList<>();

        if(images_path != null && !images_path.isEmpty()) {
            for (int i = 0; i < images_path.size(); i++) {
                file = new File(images_path.get(i).getThumbnailPath());

                Log.d("Image path: ", file.getPath());

                if (file.exists()) {

                    try {
                        new MultipartUploadRequest(this.getContext(), uploadId, AppModule.UPLOAD_URL2)
                                .addFileToUpload(file.getPath(), "image") //Adding file
                                .addParameter("name", mId)
                                .addParameter("record_id", record_id)
                                .setNotificationConfig(new UploadNotificationConfig())
                                .setMaxRetries(1)
                                .startUpload(); //S
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final MediaType MEDIA_TYPE = MediaType.parse(images_path.get(i).getMimeType());
                    parts.add(MultipartBody.Part.createFormData("my_images[]",
                            file.getName(), RequestBody.create(MEDIA_TYPE, file)));

                    Log.d(TAG, "File exist" + MultipartBody.Part.createFormData("my_images[]",
                            file.getName(), RequestBody.create(MEDIA_TYPE, file)));

                } else {

                }

            }
        }


        apiFactory.lab_image(mId, parts,record_id)
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model-> {

                    if(model.status){
                        showSuccess(model.getMessage());
                        getFragmentHandlingActivity().replaceFragment(MainLaboratoryFragment.newInstance());
                    }
                    else {
                        showError(model.getMessage());
                    }

                },throwable -> {
                    hideProgress();
                    throwable.printStackTrace();
                });

    }



    @OnClick(R.id.btn_upload)
    public void CreateCaseClick() {
        onValidation();
    }






}
