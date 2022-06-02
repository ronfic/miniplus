package com.example.miniplus_dev.setting;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.miniplus_dev.MeasureTestActivity;
import com.example.miniplus_dev.MeasureTestActivity2;
import com.example.miniplus_dev.PublicFunctions;
import com.example.miniplus_dev.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeasureResultFrag extends Fragment {

    PublicFunctions functionBundle = new PublicFunctions();
    OnTimePickerSetListener onTimePickerSetListener;

    private final String TAG = "";


    View view;


    LinearLayout bgLR, bgAM, resHand;
    Button btnSelResultRight, btnSelResultLeft;
    Button btnSelResultAVG, btnSelResultMax;

    TextView txtAvgAvgSpeed, txtAvgAvgPower;
    TextView txtAvgMaxSpeed, txtAvgMaxPower;
    TextView txtMaxAvgSpeed, txtMaxAvgPower;
    TextView txtMaxMaxSpeed, txtMaxMaxPower;


    int selNum;


    double valAvgAvgSpeed, valAvgAvgPower;
    double valAvgMaxSpeed, valAvgMaxPower;
    double valMaxAvgSpeed, valMaxAvgPower;
    double valMaxMaxSpeed, valMaxMaxPower;

    public boolean selResultFlag = false;

    String[][] resultValue = new String[3][6];   //결과 값
    String[][] resultValueL = new String[3][6];   //왼손 결과 값

    public interface OnTimePickerSetListener {
        void onTimePickerSet(boolean LR_Button);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_measure_workresult, container, false);
        // Inflate the layout for this fragment

        selResultFlag = false;


        initView(view);
        valAvgAvgSpeed = 0;
        valAvgAvgPower = 0;
        valAvgMaxSpeed = 0;
        valAvgMaxPower = 0;
        valMaxAvgSpeed = 0;
        valMaxAvgPower = 0;
        valMaxMaxSpeed = 0;
        valMaxMaxPower = 0;
        getActivityData();


        setTextData(false);

        onTimePickerSetListener = (OnTimePickerSetListener) getActivity();

        MeasureTestActivity.fragChangeFlag = MeasureTestActivity2.fragChangeFlag = false;


        btnSelResultRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG, "touch!!! RIGHT");
                selResultFlag = false;
                setTextData(false);
                bgLR.setBackgroundResource(R.drawable.measure_btn_right);

                onTimePickerSetListener.onTimePickerSet(false);
            }
        });
        btnSelResultLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(TAG, "touch!!! LEFT");
                selResultFlag = true;
                setTextData(true);
                bgLR.setBackgroundResource(R.drawable.measure_btn_left);

                onTimePickerSetListener.onTimePickerSet(true);
            }
        });

        btnSelResultAVG.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.i(TAG, "touch!!! AVG");

                bgAM.setBackgroundResource(R.drawable.measure_btn_avg);
                //btnSelResultAVG.setTextColor(0xFFFFFFFF);
                //btnSelResultMax.setTextColor(0xFF000000);

                txtAvgAvgPower.setVisibility(View.VISIBLE);
                txtMaxAvgPower.setVisibility(View.VISIBLE);
                txtAvgAvgSpeed.setVisibility(View.VISIBLE);
                txtMaxAvgSpeed.setVisibility(View.VISIBLE);
                txtAvgMaxPower.setVisibility(View.GONE);
                txtMaxMaxPower.setVisibility(View.GONE);
                txtAvgMaxSpeed.setVisibility(View.GONE);
                txtMaxMaxSpeed.setVisibility(View.GONE);

            }
        });

        btnSelResultMax.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.i(TAG, "touch!!! MAx");
                bgAM.setBackgroundResource(R.drawable.measure_btn_max);

                //btnSelResultAVG.setTextColor(0xFF000000);
                //btnSelResultMax.setTextColor(0xFFFFFFFF);

                txtAvgAvgPower.setVisibility(View.GONE);
                txtMaxAvgPower.setVisibility(View.GONE);
                txtAvgAvgSpeed.setVisibility(View.GONE);
                txtMaxAvgSpeed.setVisibility(View.GONE);
                txtAvgMaxPower.setVisibility(View.VISIBLE);
                txtMaxMaxPower.setVisibility(View.VISIBLE);
                txtAvgMaxSpeed.setVisibility(View.VISIBLE);
                txtMaxMaxSpeed.setVisibility(View.VISIBLE);

            }
        });


        if (selNum > 2) {
            resHand.setVisibility(View.INVISIBLE);
        } else {
            resHand.setVisibility(View.VISIBLE);
        }


        return view;
    }


    private void initView(View view) {

        btnSelResultAVG = (Button) view.findViewById(R.id.measure_btn_sel_result_avg);
        btnSelResultMax = (Button) view.findViewById(R.id.measure_btn_sel_result_max);
        btnSelResultRight = (Button) view.findViewById(R.id.measure_btn_sel_result_right);
        btnSelResultLeft = (Button) view.findViewById(R.id.measure_btn_sel_result_left);


        txtAvgAvgSpeed = (TextView) view.findViewById(R.id.txt_avg_avg_speed);
        txtAvgMaxSpeed = (TextView) view.findViewById(R.id.txt_avg_max_speed);
        txtMaxAvgSpeed = (TextView) view.findViewById(R.id.txt_max_avg_speed);
        txtMaxMaxSpeed = (TextView) view.findViewById(R.id.txt_max_max_speed);
        txtAvgAvgPower = (TextView) view.findViewById(R.id.txt_avg_avg_power);
        txtAvgMaxPower = (TextView) view.findViewById(R.id.txt_avg_max_power);
        txtMaxAvgPower = (TextView) view.findViewById(R.id.txt_max_avg_power);
        txtMaxMaxPower = (TextView) view.findViewById(R.id.txt_max_max_power);


        resHand = (LinearLayout) view.findViewById(R.id.measure_layout_hand);

        bgLR = (LinearLayout) view.findViewById(R.id.measure_bg_rl);
        bgAM = (LinearLayout) view.findViewById(R.id.measure_bg_am);

    }


    public void getActivityData() {
        Bundle bundle = getArguments();
        selNum = bundle.getInt("selNum");

        resultValue[0] = bundle.getStringArray("resultValue0");
        resultValue[1] = bundle.getStringArray("resultValue1");
        resultValue[2] = bundle.getStringArray("resultValue2");

        if (selNum < 3) {
            resultValueL[0] = bundle.getStringArray("resultValueL0");
            resultValueL[1] = bundle.getStringArray("resultValueL1");
            resultValueL[2] = bundle.getStringArray("resultValueL2");
        }

    }

    private void setTextData(boolean sel) {


        //Log.e(TAG, "setText!!!!!!!!!!!!");

        String[][] tmpResult = new String[3][6];

        valAvgAvgSpeed = 0;
        valAvgAvgPower = 0;
        valAvgMaxSpeed = 0;
        valAvgMaxPower = 0;
        valMaxAvgSpeed = 0;
        valMaxAvgPower = 0;
        valMaxMaxSpeed = 0;
        valMaxMaxPower = 0;

        if (sel) {
            tmpResult[0] = resultValueL[0];
            tmpResult[1] = resultValueL[1];
            tmpResult[2] = resultValueL[2];
        } else {
            tmpResult[0] = resultValue[0];
            tmpResult[1] = resultValue[1];
            tmpResult[2] = resultValue[2];

        }
/*
        Log.e(TAG, "sel = " + sel);
        Log.e(TAG, "tmpResult[0] = " + tmpResult[0][0]);
        Log.e(TAG, "tmpResult[0] = " + tmpResult[0][1]);
        Log.e(TAG, "tmpResult[0] = " + tmpResult[0][2]);

        Log.e(TAG, "\ntmpResult[1] = " + tmpResult[1][0]);
        Log.e(TAG, "tmpResult[2] = " + tmpResult[2][0]);
*/


        for (int i = 0; i < 3; i++) {
            //AVG계산을 위한 합산
            valAvgAvgPower += Double.valueOf(tmpResult[i][2]);  //avg power
            valMaxAvgPower += Double.valueOf(tmpResult[i][3]);  //max power
            valAvgAvgSpeed += Double.valueOf(tmpResult[i][4]);  //avg speed
            valMaxAvgSpeed += Double.valueOf(tmpResult[i][5]);  //max speed의 평균

            if (i == 0) { //MAX 계산구문
                valAvgMaxPower = Double.valueOf(tmpResult[i][2]);
                valMaxMaxPower = Double.valueOf(tmpResult[i][3]);
                valAvgMaxSpeed = Double.valueOf(tmpResult[i][4]);
                valMaxMaxSpeed = Double.valueOf(tmpResult[i][5]);
            } else if (i > 0) {
                if (valAvgMaxPower < Double.valueOf(tmpResult[i][2]))
                    valAvgMaxPower = Double.valueOf(tmpResult[i][2]);
                if (valMaxMaxPower < Double.valueOf(tmpResult[i][3]))
                    valMaxMaxPower = Double.valueOf(tmpResult[i][3]);
                if (valAvgMaxSpeed < Double.valueOf(tmpResult[i][4]))
                    valAvgMaxSpeed = Double.valueOf(tmpResult[i][4]);
                if (valMaxMaxSpeed < Double.valueOf(tmpResult[i][5]))
                    valMaxMaxSpeed = Double.valueOf(tmpResult[i][5]);
            }
        }

        //AVG계산
        valAvgAvgPower /= 3.0;
        valMaxAvgPower /= 3.0;
        valAvgAvgSpeed /= 3.0;
        valMaxAvgSpeed /= 3.0;

        //Text Set
        txtAvgAvgPower.setText(String.format("%.2f", valAvgAvgPower));
        txtMaxAvgPower.setText(String.format("%.2f", valMaxAvgPower));
        txtAvgAvgSpeed.setText(String.format("%.2f", valAvgAvgSpeed));
        txtMaxAvgSpeed.setText(String.format("%.2f", valMaxAvgSpeed));
        txtAvgMaxPower.setText(String.format("%.2f", valAvgMaxPower));
        txtMaxMaxPower.setText(String.format("%.2f", valMaxMaxPower));
        txtAvgMaxSpeed.setText(String.format("%.2f", valAvgMaxSpeed));
        txtMaxMaxSpeed.setText(String.format("%.2f", valMaxMaxSpeed));

    }

    @Override
    public void onResume() {
        super.onResume();


    }


}
