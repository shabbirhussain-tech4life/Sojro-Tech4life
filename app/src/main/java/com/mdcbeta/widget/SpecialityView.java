package com.mdcbeta.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mdcbeta.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shakil Karim on 4/23/17.
 */

public class SpecialityView extends LinearLayout {


    HorizontalFlowLayout horizontalFlowLayout;
    AppCompatAutoCompleteTextView autocompletetextview;

    String[] skill;
    HashMap<String,Integer> map;



    public SpecialityView(Context context) {
        super(context);
        init();

    }

    public SpecialityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpecialityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpecialityView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }





    public void init(){
        setOrientation(VERTICAL);

        skill = getResources().getStringArray(R.array.speciality);
        map = new HashMap<>();
        buildData();

        horizontalFlowLayout = new HorizontalFlowLayout(getContext());
        horizontalFlowLayout.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        autocompletetextview = new AppCompatAutoCompleteTextView(getContext());
        autocompletetextview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        autocompletetextview.setHint("Add speciality...");
        autocompletetextview.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        autocompletetextview.setSingleLine(true);
        autocompletetextview.setThreshold(1);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(),android.R.layout.select_dialog_item, skill);

        autocompletetextview.setAdapter(adapter);



        autocompletetextview.setOnItemClickListener((parent, view, position, id) -> {

            LinearLayout linearLayout = (LinearLayout) getBubbleView();
            TextView view1 = (TextView) linearLayout.findViewById(R.id.textname);

            view1.setText(autocompletetextview.getText().toString());


            horizontalFlowLayout.addView(linearLayout);


            autocompletetextview.setText("");

        });


        addView(horizontalFlowLayout);
        addView(autocompletetextview);



    }

    public View getBubbleView(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.view_bubble, horizontalFlowLayout, false);

        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                horizontalFlowLayout.removeView(v);

            }
        });


        return  v;
    }


    public void buildData(){
        map.put("Pathology",9);
        map.put("Radiology",10);
        map.put("Cardiology",11);
        map.put("Other",12);
        map.put("Dermatoloty",13);
        map.put("paediatrics",16);
        map.put("ENT",21);
        map.put("general practitioner",22);
        map.put("family medicine",23);
        map.put("internal medicine",32);
        map.put("opthalmology",33);
        map.put("neurology",34);
        map.put("neurosurgery",35);

    }

    public void isEditable(boolean flag){

        autocompletetextview.setVisibility(flag ? VISIBLE : GONE);

    }

    public void addSkills(List<String> skills){

        if(skills != null) {

            horizontalFlowLayout.removeAllViews();

            for (String s : skills) {

                final LinearLayout linearLayout = (LinearLayout) getBubbleView();
                TextView view1 = (TextView) linearLayout.findViewById(R.id.textname);
                view1.setText(s);
                horizontalFlowLayout.addView(linearLayout);


            }
        }



    }

    public void addSkillsText(String skills){

        addSkills(getListSkills(skills));

    }



    public List<Integer> getSepicallitIds(){


        List<Integer> ids = new ArrayList<Integer>();
        int size = horizontalFlowLayout.getChildCount();
        for (int i = 0; i < size; i++) {
            final LinearLayout linearLayout = (LinearLayout) horizontalFlowLayout.getChildAt(i);
            TextView view1 = (TextView) linearLayout.findViewById(R.id.textname);
            ids.add(map.get(view1.getText().toString()));

        }



        return  ids;

    }


    public List<String> getListSkills(String skills){

        return Arrays.asList(skills.split("\\s*,\\s*"));


    }





}
