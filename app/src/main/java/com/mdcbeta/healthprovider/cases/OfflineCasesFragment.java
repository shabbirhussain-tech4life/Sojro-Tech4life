package com.mdcbeta.healthprovider.cases;


import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.DataModel;
import com.mdcbeta.healthprovider.cases.adapter.MyRecyclerViewAdapter;
import com.mdcbeta.healthprovider.cases.adapter.RecyclerTouchListener;
import com.mdcbeta.util.DbHelper;
import com.mdcbeta.util.RxBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;
import com.mdcbeta.Events;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.MainHealthProviderActivity;
import com.mdcbeta.healthprovider.cases.adapter.ImagePreviewAdapter;
import com.mdcbeta.healthprovider.cases.adapter.MyCasesAdapter;

import com.mdcbeta.util.AppPref;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.VerticalSpaceItemDecoration;
import com.mdcbeta.widget.ActionBar;
import com.mobsandgeeks.saripaar.Validator;
import com.trello.rxlifecycle2.android.FragmentEvent;

import static com.mdcbeta.util.DbHelper.TABLE1;

/**
 * Created by MahaRani on 05/12/2017.
 */

public class OfflineCasesFragment extends BaseFragment {
@BindView(R.id.main_list)
RecyclerView mainList;
@BindDimen(R.dimen.item_space_small)
int listviewSpace;

@Inject
RxBus rxBus;
@Inject
ApiFactory apiFactory;

@BindView(R.id.search_view)
SearchView searchView;
    int genderPosition;
            List<Integer> groups = new ArrayList<>();
        List<Integer> users = new ArrayList<>();
private Validator validator;
private ImagePreviewAdapter adapter1;

private MyCasesAdapter adapter;
private MyRecyclerViewAdapter mAdapter;
        List<DataModel> dataOffline = new ArrayList<>();
        DbHelper databaseHelper;
        DataModel dataModel = null;
    String id;
        String patientid;
        String refererid;
        String fname;
        String lname;
        String gender;
        String month;
        String year;
        String history;
        String comments;
        String complains;
        String location_id;
        String ageinyear;
        String ageinmonth;
        String patient_id;
        String comments_id;
        String history_id;
        String complain_id;
        String gender_id;

        String firstname;
        String lastname;
        String location;
        String origindate;
        String complain;


public OfflineCasesFragment() {
        // Required empty public constructor
        }

public static OfflineCasesFragment newInstance() {
        OfflineCasesFragment fragment = new OfflineCasesFragment();
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

        });
        }

@Override
public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if ((info == null || !info.isConnected() || !info.isAvailable())) {

        initListOffline();

        String abc = AppPref.getInstance(getContext()).getString(AppPref.Key.DATA);

        } else {


        if(MainHealthProviderActivity.isReviewer){
        showProgress("Loading");
        getCaseforReviwers();
        }else {
        getCasesForReffer();
        }

        //  onValidationSucceeded();
        //   Toast.makeText(getContext(), "Internet connected...", Toast.LENGTH_SHORT).show();
        }

        }

public void getCasesForReffer(){

        apiFactory.mycases(getUser().getId())
        .compose(RxUtil.applySchedulers())
        .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(model->{

        hideProgress();
        adapter.swap(model.getData());

        }, Throwable::printStackTrace);

        }

        List<JsonObject> data = new ArrayList<>();

