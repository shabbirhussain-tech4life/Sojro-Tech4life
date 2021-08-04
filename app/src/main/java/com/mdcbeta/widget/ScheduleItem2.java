package com.mdcbeta.widget;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
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

public class ScheduleItem2 extends FrameLayout implements DayTimeProvider{


    ScheduleItem2ItemClickListener ScheduleItem2ItemClickListener;
    Activity mactivity;
    @BindView(R.id.to_txt)
    public TextView toTxt;
    @BindView(R.id.from_txt)
    public TextView fromTxt;
    @BindView(R.id.to_txt_am_pm)
    public TextView toTxtAmPm;
    @BindView(R.id.from_txt_am_pm)
    public TextView fromTxtAmPm;

    @BindView(R.id.open_con_check)
    public CheckBox openConCheck;

    private String isOpen = "0";

    public int id;

    public ScheduleItem2(Context context, Activity activity) {
        super(context);
        mactivity = activity;
        inits();
    }

    public ScheduleItem2(Context context, AttributeSet attrs) {
        super(context, attrs);
        inits();
    }

    private void inits() {
        inflate(getContext(), R.layout.include_fragment_schedule_item, this);
        ButterKnife.bind(this);
        toTxt.setText("");
        fromTxt.setText("");
        toTxtAmPm.setText("");
        fromTxtAmPm.setText("");
    }


    public void setOnItemsClickListner(final ScheduleItem2ItemClickListener onActionClick) {
        this.ScheduleItem2ItemClickListener = onActionClick;
    }

    @OnCheckedChanged(R.id.open_con_check) void onChecked(boolean checked) {
        isOpen = checked ? "1" : "0";
        this.ScheduleItem2ItemClickListener.isopen(ScheduleItem2.this,checked);
    }

    @OnClick({R.id.to_time, R.id.from_time, R.id.add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.to_time:
                Utils.setTime(mactivity, date -> {
                    toTxt.setText(date);
                    toTxtAmPm.setText(Utils.format12HourTime(date));
                    this.ScheduleItem2ItemClickListener.toTime(ScheduleItem2.this,date,Utils.format12HourTime(date));
                });

                break;
            case R.id.from_time:
                Utils.setTime(mactivity, date ->{
                    fromTxt.setText(date);
                    fromTxtAmPm.setText(Utils.format12HourTime(date));
                    this.ScheduleItem2ItemClickListener.fromTime(ScheduleItem2.this,date,Utils.format12HourTime(date));
                });

                break;
            case R.id.add:
                this.ScheduleItem2ItemClickListener.deletebtn(ScheduleItem2.this);
                break;
        }
    }

    public interface ScheduleItem2ItemClickListener {
        void deletebtn(ScheduleItem2 scheduleItem2);
        void fromTime(ScheduleItem2 scheduleItem2,String time,String timeinAmPm);
        void toTime(ScheduleItem2 scheduleItem2,String time,String timeinAmPm);
        void isopen(ScheduleItem2 scheduleItem2,boolean isChecked);
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
