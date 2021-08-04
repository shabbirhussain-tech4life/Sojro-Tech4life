package com.mdcbeta.widget;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mdcbeta.R;
import com.mdcbeta.data.remote.model.DayTime;
import com.mdcbeta.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Shakil Karim on 4/16/17.
 */

public class ScheduleItem1 extends FrameLayout implements DayTimeProvider{

    Activity mactivity;
    @BindView(R.id.to_txt)
    public TextView toTxt;
    @BindView(R.id.to_time)
     LinearLayout toTime;
    @BindView(R.id.from_txt)
    public  TextView fromTxt;
    @BindView(R.id.from_time)
    LinearLayout fromTime;
    @BindView(R.id.to_txt_am_pm)
    public TextView toTxtAmPm;
    @BindView(R.id.from_txt_am_pm)
    public TextView fromTxtAmPm;


    @BindView(R.id.open_con_check)
    public CheckBox openConCheck;
    @BindView(R.id.add)
    ImageButton add;

    private String isOpen = "0";
    public int id;

    ScheduleItem1ItemClickListener scheduleItem1ItemClickListener;

    public ScheduleItem1(Context context,Activity activity) {
        super(context);
        mactivity = activity;
        inits();
    }

    public ScheduleItem1(Context context, AttributeSet attrs) {
        super(context, attrs);
        inits();
    }

    private void inits() {
        inflate(getContext(), R.layout.include_fragment_schedule_init_item,this);
        ButterKnife.bind(this);
        toTxt.setText("09:00");
        fromTxt.setText("10:00");
        toTxtAmPm.setText("09:00 AM");
        fromTxtAmPm.setText("10:00 AM");
    }



    public void setOnItemsClickListner(final ScheduleItem1ItemClickListener onActionClick) {
        this.scheduleItem1ItemClickListener = onActionClick;
    }

    @OnClick({R.id.to_time, R.id.from_time,R.id.add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.to_time:
                Utils.setTime(mactivity, date -> {
                    toTxt.setText(date);
                    toTxtAmPm.setText(Utils.format12HourTime(date));
                    this.scheduleItem1ItemClickListener.toTime(this,date,Utils.format12HourTime(date));
                });
                break;
            case R.id.from_time:
                Utils.setTime(mactivity, date -> {
                    fromTxt.setText(date);
                    fromTxtAmPm.setText(Utils.format12HourTime(date));
                    this.scheduleItem1ItemClickListener.fromTime(this,date,Utils.format12HourTime(date));
                });

                break;
            case R.id.add:
                this.scheduleItem1ItemClickListener.addbutton(this);
                break;
            case R.id.open_con_check:

                break;
        }
    }


    @OnCheckedChanged(R.id.open_con_check) void onChecked(boolean checked) {
        isOpen = checked ? "1" : "0";
        this.scheduleItem1ItemClickListener.isopen(ScheduleItem1.this,checked);
    }



    public interface ScheduleItem1ItemClickListener {
        void addbutton(ScheduleItem1 scheduleItem1);
        void fromTime(ScheduleItem1 scheduleItem1,String time,String timeinAmPm);
        void toTime(ScheduleItem1 scheduleItem1,String time,String timeinAmPm);
        void isopen(ScheduleItem1 scheduleItem1,boolean isChecked);
    }

    @Override
    public DayTime getData(){

        if(TextUtils.isEmpty(toTxt.getText().toString()) || TextUtils.isEmpty(toTxt.getText().toString()) ){
            throw new NullPointerException("Please check missing time slot !");
        }
        DayTime dayTime =  new DayTime();
        dayTime.start_time = toTxt.getText().toString();
        dayTime.end_time = fromTxt.getText().toString();
        dayTime.open = isOpen;

        return dayTime;
    }

}
