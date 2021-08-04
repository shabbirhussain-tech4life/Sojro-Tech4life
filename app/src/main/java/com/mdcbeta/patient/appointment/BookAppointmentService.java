package com.mdcbeta.patient.appointment;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

//import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.mdcbeta.Events;
import com.mdcbeta.R;
import com.mdcbeta.app.MyApplication;
import com.mdcbeta.data.model.User;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.ProgressRequestBody;
import com.mdcbeta.util.RxBus;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookAppointmentService extends Service {
    private static final String TAG = "ImagesUploadService";

    int NOTIFICATION_ID = 1232312;

    NotificationManager mNotifyManager;

    List<ChosenImage> images_path;
    String slot_id;
    String doc_id;
    String description;
    String location_id;
    String docName;
    String docEmail;

    @Inject
    RxBus rxBus;
    @Inject
    ApiFactory apiFactory;


    private String date;

    public BookAppointmentService() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        images_path = AppPref.getInstance(this).getImageList(AppPref.Key.STEP_LIST_IMAGES);
        slot_id = AppPref.getInstance(this).getString(AppPref.Key.STEP_SLOT_ID);
        doc_id = AppPref.getInstance(this).getString(AppPref.Key.STEP_DOC_ID);
        docName = AppPref.getInstance(this).getString(AppPref.Key.STEP_DOC_NAME);
        docEmail = AppPref.getInstance(this).getString(AppPref.Key.STEP_DOC_EMAIL);
        description = AppPref.getInstance(this).getString(AppPref.Key.STEP_DISCRIPTION);
        location_id = AppPref.getInstance(this).getString(AppPref.Key.STEP_LOCATION_ID);
        date = AppPref.getInstance(this).getString(AppPref.Key.STEP_DATE);

        UploadtoServer();

        return START_NOT_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        mNotifyManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        ((MyApplication) getApplication()).getAppComponent().inject(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint("CheckResult")
    public void UploadtoServer() {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.icon_camera);
        builder.setContentTitle("MDConsults 2.0");
        builder.setContentText("Image uploading");
        builder.setProgress(100, 0, true);
//            NotificationChannel channel = new NotificationChannel(NotificationChannel.DEFAULT_CHANNEL_ID, "NAME OF CHANNEL",NotificationManager.IMPORTANCE_DEFAULT);
//
        startForeground(NOTIFICATION_ID, builder.build());
        mNotifyManager.notify(NOTIFICATION_ID, builder.build());
//
//            mNotifyManager.createNotificationChannel(channel);}

        User user = AppPref.getInstance(this).getUser(AppPref.Key.USER_LOGIN);


        List<MultipartBody.Part> parts = new ArrayList<>();


        if (images_path != null && !images_path.isEmpty()) {
            for (int i = 0; i < images_path.size(); i++) {
                File file = new File(images_path.get(i).getThumbnailPath());
                if (file.exists()) {
                    final MediaType MEDIA_TYPE = MediaType.parse(images_path.get(i).getMimeType());
                    parts.add(MultipartBody.Part.createFormData("my_images[]",
                            file.getName(), RequestBody.create(MEDIA_TYPE, file)));

                } else {
                    Log.d(TAG, "file not exist " + images_path.get(i).getOriginalPath());
                }

            }
        }

        rxBus.send(new Events.Start());

        apiFactory.createAppointment(
                TextUtils.isEmpty(description) ? "" : description,
                doc_id,
                user.id, user.name, "", user.gender,
                "", "", "", slot_id, date,
                Utils.localToUTCDateTime(Utils.getDatetime()), "patient",
                UUID.randomUUID().toString(), parts)
                .compose(RxUtil.applySchedulers())
                .subscribe(model -> {

                    AppPref.getInstance(this).remove(AppPref.Key.STEP_LIST_IMAGES,
                            AppPref.Key.STEP_SLOT_ID, AppPref.Key.STEP_DOC_ID,
                            AppPref.Key.STEP_DISCRIPTION, AppPref.Key.STEP_DATE);


                    if (model.status) {
                        rxBus.send(new Events.Success());

                        new EmailSender(user.getEmail(), docEmail,
                                user.getName(), docName, user.id)
                                .execute();

                        stopForeground(true);
                        rxBus.send(new Events.Failed("Appointment created successfully"));
                        stopSelf();
                    } else {
                        rxBus.send(new Events.Failed(model.getMessage()));
                        stopForeground(true);
                        stopSelf();
                    }

                }, throwable -> {
                    rxBus.send(new Events.Failed(throwable.getMessage()));
                    stopForeground(true);
                    stopSelf();
                    throwable.printStackTrace();

                });


    }

    private static class EmailSender extends AsyncTask<Void, Void, String> {

        OkHttpClient client = new OkHttpClient();
        String email1, email2, name1, name2;
        String caseCode;

        EmailSender(String selectedPatientEmail, String selectedReferEmail,
                    String selectedPatientName, String selectedReferName, String caseCode) {
            this.email1 = selectedPatientEmail;
            this.email2 = selectedReferEmail;
            this.name1 = selectedPatientName;
            this.name2 = selectedReferName;
            this.caseCode = caseCode;
        }

        @Override
        protected String doInBackground(Void... voids) {

            Request request = new Request.Builder()
                    .url(AppModule.emailUrl + "email1=" + email1 +
                            "&email2=" + email2 + "&name1=" + name1 +
                            "&name2=" + name2 + "&case_code=" + caseCode + "&meeting-time=" + new Date().toString())
                    .post(RequestBody.create(null, new byte[]{}))
                    .build();

            try {
                Response response = client.newCall(request).execute();

              Log.e("emailRESULT",response.message());
                return response.message();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("EMAIL RESPONSE", s);

        }
    }

    ProgressRequestBody.Listener listener = new ProgressRequestBody.Listener() {
        @Override
        public void onProgress(int progress) {
            Log.d(TAG, "onProgress: " + progress);
            // rxBus.send(new Events.Progress(progress));
        }
    };


}
