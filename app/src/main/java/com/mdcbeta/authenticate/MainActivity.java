package com.mdcbeta.authenticate;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mdcbeta.R;
import com.mdcbeta.bluetooth.BTController;
import com.mdcbeta.data.DataParser;
import com.mdcbeta.data.ECG;
import com.mdcbeta.data.NIBP;
import com.mdcbeta.data.Pulse;
import com.mdcbeta.data.SpO2;
import com.mdcbeta.data.Temp;
import com.mdcbeta.dialog.BluetoothDeviceAdapter;
import com.mdcbeta.dialog.SearchDevicesDialog;
import com.mdcbeta.view.WaveformView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;



public class MainActivity extends AppCompatActivity
        implements BTController.Listener, DataParser.onPackageReceivedListener,
        View.OnClickListener {

    private BTController mBtController;

    //UI
    private Button btnBtCtr;
    private TextView tvBtinfo;
    private TextView tvECGinfo;
    private TextView tvSPO2info;
    private TextView tvTEMPinfo;
    private TextView tvNIBPinfo;
    private LinearLayout llAbout;
    private TextView tvFWVersion;
    private TextView tvHWVersion;
    private WaveformView wfSpO2;
    private WaveformView wfECG;


    //Bluetooth
    BluetoothDeviceAdapter mBluetoothDeviceAdapter;
    SearchDevicesDialog mSearchDialog;
    ProgressDialog mConnectingDialog;
    ArrayList<BluetoothDevice> mBluetoothDevices;

    //data
    DataParser mDataParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this,
          Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this,
                  Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        )

            Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
        else
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    100);

        initData();
        initView();
    }


    private void initData() {
        // enable the Bluetooth Adapter
        mBtController = BTController.getDefaultBTController(this);
        mBtController.registerBroadcastReceiver(this);
        mBtController.enableBtAdpter();

        mDataParser = new DataParser(this);
        mDataParser.start();
    }

    private void initView() {
        //UI widgets
        btnBtCtr = (Button) findViewById(R.id.btnBtCtr);
        tvBtinfo = (TextView) findViewById(R.id.tvbtinfo);
        tvECGinfo = (TextView) findViewById(R.id.tvECGinfo);
        tvSPO2info = (TextView) findViewById(R.id.tvSPO2info);
        tvTEMPinfo = (TextView) findViewById(R.id.tvTEMPinfo);
        tvNIBPinfo = (TextView) findViewById(R.id.tvNIBPinfo);
        llAbout = (LinearLayout) findViewById(R.id.llAbout);
        tvFWVersion = (TextView) findViewById(R.id.tvFWverison);
        tvHWVersion = (TextView) findViewById(R.id.tvHWverison);


        btnBtCtr.setOnClickListener(this);

        //Bluetooth Search Dialog
        mBluetoothDevices = new ArrayList<>();
//        mBluetoothDeviceAdapter = new BluetoothDeviceAdapter(MainActivity.this, mBluetoothDevices);
        mSearchDialog = new SearchDevicesDialog(MainActivity.this, mBluetoothDeviceAdapter) {
            @Override
            public void onStartSearch() {
                mBtController.startScan(true);
            }

            @Override
            public void onClickDeviceItem(int pos) {
                BluetoothDevice device = mBluetoothDevices.get(pos);
                mBtController.startScan(false);
                mBtController.connect(MainActivity.this, device);
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

        mConnectingDialog = new ProgressDialog(MainActivity.this);
        mConnectingDialog.setMessage("Connecting...");

        //About Information
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtController.write(DataParser.CMD_FW_VERSION);
                mBtController.write(DataParser.CMD_HW_VERSION);
            }
        });

        //SpO2 & ECG waveform
        wfSpO2 = (WaveformView) findViewById(R.id.wfSpO2);
        wfECG = (WaveformView) findViewById(R.id.wfECG);

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
    protected void onDestroy() {
        super.onDestroy();
        finish();
        System.exit(0); //for release "mBluetoothDevices" on key_back down
        mBtController.unregisterBroadcastReceiver(this);
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

