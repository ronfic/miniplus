package com.example.miniplus_dev.setting;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.miniplus_dev.ExerciseActivity;
import com.example.miniplus_dev.PublicFunctions;
import com.example.miniplus_dev.R;


public class IsokineticBiFrag extends Fragment {

    private String TAG = "IsokineticBiFrag";
    View view;

    private final String WORKOUTSET_ISOKINETICBI_SERIAL = "F2010F0302";
    PublicFunctions publicFunctions = new PublicFunctions();


    Button btn_weight, btn_startPosition, btn_endPosition, btn_conSpeed, btn_eccSpeed;
    Button btn_upLow, btn_upHigh;
    Button btn_dnLow, btn_dnHigh;

    TextView txt_nowPosition;


    int int_selButton = 0;
    int i_type = 0;

    int i_weight = 5;
    int i_startPosition = 20;
    int i_endPosition = 100;
    int i_conSpeed = 25;
    int i_eccSpeed = 25;
    String str_weight, str_startPosition, str_endPosition, str_conSpeed, str_eccSpeed = "0000";


    public void changeValues(boolean sign, int changeValue){
        if(int_selButton == 0){
//            if(sign)    i_weight += changeValue;
//            else        i_weight -= changeValue;

//            if(i_weight > 100)  i_weight = 100;
//            if(i_weight < 5)    i_weight = 5;

            btn_weight.setBackgroundResource(R.drawable.btn_setbig_press);
            btn_startPosition.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_endPosition.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_conSpeed.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_eccSpeed.setBackgroundResource(R.drawable.btn_setsmall_nopress);

        }
        else if(int_selButton == 1){
            if(sign)    i_startPosition += changeValue * 10;
            else        i_startPosition -= changeValue * 10;

            if(i_startPosition > 1000)   i_startPosition = 1000;
            if(i_startPosition < 20)     i_startPosition = 20;
            btn_weight.setBackgroundResource(R.drawable.btn_setbig_nopress);
            btn_startPosition.setBackgroundResource(R.drawable.btn_setsmall_press);
            btn_endPosition.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_conSpeed.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_eccSpeed.setBackgroundResource(R.drawable.btn_setsmall_nopress);
        }
        else if(int_selButton == 2){
            if(sign)    i_endPosition += changeValue * 10;
            else        i_endPosition -= changeValue * 10;

            if(i_endPosition > 1500)  i_endPosition = 1500;
            if(i_endPosition < 100)   i_endPosition = 100;
            btn_weight.setBackgroundResource(R.drawable.btn_setbig_nopress);
            btn_startPosition.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_endPosition.setBackgroundResource(R.drawable.btn_setsmall_press);
            btn_conSpeed.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_eccSpeed.setBackgroundResource(R.drawable.btn_setsmall_nopress);
        }
        else if(int_selButton == 3){
            if(changeValue == 1){
                if(sign)    i_conSpeed += 25;
                else        i_conSpeed -= 25;
            }
            else{
                if(sign)    i_conSpeed += changeValue*10;
                else        i_conSpeed -= changeValue*10;
            }
            //if(sign)    i_conSpeed += changeValue*10;
            //else        i_conSpeed -= changeValue*10;

            if(i_conSpeed > 1000)   i_conSpeed = 1000;
            if(i_conSpeed < 25)    i_conSpeed = 25;
            btn_weight.setBackgroundResource(R.drawable.btn_setbig_nopress);
            btn_startPosition.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_endPosition.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_conSpeed.setBackgroundResource(R.drawable.btn_setsmall_press);
            btn_eccSpeed.setBackgroundResource(R.drawable.btn_setsmall_nopress);
        }
        else{
            if(changeValue == 1){
                if(sign)    i_eccSpeed += 25;
                else        i_eccSpeed -= 25;
            }
            else{
                if(sign)    i_eccSpeed += changeValue*10;
                else        i_eccSpeed -= changeValue*10;
            }
            //if(sign)    i_eccSpeed += changeValue*10;
            //else        i_eccSpeed -= changeValue*10;

            if(i_eccSpeed > 1000)   i_eccSpeed = 1000;
            if(i_eccSpeed < 25)    i_eccSpeed = 25;
            btn_weight.setBackgroundResource(R.drawable.btn_setbig_nopress);
            btn_startPosition.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_endPosition.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_conSpeed.setBackgroundResource(R.drawable.btn_setsmall_nopress);
            btn_eccSpeed.setBackgroundResource(R.drawable.btn_setsmall_press);
        }


        if(i_type == 0){
            btn_weight.setText("" + (double)(i_weight));
            btn_startPosition.setText("" + (double)(i_startPosition/10.0));
            btn_endPosition.setText("" + (double)(i_endPosition/10.0));
            btn_conSpeed.setText("" + (double)(i_conSpeed/10.0));
            btn_eccSpeed.setText("" + (double)(i_eccSpeed/10.0));

            if(int_selButton == 0){
                btn_upHigh.setText("+5");
                btn_upLow.setText("+1");
                btn_dnLow.setText("-1");
                btn_dnHigh.setText("-5");
                btn_upHigh.setVisibility(View.INVISIBLE);
                btn_upLow.setVisibility(View.INVISIBLE);
                btn_dnLow.setVisibility(View.INVISIBLE);
                btn_dnHigh.setVisibility(View.INVISIBLE);
            }
            else if(int_selButton == 3 || int_selButton == 4){
                btn_upHigh.setText("+5");
                btn_upLow.setText("+2.5");
                btn_dnLow.setText("-2.5");
                btn_dnHigh.setText("-5");
                btn_upHigh.setVisibility(View.VISIBLE);
                btn_upLow.setVisibility(View.VISIBLE);
                btn_dnLow.setVisibility(View.VISIBLE);
                btn_dnHigh.setVisibility(View.VISIBLE);
            }
            else{
                btn_upHigh.setText("+5");
                btn_upLow.setText("+1");
                btn_dnLow.setText("-1");
                btn_dnHigh.setText("-5");
                btn_upHigh.setVisibility(View.VISIBLE);
                btn_upLow.setVisibility(View.VISIBLE);
                btn_dnLow.setVisibility(View.VISIBLE);
                btn_dnHigh.setVisibility(View.VISIBLE);
            }
        }
        else if(i_type == 1){
            btn_weight.setText("" + (double)(i_weight/4.0));
            btn_startPosition.setText("" + (double)(i_startPosition/2.5));
            btn_endPosition.setText("" + (double)(i_endPosition/2.5));
            btn_conSpeed.setText("" + (double)(i_conSpeed/2.5));
            btn_eccSpeed.setText("" + (double)(i_eccSpeed/2.5));


            if(int_selButton == 0){
                btn_upHigh.setText("+1.25");
                btn_upLow.setText("+0.25");
                btn_dnLow.setText("-0.25");
                btn_dnHigh.setText("-1.25");
                btn_upHigh.setVisibility(View.INVISIBLE);
                btn_upLow.setVisibility(View.INVISIBLE);
                btn_dnLow.setVisibility(View.INVISIBLE);
                btn_dnHigh.setVisibility(View.INVISIBLE);
            }
            else if(int_selButton == 3 || int_selButton == 4){
                btn_upHigh.setText("+20");
                btn_upLow.setText("+10");
                btn_dnLow.setText("-10");
                btn_dnHigh.setText("-20");
                btn_upHigh.setVisibility(View.VISIBLE);
                btn_upLow.setVisibility(View.VISIBLE);
                btn_dnLow.setVisibility(View.VISIBLE);
                btn_dnHigh.setVisibility(View.VISIBLE);
            }
            else{
                btn_upHigh.setText("+20");
                btn_upLow.setText("+4");
                btn_dnLow.setText("-4");
                btn_dnHigh.setText("-20");
                btn_upHigh.setVisibility(View.VISIBLE);
                btn_upLow.setVisibility(View.VISIBLE);
                btn_dnLow.setVisibility(View.VISIBLE);
                btn_dnHigh.setVisibility(View.VISIBLE);
            }
        }
        else if(i_type == 2){
            btn_weight.setText("" + (double)(i_weight/2.0));
            btn_startPosition.setText("" + (double)(i_startPosition/5.0));
            btn_endPosition.setText("" + (double)(i_endPosition/5.0));
            btn_conSpeed.setText("" + (double)(i_conSpeed/5.0));
            btn_eccSpeed.setText("" + (double)(i_eccSpeed/5.0));



            if(int_selButton == 0){
                btn_upHigh.setText("+2.5");
                btn_upLow.setText("+0.5");
                btn_dnLow.setText("-0.5");
                btn_dnHigh.setText("-2.5");
                btn_upHigh.setVisibility(View.INVISIBLE);
                btn_upLow.setVisibility(View.INVISIBLE);
                btn_dnLow.setVisibility(View.INVISIBLE);
                btn_dnHigh.setVisibility(View.INVISIBLE);
            }
            else if(int_selButton == 3 || int_selButton == 4){
                btn_upHigh.setText("+10");
                btn_upLow.setText("+5");
                btn_dnLow.setText("-5");
                btn_dnHigh.setText("-10");
                btn_upHigh.setVisibility(View.VISIBLE);
                btn_upLow.setVisibility(View.VISIBLE);
                btn_dnLow.setVisibility(View.VISIBLE);
                btn_dnHigh.setVisibility(View.VISIBLE);
            }
            else{
                btn_upHigh.setText("+10");
                btn_upLow.setText("+2");
                btn_dnLow.setText("-2");
                btn_dnHigh.setText("-10");
                btn_upHigh.setVisibility(View.VISIBLE);
                btn_upLow.setVisibility(View.VISIBLE);
                btn_dnLow.setVisibility(View.VISIBLE);
                btn_dnHigh.setVisibility(View.VISIBLE);
            }
        }

        if(i_type == 1 && (int_selButton == 0)){
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

    public String getStart() {
        return btn_startPosition.getText().toString();
    }

    public String getEnd() {
        return btn_endPosition.getText().toString();
    }

    public String getConSpeed() {
        return btn_conSpeed.getText().toString();
    }

    public String getEccSpeed() {
        return btn_eccSpeed.getText().toString();
    }

    public void setFragValues(int type) {
        i_type = type;
    }

    public void receiveValues(double position){
        if(txt_nowPosition != null) txt_nowPosition.setText("" + (int)position);
    }

    private void sendSerial(){
        str_weight      = publicFunctions.intToByte100(i_weight);
        str_startPosition   = publicFunctions.intToByte(i_startPosition);
        str_endPosition     = publicFunctions.intToByte(i_endPosition);
        str_conSpeed        = publicFunctions.intToByte(i_conSpeed);
        str_eccSpeed        = publicFunctions.intToByte(i_eccSpeed);


        ((ExerciseActivity) getActivity()).setFragValue(WORKOUTSET_ISOKINETICBI_SERIAL + str_weight + str_startPosition + str_endPosition + str_conSpeed + str_eccSpeed + "00000000" + "FE");
    }

    private void initView(){
        btn_weight          = (Button) view.findViewById(R.id.btn_weight);
        btn_startPosition   = (Button) view.findViewById(R.id.btn_startPosition);
        btn_endPosition     = (Button) view.findViewById(R.id.btn_endPosition);
        btn_conSpeed        = (Button) view.findViewById(R.id.btn_setConcentricSpeed);
        btn_eccSpeed        = (Button) view.findViewById(R.id.btn_setEccentricSpeed);

        btn_upLow   = (Button) view.findViewById(R.id.btn_upLow);
        btn_dnLow   = (Button) view.findViewById(R.id.btn_dnLow);
        btn_upHigh  = (Button) view.findViewById(R.id.btn_upHigh);
        btn_dnHigh  = (Button) view.findViewById(R.id.btn_dnHigh);

        txt_nowPosition = (TextView) view.findViewById(R.id.txt_nowPosition);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_isokinetic_bi, container, false);
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
        btn_startPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(int_selButton != 1){
                    int_selButton = 1;
                    changeValues(false, 0);
                }
            }
        });
        btn_endPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(int_selButton != 2){
                    int_selButton = 2;
                    changeValues(false, 0);
                }
            }
        });
        btn_conSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(int_selButton != 3){
                    int_selButton = 3;
                    changeValues(false, 0);
                }
            }
        });
        btn_eccSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(int_selButton != 4){
                    int_selButton = 4;
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
