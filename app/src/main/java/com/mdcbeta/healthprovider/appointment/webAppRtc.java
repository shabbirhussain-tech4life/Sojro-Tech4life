package com.mdcbeta.healthprovider.appointment;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import androidx.fragment.app.Fragment;

import com.mdcbeta.R;

import static android.content.ContentValues.TAG;


public class webAppRtc extends Fragment {


    public webAppRtc() {
        // Required empty public constructor
    }

 //   String url="https://www.mdconsults.org/liveapi/\"+ (\"room_url\")";





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {






        // Inflate the layout for this fragment
Bundle bundle = getArguments();
String data = bundle.getString("key");
        View view = inflater.inflate(R.layout.fragment_web_app_rtc, container, false);





        WebView webView = (WebView)view.findViewById(R.id.webview);
        webView.loadUrl(data);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }



       webView.setWebViewClient(new WebViewClient());

//        webView.setWebChromeClient(new WebChromeClient(){
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onPermissionRequest(final PermissionRequest request) {
//                request.grant(request.getResources());
//            }
//        });
//        webView.setWebChromeClient(new WebChromeClient(){
//            // Need to accept permissions to use the camera
//            @Override
//            public void onPermissionRequest(final PermissionRequest request) {
//                Log.e("","onPermissionRequest");
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    request.grant(request.getResources());
//                }
//            }
//        });
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.d(TAG, "onPermissionRequest");
                getActivity().runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void run() {
                        if(request.getOrigin().toString().equals(data)) {
                            request.grant(request.getResources());
                        } else {
                            request.deny();
                        }
                    }
                });
            }

        });
       return view;


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

        // AppRTC requires third party cookies to work
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptThirdPartyCookies(webView, true);
    }







    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
