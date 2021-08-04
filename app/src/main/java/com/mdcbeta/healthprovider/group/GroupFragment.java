package com.mdcbeta.healthprovider.group;

import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.util.VerticalSpaceItemDecoration;
import com.mdcbeta.widget.ActionBar;
import com.trello.rxlifecycle2.android.FragmentEvent;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;

/**
 * Created by Shakil Karim on 4/8/17.
 */

public class GroupFragment extends BaseFragment {

    @BindView(R.id.main_list)
    RecyclerView mainList;
    @BindDimen(R.dimen.item_space_small)
    int listviewSpace;
    private GroupAdapter adapter;

    @Inject
    ApiFactory apiFactory;

    public static Fragment newInstance() {
        GroupFragment fragment = new GroupFragment();
        return fragment;
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_group;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initList();
        showProgress("Loading");
        apiFactory.groups(getUser().getId())
                .compose(RxUtil.applySchedulers())
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(createGroupResponse -> {
                        if(createGroupResponse.status){
                            hideProgress();
                            adapter.swap(createGroupResponse.data);

                        }
                        else {
                            showError(createGroupResponse.message);
                        }
                    },Throwable::printStackTrace);
    }

    private void initList() {
        adapter = new GroupAdapter(getActivity());
        mainList.setAdapter(adapter);
        mainList.setHasFixedSize(true);
        mainList.setItemAnimator(new DefaultItemAnimator());
        mainList.addItemDecoration(new VerticalSpaceItemDecoration(listviewSpace));
        mainList.setNestedScrollingEnabled(false);
        mainList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter.setItemClickListner((view, item) -> {
            switch (view.getId()){
                case R.id.item_main:
                    break;
                case R.id.view_group:
                        getFragmentHandlingActivity()
                                .replaceFragmentWithBackstack(GroupDetailFragment.newInstance(item.getGroup_id()));
                  //  Toast.makeText(getContext(), item.getGroup_id(), Toast.LENGTH_LONG).show();
                    break;
                case R.id.edit_group:
                    getFragmentHandlingActivity().addDialog(NewGroupDailog.newInstance(false,true,item));
                    break;
                case R.id.add_member:
                    getFragmentHandlingActivity().addDialog(NewGroupDailog.newInstance(true,true,item));
                    break;
                case R.id.btn_delete:
                    showProgress("Deleting group");
                    apiFactory.groupDelete(item.getGroup_id())
                            .compose(RxUtil.applySchedulers())
                            .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                            .subscribe(model -> {
                                hideProgress();
                                if (model.status) {
                                    hideProgress();
                                   // getFragmentHandlingActivity().replaceFragment(GroupFragment.newInstance());
                                } else {
                                    showError(model.message);
                                }

                            },Throwable::printStackTrace);
                    break;
            }

        });

//        adapter.setItemDelete(pos -> {
//
//
//
//
////            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
////                    .setTitleText("Are you sure?")
////                    .setContentText("Won't be able to recover this group!")
////                    .setConfirmText("Yes,delete it!")
////                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
////                        @Override
////                        public void onClick(SweetAlertDialog sDialog) {
////                            sDialog
////                                    .setTitleText("Deleted!")
////                                    .setContentText("Your group has been deleted!")
////                                    .setConfirmText("OK")
////                                    .setConfirmClickListener(null)
////                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
////
////                            adapter.mlist.remove(pos);
////                            adapter.notifyDataSetChanged();
////                        }
////                    })
////                    .show();
//
//
//        });

    }
}
