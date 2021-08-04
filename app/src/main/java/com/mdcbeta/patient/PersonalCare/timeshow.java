package com.mdcbeta.patient.PersonalCare;


import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by MahaRani on 06/01/2018.
 */

public class timeshow implements TimePickerDialog.OnTimeSetListener{

    public static final String TIMEPICKER_TAG = "timepicker";
    Button mbtn;
    int mhour = -1;
    int mmin = -1;


    public timeshow(Button btn, FragmentActivity context){

        mbtn = btn;

        final Calendar calendar = Calendar.getInstance();

        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this,
                calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), false, false);

        timePickerDialog.setVibrate(true);
        timePickerDialog.setCloseOnSingleTapMinute(false);
        timePickerDialog.show(context.getSupportFragmentManager(), TIMEPICKER_TAG);
    }






    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {


        String hour = null;
        String min = null;
        hour = ""+(hourOfDay < 10 ? ( hourOfDay > 12 ? hourOfDay - 12 : hourOfDay ) : (hourOfDay > 12 ? hourOfDay - 12 : hourOfDay ) );
        min = ""+(minute < 10 ? "0"+minute : minute );

        if(Integer.valueOf(hour.trim()) < 10){
            hour = "0"+hour;
        }

        if(view.getIsCurrentlyAmOrPm() == 0)
            mbtn.setText(hour+ ":" + min+" AM");
        else if(view.getIsCurrentlyAmOrPm() == 1)
            mbtn.setText(hour+ ":" + min+" PM");


        //mbtn.setText(time12format);

        mhour = hourOfDay;
        mmin = minute;
    }

    public int getHour(){
        return mhour;
    }
    public int getMin(){
        return mmin;
    }

}
