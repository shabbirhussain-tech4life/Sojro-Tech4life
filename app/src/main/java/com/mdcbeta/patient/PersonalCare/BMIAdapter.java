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
import com.mdcbeta.data.remote.model.BMI;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


/**
 * Created by MahaRani on 27/06/2018.
 */

public class BMIAdapter extends RecyclerView.Adapter<BMIAdapter.BMIViewHolder> {
    private final Context context;

    private List<JsonObject> items;
    public List<BMI> mlist;
    private BMIAdapter.ItemClickListner itemClickListner;
    private String mQuery;
    private Filter mFilter;

    public BMIAdapter(Context context) {
        this.mlist = new ArrayList<>();
        this.items = new ArrayList<>();
        this.context = context;
    }


    public void setItemClickListner(BMIAdapter.ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }


    public void swap(List<BMI> datas) {
        mlist.clear();
        mlist.addAll(datas);
        notifyDataSetChanged();
    }


    @Override
    public BMIAdapter.BMIViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bmi, parent, false);
        return new BMIAdapter.BMIViewHolder(v);

    }


    @Override
    public void onBindViewHolder(BMIAdapter.BMIViewHolder holder, int position) {
        try {
            BMI item = mlist.get(position);
            holder.date.setText(item.getDate());
            holder.time.setText(item.getTime());
            holder.txt_height.setText(item.getHeight_feet()+ item.getHeight_inches());
            holder.txt_weight.setText(item.getWeight());
            holder.txt_calculate.setText(item.getCalculated_bmi());




        }catch (Exception ex){}

    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }




    public class BMIViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.txt_height)
        TextView txt_height;
        @BindView(R.id.txt_weight)
        TextView txt_weight;
        @BindView(R.id.txt_calculate)
        TextView txt_calculate;
        @BindView(R.id.btn_delete)
        ImageView delete_exercise;
        @BindView(R.id.view_exercise)
        ImageView view_exercise;
        @BindView(R.id.btn_edit)
        ImageView btn_edit;

        public BMIViewHolder(View itemView) {
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
        public void onItemClick(View view,BMI item);
    }

    public interface ItemDelete {
        public void onClick(int pos);
    }


}
