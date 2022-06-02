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


public class VibrationFrag extends Fragment {

    private String TAG = "VibrationFrag";
    View view;

    private final String WORKOUTSET_VABRATION_SERIAL = "F2010F0308";
    PublicFunctions publicFunctions = new PublicFunctions();


    Button btn_weight, btn_setPosition, btn_nowPosition, btn_frequency, btn_variation;
    Button btn_upLow, btn_upHigh;
    Button btn_dnLow, btn_dnHigh;

    int int_selButton = 0;
    int i_type = 0;

    int i_weight = 5;
    int i_setPosition = 20;
    int i_nowPosition = 0;
    int i_frequency = 50;
    int i_variation = 2500;
    String str_weight, str_position, str_variation = "0000";
    String str_frequency = "00";


    public void changeValues(boolean sign, int changeValue){
        if(int_selButton == 0){
            if(sign)    i_weight += changeValue;
            else        i_weight -= changeValue;

            if(i_weight > 100)  i_weight = 100;
            if(i_weight < 5)    i_weight = 5;

            btn_weight.setBackgroundResource(R.drawable.btn_setbig_press);
            btn_setPosition.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_frequency.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_variation.setBackgroundResource(R.drawable.btn_setsmall_nopress);
        }
        else if(int_selButton == 1){
            if(sign)    i_setPosition += changeValue * 10;
            else        i_setPosition -= changeValue * 10;

            if(i_setPosition > 1500)   i_setPosition = 1500;
            if(i_setPosition < 20)     i_setPosition = 20;
            btn_weight.setBackgroundResource(R.drawable.btn_setbig_nopress);
            btn_setPosition.setBackgroundResource(R.drawable.btn_setsmall_press);
            btn_frequency.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_variation.setBackgroundResource(R.drawable.btn_setsmall_nopress);
        }
        else if(int_selButton == 2){
            if(sign)    i_frequency += changeValue;
            else        i_frequency -= changeValue;

            if(i_frequency > 50)    i_frequency = 50;
            if(i_frequency < 1)      i_frequency = 1;
            btn_weight.setBackgroundResource(R.drawable.btn_setbig_nopress);
            btn_setPosition.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_frequency.setBackgroundResource(R.drawable.btn_setsmall_press);
            btn_variation.setBackgroundResource(R.drawable.btn_setsmall_nopress);
        }
        else{
            if(sign)    i_variation += changeValue*100;
            else        i_variation -= changeValue*100;

            if(i_variation > 5000)    i_variation = 5000;
            if(i_variation < 500)      i_variation = 500;
            btn_weight.setBackgroundResource(R.drawable.btn_setbig_nopress);
            btn_setPosition.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_frequency.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_variation.setBackgroundResource(R.drawable.btn_setsmall_press);
        }


        if(i_type == 0){
            btn_weight.setText("" + (double)(i_weight));
            btn_setPosition.setText("" + (double)(i_setPosition/10.0));
            btn_frequency.setText("" + (double)(i_frequency));
            btn_variation.setText("" + (double)(i_variation/1000.0));

            if(int_selButton == 3){
                btn_upHigh.setText("+0.5");
                btn_upLow.setText("+0.1");
                btn_dnLow.setText("-0.1");
                btn_dnHigh.setText("-0.5");
            }
            else{
                btn_upHigh.setText("+5");
                btn_upLow.setText("+1");
                btn_dnLow.setText("-1");
                btn_dnHigh.setText("-5");
            }
        }
        else if(i_type == 1){
            btn_weight.setText("" + (double)(i_weight/4.0));
            btn_setPosition.setText("" + (double)(i_setPosition/2.5));
            btn_frequency.setText("" + (double)(i_frequency));
            btn_variation.setText("" + (double)(i_variation/4000.0));


            if(int_selButton == 1){
                btn_upHigh.setText("+20");
                btn_upLow.setText("+4");
                btn_dnLow.setText("-4");
                btn_dnHigh.setText("-20");
            }
            else if(int_selButton == 2){
                btn_upHigh.setText("+5");
                btn_upLow.setText("+1");
                btn_dnLow.setText("-1");
                btn_dnHigh.setText("-5");
            }
            else if(int_selButton == 3){
                btn_upHigh.setText("+0.125");
                btn_upLow.setText("+0.25");
                btn_dnLow.setText("-0.25");
                btn_dnHigh.setText("-0.125");
            }
            else{
                btn_upHigh.setText("+1.25");
                btn_upLow.setText("+0.25");
                btn_dnLow.setText("-0.25");
                btn_dnHigh.setText("-1.25");
            }
        }
        else if(i_type == 2){
            btn_weight.setText("" + (double)(i_weight/2.0));
            btn_setPosition.setText("" + (double)(i_setPosition/5.0));
            btn_frequency.setText("" + (double)(i_frequency));
            btn_variation.setText("" + (double)(i_variation/2000.0));



            if(int_selButton == 1){
                btn_upHigh.setText("+10");
                btn_upLow.setText("+2");
                btn_dnLow.setText("-2");
                btn_dnHigh.setText("-10");
            }
            else if(int_selButton == 2){
                btn_upHigh.setText("+5");
                btn_upLow.setText("+1");
                btn_dnLow.setText("-1");
                btn_dnHigh.setText("-5");
            }
            else if(int_selButton == 3){
                btn_upHigh.setText("+0.25");
                btn_upLow.setText("+0.05");
                btn_dnLow.setText("-0.05");
                btn_dnHigh.setText("-0.25");
            }
            else{
                btn_upHigh.setText("+2.5");
                btn_upLow.setText("+0.5");
                btn_dnLow.setText("-0.5");
                btn_dnHigh.setText("-2.5");
            }
        }

        if(i_type == 1 && int_selButton == 0){
            btn_upLow.setTextSize(14);
            btn_dnLow.setTextSize(14);
        }
        else if(i_type != 0 && int_selButton == 3){
            btn_upHigh.setTextSize(14);
            btn_upLow.setTextSize(14);
            btn_dnLow.setTextSize(14);
            btn_dnHigh.setTextSize(14);
        }
        else{
            btn_upHigh.setTextSize(16);
            btn_upLow.setTextSize(16);
            btn_dnLow.setTextSize(16);
            btn_dnHigh.setTextSize(16);
        }
    }

