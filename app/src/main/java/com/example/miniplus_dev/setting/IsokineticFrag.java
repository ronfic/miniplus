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


public class IsokineticFrag extends Fragment {
    private String TAG = "IsokineticFrag";

    private final String WORKOUTSET_ISOKINETIC_SERIAL = "F2010F0301";

    PublicFunctions publicFunctions = new PublicFunctions();

    View view;

    Button btn_weight, btn_speed;
    Button btn_upLow, btn_upHigh;
    Button btn_dnLow, btn_dnHigh;

    int int_selButton = 0;

    int i_type = 0;
    int i_weight = 5;
    int i_speed = 100;
    String str_weight, str_speed = "0000";

    public void setFragValues(int type) {
        i_type = type;
    }

    private void sendSerial(){
        str_weight  = publicFunctions.intToByte100(i_weight);
        str_speed   = publicFunctions.intToByte(i_speed);
        ((ExerciseActivity) getActivity()).setFragValue(WORKOUTSET_ISOKINETIC_SERIAL + str_weight + str_speed + "00000000000000000000" + "FE");
    }

    public void changeValues(boolean sign, int changeValue){
        if(int_selButton == 0){
            if(sign)    i_weight += changeValue;
            else        i_weight -= changeValue;

            if(i_weight > 100)  i_weight = 100;
            if(i_weight < 5)    i_weight = 5;
            btn_weight.setBackgroundResource(R.drawable.btn_setbig_press);
            btn_speed.setBackgroundResource(R.drawable.btn_setbig_nopress);
        }
        else{
            if(changeValue == 1){
                if(sign)    i_speed += changeValue * 10;
                else        i_speed -= changeValue * 10;
            }
            else{
                if(sign)    i_speed += changeValue * 10;
                else        i_speed -= changeValue * 10;
            }

            if(i_speed > 2000)  i_speed = 2000;
            if(i_speed < 100)   i_speed = 100;
            btn_speed.setBackgroundResource(R.drawable.btn_setbig_press);
            btn_weight.setBackgroundResource(R.drawable.btn_setbig_nopress);
        }

        if(i_type == 0){
            btn_weight.setText("" + (double)(i_weight));
            btn_speed.setText("" + (double)(i_speed/10));
            if(int_selButton == 0){
                btn_upHigh.setText("+5");
                btn_upLow.setText("+1");
                btn_dnLow.setText("-1");
                btn_dnHigh.setText("-5");
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
            btn_speed.setText("" + (double)(i_speed/2.5));
            if(int_selButton == 0){
                btn_upHigh.setText("+1.25");
                btn_upLow.setText("+0.25");
                btn_dnLow.setText("-0.25");
                btn_dnHigh.setText("-1.25");
            }
            else{
                btn_upHigh.setText("+20");
                btn_upLow.setText("+4");
                btn_dnLow.setText("-4");
                btn_dnHigh.setText("-20");
            }
        }
        else if(i_type == 2){
            btn_weight.setText("" + (double)(i_weight/2.0));
            btn_speed.setText("" + (double)(i_speed/5.0));
            if(int_selButton == 0){
                btn_upHigh.setText("+2.5");
                btn_upLow.setText("+0.5");
                btn_dnLow.setText("-0.5");
                btn_dnHigh.setText("-2.5");
            }
            else{
                btn_upHigh.setText("+10");
                btn_upLow.setText("+2");
                btn_dnLow.setText("-2");
                btn_dnHigh.setText("-10");
            }
        }
        if(i_type == 1 && int_selButton == 0){
            btn_upLow.setTextSize(14);
            btn_dnLow.setTextSize(14);
        }
        else{
            btn_upLow.setTextSize(16);
            btn_dnLow.setTextSize(16);
        }
    }

    public String getWeight() {
        return btn_weight.getText().toString();
    }

    public String getSpeed() {
        return btn_speed.getText().toString();
    }

    private void initView(){
        btn_weight  = (Button) view.findViewById(R.id.btn_weight);
        btn_speed   = (Button) view.findViewById(R.id.btn_speed);

        btn_upLow   = (Button) view.findViewById(R.id.btn_upLow);
        btn_dnLow   = (Button) view.findViewById(R.id.btn_dnLow);
        btn_upHigh  = (Button) view.findViewById(R.id.btn_upHigh);
        btn_dnHigh  = (Button) view.findViewById(R.id.btn_dnHigh);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_isokinetic, container, false);
        initView();
        Log.i(TAG, "onCreateView");



        btn_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(int_selButton != 0){
                    int_selButton = 0;
                    changeValues(false, 0);
                }
            }
        });
        btn_speed.setOnClickListener(new View.OnClickListener() {
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
        changeValues(false, 0);
        sendSerial();

    }



}
