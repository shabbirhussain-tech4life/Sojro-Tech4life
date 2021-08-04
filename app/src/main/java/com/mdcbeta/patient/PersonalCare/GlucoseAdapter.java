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
import com.mdcbeta.data.remote.model.Glucose;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


/**
 * Created by MahaRani on 26/03/2018.
 */

public class GlucoseAdapter extends RecyclerView.Adapter<GlucoseAdapter.GlucoseViewHolder> {
    private final Context context;

    private List<JsonObject> items;
    public List<Glucose> mlist;
    private GlucoseAdapter.ItemClickListner itemClickListner;
    private String mQuery;
    private Filter mFilter;

    public GlucoseAdapter(Context context) {
        this.mlist = new ArrayList<>();
        this.items = new ArrayList<>();
        this.context = context;
    }


    public void setItemClickListner(GlucoseAdapter.ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }


    public void swap(List<Glucose> datas) {
        mlist.clear();
        mlist.addAll(datas);
        notifyDataSetChanged();
    }


    @Override
    public GlucoseAdapter.GlucoseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new GlucoseAdapter.GlucoseViewHolder(v);

    }


    @Override
    public void onBindViewHolder(GlucoseAdapter.GlucoseViewHolder holder, int position) {
        try {
            Glucose item = mlist.get(position);
            holder.date.setText(item.getDate());
            holder.time.setText(item.getTime());
            holder.fasting.setText(item.getFasting());
            holder.device.setText(item.getDevice());
            holder.textView23.setText("Test");
            holder.textView24.setText("Device");
            holder.textView24.setVisibility(View.VISIBLE);
            holder.textView23.setVisibility(View.VISIBLE);
            holder.device.setVisibility(View.VISIBLE);




        }catch (Exception ex){}

    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }




    public class GlucoseViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.txt_activity)
        TextView fasting;
        @BindView(R.id.txt_diastolic)
        TextView device;
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

        public GlucoseViewHolder(View itemView) {
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
        public void onItemClick(View view,Glucose item);
    }

    public interface ItemDelete {
        public void onClick(int pos);
    }


}
