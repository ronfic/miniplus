package com.example.miniplus_dev.setting;


import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miniplus_dev.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeasureResultTestFrag extends Fragment {


    View view;
    Description description;
    Legend legend;

    private LinearLayout bgGap;
    private TextView txtTitle;
    private TextView txtGrade;
    private TextView txtStrong;
    private HorizontalBarChart barChartBalance;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i("resultFrag", "OnCreate");

        view = inflater.inflate(R.layout.fragment_measure_result_test, container, false);

        bgGap = (LinearLayout) view.findViewById(R.id.measure_bg_select_gap_img);

        txtTitle = (TextView) view.findViewById(R.id.measure_txt_result_selcom);
        txtGrade = (TextView) view.findViewById(R.id.measure_txt_result_grade);
        txtStrong = (TextView) view.findViewById(R.id.measure_txt_result_strong);

        barChartBalance = (HorizontalBarChart) view.findViewById(R.id.measure_barchart_balance);

        return view;

    }

    private void setBarchartBalance(double num1, double num2) {

        //Log.i("testFrag", "setBarchartBalance");


        ArrayList<BarEntry> entries = new ArrayList<>();
        ;

        entries.add(new BarEntry(0, new float[]{(float) num1, (float) num2}));


        BarDataSet data;
        data = new BarDataSet(entries, "");

        data.setValueFormatter(new PercentFormatter());

        data.setColors(Color.argb(120, 0, 230, 248), Color.rgb(0, 200, 218));


        description = barChartBalance.getDescription();
        description.setTextColor(Color.rgb(255, 255, 255));
        description.setText("");
        description.setTextSize(0);

        legend = barChartBalance.getLegend();
        legend.setEnabled(false);

        YAxis leftAxis = barChartBalance.getAxisLeft();
        YAxis rightAxis = barChartBalance.getAxisRight();
        XAxis xAxis = barChartBalance.getXAxis();

        xAxis.setTextSize(3f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);


        leftAxis.setTextSize(3f);
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);

        //leftAxis.setAxisMinimum(-10);
        //leftAxis.setAxisMaximum(10);
        //leftAxis.setLabelCount(3, true);

        rightAxis.setDrawAxisLine(true);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);


        ArrayList<IBarDataSet> Barchart_dataSets = new ArrayList<IBarDataSet>();
        Barchart_dataSets.add(data);

        BarData barData = new BarData(Barchart_dataSets);
        barData.setBarWidth(1f);
        barData.setValueTextSize(10f);
        barData.setValueTextColor(Color.rgb(255, 255, 255));


        barChartBalance.setData(barData);
        barChartBalance.setFitBars(false); // make the x-axis fit exactly all bars

        barChartBalance.animateY(1000);
        barChartBalance.setTouchEnabled(false);

        barChartBalance.setScaleEnabled(false);
        barChartBalance.setDoubleTapToZoomEnabled(false);
        barChartBalance.setDrawBorders(false);
        barChartBalance.setDescription(description);
        barChartBalance.setDrawValueAboveBar(false);
        //barChartBalance.setVisibleXRange(0, 1);
        //barChartBalance.moveViewToX(1);

        barChartBalance.invalidate(); // refresh
    }

    public void setBg(int selPage, int selStrong, String selGrade, String selStr, double selPer_1, double selPer_2) {
        bgGap.setBackgroundResource(R.drawable.measure_result_bg);
        if (selPage == 0) {
            txtTitle.setText("상지 전면 좌우 비교");
            if (selStrong == 1) bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_push);
            else if (selStrong == 2)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_push_ln);
            else if (selStrong == 3)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_push_lb);
            else if (selStrong == 4)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_push_rn);
            else if (selStrong == 5)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_push_rb);
        }

        if (selPage == 1) {
            txtTitle.setText("상지 후면 좌우 비교");
            if (selStrong == 1) bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_pull);
            else if (selStrong == 2)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_pull_ln);
            else if (selStrong == 3)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_pull_lb);
            else if (selStrong == 4)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_pull_rn);
            else if (selStrong == 5)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_pull_rb);
        }

        if (selPage == 2) {
            txtTitle.setText("몸통 좌우 비교");
            if (selStrong == 1) bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_rot);
            else if (selStrong == 2)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_rot_ln);
            else if (selStrong == 3)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_rot_lb);
            else if (selStrong == 4)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_rot_rn);
            else if (selStrong == 5)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_fun_rot_rb);
        }

        if (selPage == 3) {
            txtTitle.setText("상지 전후방 비교");
            if (selStrong == 1) bgGap.setBackgroundResource(R.drawable.measure_result_bg_up);
            else if (selStrong == 2)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_up_rn);
            else if (selStrong == 3)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_up_rb);
            else if (selStrong == 4)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_up_fn);
            else if (selStrong == 5)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_up_fb);
        }

        if (selPage == 4) {
            txtTitle.setText("하지 전후방 비교");
            if (selStrong == 1) bgGap.setBackgroundResource(R.drawable.measure_result_bg_low);
            else if (selStrong == 2)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_low_rn);
            else if (selStrong == 3)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_low_rb);
            else if (selStrong == 4)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_low_fn);
            else if (selStrong == 5)
                bgGap.setBackgroundResource(R.drawable.measure_result_bg_low_fb);
        }

        txtGrade.setText(selGrade);
        txtStrong.setText(selStr);
        setBarchartBalance(selPer_1, selPer_2);
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle bundle = getArguments();

        try {
            //Log.i("resultFrag", "Onresum _ selPage = " + bundle.getInt("selPage"));
            //Log.i("resultFrag", "Onresum _ selStrong = " + bundle.getInt("selStrong"));

            setBg(bundle.getInt("selPage"), bundle.getInt("selStrong"), bundle.getString("selGrade"), bundle.getString("selStr"), bundle.getDouble("selPer_1"), bundle.getDouble("selPer_2"));
        } catch (Exception e) {
            Log.e("resultFrag", "Onresum _ call Bundle Fail!!!");
            setBg(0, 0, "-", "-", 0, 0);
        }

    }


}