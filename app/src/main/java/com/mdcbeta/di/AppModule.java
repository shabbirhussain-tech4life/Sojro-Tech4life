package com.mdcbeta.di;

import android.app.Application;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.mdcbeta.data.local.Doa.MainAppointmentDetailDoa;
import com.mdcbeta.data.local.Doa.MainAppointmentDoa;
import com.mdcbeta.data.local.MDCDatabase;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.util.RxBus;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shakil Karim on 4/9/17.
 */

@Module
public class AppModule {


  public static String baseurl = "https://mdconsults.org/MDCAPI/public/sojroapi/";
  public static String UPLOAD_URL = "https://mdconsults.org/MDCAPI/public/salman_multiple.php";
  // public static String UPLOAD_URLS = "https://";
  public static String UPLOAD_URL1 = "https://mdconsults.org/MDCAPI/public/sojroapi/salman.php";
  public static String UPLOAD_URL2 = "https://mdconsults.org/MDCAPI/public/labimage.php";
  public static String rootimage = "https://mdconsults.org/MDCAPI/public/opt_case_images/";
  public static String radimage = "https://mdconsults.org/sojroapi/public/radiology/";
  public static String labimage = "https://mdconsults.org/MDCAPI/public/lab/";
  public static String profileimage = "https://mdconsults.org/MDCAPI/public/profile_Images/";
  public static String commentimage = "https://mdconsults.org/MDCAPI/public/comment_files/";
  //  public static String emailUrl = "https://webmdconsults.mdconsults.org/MDCAPI/public/sojroapi/"+"email2.php?";
  // public static String emailUrl = "https://webmdconsults.mdconsults.org/MDCAPI/public/email2.php?";
  public static String emailUrl = baseurl + "email2.php?";
  public static String UPLOAD_COMMENT_IMAGES_URL = "https://mdconsults.org/MDCAPI/public/comment_image.php";





//  public static String baseurl = "https://webmdconsults.mdconsults.org/MDCAPI/public/sojroapi/";
//  public static String UPLOAD_URL = "https://webmdconsults.mdconsults.org/MDCAPI/public/salman_multiple.php";
// // public static String UPLOAD_URLS = "https://";
//  public static String UPLOAD_URL1 = "https://webmdconsults.mdconsults.org/MDCAPI/public/sojroapi/salman.php";
//  public static String UPLOAD_URL2 = "https://webmdconsults.mdconsults.org/MDCAPI/public/labimage.php";
//  public static String rootimage = "https://webmdconsults.mdconsults.org/MDCAPI/public/opt_case_images/";
//  public static String radimage = "https://webmdconsults.mdconsults.org/sojroapi/public/radiology/";
//  public static String labimage = "https://webmdconsults.mdconsults.org/MDCAPI/public/lab/";
//  public static String profileimage = "https://webmdconsults.mdconsults.org/MDCAPI/public/profile_Images/";
//  public static String commentimage = "https://webmdconsults.mdconsults.org/MDCAPI/public/comment_files/";
////  public static String emailUrl = "https://webmdconsults.mdconsults.org/MDCAPI/public/sojroapi/"+"email2.php?";
// // public static String emailUrl = "https://webmdconsults.mdconsults.org/MDCAPI/public/email2.php?";
//public static String emailUrl = baseurl + "email2.php?";
//  public static String UPLOAD_COMMENT_IMAGES_URL = "https://webmdconsults.mdconsults.org/MDCAPI/public/comment_image.php";



