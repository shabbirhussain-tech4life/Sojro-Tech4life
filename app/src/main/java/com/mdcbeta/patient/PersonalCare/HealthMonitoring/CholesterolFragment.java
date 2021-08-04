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
import com.mdcbeta.data.remote.model.Cholesterol;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.CholesterolAdapter;
import com.mdcbeta.patient.PersonalCare.DialogBox.CholesterolDialog;
import com.mdcbeta.patient.PersonalCare.DialogBox.CholesterolUpdateDialog;
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

public class CholesterolFragment extends BaseFragment implements BaseDialogFragment.DialogClickListener {


    @BindView(R.id.cholesterolbtn)
    FloatingActionButton cholesterolbtn;

    @BindView(R.id.main_list)
    RecyclerView cholesterol_list;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    private CholesterolAdapter adapter;

    Unbinder unbinder;

    List<Cholesterol> data = new ArrayList<>();
    @Inject
    ApiFactory apiFactory;


    public CholesterolFragment() {
        // Required empty public constructor
    }


    public static CholesterolFragment newInstance() {
        CholesterolFragment fragment = new CholesterolFragment();
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
        apiFactory.getCholesterol(getUser().getId())
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
        adapter = new CholesterolAdapter(getContext());
        cholesterol_list.setAdapter(adapter);
        cholesterol_list.setHasFixedSize(true);
        cholesterol_list.setItemAnimator(new DefaultItemAnimator());
        cholesterol_list.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        cholesterol_list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemClickListner((view, item) -> {
            switch (view.getId()) {
                case R.id.item_main:

                    getFragmentHandlingActivity()
                            .replaceFragmentWithBackstack(CholesterolUpdateDialog
                                    .newInstance(item.getId()));
                    break;
                case R.id.view_exercise:
                    getFragmentHandlingActivity()
                            .replaceFragmentWithBackstack(CholesterolDetailFragment
                                    .newInstance(item.getId()));
                    //     Toast.makeText(getContext(), item.getId(), Toast.LENGTH_LONG).show();
                    break;


                case R.id.btn_delete:
                    showProgress("Deleting record");
                    apiFactory.cholesterolDelete(item.getId())
                            .compose(RxUtil.applySchedulers())
                            .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                            .subscribe(model -> {
                                hideProgress();
                                if (model.status) {
                                    hideProgress();
                                    getFragmentHandlingActivity().replaceFragment(CholesterolFragment.newInstance());
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
        return R.layout.fragment_cholesterol;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @OnClick(R.id.cholesterolbtn)
    public void careGiverClick() {
        getFragmentHandlingActivity().addDialog(this, CholesterolDialog.newInstance());
    }


    @Override
    public void onYesClick() {
        showProgress("Loading");

        apiFactory.getCholesterol(getUser().getId())
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
