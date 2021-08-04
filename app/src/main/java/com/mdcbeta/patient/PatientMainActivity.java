package com.mdcbeta.patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.Gravity;
import android.widget.ListView;
import android.widget.TextView;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseActivity;
import com.mdcbeta.patient.PersonalCare.PersonalCareFragment;
import com.mdcbeta.setting.SettingActivity;
import com.mdcbeta.authenticate.LoginActivity;
import com.mdcbeta.base.FragmentsHandlerActivity;
import com.mdcbeta.data.model.User;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.MainHealthProviderActivity;
import com.mdcbeta.patient.appointment.PatientAppointmentFragment;
import com.mdcbeta.patient.caregiver.CareGiverFragment;
import com.mdcbeta.patient.dashboard.PatientMainFragment;
import com.mdcbeta.patient.healthinfo.HealthInfoFragment;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.widget.ActionBar;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PatientMainActivity extends FragmentsHandlerActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.darwerList)
    ListView darwerList;
    @BindView(R.id.toolbar)
    ActionBar toolbar;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.drawer_pic)
    CircleImageView drawerPic;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);
        unbinder = ButterKnife.bind(this);
        setUpDrawer();
        setActionBarListener();
        setNavClickListener();
        replaceFragment(PatientMainFragment.newInstance());


    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PatientMainActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void performInjection(AppComponent appComponent) {

    }

    private void setNavClickListener() {
        darwerList.setOnItemClickListener((parent, view, position, id) -> {
            drawerLayout.closeDrawer(Gravity.LEFT, true);

            switch (position) {
                case 0:
                    replaceFragment(PatientMainFragment.newInstance());
                    break;
                case 1:
                    replaceFragment(PatientAppointmentFragment.newInstance());
                    break;
                case 2:

                    replaceFragment(PersonalCareFragment.newInstance());
                 //   replaceFragment(LifeStyleFragment.newInstance());
                    break;
                case 3:
                    replaceFragment(CareGiverFragment.newInstance());
                    break;
                case 4:
                    replaceFragment(HealthInfoFragment.newInstance());
                    break;
            }


        });

    }

    private void setUpDrawer() {

        User user = AppPref.getInstance(this).getUser();
        setProfilePic(drawerPic,user.getImage());
        username.setText(getUser().getUsername());
        PatientDrawerAdapter mAdp = new PatientDrawerAdapter(this, getDrawerList());
        darwerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        darwerList.setAdapter(mAdp);
        darwerList.setItemChecked(0, true);
    }


    private void setActionBarListener() {

        if (getUser().getImage() != null) {
            setProfilePic(toolbar.profileImage, getUser().getImage());
        }

        toolbar.setOnItemsClickListner(id -> {
            switch (id) {
                case R.id.menu:
                    drawerLayout.openDrawer(Gravity.LEFT);
                    break;
            }
        });


    }

    public ArrayList<DrawerItem> getDrawerList() {
        ArrayList<DrawerItem> list = new ArrayList<>();
        list.add(new DrawerItem("My Consultations", R.drawable.mycons));
        list.add(new DrawerItem("Book an Appointment", R.drawable.bookapoin));
        list.add(new DrawerItem("Personal Care", R.drawable.bookapoin));
        list.add(new DrawerItem("Caregivers", R.drawable.caregiver));
        list.add(new DrawerItem("Health Information", R.drawable.healthinfo));
        return list;
    }


    @Override
    protected int fragmentContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public ActionBar provideActionBar() {
        return toolbar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.signout)
    public void signOut() {
        AppPref.getInstance(this).clearAll();
        AppPref.getInstance(this).remove(AppPref.Key.USER_LOGIN, AppPref.Key.IS_PATIENT_LOGIN,AppPref.Key.KEY_RECORD_ID);
        MainHealthProviderActivity.isReviewer = true;
        BaseActivity.user = null;
        LoginActivity.start(this);
        finish();
    }

    @OnClick(R.id.setting)
    public void setting() {
        drawerLayout.closeDrawer(Gravity.LEFT, true);
        SettingActivity.start(this);
    }


}
