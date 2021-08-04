package com.mdcbeta.util;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mdcbeta.data.remote.model.Age;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;



/**
 * Created by Shakil Karim on 10/29/16.
 */

public class Utils {


    private static SimpleDateFormat dateTime12Format = new SimpleDateFormat("yyyy-MMM-dd hh:mm a");
    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat timeFormat2 = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat timeFormat12 = new SimpleDateFormat("hh:mm a"); // 12 hour
    private static SimpleDateFormat dateMonthNameFormat = new SimpleDateFormat("dd-MMM-yyyy");
    private static SimpleDateFormat dateMonthYearFormat = new SimpleDateFormat("dd-MMM-yyyy");
    private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("d MMM");

    private static final String SERVICE_ACTION = "android.support.customtabs.action.CustomTabsService";
    private static final String CHROME_PACKAGE = "com.android.chrome";


    public static boolean isNullorEmpty(List list) {
        if (list == null)
            return true;
        if (list.isEmpty())
            return true;
        return false;
    }


    public static float pxFromDp(float dp, Context mContext) {
        return dp * mContext.getResources().getDisplayMetrics().density;
    }

    public static String getDate(long time) {
        Date resultdate = new Date(time);
        return dateFormat.format(resultdate);
    }

    public static String getDate(Date time) {
        return dateFormat.format(time);
    }

    public static String getTime(long time) {
        Date resultdate = new Date(time);
        return timeFormat.format(resultdate);
    }

