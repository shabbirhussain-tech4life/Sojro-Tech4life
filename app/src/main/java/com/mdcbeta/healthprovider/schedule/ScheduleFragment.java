package com.mdcbeta.healthprovider.schedule;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.model.User;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Day;
import com.mdcbeta.data.remote.model.DayTime;
import com.mdcbeta.data.remote.model.PreviousSchedule;
import com.mdcbeta.data.remote.model.Schedule;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.mdcbeta.widget.ActionBar;
import com.mdcbeta.widget.DayTimeProvider;
import com.mdcbeta.widget.ScheduleItem1;
import com.mdcbeta.widget.ScheduleItem2;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * Created by Shakil Karim on 4/4/17.
 */

public class ScheduleFragment extends BaseFragment {


    private static final String TAG = "ScheduleFragment";
    @BindView(R.id.main)
    ScrollView scrollView;
    @BindView(R.id.start_date)
    Button startDate;
    @BindView(R.id.end_date)
    Button endDate;
    @BindView(R.id.all_days_tab)
    LinearLayout allDaysTab;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.edit)
    TextView edit;
    @BindView(R.id.txt_price)
    EditText txtPrice;
    @BindView(R.id.monday_tab)
    LinearLayout mondayTab;
    @BindView(R.id.tuesday_tab)
    LinearLayout tuesdayTab;
    @BindView(R.id.wednesday_tab)
    LinearLayout wednesdayTab;
    @BindView(R.id.thrusday_tab)
    LinearLayout thrusdayTab;
    @BindView(R.id.friday_tab)
    LinearLayout fridayTab;
    @BindView(R.id.sat_tab)
    LinearLayout satTab;
    @BindView(R.id.sun_tab)
    LinearLayout sunTab;
    @BindView(R.id.previous_data)
    TextView previous_data;
    @BindView(R.id.edit_schedule)
    TextView edit_schedule;
    @Inject
    ApiFactory apiFactory;
    @BindView(R.id.spin_duration)
    Spinner spinDuration;
    @BindView(R.id.spinner6)
    Spinner spinCurrency;
    @BindView(R.id.pre_date_card)
    CardView preDateCard;

    int durationPosition;
    String currency;
    String id;

    public static final long HALF_HOUR = 1800000;
    private final static long HOUR  = 3600000;
    public static final long FIFTEEN_MIN = 900000;

    private int slotId = 0;



    public static Fragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
        return fragment;
    }


    @Override
    public void onStart() {
        super.onStart();

        User user = AppPref.getInstance(getActivity()).getUser();
        showProgress("Loading schedule");

        apiFactory.previousSchedules(user.getId())
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model -> {

                    if(model.status && model.data == null){
                        hideProgress();
                        previous_data.setVisibility(View.GONE);
                        preDateCard.setVisibility(View.GONE);
                    }
                    else if(model.status) {
                        hideProgress();
                        previous_data.setVisibility(View.VISIBLE);
                        preDateCard.setVisibility(View.VISIBLE);

                        PreviousSchedule previousSchedule = model.getData().get(0);
                        if (previousSchedule != null) {

                            String start = Utils.formatedDate(previousSchedule.getStartDate());
                            String end = Utils.formatedDate(previousSchedule.getEndDate());

                            SpannableString s1 = new SpannableString(start);
                            s1.setSpan(new StyleSpan(Typeface.BOLD), 0, s1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            SpannableString s2 = new SpannableString(end);
                            s2.setSpan(new StyleSpan(Typeface.BOLD), 0, s2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                            SpannableStringBuilder builder = new SpannableStringBuilder();
                            builder.append("You have already defined your schedule from ");
                            builder.append(s1);
                            builder.append(" to ");
                            builder.append(s2);
                            builder.append(" .selecting a new schedule will replace the previous one. ");


                            previous_data.setText(builder);
                            previous_data.setVisibility(View.VISIBLE);
                            edit_schedule.setVisibility(View.VISIBLE);
                        }
                    }

                },
                  throwable ->
                    // changed by kk 6/27/2020
                  //  showError("Still no schedule is defined")
                    Toast.makeText(getContext(),"Define your schedule",Toast.LENGTH_SHORT).show()
                );
//                    showError(throwable.getMessage()));

        edit_schedule.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                submit.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.VISIBLE);

                showProgress("Loading");
                apiFactory.getscheduledata(getUser().getId())
                        .compose(RxUtil.applySchedulers())
                        .subscribe(resource -> {

                            hideProgress();

                            if(resource.status) {
                                JsonObject data = resource.data.get("details").getAsJsonObject();

                                try {
                                    startDate.setText(Utils.formatedDate(data.get("start_date").getAsString()));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                try {
                                    endDate.setText(Utils.formatedDate(data.get("end_date").getAsString()));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                try {
                                    txtPrice.setText(data.get("slot_price").getAsString());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                try {
                                    id = data.get("id").getAsString();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }


                            }else {
                                showError(resource.getMessage());
                            }

                        },throwable -> {
                            showError(throwable.getMessage());

                        });

                return false;
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        allDaysTab.addView(provideItem1(123456,allDaysTab.getId()));   // main tag
        mondayTab.addView(provideItem1(allDaysTab.getId(),mondayTab.getId()));
        tuesdayTab.addView(provideItem1(allDaysTab.getId(),tuesdayTab.getId()));
        wednesdayTab.addView(provideItem1(allDaysTab.getId(),wednesdayTab.getId()));
        thrusdayTab.addView(provideItem1(allDaysTab.getId(),thrusdayTab.getId()));
        fridayTab.addView(provideItem1(allDaysTab.getId(),fridayTab.getId()));
        satTab.addView(provideItem1(allDaysTab.getId(),satTab.getId()));
        sunTab.addView(provideItem1(allDaysTab.getId(),sunTab.getId()));

    }


    public View provideItem1(int tag,int viewId) {
        ScheduleItem1 scheduleItem = new ScheduleItem1(getContext(), getActivity());
        scheduleItem.setTag(tag);
        scheduleItem.setOnItemsClickListner(new ScheduleItem1.ScheduleItem1ItemClickListener() {
            @Override
            public void addbutton(ScheduleItem1 scheduleItem1) {


                if ((Integer)scheduleItem1.getTag() == 123456) {

                    ++slotId;

                    allDaysTab.addView(provideItem2((123456+slotId),allDaysTab.getId()));
                    mondayTab.addView(provideItem2((allDaysTab.getId()+slotId),mondayTab.getId()));
                    tuesdayTab.addView(provideItem2((allDaysTab.getId()+slotId),tuesdayTab.getId()));
                    wednesdayTab.addView(provideItem2((allDaysTab.getId()+slotId),wednesdayTab.getId()));
                    thrusdayTab.addView(provideItem2(allDaysTab.getId()+slotId,thrusdayTab.getId()));
                    fridayTab.addView(provideItem2(allDaysTab.getId()+slotId,fridayTab.getId()));
                    satTab.addView(provideItem2(allDaysTab.getId()+slotId,satTab.getId()));
                    sunTab.addView(provideItem2(allDaysTab.getId()+slotId,sunTab.getId()));
                } else {
                    ((ViewGroup)getView().findViewById(viewId)).addView(provideItem2(-1,-1));
                }

            }

            @Override
            public void fromTime(ScheduleItem1 scheduleItem1,String time,String timeinAmPm) {

                Log.d(TAG, "fromTime: "+scheduleItem1.id+" time "+time);
                if ((Integer)scheduleItem1.getTag() == 123456) {

                    for(View v : getViewsByTag((ViewGroup) getView(),allDaysTab.getId())){
                        if(v instanceof ScheduleItem1){
                            ((ScheduleItem1) v).fromTxt.setText(time);
                            ((ScheduleItem1) v).fromTxtAmPm.setText(timeinAmPm);
                        }
                    }

                }

            }

            @Override
            public void toTime(ScheduleItem1 scheduleItem1,String time,String timeinAmPm) {

                if ((Integer)scheduleItem1.getTag() == 123456) {

                    for(View v : getViewsByTag((ViewGroup) getView(),allDaysTab.getId())){
                        if(v instanceof ScheduleItem1){
                            ((ScheduleItem1) v).toTxt.setText(time);
                            ((ScheduleItem1) v).toTxtAmPm.setText(timeinAmPm);
                        }
                    }

                }

            }

            @Override
            public void isopen(ScheduleItem1 scheduleItem1,boolean isChecked) {

                if ((Integer)scheduleItem1.getTag() == 123456) {

                    for(View v : getViewsByTag((ViewGroup) getView(),allDaysTab.getId())){
                        if(v instanceof ScheduleItem1){
                            ((ScheduleItem1) v).openConCheck.setChecked(isChecked);
                        }
                    }

                }
            }
        });
        return scheduleItem;

    }





    public View provideItem2(int tag,int viewID) {
        ScheduleItem2 scheduleItem = new ScheduleItem2(getContext(), getActivity());
        scheduleItem.setTag(tag);
        scheduleItem.setOnItemsClickListner(new ScheduleItem2.ScheduleItem2ItemClickListener() {
            @Override
            public void deletebtn(ScheduleItem2 scheduleItem2) {


                Log.d(TAG, "deletebtn: "+(Integer)scheduleItem2.getTag());


                for(int i = 0 ; i <= slotId ; i++){

                    if ((Integer)scheduleItem2.getTag() == (123456+i)) {

                        for(View v : getViewsByTag((ViewGroup) getView(),(allDaysTab.getId()+i))){

                            ((ViewGroup) v.getParent()).removeView(v);
                        }

                        ((ViewGroup) scheduleItem2.getParent()).removeView(scheduleItem2);

                       return;

                    }

                }


                ((ViewGroup) scheduleItem2.getParent()).removeView(scheduleItem2);




            }

            @Override
            public void fromTime(ScheduleItem2 scheduleItem2, String time,String timeinAmPm) {

                Log.d(TAG, "fromTime: "+scheduleItem2.id+" time "+time);


                for(int i = 0 ; i <= slotId ; i++){

                    if ((Integer)scheduleItem2.getTag() == (123456+i)) {

                        for(View v : getViewsByTag((ViewGroup) getView(),(allDaysTab.getId()+i))){

                            if(v instanceof ScheduleItem2){
                                ((ScheduleItem2) v).fromTxt.setText(time);
                                ((ScheduleItem2) v).fromTxtAmPm.setText(timeinAmPm);
                            }
                        }

                        return;

                    }

                }

            }

            @Override
            public void toTime(ScheduleItem2 scheduleItem2, String time,String timeinAmPm) {

                Log.d(TAG, "toTime: "+scheduleItem2.id+" time "+time);

                for(int i = 0 ; i <= slotId ; i++){

                    if ((Integer)scheduleItem2.getTag() == (123456+i)) {

                        for(View v : getViewsByTag((ViewGroup) getView(),(allDaysTab.getId()+i))){

                            if(v instanceof ScheduleItem2){
                                ((ScheduleItem2) v).toTxt.setText(time);
                                ((ScheduleItem2) v).toTxtAmPm.setText(timeinAmPm);
                            }
                        }

                        return;

                    }

                }
            }

            @Override
            public void isopen(ScheduleItem2 scheduleItem2, boolean isChecked) {

                for(int i = 0 ; i <= slotId ; i++){

                    if ((Integer)scheduleItem2.getTag() == (123456+i)) {

                        for(View v : getViewsByTag((ViewGroup) getView(),(allDaysTab.getId()+i))){

                            if(v instanceof ScheduleItem2){
                                ((ScheduleItem2) v).openConCheck.setChecked(isChecked);
                            }
                        }

                        return;

                    }

                }
            }
        });
        return scheduleItem;
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_schedule;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }


    @OnClick({R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thrusday, R.id.friday, R.id.sat, R.id.sun})
    public void onViewClicked(View view) {
        if (rootView != null) {

            String StringID = view.getResources().getResourceName(view.getId());
            int id = this.getResources().getIdentifier(StringID + "_tab", "id", getActivity().getPackageName());
            View hideView = rootView.findViewById(id);
            hideView.setVisibility(hideView.getVisibility() == View.GONE ? View.VISIBLE : View.VISIBLE);
        }
    }


    @OnCheckedChanged({R.id.mon_not_avail, R.id.tue_not_avail,
            R.id.wed_not_avail, R.id.thr_not_avail, R.id.fri_not_avail, R.id.sat_not_avail, R.id.sun_not_avail})
    public void onViewClicked(android.widget.CompoundButton view,boolean isChecked) {
        if (rootView != null) {

            String StringID = view.getResources().getResourceName(view.getId());


            if (StringID.contains("mon_not_avail")) {
                StringID = "monday";
            } else if (StringID.contains("tue_not_avail")) {
                StringID = "tuesday";
            } else if (StringID.contains("wed_not_avail")) {
                StringID = "wednesday";
            } else if (StringID.contains("thr_not_avail")) {
                StringID = "thrusday";
            } else if (StringID.contains("fri_not_avail")) {
                StringID = "friday";
            } else if (StringID.contains("sat_not_avail")) {
                StringID = "sat";
            } else if (StringID.contains("sun_not_avail")) {
                StringID = "sun";
            }


            int id = this.getResources().getIdentifier(StringID + "_tab", "id", getActivity().getPackageName());
            View hideView = rootView.findViewById(id);
            hideView.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        }
    }




    @OnItemSelected(R.id.spin_duration) void onItemSelected(int position) {

       // case R.id.spinDimension:
        //Toast.mak0eText(getActivity(), "Selected position " + position + "!", Toast.LENGTH_SHORT).show();
        durationPosition = position;

    }


    @OnItemSelected(R.id.spinner6) void onItemSelected1(int position) {

        // case R.id.spinDimension:
     //   Toast.makeText(getActivity(), "Selected position " + spinCurrency.getSelectedItem().toString() + "!", Toast.LENGTH_SHORT).show();
        currency = spinCurrency.getSelectedItem().toString();

    }



@OnClick({R.id.start_date, R.id.end_date})
    public void dateClick(View view) {
        switch (view.getId()) {
            case R.id.start_date:
                Utils.setDate(getActivity(), date -> {
                    startDate.setText(date);
                });
                break;
            case R.id.end_date:
                Utils.setDate(getActivity(), date -> {
                    endDate.setText(date);
                });
                break;
        }
    }


    @OnClick(R.id.submit)
    public void submitClick() {


        try {

            if(durationPosition == 0){
                showError("Select duration");
                return;
            }

            if (TextUtils.isEmpty(startDate.getText().toString()) || TextUtils.isEmpty(endDate.getText().toString())
                    || TextUtils.isEmpty(txtPrice.getText().toString())) {
                //throw new NullPointerException("Please check date and price filed !");
                showError("Please check date and price filed !");
                return;
            }



        Schedule schedule = new Schedule();
            schedule.start_date = startDate.getText().toString();
            schedule.end_date = endDate.getText().toString();
            schedule.slot_price = txtPrice.getText().toString();
            schedule.doctor_id = getBaseActivity().getUser().id;
            schedule.currency = currency;

            Day mon = new Day();
            mon.data = new ArrayList<>();
            Day tue = new Day();
            tue.data = new ArrayList<>();
            Day wed = new Day();
            wed.data = new ArrayList<>();
            Day thr = new Day();
            thr.data = new ArrayList<>();
            Day fri = new Day();
            fri.data = new ArrayList<>();
            Day sat = new Day();
            sat.data = new ArrayList<>();
            Day sun = new Day();
            sun.data = new ArrayList<>();

            schedule.mon = mon;
            schedule.tue = tue;
            schedule.wed = wed;
            schedule.thr = thr;
            schedule.fri = fri;
            schedule.sat = sat;
            schedule.sun = sun;

            if (!((CheckBox) mondayTab.findViewById(R.id.mon_not_avail)).isChecked()) {

                for (int i = 1; i < mondayTab.getChildCount(); i++) {
                    DayTimeProvider daytime = (DayTimeProvider) mondayTab.getChildAt(i);

                    DayTime dayTime = daytime.getData();

                    if (dayTime.start_time.equals(dayTime.end_time)) {
                        showErrorWithTitle("Monday", "There should be atleast 1 hour difference between times slots or choose not available");
                        return;
                    } else if (dayTime.start_time.compareTo(dayTime.end_time) > 0) {
                        showErrorWithTitle("Monday", "start time should not be greater than end time");
                        return;
                    }

                    if(durationPosition == 3) {

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HOUR);
                        mon.data.addAll(dayTimes);


                    }else if(durationPosition == 2) {
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HALF_HOUR);

                        if(dayTimes != null) {
                            mon.data.addAll(dayTimes);
                        }else {
                            showErrorWithTitle("Monday", "There should be atleast 1 hour difference between times slots or choose not available");
                            return;
                        }
                    }else if(durationPosition == 1){

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),FIFTEEN_MIN);
                        mon.data.addAll(dayTimes);
                    }

                }
            }
            if (!((CheckBox) tuesdayTab.findViewById(R.id.tue_not_avail)).isChecked()) {
                for (int i = 1; i < tuesdayTab.getChildCount(); i++) {
                    DayTimeProvider daytime = (DayTimeProvider) tuesdayTab.getChildAt(i);
                    DayTime dayTime = daytime.getData();

                    if (dayTime.start_time.equals(dayTime.end_time)) {
                        showErrorWithTitle("Tuesday", "There should be atleast 1 hour difference between times slots or choose not available");
                        return;
                    } else if (dayTime.start_time.compareTo(dayTime.end_time) > 0) {
                        showErrorWithTitle("Tuesday", "start time should not be greater than end time");
                        return;
                    }

                    if(durationPosition == 3) {

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HOUR);
                        tue.data.addAll(dayTimes);


                    }else if(durationPosition == 2){
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HALF_HOUR);

                        if(dayTimes != null) {
                            tue.data.addAll(dayTimes);
                        }else {
                            showErrorWithTitle("Tuesday", "There should be atleast 1 hour difference between times slots or choose not available");
                            return;
                        }
                    }else if(durationPosition == 1){

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),FIFTEEN_MIN);
                        tue.data.addAll(dayTimes);
                    }


                }
            }
            if (!((CheckBox) wednesdayTab.findViewById(R.id.wed_not_avail)).isChecked()) {
                for (int i = 1; i < wednesdayTab.getChildCount(); i++) {
                    DayTimeProvider daytime = (DayTimeProvider) wednesdayTab.getChildAt(i);

                    DayTime dayTime = daytime.getData();

                    if (dayTime.start_time.equals(dayTime.end_time)) {
                        showErrorWithTitle("Wednesday", "There should be atleast 1 hour difference between times slots or choose not available");
                        return;
                    } else if (dayTime.start_time.compareTo(dayTime.end_time) > 0) {
                        showErrorWithTitle("Wednesday", "start time should not be greater than end time");
                        return;
                    }


                    if(durationPosition == 3) {

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HOUR);
                        wed.data.addAll(dayTimes);

                    }else if(durationPosition == 2){
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HALF_HOUR);

                        if(dayTimes != null) {
                            wed.data.addAll(dayTimes);
                        }else {
                            showErrorWithTitle("Wednesday", "There should be atleast 1 hour difference between times slots or choose not available");
                            return;
                        }
                    }else if(durationPosition == 1){

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),FIFTEEN_MIN);
                        wed.data.addAll(dayTimes);
                    }

                }
            }
            if (!((CheckBox) thrusdayTab.findViewById(R.id.thr_not_avail)).isChecked()) {
                for (int i = 1; i < thrusdayTab.getChildCount(); i++) {
                    DayTimeProvider daytime = (DayTimeProvider) thrusdayTab.getChildAt(i);

                    DayTime dayTime = daytime.getData();

                    if (dayTime.start_time.equals(dayTime.end_time)) {
                        showErrorWithTitle("Thursday", "There should be atleast 1 hour difference between times slots or choose not available");
                        return;
                    } else if (dayTime.start_time.compareTo(dayTime.end_time) > 0) {
                        showErrorWithTitle("Thursday", "start time should not be greater than end time");
                        return;
                    }


                    if(durationPosition == 3) {

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HOUR);
                        thr.data.addAll(dayTimes);

                    }else if(durationPosition == 2){
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HALF_HOUR);

                        if(dayTimes != null) {
                            thr.data.addAll(dayTimes);
                        }else {
                            showErrorWithTitle("Thursday", "There should be atleast 1 hour difference between times slots or choose not available");
                            return;
                        }
                    }else if(durationPosition == 1){

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),FIFTEEN_MIN);
                        thr.data.addAll(dayTimes);
                    }
                }
            }
            if (!((CheckBox) fridayTab.findViewById(R.id.fri_not_avail)).isChecked()) {
                for (int i = 1; i < fridayTab.getChildCount(); i++) {
                    DayTimeProvider daytime = (DayTimeProvider) fridayTab.getChildAt(i);

                    DayTime dayTime = daytime.getData();

                    if (dayTime.start_time.equals(dayTime.end_time)) {
                        showErrorWithTitle("Friday", "There should be atleast 1 hour difference between times slots or choose not available");
                        return;
                    } else if (dayTime.start_time.compareTo(dayTime.end_time) > 0) {
                        showErrorWithTitle("Friday", "start time should not be greater than end time");
                        return;
                    }


                    if(durationPosition == 3) {

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HOUR);
                        fri.data.addAll(dayTimes);

                    }else if(durationPosition == 2){
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HALF_HOUR);

                        if(dayTimes != null) {
                            fri.data.addAll(dayTimes);
                        }else {
                            showErrorWithTitle("Friday", "There should be atleast 1 hour difference between times slots or choose not available");
                            return;
                        }
                    }else if(durationPosition == 1){

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),FIFTEEN_MIN);
                        fri.data.addAll(dayTimes);
                    }
                }
            }

            if (!((CheckBox) satTab.findViewById(R.id.sat_not_avail)).isChecked()) {
                for (int i = 1; i < satTab.getChildCount(); i++) {
                    DayTimeProvider daytime = (DayTimeProvider) satTab.getChildAt(i);

                    DayTime dayTime = daytime.getData();

                    if (dayTime.start_time.equals(dayTime.end_time)) {
                        showErrorWithTitle("Saturday", "There should be atleast 1 hour difference between times slots or choose not available");
                        return;
                    } else if (dayTime.start_time.compareTo(dayTime.end_time) > 0) {
                        showErrorWithTitle("Saturday", "start time should not be greater than end time");
                        return;
                    }

                    if(durationPosition == 3) {
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HOUR);
                        sat.data.addAll(dayTimes);
                    }else if(durationPosition == 2){
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HALF_HOUR);

                        if(dayTimes != null) {
                            sat.data.addAll(dayTimes);
                        }else {
                            showErrorWithTitle("Saturday", "There should be atleast 1 hour difference between times slots or choose not available");
                            return;
                        }
                    }else if(durationPosition == 1){

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),FIFTEEN_MIN);
                        sat.data.addAll(dayTimes);
                    }
                }
            }

            if (!((CheckBox) sunTab.findViewById(R.id.sun_not_avail)).isChecked()) {
                for (int i = 1; i < sunTab.getChildCount(); i++) {
                    DayTimeProvider daytime = (DayTimeProvider) sunTab.getChildAt(i);

                    DayTime dayTime = daytime.getData();

                    if (dayTime.start_time.equals(dayTime.end_time)) {
                        showErrorWithTitle("Sunday", "There should be atleast 1 hour difference between times slots or choose not available");
                        return;
                    } else if (dayTime.start_time.compareTo(dayTime.end_time) > 0) {
                        showErrorWithTitle("Sunday", "start time should not be greater than end time");
                        return;
                    }


                    if(durationPosition == 3) {

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HOUR);
                        sun.data.addAll(dayTimes);

                    }else if(durationPosition == 2) {
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HALF_HOUR);

                        if(dayTimes != null) {
                            sun.data.addAll(dayTimes);
                        }else {
                            showErrorWithTitle("Sunday", "There should be atleast 1 hour difference between times slots or choose not available");
                            return;
                        }
                    }else if(durationPosition == 1){

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),FIFTEEN_MIN);
                        sun.data.addAll(dayTimes);
                    }
                }
            }



            showProgress("Creating Schedule");
            apiFactory.createSchedule(formatTimeZone(schedule))
                    .compose(RxUtil.applySchedulers())
                    .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(listResponse -> {
                        hideProgress();
                        if (listResponse.status) {
                            showSuccess("Schedule created successfully ");
                        } else {
                            showError(listResponse.message);
                        }
                    },throwable -> showError(throwable.getMessage()));




        } catch (NullPointerException ex) {
            getBaseActivity().showError(ex.getMessage());
        } catch (Exception ex) {
            getBaseActivity().showError(ex.getMessage());
        }


    }


    @OnClick(R.id.edit)
    public void editClick() {

        try {

            if(durationPosition == 0){
                showError("Select duration");
                return;
            }

            if (TextUtils.isEmpty(startDate.getText().toString()) || TextUtils.isEmpty(endDate.getText().toString())
                    || TextUtils.isEmpty(txtPrice.getText().toString())) {
                //throw new NullPointerException("Please check date and price filed !");
                showError("Please check date and price filed !");
                return;
            }


            Schedule schedule = new Schedule();
            schedule.start_date = startDate.getText().toString();
            schedule.end_date = endDate.getText().toString();
            schedule.slot_price = txtPrice.getText().toString();
            schedule.doctor_id = getBaseActivity().getUser().id;
            schedule.currency = currency;


            Day mon = new Day();
            mon.data = new ArrayList<>();
            Day tue = new Day();
            tue.data = new ArrayList<>();
            Day wed = new Day();
            wed.data = new ArrayList<>();
            Day thr = new Day();
            thr.data = new ArrayList<>();
            Day fri = new Day();
            fri.data = new ArrayList<>();
            Day sat = new Day();
            sat.data = new ArrayList<>();
            Day sun = new Day();
            sun.data = new ArrayList<>();

            schedule.mon = mon;
            schedule.tue = tue;
            schedule.wed = wed;
            schedule.thr = thr;
            schedule.fri = fri;
            schedule.sat = sat;
            schedule.sun = sun;

            if (!((CheckBox) mondayTab.findViewById(R.id.mon_not_avail)).isChecked()) {

                for (int i = 1; i < mondayTab.getChildCount(); i++) {
                    DayTimeProvider daytime = (DayTimeProvider) mondayTab.getChildAt(i);

                    DayTime dayTime = daytime.getData();

                    if (dayTime.start_time.equals(dayTime.end_time)) {
                        showErrorWithTitle("Monday", "There should be atleast 1 hour difference between times slots or choose not available");
                        return;
                    } else if (dayTime.start_time.compareTo(dayTime.end_time) > 0) {
                        showErrorWithTitle("Monday", "start time should not be greater than end time");
                        return;
                    }

                    if(durationPosition == 3) {

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HOUR);
                        mon.data.addAll(dayTimes);


                    }else if(durationPosition == 2) {
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HALF_HOUR);

                        if(dayTimes != null) {
                            mon.data.addAll(dayTimes);
                        }else {
                            showErrorWithTitle("Monday", "There should be atleast 1 hour difference between times slots or choose not available");
                            return;
                        }
                    }else if(durationPosition == 1){

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),FIFTEEN_MIN);
                        mon.data.addAll(dayTimes);
                    }

                }
            }
            if (!((CheckBox) tuesdayTab.findViewById(R.id.tue_not_avail)).isChecked()) {
                for (int i = 1; i < tuesdayTab.getChildCount(); i++) {
                    DayTimeProvider daytime = (DayTimeProvider) tuesdayTab.getChildAt(i);
                    DayTime dayTime = daytime.getData();

                    if (dayTime.start_time.equals(dayTime.end_time)) {
                        showErrorWithTitle("Tuesday", "There should be atleast 1 hour difference between times slots or choose not available");
                        return;
                    } else if (dayTime.start_time.compareTo(dayTime.end_time) > 0) {
                        showErrorWithTitle("Tuesday", "start time should not be greater than end time");
                        return;
                    }

                    if(durationPosition == 3) {

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HOUR);
                        tue.data.addAll(dayTimes);


                    }else if(durationPosition == 2){
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HALF_HOUR);

                        if(dayTimes != null) {
                            tue.data.addAll(dayTimes);
                        }else {
                            showErrorWithTitle("Tuesday", "There should be atleast 1 hour difference between times slots or choose not available");
                            return;
                        }
                    }else if(durationPosition == 1){

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),FIFTEEN_MIN);
                        tue.data.addAll(dayTimes);
                    }


                }
            }
            if (!((CheckBox) wednesdayTab.findViewById(R.id.wed_not_avail)).isChecked()) {
                for (int i = 1; i < wednesdayTab.getChildCount(); i++) {
                    DayTimeProvider daytime = (DayTimeProvider) wednesdayTab.getChildAt(i);

                    DayTime dayTime = daytime.getData();

                    if (dayTime.start_time.equals(dayTime.end_time)) {
                        showErrorWithTitle("Wednesday", "There should be atleast 1 hour difference between times slots or choose not available");
                        return;
                    } else if (dayTime.start_time.compareTo(dayTime.end_time) > 0) {
                        showErrorWithTitle("Wednesday", "start time should not be greater than end time");
                        return;
                    }


                    if(durationPosition == 3) {

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HOUR);
                        wed.data.addAll(dayTimes);

                    }else if(durationPosition == 2){
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HALF_HOUR);

                        if(dayTimes != null) {
                            wed.data.addAll(dayTimes);
                        }else {
                            showErrorWithTitle("Wednesday", "There should be atleast 1 hour difference between times slots or choose not available");
                            return;
                        }
                    }else if(durationPosition == 1){

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),FIFTEEN_MIN);
                        wed.data.addAll(dayTimes);
                    }

                }
            }
            if (!((CheckBox) thrusdayTab.findViewById(R.id.thr_not_avail)).isChecked()) {
                for (int i = 1; i < thrusdayTab.getChildCount(); i++) {
                    DayTimeProvider daytime = (DayTimeProvider) thrusdayTab.getChildAt(i);

                    DayTime dayTime = daytime.getData();

                    if (dayTime.start_time.equals(dayTime.end_time)) {
                        showErrorWithTitle("Thursday", "There should be atleast 1 hour difference between times slots or choose not available");
                        return;
                    } else if (dayTime.start_time.compareTo(dayTime.end_time) > 0) {
                        showErrorWithTitle("Thursday", "start time should not be greater than end time");
                        return;
                    }


                    if(durationPosition == 3) {

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HOUR);
                        thr.data.addAll(dayTimes);

                    }else if(durationPosition == 2){
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HALF_HOUR);

                        if(dayTimes != null) {
                            thr.data.addAll(dayTimes);
                        }else {
                            showErrorWithTitle("Thursday", "There should be atleast 1 hour difference between times slots or choose not available");
                            return;
                        }
                    }else if(durationPosition == 1){

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),FIFTEEN_MIN);
                        thr.data.addAll(dayTimes);
                    }
                }
            }
            if (!((CheckBox) fridayTab.findViewById(R.id.fri_not_avail)).isChecked()) {
                for (int i = 1; i < fridayTab.getChildCount(); i++) {
                    DayTimeProvider daytime = (DayTimeProvider) fridayTab.getChildAt(i);

                    DayTime dayTime = daytime.getData();

                    if (dayTime.start_time.equals(dayTime.end_time)) {
                        showErrorWithTitle("Friday", "There should be atleast 1 hour difference between times slots or choose not available");
                        return;
                    } else if (dayTime.start_time.compareTo(dayTime.end_time) > 0) {
                        showErrorWithTitle("Friday", "start time should not be greater than end time");
                        return;
                    }


                    if(durationPosition == 3) {

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HOUR);
                        fri.data.addAll(dayTimes);

                    }else if(durationPosition == 2){
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HALF_HOUR);

                        if(dayTimes != null) {
                            fri.data.addAll(dayTimes);
                        }else {
                            showErrorWithTitle("Friday", "There should be atleast 1 hour difference between times slots or choose not available");
                            return;
                        }
                    }else if(durationPosition == 1){

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),FIFTEEN_MIN);
                        fri.data.addAll(dayTimes);
                    }
                }
            }

            if (!((CheckBox) satTab.findViewById(R.id.sat_not_avail)).isChecked()) {
                for (int i = 1; i < satTab.getChildCount(); i++) {
                    DayTimeProvider daytime = (DayTimeProvider) satTab.getChildAt(i);

                    DayTime dayTime = daytime.getData();

                    if (dayTime.start_time.equals(dayTime.end_time)) {
                        showErrorWithTitle("Saturday", "There should be atleast 1 hour difference between times slots or choose not available");
                        return;
                    } else if (dayTime.start_time.compareTo(dayTime.end_time) > 0) {
                        showErrorWithTitle("Saturday", "start time should not be greater than end time");
                        return;
                    }

                    if(durationPosition == 3) {
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HOUR);
                        sat.data.addAll(dayTimes);
                    }else if(durationPosition == 2){
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HALF_HOUR);

                        if(dayTimes != null) {
                            sat.data.addAll(dayTimes);
                        }else {
                            showErrorWithTitle("Saturday", "There should be atleast 1 hour difference between times slots or choose not available");
                            return;
                        }
                    }else if(durationPosition == 1){

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),FIFTEEN_MIN);
                        sat.data.addAll(dayTimes);
                    }
                }
            }

            if (!((CheckBox) sunTab.findViewById(R.id.sun_not_avail)).isChecked()) {
                for (int i = 1; i < sunTab.getChildCount(); i++) {
                    DayTimeProvider daytime = (DayTimeProvider) sunTab.getChildAt(i);

                    DayTime dayTime = daytime.getData();

                    if (dayTime.start_time.equals(dayTime.end_time)) {
                        showErrorWithTitle("Sunday", "There should be atleast 1 hour difference between times slots or choose not available");
                        return;
                    } else if (dayTime.start_time.compareTo(dayTime.end_time) > 0) {
                        showErrorWithTitle("Sunday", "start time should not be greater than end time");
                        return;
                    }


                    if(durationPosition == 3) {

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HOUR);
                        sun.data.addAll(dayTimes);

                    }else if(durationPosition == 2) {
                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),HALF_HOUR);

                        if(dayTimes != null) {
                            sun.data.addAll(dayTimes);
                        }else {
                            showErrorWithTitle("Sunday", "There should be atleast 1 hour difference between times slots or choose not available");
                            return;
                        }
                    }else if(durationPosition == 1){

                        List<DayTime> dayTimes =  distributeTime(daytime.getData(),FIFTEEN_MIN);
                        sun.data.addAll(dayTimes);
                    }
                }
            }



            showProgress("Updating Schedule");
        apiFactory.updateschedule(formatTimeZone(schedule))
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(listResponse -> {
                    hideProgress();
                    if (listResponse.status) {
                        showSuccess("Schedule updated successfully ");
                    } else {
                        showError(listResponse.message);
                    }
                },throwable -> showError(throwable.getMessage()));

        } catch (NullPointerException ex) {
            getBaseActivity().showError(ex.getMessage());
        } catch (Exception ex) {
            getBaseActivity().showError(ex.getMessage());
        }


    }


        private Schedule formatTimeZone(Schedule schedule) {

        for(DayTime mon: schedule.getMon().data){
            mon.start_time = Utils.localToUTC(mon.start_time);
            mon.end_time = Utils.localToUTC(mon.end_time);
        }
        for(DayTime tue: schedule.getTue().data){
            tue.start_time = Utils.localToUTC(tue.start_time);
            tue.end_time = Utils.localToUTC(tue.end_time);
        }
        for(DayTime wed: schedule.getWed().data){
            wed.start_time = Utils.localToUTC(wed.start_time);
            wed.end_time = Utils.localToUTC(wed.end_time);
        }
        for(DayTime thr: schedule.getThr().data){
            thr.start_time = Utils.localToUTC(thr.start_time);
            thr.end_time = Utils.localToUTC(thr.end_time);
        }
        for(DayTime fri: schedule.getFri().data){
            fri.start_time = Utils.localToUTC(fri.start_time);
            fri.end_time = Utils.localToUTC(fri.end_time);
        }
        for(DayTime sat: schedule.getSat().data){
            sat.start_time = Utils.localToUTC(sat.start_time);
            sat.end_time = Utils.localToUTC(sat.end_time);
        }
        for(DayTime sun: schedule.getSun().data){
            sun.start_time = Utils.localToUTC(sun.start_time);
            sun.end_time = Utils.localToUTC(sun.end_time);
        }


        return schedule;





    }


    public List<DayTime> distributeTime(DayTime dayTime,long hour) throws ParseException {

        ArrayList<String> times = new ArrayList<>();


       ArrayList<DayTime> dayTimes = new ArrayList<>();


        try {
            String time1 = dayTime.start_time;
            String time2 = dayTime.end_time;

            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            Date date1 = format.parse(time1);
            Date date2 = format.parse(time2);

            String startdateFormatted = format.format(date1);

            long startTimeinMillis = date1.getTime();
            long endTimeMillis = date2.getTime();
            long difference = endTimeMillis - startTimeinMillis;




            if(difference < HOUR){
               return null;
            }


            times.add(startdateFormatted);

            int dividedtime = Math.round(difference/hour);

            for (int i = 1; i <= dividedtime; i++) {


                startTimeinMillis += hour;

                Date date = new Date(startTimeinMillis);
                DateFormat formatter = new SimpleDateFormat("HH:mm");
                String dateFormatted = formatter.format(date);


                times.add(dateFormatted);

                Log.d(TAG, "\nslot "+i+" "+dateFormatted);


            }

            for (int i = 0; i < times.size()-1; i++) {

                Log.d(TAG, ""+times.get(i) +" "+times.get(i+1));
                dayTimes.add(new DayTime(dayTime.getOpen(),times.get(i),times.get(i+1)));


            }




        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }


         return dayTimes;

    }


    private static ArrayList<View> getViewsByTag(ViewGroup root, Integer tag){
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByTag((ViewGroup) child, tag));
            }

            final Object tagObj = child.getTag();
            if (tagObj != null && tagObj.equals(tag)) {
                views.add(child);
            }

        }
        return views;
    }


}
