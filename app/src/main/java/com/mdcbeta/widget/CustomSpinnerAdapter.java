package com.mdcbeta.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.mdcbeta.R;

import java.util.List;

/**
 * Created by Shakil Karim on 5/7/17.
 */

public class CustomSpinnerAdapter<T> extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private List<T> asr;

    public CustomSpinnerAdapter(Context context,List<T> asr) {
        this.asr = asr;
        activity = context;
    }



    public int getCount()
    {
        if(asr == null )
            return 0;
        else
        return asr.size();
    }

    public T getItem(int i)
    {
        return asr.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }



    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(activity);
        txt.setPadding(5, 5, 5, 5);
        txt.setMinHeight(40);
        txt.setTextSize(18);
        txt.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        txt.setText(asr.get(position).toString());
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

    public View getView(int i, View view, ViewGroup viewgroup) {
        TextView txt = new TextView(activity);
        txt.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        txt.setPadding(5, 10, 5, 10);
        txt.setTextSize(16);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_action_name, 0);
        txt.setText(asr.get(i).toString());
        txt.setTextColor(Color.parseColor("#000000"));
        return  txt;
    }

}
