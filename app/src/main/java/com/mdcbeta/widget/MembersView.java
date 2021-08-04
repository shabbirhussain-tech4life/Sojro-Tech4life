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
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mdcbeta.R;
import com.mdcbeta.app.MyApplication;
import com.mdcbeta.data.model.User;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Response;
import com.mdcbeta.data.remote.model.UserNames;
import com.mdcbeta.util.AppPref;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Shakil Karim on 4/23/17.
 */

public class MembersView extends LinearLayout {
    HorizontalFlowLayout horizontalFlowLayout;
    AppCompatAutoCompleteTextView autocompletetextview;


    @Inject
    ApiFactory apiFactory;
    User user;


    public MembersView(Context context) {
        super(context);
        init(context);

    }

    public MembersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MembersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MembersView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }





    public void init(Context context){
        ((MyApplication)context.getApplicationContext()).getAppComponent().inject(this);

        setOrientation(VERTICAL);

        user = AppPref.getInstance(context).getUser();



        horizontalFlowLayout = new HorizontalFlowLayout(getContext());
        horizontalFlowLayout.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        autocompletetextview = new AppCompatAutoCompleteTextView(getContext());
        autocompletetextview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        autocompletetextview.setHint("Choose members...");
        autocompletetextview.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        autocompletetextview.setSingleLine(true);
        autocompletetextview.setThreshold(1);

        MembersAdapter membersAdapter = new MembersAdapter(context);
        autocompletetextview.setAdapter(membersAdapter);



        autocompletetextview.setOnItemClickListener((parent, view, position, id) -> {

            LinearLayout linearLayout = (LinearLayout) getBubbleView();
            TextView view1 = (TextView) linearLayout.findViewById(R.id.textname);
            view1.setText(membersAdapter.getItem(position).getName());
            view1.setTag(Integer.valueOf(membersAdapter.getItem(position).getId()));


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



    public List<Integer> getMembersId(){

        List<Integer> ids = new ArrayList<Integer>();

        int size = horizontalFlowLayout.getChildCount();

        for (int i = 0; i < size; i++) {
            final LinearLayout linearLayout = (LinearLayout) horizontalFlowLayout.getChildAt(i);
            TextView view1 = (TextView) linearLayout.findViewById(R.id.textname);
            ids.add((Integer) view1.getTag());

        }

        return  ids;

    }


    public List<String> getListSkills(String skills){

        return Arrays.asList(skills.split("\\s*,\\s*"));
    }



    public class MembersAdapter extends BaseAdapter implements Filterable {

        private static final int MAX_RESULTS = 10;
        private Context mContext;
        private List<UserNames> resultList = new ArrayList<UserNames>();

        public MembersAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public UserNames getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_drop_down_list, parent, false);
            }
            ((TextView) convertView.findViewById(R.id.text1)).setText(getItem(position).getName());

            return convertView;
        }

        @Override
        public Filter getFilter() {
           Filter filter = new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {

                        findMembers(constraint.toString(), o -> {
                                filterResults.values = o.data;
                                filterResults.count = o.data.size();
                            });


                    }
                    return filterResults;

                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        resultList = (List<UserNames>) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }};



            return filter;
        }


        private void findMembers(String name,Consumer<Response<List<UserNames>>> disposableObserver) {



            Observable.just(name)
                    .debounce(100, TimeUnit.MILLISECONDS)
                    .switchMap(string -> apiFactory.filterUsername(string.toString(),
                            user.getLicenses_id() == null ? "-1" :user.getLicenses_id()))
                    .subscribe(disposableObserver);


        }


    }


}
