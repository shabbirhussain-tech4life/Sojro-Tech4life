package com.mdcbeta.healthprovider.appointment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mdcbeta.R;

import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.MainHealthProviderActivity;
import com.mdcbeta.healthprovider.cases.Appointment1DetailFragment;
import com.mdcbeta.jitsi.WelcomeActivity;
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

import static com.mdcbeta.R.color.white;


public class HealthProviderAppointmentFragment extends BaseFragment {

    @BindView(R.id.main_list)
    RecyclerView mainList;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.btn_appointment)
    Button btn_appointment;
    @BindView(R.id.button2)
    Button button2;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    @Inject
    ApiFactory apiFactory;
    private HealthProviderAppointmentAdapter adapter;
    private String tempRoomUrl, tempRoomId;


    @Inject
    RxBus rxBus;
    private final int REQUIRED_PERMISSIONS_CODE = 200;

    public HealthProviderAppointmentFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        HealthProviderAppointmentFragment fragment = new HealthProviderAppointmentFragment();
        return fragment;


    }




    @Override
    public int getLayoutID() {
        return R.layout.fragment_appointments;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent)
    {
        appComponent.inject(this);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    //  deleteCache(getContext());
//        button1.setBackgroundColor(getResources().getColor(dark_green));
//        button2.setBackgroundColor(getResources().getColor(light_green));
        button1.setBackgroundResource(R.drawable.toogle_on);
        button2.setBackgroundResource(R.drawable.toogle_off);
        button1.setTextColor(getResources().getColor(white));
        button2.setTextColor(getResources().getColor(white));
//        if (MainHealthProviderActivity.isReviewer) {
//          btn_appointment.setVisibility(View.GONE);
//            showProgress("Loading");
//            getAppointmentforReviewer();
//        } else {
//            getAppointmentforReferrer();
//        }

      getAppointmentforReferrer();

    }

    public void getAppointmentforReferrer() {
        showProgress("Loading");

        apiFactory.getAppointmentforHealthProvider(getUser().getId())
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model -> {

                    if (model.status) {
                        hideProgress();
                        adapter.swap(model.getData());
                    } else {
                        showError(model.getMessage());
                    }

                }, throwable -> showError(throwable.getMessage()));
    }


    public void getAppointmentforReferrerPrevious() {
        showProgress("Loading");

        apiFactory.getAppointmentforHealthProviderPrevious(getUser().getId())
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model -> {

                    if (model.status) {
                        hideProgress();
                        adapter.swap(model.getData());
                    } else {
                        showError(model.getMessage());
                    }

                }, throwable -> showError(throwable.getMessage()));
    }


    public void getAppointmentforReviewer() {
        showProgress("Loading");

        apiFactory.getAppointmentforReviewer(getUser().getId())
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model -> {

                    if (model.status) {
                        hideProgress();
                        adapter.swap(model.getData());
                    } else {
                        showError(model.getMessage());
                    }

                }, throwable -> showError(throwable.getMessage()));
    }

    public void getAppointmentforReviewerPrevious() {
        showProgress("Loading");

        apiFactory.getAppointmentforReviewerPrevious(getUser().getId())
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model -> {

                    if (model.status) {
                        hideProgress();
                        adapter.swap(model.getData());
                    } else {
                        showError(model.getMessage());
                    }

                }, throwable -> showError(throwable.getMessage()));
    }


    private void initList() {
        adapter = new HealthProviderAppointmentAdapter(getContext());
        mainList.setAdapter(adapter);
        mainList.setHasFixedSize(true);
        mainList.setItemAnimator(new DefaultItemAnimator());
        mainList.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        mainList.setNestedScrollingEnabled(false);
        mainList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setItemClickListner(item -> {


            getPref().put(AppPref.Key.KEY_RECORD_ID, item.get("record_id").getAsString());
            Log.d("Record id:  ", item.get("record_id").toString());
            getFragmentHandlingActivity().replaceFragmentWithBackstack(Appointment1DetailFragment.
                   newInstance(item.get("record_id").getAsString(), item.get("case_code").getAsString()));
//            getFragmentHandlingActivity().addFragmentWithBackstack(Appointment1DetailFragment.
//                    newInstance(item.get("record_id").getAsString(), item.get("case_code").getAsString()));

        });

//        adapter.setVideoClickListner(item -> {
//          // For WebView
//        //  String value = "https://mdconsults.org/sojroapi/liveapi/" + item.get("room_url").getAsString();
//          String value = "https://www.new.mdconsults.org/liveapi/" + item.get("room_url").getAsString();
//          Fragment fragment = new webAppRtc();
//          Bundle bundle = new Bundle();
//          bundle.putString("key", value);
//          fragment.setArguments(bundle);
//          android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//          fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
//
////            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) !=
////                    PackageManager.PERMISSION_GRANTED ||
////                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH) !=
////                            PackageManager.PERMISSION_GRANTED ||
////                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_ADMIN) !=
////                            PackageManager.PERMISSION_GRANTED ||
////                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
////                            PackageManager.PERMISSION_GRANTED ||
////                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
////                            PackageManager.PERMISSION_GRANTED ||
////                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) !=
////                            PackageManager.PERMISSION_GRANTED ||
////                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_NETWORK_STATE) !=
////                            PackageManager.PERMISSION_GRANTED ||
////                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.MODIFY_AUDIO_SETTINGS) !=
////                            PackageManager.PERMISSION_GRANTED ||
////            //added by kanwal khan
////            ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
////                    PackageManager.PERMISSION_GRANTED
////
////            )
////
//
//
//
//
//
//
//
//
////            {
////                tempRoomUrl = item.get("room_url").getAsString();
////                tempRoomId = item.get("record_id").getAsString();
////                CallFragment.recordId = item.get("record_id").getAsString();
////                CallFragment.caseCode = item.get("case_code").getAsString();
////
////                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_NETWORK_STATE,
////                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
////                        Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
////                        Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.CAMERA}, REQUIRED_PERMISSIONS_CODE);
////            } else {
////                CallFragment.recordId = item.get("record_id").getAsString();
////                CallFragment.caseCode = item.get("case_code").getAsString();
////
////                connectToRoom(item.get("room_url").getAsString(), false, false, 0,
////                        item.get("record_id").getAsString());
////            }
//        });
      adapter.setVideoClickListner(item -> {

//         //   connectToRoom(item.get("room_url").getAsString(), false, false, 0);
//        Intent intent = new Intent(Intent.ACTION_VIEW,
//          Uri.parse("https://mdconsults.org/liveapisojro/"+item.get("room_url").getAsString()));
//        startActivity(intent);

          Intent intent = new Intent(getActivity(), WelcomeActivity.class);
          intent.putExtra("room_key",item.get("room_url").getAsString());
          intent.putExtra("case_code",item.get("case_code").getAsString());
          intent.putExtra("record_id",item.get("record_id").getAsString());
          startActivity(intent);

      });

    }

    private void connectToRoom(String roomUrl, boolean loopback,
                               boolean useValuesFromIntent, int runTimeMs, String record_id) {

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
        intent.putExtra("record_id", record_id);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();


    }


    @OnClick({R.id.button1, R.id.button2, R.id.btn_appointment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button1:
//                button1.setBackgroundColor(getResources().getColor(dark_green));
//                button2.setBackgroundColor(getResources().getColor(light_green));
                button1.setBackgroundResource(R.drawable.toogle_on);
                button2.setBackgroundResource(R.drawable.toogle_off);
                button1.setTextColor(getResources().getColor(white));
                button2.setTextColor(getResources().getColor(white));
                if (MainHealthProviderActivity.isReviewer) {
                    showProgress("Loading");
                    getAppointmentforReviewer();
                } else {
                    getAppointmentforReferrer();
                }
                break;
            case R.id.button2:
//                button1.setBackgroundColor(getResources().getColor(light_green));
//                button2.setBackgroundColor(getResources().getColor(dark_green));
                button2.setBackgroundResource(R.drawable.toogle_on);
                button1.setBackgroundResource(R.drawable.toogle_off);
                button1.setTextColor(getResources().getColor(white));
                button2.setTextColor(getResources().getColor(white));
                if (MainHealthProviderActivity.isReviewer) {
                    showProgress("Loading");
                    getAppointmentforReviewerPrevious();
                } else {
                    getAppointmentforReferrerPrevious();
                }
                break;
            case R.id.btn_appointment:
                //  Toast.makeText(this.getContext(), "Demo", Toast.LENGTH_SHORT).show();
                getFragmentHandlingActivity().addFragmentWithBackstack(AppointmentFragment.newInstance());


                break;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUIRED_PERMISSIONS_CODE && grantResults.length > 0) {
            connectToRoom(tempRoomUrl, false, false, 0, tempRoomId);
        } else {
            Toast.makeText(getContext(), "Permission required to use this feature", Toast.LENGTH_SHORT).show();
        }
    }
//  public static void deleteCache(Context context) {
//    try {
//      File dir = context.getCacheDir();
//      deleteDir(dir);
//    } catch (Exception e) { e.printStackTrace();}
//  }

//  public static boolean deleteDir(File dir) {
//    if (dir != null && dir.isDirectory()) {
//      String[] children = dir.list();
//      for (int i = 0; i < children.length; i++) {
//        boolean success = deleteDir(new File(dir, children[i]));
//        if (!success) {
//          return false;
//        }
//      }
//      return dir.delete();
//    } else if(dir!= null && dir.isFile()) {
//      return dir.delete();
//    } else {
//      return false;
//    }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);

  }
}

//  @Override
//  public void onStart() {
//    super.onStart();
//   // deleteCache(getContext());
//  }

