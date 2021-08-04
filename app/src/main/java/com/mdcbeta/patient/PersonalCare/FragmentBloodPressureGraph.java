package com.mdcbeta.patient.PersonalCare;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mdcbeta.R;
import com.mdcbeta.base.BaseDialogFragment;
import com.mdcbeta.base.BaseFragment;
import com.mdcbeta.data.remote.ApiFactory;
import com.mdcbeta.di.AppComponent;
import com.mdcbeta.widget.ActionBar;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by MahaRani on 06/02/2018.
 */

public class FragmentBloodPressureGraph extends BaseFragment implements BaseDialogFragment.DialogClickListener {

    @BindView(R.id.chart)
    BarChart chart;

    @Inject
    ApiFactory apiFactory;

  //  ArrayList<BarEntry> systolic  = new ArrayList<BarEntry>();
  //  ArrayList<BarEntry> diastolic  = new ArrayList<BarEntry>();
 //   ArrayList<String> days = new ArrayList<String>();


    public FragmentBloodPressureGraph() {
        // Required empty public constructor
    }


    public static FragmentBloodPressureGraph newInstance() {
        FragmentBloodPressureGraph fragment = new FragmentBloodPressureGraph();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    //    BarData data = new BarData(getXAxisValues(), getDataSet());
     //   chart.setData(data);
      //  chart.setDescription("My Chart");
      //  chart.animateXY(2000, 2000);
      //  chart.invalidate();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bpgraph, container, false);
        chart = (BarChart)v.findViewById(R.id.chart);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        //chart.setDescription("My Chart");
        chart.animateXY(2000, 2000);
        chart.invalidate();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_bpgraph;
    }

    @Override
    public void getActionBar(ActionBar actionBar) {

    }

    @Override
    public void provideInjection(AppComponent appComponent) {
        appComponent.inject(this);
    }


    @Override
    public void onYesClick() {

    }

    @Override
    public void onNoClick() {

    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1); // Feb
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2); // Mar
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(30.000f, 3); // Apr
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(90.000f, 4); // May
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(100.000f, 5); // Jun
        valueSet1.add(v1e6);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(150.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Brand 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        return xAxis;
    }


}
