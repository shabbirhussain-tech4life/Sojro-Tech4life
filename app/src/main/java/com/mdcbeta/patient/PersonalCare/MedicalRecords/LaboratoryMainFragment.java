package com.mdcbeta.patient.PersonalCare.MedicalRecords;

import android.os.Bundle;


import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.Laboratory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.patient.PersonalCare.DialogBox.LaboratoryDialog;
import com.mdcbeta.patient.PersonalCare.LaboratoryAdapter;
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
 * Created by MahaRani on 28/06/2018.
 */

public class LaboratoryMainFragment extends BaseFragment implements BaseDialogFragment.DialogClickListener {


    @BindView(R.id.main_list)
    RecyclerView exercise_list;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    private LaboratoryAdapter adapter;

    @BindView(R.id.exercisebtn)
    FloatingActionButton exercisebtn;

    Unbinder unbinder;

    List<Laboratory> data = new ArrayList<>();
    @Inject
    ApiFactory apiFactory;


    public LaboratoryMainFragment() {
        // Required empty public constructor
    }


    public static LaboratoryMainFragment newInstance() {
        LaboratoryMainFragment fragment = new LaboratoryMainFragment();
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

        apiFactory.getLaboratories(getUser().getId())
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
        adapter = new LaboratoryAdapter(getContext());
        exercise_list.setAdapter(adapter);
        exercise_list.setHasFixedSize(true);
        exercise_list.setItemAnimator(new DefaultItemAnimator());
        exercise_list.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        exercise_list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemClickListner((view, item) -> {
            switch (view.getId()){
                case R.id.item_main:
                    break;
                case R.id.view_exercise:
                    getFragmentHandlingActivity().replaceFragmentWithBackstack(LaboratoryDetailFragment.newInstance(item.getid()));
                    break;

                case R.id.btn_delete:
                    showProgress("Deleting record");
                    apiFactory.laboratoriesDelete(item.getid())
                            .compose(RxUtil.applySchedulers())
                            .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                            .subscribe(model -> {
                                hideProgress();
                                if (model.status) {
                                    hideProgress();
                                    getFragmentHandlingActivity().replaceFragment(LaboratoryMainFragment.newInstance());
                                } else {
                                    showError(model.message);
                                }

                            },Throwable::printStackTrace);
                    break;
            }

        });
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_exercise;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @OnClick(R.id.exercisebtn)
    public void careGiverClick() {
        getFragmentHandlingActivity().addDialog(this, LaboratoryDialog.newInstance());
    }


    @Override
    public void onYesClick() {
        showProgress("Loading");
        apiFactory.getLaboratories(getUser().getId())
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


    @Override
    public void onNoClick() {

    }
}
