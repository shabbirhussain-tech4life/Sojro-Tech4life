package com.mdcbeta.healthprovider.group;


import android.os.Bundle;

import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.widget.ActionBar;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.Unbinder;


public class GroupDetailFragment extends BaseFragment {

    @Inject
    ApiFactory apiFactory;
    @BindView(R.id.txt_group_members)
    TextView txtGroupMembers;
    @BindView(R.id.txt_group_speciality)
    TextView txtGroupSpeciality;
    Unbinder unbinder;

    private String mGroupID;


    public GroupDetailFragment() {
    }


    public static Fragment newInstance(String groupID) {
        Bundle args = new Bundle();
        GroupDetailFragment fragment = new GroupDetailFragment();
        args.putString("groupid", groupID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupID = getArguments().getString("groupid");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showProgress("loading..");
        apiFactory.groupDetail(mGroupID)
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model -> {
                    hideProgress();

                    try {

                        if (model != null && model.getStatus() && model.getData() != null) {

                            ArrayList<String> mylist = new ArrayList<String>();
                            for (int i = 0; i < model.getData().size(); i++) {

                                mylist.add(model.getData().get(i).get("name").getAsString());

                            }

                            Set<String> set = new HashSet<String>(mylist);

                            String[] result = new String[set.size()];
                            set.toArray(result);
                            for (String s : result) {

                                txtGroupMembers.append("." + s + "\n");
                            }

                      //      txtGroupSpeciality.append("." + model.getData().get(0).get("group_speciality").getAsString() + "\n");

                        }

                    }catch (Exception ex){

                    }

                    try {

                        if (model != null && model.getStatus() && model.getData() != null) {

                            ArrayList<String> mylist = new ArrayList<String>();
                            for (int i = 0; i < model.getData().size(); i++) {

                                mylist.add(model.getData().get(i).get("group_speciality").getAsString());

                            }

                            Set<String> set = new HashSet<String>(mylist);

                            String[] result = new String[set.size()];
                            set.toArray(result);
                            for (String s : result) {

                                txtGroupSpeciality.append("." + s + "\n");
                            }

                        //    txtGroupSpeciality.append("." + model.getData().get(0).get("group_speciality").getAsString() + "\n");

                        }

                    }catch (Exception ex){

                    }


                },Throwable::printStackTrace);


    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_group_detail;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }



}
