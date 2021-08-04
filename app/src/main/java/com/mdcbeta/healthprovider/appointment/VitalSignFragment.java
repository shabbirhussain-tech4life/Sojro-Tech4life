package com.mdcbeta.healthprovider.appointment;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.bluetooth.BTController;
import com.mdcbeta.data.DataParser;
import com.mdcbeta.data.ECG;
import com.mdcbeta.data.NIBP;
import com.mdcbeta.data.Pulse;
import com.mdcbeta.data.SpO2;
import com.mdcbeta.data.Temp;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.dialog.BluetoothDeviceAdapter;
import com.mdcbeta.dialog.SearchDevicesDialog;
import com.mdcbeta.patient.appointment.MediaAdapter;
import com.mdcbeta.patient.appointment.MediaResultsAdapter;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.view.WaveformView;
import com.mdcbeta.widget.ActionBar;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnItemSelected;

import static android.app.Activity.RESULT_OK;
import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;


public class VitalSignFragment extends BaseFragment implements BlockingStep,BTController.Listener, DataParser.onPackageReceivedListener, View.OnClickListener {

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    @BindView(R.id.txt_temperature)
    EditText txt_temperature;
    @BindView(R.id.manualTemp_txt_temperature)
    EditText manualTemp_txt_temperature;
    @BindView(R.id.spin_tempunit)
    Spinner spin_tempunit;
    @BindView(R.id.manualTemp_spin_tempunit)
    Spinner manualTemp_spin_tempunit;
    @BindView(R.id.txt_pulse)
    EditText txt_pulse;
    @BindView(R.id.txt_resp_rate)
    EditText txt_resp_rate;
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
    @BindView(R.id.txt_sat)
    EditText txt_sat;
    @BindView(R.id.radio_lying)
    RadioButton radio_lying;
    @BindView(R.id.radio_standing)
    RadioButton radio_standing;
    @BindView(R.id.radio_sitting)
    RadioButton radio_sitting;
    @BindView(R.id.radio_yes)
    RadioButton radio_yes;
    @BindView(R.id.radio_group)
    RadioGroup radio_group;
    @BindView(R.id.radio_group1)
    RadioGroup radio_group1;
    @BindView(R.id.radio_group3)
    RadioGroup radio_group3;
    @BindView(R.id.radio_group4)
    RadioGroup radio_group4;
    @BindView(R.id.layout5)
    LinearLayout layout5;
    @BindView(R.id.wfSpO2)
    WaveformView wfSpO2;

    @BindView(R.id.wfECG)
    WaveformView wfECG;

    @BindView(R.id.tvSPO2info)
    TextView tvSPO2info;

  @BindView(R.id.tvECGinfo)
  TextView tvECGinfo;


    @BindView(R.id.tvTEMPinfo)
    TextView tvTEMPinfo;
    @BindView(R.id.tvNIBPinfo)
    TextView tvNIBPinfo;
    @BindView(R.id.btnBtCtr)
    Button btnBtCtr;
    @BindView(R.id.tvbtinfo)
    TextView tvBtinfo;
    @BindView(R.id.txt_cuff)
    EditText txt_cuff;
    @BindView(R.id.txt_systolic)
    EditText txt_systolic;
    @BindView(R.id.txt_diastolic)
    EditText txt_diastolic;
    @BindView(R.id.radio_group2)
    RadioGroup radio_group2;
    @BindView(R.id.editText)
    EditText description;

    private int position;
    String radiobutton;
    String radiobutton1;
    String radiobutton2;
    String radiobutton3;
    String radiobutton4;
    int spinnerbp;
    String spinnertemp;
    String   manualspinnertemp;
    String spinner_weight;
    String spinner_height;
    String Bp_position;
    DataParser mDataParser;
    String number;
    String spo = "- -";
    String pulse = "- -";
    String cuff =  "- -";
    String systolic =  "- -";
    String diastolic =  "- -";
  String celsius = "";
  String checkvital ="";
  Double a;
  int cel;
    private MediaResultsAdapter adapter;
    // private MediaAdapter adapter;
    private BTController mBtController;
    BluetoothDeviceAdapter mBluetoothDeviceAdapter;
    SearchDevicesDialog mSearchDialog;
    ProgressDialog mConnectingDialog;
    ArrayList<BluetoothDevice> mBluetoothDevices;
    String[] bp = {"Select Arm", "Left", "Right"};
    String[] weightunit = {"Select unit", "kg", "pound"};
    String[] heightunit = {"Select unit", "centimeters", "inches"};
    String[] tempunit = {"\u2103","\u2109"};
//String[] tempunit = {"Select unit", "℃", "℉"};
    private static final String TAG = "StepFragment";





