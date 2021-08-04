package com.mdcbeta.patient.appointment;

import android.graphics.Typeface;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.DoctorSchedule;
import com.mdcbeta.data.remote.model.TimeSlots;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.appointment.HealthProviderAppointmentFragment;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.RxBus;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.Utils;
import com.mdcbeta.widget.ActionBar;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MahaRani on 28/05/2018.
 */

public class Step6Fragment extends BaseFragment implements BlockingStep {

    @BindViews({R.id.jan,R.id.feb,R.id.mar, R.id.apr, R.id.may, R.id.june, R.id.july, R.id.aug, R.id.sep, R.id.oct, R.id.nov, R.id.dec})
    List<CheckedTextView> months;

    @BindView(R.id.book_slot)
    RelativeLayout bookSlot;

    @Inject
    ApiFactory apiFactory;

    @BindView(R.id.date_slot)
    LinearLayout dateSlot;
    @BindView(R.id.timeslot)
    LinearLayout timeslot;
    @BindView(R.id.no_dates_view)
    TextView no_dates_view;

    @BindView(R.id.months_list)
    HorizontalScrollView months_lst;

    @BindView(R.id.submit)
    Button submit;

    @Inject
    RxBus rxBus;



    private static final String TAG = "Step4Fragment";
    private int pad8;
    private int pad10;
    private int pad16;
    private int pad100;

    private String docId;
    private int currentMonth;
    private String scheduleId;
    private String startDate;
    private String endDate;
    private boolean isfromcase;
    private String dotorID;
    private String date;
    private String slotId;
    private String caseID;
    private String booked_as = "referrer";


    @Override
    public int getLayoutID() {
        return R.layout.fragment_step4;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        // callback.goToNextStep();

        if(AppPref.getInstance(getActivity()).getString(AppPref.Key.STEP_SLOT_ID) == null){
            showError("Please select some time slot!");
        }else {
            callback.goToNextStep();
        }

    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }


    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: ");
        pad8 = (int) Utils.pxFromDp(8,getContext());
        pad10 = (int) Utils.pxFromDp(10,getContext());
        pad16 = (int) Utils.pxFromDp(16,getContext());
        pad100 = (int) Utils.pxFromDp(100,getContext());


    }


    public static Step6Fragment newInstance(boolean isfromcase,String doctorID,String caseID, String booked) {
        Step6Fragment fragment = new Step6Fragment();
        Bundle args = new Bundle();
        args.putBoolean("is_from_case_detail",isfromcase);
        args.putString("doc_id",doctorID);
        args.putString("case_id",caseID);
        args.putString("booked_as",booked);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() !=null){
            isfromcase = getArguments().getBoolean("is_from_case_detail");
            dotorID = getArguments().getString("doc_id");
            caseID = getArguments().getString("case_id");
            booked_as = getArguments().getString("booked_as");
        }
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {


      /*  if(AppPref.getInstance(getActivity()).getString(AppPref.Key.STEP_SLOT_ID) == null){
            showError("Please select some time slot!");
        }else {
            showProgress("Loading");

            getFragmentHandlingActivity().replaceFragment(PatientMainFragment.newInstance());
            getActivity().startService(new Intent(getActivity(),BookAppointmentService.class));

            showSuccess("Appointment created successfully");
            hideProgress();


        }*/


    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!isfromcase){
            submit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(isfromcase){
            fetchData();
        }
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

        fetchData();
    }

    private void fetchData()
    {


        if(isfromcase){
            docId = dotorID;
        }else {
            docId = AppPref.getInstance(getActivity()).getString(AppPref.Key.STEP_DOC_ID);
        }

//        ButterKnife.apply(months, DISABLE);
//        currentMonth = Utils.currentMounth();
        for (int i = 0; i < currentMonth - 1; i++) {
            months.get(i).setVisibility(View.GONE);
        }
        months.get(currentMonth - 1).setChecked(true);


        apiFactory.getDoctorTimeSlots(docId)
                .compose(RxUtil.applySchedulers())
                .subscribe(model -> {
                            //Toast.makeText(getContext(),"model"+model.getData(),Toast.LENGTH_LONG).show();
                    hideProgress();
//added by kanwal khan
                    if (model.getData().size() > 0)
                        // if(model.data !=null)
                         {
                        DoctorSchedule doctorSchedule = model.getData().get(0);
                            // Toast.makeText(getContext(),"doctor"+doctorSchedule,Toast.LENGTH_LONG).show();
                    startDate = doctorSchedule.getStartDate();
                    endDate = doctorSchedule.getEndDate();
                    scheduleId = doctorSchedule.getId();

                    fetchDateAndPopulateUi();
                }
                else {
                    Toast.makeText(getContext(),"No any schedule is defined ",Toast.LENGTH_LONG).show();
                        //Calendar currentDate = Calendar.getInstance();
                        //startDate="";
                        //endDate="";
                        //scheduleId="";
                        //fetchDateAndPopulateUi();
                        no_dates_view.setVisibility(View.VISIBLE);
                        months_lst.setVisibility(View.GONE);
                        submit.setVisibility(View.GONE);


                    }

                }
                );
    }


    @Override
    public void onError(@NonNull VerificationError error) {

    }


