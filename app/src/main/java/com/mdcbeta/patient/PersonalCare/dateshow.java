package com.mdcbeta.patient.PersonalCare;


import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import java.util.Calendar;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;


/**
 * Created by MahaRani on 06/01/2018.
 */

public class dateshow implements OnDateSetListener
{

    public static final String DATEPICKER_TAG = "datepicker";
    Button mbtn;

    public dateshow(Button btn, FragmentActivity context){

        mbtn = btn;

        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog =
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),true);

        datePickerDialog.setVibrate(true);
        datePickerDialog.setYearRange(1985, 2028);
        datePickerDialog.setCloseOnSingleTapDay(false);
        datePickerDialog.show(context.getSupportFragmentManager(), DATEPICKER_TAG);
    }

   /* public dateshow(Button btn,Activity activity){

        mbtn = btn;

        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog =
                DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),true);

        datePickerDialog.setVibrate(true);
        datePickerDialog.setYearRange(1985, 2028);
        datePickerDialog.setCloseOnSingleTapDay(false);

        FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        datePickerDialog.show(fragmentManager, DATEPICKER_TAG);
    }*/






    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        int mYear = year;
        int mMonth = month;
        int mDay = day;
        String Month= "";

        switch (mMonth + 1){
            case 1: Month = "Jan"; break;
            case 2: Month = "Feb"; break;
            case 3: Month = "Mar"; break;
            case 4: Month = "Apr"; break;
            case 5: Month = "May"; break;
            case 6: Month = "Jun"; break;
            case 7: Month = "Jul"; break;
            case 8: Month = "Aug"; break;
            case 9: Month = "Sep"; break;
            case 10: Month = "Oct"; break;
            case 11: Month = "Nov"; break;
            case 12: Month = "Dec"; break;
        }



        mbtn.setText(new StringBuilder()
                .append(Month).append(" ").append(mDay).append(" , ").append(mYear));
    }
}
