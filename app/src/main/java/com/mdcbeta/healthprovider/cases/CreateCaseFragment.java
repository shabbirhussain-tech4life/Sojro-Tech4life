package com.mdcbeta.healthprovider.cases;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.base.ImageUploadFragment;
import com.mdcbeta.bluetooth.BTController;
import com.mdcbeta.data.DataParser;
import com.mdcbeta.data.ECG;
import com.mdcbeta.data.NIBP;
import com.mdcbeta.data.Pulse;
import com.mdcbeta.data.SpO2;
import com.mdcbeta.data.Temp;
import com.mdcbeta.data.model.GroupAndUser;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Age;
import com.mdcbeta.data.remote.model.GroupItem;
import com.mdcbeta.data.remote.model.UserNames;
import com.mdcbeta.data.remote.request_model.CaseCreated;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.dialog.BluetoothDeviceAdapter;
import com.mdcbeta.dialog.SearchDevicesDialog;
import com.mdcbeta.healthprovider.cases.adapter.ImagePreviewAdapter;
import com.mdcbeta.healthprovider.cases.dialog.AddPatientDialog;
import com.mdcbeta.healthprovider.cases.dialog.UpdatePatient;
import com.mdcbeta.util.RecorderService;
import com.mdcbeta.util.RxBus;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.mdcbeta.util.VerticalSpaceItemDecoration;
import com.mdcbeta.view.RecorderVisualizerView;
import com.mdcbeta.view.WaveformView;
import com.mdcbeta.widget.ActionBar;
import com.mdcbeta.widget.CustomSpinnerAdapter;
import com.mdcbeta.widget.HorizontalFlowLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.trello.rxlifecycle2.android.FragmentEvent;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;


/**
 * Created by Shakil Karim on 4/8/17.
 */

