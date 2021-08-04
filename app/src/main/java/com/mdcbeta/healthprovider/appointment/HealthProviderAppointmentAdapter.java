package com.mdcbeta.healthprovider.appointment;

import com.google.gson.JsonObject;

import android.content.Context;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mdcbeta.R;
import com.mdcbeta.healthprovider.MainHealthProviderActivity;
import com.mdcbeta.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


/**
 * Created by Shakil Karim on 4/23/17.
 */
public class HealthProviderAppointmentAdapter
        extends RecyclerView.Adapter<HealthProviderAppointmentAdapter.HealthProviderAppointmentViewHolder> {
    private final Context context;

    private List<JsonObject> mlist;
    private ItemClickListner itemClickListner;
    private VideoClickListener videoClickListener;


    public HealthProviderAppointmentAdapter(Context context) {
        this.mlist = new ArrayList<>();
        this.context = context;
    }


    public void swap(List<JsonObject> datas) {
        mlist.clear();
        mlist.addAll(datas);
        notifyDataSetChanged();
    }


    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public void setVideoClickListner(VideoClickListener videoClickListner) {
        this.videoClickListener = videoClickListner;
    }

    @Override
    public HealthProviderAppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_appointment, parent, false);
        return new HealthProviderAppointmentViewHolder(v);

    }

    @Override
    public void onBindViewHolder(HealthProviderAppointmentViewHolder holder, int position) {

        JsonObject item = mlist.get(position);

       if(item.get("case_code").getAsString().contains("no data")) {
            holder.case_code.setText("");
        }else {
            holder.case_code.setText(item.get("case_code").getAsString());
        }

        holder.txtDate.setText(Utils.formatDate(item.get("date").getAsString()));
        holder.txtTime.setText(Utils.format12HourTime(Utils.UTCTolocal(item.get("start_time").getAsString())));

        if(MainHealthProviderActivity.isReviewer) {
          //  holder.reffer_txt.setText("Referred by");
         //   holder.referrerName.setText(item.get("reffername").getAsString());
         //   holder.patient_name.setVisibility(View.GONE);
            holder.reffer_txt.setText("Referred by");
            holder.referrerName.setText(item.get("reffername").getAsString());

            if(item.get("fname").getAsString().trim().equalsIgnoreCase(item.get("lname").getAsString().trim())){
                holder.patient_name.setText(item.get("fname").getAsString());


            }else {
                holder.patient_name.setText(item.get("fname").getAsString() + " " + item.get("lname").getAsString());
            }

        }else {
        //    holder.reffer_txt.setText("Referred by");
        //    holder.referrerName.setText(item.get("doctorname").getAsString());

        //    if(item.get("fname").getAsString().trim().equalsIgnoreCase(item.get("lname").getAsString().trim())){
        //        holder.patient_name.setText(item.get("fname").getAsString());
        //    }else {
         //       holder.patient_name.setText(item.get("fname").getAsString() + " " + item.get("lname").getAsString());
         //   }

            holder.reffer_txt.setText("Referred to");
            holder.referrerName.setText(item.get("doctorname").getAsString());

            if(item.get("fname").getAsString().trim().equalsIgnoreCase(item.get("lname").getAsString().trim())){
                holder.patient_name.setText(item.get("fname").getAsString());
            }else {
                holder.patient_name.setText(item.get("fname").getAsString() + " " + item.get("lname").getAsString());
            }
        }



    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public class HealthProviderAppointmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.referrer_name)
        TextView referrerName;
        @BindView(R.id.btn_view_case)
        TextView btnViewCase;
        @BindView(R.id.video_icon)
        ImageView videoIcon;
        @BindView(R.id.reffer_txt)
        TextView reffer_txt;
        @BindView(R.id.case_code)
        TextView case_code;
        @BindView(R.id.patient_name)
        TextView patient_name;




        public HealthProviderAppointmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {
                    if (itemClickListner != null)
                        itemClickListner.onItemClick(mlist.get(pos));
                }
            });


            videoIcon.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {

                    if (videoClickListener != null) {

                        videoClickListener.onVideoIconClick(mlist.get(pos));
                    }
                }

            });

        }

    }

    public interface ItemClickListner {
        public void onItemClick(JsonObject item);

    }

    public interface VideoClickListener{
        public void onVideoIconClick(JsonObject item);
    }



}
