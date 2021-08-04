package com.mdcbeta.patient.dashboard.adapter;

import android.content.Context;
import com.mdcbeta.R;
import com.mdcbeta.data.model.MainAppointment;
import com.mdcbeta.databinding.ItemDashboardBinding;
import com.mdcbeta.di.AppModule;
import com.mdcbeta.util.MyBaseAdapter;
import com.mdcbeta.util.Utils;
import com.squareup.picasso.Picasso;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


/**
 * Created by Shakil Karim on 6/2/17.
 */

public class DBMainAppointmentAdapter extends MyBaseAdapter<MainAppointment,ItemDashboardBinding> {

    private VideoClickListener videoClickListener;


    public DBMainAppointmentAdapter(Context context) {
        super(context);
    }



    public void setVideoClickListner(VideoClickListener videoClickListner) {
        this.videoClickListener = videoClickListner;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.item_dashboard;
    }

    @Override
    protected void bindData(ItemDashboardBinding dataBinding, MainAppointment item, int position) {

        dataBinding.docName.setText(item.name);
        dataBinding.date.setText(Utils.formatedDate(item.date));
        dataBinding.time.setText(Utils.format12HourTime(Utils.UTCTolocal(item.start_time)));

        Picasso.with(context).load(AppModule.profileimage+item.image)
                .placeholder(R.drawable.image_placeholder).into(dataBinding.profileImage);

        dataBinding.getRoot().setOnClickListener(v -> {
            if (position != NO_POSITION && itemClickListner !=null) {
                itemClickListner.onItemClick(item);
            }
        });

        dataBinding.videoIcon.setOnClickListener(v -> {
            if (position != NO_POSITION) {
                if (videoClickListener != null) {
                    videoClickListener.onVideoIconClick(item.room_url);
                }
            }

        });


    }



    public interface VideoClickListener{
        public void onVideoIconClick(String url);
    }






}
