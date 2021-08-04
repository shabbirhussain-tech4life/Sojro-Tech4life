package com.mdcbeta.healthprovider.cases.dialog;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;

import com.mdcbeta.R;

import com.mdcbeta.base.FragmentsHandlerActivity;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.healthprovider.cases.Appointment1DetailFragment;

public class CaseDetailsDialog extends FragmentsHandlerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenHeight = (int) (metrics.heightPixels * 0.80);

        setContentView(R.layout.activity_case_details_dialog);

        setTitle(getResources().getString(R.string.title_case_Details));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight);

//        addFragmentWithBackstack(Appointment1DetailFragment.
//                newInstance(CallFragment.recordId, CallFragment.caseCode));
    }

    @Override
    public void performInjection(AppComponent appComponent) {

    }

    @Override
    protected int fragmentContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
