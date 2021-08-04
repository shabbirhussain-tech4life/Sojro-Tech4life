package com.mdcbeta.patient.appointment;



import android.content.Context;
import android.database.DataSetObserver;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;

import androidx.recyclerview.widget.RecyclerView;

import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.mdcbeta.R;
import com.mdcbeta.healthprovider.cases.adapter.ImagePreviewAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.DiffUtil.DiffResult.NO_POSITION;


public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ImagePreviewViewHolder> implements ListAdapter {

    private final Context context;

    private List<ChosenImage> items;
    private ImagePreviewAdapter.ItemClickListner itemClickListner;



    public MediaAdapter(Context context) {
        this.items = new ArrayList<>();
        this.context = context;
    }


    public void setItemClickListner(ImagePreviewAdapter.ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public void addImage(List<ChosenImage> files){
        for (int i = 0; i < files.size(); i++) {
            this.items.add(files.get(i));
        }
        notifyDataSetChanged();
    }





    public List<ChosenImage> getFiles(){
        return this.items;
    }



    @Override
    public ImagePreviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_images, parent, false);
       return new ImagePreviewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImagePreviewViewHolder holder, int position) {

        ChosenImage file = items.get(position);

        if (file.getThumbnailSmallPath() != null) {
            Picasso.with(context).load(Uri.fromFile(new File(file.getThumbnailSmallPath())))
                    .into(holder.ivImage);
        }


    }

    @Override
    public int getItemCount() {
       // return 0;
        return items == null ? 0 : items.size();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


    public class ImagePreviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivImage)
        ImageView ivImage;
        @BindView(R.id.cross)
        ImageView cross;

        public ImagePreviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != NO_POSITION) {
                    if (itemClickListner != null)
                        itemClickListner.onItemClick(items.get(pos));
                }
            });
            cross.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                items.remove(pos);
                notifyItemRemoved(pos);
                notifyDataSetChanged();

            });


        }

    }
    public interface ItemClickListner {
        public void onItemClick(ChosenImage item);

    }
}