    public static String getDatetime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;

    }


    public static String getCurrentDateOnly() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        return sdf.format(cal.getTime());
    }

    public static String getDateTime(long time) {
        Date resultdate = new Date(time);
        return dateTimeFormat.format(resultdate);
    }

    public static String formatDateOnly(String date) {
        Date d = null;
        try {
            d = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return date;
    }


    public static String localToUTC(String time) {
        SimpleDateFormat outputFmt = new SimpleDateFormat("HH:mm");
        Date myDate = null;
        try {
            myDate = outputFmt.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar currentdate = Calendar.getInstance();
        currentdate.setTime(myDate);

        if (currentdate.getTimeZone().useDaylightTime()) {
            currentdate.setTimeInMillis(currentdate.getTimeInMillis() - currentdate.getTimeZone().getDSTSavings());
        }

        DateFormat formatter = new SimpleDateFormat("HH:mm");
        TimeZone obj = TimeZone.getTimeZone("UTC");
        formatter.setTimeZone(obj);

        return formatter.format(currentdate.getTime());

    }


    public static String localToUTCDateTime(String time) {
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date myDate = null;
        try {
            myDate = outputFmt.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar currentdate = Calendar.getInstance();
        currentdate.setTime(myDate);

        if (currentdate.getTimeZone().useDaylightTime()) {
            currentdate.setTimeInMillis(currentdate.getTimeInMillis() - currentdate.getTimeZone().getDSTSavings());
        }


        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone obj = TimeZone.getTimeZone("UTC");
        formatter.setTimeZone(obj);

        return formatter.format(currentdate.getTime());

    }

    public static String getDatetimeinformat() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.US);
        String formattedDate = df.format(c.getTime());
        return formattedDate;

    }

    public static String UTCTolocalDateTime(String time) {
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date myDate = null;
        try {
            myDate = outputFmt.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar currentdate = Calendar.getInstance();
        currentdate.setTime(myDate);
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");


        if (currentdate.getTimeZone().useDaylightTime()) {
            currentdate.setTimeInMillis(currentdate.getTimeInMillis() + currentdate.getTimeZone().getDSTSavings());
            String date = formatter.format(currentdate.getTime());
            return date;
        } else {
            String date = formatter.format(currentdate.getTime());
            return date;
        }


    }


    public static String UTCTolocalDate(String time) {
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date myDate = null;
        try {
            myDate = outputFmt.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar currentdate = Calendar.getInstance();
        currentdate.setTime(myDate);
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");


        if (currentdate.getTimeZone().useDaylightTime()) {
            currentdate.setTimeInMillis(currentdate.getTimeInMillis() + currentdate.getTimeZone().getDSTSavings());
            String date = formatter.format(currentdate.getTime());
            return date;
        } else {
            String date = formatter.format(currentdate.getTime());
            return date;
        }


    }


    public static String UTCTolocal(String time) {

        SimpleDateFormat outputFmt = new SimpleDateFormat("HH:mm");
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date myDate = null;
        try {
            myDate = outputFmt.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar currentdate = Calendar.getInstance();
        currentdate.setTime(myDate);
        DateFormat formatter = new SimpleDateFormat("HH:mm");

        if (currentdate.getTimeZone().useDaylightTime()) {
            currentdate.setTimeInMillis(currentdate.getTimeInMillis() + currentdate.getTimeZone().getDSTSavings());
            String date = formatter.format(currentdate.getTime());
            return date;
        } else {
            String date = formatter.format(currentdate.getTime());
            return date;
        }

    }


    public static String UTCTolocalTime(String time) {

        SimpleDateFormat outputFmt = new SimpleDateFormat("HH:mm");
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date myDate = null;
        try {
            myDate = outputFmt.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar currentdate = Calendar.getInstance();
        currentdate.setTime(myDate);
        DateFormat formatter = new SimpleDateFormat("HH:mm");

        if (currentdate.getTimeZone().useDaylightTime()) {
            currentdate.setTimeInMillis(currentdate.getTimeInMillis() + currentdate.getTimeZone().getDSTSavings());
            String date = formatter.format(currentdate.getTime());
            return date;
        } else {
            String date = formatter.format(currentdate.getTime());
            return date;
        }

    }


    public static String getDateTime(Date time) {
        return dateTimeFormat.format(time);
    }

    public static String getFormateddate(Date date) {
        return dateFormat2.format(date);
    }


    public static Date stringToDate(String dateinString) {
        Date d = null;
// if condition is added by kanwal khan
        if (!TextUtils.isEmpty(dateinString) && dateinString!=null) {
           // Toast.makeText(get dateinString, Toast.LENGTH_SHORT).show();
        try {
            d = dateFormat.parse(dateinString);
        } catch (ParseException ex) {
            return null;
        } }
        return d;
    }

    public static int currentMounth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }


    public static String format12HourDateTime(String Time) {
        SimpleDateFormat actualFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        SimpleDateFormat toFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");

        Date d = null;
        try {
            d = actualFormat.parse(Time);
        } catch (ParseException e) {
            e.printStackTrace();
            return Time;
        }

        String x = toFormat.format(d);

        return x;
    }

    public static String format12HourDate(String Time) {
        SimpleDateFormat actualFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        SimpleDateFormat toFormat = new SimpleDateFormat("dd-MMM-yyyy");

        Date d = null;
        try {
            d = actualFormat.parse(Time);
        } catch (ParseException e) {
            e.printStackTrace();
            return Time;
        }

        String x = toFormat.format(d);

        return x;
    }


    public static final String md5(final String toEncrypt) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02X", bytes[i]));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return ""; // Impossibru!
        }
    }


    public static String formatDate(String date) {
        Date d = null;
        try {
            d = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }

        String x = dateMonthNameFormat.format(d);

        return x;
    }


    public static String formatedDate(String date) {
        Date d = null;
        try {
            d = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }

        String x = dateMonthYearFormat.format(d);

        return x;
    }


    public static String formatDateTime(String date) {
        SimpleDateFormat dateTimeFrom = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        SimpleDateFormat dateTimeTo = new SimpleDateFormat("dd-MM-yyyy hh:mm a");


        Date d = null;
        try {
            d = dateTimeFrom.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }

        String x = dateTimeTo.format(d);

        return x;
    }


    public static String format12HourTime(String Time) {
        Date d = null;
        try {
            d = timeFormat.parse(Time);
        } catch (ParseException e) {
            e.printStackTrace();
            return Time;
        }

        String x = timeFormat12.format(d);

        return x;
    }


    public static String format122HourTime(String Time) {
        Date d = null;
        try {
            d = timeFormat2.parse(Time);
        } catch (ParseException e) {
            e.printStackTrace();
            return Time;
        }

        String x = timeFormat12.format(d);

        return x;
    }

    public static void setDate(Activity context, MyOnDateSetListener callBack) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {

                    String fyear = String.valueOf(year);
                    String fmonth = ((monthOfYear + 1)) < 10 ? "0" + ((monthOfYear + 1)) : "" + ((monthOfYear + 1));
                    String fday = ((dayOfMonth)) < 10 ? "0" + ((dayOfMonth)) : "" + ((dayOfMonth));

                    callBack.onDateSet(fyear + "-" + fmonth + "-" + fday);

                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
//        dpd.show(context.getFragmentManager(), "Datepickerdialog");

    }

    public static void setTime(Activity context, MyOnTimeSetListener callBack) {

        TimePickerDialog tpd = new TimePickerDialog(context, (view, hourOfDay, minute) -> {

            String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
            String minuteString = minute < 10 ? "0" + minute : "" + minute;
            //String secondString = second < 10 ? "0"+second : ""+second;
            callBack.onTimeSet("" + hourString + ":" + minuteString);
        },
                24,
                0, true);


        tpd.show();


//        Calendar now = Calendar.getInstance();
//        TimePickerDialog tpd = TimePickerDialog.newInstance(
//                (view, hourOfDay, minute, second) -> {
//
//                    String hourString = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
//                    String minuteString = minute < 10 ? "0"+minute : ""+minute;
//                    String secondString = second < 10 ? "0"+second : ""+second;
//                    callBack.onTimeSet(""+hourString+":"+minuteString+":"+secondString);
//
//                },
//                24,
//                0,
//                false
//        );
//
//        tpd.enableSeconds(false);
//        tpd.enableMinutes(true);
//        tpd.setVersion(TimePickerDialog.Version.VERSION_2);
//        tpd.show(context.getFragmentManager(), "Timepickerdialog");


    }


    public static String getRelativeTime(String datetime) {
        long milliseconds = 0L;
        try {
            Date d = dateTimeFormat.parse(datetime);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long now = System.currentTimeMillis();
        return DateUtils.getRelativeTimeSpanString(milliseconds, now, DateUtils.DAY_IN_MILLIS).toString();

    }

    public static boolean setIMatchPassword(EditText pass, EditText repass) {
        boolean isAllValid = true;
        if (!pass.getText().toString().equals(repass.getText().toString())) {
            repass.setError("Password not match");
            isAllValid = false;
        }
        return isAllValid;

    }

    public static boolean isChromeCustomTabsSupported(@NonNull final Context context) {
        Intent serviceIntent = new Intent(SERVICE_ACTION);
        serviceIntent.setPackage(CHROME_PACKAGE);
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentServices(serviceIntent, 0);
        return !(resolveInfos == null || resolveInfos.isEmpty());
    }


    public interface MyOnDateSetListener {
        void onDateSet(String date);
    }

    public interface MyOnTimeSetListener {

        void onTimeSet(String date);

    }

    public static boolean setRequiredField(EditText... editTexts) {
        boolean isAllValid = true;
        for (EditText editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText().toString())) {
                editText.setError("Field is Required");
                isAllValid = false;
            }
        }
        return isAllValid;
    }

    public static boolean setValidEmail(EditText editTexts) {
        boolean isAllValid = true;
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        if (!emailPattern.matcher(editTexts.getText()).matches()) {
            editTexts.setError("Invalid Email");
            isAllValid = false;
        }
        return isAllValid;
    }

    public static boolean setValidLength(EditText editTexts, int minLength) {
        return editTexts.getText().toString().length() >= minLength;
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


    public static void setBirthDate(Fragment context, MyOnDateSetListener callBack) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                (view, year, monthOfYear, dayOfMonth) -> {

                    String fyear = String.valueOf(year);
                    String fmonth = ((monthOfYear + 1)) < 10 ? "0" + ((monthOfYear + 1)) : "" + ((monthOfYear + 1));
                    String fday = ((dayOfMonth)) < 10 ? "0" + ((dayOfMonth)) : "" + ((dayOfMonth));

                    callBack.onDateSet(fyear + "-" + fmonth + "-" + fday);

                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.show(context.getFragmentManager(), "Datepickerdialog");

    }

    public static Age calculateAge(Date birthDate) {
        int years = 0;
        int months = 0;
        int days = 0;

        //create calendar object for birth day
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());

        //create calendar object for current day
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);

        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;

        //Get difference between months
        months = currMonth - birthMonth;

        //if month difference is in negative then reduce years by one
        //and calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        }

        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }
        //Create new Age object
        return new Age(days, months, years);
    }

    public static boolean isBluetoothEnabled()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter.isEnabled();

    }

}