public void getCaseforReviwers(){

        apiFactory.ReviewerCasesUser(getUser().getId())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(model ->  {

        data.addAll(model.getData());

        apiFactory.ReviewerCasesGroups(getUser().getId())
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
       /*    adapter.setItemClickListner(object -> {

     getFragmentHandlingActivity().addFragmentWithBackstack(CaseDetailFragment.newInstance(
        object.get("record_id").getAsString()));

        });*/
        }


private void initListOffline()  {

        databaseHelper = new DbHelper(getContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = null;

        cursor = db.rawQuery("select * from " + TABLE1 + " order by created_at DESC;", null);
        StringBuffer stringBuffer = new StringBuffer();

        while (cursor.moveToNext()) {
        dataModel = new DataModel();
        id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
        patientid = cursor.getString(cursor.getColumnIndexOrThrow("case_code"));
        refererid = cursor.getString(cursor.getColumnIndexOrThrow("referer_id"));
        firstname = cursor.getString(cursor.getColumnIndexOrThrow("fname"));
        lastname = cursor.getString(cursor.getColumnIndexOrThrow("lname"));
        location = cursor.getString(cursor.getColumnIndexOrThrow("location_id"));
        origindate = cursor.getString(cursor.getColumnIndexOrThrow("created_at"));
        year = cursor.getString(cursor.getColumnIndexOrThrow("y_age"));
        month = cursor.getString(cursor.getColumnIndexOrThrow("month_age"));
        history = cursor.getString(cursor.getColumnIndexOrThrow("history"));
        comments = cursor.getString(cursor.getColumnIndexOrThrow("data"));
        complain = cursor.getString(cursor.getColumnIndexOrThrow("question"));
        dataModel.setId(id);
        dataModel.setPatientid(patientid);
        dataModel.setReferrer(refererid);
        dataModel.setFirstname(firstname);
        dataModel.setLastname(lastname);
        dataModel.setLocation(location);
        dataModel.setOrigindate(origindate);
        stringBuffer.append(dataModel);
        dataOffline.add(dataModel);

        for (DataModel mo : dataOffline) {
        Log.i("Hellomo", "" + mo.getCity());

        //   Toast.makeText(getContext(), year + ",  " +month, Toast.LENGTH_LONG).show();
        }

        mAdapter = new MyRecyclerViewAdapter(dataOffline);
        mainList.setAdapter(mAdapter);
        mainList.setHasFixedSize(true);
        mainList.setItemAnimator(new DefaultItemAnimator());
        //  mainList.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        mainList.setNestedScrollingEnabled(false);
        mainList.setLayoutManager(new LinearLayoutManager(getContext()));


        }

//        mainList.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mainList,
//          new RecyclerTouchListener.ClickListener() {
//@Override
//public void onClick(View view, int position) {
//        DataModel dataModel = dataOffline.get(position);
//        //  Toast.makeText(getContext(), dataModel.getId().toString(), Toast.LENGTH_LONG).show();
//      //  getFragmentHandlingActivity().addFragmentWithBackstack(CaseDetailFragment.newInstance(dataModel.getId()));
//
//        }
//
//@Override
//public void onLongClick(View view, int position) {
//
//        }
//        }));

        }

public void onValidationSucceeded() {

        String case_created = AppPref.getInstance(getContext()).getString(AppPref.Key.CASE_CREATE);

        String[] arrayString = case_created.split("::");
        refererid = arrayString[0];
        patient_id = arrayString[1];
        fname = arrayString[2];
        lname = arrayString[3];
        ageinyear = arrayString[4];
        ageinmonth = arrayString[5];
        history_id = arrayString[6];
        comments_id = arrayString[7];
        complain_id = arrayString[8];
        gender_id = arrayString[9];
        location_id = arrayString[10];

        users.add(Integer.parseInt(refererid));

        //  Toast.makeText(getContext(), refererid, Toast.LENGTH_LONG).show();
   /*     CaseCreated caseCreated = new CaseCreated(patient_id,
        fname, lname, gender_id, ageinyear, ageinmonth,
        "1", location_id, history_id,"",
        "1", "1", "0", "12", complain_id,
        comments_id, groups, users,
        Utils.localToUTCDateTime(Utils.getDatetime()));

        showProgress("Creating case..");

        apiFactory.createCase(caseCreated)
        .flatMap(response -> {

        String casecartedID = response.getData();

        List<ChosenImage> images_path = adapter1.getFiles();

        List<MultipartBody.Part> parts = new ArrayList<>();

        if(images_path != null && !images_path.isEmpty()) {
        for (int i = 0; i < images_path.size(); i++) {
        File file = new File(images_path.get(i).getThumbnailPath());
        if (file.exists()) {
        final MediaType MEDIA_TYPE = MediaType.parse(images_path.get(i).getMimeType());
        parts.add(MultipartBody.Part.createFormData("my_images[]",
        file.getName(), RequestBody.create(MEDIA_TYPE, file)));

        } else {
        // Log.d(TAG, "file not exist " + images_path.get(i).getOriginalPath());
        }

        }
        }

        return apiFactory.addCaseAttachments(casecartedID,parts);

        })
        .compose(RxUtil.applySchedulers())
        .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe(model-> {

        if(model.status){
        showSuccess(model.getMessage());
        //   getFragmentHandlingActivity().replaceFragment(OfflineCasasFragment.newInstance());
        }
        else {
        showError(model.getMessage());
        }

        },throwable -> {
        hideProgress();
        throwable.printStackTrace();
        });
*/
        }


        }
