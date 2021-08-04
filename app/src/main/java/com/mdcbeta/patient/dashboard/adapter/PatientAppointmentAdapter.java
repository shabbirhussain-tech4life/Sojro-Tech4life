package com.mdcbeta.patient.dashboard.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.mdcbeta.R;
import com.mdcbeta.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


/**
 * Created by MahaRani on 03/06/2018.
 */

public class PatientAppointmentAdapter extends RecyclerView.Adapter<PatientAppointmentAdapter.PatientAppointmentViewHolder> {
    private final Context context;

    private List<JsonObject> mlist;
    private PatientAppointmentAdapter.ItemClickListner itemClickListner;
    private PatientAppointmentAdapter.VideoClickListener videoClickListener;


    public PatientAppointmentAdapter(Context context) {
        this.mlist = new ArrayList<>();
        this.context = context;
    }


    public void swap(List<JsonObject> datas) {
        mlist.clear();
        mlist.addAll(datas);
        notifyDataSetChanged();
    }


    public void setItemClickListner(PatientAppointmentAdapter.ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public void setVideoClickListner(PatientAppointmentAdapter.VideoClickListener videoClickListner) {
        this.videoClickListener = videoClickListner;
    }

    @Override
    public PatientAppointmentAdapter.PatientAppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patent_appoint, parent, false);
        return new PatientAppointmentAdapter.PatientAppointmentViewHolder(v);

    }

    @Override
    public void onBindViewHolder(PatientAppointmentAdapter.PatientAppointmentViewHolder holder, int position) {

        JsonObject item = mlist.get(position);


        holder.txtDate.setText(Utils.formatDate(item.get("date").getAsString()));
        holder.txtTime.setText(Utils.format12HourTime(Utils.UTCTolocal(item.get("start_time").getAsString())));
        holder.patient_name.setVisibility(View.GONE);
        holder.case_code.setVisibility(View.GONE);
        holder.textView32.setVisibility(View.GONE);
        holder.reffer_txt.setText("Doctor Name:");
        holder.referrerName.setText(item.get("name").getAsString());
        holder.txt_status.setText(item.get("status").getAsString());

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public class PatientAppointmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.referrer_name)
        TextView referrerName;
        @BindView(R.id.btn_view_case)
        TextView btnViewCase;
        @BindView(R.id.reffer_txt)
        TextView reffer_txt;
        @BindView(R.id.case_code)
        TextView case_code;
        @BindView(R.id.patient_name)
        TextView patient_name;
        @BindView(R.id.textView32)
        TextView textView32;
        @BindView(R.id.txt_status)
        TextView txt_status;
        @BindView(R.id.video_icon)
        ImageView videoIcon;


        public PatientAppointmentViewHolder(View itemView) {
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

//                        String created = mlist.get(pos).created;
//                        String md5String = Utils.md5(created);
//                        Log.d(TAG, "date: "+created);
//                        Log.d(TAG, "md5date: "+md5String);



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
