package com.mdcbeta.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.mdcbeta.R;
import com.mdcbeta.view.WaveformView;

import de.hdodenhof.circleimageview.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shakil on 2/27/2017.
 */

public class ActionBar extends RelativeLayout {


    @BindView(R.id.menu)
    ImageButton menu;

    @BindView(R.id.profile_image)
    public CircleImageView profileImage;

    public ActionBar(Context context) {
        super(context);
        inits();
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inits();
    }

    private void inits() {
        inflate(getContext(), R.layout.view_action_bar, this);
        ButterKnife.bind(this);
    }


    public void setOnItemsClickListner(final ActionItemClickListener onActionClick) {
      //  waveformView.setVisibility(GONE);

        menu.setOnClickListener(v -> onActionClick.onItemClickID(v.getId())

        );
    }


    public interface ActionItemClickListener {
        void onItemClickID(int id);
    }


}
