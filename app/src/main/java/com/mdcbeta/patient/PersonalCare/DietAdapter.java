package com.mdcbeta.patient.PersonalCare;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mdcbeta.R;
import com.mdcbeta.data.remote.model.Diets;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;

//import static android.support.v7.util.DiffUtil.DiffResult.NO_POSITION;



/**
 * Created by MahaRani on 29/03/2018.
 */

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.DietViewHolder> {
    private final Context context;


    public List<Diets> mlist;
    private DietAdapter.ItemClickListner itemClickListner;
    private String mQuery;
    private Filter mFilter;

    public DietAdapter(Context context) {
        this.mlist = new ArrayList<>();
       // this.items = new ArrayList<>();
        this.context = context;
    }

    public void setItemClickListner(DietAdapter.ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }


    public void swap(List<Diets> datas) {
        mlist.clear();
        mlist.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public DietAdapter.DietViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new DietAdapter.DietViewHolder(v);

    }


    @Override
    public void onBindViewHolder(DietAdapter.DietViewHolder holder, int position) {
        try {

            Diets item = mlist.get(position);
            holder.date.setText(item.getDate());
            holder.time.setText(item.getTime());
            holder.systolic.setText(item.getFoodtype());
            holder.textView23.setText("Food Type");
            holder.textView23.setVisibility(View.VISIBLE);


        }catch (Exception ex){}

    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }




    public class DietViewHolder extends RecyclerView.ViewHolder {

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



        public DietViewHolder(View itemView) {
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
        public void onItemClick(View view, Diets item);
    }

    public interface ItemDelete {
        public void onClick(int pos);
    }


}
