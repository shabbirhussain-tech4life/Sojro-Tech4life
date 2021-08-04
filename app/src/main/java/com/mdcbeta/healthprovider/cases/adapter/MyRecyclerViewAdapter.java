package com.mdcbeta.healthprovider.cases.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mdcbeta.R;
import com.mdcbeta.data.remote.model.DataModel;

import java.util.List;

/**
 * Created by MahaRani on 05/12/2017.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";

    private List<DataModel> mDataset;
    // private static MyClickListener myClickListener;

    // private DBMainAppointmentAdapter.VideoClickListener videoClickListener;

    public MyRecyclerViewAdapter(List<DataModel> myDataset) {
        mDataset = myDataset;
    }


    public class DataObjectHolder extends RecyclerView.ViewHolder
    {
        TextView patientid;
        TextView referrer;
        TextView name;
        TextView location;
        TextView dateoforigin;
        TextView referer;
        TextView dateofreview;


        public DataObjectHolder(View itemView) {
            super(itemView);

            patientid = (TextView) itemView.findViewById(R.id.txt_case_id);
            name = (TextView) itemView.findViewById(R.id.txt_name);
            location = (TextView) itemView.findViewById(R.id.txt_location);
            referrer = (TextView) itemView.findViewById(R.id.reffer_name);
            dateoforigin = (TextView) itemView.findViewById(R.id.txt_time);
            referer = (TextView) itemView.findViewById(R.id.textView19);
            dateofreview = (TextView) itemView.findViewById(R.id.txt_date_of_review);


       /*     itemView.setClickable(true);
            itemView.setFocusableInTouchMode(true);

            itemView.setOnClickListener(v -> {

                 //   Toast.makeText(itemView.getContext(), "Position: " + Integer.toString(getAdapterPosition()), Toast.LENGTH_LONG).show();
              //  Toast.makeText(itemView.getContext(), "Position: " + patientid.getText(), Toast.LENGTH_LONG).show();

                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {
                    if (myClickListener != null)

                        myClickListener.onItemClick(getAdapterPosition());



                }
            });


            Log.i(LOG_TAG, "Adding Listener");
(q(
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {
                    if (myClickListener != null)

                        myClickListener.onItemClick(getAdapterPosition());



                }
            });*/
        }

       /* @Override
        public void onClick(View v) {

            myClickListener.onItemClick(getAdapterPosition());
        }*/
    }

  /*  public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;


    }*/


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_cases, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        holder.patientid.setText(mDataset.get(position).getPatientid());
        holder.name.setText(mDataset.get(position).getFirstname()+" "+mDataset.get(position).getLastname());
        holder.location.setText(mDataset.get(position).getLocation());
        holder.referrer.setText(mDataset.get(position).getReferrer());
        holder.dateoforigin.setText(mDataset.get(position).getOrigindate());
        holder.dateofreview.setText(mDataset.get(position).getOrigindate());
        holder.referer.setVisibility(View.INVISIBLE);
        holder.referrer.setVisibility(View.INVISIBLE);

    }

    public void addItem(DataModel dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position);
    }
}
