package com.mdcbeta.patient.healthinfo;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.mdcbeta.R;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.widget.ActionBar;

import butterknife.OnClick;

/**
 * Created by Shakil Karim on 4/18/17.
 */

public class HealthInfoFragment extends BaseFragment {


    public static HealthInfoFragment newInstance() {
        HealthInfoFragment fragment = new HealthInfoFragment();
        return fragment;
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_health_info;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {

    }


    @OnClick({R.id.first_aid, R.id.women_health, R.id.sexual, R.id.high_blood, R.id.health, R.id.diabities, R.id.bal_diet, R.id.child_support, R.id.mental})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.first_aid:
                    launch("https://medlineplus.gov/ency/article/002032.htm");
                break;
            case R.id.women_health:
                launch("https://medlineplus.gov/firstaid.html");
                break;
            case R.id.sexual:
                launch("http://www.mayoclinic.org/diseases-conditions/mental-illness/basics/definition/con-20033813");
                break;
            case R.id.high_blood:
                launch("http://www.mayoclinic.org/diseases-conditions/high-blood-pressure/basics/definition/con-20019580");
                break;
            case R.id.health:
                launch("http://www.mayoclinic.org/diseases-conditions/heart-disease/in-depth/heart-disease-prevention/art-20046502");
                break;
            case R.id.diabities:
                launch("http://www.diabetes.co.uk/diabetes_care/blood-sugar-level-ranges.html");
                break;
            case R.id.bal_diet:
                launch("http://www.medicinenet.com/childrens_health/article.htm");
                break;
            case R.id.child_support:
                launch("http://www.mayoclinic.org/healthy-lifestyle/sexual-health/basics/sexual-health-basics/hlv-20049432?reDate=27022017");
                break;
            case R.id.mental:
                launch("https://medlineplus.gov/womenshealth.html");
                break;
        }
    }



    public void launch (String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }




}
