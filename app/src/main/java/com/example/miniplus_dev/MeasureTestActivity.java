package com.example.miniplus_dev;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniplus_dev.setting.MeasureGuideFrag;
import com.example.miniplus_dev.setting.MeasureResultFrag;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MeasureTestActivity extends Activity implements MeasureResultFrag.OnTimePickerSetListener {

    CRC16Modbus crc = new CRC16Modbus();

    private final String TAG = MeasureTestActivity.class.getSimpleName();

    private final String MOTER_START_SERIAL = "F2010FA1000000000000000000000000000001FE";
    private final String MOTER_STOP_SERIAL = "F2010FA2000000000000000000000000000001FE";
    private final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    public static boolean fragChangeFlag = false;


    MeasureGuideFrag guideFrag;
    MeasureResultFrag resultFrag;
    FragmentManager fm;
    FragmentTransaction tran;

    Bundle putBundleData;

    LinearLayout barchartBgAble, barchartBgUnAble, layoutMuscular;

    TextView txtGuide;

    ImageView[] bgSelTest = new ImageView[7];

    Button btnStart, btnBack, btnMode, btnResult, btnChangeUser, btn_pause;
    Button[] btnSelTest = new Button[7];


    Intent intent;


    private UsbService usbService;
    private MeasureHandler mHandler;
    private StringBuffer packetBufStr;

    MeasureCountDialog countDialog = new MeasureCountDialog(this);
    MeasureCheckDialog checkDialog = new MeasureCheckDialog(this);


    //=======================================그래프=======================================//
    int posRomMin = 9999;
    int posRomMax = 0;

    //그래프/////
    ArrayList<Entry> entriesForce = new ArrayList<>();

    LineChart lineChart;
    Description lineDescription;
    Legend lineLegend;

    double lineChartPos;


    //그래프/////
    ArrayList<BarEntry> entries = new ArrayList<>();
    List<Double> barchartRepsPower = new ArrayList<Double>();
    List<Double> barchartRepsVel = new ArrayList<Double>();


    ArrayList<BarEntry> entriesLeft = new ArrayList<>();

    ArrayList [] arrL_barchartEntrys = new ArrayList[10];


    HorizontalBarChart barChart;
    Description description;
    Legend legend;

    int xEntry = 0;

    int barChartXindex = 0;

    ArrayList[] arr_forceList = new ArrayList[10];

    ArrayList<float[]> ForceDataList = new ArrayList<>();

    float barAvrRealVel = 0;
    float barAvrRealPower = 0;

    //=======================================aaa=======================================//


    boolean [] bool_measureComple = new boolean[7];
    boolean measureChecked = false;
    boolean no_internet = false;

    String[][][] testResultValue = new String[10][3][6];   //테스트 값
    String[][] sendValue = new String[3][6];   //DB전송 값


    TimerTask mTimeCal;
    Timer timeCalDisp;

    byte errorBit8 = 0x00;
    byte statusBit8 = 0x00;
    byte mcActBit8 = 0x00;
    byte workoutBit8 = 0x00;

    String str_setWeightHex = "00";
    String str_setSpeedHex = "00";


    List<Double> avrForce = new ArrayList<Double>();    //1reps중 평균포스를 구하기위한 리스트
    List<Double> avrSpeed = new ArrayList<Double>();    //1reps중 평균속도를 구하기위한 리스트
    List<Double> avrPower = new ArrayList<Double>();    //1reps중 평균파워를 구하기위한 리스트


    PublicFunctions functionBundle = new PublicFunctions();

    String testCode;

    //int stepNum, stepNumOld;
    //int stepNumTmp;
    //int selNum;
    int i_measureSelector = 0;
    int i_machineStopCnt = 0;

    int reps;


    int flowTimeS = 30;
    int flowTimeE = 50;

    int i_setWeight;
    int i_setSpeed = 600;
    int i_changeSetWeight = 500;
    int i_changeSetSpeed = 1000;


    double maxForce = 0;    //1reps중 최대포스
    double maxSpeed = 0;    //1reps중 최대속도
    double maxPower = 0;    //1reps중 최대파워

    int setData, setDataVel;
    double posReal;
    double velReal;
    double loadReal;
    double powerReal;


    double calAvrForce;
    double calAvrSpeed;
    double calAvrPower;


    int i_returnSpeed = 1000;



    boolean avrFlag = false;

    boolean startFlag = false;
    boolean measureFlag = false;

    boolean compleFlag = false;


    boolean sendFlag = false;

    boolean funcLR = false;


    boolean RGFlag = false;

    boolean breakStatus = false; // false = 브레이크OFF, true = 브레이크ON


    boolean measureRestartFlag = false; //true = 재측정 진행중


    boolean modeFlag = false;   //




    ArrayList<Double> mList_forceDataSet = new ArrayList<Double>();
    ArrayList<Double> mList_speedDataSet = new ArrayList<Double>();
    ArrayList<Double> mList_posDataSet = new ArrayList<Double>();

    ArrayList<List>  mList_forceValues = new ArrayList<>();
    ArrayList<List>  mList_speedValues = new ArrayList<>();
    ArrayList<List>  mList_posValues = new ArrayList<>();

    @Override
    public void onTimePickerSet(boolean LR_Button) {
        //barChartXindex = 0;
        barChart.highlightValue(null);
        lineChart.invalidate();
        lineChart.clear();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_measure_test);

        initDefine();

        hideSystemUI();
        Log.i(TAG, "OnCreate");



        /*================================Wifi connect================================*/
        final WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        if (wifiManager.isWifiEnabled() == false) {
            wifiManager.setWifiEnabled(true);
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_COARSE_LOCATION);
        }
        //IMarker customMarker = new CustomMarker(this);
        //barChart.setMarker(customMarker); //마커 필요하면 넣어라
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float x = e.getX();

                barChartXindex = (int) x;
                if (startFlag == false) {
                    if (i_measureSelector < 3 && !resultFrag.selResultFlag) {
                        selBarchartToLinechart(barChartXindex, arr_forceList[i_measureSelector + 7]);
                    } else selBarchartToLinechart(barChartXindex, arr_forceList[i_measureSelector]);
                } else
                    Toast.makeText(MeasureTestActivity.this, "운동종료 후 눌러주세요", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
                lineChart.invalidate();
                lineChart.clear();
            }
        });


        btnSelTest[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!startFlag) {
                    i_measureSelector = 0;
                    setFrag(0);
                    lineChart.invalidate();
                    lineChart.clear();
                    barChart.highlightValue(null);
                    barChart.clear();
                } else
                    Toast.makeText(MeasureTestActivity.this, "측정을 종료한 뒤 눌러주세요.", Toast.LENGTH_SHORT).show();

            }
        });
        btnSelTest[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!startFlag) {
                    i_measureSelector = 1;
                    setFrag(1);
                    lineChart.invalidate();
                    lineChart.clear();
                    barChart.highlightValue(null);
                    barChart.clear();
                } else
                    Toast.makeText(MeasureTestActivity.this, "측정을 종료한 뒤 눌러주세요.", Toast.LENGTH_SHORT).show();


            }
        });
        btnSelTest[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!startFlag) {
                    i_measureSelector = 2;
                    setFrag(2);
                    lineChart.invalidate();
                    lineChart.clear();
                    barChart.highlightValue(null);
                    barChart.clear();
                } else
                    Toast.makeText(MeasureTestActivity.this, "측정을 종료한 뒤 눌러주세요.", Toast.LENGTH_SHORT).show();
            }
        });
        btnSelTest[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!startFlag) {
                    i_measureSelector = 3;
                    setFrag(3);
                    lineChart.invalidate();
                    lineChart.clear();
                    barChart.highlightValue(null);
                    barChart.clear();
                } else
                    Toast.makeText(MeasureTestActivity.this, "측정을 종료한 뒤 눌러주세요.", Toast.LENGTH_SHORT).show();

            }
        });
        btnSelTest[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!startFlag) {
                    i_measureSelector = 4;
                    setFrag(4);
                    lineChart.invalidate();
                    lineChart.clear();
                    barChart.highlightValue(null);
                    barChart.clear();
                } else
                    Toast.makeText(MeasureTestActivity.this, "측정을 종료한 뒤 눌러주세요.", Toast.LENGTH_SHORT).show();


            }
        });
        btnSelTest[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!startFlag) {
                    i_measureSelector = 5;
                    setFrag(5);
                    lineChart.invalidate();
                    lineChart.clear();
                    barChart.highlightValue(null);
                    barChart.clear();
                } else
                    Toast.makeText(MeasureTestActivity.this, "측정을 종료한 뒤 눌러주세요.", Toast.LENGTH_SHORT).show();


            }
        });
        btnSelTest[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!startFlag) {
                    i_measureSelector = 6;
                    setFrag(6);
                    lineChart.invalidate();
                    lineChart.clear();
                    barChart.highlightValue(null);
                    barChart.clear();
                } else
                    Toast.makeText(MeasureTestActivity.this, "측정을 종료한 뒤 눌러주세요.", Toast.LENGTH_SHORT).show();

            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mB_mcActBit8 != 0x01 || posReal < 5) {
                    Intent intent = new Intent(MeasureTestActivity.this, MainActivity.class);


                    if (timeCalDisp != null) timeCalDisp.cancel();
                    timeCalDisp = null;

                    startActivity(intent);
                    finishAffinity();
                } else
                    Toast.makeText(MeasureTestActivity.this, "장비를 조작하지 않은 상태로 눌러주세요.", Toast.LENGTH_SHORT).show();
            }
        });


        btnMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modeFlag) {
                    modeFlag = false;
                    btnMode.setBackgroundResource(R.drawable.measure_btn_mode_two);
                } else {
                    modeFlag = true;
                    btnMode.setBackgroundResource(R.drawable.measure_btn_mode_base);
                }
            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mB_mcActBit8 != 0x01) {
                    if (!startFlag) {
                        checkDialog.DialogStart(2);
                        GetData("http://211.253.30.245/backend/index.php/MiniplusTest/getTestData?users_id=" + PublicValues.userId);
                    } else
                        Toast.makeText(MeasureTestActivity.this, "측정을 완료한 뒤 눌러주세요.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(MeasureTestActivity.this, "장비를 조작하지 않은 상태로 눌러주세요.", Toast.LENGTH_SHORT).show();
            }
        });


        btnChangeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mB_mcActBit8 != 0x01) {
                    if (!startFlag) {
                        Intent intent = new Intent(MeasureTestActivity.this, MeasureLoginPadActivity.class);


                        intent.putExtra("whereFrom", "MeasureTestActivity");

                        if (timeCalDisp != null) timeCalDisp.cancel();
                        timeCalDisp = null;

                        startActivity(intent);
                        finishAffinity();
                    } else
                        Toast.makeText(MeasureTestActivity.this, "측정을 완료한 뒤 눌러주세요.", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(MeasureTestActivity.this, "장비를 조작하지 않은 상태로 눌러주세요.", Toast.LENGTH_SHORT).show();


            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder Dialog = new AlertDialog.Builder(MeasureTestActivity.this);
                Dialog.setCancelable(false);
                Dialog.setTitle("PAUSE");
                Dialog.setMessage("측정을 중단 하시겠습니까?");
                Dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnStart.setVisibility(View.VISIBLE);
                        btn_pause.setVisibility(View.GONE);
                        startFlag = false;
                        measureFlag = false;
                        funcLR = false;
                        reps = 0;
                        lineChart.clear();
                        setFrag(i_measureSelector);
                        hideSystemUI();
                    }
                });
                Dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideSystemUI();
                    }
                }).create().show();


            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (!startFlag) {
                    startFlag = true;
                    funcLR = false;

                    if(bool_measureComple[i_measureSelector]){
                        bool_measureComple[i_measureSelector] = false;
                        setFrag(i_measureSelector);
                    }

                    entries.clear();
                    entriesLeft.clear();
                    lineChart.clear();
                    entriesForce.clear();

                    xEntry = 0;

                    if (i_measureSelector < 3) countDialog.initDialogStart(1);
                    else countDialog.initDialogStart(0);

                    //이부분만 수정하면 데이터가 커스텀 가능할듯
                    if (i_measureSelector < 3) {    //0,1,2
                        i_measureRange = 6;
                        i_setWeight = (int) (Double.valueOf(PublicValues.userWeight) * 40);

                        if (PublicValues.userGender.equals("Male")) i_setSpeed = 250;
                        else i_setSpeed = 350;
                    } else {    //3,4,5,6
                        if (i_measureSelector < 5) {    //3,4
                            i_measureRange = 12;
                            i_setWeight = (int) (Double.valueOf(PublicValues.userWeight) * 20);
                            if (PublicValues.userGender.equals("Male")) i_setSpeed = 300;
                            else i_setSpeed = 450;
                        } else {    //5,6
                            if (modeFlag) {
                                i_measureRange = 10;
                                i_setWeight = (int) (Double.valueOf(PublicValues.userWeight) * 30);
                                if (PublicValues.userGender.equals("Male")) i_setSpeed = 600;
                                else i_setSpeed = 900;
                            } else {
                                i_measureRange = 15;
                                i_setWeight = (int) (Double.valueOf(PublicValues.userWeight) * 60);
                                if (PublicValues.userGender.equals("Male")) i_setSpeed = 300;
                                else i_setSpeed = 450;
                            }

                        }
                    }
                    if (i_setWeight < 500) i_setWeight = 500;
                    str_setWeightHex = functionBundle.intToByte(i_setWeight);
                    str_setSpeedHex = functionBundle.intToByte(i_setSpeed);


                    if (i_measureSelector < 3) {    //0,1,2
                        flowTimeS = 2;
                        flowTimeE = 10;
                    }
                    else if(i_measureSelector < 5){ //3,4
                        flowTimeS = 2;
                        flowTimeE = 10;
                    }
                    else{   //5,6
                        flowTimeS = 5;
                        flowTimeE = 4;
                    }

                    i_returnSpeed = i_setSpeed + 100;
                    btnStart.setVisibility(View.GONE);
                    btn_pause.setVisibility(View.VISIBLE);

                } else
                    Toast.makeText(MeasureTestActivity.this, "측정을 종료한 뒤 눌러주세요.", Toast.LENGTH_SHORT).show();
            }
        });




        mTimeCal = new TimerTask() {
            @Override
            public void run() {
                MeasureTestActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (countDialog.DialogClosed) {   //countDilog가 켜진뒤 꺼졌을 때
                            measureFlag = true;         //측정 시작
                            txtGuide.setText("검사 진행중...");

                            if (!guideFrag.videoFlag) guideFrag.videoStart();

                            entries.clear();
                            xEntry = 0;

                            barchartBgAble.setVisibility(View.VISIBLE);
                            barchartBgUnAble.setVisibility(View.GONE);//0406
                            countDialog.DialogClosed = false;
                        }


                        if (!sendFlag && compleFlag) {                                //측정이 종료 && 데이터 전송을 실핼하지 않음
                            insertWorkoutData workdata = new insertWorkoutData();   //데이터 전송
                            workdata.execute();
                            sendFlag = true;
                        }


                        if(startFlag){

                        }
                        else{
                            txtGuide.setText("총 7가지의 검사항목이 존재하며\n화면의 영상을 참고하여 각 항목별\n검사를 5회씩 측청하여 주세요.");
                            setBtnMeasureComple();
                            if(btnStart.getVisibility() != View.VISIBLE){
                                btnStart.setVisibility(View.VISIBLE);
                                btn_pause.setVisibility(View.GONE);
                            }
                            if(measureChecked) {
                                measureChecked = false;
                                setFrag(i_measureSelector);
                            }
                        }


                        if (RGFlag) {
                            setDataRL(resultFrag.selResultFlag);
                        }

                        //무게 변환
                        if (posReal > 2 && startFlag && ((Math.abs(setData - i_setWeight) > 10) || (Math.abs(setDataVel - i_setSpeed) > 10))) { //운동 하면서 줄의 위치가 나가 있을땐

                            if (i_changeSetWeight < i_setWeight)        i_changeSetWeight += ((i_setWeight - 500) / flowTimeS)/4;   //무게 늘리는 속도(flowTimeS는 속도 늘이는 계수)   //무조건 500에서 시작함
                            if (i_changeSetWeight > i_setWeight)        i_changeSetWeight = i_setWeight;
                            if (i_changeSetWeight > i_setWeight-100)    i_changeSetWeight = i_setWeight;

                            str_setWeightHex = functionBundle.intToByte(i_changeSetWeight);
                            str_setSpeedHex = functionBundle.intToByte(i_setSpeed); //초기 스타트에 정해진 속도로 간다.
                            ControlBoard("F2010F0301" + str_setWeightHex + str_setSpeedHex + "00000000000000000000FE");
                        }
                        else if (posReal > 2 && !startFlag && ((Math.abs(setData - 500) > 10) || (Math.abs(setDataVel - i_returnSpeed) > 10))) {    //운동끝난후 줄의 위치가 나가 있을때

                            if (i_changeSetWeight > 500)    i_changeSetWeight -= ((i_setWeight - 500) / flowTimeE)/4;   //무게 줄이는 속도(flowTimeE는 속도 줄이는 계수)
                            if (i_changeSetWeight < 600)    i_changeSetWeight = 500;
                            if (i_changeSetSpeed < i_returnSpeed)     i_changeSetSpeed += ((i_returnSpeed - i_setSpeed) / flowTimeE)/4; //아마도 운동 한개를 끝내고 손잡이를 뺀 상태로 유지한 상태로 다음 운동을 시작 했을때를 대비 한것 같다.(즉, 1500의 속도가 안들어가고 이전꺼 그대로 갈때)
                            if (i_changeSetSpeed > i_returnSpeed)     i_changeSetSpeed = i_returnSpeed; //(보통 처음에는 1000이고 그다음 부터 1500으로 시작) 현재 속도 보다 10cm/s 더 빠르게 설정 해서 갑자기 속도가 확늘어나지 않도록 한다.(운동 5번 다 당기고 한번더 당길때 위험하지 않도록 속도를 서서히 늘림)
                            str_setWeightHex = functionBundle.intToByte(i_changeSetWeight);
                            str_setSpeedHex = functionBundle.intToByte(i_changeSetSpeed);   //이거를 그냥 i_changeSetSpeed 대신 i_returnSpeed를 넣어도 문제는 없을거 같다.
                            ControlBoard("F2010F0301" + str_setWeightHex + str_setSpeedHex + "00000000000000000000FE");
                        }
                        else if(!measureFlag && i_machineStopCnt > 12 && setData > 500 && setDataVel < 1000){   //기계 안 움직인지 꽤 되었고 줄 안나가 있으니깐 그냥 속도 줄이는 듯
                            i_changeSetWeight = 500;
                            i_changeSetSpeed = 1500;
                            str_setWeightHex = functionBundle.intToByte(500);
                            str_setSpeedHex = functionBundle.intToByte(1500);
                            ControlBoard("F2010F0301" + str_setWeightHex + str_setSpeedHex + "00000000000000000000FE");
                        }

                        //if ((d_velReal >= -1 && d_velReal <= 2 && d_posReal <= 5) || (mcActBit8 == 0x02 && d_posReal <= 5)) {//기계동작 없음
                        if (velReal >= -1 && velReal <= 1 && posReal <= 2) {//기계동작 없음
                            if(i_machineStopCnt < 15)    i_machineStopCnt++;
                        }
                        else    i_machineStopCnt = 0;

                    }
                });
            }
        };

        timeCalDisp = new Timer();
        timeCalDisp.schedule(mTimeCal, 0, 250);


    }

    private void setBtnMeasureComple(){
        if(bool_measureComple[0])  btnSelTest[0].setBackgroundResource(R.drawable.measure_btn_unable_1);
        else                    btnSelTest[0].setBackgroundResource(R.drawable.measure_btn_press_1);
        if(bool_measureComple[1])  btnSelTest[1].setBackgroundResource(R.drawable.measure_btn_unable_2);
        else                    btnSelTest[1].setBackgroundResource(R.drawable.measure_btn_press_2);
        if(bool_measureComple[2])  btnSelTest[2].setBackgroundResource(R.drawable.measure_btn_unable_3);
        else                    btnSelTest[2].setBackgroundResource(R.drawable.measure_btn_press_3);
        if(bool_measureComple[3])  btnSelTest[3].setBackgroundResource(R.drawable.measure_btn_unable_4);
        else                    btnSelTest[3].setBackgroundResource(R.drawable.measure_btn_press_4);
        if(bool_measureComple[4])  btnSelTest[4].setBackgroundResource(R.drawable.measure_btn_unable_5);
        else                    btnSelTest[4].setBackgroundResource(R.drawable.measure_btn_press_5);
        if(bool_measureComple[5])  btnSelTest[5].setBackgroundResource(R.drawable.measure_btn_unable_6);
        else                    btnSelTest[5].setBackgroundResource(R.drawable.measure_btn_press_6);
        if(bool_measureComple[6])  btnSelTest[6].setBackgroundResource(R.drawable.measure_btn_unable_7);
        else                    btnSelTest[6].setBackgroundResource(R.drawable.measure_btn_press_7);




    }




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

    private void initDefine() {
        //videoView = (VideoView) findViewById(R.id.measure_videoview);

        barchartBgAble = (LinearLayout) findViewById(R.id.measure_bg_barchart_layout_able);
        barchartBgUnAble = (LinearLayout) findViewById(R.id.measure_bg_barchart_layout_unable);

        layoutMuscular = (LinearLayout) findViewById(R.id.measure_layout_title_muscular);


        txtGuide = (TextView) findViewById(R.id.txt_measure_guide);

        bgSelTest[0] = (ImageView) findViewById(R.id.measure_bg_select_1);
        bgSelTest[1] = (ImageView) findViewById(R.id.measure_bg_select_2);
        bgSelTest[2] = (ImageView) findViewById(R.id.measure_bg_select_3);
        bgSelTest[3] = (ImageView) findViewById(R.id.measure_bg_select_4);
        bgSelTest[4] = (ImageView) findViewById(R.id.measure_bg_select_5);
        bgSelTest[5] = (ImageView) findViewById(R.id.measure_bg_select_6);
        bgSelTest[6] = (ImageView) findViewById(R.id.measure_bg_select_7);


        btnSelTest[0] = (Button) findViewById(R.id.measure_btn_1);
        btnSelTest[1] = (Button) findViewById(R.id.measure_btn_2);
        btnSelTest[2] = (Button) findViewById(R.id.measure_btn_3);
        btnSelTest[3] = (Button) findViewById(R.id.measure_btn_4);
        btnSelTest[4] = (Button) findViewById(R.id.measure_btn_5);
        btnSelTest[5] = (Button) findViewById(R.id.measure_btn_6);
        btnSelTest[6] = (Button) findViewById(R.id.measure_btn_7);


        btnStart = (Button) findViewById(R.id.measure_btn_start);
        btnBack = (Button) findViewById(R.id.measure_btn_back);
        btnMode = (Button) findViewById(R.id.measure_btn_mode);
        btnResult = (Button) findViewById(R.id.measure_btn_totalresult);
        btn_pause   = (Button) findViewById(R.id.btn_pause);

        btnChangeUser = (Button) findViewById(R.id.btn_measure_user_change);

        barChart = (HorizontalBarChart) findViewById(R.id.measure_barchart);
        lineChart = (LineChart) findViewById(R.id.measure_linechart);
        barChart.setNoDataText("");
        lineChart.setNoDataText("");
        initLineChart();

        btnStart.setVisibility(View.VISIBLE);
        btn_pause.setVisibility(View.GONE);

        mHandler = new MeasureHandler(this);
        packetBufStr = new StringBuffer();


        guideFrag = new MeasureGuideFrag();
        resultFrag = new MeasureResultFrag();

        putBundleData = new Bundle();

        testCode = "PUSH001";

        i_setWeight = (int) (Double.valueOf(PublicValues.userWeight) * 40);
        if (PublicValues.userGender.equals("Male")) i_setSpeed = 250;
        else i_setSpeed = 350;


        str_setWeightHex = functionBundle.intToByte(i_setWeight);
        str_setSpeedHex = functionBundle.intToByte(i_setSpeed);


        setFrag(0);
    }


    private void initSet() {
        startFlag = false;
        measureFlag = false;
    }
    byte mB_mcActBit8 = 0x00;

    int i_posCnt = 0;

    double d_minPos = 0;
    double d_maxPos = 0;
    int i_measureRange = 10;


    private class MeasureHandler extends Handler {
        private final WeakReference<MeasureTestActivity> measureActivity;

        private MeasureHandler(MeasureTestActivity activity) {
            measureActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    break;
                case UsbService.SYNC_READ:
                    byte[] buffer = (byte[]) msg.obj;
                    packetBufStr.append(PacketbyteArrayToHexString(buffer));
                    if (packetBufStr.toString().contains("4E4E4E")) {
                        packetBufStr.setLength(0);
                        Log.e(TAG, "ControlBoard Connect Missing!!!");
                    }

                    if (packetBufStr.length() >= 84) {
                        if (packetBufStr.indexOf("F10124") >= 0 && packetBufStr.lastIndexOf("FE") >= 0 && packetBufStr.lastIndexOf("FE") - packetBufStr.indexOf("F10124") > 84) {

                            int startIndex = packetBufStr.indexOf("F10124");
                            int endIndex = packetBufStr.indexOf("FE", startIndex + 78);

                            if (endIndex - startIndex == 82) {

                                byte[] Bytebuf = hexStringToByteArray(packetBufStr.substring(startIndex, endIndex + 2));
                                byte[] RecvChkSum = FV_RecvChkSum(Bytebuf);

                                if (Bytebuf[39] == RecvChkSum[1] && Bytebuf[40] == RecvChkSum[0]) {

                                    if ((byte) Bytebuf[0] == -15 && (byte) Bytebuf[1] == 1) {

                                        measureActivity.get().setData = ((((int) Bytebuf[14]) << 8) | ((int) Bytebuf[15] & 0xff));
                                        measureActivity.get().setDataVel = ((((int) Bytebuf[16]) << 8) | ((int) Bytebuf[17] & 0xff));
                                        int posData = ((((int) Bytebuf[5]) << 8) | ((int) Bytebuf[6] & 0xff));
                                        int velData = ((((int) Bytebuf[7]) << 8) | ((int) Bytebuf[8] & 0xff));
                                        int loadData = ((((int) Bytebuf[9]) << 8) | ((int) Bytebuf[10] & 0xff));

                                        measureActivity.get().mB_mcActBit8 = (byte) Bytebuf[32];

                                        measureActivity.get().posReal = (double) (posData * -1) / 10.0;
                                        measureActivity.get().velReal = (double) (velData * -1) / 1000.0;
                                        measureActivity.get().loadReal = (double) loadData / 100.0;
                                        measureActivity.get().powerReal = measureActivity.get().loadReal * measureActivity.get().velReal;

                                        if (i_measureSelector < 3) {
                                            velReal = velReal * 4;
                                            loadReal = loadReal / 4;
                                            posReal = posReal * 4;
                                        } else if (i_measureSelector == 2) {    //이건 안들어가질껄 //이건 한번더 확인 해보기
                                            if(!modeFlag){
                                                velReal = velReal * 2;
                                                loadReal = loadReal / 2;
                                                posReal = posReal * 2;
                                            }
                                        }



                                        if (measureActivity.get().measureFlag) {

                                            measureActivity.get().lineChartPos = posReal;

                                            if(measureActivity.get().d_minPos > posReal){
                                                measureActivity.get().d_minPos = posReal;
                                            }
                                            if(measureActivity.get().d_maxPos < posReal){
                                                measureActivity.get().d_maxPos = posReal;
                                            }



                                            if (measureActivity.get().velReal > 0.02 && posReal > 2) {//측정조건

                                                measureActivity.get().avrFlag = false;

                                                i_posCnt++;

                                                //Max값 저장 구문//*************************************
                                                if (measureActivity.get().maxForce < loadReal) {   //maxForce 재설정
                                                    measureActivity.get().maxForce = loadReal;
                                                }
                                                if (measureActivity.get().maxSpeed < velReal) {   //maxSpeed 재설정
                                                    measureActivity.get().maxSpeed = velReal;
                                                }
                                                if (measureActivity.get().maxPower < powerReal) {   //maxPower 재설정
                                                    measureActivity.get().maxPower = powerReal;
                                                }
                                                //End//*************************************
                                                //Avr값 저장 구문//*************************************
                                                if (velReal > 0.02) {
                                                    measureActivity.get().avrForce.add(loadReal);
                                                    measureActivity.get().avrSpeed.add(velReal);
                                                    measureActivity.get().avrPower.add(powerReal);
                                                    measureActivity.get().barchartRepsPower.add(powerReal); //막대그래프용
                                                    measureActivity.get().barchartRepsVel.add(velReal * 100); //막대그래프용
                                                }

                                                if (measureActivity.get().posRomMin > (int) lineChartPos)
                                                    measureActivity.get().posRomMin = (int) lineChartPos;
                                                if (measureActivity.get().posRomMax < (int) lineChartPos)
                                                    measureActivity.get().posRomMax = (int) lineChartPos;
                                                if (measureActivity.get().lineChart != null) {

                                                    measureActivity.get().ForceDataList.add(new float[]{(float) measureActivity.get().reps, (float) measureActivity.get().lineChartPos, (float) loadReal});


                                                    measureActivity.get().entriesForce.add(new Entry((float) measureActivity.get().lineChartPos, (float) loadReal)); //N=> kgf

                                                    LinesetData();
                                                }

                                                measureActivity.get().mList_forceDataSet.add(loadReal);
                                                measureActivity.get().mList_speedDataSet.add(velReal);
                                                measureActivity.get().mList_posDataSet.add(posReal);
                                                //End//*************************************
                                            }


                                            //Avr값 계산 구문//*************************************
                                            else if (measureActivity.get().velReal < -0.02 && posReal > 2 && !measureActivity.get().avrFlag && measureActivity.get().i_posCnt > 7 && (measureActivity.get().d_maxPos - measureActivity.get().d_minPos) > measureActivity.get().i_measureRange) {
                                            //마지막 if는 가용범위가 정해진거 이상 나왔을때 계산한다는 것
                                                measureActivity.get().d_minPos = 0;
                                                measureActivity.get().d_maxPos = 0;

                                                measureActivity.get().entriesForce.clear(); //선 그릴 데이터 지움
                                                lineChart.clear();
                                                measureActivity.get().i_posCnt = 0;
                                                measureActivity.get().avrFlag = true;
                                                measureActivity.get().reps++;



                                                measureActivity.get().calAvrForce = measureActivity.get().calculateAverage(measureActivity.get().avrForce);
                                                measureActivity.get().calAvrSpeed = measureActivity.get().calculateAverage(measureActivity.get().avrSpeed);
                                                measureActivity.get().calAvrPower = measureActivity.get().calculateAverage(measureActivity.get().avrPower);

                                                measureActivity.get().barAvrRealVel = (float) (measureActivity.get().calculateAverage(measureActivity.get().barchartRepsVel));
                                                measureActivity.get().barAvrRealPower = (float) (measureActivity.get().calculateAverage(measureActivity.get().barchartRepsPower));

                                                /*Log.i(TAG, "calAvrForce = " + calAvrForce);
                                                Log.i(TAG, "calAvrSpeed = " + calAvrSpeed);
                                                Log.i(TAG, "calAvrPower = " + calAvrPower);

                                                Log.i(TAG, "barAvrRealVel = " + barAvrRealVel);
                                                Log.i(TAG, "barAvrRealPower = " + barAvrRealPower);*/



                                                if (measureActivity.get().funcLR) {
                                                    measureActivity.get().entriesLeft.add(new BarEntry(xEntry, new float[]{barAvrRealVel, barAvrRealPower}));
                                                }
                                                else {
                                                    measureActivity.get().entries.add(new BarEntry(xEntry, new float[]{barAvrRealVel, barAvrRealPower}));
                                                }

                                                setData();
                                                measureActivity.get().xEntry++;


                                                int index = measureActivity.get().reps - 3;
                                                if (index >= 0 && measureActivity.get().reps < 6) { //3번째 데이터 부터 넣어준다.
                                                    measureActivity.get().sendValue[index][0] = String.format("%.2f", measureActivity.get().calAvrForce);
                                                    measureActivity.get().sendValue[index][1] = String.format("%.2f", measureActivity.get().maxForce);
                                                    measureActivity.get().sendValue[index][2] = String.format("%.2f", measureActivity.get().calAvrPower);
                                                    measureActivity.get().sendValue[index][3] = String.format("%.2f", measureActivity.get().maxPower);
                                                    measureActivity.get().sendValue[index][4] = String.format("%.4f", measureActivity.get().calAvrSpeed);
                                                    measureActivity.get().sendValue[index][5] = String.format("%.4f", measureActivity.get().maxSpeed);

                                                    mList_forceValues.add((ArrayList) mList_forceDataSet.clone());
                                                    mList_speedValues.add((ArrayList) mList_speedDataSet.clone());
                                                    mList_posValues.add((ArrayList) mList_posDataSet.clone());
                                                }


                                                measureActivity.get().mList_forceDataSet.clear();
                                                measureActivity.get().mList_speedDataSet.clear();
                                                measureActivity.get().mList_posDataSet.clear();


                                                measureActivity.get().maxForce = 0;
                                                measureActivity.get().maxSpeed = 0;
                                                measureActivity.get().maxPower = 0;
                                                measureActivity.get().barchartRepsPower.clear();
                                                measureActivity.get().barchartRepsVel.clear();
                                                measureActivity.get().avrForce.clear();
                                                measureActivity.get().avrSpeed.clear();
                                                measureActivity.get().avrPower.clear();
                                            }
                                            //End//*************************************


//측정종료/////////////////////////////////////////////////
                                            if (measureActivity.get().reps > 4) {
                                                measureActivity.get().reps = 0;

                                                if (measureActivity.get().i_measureSelector > 2) {

                                                    measureActivity.get().arr_forceList[i_measureSelector] = new ArrayList(ForceDataList);
                                                    measureActivity.get().arrL_barchartEntrys[i_measureSelector] = new ArrayList(entries);

                                                    measureActivity.get().startFlag = false;
                                                    measureActivity.get().measureFlag = false;

                                                    measureActivity.get().bool_measureComple[i_measureSelector] = true;
                                                    measureActivity.get().checkDialog.DialogStart(0);

                                                } else {
                                                    if (!measureActivity.get().funcLR) {
                                                        measureActivity.get().arr_forceList[i_measureSelector] = new ArrayList(ForceDataList);
                                                        measureActivity.get().arrL_barchartEntrys[i_measureSelector] = new ArrayList(entries);

                                                        measureActivity.get().guideFrag.videoStop();
                                                        measureActivity.get().funcLR = true;

                                                        measureActivity.get().measureFlag = false;

                                                        countDialog.initDialogStart(2);
                                                    } else if (measureActivity.get().funcLR) {
                                                        measureActivity.get().arr_forceList[i_measureSelector + 7] = new ArrayList(ForceDataList);
                                                        measureActivity.get().arrL_barchartEntrys[i_measureSelector + 7] = new ArrayList(entriesLeft);


                                                        measureActivity.get().funcLR = false;
                                                        measureActivity.get().startFlag = false;
                                                        measureActivity.get().measureFlag = false;
                                                        measureActivity.get().bool_measureComple[i_measureSelector] = true;
                                                        measureActivity.get().checkDialog.DialogStart(0);
                                                    }
                                                }
                                                measureActivity.get().ForceDataList.clear();
                                                measureActivity.get().compleFlag = true;

                                            }
//end/////////////////////////////////////////////////


                                        } else {


                                        }


                                        //Error검출구/////////////////////////////////////////////////
                                        measureActivity.get().statusBit8 = (byte) Bytebuf[30];
                                        if ((statusBit8 & 0xEE) == 0xEE) {
                                            measureActivity.get().errorBit8 = (byte) Bytebuf[31];
                                            measureActivity.get().workoutBit8 = (byte) 0x00;
                                        } else {
                                            measureActivity.get().errorBit8 = (byte) 0x00;
                                            measureActivity.get().workoutBit8 = (byte) Bytebuf[31];
                                        }
                                        measureActivity.get().mcActBit8 = (byte) Bytebuf[32];
                                        if ((Bytebuf[4] & 0x04) == 0x04) {
                                            breakStatus = false;
                                        } else if ((Bytebuf[4] & 0x04) == 0x00) {
                                            breakStatus = true;
                                        }
//end/////////////////////////////////////////////////


                                    }
                                }
                            }
                            packetBufStr.setLength(0);
                        }
                    }
                    break;
            }
        }
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


    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }


    private byte FV_ChkSum(byte[] data) {
        byte bChk = 0;
        for (int i = 3; i <= 17; i++) {
            bChk ^= data[i];
        }
        return bChk;
    }


    private String PacketbyteArrayToHexString(byte[] bytes) {

        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {

            sb.append(String.format("%02X", b & 0xff));
        }

        return sb.toString();
    }

    private byte[] FV_RecvChkSum(byte[] data) {
        crc.reset();
        for (int i = 3; i < 39; i++) {
            crc.update(data[i]);
        }

        //  System.out.println(Integer.toHexString((int) crc.getValue()));
        byte[] byteStr = new byte[2];
        byteStr[0] = (byte) ((crc.getValue() & 0x000000ff));
        byteStr[1] = (byte) ((crc.getValue() & 0x0000ff00) >>> 8);

        // System.out.printf("%02x%02x\n", byteStr[0], byteStr[1]);
        // DataSend += String.format("%02x%02x",byteStr[0], byteStr[1]);

        return byteStr;
    }


    private double calculateAverage(List<Double> mValue) {
        Double sum = 0.0;
        if (!mValue.isEmpty()) {
            for (Double mark : mValue) {
                sum += mark;
            }
            return sum.doubleValue() / mValue.size();
        }
        return sum;
    }


    private void ControlBoard(String hexString) {
        byte[] datapacket = hexStringToByteArray(hexString);
        datapacket[18] = FV_ChkSum(datapacket);
        if (usbService != null) { // if UsbService was correctly binded, Send data
            usbService.write(datapacket);
        }
    }


    private void SelBtnBg(int sel) {

        for (int i = 0; i < 7; i++) {
            if (i == sel) bgSelTest[i].setVisibility(View.VISIBLE);
            else bgSelTest[i].setVisibility(View.INVISIBLE);
        }
    }


    private List getColors() {
        int stacksize = 2;
        // have as many colors as stack-values per entry

        List<Integer> colors = new ArrayList<>();

        colors.add(Color.rgb(22, 83, 238));
        colors.add(Color.rgb(255, 255, 255));

        return colors;
    }


    private void putActivityData() {
        putBundleData.putBoolean("startFlag", startFlag);
        putBundleData.putInt("selNum", i_measureSelector);

        if (i_measureSelector < 3) {
            if (i_measureSelector == 0) {
                putBundleData.putStringArray("resultValueL0", testResultValue[0][0]);
                putBundleData.putStringArray("resultValueL1", testResultValue[0][1]);
                putBundleData.putStringArray("resultValueL2", testResultValue[0][2]);

                putBundleData.putStringArray("resultValue0", testResultValue[7][0]);
                putBundleData.putStringArray("resultValue1", testResultValue[7][1]);
                putBundleData.putStringArray("resultValue2", testResultValue[7][2]);
            } else if (i_measureSelector == 1) {
                putBundleData.putStringArray("resultValueL0", testResultValue[1][0]);
                putBundleData.putStringArray("resultValueL1", testResultValue[1][1]);
                putBundleData.putStringArray("resultValueL2", testResultValue[1][2]);

                putBundleData.putStringArray("resultValue0", testResultValue[8][0]);
                putBundleData.putStringArray("resultValue1", testResultValue[8][1]);
                putBundleData.putStringArray("resultValue2", testResultValue[8][2]);
            } else if (i_measureSelector == 2) {
                putBundleData.putStringArray("resultValueL0", testResultValue[2][0]);
                putBundleData.putStringArray("resultValueL1", testResultValue[2][1]);
                putBundleData.putStringArray("resultValueL2", testResultValue[2][2]);

                putBundleData.putStringArray("resultValue0", testResultValue[9][0]);
                putBundleData.putStringArray("resultValue1", testResultValue[9][1]);
                putBundleData.putStringArray("resultValue2", testResultValue[9][2]);
            }
        } else {
            putBundleData.putStringArray("resultValue0", testResultValue[i_measureSelector][0]);
            putBundleData.putStringArray("resultValue1", testResultValue[i_measureSelector][1]);
            putBundleData.putStringArray("resultValue2", testResultValue[i_measureSelector][2]);
        }
    }

    private void setFrag(int sel) {    //프래그먼트를 교체하는 작업을 하는 메소드

        fm = getFragmentManager();
        tran = fm.beginTransaction();

        SelBtnBg(sel);  //버튼 한걸로 넣어주고

        putActivityData();  //데이터 실어주고

        if (!fragChangeFlag) {
            fragChangeFlag = true;
            if (bool_measureComple[sel]) {
                //resultFrag Setting
                resultFrag = new MeasureResultFrag();
                resultFrag.setArguments(putBundleData); //데이터 보내준다.
                tran.replace(R.id.measure_setting_frag, resultFrag);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.

                RGFlag = true;

                barchartBgAble.setVisibility(View.VISIBLE);
                barchartBgUnAble.setVisibility(View.GONE);

            } else {
                //guideFrag Setting
                guideFrag = new MeasureGuideFrag();
                guideFrag.setArguments(putBundleData);
                tran.replace(R.id.measure_setting_frag, guideFrag);  //replace의 매개변수는 (프래그먼트를 담을 영역 id, 프래그먼트 객체) 입니다.
                RGFlag = false;

                if (startFlag) {
                    barchartBgAble.setVisibility(View.VISIBLE);
                    barchartBgUnAble.setVisibility(View.GONE);
                } else {
                    barchartBgAble.setVisibility(View.GONE);
                    barchartBgUnAble.setVisibility(View.VISIBLE);
                }
            }

            if (sel > 4) {
                layoutMuscular.setVisibility(View.GONE);
                btnMode.setVisibility(View.VISIBLE);
            } else {
                layoutMuscular.setVisibility(View.VISIBLE);
                btnMode.setVisibility(View.GONE);
            }

            tran.commit();
        }
    }


    private void selBarchartToLinechart(int barchartIndex, ArrayList<float[]> forceData) {
        float xDataMax = 0;

        // if(Line_Chart==null)     initChart(view);
        if (forceData.size() > 0) {
            entriesForce.clear();
            for (int i = 0; i < forceData.size(); i++) {
                if (forceData.get(i)[0] == barchartIndex) {

                    entriesForce.add(new Entry(Math.abs((float) forceData.get(i)[1]), Math.abs((float) forceData.get(i)[2]))); //N=> kgf
                    xDataMax = Math.abs((float) forceData.get(i)[1]);
                    //if(lineChart!=null) {
                    //Log.i("data",i +" "+ String.valueOf(Math.abs((float) forceData.get(i)[1]) ) +" "+ Math.abs((float) Data.get(i)[2]));
                    LinesetData();
                    //}
                    // Line_Chart.moveViewToX(i);

                }
            }
            //  Line_Chart.setVisibleXRange(0, entries.size());
            //if(lineChart!=null) {
            lineChart.setVisibleXRangeMaximum(xDataMax);
            lineChart.setDescription(lineDescription);
            lineChart.setAutoScaleMinMaxEnabled(true);
            lineChart.animateY(600);
            lineChart.invalidate(); // refresh
            //}
        }
    }

    private void initLineChart() {
        try {
            lineDescription = lineChart.getDescription();
            lineDescription.setText("");
            lineDescription.setTextColor(Color.rgb(0, 0, 0));
            lineLegend = lineChart.getLegend();
            lineLegend.setEnabled(false);

            YAxis leftAxis = lineChart.getAxisLeft();
            YAxis rightAxis = lineChart.getAxisRight();
            XAxis xAxis = lineChart.getXAxis();

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(8f);
            xAxis.setTextColor(Color.rgb(255, 255, 255));
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(false);

            leftAxis.setTextSize(8f);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setTextColor(Color.rgb(255, 255, 255));
            leftAxis.setDrawLabels(false);
            leftAxis.setDrawAxisLine(false);
            leftAxis.setDrawGridLines(false);

            rightAxis.setTextColor(Color.rgb(255, 255, 255));
            rightAxis.setDrawAxisLine(false);
            rightAxis.setDrawGridLines(false);
            rightAxis.setDrawLabels(false);

            lineChart.setTouchEnabled(false);
            lineChart.setDragEnabled(false);
            lineChart.setScaleEnabled(true);
            lineChart.setDoubleTapToZoomEnabled(false);
            lineChart.setBackgroundColor(Color.argb(1, 0, 0, 0));
            lineChart.setDrawBorders(false);

            lineChart.getXAxis().setSpaceMin(0.2f);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e("Error1", e.getMessage());
        } catch (NegativeArraySizeException e) {
            e.printStackTrace();
            Log.e("Error2", e.getMessage());
        }
    }

    private void LinesetData() {
        //IsForceChartDisp = true;
        try {
            if (entriesForce.size() > 0) {
                lineChart.invalidate(); // refresh
                lineChart.clear();

                Collections.sort(entriesForce, new EntryXComparator());
                LineDataSet data = new LineDataSet(entriesForce, "");
                data.setColor(Color.rgb(0, 230, 248));
                data.setCircleColor(Color.rgb(0, 230, 248));
                data.setDrawCircles(false);
                data.setDrawCircleHole(false);
                //data.setDrawValues(false);

                ArrayList<ILineDataSet> Linechart_dataSets = new ArrayList<ILineDataSet>();
                Linechart_dataSets.add(data);

                LineData lineData = new LineData(Linechart_dataSets);
                lineData.setValueTextSize(6f);
                lineData.setValueTextColor(Color.WHITE);

                lineChart.setData(lineData);

                //Line_Chart.setVisibleXRangeMaximum(200);
                lineChart.setDescription(lineDescription);
                lineChart.setAutoScaleMinMaxEnabled(true);
                // Line_Chart.animateY(1000);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e("Error1", e.getMessage());
        } catch (NegativeArraySizeException e) {
            e.printStackTrace();
            Log.e("Error2", e.getMessage());
        }
        //IsForceChartDisp=false;
    }


    private void setData() {


        description = barChart.getDescription();
        description.setText("Speed[cm/s] / Power[w]");
        description.setTextColor(Color.WHITE);

        legend = barChart.getLegend();
        legend.setEnabled(false);

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        XAxis xAxis = barChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(3f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);

        leftAxis.setEnabled(false);

        /*
        leftAxis.setTextSize(3f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        */
        //leftAxis.setLabelCount(3, true);

        rightAxis.setTextSize(3f);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        BarDataSet data;
        if(funcLR)  data = new BarDataSet(entriesLeft, "");
        else        data = new BarDataSet(entries, "");
        //data.setColors(Color.rgb(0, 230, 248), Color.argb(32, 0, 0, 0));
        data.setColors(Color.rgb(0, 230, 248), Color.argb(40, 183, 248, 253));
        //data.setDrawValues(false);
        /*
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                BarEntry barEntry = (BarEntry) entry;

                float[] values = barEntry.getYVals();
                Log.e("텍스트", values[0] + " / " + values[1]);
                return values[0] + " / " + values[1];
            }
        });
        */
        ArrayList<IBarDataSet> Barchart_dataSets = new ArrayList<IBarDataSet>();
        Barchart_dataSets.add(data);

        BarData barData = new BarData(Barchart_dataSets);
        barData.setBarWidth(0.7f);
        barData.setValueTextSize(8f);
        barData.setValueTextColor(Color.rgb(255, 255, 255));
        //barData.setValueTextColors(getColors());


        barChart.setData(barData);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars

        barChart.setScaleEnabled(true);
        barChart.setDoubleTapToZoomEnabled(false);
        //barChart.setBackgroundColor(Color.rgb(255, 255, 255));
        barChart.setDrawBorders(false);
        barChart.setDescription(description);
        barChart.setDrawValueAboveBar(false);
        //barChart.setVisibleXRangeMaximum(6);
        barChart.setVisibleXRange(0, 6);
        barChart.moveViewToX(xEntry);
        barChart.invalidate(); // refresh
    }


    private void setDataRL(boolean sel) {


        description = barChart.getDescription();
        description.setText("Speed[cm/s] / Power[w]");

        legend = barChart.getLegend();
        legend.setEnabled(false);

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        XAxis xAxis = barChart.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(3f);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);

        /*
        leftAxis.setTextSize(3f);
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
         */
        //leftAxis.setLabelCount(3, true);

        rightAxis.setTextSize(3f);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);


        BarDataSet data;
        if (!sel) {
            data = new BarDataSet(arrL_barchartEntrys[i_measureSelector], "");
        } else {
            data = new BarDataSet(arrL_barchartEntrys[i_measureSelector+7], "");
        }

        data.setColors(Color.rgb(0, 230, 248), Color.argb(40, 183, 248, 253));

        ArrayList<IBarDataSet> Barchart_dataSets = new ArrayList<IBarDataSet>();
        Barchart_dataSets.add(data);

        BarData barData = new BarData(Barchart_dataSets);
        barData.setBarWidth(0.7f);
        barData.setValueTextSize(8f);
        barData.setValueTextColor(Color.rgb(255, 255, 255));


        barChart.setData(barData);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars

        barChart.setScaleEnabled(true);
        barChart.setDoubleTapToZoomEnabled(false);
        //barChart.setBackgroundColor(Color.rgb(255, 255, 255));
        barChart.setDrawBorders(false);
        barChart.setDescription(description);
        barChart.setDrawValueAboveBar(false);
        //barChart.setVisibleXRangeMaximum(6);
        barChart.setVisibleXRange(0, 6);
        barChart.moveViewToX(xEntry);
        barChart.invalidate(); // refresh

    }

    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        setFilters();  // Start listening notifications from UsbService
        startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it

        hideSystemUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }


    private class insertWorkoutData extends AsyncTask<Void, Integer, Void> {

        String data = "";
        int tmpCode = i_measureSelector;

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... unused) {

            long now = System.currentTimeMillis();

            Date date = new Date(now);
            SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time_now = now_time.format(date);


            String param = "";

            String testCode = "ERR";

            if (tmpCode == 0) testCode = "PUSH00";
            else if (tmpCode == 1) testCode = "PULL00";
            else if (tmpCode == 2) testCode = "ROT00";
            else if (tmpCode == 3) testCode = "UPCODE001";
            else if (tmpCode == 4) testCode = "UPCODE002";
            else if (tmpCode == 5) testCode = "LOWCODE001";
            else if (tmpCode == 6) testCode = "LOWCODE002";


            if(tmpCode < 3){
                if(funcLR){ //오른쪽
                    testCode += "2";
                }
                else{   //왼쪽
                    testCode += "1";
                }
            }



            ArrayList<List> listForceDataValues = new ArrayList<>((ArrayList) mList_forceValues.clone());
            ArrayList<List> listSpeedDataValues = new ArrayList<>((ArrayList) mList_speedValues.clone());
            ArrayList<List> listPosDataValues   = new ArrayList<>((ArrayList) mList_posValues.clone());

            mList_forceValues.clear();
            mList_speedValues.clear();
            mList_posValues.clear();

            String [] textForceValues = new String[3];
            String [] textSpeedValues = new String[3];
            String [] textPosValues = new String[3];

            for(int i = 0; i < 3; i++){
                textForceValues[i] = "";
                textSpeedValues[i] = "";
                textPosValues[i] = "";
                for(int j = 0; j < listForceDataValues.get(i).size(); j++){
                    Log.e(TAG, "TTT i= " + i + " j = " + j + "=====" + listForceDataValues.get(i).get(j));
                    textForceValues[i] += listForceDataValues.get(i).get(j);
                    textSpeedValues[i] += listSpeedDataValues.get(i).get(j);
                    textPosValues[i]   += listPosDataValues.get(i).get(j);
                    if(j+1 != listForceDataValues.get(i).size()){
                        textForceValues[i] += "_";
                        textSpeedValues[i] += "_";
                        textPosValues[i]   += "_";

                    }
                }
            }

            String s_url;
            s_url = "http://211.253.30.245/equipment/kor/miniplus/Miniplus_test_addWorkData.php";
            param = "&users_id=" + PublicValues.userId;
            //String time_now = "2020-02-01 15:13:02";

            param = param + "&workoutdate=" + time_now +
                    "&test_code=" + testCode +
                    "&avg_force_1=" + sendValue[0][0] +
                    "&max_force_1=" + sendValue[0][1] +
                    "&avg_power_1=" + sendValue[0][2] +
                    "&max_power_1=" + sendValue[0][3] +
                    "&avg_speed_1=" + sendValue[0][4] +
                    "&max_speed_1=" + sendValue[0][5] +
                    "&force_data1=" + textForceValues[0] +
                    "&speed_data1=" + textSpeedValues[0] +
                    "&pos_data1=" + textPosValues[0] +


                    "&avg_force_2=" + sendValue[1][0] +
                    "&max_force_2=" + sendValue[1][1] +
                    "&avg_power_2=" + sendValue[1][2] +
                    "&max_power_2=" + sendValue[1][3] +
                    "&avg_speed_2=" + sendValue[1][4] +
                    "&max_speed_2=" + sendValue[1][5] +
                    "&force_data2=" + textForceValues[1] +
                    "&speed_data2=" + textSpeedValues[1] +
                    "&pos_data2=" + textPosValues[1] +

                    "&avg_force_3=" + sendValue[2][0] +
                    "&max_force_3=" + sendValue[2][1] +
                    "&avg_power_3=" + sendValue[2][2] +
                    "&max_power_3=" + sendValue[2][3] +
                    "&avg_speed_3=" + sendValue[2][4] +
                    "&max_speed_3=" + sendValue[2][5] +
                    "&force_data3=" + textForceValues[2] +
                    "&speed_data3=" + textSpeedValues[2] +
                    "&pos_data3=" + textPosValues[2];


            Log.e("POST", param);


            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 6; j++) {
                    if(funcLR){
                        testResultValue[tmpCode+7][i][j] = sendValue[i][j];
                    }
                    else{
                        testResultValue[tmpCode][i][j] = sendValue[i][j];   //여기서 데이터를 넣어주니깐 빈칸이 들어간듯 하다.
                    }
                }
            }

            sendFlag = false;
            compleFlag = false;

            try {
                //서버연결
                URL url = new URL(s_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();
                // 안드로이드 -> 서버 파라메터값 전달
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                // 서버 -> 안드로이드 파라메터값 전달
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

                // 서버에서 응답
                Log.e("RECV DATA", data);

                if (data.contains("true")) Log.e("RESULT", "성공적으로 처리되었습니다!");
                else{
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("Error1", e.getMessage());
                no_internet = true;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error2", e.getMessage());
                no_internet = true;
                if (e.getMessage().equals("Failed to connect to /211.253.30.245/php/")) {
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(no_internet) {
                no_internet = false;
                checkDialog.DialogStop();
                AlertDialog.Builder Dialog = new AlertDialog.Builder(MeasureTestActivity.this);
                Dialog.setCancelable(false);
                Dialog.setTitle("Error!!!");
                Dialog.setMessage("인터넷 연결을 확인해주세요.");
                Dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideSystemUI();
                    }
                }).create().show();
                Toast.makeText(MeasureTestActivity.this, "인터넷 연결 확인해주세요!!!", Toast.LENGTH_SHORT).show();
                //여기다가 버튼 바꾸는거 넣고 데이터 가지고 있다가 다시 넣어줘도 좋을듯 ㅋㅋㄹㅃㅃ
            } else {
                measureChecked = true;
            }


        }
    }


    String myJSON;

    private void GetData(String str) {
        class GetDataJSON extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                Log.e(TAG, "uri : " + uri);
                BufferedReader bufferedReader = null;

                try {

                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    Log.e(TAG, "서버접속" + sb.toString().trim());
                    return sb.toString().trim();

                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }

            protected void onPostExecute(String result) {
                myJSON = result;
                inputValues();
            }
        }
        GetDataJSON json = new GetDataJSON();
        json.execute(str);
    }


    protected void inputValues() {

        try {
            JSONObject jsonObj = new JSONObject(new JSONObject(myJSON).getString("result"));

            Log.e(TAG, "Json" + jsonObj);

            JSONObject jsonObjDet;

            long now = System.currentTimeMillis();

            Date date = new Date(now);
            SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd");

            String time_now = now_time.format(date);


            Intent intent = new Intent(MeasureTestActivity.this, MeasureResultActivity.class);



            String selStr = "";
            String lastDate, oldDate = "";

            for (int i = 0; i < 10; i++) {
                if (i == 0) selStr = "leftPush";
                else if (i == 1) selStr = "leftFull";
                else if (i == 2) selStr = "leftRot";

                else if (i == 3) selStr = "upPush";
                else if (i == 4) selStr = "upFull";

                else if (i == 5) selStr = "lowPush";
                else if (i == 6) selStr = "lowFull";

                else if (i == 7) selStr = "rightPush";
                else if (i == 8) selStr = "rightFull";
                else if (i == 9) selStr = "rightRot";

                jsonObjDet = new JSONObject(jsonObj.getString(selStr));
                Log.i(TAG, "jsonObjDet" + jsonObjDet);
                String[] values = new String[5];

                //Log.i(TAG, "jsonObjDet.getString(workoutdate)" + jsonObjDet.getString("workoutdate"));
                if (!jsonObjDet.getString("workoutdate").equals("null")) {
                    values[0] = StringParseUnder2(jsonObjDet.getString("avg_power"));//실제로는 force 값이 온다.
                    values[1] = StringParseUnder2(jsonObjDet.getString("max_power"));//실제로는 force 값이 온다.
                    values[2] = StringParseUnder2(jsonObjDet.getString("avg_speed"));
                    values[3] = StringParseUnder2(jsonObjDet.getString("max_speed"));
                    values[4] = jsonObjDet.getString("workoutdate");
                } else {
                    values[0] = "0.0";
                    values[1] = "0.0";
                    values[2] = "0.0";
                    values[3] = "0.0";
                    values[4] = "-";
                }
                //Log.i(TAG, "values[0]" + values[0]);
                //Log.i(TAG, "values[1]" + values[1]);
                //Log.i(TAG, "values[2]" + values[2]);
                //Log.i(TAG, "values[3]" + values[3]);
                //Log.i(TAG, "values[4]" + values[4]);


                intent.putExtra(selStr, values);
            }


            ///////////////////////////old Date///////////////////

                /*try{
                    jsonObjDet = new JSONObject(jsonObj.getString("lowOldFull"));
                    oldDate = jsonObjDet.getString("workoutdate");
                }catch (Exception e){
                    oldDate = "";
                }*/

            //Log.i(TAG, "oldDate = " + oldDate + "nowDate = " + time_now);
/*
                if(oldDate == null || oldDate.length() < 8){
                    //checkDialog.DialogStart(1);
                }
                else{*/
            for (int i = 0; i < 10; i++) {
                if (i == 0) selStr = "leftOldPush";
                else if (i == 1) selStr = "leftOldFull";
                else if (i == 2) selStr = "leftOldRot";

                else if (i == 3) selStr = "upOldPush";
                else if (i == 4) selStr = "upOldFull";

                else if (i == 5) selStr = "lowOldPush";
                else if (i == 6) selStr = "lowOldFull";

                else if (i == 7) selStr = "rightOldPush";
                else if (i == 8) selStr = "rightOldFull";
                else if (i == 9) selStr = "rightOldRot";

                jsonObjDet = new JSONObject(jsonObj.getString(selStr));
                String[] values = new String[5];

                if (!jsonObjDet.getString("workoutdate").equals("null")) {
                    values[0] = StringParseUnder2(jsonObjDet.getString("avg_power"));//실제로는 force 값이 온다.
                    values[1] = StringParseUnder2(jsonObjDet.getString("max_power"));//실제로는 force 값이 온다.
                    values[2] = StringParseUnder2(jsonObjDet.getString("avg_speed"));
                    values[3] = StringParseUnder2(jsonObjDet.getString("max_speed"));
                    values[4] = jsonObjDet.getString("workoutdate");
                } else {
                    values[0] = "0.0";
                    values[1] = "0.0";
                    values[2] = "0.0";
                    values[3] = "0.0";
                    values[4] = "-";
                }

                //Log.i(TAG, "values[0]===" + values[0]);
                //Log.i(TAG, "values[1]===" + values[1]);
                //Log.i(TAG, "values[2]===" + values[2]);
                //Log.i(TAG, "values[3]===" + values[3]);
                //Log.i(TAG, "values[4]===" + values[4]);

                intent.putExtra(selStr, values);
            }
            //}


            startActivity(intent);

            if (timeCalDisp != null) timeCalDisp.cancel();
            timeCalDisp = null;

            checkDialog.DialogStop();
            finishAffinity();
            //}


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Json Error!!!");
            checkDialog.DialogStop();
            AlertDialog.Builder Dialog = new AlertDialog.Builder(MeasureTestActivity.this);
            Dialog.setCancelable(false);
            Dialog.setTitle("Error!!!");
            Dialog.setMessage("인터넷 연결을 확인해주세요.");
            Dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    hideSystemUI();
                }
            }).create().show();
        }
    }

    String StringParseUnder2(String str) {
        return str.substring(0, str.lastIndexOf(".") + 4);
    }

}
