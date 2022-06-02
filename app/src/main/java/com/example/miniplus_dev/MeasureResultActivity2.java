package com.example.miniplus_dev;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

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
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.relex.circleindicator.CircleIndicator;


public class MeasureResultActivity2 extends FragmentActivity {

    private String TAG = "MeasureResultActivity";

    ViewPager viewPager = null;


    TextView txtUserName;
    TextView txtUserAge;
    TextView txtUserGender;
    TextView txtUserHeight;
    TextView txtUserWeight;
    TextView txtWorkoutDate;
    TextView txtUserGrage;

    TextView txtBalloonGrade;


    Intent intent;

    //String lastDate, oldDate;
    ScrollView scrollView;

    TextView txtTitle;
    TextView txtGrade;
    TextView txtStrong;
    TextView[] txtWorkOutDate = new TextView[10];

    HorizontalBarChart barChartBalance;

    HorizontalBarChart[] barChart = new HorizontalBarChart[10];
    Description description;
    Legend legend;


    Button btnHome, btnChangeUser;

    Button btnTop, btnMid, btnBot;

    Button btnBalloon;


    MyViewPagerAdapter2 adapter;
    Bundle putBundleData = new Bundle();


    private String[][] resultValue = new String[10][4];   //종합지 표기를 위한 결과값 저장 배열
    private String[][] resultOldValue = new String[10][4];   //종합지 표기를 위한 결과값 저장 배열

    private String[][] getD = new String[5][3];

    private int nowPage;


