package com.mdcbeta.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.AudioManager;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import com.mdcbeta.R;
import com.mdcbeta.bluetooth.Const;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class RecorderService extends Service {

    // by kk
    public static final String NOTIFICATION_CHANNEL_ID_SERVICE = "com.mdcbeta.util";
    public static final String NOTIFICATION_CHANNEL_ID_INFO = "com.package.download_info";

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private int WIDTH, HEIGHT, FPS, DENSITY_DPI;
    private int BITRATE;
    private String audioRecSource;
    private String SAVEPATH;
    private AudioManager mAudioManager;

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    private int screenOrientation;
    private String saveLocation;

    private boolean isRecording;
    private boolean useFloatingControls;
    private boolean showCameraOverlay;
    private boolean showTouches;
    private boolean isShakeGestureActive;
    private boolean showSysUIDemo = false;
    private NotificationManager mNotificationManager;

    private Intent data;
    private int result;

    private long startTime, elapsedTime = 0;
    private SharedPreferences prefs;
    private WindowManager window;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;
    private MediaRecorder mMediaRecorder;
    private String fileName = "";

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public RecorderService getService() {
            return RecorderService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public int getMaxAmplitude() {
        return mMediaRecorder != null ? mMediaRecorder.getMaxAmplitude() : 0;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            createNotificationChannels();



        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //return super.onStartCommand(intent, flags, startId);
        //Find the action to perform from intent
        switch (intent.getAction()) {
            case Const.SCREEN_RECORDING_START:

                if (!isRecording) {
                    screenOrientation = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
                    data = intent.getParcelableExtra(Const.RECORDER_INTENT_DATA);
                    result = intent.getIntExtra(Const.RECORDER_INTENT_RESULT, Activity.RESULT_OK);
                    fileName = intent.getStringExtra(Const.VIDEO_FILE_NAME);

                    getValues();

                    startRecording();

                } else {
                    Toast.makeText(this, R.string.screenrecording_already_active_toast, Toast.LENGTH_SHORT).show();
                }
                break;
            case Const.SCREEN_RECORDING_PAUSE:
                pauseScreenRecording();
                break;
            case Const.SCREEN_RECORDING_RESUME:
                resumeScreenRecording();
                break;
            case Const.SCREEN_RECORDING_STOP:
                stopRecording();
                break;
            case Const.SCREEN_RECORDING_DESTORY_SHAKE_GESTURE:
                stopSelf();
                break;
        }
        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void stopRecording() {
        stopScreenSharing();

        //The service is started as foreground service and hence has to be stopped
        stopForeground(true);
    }

    @TargetApi(24)
    private void pauseScreenRecording() {
//        mMediaRecorder.pause();
//        //calculate total elapsed time until pause
//        elapsedTime += (System.currentTimeMillis() - startTime);
//
//        //Set Resume action to Notification and update the current notification
//        Intent recordResumeIntent = new Intent(this, RecorderService.class);
//        recordResumeIntent.setAction(Const.SCREEN_RECORDING_RESUME);
//        PendingIntent precordResumeIntent = PendingIntent.getService(this, 0, recordResumeIntent, 0);
//        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_play_arrow_white,
//                getString(R.string.screen_recording_notification_action_resume), precordResumeIntent);
//        updateNotification(createRecordingNotification(action).setUsesChronometer(false).build(), Const.SCREEN_RECORDER_NOTIFICATION_ID);
//        Toast.makeText(this, R.string.screen_recording_paused_toast, Toast.LENGTH_SHORT).show();
    }

    @TargetApi(24)
    private void resumeScreenRecording() {
//        mMediaRecorder.resume();
//
//        //Reset startTime to current time again
//        startTime = System.currentTimeMillis();
//
//        //set Pause action to Notification and update current Notification
//        Intent recordPauseIntent = new Intent(this, RecorderService.class);
//        recordPauseIntent.setAction(Const.SCREEN_RECORDING_PAUSE);
//        PendingIntent precordPauseIntent = PendingIntent.getService(this, 0, recordPauseIntent, 0);
//        NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_pause_white,
//                getString(R.string.screen_recording_notification_action_pause), precordPauseIntent);
//        updateNotification(createRecordingNotification(action).setUsesChronometer(true)
//                .setWhen((System.currentTimeMillis() - elapsedTime)).build(), Const.SCREEN_RECORDER_NOTIFICATION_ID);
////        Toast.makeText(this, getString(R.string.screen_recording_resumed_toast), Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startRecording() {

        //Initialize MediaRecorder class and initialize it with preferred configuration
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                Log.e(Const.TAG, "Screencam Error: " + what + ", Extra: " + extra);
                destroyMediaProjection();
            }
        });

        initRecorder();

        //Set Callback for MediaProjection
        mMediaProjectionCallback = new MediaProjectionCallback();
        MediaProjectionManager mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        //Initialize MediaProjection using data received from Intent
        mMediaProjection = mProjectionManager.getMediaProjection(result, data);
        mMediaProjection.registerCallback(mMediaProjectionCallback, null);

        /* Create a new virtual display with the actual default display
         * and pass it on to MediaRecorder to start recording */
        mVirtualDisplay = createVirtualDisplay();
        try {
            mMediaRecorder.start();
            isRecording = true;
            Toast.makeText(this, R.string.screen_recording_started_toast, Toast.LENGTH_SHORT).show();

        } catch (IllegalStateException e) {
            Log.e(Const.TAG, "Mediarecorder reached Illegal state exception. Did you start the recording twice?");
            Toast.makeText(this, R.string.recording_failed_toast, Toast.LENGTH_SHORT).show();
            isRecording = false;
            mMediaProjection.stop();
            stopSelf();
        }

        /* Add Pause action to Notification to pause screen recording if the user's android version
         * is >= Nougat(API 24) since pause() isnt available previous to API24 else build
         * Notification with only default stop() action */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //startTime is to calculate elapsed recording time to update notification during pause/resume
            startTime = System.currentTimeMillis();
//            Intent recordPauseIntent = new Intent(this, RecorderService.class);
//            recordPauseIntent.setAction(Const.SCREEN_RECORDING_PAUSE);
//            PendingIntent precordPauseIntent = PendingIntent.getService(this, 0, recordPauseIntent, 0);
//            NotificationCompat.Action action = new NotificationCompat.Action(R.drawable.ic_pause_white,
//                    getString(R.string.screen_recording_notification_action_pause), precordPauseIntent);

            //Start Notification as foreground
            startNotificationForeGround(createRecordingNotification(null).build(), Const.SCREEN_RECORDER_NOTIFICATION_ID);
        } else
            startNotificationForeGround(createRecordingNotification(null).build(), Const.SCREEN_RECORDER_NOTIFICATION_ID);
    }

    //Virtual display created by mirroring the actual physical display
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private VirtualDisplay createVirtualDisplay() {
        return mMediaProjection.createVirtualDisplay("MainActivity",
                WIDTH, HEIGHT, DENSITY_DPI,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mMediaRecorder.getSurface(), null /*Callbacks*/, null
                /*Handler*/);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public int getBestSampleRate() {
        AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        String sampleRateString = am.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
        int samplingRate = (sampleRateString == null) ? 44100 : Integer.parseInt(sampleRateString);
        Log.d(Const.TAG, "Sampling rate: " + samplingRate);
        return samplingRate;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean getMediaCodecFor(String format) {
        MediaCodecList list = new MediaCodecList(MediaCodecList.REGULAR_CODECS);
        MediaFormat mediaFormat = MediaFormat.createVideoFormat(
                format,
                WIDTH,
                HEIGHT
        );
        String encoder = list.findEncoderForFormat(mediaFormat);
        if (encoder == null) {
            Log.d("Null Encoder: ", format);
            return false;
        }
        Log.d("Encoder", encoder);
        return !encoder.startsWith("OMX.google");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private int getBestVideoEncoder() {
        int VideoCodec = MediaRecorder.VideoEncoder.DEFAULT;
        if (getMediaCodecFor(MediaFormat.MIMETYPE_VIDEO_HEVC)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                VideoCodec = MediaRecorder.VideoEncoder.HEVC;
            }
        } else if (getMediaCodecFor(MediaFormat.MIMETYPE_VIDEO_AVC))
            VideoCodec = MediaRecorder.VideoEncoder.H264;
        return VideoCodec;
    }

    /* Initialize MediaRecorder with desired default values and values set by user. Everything is
     * pretty much self explanatory */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initRecorder() {
        boolean mustRecAudio = false;
        try {
//            String audioBitRate = "192";
            String audioBitRate = "384000";
            String audioSamplingRate = getBestSampleRate() + "";
            String audioChannel = "1";
            switch (audioRecSource) {
                case "1":
                    break;
                case "2":
                    Log.d(Const.TAG, "bit rate: " + audioBitRate + " sampling: " + audioSamplingRate + " channel" + audioChannel);
                    break;
                case "3":
                    mAudioManager.setParameters("screenRecordAudioSource=8");
                    break;
            }
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setOutputFile(SAVEPATH);
            mMediaRecorder.setVideoSize(WIDTH, HEIGHT);
//            mMediaRecorder.setVideoEncoder(getBestVideoEncoder());
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setVideoEncodingBitRate(BITRATE);
            mMediaRecorder.setVideoFrameRate(FPS);
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Add notification channel for supporting Notification in Api 26 (Oreo)
//    @TargetApi(26)
//    private void createNotificationChannels() {
//        List<NotificationChannel> notificationChannels = new ArrayList<>();
//        NotificationChannel recordingNotificationChannel = new NotificationChannel(
//                Const.RECORDING_NOTIFICATION_CHANNEL_ID,
//                Const.RECORDING_NOTIFICATION_CHANNEL_NAME,
//                NotificationManager.IMPORTANCE_DEFAULT
//        );
//        recordingNotificationChannel.enableLights(true);
//        recordingNotificationChannel.setSound(null, null);
//        recordingNotificationChannel.setLightColor(Color.RED);
//        recordingNotificationChannel.setShowBadge(true);
//        recordingNotificationChannel.enableVibration(true);
//        recordingNotificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//        notificationChannels.add(recordingNotificationChannel);
//
//        NotificationChannel shareNotificationChannel = new NotificationChannel(
//                Const.SHARE_NOTIFICATION_CHANNEL_ID,
//                Const.SHARE_NOTIFICATION_CHANNEL_NAME,
//                NotificationManager.IMPORTANCE_DEFAULT
//        );
//        shareNotificationChannel.enableLights(true);
//        shareNotificationChannel.setLightColor(Color.RED);
//        shareNotificationChannel.setShowBadge(true);
//        shareNotificationChannel.enableVibration(true);
//        shareNotificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//        notificationChannels.add(shareNotificationChannel);
//
//        getManager().createNotificationChannels(notificationChannels);
//    }

    /* Create Notification.Builder with action passed in case user's android version is greater than
     * API24 */
    private NotificationCompat.Builder createRecordingNotification(NotificationCompat.Action action) {


        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.mdconcults);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, Const.RECORDING_NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.screen_recording_notification_title))
                .setTicker(getResources().getString(R.string.screen_recording_notification_title))
                .setSmallIcon(R.mipmap.mdconcults)
                .setLargeIcon(icon)
                .setUsesChronometer(true)
                .setOngoing(true)
//                .setStyle(new NotificationCompat.MediaStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        if (action != null)
            notification.addAction(action);
        return notification;
    }

    //Start service as a foreground service. We dont want the service to be killed in case of low memory
    private void startNotificationForeGround(Notification notification, int ID) {

        startForeground(ID, notification);
    }

    //Update existing notification with its ID and new Notification data
    private void updateNotification(Notification notification, int ID) {
        getManager().notify(ID, notification);
    }

    private NotificationManager getManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    @Override
    public void onDestroy() {
        Log.d(Const.TAG, "Recorder service destroyed");
        super.onDestroy();
    }

    private String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    //Get user's choices for user choosable settings
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void getValues() {
        String res = getResolution();
        setWidthHeight(res);
        FPS = Integer.parseInt("30");
        BITRATE = Integer.parseInt("7130317");
        audioRecSource = "1";
        saveLocation = prefs.getString(getString(R.string.savelocation_key),
                Environment.getExternalStorageDirectory() + File.separator + getApplicationName(this));
        File saveDir = new File(saveLocation);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !saveDir.isDirectory()) {
            saveDir.mkdirs();
        }
        useFloatingControls = false;
        showSysUIDemo = false;
        showCameraOverlay = false;
        showTouches = false;
        SAVEPATH = saveLocation + File.separator + fileName + ".mp4";
        isShakeGestureActive = false;
    }

    /* The PreferenceScreen save values as string and we save the user selected video resolution as
     * WIDTH x HEIGHT. Lets split the string on 'x' and retrieve width and height */
    private void setWidthHeight(String res) {
        String[] widthHeight = res.split("x");
        WIDTH = Integer.parseInt(widthHeight[0]);
        HEIGHT = Integer.parseInt(widthHeight[1]);
        Log.d(Const.TAG, "Width: " + WIDTH + ",Height:" + HEIGHT);
    }

    //Get the device resolution in pixels
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private String getResolution() {
        DisplayMetrics metrics = new DisplayMetrics();
        window = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        window.getDefaultDisplay().getRealMetrics(metrics);
        DENSITY_DPI = metrics.densityDpi;
        int width = metrics.widthPixels;
        width = Integer.parseInt(Integer.toString(width));
        float aspectRatio = getAspectRatio(metrics);
        int height = calculateClosestHeight(width, aspectRatio);
        //String res = width + "x" + (int) (width * getAspectRatio(metrics));
        String res = width + "x" + height;
        Log.d(Const.TAG, "resolution service: " + "[Width: "
                + width + ", Height: " + width * aspectRatio + ", aspect ratio: " + aspectRatio + "]");
        return res;
    }

    private int calculateClosestHeight(int width, float aspectRatio) {
        int calculatedHeight = (int) (width * aspectRatio);
        Log.d(Const.TAG, "Calculated width=" + calculatedHeight);
        Log.d(Const.TAG, "Aspect ratio: " + aspectRatio);
        if (calculatedHeight / 16 != 0) {
            int quotient = calculatedHeight / 16;
            Log.d(Const.TAG, calculatedHeight + " not divisible by 16");

            calculatedHeight = 16 * quotient;

            Log.d(Const.TAG, "Maximum possible height is " + calculatedHeight);
        }
        return calculatedHeight;
    }

    private float getAspectRatio(DisplayMetrics metrics) {
        float screen_width = metrics.widthPixels;
        float screen_height = metrics.heightPixels;
        float aspectRatio;
        if (screen_width > screen_height) {
            aspectRatio = screen_width / screen_height;
        } else {
            aspectRatio = screen_height / screen_width;
        }
        return aspectRatio;
    }

    //Stop and destroy all the objects used for screen recording
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void destroyMediaProjection() {

        this.mAudioManager.setParameters("screenRecordAudioSource=0");
        try {
            mMediaRecorder.stop();
            indexFile();
            Log.i(Const.TAG, "MediaProjection Stopped");
        } catch (RuntimeException e) {
            Log.e(Const.TAG, "Fatal exception! Destroying media projection failed." + "\n" + e.getMessage());
            if (new File(SAVEPATH).delete())
                Log.d(Const.TAG, "Corrupted file delete successful");
            Toast.makeText(this, getString(R.string.fatal_exception_message), Toast.LENGTH_SHORT).show();
        } finally {
            mMediaRecorder.reset();
            mVirtualDisplay.release();
            mMediaRecorder.release();
            if (mMediaProjection != null) {
                mMediaProjection.unregisterCallback(mMediaProjectionCallback);
                mMediaProjection.stop();
                mMediaProjection = null;
            }
            stopSelf();
            //The service is started as foreground service and hence has to be stopped
            stopForeground(true);
        }
        isRecording = false;
    }

    private void indexFile() {
//        //Create a new ArrayList and add the newly created video file path to it
//        ArrayList<String> toBeScanned = new ArrayList<>();
//        toBeScanned.add(SAVEPATH);
//        String[] toBeScannedStr = new String[toBeScanned.size()];
//        toBeScannedStr = toBeScanned.toArray(toBeScannedStr);
//
//        //Request MediaScannerConnection to scan the new file and index it
//        MediaScannerConnection.scanFile(this, toBeScannedStr, null, (path, uri) -> {
//        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void stopScreenSharing() {
        if (mVirtualDisplay == null) {
            Log.d(Const.TAG, "Virtual display is null. Screen sharing already stopped");
            return;
        }
        destroyMediaProjection();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            Log.v(Const.TAG, "Recording Stopped");
            stopScreenSharing();
        }
    }







}
