 package com.mdcbeta.healthprovider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.view.Gravity;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseActivity;
import com.mdcbeta.healthprovider.appointment.AppointmentFragment;
import com.mdcbeta.healthprovider.patientmonitor.PatientMonitorFragment;
import com.mdcbeta.healthprovider.settings.InviteMemberActivity;
import com.mdcbeta.setting.SettingActivity;
import com.mdcbeta.app.MyApplication;
import com.mdcbeta.authenticate.LoginActivity;
import com.mdcbeta.base.FragmentsHandlerActivity;
import com.mdcbeta.data.model.User;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.appointment.HealthProviderAppointmentFragment;
import com.mdcbeta.healthprovider.cases.CreateCaseFragment;
import com.mdcbeta.healthprovider.cases.MyCasesFragment;
import com.mdcbeta.healthprovider.group.GroupFragment;
import com.mdcbeta.healthprovider.group.NewGroupDailog;
import com.mdcbeta.healthprovider.schedule.ScheduleFragment;
import com.mdcbeta.healthprovider.settings.AddLocationDialog;
import com.mdcbeta.healthprovider.settings.AddSpecialitiesDialog;
import com.mdcbeta.healthprovider.settings.InviteMemberDailog;
import com.mdcbeta.patient.DrawerItem;
import com.mdcbeta.util.AppPref;
import com.mdcbeta.widget.ActionBar;
import com.mdcbeta.widget.VectorDrawableTextView;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainHealthProviderActivity extends FragmentsHandlerActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.darwerList)
    ListView darwerList;
    @BindView(R.id.toolbar)
    ActionBar toolbar;
    @BindView(R.id.txt_role)

    VectorDrawableTextView txtRole;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.drawer_pic)
    CircleImageView drawerPic;

    private Unbinder unbinder;
    private HealthProviderDrawerAdapter mAdp;
    private static final String TAG = "MainHealthProviderActiv";

    public static boolean isReviewer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_health_provider);
        unbinder = ButterKnife.bind(this);

        ((MyApplication) getApplication()).getAppComponent().inject(this);

        setUpDrawer();
        setActionBarListener();
        setNavClickListener();
        replaceFragment(HealthProviderAppointmentFragment.newInstance());
    }

    @Override
    public void performInjection(AppComponent appComponent) {

    }

    public static void start(Context context) {
        Intent starter = new Intent(context, MainHealthProviderActivity.class);
        context.startActivity(starter);
    }


    private void setNavClickListener() {
        darwerList.setOnItemClickListener((parent, view, position, id) -> {
             //   drawerLayout.setFitsSystemWindows(true);

            drawerLayout.closeDrawer(Gravity.LEFT, true);

            DrawerItem drawerItem = mAdp.getItem(position);


            switch (drawerItem.name) {
                case "My cases":
                    replaceFragment(MyCasesFragment.newInstance());
                    // replaceFragment(OfflineCasesFragment.newInstance());
                    break;
                case "Schedule":
                    replaceFragment(ScheduleFragment.newInstance());
                    break;
                case "Patient monitor":
                    replaceFragment(PatientMonitorFragment.newInstance());
                    break;
                case "Appointments":
                case "My appointments":

                    replaceFragment(HealthProviderAppointmentFragment.newInstance());
                    break;
//                case "Add location":
//                    addDialog(AddLocationDialog.newInstance());
//                    break;
//                case "Add speciality":
//                    addDialog(AddSpecialitiesDialog.newInstance());
//                    break;
//                case "Invite members":
//                    String str = getUser().licenses_id;
//                    int val = Integer.parseInt(str);
//                    int num=27;
//                    int tr=27;
//                    if(num==val)
//                    {
//                        showError("This feature is  not available for demo accounts");
//                    }
//                    else {
//
//                  //  addDialog(InviteMemberDailog.newInstance());
//                        String adm = getUser().is_admin;
//
//                        int admin = Integer.parseInt(adm);
//                        int adVal = 1;
//                        if(adVal==admin){
//                            //addDialog(InviteMemberDailog.newInstance());
//                            openDialog();}
//                        else {
//                            showError("This feature is  only available for admin");
//                        }
//                    }
//                    break;
//                case "Create a group":
//                    String str1 = getUser().licenses_id;
//                    int val1 = Integer.parseInt(str1);
//                    int num1=27;
//
//                    if(num1==val1)
//                    {
//                        showError("This feature is  not available for demo accounts");
//                    }
//                    else {
//                    addDialog(NewGroupDailog.newInstance(false,false,null)); }
//                    break;
//                case "My groups":
//                    replaceFragment(GroupFragment.newInstance());
//                    break;
                case "Profile":
                    // addDialog(InviteMemberDailog.newInstance());
//                    if (ContextCompat.checkSelfPermission(MainHealthProviderActivity.this, Manifest.permission.WRITE_CALENDAR)
//                            != PackageManager.PERMISSION_GRANTED) {
//                        // Permission is not granted
//                    }
//                    Context context = getApplicationContext();
////                    CharSequence text = "Hello toast!";
////                    int duration = Toast.LENGTH_LONG;
////
////                    Toast toast = Toast.makeText(context, text, duration);
////                    toast.show();

                    SettingActivity.start(this);

                    break;
//                case "Create a case":
//                    replaceFragment(CreateCaseFragment.newInstance());
//                    break;

              case  "Book a new appointment":
       addFragmentWithBackstack(AppointmentFragment.newInstance( ));
                break;
            }


        });

    }

    private void openDialog() {
        Intent intent = new Intent(this, InviteMemberActivity.class);
        startActivity(intent);
    }


    private void setUpDrawer() {

        User user = AppPref.getInstance(this).getUser();
        setProfilePic(drawerPic,user.getImage());

        username.setText(getUser().getName());
        mAdp = new HealthProviderDrawerAdapter(this, getRefererDrawerList());

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

    public ArrayList<DrawerItem> getReviewerDrawerList() {
        ArrayList<DrawerItem> list = new ArrayList<>();

        list.add(new DrawerItem("My cases", R.drawable.ic_c_view));
      //  list.add(new DrawerItem("Patient monitor", R.drawable.my_appointments));
        list.add(new DrawerItem("Schedule", R.drawable.ic_schedule_a));
        list.add(new DrawerItem("Appointments", R.drawable.ic_appoint));
        list.add(new DrawerItem("Add location", R.drawable.ic_location));
        list.add(new DrawerItem("Add speciality", R.drawable.ic_special));
        list.add(new DrawerItem("Invite members", R.drawable.ic_invite));
        list.add(new DrawerItem("Create a group", R.drawable.ic_group));
        list.add(new DrawerItem("My groups", R.drawable.ic_mgroup));
        list.add(new DrawerItem("Profile", R.drawable.ic_profile));
        return list;
    }


    public ArrayList<DrawerItem> getRefererDrawerList() {
        ArrayList<DrawerItem> list = new ArrayList<>();
        list.add(new DrawerItem("Book a new appointment", R.drawable.ic_c_case));
        list.add(new DrawerItem("My appointments", R.drawable.ic_appoint));
        list.add(new DrawerItem("My cases", R.drawable.ic_c_view));
       // list.add(new DrawerItem("Add location", R.drawable.ic_location));
      //  list.add(new DrawerItem("Add speciality", R.drawable.ic_special));
      //  list.add(new DrawerItem("Invite members", R.drawable.ic_invite));
      //  list.add(new DrawerItem("Create a group", R.drawable.ic_group));
       // list.add(new DrawerItem("My groups", R.drawable.ic_mgroup));
        list.add(new DrawerItem("Profile", R.drawable.ic_profile));
        return list;
    }


    @Override
    protected int fragmentContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public void onBackPressed() {
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
        super.onBackPressed();
    }

    public ActionBar provideActionBar() {
        return toolbar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.switcher)
    public void switerClick(View view) {

        if (((CheckedTextView) view).isChecked()) {
            ((CheckedTextView) view).setChecked(false);
            ((CheckedTextView) view).setText("Switch to reviewer");
            mAdp.updateList(getRefererDrawerList());
            txtRole.setText("Referrer");

            isReviewer = false;
            drawerLayout.closeDrawer(Gravity.LEFT, true);
            replaceFragment(HealthProviderAppointmentFragment.newInstance());


        } else {

            ((CheckedTextView) view).setChecked(true);
            ((CheckedTextView) view).setText("Switch to referrer");
          ((CheckedTextView) view).setTextColor(this.getResources().getColor(R.color.white));
            mAdp.updateList(getReviewerDrawerList());
            txtRole.setText("Reviewer");
            isReviewer = true;
            drawerLayout.closeDrawer(Gravity.LEFT, true);
            replaceFragment(HealthProviderAppointmentFragment.newInstance());


        }




    }

    @OnClick(R.id.setting)
    public void setting() {
        drawerLayout.closeDrawer(Gravity.LEFT, true);
        SettingActivity.start(this);

    }

    @OnClick(R.id.signout)
    public void signOut() {
        AppPref.getInstance(this).clearAll();
        AppPref.getInstance(this).remove(AppPref.Key.USER_LOGIN, AppPref.Key.IS_PATIENT_LOGIN,AppPref.Key.KEY_RECORD_ID);
        MainHealthProviderActivity.isReviewer = false;
        BaseActivity.user = null;
        LoginActivity.start(this);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//    }


//  @Override
//  public void onTrimMemory(int level) {
//    super.onTrimMemory(level);
//
//  }

//  @Override
//  public void trimCache() {
//    super.trimCache();
//    Toast.makeText(getApplicationContext(),"trim memory",Toast.LENGTH_SHORT).show();
//  }



}
