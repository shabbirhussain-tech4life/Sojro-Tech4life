package com.mdcbeta.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.di.DaggerAppComponent;
import com.squareup.picasso.Picasso;


public class MyApplication extends Application {


    private AppComponent mAppComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        // by kanwal khan

        mAppComponent =  DaggerAppComponent.builder()
                        .appModule(new AppModule(this))
                        .build();

        Fresco.initialize(this);



        //mapiComponent = Dag

        Picasso picasso = new Picasso.Builder(getApplicationContext())
                .downloader(new OkHttp3Downloader(this))
                .build();

        //picasso.setIndicatorsEnabled(true);


        try {
            Picasso.setSingletonInstance(picasso);
        } catch (IllegalStateException ignored) {
            // Picasso instance was already set
            // cannot set it after Picasso.with(Context) was already in use
        }
    }
// by kk


    public AppComponent getAppComponent() {
        return mAppComponent;
    }




}
