package com.mdcbeta.dialog;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mdcbeta.R;
import com.mdcbeta.bluetooth.BTController;

import java.util.ArrayList;
import java.util.Set;



public abstract class SearchDevicesDialog extends Dialog  {


    public ListView lvBluetoothDevices;
    private ProgressBar pbSearchDevices;
    private Button btnSearchDevices;
  private  BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
    private ArrayAdapter aAdapter;


    public SearchDevicesDialog(Context context, BluetoothDeviceAdapter adapter) {
        super(context);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.devices_dialog);

        lvBluetoothDevices = (ListView) findViewById(R.id.lvBluetoothDevices);
        lvBluetoothDevices.setAdapter(adapter);
        lvBluetoothDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickDeviceItem(position);
            }
        });

        pbSearchDevices = (ProgressBar) findViewById(R.id.pbSearchDevices);
        btnSearchDevices = (Button) findViewById(R.id.btnSearchDevices);
        btnSearchDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearch();
            }
        });
    }

    public void stopSearch() {
        pbSearchDevices.setVisibility(View.GONE);
        btnSearchDevices.setVisibility(View.VISIBLE);
    }

    public void startSearch() {
        onStartSearch();
        pbSearchDevices.setVisibility(View.VISIBLE);
        btnSearchDevices.setVisibility(View.GONE);
    }

    public abstract void onStartSearch();

    public abstract void onClickDeviceItem(int pos);


//    public void paired() {
//
//        Set<BluetoothDevice> pairedDevices = bAdapter.getBondedDevices();
//        ArrayList list = new ArrayList();
//        if (pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                String devicename = device.getName();
//                String macAddress = device.getAddress();
//                list.add("Name: " + devicename + "MAC Address: " + macAddress);
//            }
//
//            lvBluetoothDevices = (ListView) findViewById(R.id.lvBluetoothDevices);
//            aAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
//            lvBluetoothDevices.setAdapter(aAdapter);
//
//
//        }
//    }
}
