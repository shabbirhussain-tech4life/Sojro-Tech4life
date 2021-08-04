package com.mdcbeta.healthprovider.cases;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Space;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.widget.ActionBar;
import com.mdcbeta.widget.FlowLayout;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;

/**
 * Created by MahaRani on 13/11/2018.
 */

public class RadImageFragment extends BaseFragment {

    @Inject
    ApiFactory apiFactory;

    @BindView(R.id.rad_image_container)
    FlowLayout rad_image_container;

    @Inject
    MDCRepository mdcRepository;

    @BindDimen(R.dimen.dp_150)
    int dp_150;
    @BindDimen(R.dimen.dp_10)
    int dp_10;

    static String img_id;
    ArrayList<String> images;
    private LayoutInflater inflater;


    public RadImageFragment() {
    }


    public static RadImageFragment newInstance(String id) {
        RadImageFragment fragment = new RadImageFragment();
        img_id = id;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images = new ArrayList<>();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inflater = LayoutInflater.from(getContext());
        getData();


    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_rad_images;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    public void getData(){



        showProgress("Loading...");

        apiFactory.rad_images(img_id)
                .compose(RxUtil.applySchedulers())
                .subscribe(resource -> {

                    images = new ArrayList<>();

                    hideProgress();

                    if(resource.status) {


                        try {
                            int imagesSize = resource.data.get("rec_images").getAsJsonArray().size();

                            rad_image_container.removeAllViews();

                            List<String> imageUrls = new ArrayList<String>();
                            List<String> dl = new ArrayList<String>();
                            ImageView imageView = null;
                            View imageViewItem = null;
                            for (int i = 0; i < imagesSize; i++) {
                                JsonObject images = resource.data.get("rec_images").getAsJsonArray().get(i).getAsJsonObject();

                                imageUrls.add(AppModule.radimage + images.get("image").getAsString());

                                Log.d("image url 2 :  ", AppModule.radimage + images.get("image").getAsString());


                                imageViewItem = inflater.inflate(R.layout.include_image, rad_image_container, false);
                                imageView = (ImageView) imageViewItem.findViewById(R.id.image);


                                Picasso.with(getContext())
                                        .load(AppModule.radimage + images.get("image").getAsString())
                                        .fit().centerCrop()
                                        .placeholder(R.drawable.image_placeholder).into(imageView);

                                int j = 0;

                                imageView.setTag(j++);

                                imageView.setOnClickListener(v -> {
                                    int pos = (int) v.getTag();

                                    new ImageViewer.Builder(getActivity(), imageUrls)
                                            .setStartPosition(pos)
                                            .show();

                                });

                                Space space = new Space(RadImageFragment.this.getContext());
                                space.setLayoutParams(new FlowLayout.LayoutParams(dp_10, dp_10));

                                rad_image_container.addView(space);
                                rad_image_container.addView(imageViewItem);

                            }

                        } catch (Exception ex) {
                        }





                    }else {

                        showError("");
                    }

                },throwable -> {

                });



    }

}
