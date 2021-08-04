package com.mdcbeta.patient.appointment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.mdcbeta.R;
import com.mdcbeta.healthprovider.cases.adapter.ImagePreviewAdapter;
import com.mdcbeta.healthprovider.cases.adapter.ListViewAdapter;
import com.squareup.picasso.Picasso;
import com.stfalcon.frescoimageviewer.adapter.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.mdcbeta.R.id.*;

/**
 * Created by Shakil Karim on 4/2/17.
 */

public class MediaResultsAdapter extends BaseAdapter  {
    private final static String TAG = MediaResultsAdapter.class.getSimpleName();





    private final static int TYPE_IMAGE = 0;
    private final static int TYPE_VIDEO = 1;
    private final static int TYPE_FILE = 2;
    private final static int TYPE_CONTACT = 3;
    private final static int TYPE_AUDIO = 4;

    private final static String FORMAT_IMAGE_VIDEO_DIMENSIONS = "%sw x %sh";
    private final static String FORMAT_ORIENTATION = "Ortn: %s";
    private final static String FORMAT_DURATION = "%s";

    private final Context context;
    private List<ChosenImage> files;

    public MediaResultsAdapter(Context context) {
        this.files = new ArrayList<>();
        this.context = context;
    }

    public void addImage(List<ChosenImage> files){
        for (int i = 0; i < files.size(); i++) {
            this.files.add(files.get(i));
        }
        notifyDataSetChanged();

    }

    public void clearAll(){
        this.files.clear();
        notifyDataSetChanged();
    }


    public List<ChosenImage> getFiles(){
        return this.files;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        Log.d(TAG, "getView: " + files.size());
        ChosenFile file = (ChosenFile) getItem(position);
        int itemViewType = getItemViewType(position);
        if (convertView == null) {
            switch (itemViewType) {
                case TYPE_IMAGE:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_images, null);
                    // added by kk 1/13/2020
                    ImageView img = (ImageView)convertView.findViewById(R.id.ivImage);
                    ImageView cross = (ImageView)convertView.findViewById(R.id.cross);


cross.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

       files.remove(file);
notifyDataSetChanged();
    }
});


                    break;
//
            }
        }

        switch (itemViewType) {
            case TYPE_IMAGE:
                showImage(file, convertView);
                break;

        }


        return convertView;






    }



    private void showImage(ChosenFile file, View view) {
        final ChosenImage image = (ChosenImage) file;

        ImageView ivImage = (ImageView) view.findViewById(R.id.ivImage);
       ImageView cross = (ImageView) view.findViewById(R.id.cross);

        if (image.getThumbnailPath() != null) {
           // Picasso.with().load(Uri.fromFile(new File(image.getThumbnailPath()))).into(ivImage);
            Picasso.with(context).load(Uri.fromFile(new File(image.getThumbnailPath()))).into(ivImage);
        }
//        cross.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }



//    private void showFile(ChosenFile file, View view) {
//
//        TextView tvName = (TextView) view.findViewById(R.id.tvName);
//        tvName.setText(file.getDisplayName());
//
//        TextView tvCompleteMimeType = (TextView) view.findViewById(R.id.tvCompleteMimeType);
//        tvCompleteMimeType.setText(file.getMimeType());
//
//        TextView tvMimeType = (TextView) view.findViewById(R.id.tvMimeType);
//        tvMimeType.setText(file.getFileExtensionFromMimeTypeWithoutDot());
//
//        TextView tvSize = (TextView) view.findViewById(R.id.tvSize);
//        tvSize.setText(file.getHumanReadableSize(false));
//
//    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        String type = ((ChosenFile) getItem(position)).getType();
        switch (type) {
            case "image":
                return TYPE_IMAGE;
            case "file":
                return TYPE_FILE;
            case "video":
                return TYPE_VIDEO;
            case "audio":
                return TYPE_AUDIO;
        }
        return TYPE_FILE;
    }






}
