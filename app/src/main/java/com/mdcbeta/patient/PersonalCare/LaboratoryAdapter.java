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
import com.mdcbeta.data.remote.model.Laboratory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


/**
 * Created by MahaRani on 28/06/2018.
 */

public class LaboratoryAdapter extends RecyclerView.Adapter<LaboratoryAdapter.LaboratoryViewHolder> {
    private final Context context;

    private List<JsonObject> items;
    public List<Laboratory> mlist;
    private LaboratoryAdapter.ItemClickListner itemClickListner;
    private String mQuery;
    private Filter mFilter;

    public LaboratoryAdapter(Context context) {
        this.mlist = new ArrayList<>();
        this.items = new ArrayList<>();
        this.context = context;
    }


    public void setItemClickListner(LaboratoryAdapter.ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }


    public void swap(List<Laboratory> datas) {
        mlist.clear();
        mlist.addAll(datas);
        notifyDataSetChanged();
    }


    @Override
    public LaboratoryAdapter.LaboratoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new LaboratoryAdapter.LaboratoryViewHolder(v);

    }


    @Override
    public void onBindViewHolder(LaboratoryAdapter.LaboratoryViewHolder holder, int position) {
        try {
            Laboratory item = mlist.get(position);
            holder.date.setText(item.getDate());
            holder.time.setText(item.getTime());
            holder.fasting.setText(item.getTest());
            holder.device.setText(item.getResult());
            holder.textView23.setText("Test");
            holder.textView24.setText("Result");
            holder.textView24.setVisibility(View.VISIBLE);
            holder.textView23.setVisibility(View.VISIBLE);
            holder.device.setVisibility(View.VISIBLE);




        }catch (Exception ex){}

    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }




    public class LaboratoryViewHolder extends RecyclerView.ViewHolder {


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

        public LaboratoryViewHolder(View itemView) {
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
        public void onItemClick(View view,Laboratory item);
    }

    public interface ItemDelete {
        public void onClick(int pos);
    }


}