public class CreateCaseFragment extends ImageUploadFragment implements
        Validator.ValidationListener,
        BaseDialogFragment.DialogClickListener,
        View.OnClickListener,
        BTController.Listener,
        DataParser.onPackageReceivedListener {

    // added by kanwal khan 22-11-2019
    boolean chck = false;
    private static final String TAG = "CreateCaseFragment";
    public static int scrollX = 0;
    public static int scrollY = -1;
    private static ArrayList<String> bluethoothNameList;
    private ArrayList<HashMap<String, String>> list;
    public static final String FIRST_COLUMN = "First";
    public static final String SECOND_COLUMN = "Second";
    public static final String THIRD_COLUMN = "Third";
    private BluetoothDevice bdDevice;
    public String labelInfo;
    Context context;

    //Bluetooth
    BluetoothDeviceAdapter mBluetoothDeviceAdapter;
    SearchDevicesDialog mSearchDialog;
    ProgressDialog mConnectingDialog;
    ArrayList<BluetoothDevice> mBluetoothDevices;
    private static BluetoothSocket mBluetoothSocket;
    private BluetoothDevice mBluetoothDevice;
    ProgressDialog progressDialog;
    boolean isRecordCompelted = false;
    int recordingEndTime = 0;
    int recordingStartTime = 0;
    public static String recordingStatus;
    public boolean isRecordingStart = false;

    private static final int PERMISSION_CODE = 1;
    private int mScreenDensity;
    private MediaProjectionManager mProjectionManager;
    private int DISPLAY_WIDTH = 480;
    private int DISPLAY_HEIGHT = 640;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaRecorder mMediaRecorder;
    private RecorderVisualizerView visualizerView;
    private Dialog dialog;
    public static final int REPEAT_INTERVAL = 40;
    private static ArrayList<BluetoothDevice> BluetoothObjectArraylist = null;

    double result;
  double temp;
    int seconds;
    int minute;
    Timer timer;
    String fileName = "";
    String spo = "- -";
    String pulse = "- -";

    // a by kk 1/18/2020
    private int countConnection = 0;
    String plist[];

    //data
    DataParser mDataParser;

    @BindView(R.id.lvResults)
    RecyclerView lvResults;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    //  List<DataHelper> dataOffline = new ArrayList<>();
    @BindView(R.id.txt_cuff)
    EditText txt_cuff;
    @BindView(R.id.main_scrollview)
    NestedScrollView mainScrollview;
    int check = 0;
    @BindView(R.id.txt_patient_id)
    EditText txtPatientId;
    // aded by kk

  @BindView(R.id.txt_head_patient_id)
  TextView PatientIdText;

  @BindView(R.id.spin_head_gender)
  TextView Sgender;


    @NotEmpty
    @BindView(R.id.txt_firstname)
    EditText txtFirstname;

  @BindView(R.id.txt_head_firstname)
  TextView FirstnameText;

//added by kk 7/23/2020

  @NotEmpty
  @BindView(R.id.phone)
  EditText txtPhone;

  @BindView(R.id.txt_head_phone)
  TextView PhoneText;

  @BindView(R.id.textView57)
  TextView patientDemo;


    @NotEmpty
    @BindView(R.id.txt_lastname)
    EditText txtLastname;

  @BindView(R.id.txt_head_lastname)
  TextView LastnameText;


    @BindView(R.id.txt_age_years)
    EditText txtAgeYears;

  @BindView(R.id.txt_head_age)
  TextView AgeYears;

    @BindView(R.id.txt_age_month)
    EditText txtAgeMonth;
    @BindView(R.id.txt_current_complain)
    EditText txtCurrentComplain;
    @BindView(R.id.txt_past_history)
    EditText txtPastHistory;
    @BindView(R.id.txt_comments)
    EditText txtComments;


    @BindView(R.id.spin_location)
    Spinner spinLocation;

  @BindView(R.id.location)
  TextView locationText;

    @BindView(R.id.referrerItems)
    HorizontalFlowLayout referrerItems;
    @BindView(R.id.spin_user)
    Spinner spin_user;
    @BindView(R.id.referview)
    Spinner referview;
    @BindView(R.id.txt_temperature)
    EditText txt_temperature;
    @BindView(R.id.spin_tempunit)
    Spinner spin_tempunit;
    @BindView(R.id.txt_systolic)
    EditText txt_systolic;
    @BindView(R.id.txt_diastolic)
    EditText txt_diastolic;
    @BindView(R.id.txt_pulse)
    EditText txt_pulse;
    @BindView(R.id.txt_resp_rate)
    EditText txt_resp_rate;
    @BindView(R.id.txt_sat)
    EditText txt_sat;
    @BindView(R.id.txt_weight)
    EditText txt_weight;
    @BindView(R.id.spin_weight)
    Spinner spin_weight;
    @BindView(R.id.txt_height)
    EditText txt_height;
    @BindView(R.id.spin_height)
    Spinner spin_height;
    @BindView(R.id.spin_bp)
    Spinner spin_bp;
    @BindView(R.id.radio_group)
    RadioGroup radio_group;
    @BindView(R.id.radio_group1)
    RadioGroup radio_group1;
    @BindView(R.id.radio_group2)
    RadioGroup radio_group2;
    @BindView(R.id.radio_group3)
    RadioGroup radio_group3;
    @BindView(R.id.radio_group4)
    RadioGroup radio_group4;
    @BindView(R.id.radio_axila)
    RadioButton radio_axila;
    @BindView(R.id.radio_oral)
    RadioButton radio_oral;
    @BindView(R.id.radio_rectum)
    RadioButton radio_rectum;
    @BindView(R.id.radio_oxygen)
    RadioButton radio_oxygen;
    @BindView(R.id.radio_roomair)
    RadioButton radio_roomair;
    @BindView(R.id.radio_regular)
    RadioButton radio_regular;
    @BindView(R.id.radio_irregular)
    RadioButton radio_irregular;
    @BindView(R.id.radio_lying)
    RadioButton radio_lying;
    @BindView(R.id.radio_standing)
    RadioButton radio_standing;
    @BindView(R.id.radio_sitting)
    RadioButton radio_sitting;
    @BindView(R.id.radio_yes)
    RadioButton radio_yes;
//    @BindView(R.id.expandableButton1)
//    Button expandableButton1;
//    @BindView(R.id.expandableLayout1)
//    ExpandableRelativeLayout expandableLayout1;
    @BindView(R.id.expandableButton2)
    Button expandableButton2;
    @BindView(R.id.expandableButton)
    Button expandableButton;
    @BindView(R.id.vital_layout)
    LinearLayout vital_layout;
    @BindView(R.id.table_main)
    TableLayout stk;
    @BindView(R.id.table_date)
    TableLayout stkdate;
    @BindView(R.id.spin_number)
    Spinner spin_number;
    @BindView(R.id.spin_gender)
   TextView spinGender;
    @BindView(R.id.table_main1)
    TableLayout stk1;
    @BindView(R.id.btnBtCtr)
    Button btnBtCtr;
    @BindView(R.id.tvbtinfo)
    TextView tvBtinfo;
    @BindView(R.id.tvSPO2info)
    TextView tvSPO2info;
    @BindView(R.id.tvTEMPinfo)
    TextView tvTEMPinfo;
    @BindView(R.id.tvNIBPinfo)
    TextView tvNIBPinfo;
  @BindView(R.id.tvECGinfo)
  TextView tvECGinfo;
  @BindView(R.id.wfECG)
  WaveformView wfECG;
    @BindView(R.id.wfSpO2)
    WaveformView wfSpO2;


    @BindView(R.id.layout5)
    LinearLayout layout5;


    //added by kk
    @BindView(R.id.update)
    Button UpdateProfile;

    @BindView(R.id.bdevice)
    ListView bdevices;


    String radiobutton;
    String radiobutton1;
    String radiobutton2;
    String radiobutton3;
    String radiobutton4;
    int spinnerbp;
    String spinnertemp;
    String spinner_weight;
    String spinner_height;
    String Bp_position;
    String dofbirth;

  String checkvital = "" ;
    String cuff = "- -";
    String systolic = "- -";
    String diastolic = "- -";

    @Inject
    ApiFactory apiFactory;

    @Inject
    OkHttpClient okHttpClient;

    @Inject
    RxBus rxBus;

    String locationId;
    String refererId;
    String image;
    String PatientId;
    String pain_scale;
    String number;
    int genderPosition;
    int weightu;
    List<Integer> groups = new ArrayList<>();
    List<Integer> users = new ArrayList<>();
    List<String> lables = new ArrayList<>();
    private ImagePreviewAdapter adapter;
    private Validator validator;
    private ArrayAdapter arrayAdapter;
    private CustomSpinnerAdapter<GroupAndUser> adapter1;
    Set<BluetoothDevice> devices;

    //   List<String> list = new ArrayList<String>();
    List<ChosenImage> images_path;
    String[] bp = {"Select Arm", "Left", "Right"};
    String[] weightunit = {"Select unit", "kg", "pound"};
    String[] heightunit = {"Select unit", "centimeters", "inches"};
    String[] tempunit = {"Select unit", "℃", "℉"};
    LayoutInflater inflater;
    private String selectedPatientEmail;
    private String selectedPatientName;
    private List<String> selectedReferName = new ArrayList<>();
    private List<String> selectedReferEmail = new ArrayList<>();
    private Handler handler = new Handler();
    private BTController mBtController;
    private RecyclerView rvDiagnosis;
    private Button btnAddMore;


    private boolean mBounded;
    private boolean isPausedAvailable = false;
    private RecorderService myService;
    BluetoothAdapter bluetoothAdapter;
    private int androidVersion;
    private static final int REQUEST_ENABLE_BLUETOOTH = 1;




    private static SharedPreferences prefsInstance;
    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBounded = false;
            myService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBounded = true;
            RecorderService.LocalBinder mLocalBinder = (RecorderService.LocalBinder) service;
            myService = mLocalBinder.getService();
        }
    };


    public static CreateCaseFragment newInstance() {
        CreateCaseFragment fragment = new CreateCaseFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        validator = new Validator(this);
        validator.setValidationListener(this);
        setRetainInstance(true);
        BluetoothObjectArraylist = new ArrayList<BluetoothDevice>();
        // added by kanwal khan 1/18/2020


     //spin_tempunit.setSelection(1);

//added by kk 7/23/2020

//      patientDemo.setVisibility(View.VISIBLE);
//      spin_user.setVisibility(View.VISIBLE);



    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_create_case;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        scrollX = mainScrollview.getScrollX();
        scrollY = mainScrollview.getScrollY();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetScrollView(scrollX, scrollY);
    }


    public void resetScrollView(int x, int y) {
        ViewTreeObserver vto = mainScrollview.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                mainScrollview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mainScrollview.scrollTo(x, y);
            }
        });
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");

        outState.putIntArray("SCROLL_POSITION",
                new int[]{mainScrollview.getScrollX(), mainScrollview.getScrollY()});
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inflater = LayoutInflater.from(getContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                initLocation();
                getGroupsandUsers();
                initPatientId();
                initBp();
                initHeight();
                initWeight();
                initTemp();

            }
        },2000);


        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED

        ) {

        } else
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);

        // c by kk 1/17/2020
        // initData();
        initView();

        // txt_pulse.setFilters(new InputFilter[]{new InputFilterMinMax("1", "12")});

        layout5.setVisibility(View.INVISIBLE);

        //added by kk 7/19/2020
      radiobutton2 = "Arm";
      radiobutton3 = "On room air";
      radiobutton = "Forehead";
      radiobutton1 = "Sitting";


        stk.removeAllViews();

        TableRow tbrow0 = new TableRow(getContext());
        TextView tv1 = new TextView(getContext());
        tv1.setText(" Temperature ");
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(null, Typeface.BOLD);
        tv1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(getContext());
        tv2.setText(" Temp type ");
        tv2.setTextColor(Color.BLACK);
        tv2.setTypeface(null, Typeface.BOLD);
        tv2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(getContext());
        tv3.setText(" Blood pressure (mmHg) ");
        tv3.setTextColor(Color.BLACK);
        tv3.setTypeface(null, Typeface.BOLD);
        tv3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv3);
        TextView tv4 = new TextView(getContext());
        tv4.setText("  Arm ");
        tv4.setTextColor(Color.BLACK);
        tv4.setTypeface(null, Typeface.BOLD);
        tv4.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv4);
        TextView tv5 = new TextView(getContext());
        tv5.setText(" B.P position ");
        tv5.setTextColor(Color.BLACK);
        tv5.setTypeface(null, Typeface.BOLD);
        tv5.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv5);
        TextView tv6 = new TextView(getContext());
        tv6.setText(" Pulse rate (beats/min) ");
        tv6.setTextColor(Color.BLACK);
        tv6.setTypeface(null, Typeface.BOLD);
        tv6.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv6);
        TextView tv7 = new TextView(getContext());
        tv7.setText(" Presentation ");
        tv7.setTextColor(Color.BLACK);
        tv7.setTypeface(null, Typeface.BOLD);
        tv7.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv7);
        TextView tv8 = new TextView(getContext());
        tv8.setText(" Resp rate (breaths/min) ");
        tv8.setTextColor(Color.BLACK);
        tv8.setTypeface(null, Typeface.BOLD);
        tv8.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv8);
        TextView tv9 = new TextView(getContext());
        tv9.setText(" Oxygen saturation (%) ");
        tv9.setTextColor(Color.BLACK);
        tv9.setTypeface(null, Typeface.BOLD);
        tv9.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv9);
        TextView tv10 = new TextView(getContext());
        tv10.setText(" Saturation taken ");
        tv10.setTextColor(Color.BLACK);
        tv10.setTypeface(null, Typeface.BOLD);
        tv10.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv10);
        TextView tv11 = new TextView(getContext());
        tv11.setText(" Weight ");
        tv11.setTextColor(Color.BLACK);
        tv11.setTypeface(null, Typeface.BOLD);
        tv11.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv11);
        TextView tv12 = new TextView(getContext());
        tv12.setText(" Height ");
        tv12.setTextColor(Color.BLACK);
        tv12.setTypeface(null, Typeface.BOLD);
        tv12.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv12);
        TextView tv13 = new TextView(getContext());
        tv13.setText(" Pain present ");
        tv13.setTextColor(Color.BLACK);
        tv13.setTypeface(null, Typeface.BOLD);
        tv13.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow0.addView(tv13);
        stk.addView(tbrow0);

        TableRow tbrow1 = new TableRow(getContext());
        TextView tvdate = new TextView(getContext());
        tvdate.setText(" Date ");
        tvdate.setTextColor(Color.BLACK);
        tvdate.setTypeface(null, Typeface.BOLD);
        tvdate.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow1.addView(tvdate);
        stkdate.addView(tbrow1);

        stk1.removeAllViews();

        TableRow tbrow10 = new TableRow(getContext());
        TextView ph1 = new TextView(getContext());
        ph1.setText(" Date of origin");
        ph1.setTextColor(Color.BLACK);
        ph1.setTypeface(null, Typeface.BOLD);
        ph1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow10.addView(ph1);
        TextView ph2 = new TextView(getContext());
        ph2.setText(" Referred to ");
        ph2.setTextColor(Color.BLACK);
        ph2.setTypeface(null, Typeface.BOLD);
        ph2.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow10.addView(ph2);
        TextView ph3 = new TextView(getContext());
        ph3.setText(" Date of review ");
        ph3.setTextColor(Color.BLACK);
        ph3.setTypeface(null, Typeface.BOLD);
        ph3.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
        tbrow10.addView(ph3);
        stk1.addView(tbrow10);

        if (savedInstanceState != null) {
            final int[] position = savedInstanceState.getIntArray("SCROLL_POSITION");
            if (position != null) {
                resetScrollView(position[0], position[1]);
            }

        }


    }

    private LinearLayout layoutDiagnosis;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        DISPLAY_WIDTH = metrics.widthPixels;
        DISPLAY_HEIGHT = metrics.heightPixels;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    2);

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mProjectionManager = (MediaProjectionManager) getContext().getSystemService
                    (Context.MEDIA_PROJECTION_SERVICE);
        }

        btnAddMore = (Button) view.findViewById(R.id.btnAddMore);
        layoutDiagnosis = view.findViewById(R.id.layoutDiagnosis);

        View diagnosisLayout = LayoutInflater.from(getContext()).inflate(R.layout.item_preliminary_diagnosis, null);
        diagnosisLayout.findViewById(R.id.btnDelete).setVisibility(View.GONE);
        layoutDiagnosis.addView(diagnosisLayout);

        btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View diagnosisLayout = LayoutInflater.from(getContext()).inflate(R.layout.item_preliminary_diagnosis, null);

                diagnosisLayout.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        layoutDiagnosis.removeView(diagnosisLayout);
                    }
                });

                layoutDiagnosis.addView(diagnosisLayout);
            }
        });

        radio_group.setOnCheckedChangeListener((group, checkedId) -> {
            // checkedId is the RadioButton selected
            switch (checkedId) {
                case R.id.radio_oral:
                    radiobutton = "Oral";
                    break;
                case R.id.radio_axila:
                    radiobutton = "Axilla";
                    break;
                case R.id.radio_rectum:
                    radiobutton = "Rectum";
                    break;


              case R.id.radio_forehead:
                radiobutton = "Forehead";
                break;
            }
        });

        radio_group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId) {
                    case R.id.radio_lying:
                        radiobutton1 = "Laying";
                        break;
                    case R.id.radio_standing:
                        radiobutton1 = "Standing";
                        break;
                    case R.id.radio_sitting:
                        radiobutton1 = "Sitting";
                        break;
                }
            }
        });

        radio_group2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId) {
                    case R.id.radio_regular:
                        radiobutton2 = "Arm";
                        break;
                    case R.id.radio_irregular:
                        radiobutton2 = "Wrist";
                        break;
                }
            }
        });

        radio_group3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId) {
                    case R.id.radio_oxygen:
                        radiobutton3 = "On oxygen";
                        break;
                    case R.id.radio_roomair:
                        radiobutton3 = "On room air";
                        break;
                }
            }
        });

        radio_group4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId) {
                    case R.id.radio_yes:
                        radiobutton4 = "Yes";
                        break;
                    case R.id.radio_no:
                        radiobutton4 = "No";
                        break;
                }

                if (radio_yes.isChecked()) {
                    layout5.setVisibility(View.VISIBLE);
                } else

                    layout5.setVisibility(View.GONE);
            }

        });

    }

    private List<String> getPrimilinaryDiagnosis() {
        List<String> list = new ArrayList<>();
        // For getting text from all primilinary diagnosis edittexts

        for (int i = 0; i < layoutDiagnosis.getChildCount(); i++) {
            EditText editText = layoutDiagnosis.getChildAt(i).findViewById(R.id.etString);
            if (!editText.getText().toString().trim().isEmpty())
                list.add(editText.getText().toString());
        }
        return list;
    }

    @SuppressLint("CheckResult")
    private void initPatientId() {
        showProgress("Loading");
       // apiFactory.existing_id()
        apiFactory.DemoLicenseData(getUser().id)
                .compose(RxUtil.applySchedulers())
                .subscribe(locationResponse -> {

                    hideProgress();

                    List<String> list1 = new ArrayList<String>();
                    list1.add("Select existing patient ID");
                    for (int i = 0; i < locationResponse.data.size(); i++) {
                        list1.add(locationResponse.data.get(i).getId() + ":" + locationResponse.data.get(i).getName());
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, list1);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_user.setAdapter(spinnerArrayAdapter);
                    spin_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                PatientId = null;
                                return;
                            }

                            PatientId = locationResponse.data.get(position - 1).getId();
                            Log.d(TAG, "initLocation: " + PatientId);


                            showProgress("Loading");
                            apiFactory.getPatientData(PatientId)
                                    .compose(RxUtil.applySchedulers())
                                    .subscribe(resource -> {

                                        hideProgress();
                                      txtPatientId.setVisibility(View.VISIBLE);
                                      PatientIdText.setVisibility(View.VISIBLE);
                                      txtFirstname.setVisibility(View.VISIBLE);
                                      FirstnameText.setVisibility(View.VISIBLE);
                                      txtLastname.setVisibility(View.VISIBLE);
                                      LastnameText.setVisibility(View.VISIBLE);
                                      txtPhone.setVisibility(View.VISIBLE);
                                      PhoneText.setVisibility(View.VISIBLE);
                                      txtAgeYears.setVisibility(View.VISIBLE);
                                      AgeYears.setVisibility(View.VISIBLE);
                                      spinGender.setVisibility(View.VISIBLE);
                                      Sgender.setVisibility(View.VISIBLE);
                                      spinLocation.setVisibility(View.VISIBLE);
                                      locationText.setVisibility(View.VISIBLE);
UpdateProfile.setVisibility(View.VISIBLE);
                                        if (resource.status) {

                                            JsonObject data = resource.data.get("details").getAsJsonObject();

                                            try {
                                                txtPatientId.setText(data.get("id").getAsString());
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }


                                            try {
                                                txtFirstname.setText(data.get("firstname").getAsString());
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }

                                          try {
                                           txtPhone.setText(data.get("phone").getAsString());
                                          } catch (Exception ex) {
                                            ex.printStackTrace();
                                          }

                                            try {
                                                txtLastname.setText(data.get("lastname").getAsString());
                                            } catch (Exception ex) {
                                                ex.printStackTrace();

                                            }

                                            try {
                                                selectedPatientName = data.get("name").getAsString();
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }

                                            try {
                                                selectedPatientEmail = data.get("email").getAsString();
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }

                                            try {
                                                String gender = data.get("gender").getAsString();
                                                if (gender.equals("male")) {
                                                  // spinGender.setSelection(1);
                                                    genderPosition = 1;
                                                  spinGender.setText("Male");
                                                } else {
                                                  genderPosition = 2;
                                                  spinGender.setText("Female");
                                                  //  spinGender.setSelection(2);

                                                }
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }

                                            try {
                                                dofbirth = data.get("dobirth").getAsString();
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                Date birthDate = sdf.parse(dofbirth);
                                                Age age = Utils.calculateAge(birthDate);
//Toast.makeText(getContext(),""+age,Toast.LENGTH_SHORT).show();
                                              //  txtAgeYears.setText(age.getYears() + " years");
                                                if (age.getYears() > 18) {
                                                    radio_rectum.setVisibility(View.GONE);
                                                }
                                                if(age.getYears()<=2)
                                                {


                                                  txtAgeYears.setText(age.getYears() + " years and " + age.getMonths() + " months"); }
else if(age.getYears()==2017)
                                                {
                                                  txtAgeYears.setText("");
                                                }

                                                else {
                                                txtAgeYears.setText(age.getYears()+" years"); }
                                              //  txtAgeMonth.setText(age.getMonths() + " months");
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                                txtAgeYears.setText("");
                                                txtAgeMonth.setText("");
                                            }


                                            try {
                                                int commemtsSize = resource.data.get("recorddata").getAsJsonArray().size();
                                                for (int i = 0; i < 10; i++) {
                                                    JsonObject comment = resource.data.get("recorddata").getAsJsonArray().get(i).getAsJsonObject();

                                                    if (!comment.get("oraltemperature").getAsString().isEmpty() ||
                                                            !comment.get("systolic").getAsString().isEmpty() ||
                                                            !comment.get("hands").getAsString().isEmpty() ||
                                                            !comment.get("bloodpressureposition").getAsString().isEmpty() ||
                                                      !comment.get("weightunit").getAsString().equals(0)
                                                      ||
                                                      !comment.get("heightunit").getAsString().equals(0)

                                                    ) {

                                                        TableRow tbrow1 = new TableRow(getContext());
                                                        TextView tvdate1 = new TextView(getContext());
                                                        tvdate1.setText(Utils.format12HourDateTime(comment.get("created_at").getAsString()));
                                                        tvdate1.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                        tvdate1.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                        tbrow1.addView(tvdate1);
                                                        stkdate.addView(tbrow1);

                                                        TableRow tbrow = new TableRow(getContext());

                                                        TextView t2v = new TextView(getContext());
                                                        if (!TextUtils.isEmpty(comment.get("temperature").getAsString())) {

                                                            t2v.setText(comment.get("temperature").getAsString() + " " + comment.get("temperaturevalue").getAsString());
                                                        }

                                                        t2v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                        t2v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                        t2v.setGravity(Gravity.CENTER);
                                                        tbrow.addView(t2v);

                                                        TextView t3v = new TextView(getContext());
                                                        t3v.setText(comment.get("oraltemperature").getAsString());
                                                        t3v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                        t3v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                        t3v.setGravity(Gravity.CENTER);
                                                        tbrow.addView(t3v);
                                                        TextView t4v = new TextView(getContext());
                                                        if (!TextUtils.isEmpty(comment.get("systolic").getAsString())) {
                                                            t4v.setText(comment.get("systolic").getAsString() + "/" + comment.get("diastolic").getAsString());
                                                        }
                                                        t4v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                        t4v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                        t4v.setGravity(Gravity.CENTER);
                                                        tbrow.addView(t4v);
                                                        TextView t5v = new TextView(getContext());
                                                        t5v.setText(comment.get("hands").getAsString());
                                                        t5v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                        t5v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                        t5v.setGravity(Gravity.CENTER);
                                                        tbrow.addView(t5v);
                                                        TextView t6v = new TextView(getContext());
                                                        t6v.setText(comment.get("bloodpressureposition").getAsString());
                                                        t6v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                        t6v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                        t6v.setGravity(Gravity.CENTER);
                                                        tbrow.addView(t6v);
                                                        TextView t7v = new TextView(getContext());
                                                        t7v.setText(comment.get("pulse").getAsString());
                                                        t7v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                        t7v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                        t7v.setGravity(Gravity.CENTER);
                                                        tbrow.addView(t7v);
                                                        TextView t8v = new TextView(getContext());
                                                        t8v.setText(comment.get("pulsepres").getAsString());
                                                        t8v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                        t8v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                        t8v.setGravity(Gravity.CENTER);
                                                        tbrow.addView(t8v);
                                                        TextView t9v = new TextView(getContext());
                                                        t9v.setText(comment.get("respiratoryrate").getAsString());
                                                        t9v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                        t9v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                        t9v.setGravity(Gravity.CENTER);
                                                        tbrow.addView(t9v);
                                                        TextView t10v = new TextView(getContext());
                                                        t10v.setText(comment.get("saturation").getAsString());
                                                        t10v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                        t10v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                        t10v.setGravity(Gravity.CENTER);
                                                        tbrow.addView(t10v);
                                                        TextView t11v = new TextView(getContext());
                                                        t11v.setText(comment.get("satoxygen").getAsString());
                                                        t11v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                        t11v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                        t11v.setGravity(Gravity.CENTER);
                                                        tbrow.addView(t11v);
                                                        TextView t12v = new TextView(getContext());
                                                        if (!TextUtils.isEmpty(comment.get("weight").getAsString())) {

                                                            t12v.setText(comment.get("weight").getAsString() + " " + comment.get("weightunit").getAsString());

                                                        }
                                                        t12v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                        t12v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                        t12v.setGravity(Gravity.CENTER);
                                                        tbrow.addView(t12v);
                                                        TextView t13v = new TextView(getContext());
                                                        if (!TextUtils.isEmpty(comment.get("height").getAsString())
                                                        ) {
if(comment.get("height").getAsString().length()==0 || comment.get("heightunit").getAsString().length()==0 )
{
  t13v.setText("");
}
                                                            t13v.setText(comment.get("height").getAsString() + " " + comment.get("heightunit").getAsString());

                                                        }
                                                        t13v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                        t13v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                        t13v.setGravity(Gravity.CENTER);
                                                        tbrow.addView(t13v);
                                                        TextView t14v = new TextView(getContext());
                                                        if (comment.get("painpatienty").getAsString().equals("Yes")) {
                                                            t14v.setText(comment.get("painpatienty").getAsString() + " - " + comment.get("painpatient").getAsString());
                                                        } else if (comment.get("painpatienty").getAsString().equals("No")) {
                                                            t14v.setText("No");
                                                        }
                                                        t14v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                        t14v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                        t14v.setGravity(Gravity.CENTER);
                                                        tbrow.addView(t14v);
                                                        stk.addView(tbrow);
                                                    }
                                                }
                                            } catch (Exception e) {
                                            }

                                            try {
                                                int commemtsSize = resource.data.get("historydata").getAsJsonArray().size();
                                                for (int i = 0; i < 10; i++) {
                                                    JsonObject comment = resource.data.get("historydata").getAsJsonArray().get(i).getAsJsonObject();

                                                    TableRow tbrow = new TableRow(getContext());

                                                    TextView t2v = new TextView(getContext());
                                                    if (!TextUtils.isEmpty(comment.get("created_at").getAsString())) {

                                                        t2v.setText(Utils.format12HourDate(Utils.UTCTolocalDate(comment.get("created_at").getAsString())));

                                                    }
                                                    t2v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                    t2v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                    t2v.setGravity(Gravity.CENTER);
                                                    tbrow.addView(t2v);
                                                    TextView t3v = new TextView(getContext());
                                                    t3v.setText(comment.get("name").getAsString());
                                                    t3v.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                    t3v.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                    t3v.setGravity(Gravity.CENTER);
                                                    tbrow.addView(t3v);
                                                    TextView reviewer = new TextView(getContext());
                                                    reviewer.setText(Utils.format12HourDate(Utils.UTCTolocalDate(comment.get("updated_at").getAsString())));
                                                    reviewer.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
                                                    reviewer.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.tablebg));
                                                    reviewer.setGravity(Gravity.CENTER);
                                                    tbrow.addView(reviewer);

                                                    stk1.addView(tbrow);

                                                    tbrow.setOnClickListener(new View.OnClickListener() {

                                                        @Override
                                                        public void onClick(View v) {

                                                            //    Toast.makeText(getContext(), comment.get("id").getAsString(), Toast.LENGTH_SHORT).show();

                                                            getFragmentHandlingActivity().addFragmentWithBackstack(CaseDetailFragment.newInstance(
                                                                    comment.get("id").getAsString(), comment.get("case_code").getAsString()));

                                                            // or do something more use full here.

                                                        }
                                                    });
                                                }

                                            } catch (Exception e) {
                                            }


                                        } else {
                                            showError(resource.getMessage());
                                        }

                                    }, throwable -> {
                                        showError(throwable.getMessage());

                                    });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Log.d(TAG, "initLocation: ");
                        }
                    });


                });

    }

    private void initLocation() {
        apiFactory.locations()
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(locationResponse -> {

                    List<String> list = new ArrayList<String>();
                    list.add("Select location");
                    for (int i = 0; i < locationResponse.data.size(); i++) {
                        list.add(locationResponse.data.get(i).getArea());
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item, list);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinLocation.setAdapter(spinnerArrayAdapter);
                    spinLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (position == 0) {
                                locationId = null;
                                return;
                            }

                            locationId = locationResponse.data.get(position - 1).getId();
                            Log.d(TAG, "initLocation: " + locationId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Log.d(TAG, "initLocation: ");
                        }
                    });


                });

    }

    private void initBp() {

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, bp);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_bp.setAdapter(spinnerArrayAdapter);
        spin_bp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Bp_position = null;
                    return;
                }

                Bp_position = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "initLocation: " + spinnerbp);
                spinnerbp = position;
                //       Toast.makeText(getContext(),spinnerbp + ", " + Bp_position, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "initLocation: ");
            }
        });


    }

    private void initWeight() {

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, weightunit);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_weight.setAdapter(spinnerArrayAdapter);
        spin_weight.setSelection(1);
        spin_weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinner_weight = (String) parent.getItemAtPosition(position);
                Log.d(TAG, "initLocation: " + spinner_weight);
                //     Toast.makeText(getContext(),spinner_weight, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "initLocation: ");
            }
        });

    }

    private void initTemp() {

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, tempunit);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_tempunit.setAdapter(spinnerArrayAdapter);
        spin_tempunit.setSelection(1);
        spin_tempunit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//

                if (position == 0) {
                    spinnertemp = null;
                    return;
                }




                spinnertemp = String.valueOf(parent.getSelectedItem());
                Log.d(TAG, "initLocation: " + spinnertemp);
                //   Log.d(TAG, "initLocation: "+spinnerbp);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "initLocation: ");
            }
        });

    }

    private void initHeight() {

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, heightunit);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_height.setAdapter(spinnerArrayAdapter);
        spin_height.setSelection(1);
        spin_height.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinner_height = (String) parent.getItemAtPosition(position);
                //   Log.d(TAG, "initLocation: "+spinnerbp);
                //    Toast.makeText(getContext(), spinner_height, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "initLocation: ");
            }
        });

    }

    private void getGroupsandUsers() {

        Observable.zip(

                apiFactory.groupByUsername(getUser().getId()),
                apiFactory.usersNames(getUser().licenses_id == null ? "-1" : getUser().licenses_id),
                (groupItemResponse, userResponse) -> {

                    ArrayList<GroupAndUser> list = new ArrayList<>();
                    list.add(new GroupAndUser("", "", "Select groups/individuals", ""));
                    for (int i = 0; i < groupItemResponse.data.size(); i++) {
                        GroupItem grop = groupItemResponse.data.get(i);
                        list.add(new GroupAndUser(grop.getGroupId(), "group", grop.getName(), grop.getEmail()));
                    }

                    for (int j = 0; j < userResponse.data.size(); j++) {
                        UserNames user = userResponse.data.get(j);
                        list.add(new GroupAndUser(user.getId(), "user", user.getName(), user.getEmail()));
                    }

                    return list;
                })
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(groupAndUsers -> {
                    adapter1 = new CustomSpinnerAdapter<>(getContext(), groupAndUsers);
                    referview.setAdapter(adapter1);


                }, Throwable::printStackTrace);

    }


    private void initList() {

        adapter = new ImagePreviewAdapter(getActivity());
        lvResults.setAdapter(adapter);
        lvResults.setHasFixedSize(true);
        lvResults.setItemAnimator(new DefaultItemAnimator());
        lvResults.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        lvResults.setNestedScrollingEnabled(false);
        lvResults.setLayoutManager(new GridLayoutManager(getContext(), 2));

    }

    @OnClick(R.id.camera_fab)
    public void onViewClicked() {
        showImageDialog();
    }

    @Override
    public void onImagesChosen(List<ChosenImage> list) {
        adapter.addImage(list);
    }


    @SuppressLint("CheckResult")
    @Override
    public void onValidationSucceeded() {

        if (referview.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Referrer is not selected", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(txtPatientId.getText().toString())) {
            Toast.makeText(getContext(), "Please enter case code", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(txtFirstname.getText().toString())) {
            Toast.makeText(getContext(), "Please enter first name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(locationId)) {
            Toast.makeText(getContext(), "Location is not selected", Toast.LENGTH_SHORT).show();
            return;
        }

        if (genderPosition == 0) {
            Toast.makeText(getContext(), "Gender is not selected", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(txtAgeYears.getText().toString()) && TextUtils.isEmpty(txtAgeMonth.getText().toString())) {
            Toast.makeText(getContext(), "Please enter age in years or months", Toast.LENGTH_SHORT).show();
            return;
        }

        String year = TextUtils.isEmpty(txtAgeYears.getText().toString()) ? "0" : txtAgeYears.getText().toString().split(" ")[0];
        if (Integer.valueOf(year) > 150) {
            Toast.makeText(getContext(), "Year cannot be greater than 150", Toast.LENGTH_SHORT).show();
            return;
        }
        String month = TextUtils.isEmpty(txtAgeMonth.getText().toString()) ? "0" : txtAgeMonth.getText().toString().split(" ")[0];
        if (Integer.valueOf(month) > 11) {
            Toast.makeText(getContext(), "Month cannot be greater than 11", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!TextUtils.isEmpty(txt_systolic.getText().toString())) {
            String systolic = txt_systolic.getText().toString();
            if (Integer.valueOf(systolic) < 60 || Integer.valueOf(systolic) > 500) {

                Toast.makeText(getContext(), "Systolic value is incorrect", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!TextUtils.isEmpty(txt_diastolic.getText().toString())) {
            String diastolic = txt_diastolic.getText().toString();
            if (Integer.valueOf(diastolic) < 30 || Integer.valueOf(diastolic) > 250) {
                Toast.makeText(getContext(), "Diastolic value is incorrect", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!TextUtils.isEmpty(txt_pulse.getText().toString())) {
            String pulse = txt_pulse.getText().toString().trim();

            if (Integer.valueOf(pulse) <30 || Integer.valueOf(pulse) > 300) {
                Toast.makeText(getContext(), "Pulse value is incorrect", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!TextUtils.isEmpty(txt_resp_rate.getText().toString())) {
            String resp_rate = txt_resp_rate.getText().toString();
            if (Integer.valueOf(resp_rate) < 5 || Integer.valueOf(resp_rate) > 70) {
                Toast.makeText(getContext(), "Respiratory rate value is incorrect", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!TextUtils.isEmpty(txt_sat.getText().toString())) {
            String saturation = txt_sat.getText().toString();
            if (Integer.valueOf(saturation) < 70 || Integer.valueOf(saturation) > 105) {

                Toast.makeText(getContext(), "Oxygen saturation value is incorrect", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!TextUtils.isEmpty(txt_temperature.getText().toString())) {
            String temperature = txt_temperature.getText().toString();

            if (spinnertemp.equals("1")) {

                if (Integer.valueOf(temperature) < 35 || Integer.valueOf(temperature) > 40) {
                    Toast.makeText(getContext(), "Temperature value is incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (spinnertemp.equals("2")) {
                if (Integer.valueOf(temperature) < 95 || Integer.valueOf(temperature) > 110) {
                    Toast.makeText(getContext(), "Temperature value is incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        CaseCreated caseCreated = new CaseCreated(

                txtPatientId.getText().toString(), txtFirstname.getText().toString(), txtLastname.getText().toString(),txtPhone.getText().toString(),
                genderPosition == 1 ? "male" : "female", year, month, "1", locationId, txtPastHistory.getText().toString(),
                "", "1", "null", "0", getUser().getId(), txtCurrentComplain.getText().toString(),
                txtComments.getText().toString(), groups, users, Utils.localToUTCDateTime(Utils.getDatetime()),
                txt_temperature.getText().toString(), spinnertemp, radiobutton, txt_systolic.getText().toString(),
                txt_diastolic.getText().toString(), Bp_position, radiobutton1, txt_pulse.getText().toString(), radiobutton2,
                txt_resp_rate.getText().toString(), txt_sat.getText().toString(), radiobutton3, txt_weight.getText().toString(),
                spinner_weight, txt_height.getText().toString(), spinner_height, radiobutton4, number, getPrimilinaryDiagnosis(),checkvital);

        Log.d(TAG, "onValidationSucceeded: " + Utils.localToUTCDateTime(Utils.getDatetime()));

        showProgress("Creating case...");


        apiFactory.createCase(caseCreated)
                .flatMap(response -> {

                    String casecartedID = response.getData();
                    File file;
                    String uploadId = UUID.randomUUID().toString();
                    List<ChosenImage> images_path = adapter.getFiles();

                    List<MultipartBody.Part> parts = new ArrayList<>();

                    if (images_path != null && !images_path.isEmpty()) {
                        for (int i = 0; i < images_path.size(); i++) {
                            file = new File(images_path.get(i).getThumbnailPath());

                            Log.d("Image path: ", file.getPath());

                            if (file.exists()) {

                                new MultipartUploadRequest(this.getContext(), uploadId, AppModule.UPLOAD_URL)
                                        .addFileToUpload(file.getPath(), "image") //Adding file
                                        .addParameter("name", casecartedID) //Adding text parameter to the request
//                                        .setNotificationConfig(new UploadNotificationConfig())
//                                        .setMaxRetries(2)
                                        .startUpload(); //S
                                final MediaType MEDIA_TYPE = MediaType.parse(images_path.get(i).getMimeType());
                                parts.add(MultipartBody.Part.createFormData("my_images[]",
                                        file.getName(), RequestBody.create(MEDIA_TYPE, file)));

                                Log.d(TAG, "File exist" + MultipartBody.Part.createFormData("my_images[]",
                                        file.getName(), RequestBody.create(MEDIA_TYPE, file)));

                            } else {
                                //  Log.d(TAG, "file not exist " + images_path.get(i).getOriginalPath());
                            }

                        }
                    }
//sojroapi//publuc
                  //https://sojro.mdconsults.org/sojroapi/public/case_images/1485512488-1.jpg

                    return apiFactory.addCaseAttachments(casecartedID, parts);

                })
                .compose(RxUtil.applySchedulers())

                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model -> {

                    if (model.status) {

                        if (selectedPatientEmail != null)
                            new EmailSender(selectedPatientEmail, selectedReferEmail,
                                    selectedPatientName, selectedReferName, txtPatientId.getText().toString())
                                    .execute();

                        showSuccess(model.getMessage());
                        getFragmentHandlingActivity().replaceFragment(MyCasesFragment.newInstance());
                    } else {
                        showError(model.getMessage());
                    }

                }, throwable -> {
                    hideProgress();
                    throwable.printStackTrace();
                    showError(throwable.getMessage());
                });

    }


    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        }
    }


    @OnClick(R.id.btn_create_case)
    public void CreateCaseClick() {
      if (txt_temperature.getText().toString().isEmpty())
      {
        spinnertemp = null;
        radiobutton= null;


      }
      if(txt_sat.getText().toString().isEmpty())
      {
        radiobutton3="";
      }
if(txt_systolic.getText().toString().isEmpty() || txt_diastolic.getText().toString().isEmpty())
{
  radiobutton1=null;
  radiobutton2=null;
  number =null;

}
if(txt_pulse.getText().toString().isEmpty())
{
  radiobutton2 = null;
}
if(txt_sat.getText().toString().isEmpty())
{
  radiobutton3 = null;
}

if(txt_weight.getText().toString().isEmpty())
{
  spinner_weight = null;
}
if(txt_height.getText().toString().isEmpty())
{
  spinner_height = null;
}
if(txt_temperature.getText().toString().isEmpty() && spinnertemp == null && radiobutton==null && txt_systolic.getText().toString().isEmpty() &&
        txt_diastolic.getText().toString().isEmpty() && Bp_position==null && radiobutton1==null && txt_pulse.getText().toString().isEmpty() &&
      radiobutton2==null &&txt_resp_rate.getText().toString().isEmpty() && txt_sat.getText().toString().isEmpty() && radiobutton3==null &&
      txt_weight.getText().toString().isEmpty() && spinner_weight == null && txt_height.getText().toString().isEmpty() &&
        spinner_height==null && radiobutton4==null)
{
  checkvital = "no";
  Toast.makeText(getContext(),"no"+checkvital,Toast.LENGTH_SHORT).show();
}
else {

  checkvital = "yes";
  Toast.makeText(getContext(),"yes"+checkvital,Toast.LENGTH_SHORT).show();
}
        validator.validate();
//        if(mBtController.isBTConnected())
//        {
//        mBtController.disableBtAdpter(); }
//
//    }

//        if(bluetoothAdapter.isEnabled())
//        {
//            mBtController.disableBtAdpter();
//        }
    }

    @OnClick(R.id.btn_new_patient)
    public void NewPatientClick() {

        String str = getUser().licenses_id;
        int val = Integer.parseInt(str);
        int num=27;
        int tr=27;
        if(num==val)
        {
            showError("This feature is  not available for demo accounts");
        }
        else {
            Bundle args = new Bundle();
            args.putBoolean("Key", true);
            // args.putString("key", "14");
            DialogFragment newFragment = new AddPatientDialog();
            newFragment.setArguments(args);
            newFragment.show(getFragmentManager(), "CreateCaseFragment");

            //  getFragmentHandlingActivity().addDialog(this, AddPatientDialog.newInstance());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            shareScreen();
//        }
        }
    }



//    @OnItemSelected(R.id.spin_gender)
//    public void spinnerItemSelected(Spinner spinner, int position) {
//        genderPosition = position;
//    }


    @OnItemSelected(R.id.spin_number)
    void onRealationselected(AdapterView<?> parent, int position) {
        number = (String) parent.getItemAtPosition(position);
    }


    @OnItemSelected(R.id.referview)
    public void RefereItemSelected(Spinner spinner, int position) {
        if (++check > 1) {
            referrerItems.addView(getBubble(adapter1.getItem(position).name, adapter1.getItem(position)));
            if (adapter1.getItem(position).type.equalsIgnoreCase("group")) {
                groups.add(Integer.valueOf(adapter1.getItem(position).id));
            } else {
                users.add(Integer.valueOf(adapter1.getItem(position).id));
                selectedReferEmail.add(adapter1.getItem(position).email);
                selectedReferName.add(adapter1.getItem(position).name);
            }
        }
    }

    public View getBubble(String name, GroupAndUser groupAndUser) {
        LinearLayout linearLayout = (LinearLayout) getBubbleView();
        TextView view1 = (TextView) linearLayout.findViewById(R.id.textname);
        view1.setText(name);
        view1.setTag(groupAndUser);
        return linearLayout;
    }


    public View getBubbleView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.view_bubble, referrerItems, false);
        v.setOnClickListener(v1 -> referrerItems.removeView(v1));
        return v;
    }


    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {

    }

    private void initData() {
        //added by Kanwal Khan

//checkBluetoothState();


        // enable the Bluetooth Adapter
//        `                                                                                                         mBtController = BTController.getDefaultBTController(this);
////
    }

    private void checkBluetoothState() {


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // bdevices.append("\nAdapter :" +bluetoothAdapter);

        if (bluetoothAdapter == null) {
            Toast.makeText(getContext(), "No device", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (bluetoothAdapter.isEnabled()) {
//                bdevices.append("\nBluetooth is enabled");
////                bdevices.append("Paired devices");
                ArrayList list = new ArrayList();
                Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                for (BluetoothDevice device : devices) {
                    //   bdevices.add(device.getName());
                    //   bdevices.append("\ndevices are"+device.getName()+""+ device);


                }
            } else {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
            }
        }


    }

    public void onClick(View v) {


        //  initData();


        switch (v.getId()) {
            case R.id.btnBtCtr:
                // below code is works fine
//                bdevices.setVisibility(View.VISIBLE);
//
//                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//
//                if (bluetoothAdapter == null) {
//                    Toast.makeText(getContext(), "No device", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//
//                if (!bluetoothAdapter.isEnabled()) {
//                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
//                }
//
//                else {
//showPairedDevices();
//                }

                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!bluetoothAdapter.isEnabled()) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
                }

                else {
                    mBtController = BTController.getDefaultBTController(this);
                    mBtController.registerBroadcastReceiver(this.getContext());
                    mBtController.enableBtAdpter();

                    mDataParser = new DataParser(this);
                    mDataParser.start();



                    if (!mBtController.isBTConnected()) {
                        mSearchDialog.show();
                        mSearchDialog.startSearch();
                        mBtController.startScan(true);
                    } else {
                        mBtController.disconnect();
                        tvBtinfo.setText("");


                    }
                }


// above code is works fine

//                if(bluetoothAdapter==null)
//                {
//                    Toast.makeText(getContext(),"No device",Toast.LENGTH_SHORT).show();
//                    return;
//                }

//                else {
//                    if(bluetoothAdapter.isEnabled())
//                    {
////                bdevices.append("\nBluetooth is enabled");
//////                bdevices.append("Paired devices");
//                        ArrayList list = new ArrayList();
//                        Set<BluetoothDevice>devices=bluetoothAdapter.getBondedDevices();
//                        for(BluetoothDevice device:devices)
//                        {
//                            //   bdevices.add(device.getName());
//                            //   bdevices.append("\ndevices are"+device.getName()+""+ device);
//
//
//                        }
//                    }
//
//                    else {
//                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                        startActivityForResult(intent,REQUEST_ENABLE_BLUETOOTH);
//                    }


                // if (mBtController.isBTConnected()) {

                //checkBluetoothState();
                //mBtController.registerBroadcastReceiver(this.getContext());
////        mBtController.enableBtAdpter();
////
                // mDataParser = new DataParser(this);
                //  mDataParser.start();

                // }
                // mSearchDialog.show();
                // mSearchDialog.startSearch();
                //   mBtController.startScan(true);
                //    mBtController.pairedDevies();

                //  else {
                // mBtController.disconnect();
                //   tvBtinfo.setText("");
                // mBtController.disableBtAdpter();
                //  }
                //
                break;
////            case R.id.btnNIBPStart:
////                mBtController.write(DataParser.CMD_START_NIBP);
////                break;
////            case R.id.btnNIBPStop:
////                mBtController.write(DataParser.CMD_STOP_NIBP);
////                break;
//        }


        }

    }

    @Override
    public void onFoundDevice(BluetoothDevice device) {
        if (mBluetoothDevices.contains(device))
            return;
        mBluetoothDevices.add(device);
        mBluetoothDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStopScan() {
        mSearchDialog.stopSearch();
    }


    @Override
    public void onStartScan() {
        mBluetoothDevices.clear();
        mBluetoothDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConnected() {
        mConnectingDialog.setMessage("Connected √");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mConnectingDialog.dismiss();
                    }
                });
            }
        }, 800);

        btnBtCtr.setText("Disconnect");
    }

    @Override
    public void onDisconnected() {
        btnBtCtr.setText("Search Devices");
    }

    @Override
    public void onReceiveData(byte[] dat) {
        mDataParser.add(dat);
    }

    @Override
    public void onSpO2WaveReceived(int dat) {
        wfSpO2.addAmp(dat);
       // Toast.makeText(getContext(),"waveform",Toast.LENGTH_SHORT).show();
    }

    @Override
//    public void onSpO2Received(SpO2 spo2) {
//        runOnUiThread(new Runnable() {
//            @Override
//
//
//
//            public void run() {
//
//                 //  tvSPO2info.setText(spo2.toString());
//                    txt_sat.setText(spo2.toString());
//            }
//        });
    public void onSpO2Received(final SpO2 spo2) {
        runOnUiThread(new Runnable() {
            @Override


            public void run() {

                String temp = spo2.toString();
                int number = 0;
// here i have to add range
                //  if (spo2 >=90 && spo2<=100 )
                if (temp.contains("- -")) {

                    txt_pulse.setText(pulse);
                }


                //if(number>=90 && number<=100)
                else {

                    String[] parts = temp.split(":"); // escape .

                    spo = parts[0];
                    pulse = parts[1];
                    txt_sat.setText(spo);
                    txt_pulse.setText(pulse);
                }


            }
        });
    }


    @Override
    public void onPulseReceived(Pulse pulse) {

        runOnUiThread(new Runnable() {
            @Override


            public void run() {

                //   txt_pulse.setText(pulse.toString());

            }
        });

    }

    @Override
    public void onTempReceived(Temp temp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_temperature.setText(temp.toString());
            }
        });
    }

    @Override
    public void onNIBPReceived(NIBP nibp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // tvNIBPinfo.setText(nibp.toString());
                String temp = nibp.toString();

                if (temp.contains("- -")) {
                    txt_cuff.setText(cuff);
                    txt_systolic.setText(systolic);
                    txt_diastolic.setText(diastolic);
                } else {
                    String[] parts = temp.split(":"); // escape .
                    cuff = parts[0];
                    systolic = parts[1];
                    diastolic = parts[2];
                    txt_cuff.setText(cuff);
                    txt_systolic.setText(systolic);
                    txt_diastolic.setText(diastolic);
                }
            }
        });
    }

    private void initView() {


        btnBtCtr.setOnClickListener(this);

        //Bluetooth Search Dialog
        mBluetoothDevices = new ArrayList<>();
        mBluetoothDeviceAdapter = new BluetoothDeviceAdapter(this.getContext(), mBluetoothDevices);
        mSearchDialog = new SearchDevicesDialog(this.getContext(), mBluetoothDeviceAdapter) {
            @Override
            public void onStartSearch() {
                mBtController.startScan(true);
            }

            @Override
            public void onClickDeviceItem(int pos) {
                BluetoothDevice device = mBluetoothDevices.get(pos);
                mBtController.startScan(false);
                mBtController.connect(this.getContext(), device);
                tvBtinfo.setText(device.getName() + ": " + device.getAddress());
                mConnectingDialog.show();
                mSearchDialog.dismiss();
            }
        };
        mSearchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBtController.startScan(false);
            }
        });

        mConnectingDialog = new ProgressDialog(this.getContext());
        mConnectingDialog.setMessage("Connecting...");

        //About Information
//        llAbout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mBtController.write(DataParser.CMD_FW_VERSION);
//                mBtController.write(DataParser.CMD_HW_VERSION);
//            }
//        });


    }




    private static class EmailSender extends AsyncTask<Void, Void, String> {

        OkHttpClient client = new OkHttpClient();
        String email1, name1;
        List<String> email2, name2;
        String caseCode;

        EmailSender(String selectedPatientEmail, List<String> selectedReferEmail,
                    String selectedPatientName, List<String> selectedReferName, String caseCode) {
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
                            "&email2=" + new Gson().toJson(email2) + "&name1=" + name1 +
                            "&name2=" + new Gson().toJson(name2) + "&case_code=" + caseCode +
                            "&meeting-time=" + new Date().toString())
                    .post(RequestBody.create(null, new byte[]{}))
                    .build();

            try {
                Response response = client.newCall(request).execute();
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


//    @OnClick(R.id.expandableButton1)
//    public void Expandable1Click() {
//
//        chck = !chck;
//        expandableLayout1.toggle();
//        // added bt kanwal khan
//
//        if (chck == true) {
//            wfSpO2.setVisibility(View.VISIBLE);
//        } else {
//            wfSpO2.setVisibility(View.GONE);
//
//
//        }
//
//    }


    @OnClick(R.id.expandableButton2)
    public void Expandable2Click() {

        stk1.setVisibility(stk1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

    }

    @OnClick(R.id.expandableButton)
    public void ExpandableClick() {

        vital_layout.setVisibility(vital_layout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

    }


    //@Override
    //public void onDestroy() {
    //Thread.setDefaultUncaughtExceptionHandler(null);
    //mBtController.unregisterBroadcastReceiver(this);
    // unregisterBroadcastReceiver(this);

    //super.onDestroy();
    //}


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            //checkBluetoothState();
            if (resultCode == RESULT_OK) {
                System.out.println("Bluetooth turned on successfully");
            }
        }
    }
    public void showPairedDevices()
    {
        bdevices.setVisibility(View.VISIBLE);
        Set<BluetoothDevice> pairedDevices =bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            ArrayList<String> bluetoothDevices = new ArrayList<String>();
            for (BluetoothDevice device : pairedDevices) {

                String deviceName = device.getName(); // Get BT name
                String deviceAddress = device.getAddress(); // Get MAC


                bluetoothDevices.add(deviceName);

                final ArrayAdapter<String> adapter =
                        new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,bluetoothDevices);
                bdevices.setAdapter(adapter);

            }

        }
    }


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    super.onCreateOptionsMenu(menu, inflater);

  }


  @OnClick(R.id.update)
  public void UpdatePatientClick() {


       // getFragmentHandlingActivity().addDialog(this, UpdatePatient.newInstance());
    Bundle args = new Bundle();
    //args.putBoolean("Keys", txtPatientId);
    // args.putString("key", "14");
    args.putString("keys",txtPatientId.getText().toString());
    args.putString("fname",txtFirstname.getText().toString());
    args.putString("lname",txtLastname.getText().toString());
    args.putString("phone",txtPhone.getText().toString());
    args.putBoolean("Key",true);
    DialogFragment newFragment = new UpdatePatient();
    newFragment.setArguments(args);
    newFragment.show(getFragmentManager(), "CreateCaseFragment");
//

  }

  @Override
  public void onECGWaveReceived(int dat) {
    wfECG.addAmp(dat);
  }

  @Override
  public void onECGReceived(final ECG ecg) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        tvECGinfo.setText(ecg.toString());
      }
    });
  }



}
