package com.mdcbeta.healthprovider.cases.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mdcbeta.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    private final Context context;
    public static final String FIRST_COLUMN="First";
    public static final String SECOND_COLUMN="Second";
    public static final String THIRD_COLUMN="Third";

    public ListViewAdapter(Context context, ArrayList<HashMap<String, String>> list){
        super();
        this.context = context;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public class ViewHolder{
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder;

      //  LayoutInflater inflater=this.context.getLayoutInflater();

        if(convertView == null){

            convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.colmn_row, parent, false);
            holder=new ViewHolder();

            holder.txtFirst=(TextView) convertView.findViewById(R.id.TextFirst);
            holder.txtSecond=(TextView) convertView.findViewById(R.id.TextSecond);
            holder.txtThird=(TextView) convertView.findViewById(R.id.TextThird);

            convertView.setTag(holder);
        }else{

            holder=(ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map=list.get(position);
        holder.txtFirst.setText(map.get(FIRST_COLUMN));
        holder.txtSecond.setText(map.get(SECOND_COLUMN));
        holder.txtThird.setText(map.get(THIRD_COLUMN));

        return convertView;
    }

}
