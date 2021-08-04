package com.mdcbeta.patient.PersonalCare;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.desai.vatsal.mydynamiccalendar.EventModel;
import com.desai.vatsal.mydynamiccalendar.GetEventListListener;
import com.desai.vatsal.mydynamiccalendar.MyDynamicCalendar;
import com.desai.vatsal.mydynamiccalendar.OnDateClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.model.GroupAndUser;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.widget.ActionBar;
import com.mdcbeta.widget.CustomSpinnerAdapter;
import com.mdcbeta.widget.HorizontalFlowLayout;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by MahaRani on 16/01/2018.
 */

public class FragmentDiet extends BaseFragment implements BlockingStep {

    @BindView(R.id.dietbtn)
    FloatingActionButton dietbtn;

    @BindView(R.id.myCalendar)
    MyDynamicCalendar myCalendar;


    dateshow d;
    timeshow t;
    String res, dtime;
    TextView namefood,nameside,namedrink;


    private CustomSpinnerAdapter<GroupAndUser> adapter1;


    @Inject
    ApiFactory apiFactory;

    private static final String TAG = "Step3Fragment";

    public FragmentDiet() {
    }

    public static FragmentDiet newInstance() {
        FragmentDiet fragment = new FragmentDiet();
        return fragment;
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_diet;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    //    getGroupsandUsers();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        myCalendar.showMonthViewWithBelowEvents();

    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }



    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }


    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @OnClick(R.id.dietbtn)
    public void onViewClicked() {
        exercisedialog();
    }


    public void exercisedialog() {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.diet_dailog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);

        final AlertDialog ex_alert = alertDialogBuilder.create();
        ex_alert.getWindow().getAttributes().windowAnimations = R.style.MyDailog_Animination;

        final Button date = (Button) promptsView.findViewById(R.id.diet_date);
        final Button time = (Button) promptsView.findViewById(R.id.diet_time);
        final Button add_main_course = (Button) promptsView.findViewById(R.id.add_main_course);
        final Button add_sides = (Button) promptsView.findViewById(R.id.add_sides);
        final Button add_drinks = (Button) promptsView.findViewById(R.id.add_drinks);
        final Button cancel = (Button) promptsView.findViewById(R.id.diet_cancel);
        final Button ok = (Button) promptsView.findViewById(R.id.diet_ok);
        final  EditText othersOptions = (EditText) promptsView.findViewById(R.id.othersOptions);
        final  EditText othersQuantity = (EditText) promptsView.findViewById(R.id.othersQuantity);
        final  EditText othersCalories = (EditText) promptsView.findViewById(R.id.othersCalories);
        namefood = (TextView) promptsView.findViewById(R.id.txt);
        namedrink = (TextView) promptsView.findViewById(R.id.txtdrink);
        nameside = (TextView) promptsView.findViewById(R.id.txtside);
        final HorizontalFlowLayout addtext = (HorizontalFlowLayout) promptsView.findViewById(R.id.referrerItems);


        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
             //   d = new dateshow(date, getActivity());
            }
        });

        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            //    t = new timeshow(time, getActivity());
            }

        });

        add_main_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addMainCourse();


            }
        });

        add_sides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addSides();

            }
        });

        add_drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addDrink();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ex_alert.cancel();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (t == null || d == null) {
                    toast.settext(getActivity(), "Time and Date is Empty");
                    return;
                }


                String a = date.getText().toString();
                String b = time.getText().toString();
                String c = othersOptions.getText().toString();
                String d = othersQuantity.getText().toString();
                String e = othersCalories.getText().toString();

                SimpleDateFormat from = new SimpleDateFormat("MMM dd , yyyy", Locale.US);
                SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);


                try {
                    res = to.format(from.parse(a));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }


                String[] parts = b.split(" ");
                dtime = parts[0];


                if (a.equals("CHOOSE DATE")) {
                    toast.settext(getActivity(), "Select Date");
                    return;
                }

                if (b.equals("CHOOSE TIME")) {
                    toast.settext(getActivity(), "Select Time");
                    return;
                }

                if (!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b)) {



                    showProgress("Loading");

                    apiFactory.addDietRecord(getUser().getId(), res,
                            dtime, "Starter",
                            "Corn Soup", "1", "87",
                            com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()),
                            com.mdcbeta.util.Utils.localToUTCDateTime(com.mdcbeta.util.Utils.getDatetime()))
                            .compose(RxUtil.applySchedulers())
                            .subscribe(model -> {
                                if(model.status) {
                                    hideProgress();
                                    //   callback.onYesClick();
                                    //   dismiss();

                                }else {
                                    showError(model.getMessage());
                                }

                            },throwable -> showError(throwable.getMessage()));



                } else {
                    Toast t = Toast.makeText(getActivity().getApplicationContext(), " Fields is Empty", Toast.LENGTH_SHORT);
                    t.getView().setBackgroundColor(Color.RED);
                    t.show();

                }

                ex_alert.cancel();

            }
        });


        ex_alert.show();
    }

    public void addMainCourse()
    {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.add_maincourse_dailog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);

        final AlertDialog ex_alert = alertDialogBuilder.create();
        ex_alert.getWindow().getAttributes().windowAnimations = R.style.MyDailog_Animination;

        final Spinner maincourse_food_type = (Spinner) promptsView.findViewById(R.id.maincourse_food_type);
        final Button maincourse_add_new = (Button) promptsView.findViewById(R.id.maincourse_add_new);
        final Button maincourse_submit = (Button) promptsView.findViewById(R.id.maincourse_submit);
        final  EditText maincourse_dish = (EditText) promptsView.findViewById(R.id.maincourse_dish);
        final  EditText maincourse_quantity = (EditText) promptsView.findViewById(R.id.maincourse_quantity);
        final  EditText maincourse_calories = (EditText) promptsView.findViewById(R.id.maincourse_calories);


        final ArrayAdapter spinneradapter = ArrayAdapter.createFromResource(getActivity(), R.array.Foodtype,
                android.R.layout.simple_spinner_item);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        maincourse_food_type.setAdapter(spinneradapter);


        maincourse_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                namefood.setText(maincourse_dish.getText());

            //    Toast.makeText(getContext(), "abc",Toast.LENGTH_LONG).show();

                ex_alert.cancel();
            }
        });


        ex_alert.show();
    }

    public void addSides()
    {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.add_sides_dailog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);

        final AlertDialog ex_alert = alertDialogBuilder.create();
        ex_alert.getWindow().getAttributes().windowAnimations = R.style.MyDailog_Animination;

        final Spinner sides_food_type = (Spinner) promptsView.findViewById(R.id.sides_food_type);
        final Button sides_add_new = (Button) promptsView.findViewById(R.id.sides_add_new);
        final Button sides_submit = (Button) promptsView.findViewById(R.id.sides_submit);
        final  EditText sides_dish = (EditText) promptsView.findViewById(R.id.sides_dish);
        final  EditText sides_quantity = (EditText) promptsView.findViewById(R.id.sides_quantity);
        final  EditText sides_calories = (EditText) promptsView.findViewById(R.id.sides_calories);

        final ArrayAdapter spinneradapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.Foodtype,
                android.R.layout.simple_spinner_item);
        spinneradapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        sides_food_type.setAdapter(spinneradapter1);

        sides_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameside.setText(sides_dish.getText());

                ex_alert.cancel();
            }
        });

        ex_alert.show();
    }


    public void addDrink()
    {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.add_drink_dailog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        alertDialogBuilder.setView(promptsView);

        final AlertDialog ex_alert = alertDialogBuilder.create();
        ex_alert.getWindow().getAttributes().windowAnimations = R.style.MyDailog_Animination;

        final Spinner drink_type = (Spinner) promptsView.findViewById(R.id.drink_type);
        final Button drink_add_new = (Button) promptsView.findViewById(R.id.drink_add_new);
        final Button drink_submit = (Button) promptsView.findViewById(R.id.drink_submit);
        final  EditText drink_quantity = (EditText) promptsView.findViewById(R.id.drink_quantity);
        final  EditText drink_name = (EditText) promptsView.findViewById(R.id.drink_name);
        final  EditText drink_calories = (EditText) promptsView.findViewById(R.id.drink_calories);

        final ArrayAdapter spinneradapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.Foodtype,
                android.R.layout.simple_spinner_item);
        spinneradapter2.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        drink_type.setAdapter(spinneradapter2);

        drink_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                namedrink.setText(drink_name.getText());
                ex_alert.cancel();
            }
        });


        ex_alert.show();
    }



    private void showMonthView() {

        myCalendar.showMonthView();

        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date) {
                Log.e("date", String.valueOf(date));
            }

            @Override
            public void onLongClick(Date date) {
                Log.e("date", String.valueOf(date));
            }
        });

    }

    private void showMonthViewWithBelowEvents() {

        myCalendar.showMonthViewWithBelowEvents();

        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date) {
                Log.e("date", String.valueOf(date));
            }

            @Override
            public void onLongClick(Date date) {
                Log.e("date", String.valueOf(date));
            }
        });

    }



    @Override
    public void onStart() {
        super.onStart();

        showProgress("Loading");
        apiFactory.getDiet(getUser().getId())
                .compose(RxUtil.applySchedulers())
                .subscribe(resource -> {
                    hideProgress();

                    if(resource.status) {

                        JsonObject data = resource.data.get("details").getAsJsonObject();
                        try {
                         //   txtName.setText(data.get("fname").getAsString());

                            SimpleDateFormat abc = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            SimpleDateFormat xyz = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                            String dd = xyz.format(abc.parse(data.get("date").getAsString()));

                       //     myCalendar.addEvent("5-03-2018", "8:00", "8:15", "Today Event 1");
                       //     myCalendar.addEvent("05-10-2018", "8:15", "8:30", "Today Event 2");
                      //      myCalendar.addEvent("05-10-2018", "8:30", "8:45", "Today Event 3");
                      //      myCalendar.addEvent("05-10-2018", "8:45", "9:00", "Today Event 4");
                     //       myCalendar.addEvent("8-03-2018", "8:00", "8:30", "Today Event 5");
                     //       myCalendar.addEvent("08-10-2018", "9:00", "10:00", "Today Event 6");
                    //        myCalendar.addEvent("23-03-2018", "9:00", "10:00", "Today Event 7");
                            myCalendar.addEvent(dd, "9:00", "10:00", "abc");

                            myCalendar.getEventList(new GetEventListListener() {
                                @Override
                                public void eventList(ArrayList<EventModel> eventList) {

                                    Log.e("tag", "eventList.size():-" + eventList.size());
                                    for (int i = 0; i < eventList.size(); i++) {
                                        Log.e("tag", "eventList.getStrName:-" + eventList.get(i).getStrName());
                                    }

                                }
                            });

                            myCalendar.setBelowMonthEventTextColor("#00BFFF");
//        myCalendar.setBelowMonthEventTextColor(R.color.black);

                            myCalendar.setBelowMonthEventDividerColor("#635478");
//        myCalendar.setBelowMonthEventDividerColor(R.color.black);

                            myCalendar.setHolidayCellBackgroundColor("#00BFFF");
//        myCalendar.setHolidayCellBackgroundColor(R.color.black);

                            myCalendar.setHolidayCellTextColor("#d590bb");
//        myCalendar.setHolidayCellTextColor(R.color.black);

                            myCalendar.setHolidayCellClickable(true);

                      //      Toast.makeText(getContext(), "Test", Toast.LENGTH_LONG).show();


                        //     myCalendar.addHoliday("2-03-2018");
                       //     myCalendar.addHoliday("8-03-2018");
                       //     myCalendar.addHoliday("12-03-2018");
                       //     myCalendar.addHoliday("13-03-2018");
                        //    myCalendar.addHoliday("8-03-2018");
                          //  myCalendar.addHoliday("10-03-2018");
                         //   myCalendar.addHoliday("28-03-2018");
                        //    String dd = xyz.format(abc.parse(data.get("date").getAsString()));
                            myCalendar.addHoliday(dd);
                            Log.d("Date",data.get("date").getAsString() );
                            Log.d("Formatted Date", xyz.format(abc.parse(data.get("date").getAsString())));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                    }else {
                        showError(resource.getMessage());
                    }

                },throwable -> {
                    showError(throwable.getMessage());

                });



        myCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onClick(Date date) {

                getFragmentHandlingActivity()
                        .replaceFragmentWithBackstack(FragmentDietList.newInstance(getUser().getId()));

       //         Toast.makeText(getContext(),getUser().getId() + ",   " + date,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onLongClick(Date date) {

            }
        });

    }


}