    @Override
    public int getLayoutID() {
        return R.layout.fragment_vitalsign;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: " + position);
        //  BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

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


        radio_group2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId) {
                    case R.id.radio_regular:
                        radiobutton2 = "Radial";
                        break;
                    case R.id.radio_irregular:
                        radiobutton2 = "Brachial";
                        break;
                }
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
          //  Toast.makeText(this.getContext(), "", Toast.LENGTH_SHORT).show();
          Log.e("Location permission","location of permisssion is granted");
        else
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 100);


        initBp();
        initHeight();
        initWeight();
        initTemp();
        //initData();
        initView();



      //added by kk 7/20/2020
      radiobutton2 = "Radial";
      radiobutton3 = "On oxygen";
      radiobutton = "Oral";
      radiobutton1 = "Sitting";
    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }


    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {


      if (txt_temperature.getText().toString().isEmpty())
      {
        spinnertemp = null;
//        radiobutton= null;


      }

      if(manualTemp_txt_temperature.getText().toString().isEmpty())
      {
        manualTemp_spin_tempunit = null;
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

      if(manualTemp_txt_temperature.getText().toString().isEmpty()&& manualTemp_spin_tempunit == null && txt_temperature.getText().toString().isEmpty() && spinnertemp == null && radiobutton==null && txt_systolic.getText().toString().isEmpty() &&
        txt_diastolic.getText().toString().isEmpty() && Bp_position==null && radiobutton1==null && txt_pulse.getText().toString().isEmpty() &&
        radiobutton2==null &&txt_resp_rate.getText().toString().isEmpty() && txt_sat.getText().toString().isEmpty() && radiobutton3==null &&
        txt_weight.getText().toString().isEmpty() && spinner_weight == null && txt_height.getText().toString().isEmpty() &&
        spinner_height==null && radiobutton4==null)
      {
        checkvital = "no";
       // Toast.makeText(getContext(),"no"+checkvital,Toast.LENGTH_SHORT).show();
      }
      else {

        checkvital = "yes";
      //  Toast.makeText(getContext(),"yes"+checkvital,Toast.LENGTH_SHORT).show();
      }
      AppPref.getInstance(getActivity()).put(AppPref.Key.CHECK_VITAL,checkvital);
        AppPref.getInstance(getActivity()).put(AppPref.Key.SPO2,txt_sat.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.CUFF_VALUE,txt_cuff.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.TEMP_R,radiobutton);
        AppPref.getInstance(getActivity()).put(AppPref.Key.SYSTOLIC,txt_systolic.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.DIASTOLIC,txt_diastolic.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.TEMPERATURE,txt_temperature.getText().toString());
      AppPref.getInstance(getActivity()).put(AppPref.Key.TEMPERATUREMANUAL,manualTemp_txt_temperature.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.BP_R,radiobutton1);
        AppPref.getInstance(getActivity()).put(AppPref.Key.PULSE_RATE,txt_pulse.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.RESP_RATE,txt_resp_rate.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.RESP_RATE_R,radiobutton3);
        AppPref.getInstance(getActivity()).put(AppPref.Key.PULSE_R,radiobutton2);
        AppPref.getInstance(getActivity()).put(AppPref.Key.WEIGHT,txt_weight.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.WEIGHT_UNIT,spinner_weight);
        AppPref.getInstance(getActivity()).put(AppPref.Key.HEIGHT,txt_height.getText().toString());
        AppPref.getInstance(getActivity()).put(AppPref.Key.HEIGHT_UNIT,spinner_height);
        AppPref.getInstance(getActivity()).put(AppPref.Key.TEMP_UNIT,spinnertemp);
       AppPref.getInstance(getActivity()).put(AppPref.Key.TEMP_UNIT2,manualspinnertemp);
        AppPref.getInstance(getActivity()).put(AppPref.Key.PAIN,radiobutton4);
        AppPref.getInstance(getActivity()).put(AppPref.Key.PAIN_R,number);
        AppPref.getInstance(getActivity()).put(AppPref.Key.BP_POS,Bp_position);
        AppPref.getInstance(getActivity()).put(AppPref.Key.STEP_DISCRIPTION,description.getText().toString());

        //   Toast.makeText(this.getContext(), tvSPO2info.getText().toString() , Toast.LENGTH_SHORT).show();

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {


        callback.goToPrevStep();



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
        manualTemp_spin_tempunit.setAdapter(spinnerArrayAdapter);
        spin_tempunit.setSelection(0);
        manualTemp_spin_tempunit.setSelection(0);
        spin_tempunit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                if (position == 0) {
//                    spinnertemp = null;
//                    return;
//                }
//
//                spinnertemp = String.valueOf(parent.getSelectedItemPosition());
             //   Log.d(TAG, "initLocation: " + spinnertemp);
                //   Log.d(TAG, "initLocation: "+spinnerbp);
              spinnertemp = (String) parent.getItemAtPosition(position);
              celsius = spinnertemp;
//             if(celsius=="\u2103")
//             {
//              cel = 0;}
//            if(celsius == "\u2109")
//            {
//              cel = 1;
//            }




//              //for calculator
              if(!txt_temperature.getText().toString().isEmpty() && celsius=="\u2103")
              {

                a=Double.parseDouble(String.valueOf(txt_temperature.getText()));
                Double b = (a* 9/5) + 32 ;
              //  Double b=a*9/5+32;
                String r=String.valueOf(b);
                Toast.makeText(getContext(),r+"°F",Toast.LENGTH_SHORT).show();
                txt_temperature.setText(r);
                Toast.makeText(getContext(),"cel"+celsius,Toast.LENGTH_SHORT).show();

              }
////
              if(!txt_temperature.getText().toString().isEmpty() && celsius == "\u2109")
              {

                a=Double.parseDouble(String.valueOf(txt_temperature.getText()));
               // Double b=a-32;
                Double c = (a-32) * 5/9 ;
              //  Double c=b*5/9;
                String r=String.valueOf(c);
                Toast.makeText(getContext(),r+"°C",Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(),"I am Fahrenheit",Toast.LENGTH_SHORT).show();
                txt_temperature.setText(r);
                Toast.makeText(getContext(),"cel"+celsius,Toast.LENGTH_SHORT).show();
              }




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "initLocation: ");
            }
        });
      manualTemp_spin_tempunit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

