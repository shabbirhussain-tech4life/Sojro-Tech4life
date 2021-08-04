package com.mdcbeta.util;

import androidx.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.mdcbeta.R;
import com.mdcbeta.di.AppModule;
import com.squareup.picasso.Picasso;

/**
 * Created by Shakil Karim on 5/29/17.
 */

public class UiBinder {


    @BindingAdapter("imageUrl")
    public static void loadImagefromFile(ImageView view, String imageUrl) {
        if(!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(view.getContext()).load(AppModule.profileimage+imageUrl).error(R.drawable.placeholder).into(view);
        }
    }


}
