package com.mdcbeta.patient.PersonalCare;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.mdcbeta.R;
import com.mdcbeta.data.remote.model.BloodPressure;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


/**
 * Created by MahaRani on 25/03/2018.
 */

public class BloodpressureAdapter extends RecyclerView.Adapter<BloodpressureAdapter.BloodpressureViewHolder> {
    private final Context context;

    private List<JsonObject> items;
    public List<BloodPressure> mlist;
    private BloodpressureAdapter.ItemClickListner itemClickListner;
    private String mQuery;
    private Filter mFilter;

    public BloodpressureAdapter(Context context) {
        this.mlist = new ArrayList<>();
        this.items = new ArrayList<>();
        this.context = context;
    }


    public void setItemClickListner(BloodpressureAdapter.ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }


    public void swap(List<BloodPressure> datas) {
        mlist.clear();
        mlist.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public BloodpressureAdapter.BloodpressureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new BloodpressureAdapter.BloodpressureViewHolder(v);

    }


    @Override
    public void onBindViewHolder(BloodpressureAdapter.BloodpressureViewHolder holder, int position) {
        try {


            BloodPressure item = mlist.get(position);
            holder.date.setText(item.getBpDate());
            holder.time.setText(item.getBpTime());
            holder.systolic.setText(item.getSystolic());
            holder.diastolic.setText(item.getDiastolic());
            holder.textView23.setText("Systolic");
            holder.textView24.setText("Diastolic");
            holder.textView24.setVisibility(View.VISIBLE);
            holder.textView23.setVisibility(View.VISIBLE);
            holder.diastolic.setVisibility(View.VISIBLE);




        }catch (Exception ex){}

    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }




    public class BloodpressureViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.txt_activity)
        TextView systolic;
        @BindView(R.id.txt_diastolic)
        TextView diastolic;
        @BindView(R.id.textView23)
        TextView textView23;
        @BindView(R.id.textView24)
        TextView textView24;
        @BindView(R.id.btn_delete)
        ImageView delete_exercise;
        @BindView(R.id.view_exercise)
        ImageView view_exercise;
        @BindView(R.id.btn_edit)
        ImageView btn_edit;



        public BloodpressureViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);



            view_exercise.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {
                    if (itemClickListner != null)
                        itemClickListner.onItemClick(v,mlist.get(pos));

                }
            });

            delete_exercise.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {
                    if (itemClickListner != null)
                        itemClickListner.onItemClick(v,mlist.get(pos));

                }
            });

            btn_edit.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {
                    if (itemClickListner != null)
                        itemClickListner.onItemClick(v,mlist.get(pos));
                }
            });


        }

    }


    public interface ItemClickListner {
        public void onItemClick(View view,BloodPressure item);
    }

    public interface ItemDelete {
        public void onClick(int pos);
    }


}
