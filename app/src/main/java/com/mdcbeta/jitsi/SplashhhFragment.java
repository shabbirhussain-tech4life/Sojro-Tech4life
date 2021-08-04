package com.mdcbeta.jitsi;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.mdcbeta.R;

import java.util.ArrayList;
import java.util.List;

import static com.mdcbeta.jitsi.WelcomeActivity.fragmentManager;


public class SplashhhFragment extends Fragment {
    private static final String[] REQUIRED_PERMISSION_LIST = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    private static final int REQUEST_CODE = 1;
    private List<String> mMissPermissions = new ArrayList<>();


    public SplashhhFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splashhh, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //   getActivity().setContentView(R.layout.activity_splash_jitsi);

        if (isVersionM()) {
            checkAndRequestPermissions();
        } else {
            startMainActivity();
        }
    }

    private void checkAndRequestPermissions() {
     //   Toast.makeText(getContext(),"I am permission",Toast.LENGTH_SHORT).show();
        mMissPermissions.clear();
        for (String permission : REQUIRED_PERMISSION_LIST) {
            int result = ContextCompat.checkSelfPermission(getContext(), permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                mMissPermissions.add(permission);
            }
        }
        // check permissions has granted
        if (mMissPermissions.isEmpty()) {
            startMainActivity();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
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

                Toast.makeText(getContext(),"MOVE",Toast.LENGTH_SHORT).show();

                Fragment newFragment = new ExternalUSBCameraFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack
                transaction.replace(R.id.fragment_container_view2, newFragment);
                transaction.addToBackStack(null);

// Commit the transaction
                transaction.commit();
                Toast.makeText(getContext(),"EXTERNAL ",Toast.LENGTH_SHORT).show();
            }
        }, 3000);
    }

    private boolean isVersionM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}

