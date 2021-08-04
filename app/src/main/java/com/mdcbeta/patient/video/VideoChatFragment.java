package com.mdcbeta.patient.video;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.widget.ActionBar;

import butterknife.BindView;

/**
 * Created by Shakil Karim on 4/22/17.
 */

public class VideoChatFragment extends BaseFragment {

    @BindView(R.id.webview)
    WebView webview;
    private static final String TAG = "VideoChatFragment";
    private String mcode;
    private boolean isforgetPassword;


    public static VideoChatFragment newInstance(String code,boolean isforgetPassword) {
        Bundle args = new Bundle();
        //args.putString("url",url);
        args.putString("code",code);
        args.putBoolean("isforgetPassword",isforgetPassword);
        VideoChatFragment fragment = new VideoChatFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            //mUrl =  getArguments().getString("url");
            mcode = getArguments().getString("code");
            isforgetPassword = getArguments().getBoolean("isforgetPassword");
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // Log.d(TAG, "https://mdconsults.org/live/"+mUrl);


        setUpWebViewDefaults(webview);


        if(isforgetPassword){
            webview.loadUrl("https://mdconsults.org/password/reset");
        }else {
            webview.loadUrl("https://mdconsults.org/live/"+mcode);
        }

        webview.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d(TAG, "onPermissionRequest");
                getActivity().runOnUiThread(() -> {
//                    if(request.getOrigin().toString().equals("https://apprtc-m.appspot.com/")) {
//                        request.grant(request.getResources());
//                    } else {
//                        request.deny();
//                    }
                    request.grant(request.getResources());
                });
            }

        });

//        webview.setWebViewClient(new WebViewClient(){
//            ProgressDialog progressDialog;
//
//
//            //Show loader on url load
//            public void onLoadResource (final WebView view, String url) {
//                if (progressDialog == null) {
//                    // in standard case YourActivity.this
//                    progressDialog = new ProgressDialog(view.getContext());
//                    progressDialog.setMessage("Loading...");
//                    progressDialog.show();
//                }
//            }
//            public void onPageFinished(WebView view, String url) {
//                try{
//                    if (progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                        progressDialog = null;
//                    }
//                }catch(Exception exception){
//                    exception.printStackTrace();
//                }
//            }
//
//
//
//
//        });
//        WebSettings webSettings = webview.getSettings();
//        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_video_chat;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();

        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(true);

        // Allow use of Local Storage
        settings.setDomStorageEnabled(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }

        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.setWebViewClient(new WebViewClient());

//        // AppRTC requires third party cookies to work
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.setAcceptThirdPartyCookies(webview, true);
    }


}