    public String getWeight() {
        return btn_weight.getText().toString();
    }

    public String getFrequency() {
        return btn_frequency.getText().toString();
    }

    public String getVariation() {
        return btn_variation.getText().toString();
    }

    public String getSetpos() {
        return btn_setPosition.getText().toString();
    }

    public void setFragValues(int type) {
        i_type = type;
    }

    public void receiveValues(double position){
        if(btn_nowPosition != null) btn_nowPosition.setText("" + (int)position);
    }

    private void sendSerial(){
        str_weight      = publicFunctions.intToByte100(i_weight);
        str_position    = publicFunctions.intToByte(i_setPosition);
        str_frequency   = publicFunctions.intTo1Byte(i_frequency);
        str_variation   = publicFunctions.intToByte(i_variation);

        ((ExerciseActivity) getActivity()).setFragValue(WORKOUTSET_VABRATION_SERIAL + str_weight + str_position + str_frequency + str_variation + "00000000000000" + "FE");
    }

    private void initView(){
        btn_weight      = (Button) view.findViewById(R.id.btn_weight);
        btn_setPosition = (Button) view.findViewById(R.id.btn_setPosition);
        btn_nowPosition = (Button) view.findViewById(R.id.btn_nowPosition);
        btn_frequency   = (Button) view.findViewById(R.id.btn_frequency);
        btn_variation   = (Button) view.findViewById(R.id.btn_variation);

        btn_upLow   = (Button) view.findViewById(R.id.btn_upLow);
        btn_dnLow   = (Button) view.findViewById(R.id.btn_dnLow);
        btn_upHigh  = (Button) view.findViewById(R.id.btn_upHigh);
        btn_dnHigh  = (Button) view.findViewById(R.id.btn_dnHigh);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vibration, container, false);
        Log.i(TAG, "onCreateView");
        initView();

        btn_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(int_selButton != 0){
                    int_selButton = 0;
                    changeValues(false, 0);
                }
            }
        });
        btn_setPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(int_selButton != 1){
                    int_selButton = 1;
                    changeValues(false, 0);
                }
            }
        });
        btn_frequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(int_selButton != 2){
                    int_selButton = 2;
                    changeValues(false, 0);
                }
            }
        });
        btn_variation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(int_selButton != 3){
                    int_selButton = 3;
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
