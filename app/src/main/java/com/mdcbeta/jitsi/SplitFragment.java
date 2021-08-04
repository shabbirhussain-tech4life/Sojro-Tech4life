package com.mdcbeta.jitsi;


import android.app.FragmentManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.cases.Appointment1DetailFragment;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.mdcbeta.widget.ActionBar;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import static com.mdcbeta.jitsi.WelcomeActivity.fragmentManager;


public class SplitFragment extends BaseFragment implements BaseDialogFragment.DialogClickListener {
    ImageView  camera, eLab, ePres, eRadi, doctor_note;
    EditText add_comment;
    CardView cardView;
    private String Roomkey, CaseCode, Record_id;
    TextView case_id;
    Space space1;
    FrameLayout frameLayout;
    boolean count = true;
    LinearLayout caseNo, cameraNo, eLabNo, eRadNo, ePresNo, commentNo,comm;
    SplashhhFragment elf;
    String reviewer_id;
    Button submit;
    @Inject
    ApiFactory apiFactory;
    @Inject
    MDCRepository mdcRepository;
//    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            onBroadcastReceived(intent);
//        }
//    };
    public SplitFragment() {

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_split;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);

    }




    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_split, container, false);
reviewer_id = getUser().id;
        cardView = view.findViewById(R.id.card_view);
        comm =view.findViewById(R.id.comm);
        submit =view.findViewById(R.id.comment);
        camera = view.findViewById(R.id.cam_img);
        frameLayout = view.findViewById(R.id.two);

        eLab = view.findViewById(R.id.eLab);
        add_comment = view.findViewById(R.id.add_comment);

        caseNo = view.findViewById(R.id.cases);
        cameraNo = view.findViewById(R.id.cameraNo);
        eLabNo = view.findViewById(R.id.eLabNo);
        eRadNo = view.findViewById(R.id.eRadNo);
        ePresNo = view.findViewById(R.id.ePresNo);
        commentNo = view.findViewById(R.id.commentNo);

        case_id = view.findViewById(R.id.case_rec);
        case_id.setText("Case No: " + CaseCode);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(),add_comment.getText().toString(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(),"hello"+getUser().getId(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(),"time"+Utils.localToUTCDateTime(Utils.getDatetime()),Toast.LENGTH_SHORT).show();
               sendComment();
            }
        });


        commentNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                comm.setVisibility(View.VISIBLE);

            }
        });

//        eLabNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentHandlingActivity().
//                       // addDialog(SplitFragment.this, AddLaboratoryDialog.newInstance(reviewer_id, CaseCode, Record_id));
//            }
//        });
//
//        ePresNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentHandlingActivity().
//                      //  addDialog(SplitFragment.this, AddPrescrptionDialog.newInstance(reviewer_id, CaseCode, Record_id));
//            }
//        });
//
//        eRadNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentHandlingActivity().
//                        addDialog(SplitFragment.this, AddRadiologyDialog.newInstance(reviewer_id, CaseCode, Record_id ));
//                Toast.makeText(getContext(),"rid"+Record_id,Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(),"case"+CaseCode,Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(),"reviewer_id"+reviewer_id,Toast.LENGTH_SHORT).show();
//            }
//        });
        cameraNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                if (count == true) {
//                    frameLayout.setVisibility(View.VISIBLE);
//                    cardView.setVisibility(View.VISIBLE);
//                    //  space1.setVisibility(View.VISIBLE);
//                }
//
//                if (count == false) {
//                    cardView.setVisibility(View.GONE);
//                    frameLayout.setVisibility(View.GONE);
//
//
//                }


               // getFragmentHandlingActivity().addFragmentWithBackstack(new SplashhhFragment());
               FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
                SplashhhFragment splashhhFragment = new SplashhhFragment();

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.two, splashhhFragment).commit();
               // getFragmentManager().beginTransaction().replace(android.R.id.two, new SplashhhFragment()).commit();


            }
        });

        caseNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), Activitycaseview.class);
