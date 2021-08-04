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
import com.mdcbeta.data.remote.model.Glucose;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.DialogBox.GlucoseDialog;
import com.mdcbeta.patient.PersonalCare.GlucoseAdapter;
import com.mdcbeta.patient.PersonalCare.GlucoseDetailFragment;
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
 * Created by MahaRani on 07/04/2018.
 */

public class GlucoseFragment extends BaseFragment implements BaseDialogFragment.DialogClickListener {


    @BindView(R.id.glucosebtn)
    FloatingActionButton glucosebtn;

    @BindView(R.id.main_list)
    RecyclerView glucose_list;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    private GlucoseAdapter adapter;

    Unbinder unbinder;

    List<Glucose> data = new ArrayList<>();
    @Inject
    ApiFactory apiFactory;


    public GlucoseFragment() {
        // Required empty public constructor
    }


    public static GlucoseFragment newInstance() {
        GlucoseFragment fragment = new GlucoseFragment();
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

        showProgress("Loading");
        apiFactory.getGlucose(getUser().getId())
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
        adapter = new GlucoseAdapter(getContext());
        glucose_list.setAdapter(adapter);
        glucose_list.setHasFixedSize(true);
        glucose_list.setItemAnimator(new DefaultItemAnimator());
        glucose_list.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        glucose_list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemClickListner((view, item) -> {
            switch (view.getId()) {
                case R.id.item_main:
                    break;
                case R.id.view_exercise:
                    getFragmentHandlingActivity()
                            .replaceFragmentWithBackstack(GlucoseDetailFragment
                                    .newInstance(item.getId()));
                    //     Toast.makeText(getContext(), item.getId(), Toast.LENGTH_LONG).show();
                    break;


                case R.id.btn_delete:
                    showProgress("Deleting record");
                    apiFactory.glucoseDelete(item.getId())
                            .compose(RxUtil.applySchedulers())
                            .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                            .subscribe(model -> {
                                hideProgress();
                                if (model.status) {
                                    hideProgress();
                                    getFragmentHandlingActivity().replaceFragment(GlucoseFragment.newInstance());
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
        return R.layout.fragment_glucose;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @OnClick(R.id.glucosebtn)
    public void careGiverClick() {
        getFragmentHandlingActivity().addDialog(this, GlucoseDialog.newInstance());
    }


    @Override
    public void onYesClick() {
        showProgress("Loading");

        apiFactory.getGlucose(getUser().getId())
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
