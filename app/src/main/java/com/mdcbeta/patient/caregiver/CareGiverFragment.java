package com.mdcbeta.patient.caregiver;


import android.os.Bundle;


import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.CareGiver;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.VerticalSpaceItemDecoration;
import com.mdcbeta.widget.ActionBar;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;


public class CareGiverFragment extends BaseFragment implements BaseDialogFragment.DialogClickListener {


    @BindView(R.id.care_giver_list)
    RecyclerView careGiverList;
    @BindView(R.id.add_care_fab)
    FloatingActionButton addCareFab;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    Unbinder unbinder;

    @Inject
    ApiFactory apiFactory;
    private CareGiverAdapter adapter;

    public CareGiverFragment() {
        // Required empty public constructor
    }


    public static CareGiverFragment newInstance() {
        CareGiverFragment fragment = new CareGiverFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public void onStart() {
        super.onStart();

        initList();

        showProgress("Loading");
        apiFactory.getCareGiver(getUser().getId())
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model -> {
                    if(model.status){
                        hideProgress();
                        adapter.swap((List<CareGiver>) model.getData());
                    }else {
                        showError(model.getMessage());
                    }

                },throwable -> showError(throwable.getMessage()));
    }

    private void initList() {
        adapter = new CareGiverAdapter(getContext());
        careGiverList.setAdapter(adapter);
        careGiverList.setHasFixedSize(true);
        careGiverList.setItemAnimator(new DefaultItemAnimator());
        careGiverList.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        careGiverList.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_care_giver;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
    appComponent.inject(this);
    }

    @OnClick(R.id.add_care_fab)
    public void careGiverClick() {
        getFragmentHandlingActivity().addDialog(this,AddCareGiverDailog.newInstance());
    }


    @Override
    public void onYesClick() {
        showProgress("Loading");
        apiFactory.getCareGiver(getUser().getId())
                .compose(RxUtil.applySchedulers())
                .subscribe(model -> {
                    if(model.status) {
                        hideProgress();
                        adapter.swap(model.getData());
                    }else {
                        showError(model.getMessage());
                    }

                },throwable -> showError(throwable.getMessage()));
    }

    @Override
    public void onNoClick() {

    }
}
