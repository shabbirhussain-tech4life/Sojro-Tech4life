package com.mdcbeta.patient.dashboard;

import android.os.Bundle;

import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.mdcbeta.widget.ActionBar;
import com.mdcbeta.widget.FlowLayout;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.ImageViewer;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;

import static com.mdcbeta.util.AppPref.Key.KEY_RECORD_ID;

/**
 * Created by shakil on 4/18/2017.
 */

public class AppointmentDetailFragment extends BaseFragment {


    @Inject
    MDCRepository mdcRepository;
    @Inject
    ApiFactory apiFactory;
    private static final String TAG = "AppointmentDetailFragme";
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_gender)
    TextView txtGender;
    @BindView(R.id.txt_location)
    TextView txtLocation;
    @BindView(R.id.txt_age)
    TextView txtAge;
    @BindView(R.id.txt_datetime)
    TextView txtDatetime;
    @BindView(R.id.txt_comment)
    TextView txtComment;
    ArrayList<String> images;
    @BindView(R.id.image_container)
    FlowLayout imageContainer;
    @BindDimen(R.dimen.dp_150)
    int dp_150;
    @BindDimen(R.dimen.dp_10)
    int dp_10;
    static String starttime;
    static String endtime;
    static String date;




    public static AppointmentDetailFragment newInstance(String start_time,String end_time, String date) {
        AppointmentDetailFragment fragment = new AppointmentDetailFragment();
        starttime = start_time;
        endtime = end_time;
        date = date;
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        images = new ArrayList<>();

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_appointment_detail;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String id = getPref().getString(KEY_RECORD_ID);

        if(id == null)
            return;


        imageContainer.removeAllViews();

        showProgress("Loading");


        mdcRepository.getMainAppointmentsDetails(id)
                .compose(RxUtil.applySchedulersFlow())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(response-> {
                    hideProgress();
                    if(response.size() <= 0) {
                        return;

                    }
                    txtDatetime.setText(Utils.format12HourDateTime(Utils.UTCTolocalDateTime(response.get(0).createdAt)));
                    txtName.setText(getBaseActivity().getUser().getName());
                    txtAge.setText(response.get(0).yAge);
                    txtGender.setText(getBaseActivity().getUser().gender.equalsIgnoreCase("f") ? "Female" : "Male");
                    txtLocation.setText(getBaseActivity().getUser().city + " " + getBaseActivity().getUser().state + " " + getBaseActivity().getUser().country);
                    txtComment.setText(Html.fromHtml(response.get(0).data));


                },t->  showError(t.getMessage()));



        mdcRepository.getMainAppointmentsImageDetails(id)
                .compose(RxUtil.applySchedulersFlow())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(response -> {

                    hideProgress();

                    if(response.size() <= 0) {
                        return;
                    }

                   for (int i = 0; i < response.size(); i++) {
                       images.add(AppModule.rootimage+response.get(i).image);

                    }


                    int j = 0;
                    for (String image : images) {

                        ImageView imageView = new ImageView(getContext());
                        imageView.setLayoutParams(new FlowLayout.LayoutParams(dp_150,dp_150));
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setTag(j++);

                        imageView.setOnClickListener(v -> {
                            int pos = (int) v.getTag();

                            Log.d(TAG, "onActivityCreated: "+pos);

                            new ImageViewer.Builder(getActivity(), images)
                                    .setStartPosition(pos)
                                    .show();

                        });

                        Picasso.with(getContext()).load(image)
                                .placeholder(R.drawable.image_placeholder).into(imageView);


                        Space space = new Space(getContext());
                        space.setLayoutParams(new FlowLayout.LayoutParams(dp_10,dp_10));

                        imageContainer.addView(imageView);
                        imageContainer.addView(space);

                    }


                    Log.d(TAG, "onNext: " + response);

                },t->  showError(t.getMessage()));
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }



}