    private UsbService usbService;
    private MeasureHandler mHandler;
    private StringBuffer packetBufStr;



    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

    }



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_measure_result2);

        initSetId();

        adapter = new MyViewPagerAdapter2(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);


        nowPage = 0;

        try {
            initGetIntent();

            setUserInfo();


            setResultBarChart();

            setWorkOutDate();

            setTotalResult();

            setBundleData(0);
            adapter.fragments[0].setArguments(putBundleData);

            getData();
            if(PublicValues.club_id.equals("1121") && !PublicValues.userSecondId.equals("0") && PublicValues.MeasureResultFlag) {
                InsertESEWorkout insertESEWorkout = new InsertESEWorkout();
                insertESEWorkout.execute();
            }

        } catch (Exception e) {
            Log.e(TAG, "Fatal Error. onCreate!!!");
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                nowPage = position;
                if(nowPage == 0) {
                    scrollView.smoothScrollTo(0, 0);
                } else if(nowPage == 1) {
                    scrollView.smoothScrollTo(0, 300);
                } else if(nowPage == 2) {
                    scrollView.smoothScrollTo(0, 548);
                } else if(nowPage == 3) {
                    scrollView.smoothScrollTo(0, 0);
                } else if(nowPage == 4) {
                    scrollView.smoothScrollTo(0, 800);
                }

                Log.i(TAG, "onPageSelected" + position);
                try {
                    setTotalResult();

                    /*
                    if(position < 4){
                        setBundleData(position+1);
                        adapter.fragments[position+1].setArguments(putBundleData);
                        adapter.fragments[position+1].onResume();
                    }
                    if(position > 0){
                        setBundleData(position-1);
                        adapter.fragments[position-1].setArguments(putBundleData);
                        adapter.fragments[position-1].onResume();
                    }
                     */


                } catch (Exception e) {
                    Log.e(TAG, "Fatal Error. viewPager onPageSelected");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i(TAG, "onPageScrollStateChanged = " + state);
                //setTotalResult();

                if (state == 1) {
                    if (nowPage < 4) {
                        setBundleData(nowPage + 1);
                        adapter.fragments[nowPage + 1].setArguments(putBundleData);
                        adapter.fragments[nowPage + 1].onResume();
                    }
                    if (nowPage > 0) {
                        setBundleData(nowPage - 1);
                        adapter.fragments[nowPage - 1].setArguments(putBundleData);
                        adapter.fragments[nowPage - 1].onResume();
                    }

                }
            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MeasureResultActivity2.this, MeasureTestActivity2.class);

                startActivity(intent);

                finishAffinity();
            }
        });

        btnChangeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MeasureResultActivity2.this, MeasureLoginPadActivity2.class);

                intent.putExtra("whereFrom", "MeasureResultActivity");

                startActivity(intent);

                finishAffinity();
            }
        });


        btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scrollView.smoothScrollTo(0, 0);
                nowPage = 0;
                setBundleData(0);
                adapter.fragments[0].setArguments(putBundleData);
                viewPager.setCurrentItem(0);
            }
        });

        btnMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scrollView.smoothScrollTo(0, 548);
                nowPage = 2;
                setBundleData(2);
                adapter.fragments[2].setArguments(putBundleData);
                viewPager.setCurrentItem(2);

            }
        });

        btnBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scrollView.smoothScrollTo(0, 800);
                nowPage = 4;
                setBundleData(4);
                adapter.fragments[4].setArguments(putBundleData);
                viewPager.setCurrentItem(4);
            }
        });


        btnBalloon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txtBalloonGrade.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtBalloonGrade.setVisibility(View.INVISIBLE);
                    }
                }, 3000);
            }
        });


        mHandler = new MeasureHandler(this);


    }


    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it

    }


    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

    private class MeasureHandler extends Handler {
        private final WeakReference<MeasureResultActivity2> mActivity;

        public MeasureHandler(MeasureResultActivity2 activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    // String data = (String) msg.obj;
                    //  mActivity.get().display.append(data);
                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case UsbService.SYNC_READ:


                    byte[] buffer = (byte[]) msg.obj; //"f1de3e44dcb2";//

                    break;
            }
        }

    }


    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }


    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "*************USB READY*************");
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    //  Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    //  Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    //  Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    //   Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    void initSetId() {

        btnHome = (Button) findViewById(R.id.btn_measure_home);
        btnChangeUser = (Button) findViewById(R.id.btn_measure_user_change);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        scrollView = (ScrollView) findViewById(R.id.measure_view_scroll);

        btnTop = (Button) findViewById(R.id.measure_btn_result_move_top);
        btnMid = (Button) findViewById(R.id.measure_btn_result_move_mid);
        btnBot = (Button) findViewById(R.id.measure_btn_result_move_bot);


        btnBalloon = (Button) findViewById(R.id.measure_btn_balloon_grade);
        txtBalloonGrade = (TextView) findViewById(R.id.measure_txt_balloon_grade);


        txtUserName = (TextView) findViewById(R.id.measure_txt_username);
        txtUserAge = (TextView) findViewById(R.id.measure_txt_userage);
        txtUserGender = (TextView) findViewById(R.id.measure_txt_usergender);
        txtUserHeight = (TextView) findViewById(R.id.measure_txt_userheight);
        txtUserWeight = (TextView) findViewById(R.id.measure_txt_userweight);
        txtWorkoutDate = (TextView) findViewById(R.id.measure_txt_workoutdate);
        txtUserGrage = (TextView) findViewById(R.id.measure_txt_usergrade);


        ///////////////////////////////////////////////

        //txtTitle = (TextView) findViewById(R.id.measure_txt_result_selcom);

        //txtGrade = (TextView) findViewById(R.id.measure_txt_result_grade);
        //txtStrong = (TextView) findViewById(R.id.measure_txt_result_strong);

        //barChartBalance = (HorizontalBarChart) findViewById(R.id.measure_barchart_balance);

        txtWorkOutDate[0] = (TextView) findViewById(R.id.measure_txt_workoutdate_0);
        txtWorkOutDate[1] = (TextView) findViewById(R.id.measure_txt_workoutdate_1);
        txtWorkOutDate[2] = (TextView) findViewById(R.id.measure_txt_workoutdate_2);
        txtWorkOutDate[3] = (TextView) findViewById(R.id.measure_txt_workoutdate_3);
        txtWorkOutDate[4] = (TextView) findViewById(R.id.measure_txt_workoutdate_4);
        txtWorkOutDate[5] = (TextView) findViewById(R.id.measure_txt_workoutdate_5);
        txtWorkOutDate[6] = (TextView) findViewById(R.id.measure_txt_workoutdate_6);
        txtWorkOutDate[7] = (TextView) findViewById(R.id.measure_txt_workoutdate_7);
        txtWorkOutDate[8] = (TextView) findViewById(R.id.measure_txt_workoutdate_8);
        txtWorkOutDate[9] = (TextView) findViewById(R.id.measure_txt_workoutdate_9);


        barChart[0] = (HorizontalBarChart) findViewById(R.id.measure_barchart_0);
        barChart[1] = (HorizontalBarChart) findViewById(R.id.measure_barchart_1);
        barChart[2] = (HorizontalBarChart) findViewById(R.id.measure_barchart_2);
        barChart[3] = (HorizontalBarChart) findViewById(R.id.measure_barchart_3);
        barChart[4] = (HorizontalBarChart) findViewById(R.id.measure_barchart_4);
        barChart[5] = (HorizontalBarChart) findViewById(R.id.measure_barchart_5);
        barChart[6] = (HorizontalBarChart) findViewById(R.id.measure_barchart_6);
        barChart[7] = (HorizontalBarChart) findViewById(R.id.measure_barchart_7);
        barChart[8] = (HorizontalBarChart) findViewById(R.id.measure_barchart_8);
        barChart[9] = (HorizontalBarChart) findViewById(R.id.measure_barchart_9);


    }


    private void initGetIntent() {
        intent = getIntent();


        resultValue[0] = intent.getStringArrayExtra("rightPush");
        resultValue[1] = intent.getStringArrayExtra("leftPush");
        resultValue[2] = intent.getStringArrayExtra("rightFull");
        resultValue[3] = intent.getStringArrayExtra("leftFull");
        resultValue[4] = intent.getStringArrayExtra("rightRot");
        resultValue[5] = intent.getStringArrayExtra("leftRot");
        resultValue[6] = intent.getStringArrayExtra("upPush");
        resultValue[7] = intent.getStringArrayExtra("upFull");
        resultValue[8] = intent.getStringArrayExtra("lowPush");
        resultValue[9] = intent.getStringArrayExtra("lowFull");

        //if(oldDate != null && oldDate.length() > 0){
        resultOldValue[0] = intent.getStringArrayExtra("rightOldPush");
        resultOldValue[1] = intent.getStringArrayExtra("leftOldPush");
        resultOldValue[2] = intent.getStringArrayExtra("rightOldFull");
        resultOldValue[3] = intent.getStringArrayExtra("leftOldFull");
        resultOldValue[4] = intent.getStringArrayExtra("rightOldRot");
        resultOldValue[5] = intent.getStringArrayExtra("leftOldRot");

        resultOldValue[6] = intent.getStringArrayExtra("upOldPush");
        resultOldValue[7] = intent.getStringArrayExtra("upOldFull");
        resultOldValue[8] = intent.getStringArrayExtra("lowOldPush");
        resultOldValue[9] = intent.getStringArrayExtra("lowOldFull");


        //for(int i = 0; i < 10; i++){
        //  StringParseUnder2(resultValue[i])
        //}


        //}
/*
        for(int i = 0; i < 4; i++){
            Log.i(TAG, "=========================intent.getStringArrayExtra(leftPush); = " + intent.getStringArrayExtra("leftPush")[i]);
        }

        for(int i = 0; i < 10; i++){
            Log.i(TAG, "=========================resultValue[i][0] = " + resultValue[i][0]);
        }*/
    }


    private void setUserInfo() {
        txtUserName.setText(PublicValues.userName);


        long now = System.currentTimeMillis();

        Date date = new Date(now);
        SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd");
        String timeNow = now_time.format(date);

        txtUserAge.setText("" + PublicValues.userAge + " 세");

        if (PublicValues.userGender.equals("Male")) txtUserGender.setText("남성");
        else txtUserGender.setText("여성");


        String str = "";

        if (PublicValues.userHeight.contains(".")) str = PublicValues.userHeight.substring(0, PublicValues.userHeight.lastIndexOf("."));
        else str = PublicValues.userHeight;

        txtUserHeight.setText(str + " cm");


        if (PublicValues.userWeight.contains(".")) str = PublicValues.userWeight.substring(0, PublicValues.userWeight.lastIndexOf("."));
        else str = PublicValues.userWeight;


        txtUserWeight.setText(str + " kg");

        txtWorkoutDate.setText(timeNow.substring(0, 10));
        txtUserGrage.setText("" + getGrade() + " 등급");
    }


    private void setWorkOutDate() {
        String str = "";

/*        for(int i = 0; i < 10; i++){
            str = lastDate + "\n";
            if(oldDate != null && oldDate.length() > 0) str = str + oldDate;
            else                                        str = str + "-";

            txtWorkOutDate[i].setText(str);
        }
 */

        for (int i = 0; i < 10; i++) {
            str = resultValue[i][4] + "\n" + resultOldValue[i][4];

            txtWorkOutDate[i].setText(str);
        }


    }


    private void setBundleData(int selPage) {

        int selStrong = 0;

        double num1, num2;
        double selPer_1, selPer_2;
        double gapPer;

        String selGrade = "";
        String selStr = "";

        num1 = StringToDouble(resultValue[selPage * 2][0]);
        num2 = StringToDouble(resultValue[(selPage * 2) + 1][0]);

        Log.e("데이터???", num1 + "   " + num2);


        if (num1 > num2) gapPer = (Math.abs(num1 / num2) - 1) * 100;    //((num1/num2) - (num2/num2)) * 100 앱의 구겅에서는 무슨 기준이 있는데 그걸로 점수 뺀건데 그건 보내주신다고함
        else gapPer = (Math.abs(num2 / num1) - 1) * 100;


        if (gapPer < 5) {
            selStrong = 1;
            selGrade = "Good";

        } else {
            if (num1 > num2) selStrong += 2;
            else selStrong += 4;
            selGrade = "Normal";

            if (gapPer >= 15) {
                selStrong += 1;
                selGrade = "Bad";
            }
        }

        if (num1 == 0 && num2 == 0) {
            selStrong = 0;
            selGrade = "-";
        }

        if (selPage < 3) {
            if (num1 < num2) selStr = "우측이 ";
            else selStr = "좌측이 ";
        } else {
            if (num1 < num2) selStr = "전면이 ";
            else selStr = "후면이 ";
        }

        if (num1 == 0 && num2 == 0) selStr += "-% 약함";
        else selStr += String.format("%.1f", gapPer) + "% 약함";

        selPer_1 = num1 / (num1 + num2) * 100;
        selPer_2 = 100 - selPer_1;

        Log.i(TAG, "selPage = " + selPage);
        Log.i(TAG, "selStrong = " + selStrong);
        Log.i(TAG, "selGrade = " + selGrade);
        Log.i(TAG, "selStr = " + selStr);
        Log.i(TAG, "selPer_1 = " + selPer_1);
        Log.i(TAG, "selPer_2 = " + selPer_2);

        putBundleData.putInt("selPage", selPage);
        putBundleData.putInt("selStrong", selStrong);
        putBundleData.putString("selGrade", selGrade);
        putBundleData.putString("selStr", selStr);
        putBundleData.putDouble("selPer_1", selPer_1);
        putBundleData.putDouble("selPer_2", selPer_2);
    }

    private void getData() {

        int selStrong = 0;

        double num1, num2;
        double selPer_1, selPer_2;
        double gapPer;

        String selGrade = "";
        String selStr = "";
        for(int i = 0 ; i < 5 ; i++) {
            num1 = StringToDouble(resultValue[i * 2][0]);
            num2 = StringToDouble(resultValue[(i * 2) + 1][0]);

            Log.e("데이터???", num1 + "   " + num2);


            if (num1 > num2)
                gapPer = (Math.abs(num1 / num2) - 1) * 100;    //((num1/num2) - (num2/num2)) * 100 앱의 구겅에서는 무슨 기준이 있는데 그걸로 점수 뺀건데 그건 보내주신다고함
            else gapPer = (Math.abs(num2 / num1) - 1) * 100;


            if (gapPer < 5) {
                selStrong = 1;
                selGrade = "Good";

            } else {
                if (num1 > num2) selStrong += 2;
                else selStrong += 4;
                selGrade = "Normal";

                if (gapPer >= 15) {
                    selStrong += 1;
                    selGrade = "Bad";
                }
            }

            if (num1 == 0 && num2 == 0) {
                selStrong = 0;
                selGrade = "-";
            }

            if (i < 3) {
                if (num1 < num2) selStr = "우측이 ";
                else selStr = "좌측이 ";
            } else {
                if (num1 < num2) selStr = "전면이 ";
                else selStr = "후면이 ";
            }

            if (num1 == 0 && num2 == 0) selStr += "-% 약함";
            else selStr += String.format("%.1f", gapPer) + "% 약함";

            selPer_1 = num1 / (num1 + num2) * 100;
            selPer_2 = 100 - selPer_1;

            Log.i(TAG, "selPage = " + i);
            Log.i(TAG, "selStrong = " + selStrong);
            Log.i(TAG, "selGrade = " + selGrade);
            Log.i(TAG, "selStr = " + selStr);
            Log.i(TAG, "selPer_1 = " + selPer_1);
            Log.i(TAG, "selPer_2 = " + selPer_2);
            String selName = "";
            if(i == 0)
                selName = "상지 전면 좌우 비교";
            else if(i == 1)
                selName = "상지 후면 좌우 비교";
            else if(i == 2)
                selName = "몸통 좌우 비교";
            else if(i == 3)
                selName = "상지 전후방 비교";
            else if(i == 4)
                selName = "하지 전후방 비교";
            getD[i][0] = selName;
            getD[i][1] = selGrade;
            getD[i][2] = selStr;

            Log.e("받은 데이터", getD[i][0] + "   " + getD[i][1] + "   " + getD[i][2]);

        }
    }

    private void setTotalResult() {

        String str;

        int selPage = viewPager.getCurrentItem();

        //if (selPage == 0) txtTitle.setText("상지 전면 좌우 비교");
        //else if (selPage == 1) txtTitle.setText("상지 후면 좌우 비교");
        //else if (selPage == 2) txtTitle.setText("몸통 좌우 비교");
        //else if (selPage == 3) txtTitle.setText("상지 전후방 비교");
        //else if (selPage == 4) txtTitle.setText("하지 전후방 비교");


        double num1, num2;
        double num1Per, num2Per;

        double gapPer;


        num1 = StringToDouble(resultValue[selPage * 2][0]);
        num2 = StringToDouble(resultValue[(selPage * 2) + 1][0]);


        if (num1 > num2) gapPer = (Math.abs(num1 / num2) - 1) * 100;
        else gapPer = (Math.abs(num2 / num1) - 1) * 100;


        //if (gapPer < 5) txtGrade.setText("Good");
        //else if (gapPer < 15) txtGrade.setText("Normal");
        //else txtGrade.setText("Bad");

        //if (num1 == 0 && num2 == 0) txtGrade.setText("-");


        if (selPage < 3) {
            if (num1 < num2) str = "우측이 ";
            else str = "좌측이 ";
        } else {
            if (num1 < num2) str = "전면이 ";
            else str = "후면이 ";
        }


        if (num1 == 0 && num2 == 0) str += "-% 약함";
        else str += String.format("%.1f", gapPer) + "% 약함";

        //txtStrong.setText(str);


        num1Per = num1 / (num1 + num2) * 100;
        num2Per = 100 - num1Per;

        //setBarchartBalance(num1Per, num2Per);

    }


    private String getGrade() {
        int num = 0;
        String grade = "";

        double num1, num2;
        double gap;

        for (int i = 0; i < 5; i++) {
            num1 = StringToDouble(resultValue[i * 2][0]);
            num2 = StringToDouble(resultValue[(i * 2) + 1][0]);
            Log.e("이거이거",num1 + "   " + num2);

            if (num1 > num2) {
                gap = (Math.abs(num1 / num2) - 1) * 100;    //??뭐지 이건?? 나눗셈하는데 절대값을 왜 뺐고 또 -1은 왜 하는거지?? (num1/num2 - num2/num2<1>) * 100
            } else {
                gap = (Math.abs(num2 / num1) - 1) * 100;    //??뭐지 이건??
            }


            if (num1 == 0 && num2 == 0) {

            } else {
                if (gap < 5) num += 2;
                else if (gap < 15) num += 1;
            }
        }

/*
        if(num == 10)       grade = "A+";
        else if(num == 9)   grade = "A";
        else if(num == 8)   grade = "A-";
        else if(num == 7)   grade = "B+";
        else if(num == 6)   grade = "B";
        else if(num == 5)   grade = "B-";
        else if(num == 4)   grade = "C+";
        else if(num == 3)   grade = "C";
        else if(num == 2)   grade = "C-";
        else if(num == 1)   grade = "D";
        else                grade = "E";
*/


        if (num == 10) grade = "A+";
        else if (num > 7) grade = "A";
        else if (num > 5) grade = "B";
        else if (num > 3) grade = "C";
        else if (num > 1) grade = "D";
        else if (num > 0) grade = "E";
        else grade = "F";


        return grade;
    }


    private void setResultBarChart() {

        float barMax;
        float value, oldValue;
        for (int i = 0; i < 10; i++) {

            //Log.i(TAG, "resultValue[i][0] = " + resultValue[i][0]);

            value = Float.valueOf(resultValue[i][0]);
            //if(oldDate != null && oldDate.length() > 0)     oldValue = Float.valueOf(resultOldValue[i][0]);
            //else                                            oldValue = 0;
            oldValue = Float.valueOf(resultOldValue[i][0]);

            if (Float.valueOf(PublicValues.userWeight) < value || Float.valueOf(PublicValues.userWeight) < oldValue) {
                if (value > oldValue) barMax = (((int) value / 10) * 10) + 20;
                else barMax = (((int) oldValue / 10) * 10) + 20;
            } else {
                barMax = (((int) (Float.valueOf(PublicValues.userWeight) / 10)) * 10) + 10;
            }

            setBarchartResult(barChart[i], value, oldValue, barMax);

            Log.e(TAG, "barMax = " + barMax);
        }

    }


    private int[] getColors() {
        int stacksize = 2;
        // have as many colors as stack-values per entry

        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.LIBERTY_COLORS[i + 3];
        }
        return colors;
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

    private void setBarchartResult(HorizontalBarChart bc, float resultPower, float resultOldPower, float maximumValue) {

        //Log.i(TAG, "setBarchartResult (HorizontalBarChart bc, float resultPower) : " + resultPower);


        ArrayList<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(0, resultOldPower));
        entries.add(new BarEntry(1, resultPower));


        BarDataSet data = new BarDataSet(entries, "");


        if (resultPower < resultOldPower)
            data.setColors(Color.argb(150, 80, 80, 80), Color.rgb(248, 100, 100));
        else data.setColors(Color.argb(150, 80, 80, 80), Color.rgb(0, 230, 248));

        //data.setDrawValues(false);

        ArrayList<IBarDataSet> Barchart_dataSets = new ArrayList<IBarDataSet>();
        Barchart_dataSets.add(data);

        BarData barData = new BarData(Barchart_dataSets);
        barData.setBarWidth(1f);
        barData.setValueTextSize(8f);
        barData.setValueTextColor(Color.rgb(255, 255, 255));


        Description description = bc.getDescription();
        description.setText("");
        description.setTextSize(10);

        Legend legend = bc.getLegend();
        legend.setEnabled(false);

        YAxis leftAxis = bc.getAxisLeft();
        YAxis rightAxis = bc.getAxisRight();
        XAxis xAxis = bc.getXAxis();

        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);


        leftAxis.setDrawLabels(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);

        leftAxis.setAxisMinimum(0);
        leftAxis.setAxisMaximum(maximumValue);//10

        rightAxis.setDrawAxisLine(true);
        rightAxis.setDrawGridLines(true);
        rightAxis.setDrawLabels(false);

        rightAxis.setAxisMinimum(0);
        rightAxis.setAxisMaximum(maximumValue);//10


        bc.setData(barData);
        bc.setFitBars(true); // make the x-axis fit exactly all bars

        bc.setTouchEnabled(false);

        bc.setScaleEnabled(false);
        bc.setDoubleTapToZoomEnabled(false);
        //barChart.setBackgroundColor(Color.rgb(255, 255, 255));
        bc.setDrawBorders(false);
        bc.setDescription(description);
        bc.setDrawValueAboveBar(true);
        bc.setVisibleXRange(0, 2);
        bc.moveViewToX(1);

        bc.invalidate(); // refresh
    }


    double StringToDouble(String str) {
        return Double.valueOf(str);
    }

    String StringParseUnder2(String str) {

        //if(userHeight.contains("."))    str = userHeight.substring(0, userHeight.lastIndexOf("."));
        //else                            str = userHeight;

        return str.substring(0, str.lastIndexOf(".") + 3);
    }

    public class InsertFITTWorkout extends AsyncTask<Void, Integer, Void> {

        String data = "";

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... unused) {


            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            String timeNow = now_time.format(date);

            String param = "";
            String s_url = "";
            Log.e(TAG, PublicValues.userId);
            if(PublicValues.userId.length() > 0){
                s_url = "http://211.253.30.245/php/fitt/fitt_miniplus.php";

                param = "users_id="         + PublicValues.userSecondId +
                        "inputType="        + "test" +
                        "&total_grade="     + getGrade() +
                        "&upperFront_LR="   + getD[0][1] +
                        "&upperRear_LR="    + getD[1][1] +
                        "&body_LR="         + getD[2][1] +
                        "&upper_FrontRear=" + getD[3][1] +
                        "&lower_FrontRear=" + getD[4][1];
            }
            /*
            else{   //이부분은 수정해야 하는데 만들기로 안되어있어서 만들어야 된다면 넣어주자
                s_url = "http://211.253.30.245/equipment/kor/miniplus/Miniplus_addGuestData.php";
                param = "&workout_datetime="    +timeNow +
                        "&weight_up="   + sendWeightUp+
                        "&weight_dn="   + sendWeightDn+
                        "&set_speed="   + sendSetSpeed+
                        "&set_tension=" + sendSetTention+
                        "&set_seq="     + sets+
                        "&reps="        + reps+
                        "&workouttime=" + workTime+
                        "&breaktime="   + breakTime+
                        "&type="        + sendType+
                        "&part="        + sendPart+
                        "&mode="        + sendMode +
                        "&equipment_serial=" + PublicValues.serialNumber +
                        "&club_id=" + PublicValues.club_id;
            }
            */
            Log.e("POST", param);
            try {
                /* 서버연결 */
                URL url = new URL(s_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;


                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                /* 서버에서 응답 */
                Log.e("RECV DATA", data);

                if (data.contains("true")) {

                    Log.e("RESULT", "성공적으로 처리되었습니다!");
                } else {
                    Log.e("RESULT", "에러 발생! ERRCODE =! " + data);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("Error1", e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error2", e.getMessage());
                if (e.getMessage().equals("Failed to connect to /211.253.30.245/php/")) {
                    //Networkchk = 0;
                    //Networkchk_flag = true;
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }

    public class InsertESEWorkout extends AsyncTask<Void, Integer, Void> {

        String data = "";

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... unused) {


            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            String timeNow = now_time.format(date);

            String param = "";
            String s_url = "";
            Log.e(TAG, PublicValues.userId);
            if(PublicValues.userId.length() > 0){
                s_url = "http://211.253.30.245/php/iot/miniplus_iot.php";

                param = "method="           + "test" +
                        "&users_id="        + PublicValues.userSecondId +
                        "&test_datetime="   + timeNow +
                        "&total_grade="     + getGrade() +
                        "&upperFront_LR="   + getD[0][1] +
                        "&upperRear_LR="    + getD[1][1] +
                        "&body_LR="         + getD[2][1] +
                        "&upper_FrontRear=" + getD[3][1] +
                        "&lower_FrontRear=" + getD[4][1];
            }
            /*
            else{   //이부분은 수정해야 하는데 만들기로 안되어있어서 만들어야 된다면 넣어주자
                s_url = "http://211.253.30.245/equipment/kor/miniplus/Miniplus_addGuestData.php";
                param = "&workout_datetime="    +timeNow +
                        "&weight_up="   + sendWeightUp+
                        "&weight_dn="   + sendWeightDn+
                        "&set_speed="   + sendSetSpeed+
                        "&set_tension=" + sendSetTention+
                        "&set_seq="     + sets+
                        "&reps="        + reps+
                        "&workouttime=" + workTime+
                        "&breaktime="   + breakTime+
                        "&type="        + sendType+
                        "&part="        + sendPart+
                        "&mode="        + sendMode +
                        "&equipment_serial=" + PublicValues.serialNumber +
                        "&club_id=" + PublicValues.club_id;
            }
            */
            Log.e("POST", param);
            try {
                /* 서버연결 */
                URL url = new URL(s_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;


                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                /* 서버에서 응답 */
                Log.e("RECV DATA!@#", data);

                if (data.contains("true")) {

                    Log.e("RESULT", "성공적으로 처리되었습니다!");
                } else {
                    Log.e("RESULT", "에러 발생! ERRCODE = " + data);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("Error1", e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error2", e.getMessage());
                if (e.getMessage().equals("Failed to connect to /211.253.30.245/php/")) {
                    //Networkchk = 0;
                    //Networkchk_flag = true;
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            InsertFITTWorkout insertFITTWorkout = new InsertFITTWorkout();
            insertFITTWorkout.execute();
        }
    }

}
