package com.mdcbeta.patient.PersonalCare.HealthMonitoring;

import android.os.Bundle;


import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.BloodPressure;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.BloodpressureAdapter;
import com.mdcbeta.patient.PersonalCare.BpDetailFragment;
import com.mdcbeta.patient.PersonalCare.DialogBox.BloodPressureDialog;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.VerticalSpaceItemDecoration;
import com.mdcbeta.widget.ActionBar;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by MahaRani on 04/04/2018.
 */

public class BloodPressureFragment extends BaseFragment implements BaseDialogFragment.DialogClickListener {


    @BindView(R.id.bloodpressurebtn)
    FloatingActionButton bloodpressurebtn;

    @BindView(R.id.main_list)
    RecyclerView bp_list;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    private BloodpressureAdapter adapter;

    Unbinder unbinder;

    List<BloodPressure> data = new ArrayList<>();
    @Inject
    ApiFactory apiFactory;


    public BloodPressureFragment() {
        // Required empty public constructor
    }


    public static BloodPressureFragment newInstance() {
        BloodPressureFragment fragment = new BloodPressureFragment();
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
        apiFactory.getBloodPressure(getUser().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model2 ->  {

                    data.addAll(model2.getData());
                    adapter.swap(data);
                    hideProgress();

                } , t -> {
                    t.printStackTrace();
                    adapter.swap(data);

                });
    }

    private void initList() {
        adapter = new BloodpressureAdapter(getContext());
        bp_list.setAdapter(adapter);
        bp_list.setHasFixedSize(true);
        bp_list.setItemAnimator(new DefaultItemAnimator());
        bp_list.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        bp_list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemClickListner((view, item) -> {
            switch (view.getId()) {
                case R.id.item_main:
                    break;
                case R.id.view_exercise:
                    getFragmentHandlingActivity()
                            .replaceFragmentWithBackstack(BpDetailFragment.newInstance(item.getId()));
                    //     Toast.makeText(getContext(), item.getId(), Toast.LENGTH_LONG).show();
                    break;


                case R.id.btn_delete:
                    showProgress("Deleting record");
                    apiFactory.bpDelete(item.getId())
                            .compose(RxUtil.applySchedulers())
                            .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                            .subscribe(model -> {
                                hideProgress();
                                if (model.status) {
                                    hideProgress();
                                    getFragmentHandlingActivity().replaceFragment(BloodPressureFragment.newInstance());
                                } else {
                                    showError(model.message);
                                }

                            },Throwable::printStackTrace);
                    break;
                case R.id.btn_edit:

                    break;

            }

        });
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_bloodpressure;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @OnClick(R.id.bloodpressurebtn)
    public void careGiverClick() {
        getFragmentHandlingActivity().addDialog(this, BloodPressureDialog.newInstance());
    }


    @Override
    public void onYesClick() {
        showProgress("Loading");

        apiFactory.getBloodPressure(getUser().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model2 ->  {

                    data.addAll(model2.getData());
                    adapter.swap(data);
                    hideProgress();

                } , t -> {
                    t.printStackTrace();
                    adapter.swap(data);

                });
    }




    @Override
    public void onNoClick() {

    }
}
