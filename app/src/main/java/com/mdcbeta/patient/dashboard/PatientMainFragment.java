package com.mdcbeta.patient.dashboard;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mdcbeta.Events;
import com.mdcbeta.R;

import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PatientViewModel;
import com.mdcbeta.patient.dashboard.adapter.PatientAppointmentAdapter;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.RxBus;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.VerticalSpaceItemDecoration;
import com.mdcbeta.widget.ActionBar;
import com.trello.rxlifecycle2.android.FragmentEvent;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.mdcbeta.R.color.black;
import static com.mdcbeta.R.color.dark_green;
import static com.mdcbeta.R.color.light_green;
import static com.mdcbeta.R.color.white;


public class PatientMainFragment extends BaseFragment {


    @BindView(R.id.main_list)
    RecyclerView mainList;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;

    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.btn_appointment)
    Button btn_appointment;
    private static final String TAG = "PatientMainFragment";
    @Inject
    ApiFactory apiFactory;
    @Inject
    RxBus rxBus;
    @BindView(R.id.empty_view)
    TextView emptyView;
    //  @Inject
    // MDCRepository repository;


    //   private DBMainAppointmentAdapter mainAppointmentAdapter;
    private PatientViewModel patientViewModel;
    private PatientAppointmentAdapter adapter;


    public PatientMainFragment() {
        // Required empty public constructor
    }

    public static PatientMainFragment newInstance() {
        PatientMainFragment fragment = new PatientMainFragment();
        return fragment;
    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_appointment.setVisibility(View.GONE);
        initList();
    }

    private void initList() {
        adapter = new PatientAppointmentAdapter(getContext());
        mainList.setAdapter(adapter);
        mainList.setHasFixedSize(true);
        mainList.setItemAnimator(new DefaultItemAnimator());
        mainList.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        mainList.setNestedScrollingEnabled(false);
        mainList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemClickListner(item -> {


            getPref().put(AppPref.Key.KEY_RECORD_ID, item.get("record_id").getAsString());
            Log.d("Record id:  ", item.get("record_id").toString());
            getFragmentHandlingActivity()
                    .addFragmentWithBackstack(AppointmentDetailFragment.
                            newInstance(item.get("start_time").getAsString(), item.get("end_time").getAsString(), item.get("date").getAsString()));

            //      Toast.makeText(getContext(), item.get("record_id").getAsString(), Toast.LENGTH_LONG ).show();

        });


        adapter.setVideoClickListner(item -> {

            if (item.get("status").getAsString().equals("Booked")) {

//                if (Utils.isChromeCustomTabsSupported(getContext()))
//                    customTabsIntent.launchUrl(getContext(), Uri.parse("https://mdconsults.org/liveapi/" + item.get("room_url").getAsString()));
//                else {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mdconsults.org/liveapi/" + item.get("room_url").getAsString()));
//                    startActivity(intent);
//                }

                connectToRoom(item.get("room_url").getAsString(), false, false, 0);


            } else {
                Toast.makeText(getContext(), "Appointment cancelled", Toast.LENGTH_LONG).show();
            }

        });

    }

    private void connectToRoom(String roomUrl, boolean loopback,
                               boolean useValuesFromIntent, int runTimeMs) {

        // Video call enabled flag.
        boolean videoCallEnabled = true;

        // Use screencapture option.
        boolean useScreencapture = false;

        // Use Camera2 option.
        boolean useCamera2 = true;

        // Get default codecs.
        String videoCodec = getString(R.string.pref_videocodec_default);
        String audioCodec = getString(R.string.pref_audiocodec_default);

        // Check HW codec flag.
        boolean hwCodec = true;

        // Check Capture to texture.
        boolean captureToTexture = true;

        // Check FlexFEC.
        boolean flexfecEnabled = false;

        // Check Disable Audio Processing flag.
        boolean noAudioProcessing = false;

        // Check Disable Audio Processing flag.
        boolean aecDump = false;

        // Check OpenSL ES enabled flag.
        boolean useOpenSLES = false;

        // Check Disable built-in AEC flag.
        boolean disableBuiltInAEC = false;

        // Check Disable built-in AGC flag.
        boolean disableBuiltInAGC = false;

        // Check Disable built-in NS flag.
        boolean disableBuiltInNS = false;

        // Check Enable level control.
        boolean enableLevelControl = false;

        // Check Disable gain control
        boolean disableWebRtcAGCAndHPF = true;

        // Get video resolution from settings.
        //640 x 480 and 1280 x 720
        int videoWidth = 640;
        int videoHeight = 480;

        // Get camera fps from settings.
        int cameraFps = 30;

        // Check capture quality slider flag.
        boolean captureQualitySlider = false;

        // Get video and audio start bitrate.
        int videoStartBitrate = 1700;

        int audioStartBitrate = 32;

        // Check statistics display option.
        boolean displayHud = false;

        boolean tracing = false;

        // Get datachannel options
        boolean dataChannelEnabled = true;

        boolean ordered = true;

        boolean negotiated = false;

        int maxRetrMs = -1;

        int maxRetr = -1;

        int id = -1;

        String protocol = "";

/*
        Intent intent = new Intent(getContext(), CallActivity.class);
        //roomId is room/consultation name, pass this value to CallActivity
        intent.putExtra(CallActivity.EXTRA_ROOMID, roomUrl);
        intent.putExtra(CallActivity.EXTRA_LOOPBACK, loopback);
        intent.putExtra(CallActivity.EXTRA_VIDEO_CALL, videoCallEnabled);
        intent.putExtra(CallActivity.EXTRA_SCREENCAPTURE, useScreencapture);
        intent.putExtra(CallActivity.EXTRA_CAMERA2, useCamera2);
        intent.putExtra(CallActivity.EXTRA_VIDEO_WIDTH, videoWidth);
        intent.putExtra(CallActivity.EXTRA_VIDEO_HEIGHT, videoHeight);
        intent.putExtra(CallActivity.EXTRA_VIDEO_FPS, cameraFps);
        intent.putExtra(CallActivity.EXTRA_VIDEO_CAPTUREQUALITYSLIDER_ENABLED, captureQualitySlider);
        intent.putExtra(CallActivity.EXTRA_VIDEO_BITRATE, videoStartBitrate);
        intent.putExtra(CallActivity.EXTRA_VIDEOCODEC, videoCodec);
        intent.putExtra(CallActivity.EXTRA_HWCODEC_ENABLED, hwCodec);
        intent.putExtra(CallActivity.EXTRA_CAPTURETOTEXTURE_ENABLED, captureToTexture);
        intent.putExtra(CallActivity.EXTRA_FLEXFEC_ENABLED, flexfecEnabled);
        intent.putExtra(CallActivity.EXTRA_NOAUDIOPROCESSING_ENABLED, noAudioProcessing);
        intent.putExtra(CallActivity.EXTRA_AECDUMP_ENABLED, aecDump);
        intent.putExtra(CallActivity.EXTRA_OPENSLES_ENABLED, useOpenSLES);
        intent.putExtra(CallActivity.EXTRA_DISABLE_BUILT_IN_AEC, disableBuiltInAEC);
        intent.putExtra(CallActivity.EXTRA_DISABLE_BUILT_IN_AGC, disableBuiltInAGC);
        intent.putExtra(CallActivity.EXTRA_DISABLE_BUILT_IN_NS, disableBuiltInNS);
        intent.putExtra(CallActivity.EXTRA_ENABLE_LEVEL_CONTROL, enableLevelControl);
        intent.putExtra(CallActivity.EXTRA_DISABLE_WEBRTC_AGC_AND_HPF, disableWebRtcAGCAndHPF);
        intent.putExtra(CallActivity.EXTRA_AUDIO_BITRATE, audioStartBitrate);
        intent.putExtra(CallActivity.EXTRA_AUDIOCODEC, audioCodec);
        intent.putExtra(CallActivity.EXTRA_DISPLAY_HUD, displayHud);
        intent.putExtra(CallActivity.EXTRA_TRACING, tracing);
        intent.putExtra(CallActivity.EXTRA_CMDLINE, false);
        intent.putExtra(CallActivity.EXTRA_RUNTIME, runTimeMs);
        intent.putExtra(CallActivity.EXTRA_DATA_CHANNEL_ENABLED, dataChannelEnabled);

        if (dataChannelEnabled) {
            intent.putExtra(CallActivity.EXTRA_ORDERED, ordered);
            intent.putExtra(CallActivity.EXTRA_MAX_RETRANSMITS_MS, maxRetrMs);
            intent.putExtra(CallActivity.EXTRA_MAX_RETRANSMITS, maxRetr);
            intent.putExtra(CallActivity.EXTRA_PROTOCOL, protocol);
            intent.putExtra(CallActivity.EXTRA_NEGOTIATED, negotiated);
            intent.putExtra(CallActivity.EXTRA_ID, id);
        }
        startActivity(intent);

 */
    }

    @Override
    public void onStart() {
        super.onStart();
        rxBus.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(FragmentEvent.STOP))
                .subscribe(o -> {

                    if (o instanceof Events.Start) {
                        // progressContainer.setVisibility(View.VISIBLE);
                        showProgress("Booking an appointment...");
                    }

                    if (o instanceof Events.Progress) {
                        int pro = ((Events.Progress) o).getProgress();
                        //progressBar.setProgress(pro);
                        //progressText.setText(pro+"%");
                    }

                    if (o instanceof Events.Success) {
                        //progressContainer.setVisibility(View.GONE);
                        showSuccess("Appointment booked successfully");
                        getAppointmentforReferrer();
                    }
                    if (o instanceof Events.Failed) {
                        showError(((Events.Failed) o).getMessage());
                    }

                });

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_appointments;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        button1.setBackgroundColor(getResources().getColor(dark_green));
        button2.setBackgroundColor(getResources().getColor(light_green));
        button1.setTextColor(getResources().getColor(white));
        button2.setTextColor(getResources().getColor(black));

        showProgress("Loading");
        getAppointmentforReferrer();

    }

    @Override
    public void onDestroyView() {
        mainList.setAdapter(null);
        super.onDestroyView();

    }


    @OnClick({R.id.button1, R.id.button2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button1:
                button1.setBackgroundColor(getResources().getColor(dark_green));
                button2.setBackgroundColor(getResources().getColor(light_green));
                button1.setTextColor(getResources().getColor(white));
                button2.setTextColor(getResources().getColor(black));

                showProgress("Loading");
                getAppointmentforReferrer();

                break;
            case R.id.button2:
                button1.setBackgroundColor(getResources().getColor(light_green));
                button2.setBackgroundColor(getResources().getColor(dark_green));
                button1.setTextColor(getResources().getColor(black));
                button2.setTextColor(getResources().getColor(white));

                showProgress("Loading");
                getAppointmentforReferrerPrevious();

                break;
        }
    }


    public void getAppointmentforReferrer() {
        showProgress("Loading");

        apiFactory.getAppointmentfordoctors(getUser().getId())
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model -> {

                    if (model.status) {
                        hideProgress();
                        adapter.swap(model.getData());
                        if (model.getData().equals(" ")) {
                            showError(model.getMessage());
                        }
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                        showError(model.getMessage());
                    }

                }, throwable -> showError(throwable.getMessage()));
    }


    public void getAppointmentforReferrerPrevious() {
        showProgress("Loading");

        apiFactory.getAppointmentfordoctorsPrevious(getUser().getId())
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model -> {

                    if (model.status) {
                        hideProgress();
                        adapter.swap(model.getData());

                        if (model.getData().equals(null)) {
                            showError(model.getMessage());
                        }
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                        showError(model.getMessage());
                    }

                }, throwable -> showError(throwable.getMessage()));
    }
}