    //   public static String baseurl = "http://smg.mdconsults.org/MDCAPI/maha/";
 //   public static String UPLOAD_URL = "http://smg.mdconsults.org/MDCAPI/maha/salman_multiple.php";
 //   public static String rootimage = "http://smg.mdconsults.org/MDCAPI/maha/opt_case_images/";
 //   public static String profileimage = "http://smg.mdconsults.org/MDCAPI/maha/opt_case_images/";
//
//   public static String baseurl = "https://mdconsults.org/sojroapi/public/";
//  public static String UPLOAD_URL = "https://mdconsults.org/sojroapi/public/salman_multiple.php";
//  public static String UPLOAD_URL1 = "https://mdconsults.org/sojroapi/public/salman.php";
//  public static String UPLOAD_URL2 = "https://mdconsults.org/sojroapi/public/labimage.php";
//  public static String rootimage = "https://mdconsults.org/sojroapi/public/opt_case_images/";
//  public static String radimage = "https://mdconsults.org/sojroai/public/radiology/";
//  public static String labimage = "https://mdconsults.org/sojroapi/public/lab/";
//  public static String profileimage = "https://mdconsults.org/sojroapi/public/profile_Images/";
//  public static String commentimage = "https://mdconsults.org/sojroapi/public/comment_files/";
//  public static String emailUrl = baseurl + "email2.php?";
//  public static String UPLOAD_COMMENT_IMAGES_URL = baseurl + "comment_image.php";



//    public static String baseurl = "https://new.mdconsults.org/MDCAPI/public/Sojro/";
//    public static String UPLOAD_URL = "https://new.mdconsults.org/MDCAPI/public/salman_multiple.php";
//    public static String UPLOAD_URL1 = "https://new.mdconsults.org/MDCAPI/public/salman.php";
//    public static String UPLOAD_URL2 = "https://new.mdconsults.org/MDCAPI/public/labimage.php";
//    public static String rootimage = "https://new.mdconsults.org/MDCAPI/public/opt_case_images/";
//    public static String radimage = "https://new.mdconsults.org/MDCAPI/public/radiology/";
//    public static String labimage = "https://new.mdconsults.org/MDCAPI/public/lab/";
//    public static String profileimage = "https://new.mdconsults.org/MDCAPI/public/profile_Images/";
//    public static String commentimage = "https://new.mdconsults.org/MDCAPI/public/comment_files/";
//    public static String emailUrl = baseurl + "email2.php?";
//    public static String UPLOAD_COMMENT_IMAGES_URL = baseurl + "comment_image.php";

//  public static String baseurl = "https://sojro.mdconsults.org/MDCAPI/public/";
//  public static String UPLOAD_URL = "https://sojro.mdconsults.org/MDCAPI/public/salman_multiple.php";
//  public static String UPLOAD_URL1 = "https://sojro.mdconsults.org/MDCAPI/public/salman.php";
//  public static String UPLOAD_URL2 = "https://sojro.mdconsults.org/MDCAPI/public/labimage.php";
//  public static String rootimage = "https://sojro.mdconsults.org/MDCAPI/public/opt_case_images/";
//  public static String radimage = "https://sojro.mdconsults.org/MDCAPI/public/radiology/";
//  public static String labimage = "https://sojro.mdconsults.org/MDCAPI/public/lab/";
//  public static String profileimage = "https://sojro.mdconsults.org/MDCAPI/public/profile_Images/";
//  public static String commentimage = "https://sojro.mdconsults.org/MDCAPI/public/comment_files/";
//  public static String emailUrl = baseurl + "email2.php?";
//  public static String UPLOAD_COMMENT_IMAGES_URL = baseurl + "comment_image.php";

//    public static String baseurl = "https://mdconsults.org/MDCAPI/public/";
//    public static String UPLOAD_URL = "https://mdconsults.org/MDCAPI/public/salman_multiple.php";
//    public static String UPLOAD_URL1 = "https://mdconsults.org/MDCAPI/public/salman.php";
//    public static String UPLOAD_URL2 = "https://mdconsults.org/MDCAPI/public/labimage.php";
//    public static String rootimage = "https://mdconsults.org/MDCAPI/public/opt_case_images/";
//    public static String radimage = "https://mdconsults.org/MDCAPI/public/radiology/";
//    public static String labimage = "https://mdconsults.org/MDCAPI/public/lab/";
//    public static String profileimage = "https://mdconsults.org/MDCAPI/public/profile_Images/";
//    public static String commentimage = "https://mdconsults.org/MDCAPI/public/comment_files/";
//    public static String emailUrl = baseurl + "email2.php?";
//    public static String UPLOAD_COMMENT_IMAGES_URL = baseurl + "comment_image.php";

    //  public static String baseurl = "https://mdconsults2sl.roohbarzu.com/MDCAPI_old/public/";
 //   public static String UPLOAD_URL = "https://mdconsults2sl.roohbaru.com/MDCAPI_old/public/salman_multiple.php";
 //   public static String rootimage = "https://mdconsults2sl.roohbaru.com/MDCAPI_old/public/opt_case_images/";
 //   public static String profileimage = "https://mdconsults2sl.roohbaru.com/resources/views/assets/dashboard/profile_Images/";

    Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    // Application reference must come from AppModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .create();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                         .cache(cache)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60,TimeUnit.SECONDS)
                        .addInterceptor(logging)
                        .build();
        return okHttpClient;
    }

    @Provides
    @Singleton
    RxBus provideBus() {
        RxBus rxBus = new RxBus();
        return rxBus;
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }


    @Provides @Singleton
     public ApiFactory provideMDCApi(Retrofit retrofit) {
        return retrofit.create(ApiFactory.class);
    }

    @Singleton @Provides
    MDCDatabase provideDb(Application app) {
      return Room.databaseBuilder(app, MDCDatabase.class,"sojrobag").build();
       // return Room.databaseBuilder(app, MDCDatabase.class,"tech4lifebiz").build();
     //   return Room.databaseBuilder(app, MDCDatabase.class,"mdconsults2rbapi").build();
    }

    @Singleton @Provides
    MainAppointmentDoa provideMainAppointmentDoa(MDCDatabase db) {
        return db.mainAppointmentDoa();
    }

    @Singleton @Provides
    MainAppointmentDetailDoa provideMainAppointmentDetail(MDCDatabase db) {
        return db.mainAppointmentDetailDoa();
    }

}
