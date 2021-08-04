package com.mdcbeta.healthprovider.group;

import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.data.remote.model.CreateGroup;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.util.RxUtil;
import com.mdcbeta.widget.MembersView;
import com.mdcbeta.widget.SpecialityView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by Shakil Karim on 4/8/17.
 */

public class NewGroupDailog extends BaseDialogFragment {


    @BindView(R.id.item_1)
    LinearLayout item1;
    @BindView(R.id.item_2)
    LinearLayout item2;
    @BindView(R.id.check_private)
    CheckedTextView checkPrivate;
    @BindView(R.id.check_public)
    CheckedTextView checkPublic;
    String groupType = "Public";
    @BindView(R.id.txt_group_name)
    EditText txtGroupName;
    @BindView(R.id.next_btn)
    Button nextbtn;
    @BindView(R.id.skillview)
    SpecialityView specialityView;
    @BindView(R.id.pre_btn)
    Button pre_btn;
    @Inject
    ApiFactory apiFactory;
    String Color;



    private static final String TAG = "NewGroupDailog";
    @BindView(R.id.view_member)
    MembersView viewMember;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    @BindView(R.id.btn_color)
    Button btnColor;

    static boolean mEditMembers;
    static boolean misView;
    static CreateGroup mCreateGroup;
    private int currentColor;


    public static DialogFragment newInstance(boolean isEditMember, boolean isView, CreateGroup createGroup) {
        NewGroupDailog fragment = new NewGroupDailog();
        misView = isView;
        mCreateGroup = createGroup;
        mEditMembers = isEditMember;
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!misView) {
            RxTextView.textChanges(txtGroupName)
                    .skip(1)
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .switchMap(string -> apiFactory.isGroupExists(string.toString()))
                    .compose(RxUtil.applySchedulers())
                    .subscribe(objectResponse -> {

                        Log.d(TAG, "onNext: " + objectResponse);

                        if (objectResponse.status) {
                            txtGroupName.setError(null);
                            // nextbtn.setVisibility(View.VISIBLE);
                        } else {
                            txtGroupName.setError("Group already exists!");
                            //nextbtn.setVisibility(View.GONE);
                        }

                    },throwable -> {throwable.printStackTrace();});
        }


    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }

    @Override
    public int getLayoutID() {
        return R.layout.dailog_create_group;
    }


    @OnClick(R.id.next_btn)
    public void nextClicked() {

        if(TextUtils.isEmpty(txtGroupName.getText().toString())){
            showError("Group name is empty");
            return;
        }

        item1.setVisibility(View.GONE);
        item2.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.pre_btn)
    public void preClicked() {
        item1.setVisibility(View.VISIBLE);
        item2.setVisibility(View.GONE);
    }


    @OnClick({R.id.check_private, R.id.check_public, R.id.btn_color})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_private:
                groupType = "Private";
                checkPrivate.setChecked(true);
                checkPublic.setChecked(false);
                break;
            case R.id.check_public:
                groupType = "Public";
                checkPrivate.setChecked(false);
                checkPublic.setChecked(true);
                break;
            case R.id.btn_color:
                openDialog(false);
                break;

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mEditMembers){
            item1.setVisibility(View.GONE);
            item2.setVisibility(View.VISIBLE);
            pre_btn.setVisibility(View.GONE);

        }


        if(misView){
            txtGroupName.setText(mCreateGroup.getName());

        }
    }

    @OnClick(R.id.btn_finish)
    public void onBtnFinish() {

        showProgress("Creating group");

        CreateGroup createGroup = new CreateGroup();
        createGroup.setName(txtGroupName.getText().toString());
        createGroup.setMembers(viewMember.getMembersId());
        createGroup.setPermission(groupType);
     //   createGroup.setColor(generateColor(new Random()));
        createGroup.setColor(Color);
        createGroup.setUserId(getUser().getId());
        createGroup.setGroup_diseases(specialityView.getSepicallitIds());

        if(misView){
            createGroup.setId(mCreateGroup.getGroup_id());
        }

        if(!misView) {
            apiFactory.createGroup(createGroup)
                    .compose(RxUtil.applySchedulers())
                    .subscribe(objectResponse -> {

                            if (objectResponse.status) {
                                hideProgress();
                                getFragmentHandlingActivity().replaceFragment(GroupFragment.newInstance());
                                dismiss();
                            } else {
                                showError(objectResponse.message);
                            }

                        });
        }else {
            if(!mEditMembers)
            {
                apiFactory.groupEdit(createGroup)
                        .compose(RxUtil.applySchedulers())
                        .subscribe(objectResponse ->{

                                if (objectResponse.status) {
                                    hideProgress();
                                    getFragmentHandlingActivity().replaceFragment(GroupFragment.newInstance());
                                    dismiss();
                                } else {
                                    showError(objectResponse.message);
                                }

                            });
            }else{
                apiFactory.groupEditAddMember(createGroup)
                        .compose(RxUtil.applySchedulers())
                        .subscribe(objectResponse-> {

                                if (objectResponse.status) {
                                    hideProgress();
                                    getFragmentHandlingActivity().replaceFragment(GroupFragment.newInstance());
                                    dismiss();
                                } else {
                                    showError(objectResponse.message);
                                }

                            },Throwable::printStackTrace);

            }


        }



    }

    private void openDialog(boolean supportsAlpha) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(getContext(), currentColor, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                currentColor = color;
                btnColor.setBackgroundColor(color);

                String hex_value = color < 0
                        ? Integer.toHexString(color+65536) :    Integer.toHexString(color);
                Color = "#"+hex_value.substring(2);

            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
        //        Toast.makeText(getApplicationContext(), "Action canceled!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }


    private static String generateCkklilor(Random r) {
        final char [] hex = { '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char [] s = new char[7];
        int     n = r.nextInt(0x1000000);

        s[0] = '#';
        for (int i=1;i<7;i++) {
            s[i] = hex[n & 0xf];
            n >>= 4;
        }
        return new String(s);
    }

    @Override
    public void onValidationSucceeded() {

    }
}
