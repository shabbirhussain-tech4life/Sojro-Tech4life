package com.mdcbeta.healthprovider.cases;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.mdcbeta.Events;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.PastHistory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.MainHealthProviderActivity;
import com.mdcbeta.healthprovider.cases.adapter.MyCasesAdapter;
import com.mdcbeta.util.RxBus;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.VerticalSpaceItemDecoration;
import com.mdcbeta.widget.ActionBar;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MyCasesFragment extends BaseFragment {

    @BindView(R.id.main_list)
    RecyclerView mainList;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;

    @Inject
    RxBus rxBus;
    @Inject
    ApiFactory apiFactory;
    @Inject
    MDCRepository repository;

    @BindView(R.id.search_view)
    SearchView searchView;

    private MyCasesAdapter adapter;

    String booked_as = "referrer";

    public MyCasesFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        MyCasesFragment fragment = new MyCasesFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        rxBus.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(FragmentEvent.STOP))
                .subscribe(o -> {

                    if (o instanceof Events.Success) {
                        showSuccess("Case Created");
                        getCasesForReffer();

                    }
                    if (o instanceof Events.Failed) {
                        showError(((Events.Failed) o).getMessage());
                    }


                }, Throwable::printStackTrace);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (MainHealthProviderActivity.isReviewer) {
            showProgress("Loading");
            booked_as = "reviewer";
            getCaseforReviwers();
        } else {
            showProgress("Loading");
            booked_as = "referrer";
            getCasesForReffer();
        }
    }

    public void getCasesForReffer() {

        apiFactory.mycases(getUser().getId())
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model -> {

                    hideProgress();
                    adapter.swap(model.getData());

                }, Throwable::printStackTrace);

    }

    List<JsonObject> data = new ArrayList<>();

    public void getCaseforReviwers() {

        apiFactory.ReviewerCasesUser(getUser().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model -> {

                    data.addAll(model.getData());

                    apiFactory.ReviewerCasesGroups(getUser().getId())
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

                });

    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_my_cases;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.setQuery((newText != null) ? newText : null);
                return true;
            }
        });
    }

    private void initList() {
        adapter = new MyCasesAdapter(getContext());
        mainList.setAdapter(adapter);
        mainList.setHasFixedSize(true);
        mainList.setItemAnimator(new DefaultItemAnimator());
        mainList.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        mainList.setNestedScrollingEnabled(false);

        mainList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemClickListner(object -> {

          //For Past History











         //   Toast.makeText(getContext(), object.get("id").getAsString(), Toast.LENGTH_SHORT).show();

         //   Toast.makeText(getContext(), object.get("case_code").getAsString(), Toast.LENGTH_SHORT).show();

            if (!object.get("id").isJsonNull() && !object.get("case_code").isJsonNull())


            {
            //  Toast.makeText(getContext(),object.get("id").getAsString(),Toast.LENGTH_SHORT).show();
String caseid = object.get("id").getAsString();

              PastHistory pastHistory = new PastHistory
                (caseid);



              apiFactory.PastHistory(pastHistory)
                .compose(RxUtil.applySchedulers())
                .subscribe(model-> {

                  if(model.status){
                    //adapter.notifyDataSetChanged();
                   // showSuccess(model.getMessage());


                    Log.e("Updated","updated");





                  }
                  else {
                    showError(model.getMessage());
                  }

                },throwable -> {
                  hideProgress();
                  throwable.printStackTrace();
                });






              getFragmentHandlingActivity().addFragmentWithBackstack(CaseDetailFragment.newInstance(
                        object.get("id").getAsString(), object.get("case_code").getAsString()));}
            else
                showError("Record data missing!");

        });

    }


}
