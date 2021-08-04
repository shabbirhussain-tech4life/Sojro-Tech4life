package com.mdcbeta.patient.PersonalCare;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

/**
 * Created by MahaRani on 15/01/2018.
 */

public class toast {


    public static void settext(Context mcontext, String text ){
        Toast toast = Toast.makeText(mcontext, text, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundColor(Color.parseColor("#014421"));
        toast.show();
    }

    public static void settextlong(Context mcontext,String text ){
        Toast toast = Toast.makeText(mcontext, text, Toast.LENGTH_LONG);
        toast.getView().setBackgroundColor(Color.parseColor("#014421"));
        toast.show();
    }


}