//    static final ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
//        @Override
//        public void apply(View view, int index) {
//            ((CheckedTextView) view).setChecked(false);
//        }
//    };

//    @OnClick({R.id.jan,R.id.feb,R.id.mar, R.id.apr, R.id.may, R.id.june, R.id.july, R.id.aug, R.id.sep, R.id.oct, R.id.nov, R.id.dec})
//    public void onViewClicked(View view) {
//        ButterKnife.apply(months, DISABLE);
//        CheckedTextView checkedTextView = ButterKnife.findById(view, view.getId());
//        checkedTextView.setChecked(true);
//        currentMonth = months.indexOf(checkedTextView)+1;
//        fetchDateAndPopulateUi();
   // }

    private void fetchDateAndPopulateUi() {

        dateSlot.removeAllViews();
        timeslot.removeAllViews();

            List<Step6Fragment.DateData> list = fetchDatesExample(startDate, endDate);





//if(list.isEmpty())
//{
//    Toast.makeText(getContext(),"list"+list, Toast.LENGTH_LONG).show();
//}


        if(TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
            no_dates_view.setVisibility(View.VISIBLE);
            return;
        }
        // added if by kanwal
        if(TextUtils.isEmpty(startDate) && TextUtils.isEmpty(endDate))
        {
            no_dates_view.setVisibility(View.VISIBLE);
            return;
        }


        if(list == null || list.isEmpty()){
            no_dates_view.setVisibility(View.VISIBLE);
            // dateSlot.setVisibility();
            return;
        }else {
            no_dates_view.setVisibility(View.GONE);
        }

        // added by kanwal
        if(list == null && list.isEmpty()){
            no_dates_view.setVisibility(View.VISIBLE);
            //   dateSlot.setVisibility();
            return;
        }else {
            no_dates_view.setVisibility(View.GONE);
        }


        for (int i = 0; i < list.size(); i++) {

            CheckedTextView checktextView = new CheckedTextView(getContext());
            checktextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            checktextView.setTextColor(getResources().getColor(R.color.dark_grey));
            checktextView.setText(list.get(i).date);
            checktextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            checktextView.setTypeface(null, Typeface.BOLD);
            checktextView.setPadding(pad8,pad8,pad8,pad8);
            checktextView.setBackgroundResource(R.drawable.rounded_checked_white_blue_bg);
            checktextView.setTag(list.get(i));

            checktextView.setOnClickListener(v ->
            {

                for (int i1 = 0; i1 < dateSlot.getChildCount(); i1++) {
                    if( dateSlot.getChildAt(i1) instanceof Space){
                        continue;
                    }
                    ((CheckedTextView)dateSlot.getChildAt(i1)).setTextColor(getResources().getColor(R.color.dark_grey));
                    ((CheckedTextView)dateSlot.getChildAt(i1)).setChecked(false);
                }

                ((CheckedTextView)v).setChecked(true);
                ((CheckedTextView)v).setTextColor(getResources().getColor(R.color.white));

                Step6Fragment.DateData dateData = ((Step6Fragment.DateData)v.getTag());
                Log.d("Day of week", String.valueOf(dateData.day_of_week));


                AppPref.getInstance(getActivity()).put(AppPref.Key.STEP_DATE,dateData.datewithoutformat);
                date = dateData.datewithoutformat;

                showProgress("Loading");

                apiFactory.scheduleforReviewer(scheduleId,String.valueOf(dateData.day_of_week))
                        .compose(RxUtil.applySchedulers())
                        .subscribe(model -> {

                            hideProgress();

                            timeslot.removeAllViews();

                            List<TimeSlots> timeSlotses = model.getData();

                            if(timeSlotses == null || timeSlotses.isEmpty()){

                                CheckedTextView timeTextView = new CheckedTextView(getContext());
                                timeTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                timeTextView.setTextColor(getResources().getColor(R.color.white));
                                timeTextView.setText("No Slots Available");

                                timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
                                timeTextView.setPadding(pad16,pad16,pad16,pad16);
                                timeTextView.setBackgroundResource(R.color.red);

                                timeslot.addView(timeTextView);

                            }

                            for (int j = 0; j < timeSlotses.size(); j++) {

                                CheckedTextView timeTextView = new CheckedTextView(getContext());
                                timeTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                timeTextView.setTextColor(getResources().getColor(R.color.white));
                                timeTextView.setText(Utils.format122HourTime(Utils.UTCTolocal(timeSlotses.get(j).getStartTime()))+" - "+Utils.format122HourTime(Utils.UTCTolocal(timeSlotses.get(j).getEndTime())));
                                timeTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                                timeTextView.setTypeface(null, Typeface.BOLD);
                                timeTextView.setPadding(pad16,pad16,pad16,pad16);
                                timeTextView.setBackgroundResource(R.drawable.rounded_checked_green_blue_bg);
                                timeTextView.setTag(timeSlotses.get(j).getId());



                                timeTextView.setOnClickListener(v1 -> {

                                    for (int i1 = 0; i1 < timeslot.getChildCount(); i1++) {
                                        if( timeslot.getChildAt(i1) instanceof Space){
                                            continue;
                                        }
                                        ((CheckedTextView)timeslot.getChildAt(i1)).setChecked(false);
                                    }

                                    ((CheckedTextView) v1).setChecked(true);
                                    AppPref.getInstance(getActivity()).put(AppPref.Key.STEP_SLOT_ID,(String) v1.getTag());
                                    slotId = (String) v1.getTag();

                                });

                                Space space = new Space(getContext());
                                space.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                        pad10));

                                timeslot.addView(timeTextView);
                                timeslot.addView(space);

                            }

                        },throwable -> showError(throwable.getMessage()));

            });

            Space space = new Space(getContext());
            space.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    pad10));

            dateSlot.addView(checktextView);
            dateSlot.addView(space);

        }

    }

    public List<Step6Fragment.DateData> fetchDatesExample (String startDate, String endDate) {

        List<Step6Fragment.DateData> dateList = new ArrayList<>();


        Calendar currentDate = Calendar.getInstance();


        Calendar startCal = Calendar.getInstance();
        startCal.setTime(Utils.stringToDate(startDate));


            Calendar endCal = Calendar.getInstance();
            endCal.setTime(Utils.stringToDate(endDate));

        Calendar currentCal = Calendar.getInstance();
        currentCal.set(Calendar.DAY_OF_MONTH, 1);
        currentCal.set(Calendar.MONTH, currentMonth-1);


        while (currentMonth == currentCal.get(Calendar.MONTH)+1 ) {

            if(currentCal.getTime().compareTo(startCal.getTime()) >= 0 &&
                    currentCal.getTime().compareTo(endCal.getTime()) <= 0)
            {
                int dayofWeek = ((currentCal.get(Calendar.DAY_OF_WEEK) - 1) == 0 ? 7 : (currentCal.get(Calendar.DAY_OF_WEEK) - 1));
                Log.d("Current Date:  ", String.valueOf(Calendar.DAY_OF_MONTH));

                if(currentCal.compareTo(currentDate) >= 0) {
                    dateList.add(new Step6Fragment.DateData(Utils.getFormateddate(currentCal.getTime()),
                            dayofWeek, Utils.getDate(currentCal.getTime())));
                }



            }

            currentCal.add(Calendar.DAY_OF_MONTH, 1);



        }


        return dateList;

    }


    @OnClick(R.id.submit) void click(){

        showProgress("Loading");

        apiFactory.createAppointmentfromMycase(caseID,dotorID,getUser().getId(),slotId,getUser().getId(),
                date,Utils.getDatetime(), UUID.randomUUID().toString(), booked_as)
                .compose(RxUtil.applySchedulers())
                .subscribe(model-> {

                    if(model.status){
                        showSuccess(model.getMessage());
                        getFragmentHandlingActivity().replaceFragment(HealthProviderAppointmentFragment.newInstance());
                    }
                    else {
                        showError(model.getMessage());
                    }

                },throwable -> {
                    hideProgress();
                    throwable.printStackTrace();
                });
    }


    private class DateData{

        public String date;
        public int day_of_week;
        public String datewithoutformat;

        public DateData(String date, int day_of_week, String datewithoutformat) {
            this.date = date;
            this.day_of_week = day_of_week;
            this.datewithoutformat = datewithoutformat;
        }
    }

}

