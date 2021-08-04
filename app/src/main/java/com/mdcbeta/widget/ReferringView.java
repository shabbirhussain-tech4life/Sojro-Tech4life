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
import java.util.List;

/**
 * Created by Shakil Karim on 5/14/17.
 */

public class ReferringView extends LinearLayout {

    HorizontalFlowLayout horizontalFlowLayout;
    AppCompatAutoCompleteTextView autocompletetextview;

    String[] skill;



    public ReferringView(Context context) {
        super(context);
        init();

    }

    public ReferringView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReferringView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ReferringView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }





    public void init(){
        setOrientation(VERTICAL);

        skill = getResources().getStringArray(R.array.referingto);


        horizontalFlowLayout = new HorizontalFlowLayout(getContext());
        horizontalFlowLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        autocompletetextview = new AppCompatAutoCompleteTextView(getContext());
        autocompletetextview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        autocompletetextview.setHint("Select groups/individuals...");
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
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalFlowLayout.removeView(v);

            }
        });


        return  v;
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





    public String getTextSkills(){


        List<String> ids = new ArrayList<String>();

        int size = horizontalFlowLayout.getChildCount();

        for (int i = 0; i < size; i++) {
            final LinearLayout linearLayout = (LinearLayout) horizontalFlowLayout.getChildAt(i);
            TextView view1 = (TextView) linearLayout.findViewById(R.id.textname);
            ids.add(view1.getText().toString());

        }

        String allIds = android.text.TextUtils.join(",", ids);


        return  allIds;

    }


    public List<String> getListSkills(String skills){

        return Arrays.asList(skills.split("\\s*,\\s*"));


    }


}