//                intent.putExtra("room_key", Roomkey);
//                intent.putExtra("case_code", CaseCode);
//                intent.putExtra("record_id", Record_id);
//                startActivity(intent);
                getPref().put(AppPref.Key.KEY_RECORD_ID, Record_id);

                getFragmentHandlingActivity().addFragmentWithBackstack(Appointment1DetailFragment.
                        newInstance(Record_id, CaseCode))
                ;


            }
        });
        return view;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Roomkey = getArguments().getString("key");
        CaseCode = getArguments().getString("case_code");
        Record_id = getArguments().getString("record_id");


//        Intent intent = new Intent(getActivity(), MainActivity_jit.class);
//        intent.putExtra("roomkey", Roomkey);
//        startActivity(intent);



        URL serverURL;
        try {

            serverURL = new URL("https://video.mdconsults.org");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid server URL!");
        }
        JitsiMeetConferenceOptions defaultOptions
                = new JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                // When using JaaS, set the obtained JWT here
                //.setToken("MyJWT")
                .setWelcomePageEnabled(false)
                .build();
        JitsiMeet.setDefaultConferenceOptions(defaultOptions);
      //  registerForBroadcastMessages();


        // Build options object for joining the conference. The SDK will merge the default
        // one we set earlier and this one when joining.
        JitsiMeetConferenceOptions options
                = new JitsiMeetConferenceOptions.Builder()
                .setRoom(Roomkey)
                // .setWelcomePageEnabled(false)
                .setFeatureFlag("chat.enabled",false)
                .setFeatureFlag("invite.enabled",false)
                .setFeatureFlag("pip.enabled",true)
                .setFeatureFlag("fullscreen.enabled",true)
                .setFeatureFlag(" meeting-name.enabled",false)
                .setFeatureFlag("tile-view.enabled",false)
                .build();
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        JitsiMeetActivity.launch(getContext(), options);
//        PictureInPictureParams.Builder builder
//                = new PictureInPictureParams.Builder()
//                .setAspectRatio(new Rational(5, 1);










    }


    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {

    }
    @Override
    public void onDestroy() {
       // LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);

        super.onDestroy();
    }
//    private void registerForBroadcastMessages() {
//        IntentFilter intentFilter = new IntentFilter();
//
//        /* This registers for every possible event sent from JitsiMeetSDK
//           If only some of the events are needed, the for loop can be replaced
//           with individual statements:
//           ex:  intentFilter.addAction(BroadcastEvent.Type.AUDIO_MUTED_CHANGED.getAction());
//                intentFilter.addAction(BroadcastEvent.Type.CONFERENCE_TERMINATED.getAction());
//                ... other events
//         */
//        for (BroadcastEvent.Type type : BroadcastEvent.Type.values()) {
//            intentFilter.addAction(type.getAction());
//        }
//
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, intentFilter);
//    }

    // Example for handling different JitsiMeetSDK events
//    private void onBroadcastReceived(Intent intent) {
//        if (intent != null) {
//            BroadcastEvent event = new BroadcastEvent(intent);
//
//            switch (event.getType()) {
//                case CONFERENCE_JOINED:
//                    Timber.i("Conference Joined with url%s", event.getData().get("url"));
//                    break;
//                case PARTICIPANT_JOINED:
//                    Timber.i("Participant joined%s", event.getData().get("name"));
//                    break;
//            }
//        }
//    }

    // Example for sending actions to JitsiMeetSDK
//    private void hangUp() {
//        Intent hangupBroadcastIntent = BroadcastIntentHelper.buildHangUpIntent();
//        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(hangupBroadcastIntent);
//    }


    void sendComment() {



        if (!TextUtils.isEmpty(add_comment.getText().toString())) {
            //showProgress("Adding comment");


            apiFactory.addCommentsInCase(
                    getUser().getId(),
                    add_comment.getText().toString(),
                    Record_id, Utils.localToUTCDateTime(Utils.getDatetime()))
                    .compose(RxUtil.applySchedulers())
                    .subscribe(data -> {
                        clearFields();
                        //getData();

                    }, throwable -> {
                     //   clearFields();
                        throwable.printStackTrace();
                    });
           }
        else {
            Toast.makeText(getContext(),"Kindly add the comment",Toast.LENGTH_SHORT).show();
        }
        }

    private void clearFields() {

        Toast.makeText(getContext(),"The Doctor Notes have been added to the case",Toast.LENGTH_SHORT).show();
        add_comment.setText("");

}

}

