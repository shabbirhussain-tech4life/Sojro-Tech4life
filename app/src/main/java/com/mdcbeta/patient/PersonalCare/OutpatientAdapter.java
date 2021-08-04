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
import com.mdcbeta.data.remote.model.Outpatient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


/**
 * Created by MahaRani on 28/06/2018.
 */

public class OutpatientAdapter extends RecyclerView.Adapter<OutpatientAdapter.OutpatientViewHolder> {
    private final Context context;

    private List<JsonObject> items;
    public List<Outpatient> mlist;
    private OutpatientAdapter.ItemClickListner itemClickListner;
    private String mQuery;
    private Filter mFilter;

    public OutpatientAdapter(Context context) {
        this.mlist = new ArrayList<>();
        this.items = new ArrayList<>();
        this.context = context;
    }


    public void setItemClickListner(OutpatientAdapter.ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }


    public void swap(List<Outpatient> datas) {
        mlist.clear();
        mlist.addAll(datas);
        notifyDataSetChanged();
    }


    @Override
    public OutpatientAdapter.OutpatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bmi, parent, false);
        return new OutpatientAdapter.OutpatientViewHolder(v);

    }


    @Override
    public void onBindViewHolder(OutpatientAdapter.OutpatientViewHolder holder, int position) {
        try {
            Outpatient item = mlist.get(position);
            holder.date.setText(item.getDate());
            holder.textView28.setText("Type");
            holder.textView23.setText("Health Facility");
            holder.textView24.setText("Health Provider");
            holder.time.setText(item.getType());
            holder.txt_height.setText(item.getHealthfacility());
            holder.textView25.setVisibility(View.INVISIBLE);
            holder.txt_weight.setText(item.getHealthprovider());





        }catch (Exception ex){}

    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }




    public class OutpatientViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.textView28)
        TextView textView28;
        @BindView(R.id.textView23)
        TextView textView23;
        @BindView(R.id.textView24)
        TextView textView24;
        @BindView(R.id.textView25)
        TextView textView25;
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

        public OutpatientViewHolder(View itemView) {
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
        public void onItemClick(View view,Outpatient item);
    }

    public interface ItemDelete {
        public void onClick(int pos);
    }


}
