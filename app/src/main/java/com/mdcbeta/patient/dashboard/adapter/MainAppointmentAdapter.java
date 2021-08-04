package com.mdcbeta.patient.dashboard.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mdcbeta.R;
import com.mdcbeta.data.model.MainAppointment;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.util.Utils;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


/**
 * Created by Shakil Karim on 4/18/17.
 */
public class MainAppointmentAdapter extends RecyclerView.Adapter<MainAppointmentAdapter.MainAppointmentViewHolder> {
    private final Context context;
    private List<MainAppointment> mlist;
    private ItemClickListner itemClickListner;
    private VideoClickListener videoClickListener;

    private static final String TAG = "MainAppointmentAdapter";

    public MainAppointmentAdapter(Context context) {
        this.mlist = new ArrayList<>();
        this.context = context;
    }


    public void swap(List<MainAppointment> datas) {
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
    public MainAppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dashboard, parent, false);
        return new MainAppointmentViewHolder(v);

    }

    @Override
    public void onBindViewHolder(MainAppointmentViewHolder holder, int position) {
        MainAppointment item = mlist.get(position);
        holder.docName.setText(item.name);
        holder.date.setText(Utils.formatDate(item.date));
        holder.time.setText(Utils.format12HourTime(item.start_time));

        Picasso.with(context)
                .load(AppModule.profileimage+item.image)
                .placeholder(R.drawable.image_placeholder).into(holder.profile_image);



    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public class MainAppointmentViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.doc_name)
        TextView docName;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.video_icon)
        ImageView videoIcon;
        @BindView(R.id.profile_image)
        CircleImageView profile_image;

        public MainAppointmentViewHolder(View itemView) {
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

                        videoClickListener.onVideoIconClick(mlist.get(pos).record_id);
                    }
                }

            });




        }

    }

    public interface ItemClickListner {
        public void onItemClick(MainAppointment item);
    }
    public interface VideoClickListener{
        public void onVideoIconClick(String url);
    }


}
