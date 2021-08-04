package com.mdcbeta.healthprovider;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdcbeta.R;
import com.mdcbeta.patient.DrawerItem;

import java.util.ArrayList;

/**
 * Created by Shakil Karim on 4/4/17.
 */

public class HealthProviderDrawerAdapter extends ArrayAdapter<DrawerItem> {



    ArrayList<DrawerItem> data;
    Activity context;
    static final int Layout = R.layout.item_drawer;  // Set Layout

    public HealthProviderDrawerAdapter(Activity context, ArrayList<DrawerItem> data) {
        super(context, Layout, data);
        this.context = context;
        this.data = data;
    }


    public void updateList( ArrayList<DrawerItem> data){
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
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
