package com.example.miniplus_dev.setting;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.miniplus_dev.ExerciseActivity;
import com.example.miniplus_dev.PublicFunctions;
import com.example.miniplus_dev.R;


public class EccentricFrag2 extends Fragment {

    private String TAG = "EccentricFrag2";

    private final String WORKOUTSET_ECCENTRIC2_SERIAL = "F2010F0304";

    PublicFunctions publicFunctions = new PublicFunctions();

    View view;

    Button btn_upWeight, btn_dnWeight;
    Button btn_upLow, btn_upHigh;
    Button btn_dnLow, btn_dnHigh;

    int int_selButton = 0;

    int i_type = 0;
    int i_upWeight, i_dnWeight = 5;
    String str_upWeight, str_dnWeight = "0000";

    public void setFragValues(int type) {
        i_type = type;
    }


    private void sendSerial(){
        str_upWeight = publicFunctions.intToByte100(i_upWeight);
        str_dnWeight = publicFunctions.intToByte100(i_dnWeight);
        ((ExerciseActivity) getActivity()).setFragValue(WORKOUTSET_ECCENTRIC2_SERIAL + str_upWeight + str_dnWeight + "00000000000000000000" + "FE");
    }

    public void changeValues(boolean sign, int changeValue){
        if(int_selButton == 0){
            if(sign){
                i_upWeight += changeValue;
                i_dnWeight += changeValue;
            }
            else{
                i_upWeight -= changeValue;
                i_dnWeight -= changeValue;
            }

            if(i_upWeight > 100)  i_upWeight = 100;
            if(i_upWeight < 5)    i_upWeight = 5;
            if(i_dnWeight > 100)  i_dnWeight = 100;
            if(i_dnWeight < 5)    i_dnWeight = 5;

            btn_upWeight.setBackgroundResource(R.drawable.btn_setbig_press);
            btn_dnWeight.setBackgroundResource(R.drawable.btn_setbig_nopress);
        }
        else{
            if(sign)    i_dnWeight += changeValue;
            else        i_dnWeight -= changeValue;

            if(Math.abs(i_upWeight - i_dnWeight) > 20){
                if(sign)    i_upWeight += changeValue;
                else        i_upWeight -= changeValue;
            }

            if(i_upWeight > 100)  i_upWeight = 100;
            if(i_upWeight < 5)    i_upWeight = 5;
            if(i_dnWeight > 100)   i_dnWeight = 100;
            if(i_dnWeight < 5)    i_dnWeight = 5;
            btn_dnWeight.setBackgroundResource(R.drawable.btn_setbig_press);
            btn_upWeight.setBackgroundResource(R.drawable.btn_setbig_nopress);
        }

        if(i_type == 0){
            btn_upWeight.setText("" + (double)(i_upWeight));
            btn_dnWeight.setText("" + (double)(i_dnWeight));
            btn_upHigh.setText("+5");
            btn_upLow.setText("+1");
            btn_dnLow.setText("-1");
            btn_dnHigh.setText("-5");
            btn_upLow.setTextSize(16);
            btn_dnLow.setTextSize(16);
        }
        else if(i_type == 1){
            btn_upWeight.setText("" + (double)(i_upWeight/4.0));
            btn_dnWeight.setText("" + (double)(i_dnWeight/4.0));
            btn_upHigh.setText("+1.25");
            btn_upLow.setText("+0.25");
            btn_dnLow.setText("-0.25");
            btn_dnHigh.setText("-1.25");
            btn_upLow.setTextSize(14);
            btn_dnLow.setTextSize(14);
        }
        else if(i_type == 2){
            btn_upWeight.setText("" + (double)(i_upWeight/2.0));
            btn_dnWeight.setText("" + (double)(i_dnWeight/2.0));
            btn_upHigh.setText("+2.5");
            btn_upLow.setText("+0.5");
            btn_dnLow.setText("-0.5");
            btn_dnHigh.setText("-2.5");
            btn_upLow.setTextSize(16);
            btn_dnLow.setTextSize(16);
        }
    }

    public String getUpWeight() {
        return btn_upWeight.getText().toString();
    }

    public String getDnWeight() {
        return btn_dnWeight.getText().toString();
    }

    private void initView(){
        btn_upWeight    = (Button) view.findViewById(R.id.btn_upWeight);
        btn_dnWeight    = (Button) view.findViewById(R.id.btn_dnWeight);

        btn_upLow   = (Button) view.findViewById(R.id.btn_upLow);
        btn_dnLow   = (Button) view.findViewById(R.id.btn_dnLow);
        btn_upHigh  = (Button) view.findViewById(R.id.btn_upHigh);
        btn_dnHigh  = (Button) view.findViewById(R.id.btn_dnHigh);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_eccen2, container, false);
        initView();
        Log.i(TAG, "onCreateView");



        btn_upWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(int_selButton != 0){
                    int_selButton = 0;
                    changeValues(false, 0);
                }
            }
        });
        btn_dnWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(int_selButton != 1){
                    int_selButton = 1;
                    changeValues(false, 0);
                }
            }
        });

        btn_upHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeValues(true, 5);
                sendSerial();
            }
        });
        btn_upLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeValues(true, 1);
                sendSerial();
            }
        });
        btn_dnLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeValues(false, 1);
                sendSerial();
            }
        });
        btn_dnHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeValues(false, 5);
                sendSerial();
            }
        });

        changeValues(false, 0);
        sendSerial();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        sendSerial();
    }
}
