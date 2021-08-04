package com.mdcbeta.healthprovider.patientmonitor;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.mdcbeta.view.WaveformView;
import com.mdcbeta.widget.ActionBar;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;


public class PatientMonitorFragment extends BaseFragment
        implements BTController.Listener,
        DataParser.onPackageReceivedListener,
        View.OnClickListener {

    private BTController mBtController;


    @BindView(R.id.btnBtCtr)
    Button btnBtCtr;
    @BindView(R.id.tvbtinfo)
    TextView tvBtinfo;
    @BindView(R.id.tvECGinfo)
    TextView tvECGinfo;
    @BindView(R.id.tvSPO2info)
    TextView tvSPO2info;
    @BindView(R.id.tvTEMPinfo)
    TextView tvTEMPinfo;
    @BindView(R.id.tvNIBPinfo)
    TextView tvNIBPinfo;
    @BindView(R.id.tvFWverison)
    TextView tvFWVersion;
    @BindView(R.id.tvHWverison)
    TextView tvHWVersion;
    @BindView(R.id.wfSpO2)
    WaveformView wfSpO2;
    @BindView(R.id.wfECG)
    WaveformView wfECG;
    @BindView(R.id.llAbout)
    LinearLayout llAbout;


    //Bluetooth
    BluetoothDeviceAdapter mBluetoothDeviceAdapter;
    SearchDevicesDialog mSearchDialog;
    ProgressDialog mConnectingDialog;
    ArrayList<BluetoothDevice> mBluetoothDevices;

    //data
    DataParser mDataParser;

    public PatientMonitorFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        PatientMonitorFragment fragment = new PatientMonitorFragment();
        return fragment;
    }


    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this.getContext(), "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
        else
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 100);

        initData();
        initView();
    }


        private void initData() {
        // enable the Bluetooth Adapter
        mBtController = BTController.getDefaultBTController(this);
        mBtController.registerBroadcastReceiver(this.getContext());
        mBtController.enableBtAdpter();

        mDataParser = new DataParser(this);
        mDataParser.start();
    }


    private void initView() {


        btnBtCtr.setOnClickListener( this);

        //Bluetooth Search Dialog
        mBluetoothDevices = new ArrayList<>();
//        mBluetoothDeviceAdapter = new BluetoothDeviceAdapter(this.getContext(), mBluetoothDevices);
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
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtController.write(DataParser.CMD_FW_VERSION);
                mBtController.write(DataParser.CMD_HW_VERSION);
            }
        });


    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBtCtr:
                if (!mBtController.isBTConnected()) {
                    mSearchDialog.show();
                    mSearchDialog.startSearch();
                    mBtController.startScan(true);
                } else {
                    mBtController.disconnect();
                    tvBtinfo.setText("");
                }
                break;
//            case R.id.btnNIBPStart:
//                mBtController.write(DataParser.CMD_START_NIBP);
//                break;
//            case R.id.btnNIBPStop:
//                mBtController.write(DataParser.CMD_STOP_NIBP);
//                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //finish();
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
                tvSPO2info.setText(spo2.toString());
            }
        });
    }

    @Override
    public void onPulseReceived(Pulse pulse) {

    }

//    @Override
//    public void onECGWaveReceived(int dat) {
//        wfECG.addAmp(dat);
//    }
//
//    @Override
//    public void onECGReceived(final ECG ecg) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                tvECGinfo.setText(ecg.toString());
//            }
//        });
//    }

    @Override
    public void onTempReceived(final Temp temp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTEMPinfo.setText(temp.toString());
            }
        });
    }

    @Override
    public void onNIBPReceived(final NIBP nibp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvNIBPinfo.setText(nibp.toString());
            }
        });
    }

//    @Override
//    public void onFirmwareReceived(final String str) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                tvFWVersion.setText("Firmware Version:" + str);
//            }
//        });
//    }
//
//    @Override
//    public void onHardwareReceived(final String str) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                tvHWVersion.setText("Hardware Version:" + str);
//            }
//        });
//    }

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