//                if (position == 0) {
//                    spinnertemp = null;
//                    return;
//                }
//
//                spinnertemp = String.valueOf(parent.getSelectedItemPosition());
          //   Log.d(TAG, "initLocation: " + spinnertemp);
          //   Log.d(TAG, "initLocation: "+spinnerbp);
          manualspinnertemp = (String) parent.getItemAtPosition(position);
          celsius = manualspinnertemp;
          if(celsius=="1")
          {
            cel = 1;}
          else {
            cel =0;
          }

          //  Toast.makeText(getContext(),celsius,Toast.LENGTH_SHORT).show();
          //for calculator
          if(!manualTemp_txt_temperature.getText().toString().isEmpty() && cel == 0)
          {
            a=Double.parseDouble(String.valueOf(manualTemp_txt_temperature.getText()));
            Double b=a*9/5+32;
            String r=String.valueOf(b);
          //  Toast.makeText(getContext(),r+"°F",Toast.LENGTH_SHORT).show();
            manualTemp_txt_temperature.setText(r);
          //  Toast.makeText(getContext(),"cel"+celsius,Toast.LENGTH_SHORT).show();

          }

          if(!manualTemp_txt_temperature.getText().toString().isEmpty() && cel == 1)
          {
            a=Double.parseDouble(String.valueOf(txt_temperature.getText()));
            Double b=a-32;
            Double c=b*5/9;
            String r=String.valueOf(c);
          //  Toast.makeText(getContext(),r+"°C",Toast.LENGTH_SHORT).show();
            //   Toast.makeText(getContext(),"I am Fahrenheit",Toast.LENGTH_SHORT).show();
            manualTemp_txt_temperature.setText(r);
          //  Toast.makeText(getContext(),"cel"+celsius,Toast.LENGTH_SHORT).show();
          }




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

    @OnItemSelected(R.id.spin_number)
    void onRealationselected(AdapterView<?> parent, int position) {
        number = (String) parent.getItemAtPosition(position);
    }

    private void initData() {
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (!bluetoothAdapter.isEnabled()) {
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
//        }

        // enable the Bluetooth Adapter

        mBtController = BTController.getDefaultBTController(this);
        mBtController.registerBroadcastReceiver(this.getContext());
        mBtController.enableBtAdpter();

        mDataParser = new DataParser(this);
        mDataParser.start();







//public void onBlue() {
//    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//    if (!bluetoothAdapter.isEnabled()) {
//        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//        startActivityForResult(enableBtIntent, 200);
//
//
//
    }




//}
    // enable the Bluetooth Adapter



    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.btnBtCtr:
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




                break;
        } }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //    finish();
        System.exit(0); //for release "mBluetoothDevices" on key_back down
        mBtController.unregisterBroadcastReceiver(this.getContext());
    }

    //BTController implements
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


    //DataParser implements
    @Override
    public void onSpO2WaveReceived(int dat) {
        wfSpO2.addAmp(dat);
    }

    @Override
    public void onSpO2Received(final SpO2 spo2) {
        runOnUiThread(new Runnable() {
            @Override



            public void run() {

                String temp = spo2.toString();
                int number=0;
// here i have to add range
                //  if (spo2 >=90 && spo2<=100 )
                if(temp.contains("- -"))
                {

                    txt_pulse.setText(pulse); }


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
    public void onTempReceived(final Temp temp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txt_temperature.setText(temp.toString());
            }
        });
    }

    @Override
    public void onNIBPReceived(final NIBP nibp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String temp = nibp.toString();

                if (temp.contains("- -"))
                {
                    txt_cuff.setText(cuff);
                    txt_systolic.setText(systolic);
                    txt_diastolic.setText(diastolic);
                }else {
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
