package com.mdcbeta.util;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;

import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.mdcbeta.data.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Shakil Karim on 4/1/17.
 */

public class AppPref {

    private static final String SETTINGS_NAME = "com.example.shakil.finalproject.appsettings";
    private static AppPref sSharedPrefs;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;



    public enum Key {
      IS_PATIENT_LOGIN,
      IS_RADIOLOGY_LOGIN,
      IS_HEALTH_LOGIN,
      IS_LABORATORY_LOGIN,
      IS_PHARMACY_LOGIN,
      USER_LOGIN,
      STEP_DISCRIPTION,
      STEP_LIST_IMAGES,
      STEP_DOC_ID,
      STEP_SLOT_ID,
      STEP_DATE,
      STEP_AGE_Y,
      STEP_AGE_M,
      STEP_PATIENT_ID,
      STEP_PAST_HISTORY,
      STEP_LOCATION_ID,
      STEP_COMMENTS,
      KEY_CURRENT_APPOINTMENT_ID,


      STEP_FIRST_NAME, //for pass
      STEP_LAST_NAME,
      KEY_RECORD_ID,
      IS_REMEMBER,
      IS_REMEMBER_IS_PATIENT,
      IS_REMEMBER_IS_RADIOLOGY,
      IS_REMEMBER_IS_HEALTH,
      IS_REMEMBER_IS_LABORATORY,
      IS_REMEMBER_IS_PHARMACY,
      IS_REMEMBER_USERNAME,
      IS_REMEMBER_PASSWORD,

      DATA,
      CASE_CREATE,
      STEP_DOC_NAME,
      STEP_DOC_EMAIL,

      PATIENT_ID,
      FIRST_NAME,
      LAST_NAME,
      PHONE,
      PHONE2,
      GENDER,
      AGE_YEARS,
      AGE_MONTHS,
      LOCATION,

      SPO2,
      PULSE_RATE,
      TEMPERATURE,
      TEMPERATUREMANUAL,
      TEMP_UNIT,
      TEMP_UNIT2,
      TEMP_R,
      PULSE_R,
      BP_R,
      PAIN,
      PAIN_R,
      SYSTOLIC,
      DIASTOLIC,
      CUFF_VALUE,
      RESP_RATE,
      RESP_RATE_R,
      HANDS,
      WEIGHT,
      WEIGHT_UNIT,
      HEIGHT,
      HEIGHT_UNIT,
      ARM_POS,
      USER_LIST,
      GROUP_LIST,
      BP_POS,
      CHECK_VITAL;


    }

    private AppPref(Context context) {
        mPref = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }


    public static AppPref getInstance(Context context) {
        if (sSharedPrefs == null) {
            sSharedPrefs = new AppPref(context.getApplicationContext());
        }
        return sSharedPrefs;
    }


    public void put(Key key, String val) {
        doEdit();
        mEditor.putString(key.name(), val);
        doCommit();
    }

    public void put(Key key, int val) {
        doEdit();
        mEditor.putInt(key.name(), val);
        doCommit();
    }

    public void put(Key key, boolean val) {
        doEdit();
        mEditor.putBoolean(key.name(), val);
        doCommit();
    }

    public void put(Key key, float val) {
        doEdit();
        mEditor.putFloat(key.name(), val);
        doCommit();
    }

    public void put(Key key, long val) {
        doEdit();
        mEditor.putLong(key.name(), val);
        doCommit();
    }

    public void put(Key key, double val) {
        doEdit();
        mEditor.putString(key.name(), String.valueOf(val));
        doCommit();
    }


    public String getString(Key key, String defaultValue) {
        return mPref.getString(key.name(), defaultValue);
    }

    public String getString(Key key) {
        return mPref.getString(key.name(), null);
    }

    public int getInt(Key key) {
        return mPref.getInt(key.name(), 0);
    }

    public int getInt(Key key, int defaultValue) {
        return mPref.getInt(key.name(), defaultValue);
    }

    public long getLong(Key key) {
        return mPref.getLong(key.name(), 0);
    }

    public long getLong(Key key, long defaultValue) {
        return mPref.getLong(key.name(), defaultValue);
    }

    public float getFloat(Key key) {
        return mPref.getFloat(key.name(), 0);
    }

    public float getFloat(Key key, float defaultValue) {
        return mPref.getFloat(key.name(), defaultValue);
    }


    public boolean getBoolean(Key key, boolean defaultValue) {
        return mPref.getBoolean(key.name(), defaultValue);
    }

    public boolean getBoolean(Key key) {
        return mPref.getBoolean(key.name(), false);
    }

    public double getDouble(Key key) {
        return getDouble(key, 0);
    }

    public double getDouble(Key key, double defaultValue) {
        try {
            return Double.valueOf(mPref.getString(key.name(), String.valueOf(defaultValue)));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }
    public void putImageList(Key key, List<ChosenImage> images) {
        Gson gson = new Gson();
        String json = gson.toJson(images);
        doEdit();
        put(key,json);
        doCommit();
    }
    public List<ChosenImage> getImageList(Key key) {
        List images;
        Gson gson = new Gson();
        String json = mPref.getString(key.name(),null);
        if(json != null) {
            ChosenImage[] obj = gson.fromJson(json, ChosenImage[].class);
            images = Arrays.asList(obj);
            return images;
        }
        return null;
    }

    public void putUserList(Key key, List<Integer> user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        doEdit();
        put(key,json);
        doCommit();
    }
    public List<Integer> getUserList(Key key) {
        List<Integer> user;
        Gson gson = new Gson();
        String json = mPref.getString(key.name(),null);
        if(json != null) {
            Integer[] obj = gson.fromJson(json, Integer[].class);
            user = Arrays.asList(obj);
            return user;
        }
        return null;
    }


    public void putUser(Key key,User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        doEdit();
        put(key,json);
        doCommit();
    }



    public  User getUser(Key key) {
        Gson gson = new Gson();
        String json = mPref.getString(key.name(),null);
        if(json != null) {
            User obj = gson.fromJson(json, User.class);
            return obj;
        }
        return null;
    }


    public  User getUser() {
        Gson gson = new Gson();
        String json = mPref.getString(Key.USER_LOGIN.name(),null);
        if(json != null) {
            User obj = gson.fromJson(json, User.class);
            return obj;
        }
        return null;
    }

    public void putUser(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        doEdit();
        put(Key.USER_LOGIN,json);
        doCommit();
    }



    public void remove(Key... keys) {
        doEdit();
        for (Key key : keys) {
            mEditor.remove(key.name());
        }
        doCommit();
    }


    public void clearAll() {
        doEdit();
        mEditor.clear();
        doCommit();
    }



    private void doEdit() {
        if (mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    private void doCommit() {
        if (mEditor != null) {
            mEditor.commit();
            mEditor = null;
        }
    }
}
