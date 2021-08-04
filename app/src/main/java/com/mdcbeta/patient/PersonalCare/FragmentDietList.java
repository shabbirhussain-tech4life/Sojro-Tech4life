package com.mdcbeta.patient.PersonalCare;

import android.os.Bundle;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Diets;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.*;
import com.mdcbeta.widget.ActionBar;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by MahaRani on 29/03/2018.
 */

public class FragmentDietList extends BaseFragment {

    @Inject
    ApiFactory apiFactory;

    @BindView(R.id.main_list)
    RecyclerView diet_list;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    private DietAdapter adapter;

    static String mId;


    @Inject
    MDCRepository mdcRepository;


    @BindDimen(R.dimen.dp_150)
    int dp_150;
    @BindDimen(R.dimen.dp_10)
    int dp_10;


    @Override
    public int getLayoutID() {
        return R.layout.fragment_record_list;
    }

    public static FragmentDietList newInstance(String id) {
        FragmentDietList fragment = new FragmentDietList();
        mId = id;
        return fragment;
    }


    @Override
    public void getActionBar(ActionBar actionBar) {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }


    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }



    @Override
    public void onStart() {
        super.onStart();

        initList();

        List<Diets> data = new ArrayList<>();

        showProgress("Loading");
        apiFactory.getDietList(getUser().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model2 -> {

                    data.addAll(model2.getData());
                    adapter.swap(data);
                    hideProgress();

                }, t -> {
                    t.printStackTrace();
                    adapter.swap(data);

                });
    }

    private void initList() {
        adapter = new DietAdapter(getContext());
        diet_list.setAdapter(adapter);
        diet_list.setHasFixedSize(true);
        diet_list.setItemAnimator(new DefaultItemAnimator());
        diet_list.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        diet_list.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter.setItemClickListner((view, item) -> {
            switch (view.getId()) {
                case R.id.item_main:
                    break;
                case R.id.view_exercise:
                    getFragmentHandlingActivity()
                            .replaceFragmentWithBackstack(DietDetailFragment.newInstance(item.getUserId()));
                    //  Toast.makeText(getContext(), item.getGroup_id(), Toast.LENGTH_LONG).show();
                    break;

                case R.id.btn_delete:
                    showProgress("Deleting record");
                    apiFactory.dietDelete(item.getId())
                            .compose(RxUtil.applySchedulers())
                            .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                            .subscribe(model -> {
                                hideProgress();
                                if (model.status) {
                                    hideProgress();
                                    initList();
                                } else {
                                    showError(model.message);
                                }

                            },Throwable::printStackTrace);
                    break;




            }
        });
    }


}
