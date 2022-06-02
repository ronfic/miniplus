package com.example.miniplus_dev.setting;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.miniplus_dev.ExerciseActivity;
import com.example.miniplus_dev.PublicValues;
import com.example.miniplus_dev.R;


public class ModeSelectFragment extends Fragment {

    private String TAG = "ModeSelect";

    View view;

    public static FrameLayout layout_exercise;
    public boolean bool_popLayoutStatus;

    public LinearLayout whole_page;
    public LinearLayout linear_modesel;
    public Button btn_isokineticMode;
    public Button btn_isokineticBiMode;
    public Button btn_eccentricMode;
    public Button btn_springMode;
    public Button btn_dampingMode;
    public Button btn_vibrationMode;
    public Button btn_exit;



    public void sendData(int modeSel) {

        ((ExerciseActivity) getActivity()).receiveFragNum(modeSel);
        fragment_close();

    }


    private void initView(){
        whole_page             = (LinearLayout) view.findViewById(R.id.whole_page);

        btn_isokineticMode     = (Button) view.findViewById(R.id.btn_isokineticMode);
        btn_isokineticBiMode     = (Button) view.findViewById(R.id.btn_isokineticBiMode);
        btn_eccentricMode   = (Button) view.findViewById(R.id.btn_eccentricMode);
        btn_springMode      = (Button) view.findViewById(R.id.btn_springMode);
        btn_dampingMode     = (Button) view.findViewById(R.id.btn_dampingMode);
        btn_vibrationMode   = (Button) view.findViewById(R.id.btn_vibrationMode);
        btn_exit            = (Button) view.findViewById(R.id.btn_exit);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_mode_select, container, false);

        initView();
        if(PublicValues.club_id.equals("1059") || PublicValues.club_id.equals("1000")) {
            btn_isokineticBiMode.setVisibility(View.VISIBLE);
        }

        view.setClickable(true);

        whole_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "운동을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_isokineticMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData(1);
            }
        });
        btn_isokineticBiMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData(2);
            }
        });
        btn_eccentricMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData(4);
            }
        });
        btn_springMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData(6);
            }
        });
        btn_dampingMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData(7);
            }
        });
        btn_vibrationMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData(8);
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_close();
            }
        });



        return view;
    }

    public void fragment_close() {
        bool_popLayoutStatus = false;
        ExerciseActivity.fragmentManager.beginTransaction().remove(ModeSelectFragment.this).commitAllowingStateLoss();
        ExerciseActivity.fragmentManager.popBackStack();
    }

}
