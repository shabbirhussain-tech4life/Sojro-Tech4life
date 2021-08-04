package com.mdcbeta.patient.caregiver;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mdcbeta.R;
import com.mdcbeta.data.remote.model.CareGiver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shakil Karim on 4/4/17.
 */
public class CareGiverAdapter extends RecyclerView.Adapter<CareGiverAdapter.CareGiverViewHolder> {
    private final Context context;

    private List<CareGiver> items;

    public CareGiverAdapter(Context context) {
        items = new ArrayList<>();
        this.context = context;
    }



    public void swap(List<CareGiver> careGivers){
        items.clear();
        items.addAll(careGivers);
        notifyDataSetChanged();
    }


    @Override
    public CareGiverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_care_giver, parent, false);
        return new CareGiverViewHolder(v);

    }

    @Override
    public void onBindViewHolder(CareGiverViewHolder holder, int position) {
        CareGiver item = items.get(position);
        holder.name.setText(item.getName());
        holder.email.setText(item.getEmail());
        holder.mobile.setText(item.getMobile());
        holder.txt_share_option.setText(item.getSendTo());
        holder.realtion.setText(item.getRelation());

    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
        //return 40;
    }


    public class CareGiverViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.mobile)
        TextView mobile;
        @BindView(R.id.email)
        TextView email;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.txt_relationship)
        TextView realtion;
        @BindView(R.id.txt_share_option)
        TextView txt_share_option;

        public CareGiverViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }


}
