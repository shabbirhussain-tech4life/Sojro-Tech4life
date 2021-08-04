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
import com.mdcbeta.data.remote.model.Exercise;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


/**
 * Created by MahaRani on 18/03/2018.
 */

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private final Context context;

    private List<JsonObject> items;
    public List<Exercise> mlist;
    private ExerciseAdapter.ItemClickListner itemClickListner;
    private String mQuery;
    private Filter mFilter;

    public ExerciseAdapter(Context context) {
        this.mlist = new ArrayList<>();
        this.items = new ArrayList<>();
        this.context = context;
    }


    public void setItemClickListner(ExerciseAdapter.ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }


    public void swap(List<Exercise> datas) {
        mlist.clear();
        mlist.addAll(datas);
        notifyDataSetChanged();
    }


    @Override
    public ExerciseAdapter.ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        return new ExerciseAdapter.ExerciseViewHolder(v);

    }


    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        try {
            Exercise item = mlist.get(position);
            holder.date.setText(item.getDate());
            holder.time.setText(item.getTime());
            holder.activity.setText(item.getActivity());




        }catch (Exception ex){}

    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }




    public class ExerciseViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.txt_activity)
        TextView activity;
        @BindView(R.id.btn_delete)
        ImageView delete_exercise;
        @BindView(R.id.view_exercise)
        ImageView view_exercise;
        @BindView(R.id.btn_edit)
        ImageView btn_edit;

        public ExerciseViewHolder(View itemView) {
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
        public void onItemClick(View view,Exercise item);
    }

    public interface ItemDelete {
        public void onClick(int pos);
    }


}
