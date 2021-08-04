package com.mdcbeta.jitsi;


import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


import com.mdcbeta.R;
import com.mdcbeta.base.FragmentsHandlerActivity;
import com.mdcbeta.di.AppComponent;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetFragment;
import org.jitsi.meet.sdk.JitsiMeetView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class WelcomeActivity extends FragmentsHandlerActivity {
    public static FragmentManager fragmentManager;
    private String Roomname, CaseCode, Record_id;
    private static final String[] REQUIRED_PERMISSION_LIST = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    private static final int REQUEST_CODE = 1;
    private List<String> mMissPermissions = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        fragmentManager = getSupportFragmentManager();
        Intent intent = getIntent();
        Roomname = intent.getStringExtra("room_key");
        CaseCode = intent.getStringExtra("case_code");
        Record_id = intent.getStringExtra("record_id");


/*
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



        // Build options object for joining the conference. The SDK will merge the default
        // one we set earlier and this one when joining.
        JitsiMeetConferenceOptions options
                = new JitsiMeetConferenceOptions.Builder()
                .setRoom(Roomname)
                // .setWelcomePageEnabled(false)
                .setFeatureFlag("chat.enabled",false)
                .setFeatureFlag("invite.enabled",false)
                .setFeatureFlag("pip.enabled",true)
                .setFeatureFlag("fullscreen.enabled",false)
                .build();
        // Launch the new activity with the given options. The launch() method takes care
        // of creating the required Intent and passing the options.
        JitsiMeetActivity.launch(getApplicationContext(), options);


 */
        if (findViewById(R.id.fragment_container1) != null) {
            if (savedInstanceState != null) {
                return;
            }

//            if (isVersionM()) {
//                checkAndRequestPermissions();
//            } else {
//                startMainActivity();
//            }
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////            SplitFragment splitFragment = new SplitFragment();
////            fragmentTransaction.add(R.id.fragment_container1, splitFragment, null);
////            fragmentTransaction.commit();

//            Toast.makeText(getApplicationContext(),"case"+CaseCode,Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(),"room"+Roomname,Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(),"record id"+Record_id,Toast.LENGTH_SHORT).show();

//
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundleobj = new Bundle();
            bundleobj.putCharSequence("key", Roomname);
            bundleobj.putCharSequence("case_code", CaseCode);
            bundleobj.putCharSequence("record_id", Record_id);
            SplitFragment splitFragment = new SplitFragment();
            splitFragment.setArguments(bundleobj);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.fragment_container1, splitFragment).commit();



//            FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
//            Bundle bundleobj1 = new Bundle();
//            bundleobj1.putCharSequence("key", Roomname);
//
//            JitsiFragment jitsiFragment = new JitsiFragment();
//         //   jitsiFragment.setArguments(bundleobj1);
//            fragmentTransaction1.addToBackStack(null);
//            fragmentTransaction1.replace(R.id.fragment_container2, jitsiFragment).commit();
//

        }

    }

    @Override
    public void performInjection(AppComponent appComponent) {

    }

    @Override
    protected int fragmentContainerId() {
        return R.id.fragment_container1;
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
////        Intent intent = new Intent(this, MainHealthProviderActivity.class);
////        startActivity(intent);
//        replaceFragment(HealthProviderAppointmentFragment.newInstance());
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void checkAndRequestPermissions() {
        //   Toast.makeText(getContext(),"I am permission",Toast.LENGTH_SHORT).show();
        mMissPermissions.clear();
        for (String permission : REQUIRED_PERMISSION_LIST) {
            int result = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                mMissPermissions.add(permission);
            }
        }
        // check permissions has granted
        if (mMissPermissions.isEmpty()) {
            startMainActivity();
        } else {
            ActivityCompat.requestPermissions(this,
                    mMissPermissions.toArray(new String[mMissPermissions.size()]),
                    REQUEST_CODE);
        }
    }

    private void startMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//ExternalUSBCameraFragment externalUSBCameraFragment;
//                final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
//                externalUSBCameraFragment = new ExternalUSBCameraFragment();
//                ft.replace(R.id.fragment_container_splash, externalUSBCameraFragment);
//                ft.addToBackStack(null);
//                ft.commit();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                ExternalUSBCameraFragment externalUSBCameraFragment = new ExternalUSBCameraFragment();
//                fragmentTransaction.add(R.id.fragment_container_splash, externalUSBCameraFragment, null);
//                fragmentTransaction.commit();
//                startActivity(new Intent(getActivity(), USBCameraActivity.class));
//              getActivity().finish();

                Toast.makeText(getApplicationContext(),"MOVE",Toast.LENGTH_SHORT).show();

//                Fragment newFragment = new ExternalUSBCameraFragment();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//// Replace whatever is in the fragment_container view with this fragment,
//// and add the transaction to the back stack
//                transaction.replace(R.id.fragment_container_view2, newFragment);
//                transaction.addToBackStack(null);
//
//// Commit the transaction
//                transaction.commit();
                getSupportFragmentManager().beginTransaction()
                        .add(android.R.id.content, new ExternalUSBCameraFragment ()).commit();
                Toast.makeText(getApplicationContext(),"EXTERNAL ",Toast.LENGTH_SHORT).show();
            }
        }, 3000);
    }

    private boolean isVersionM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}