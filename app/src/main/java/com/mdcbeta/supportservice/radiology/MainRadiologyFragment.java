package com.mdcbeta.supportservice.radiology;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mdcbeta.Events;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.MDCRepository;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.supportservice.Adapter.RadiologyAdapter;
import com.mdcbeta.util.RxBus;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.VerticalSpaceItemDecoration;
import com.mdcbeta.widget.ActionBar;
import com.trello.rxlifecycle2.android.FragmentEvent;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by MahaRani on 12/11/2018.
 */

public class MainRadiologyFragment extends BaseFragment implements BaseDialogFragment.DialogClickListener {

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

    private RadiologyAdapter adapter;

    String booked_as = "referrer";

    public MainRadiologyFragment() {
        // Required empty public constructor
    }

    public static MainRadiologyFragment newInstance() {
        MainRadiologyFragment fragment = new MainRadiologyFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        rxBus.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(FragmentEvent.STOP))
                .subscribe(o -> {

                    if(o instanceof Events.Success){
                        showSuccess("Case Created");
                        getCasesForReffer();

                    }
                    if(o instanceof Events.Failed){
                        showError(((Events.Failed) o).getMessage());
                    }


                },Throwable::printStackTrace);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

// commented by KANWAL KHAN
      //  showProgress("Loading");

        getCasesForReffer();

    }

    public void getCasesForReffer(){

        apiFactory.radiology_cases(getUser().getId())
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(model->{

                    hideProgress();
                    adapter.swap(model.getData());
                    Log.d("id", getUser().getId());

                }, Throwable::printStackTrace);

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
        adapter = new RadiologyAdapter(getContext());
        mainList.setAdapter(adapter);
        mainList.setHasFixedSize(true);
        mainList.setItemAnimator(new DefaultItemAnimator());
        mainList.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        mainList.setNestedScrollingEnabled(false);
        mainList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setItemClickListner(object -> {

            getFragmentHandlingActivity().addFragmentWithBackstack(MainRecordDetailFragment.newInstance(
                    object.get("id").getAsString(), booked_as));


        });

        adapter.setVideoClickListner(object->{

            getFragmentHandlingActivity().addFragmentWithBackstack(RadiologyImageFragment.newInstance(
                    object.get("id").getAsString()));


        });

        adapter.setImageClickListner(object-> {

            getFragmentHandlingActivity().addFragmentWithBackstack(RadiologyImgeUpload.newInstance(
                    object.get("id").getAsString(),object.get("record_id").getAsString()));


        });

        adapter.setSpinnerClickListner(object-> {

            if (object.get("status").getAsString().equals("Completed"))
            {
                Toast.makeText(getContext(),"Status is completed", Toast.LENGTH_LONG).show();
            }
            else {
                getFragmentHandlingActivity().addDialog(this, AddRadiologySpinnerDialog.newInstance(object.get("id").getAsString()));
            }
        });

    }


    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {

    }
}

