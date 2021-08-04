package com.mdcbeta.patient;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdcbeta.R;

import java.util.ArrayList;

/**
 * Created by Shakil Karim on 4/1/17.
 */

public class PatientDrawerAdapter extends ArrayAdapter<DrawerItem> {

    private int excludeSelectionPoss;

    ArrayList<DrawerItem> data;
    Activity context;
    static final int Layout = R.layout.item_drawer;  // Set Layout

    public PatientDrawerAdapter(Activity context, ArrayList<DrawerItem> data) {
        super(context, Layout, data);
        this.context = context;
        this.data = data;
    }

    public PatientDrawerAdapter(Activity context, ArrayList<DrawerItem> data, int excludeSelectionPoss) {
        super(context, Layout, data);
        this.context = context;
        this.data = data;
        this.excludeSelectionPoss = excludeSelectionPoss;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        DrawerItem item = data.get(position);

        if (view == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(Layout, null);
        }

        ImageView img = (ImageView) view.findViewById(R.id.img);
        TextView text = (TextView) view.findViewById(R.id.txt_name);

        img.setImageResource(item.icon_resource);
        text.setText(item.name + "");

        return view;

    }


}
