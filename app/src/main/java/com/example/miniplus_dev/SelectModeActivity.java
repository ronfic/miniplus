package com.example.miniplus_dev;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class SelectModeActivity extends Activity {

    private final String TAG = SelectModeActivity.class.getSimpleName();

    Button LoginBtn;
    Button noLoginBtn;

    int NetworkChk = 0;

    String myJSON;

    JSONArray peoples = null;

    long old_time = System.currentTimeMillis();

    static StringBuffer PacketBufStr = new StringBuffer();

    List<Double> mForceAvr = new ArrayList<Double>();
    List<Double> mVelAvr = new ArrayList<Double>();

    boolean SerialRcvFlag = false;


    private TextView display, TextReps, TextSets, Load, MC_Status, TextVel, TextVelMax, TextLoad, TextLoadMax, TextPwr, TextPwrMax;
    private TextView TextLoadAvr, TextVelAvr, TextPwrAvr;

    Boolean Hand_one_On = true;
    Boolean Hand_two_On = false;
    boolean Safty_Flag = true;
    TextView Work_Time, Break_Time;

    private EditText editText;
    //private MyHandler mHandler;

    private final String PIN_PwrCtrl = "BCM12";

    Display windowSize;
    Point size;
    int win_width, win_height;
    float textSize;

/*
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
    };*/

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String byteArrayToHexString(byte[] bytes) {

        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {

            sb.append(String.format("%02X", b & 0xff));
        }

        return sb.toString();
    }

    private byte FV_ChkSum(byte[] data) {
        byte bChk = 0;
        for (int i = 3; i <= 12; i++) {
            bChk ^= data[i];
        }
        return bChk;
    }


    String Admin_value = "0", userName = "Guest";
    String SSID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);

        LoginBtn = (Button) findViewById(R.id.login);
        noLoginBtn = (Button) findViewById(R.id.nologin);
        Break_Time = (TextView) findViewById(R.id.textView_Btime);

        /*PeripheralManager Gpiomanager = PeripheralManager.getInstance();
        List<String> portList = Gpiomanager.getGpioList();
        if (portList.isEmpty()) {
            Log.i(TAG, "No GPIO port available on this device.");
        } else {
            Log.i(TAG, "List of available ports: " + portList);
        }

        try {
            mGpio12 = Gpiomanager.openGpio(PIN_PwrCtrl);
            mGpio12.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mGpio12.setActiveType(Gpio.ACTIVE_HIGH);
            mGpio12.setValue(true);
        } catch (IOException e) {
            Log.i(TAG, "Gpio Error: " + e.getMessage());
        }*/

        Intent intent = getIntent();
        SSID = intent.getStringExtra("SSID");
        if (SSID == null) {
            SSID = "네트워크 없음.";
        }

        TextView network_info = (TextView) findViewById(R.id.network_info);
        if (!SSID.equals("네트워크 없음.")) {
            network_info.setText("네트워크 이름 : " + SSID);
        } else {
            network_info.setText(SSID);
        }


        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "NetworkChk : " + NetworkChk, Toast.LENGTH_LONG).show();
                if (NetworkChk != 0) {
                    //Toast.makeText(getApplicationContext(), "네트워크 연결을 확인 해 주세요.", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(SelectModeActivity.this, LoginPadActivity.class);
                    intent.putExtra("userName", userName);
                    intent.putExtra("Admin_value", Admin_value);
                    intent.putExtra("SSID", SSID);
                    startActivity(intent);
                    finish();
                }
            }
        });

        noLoginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectModeActivity.this, MainActivity.class);
                PublicValues.userId = "";
                PublicValues.userName = "Guest";
                PublicValues.userHeight = "";
                PublicValues.userWeight = "";
                PublicValues.userBirth = "";
                PublicValues.userGender = "";
                PublicValues.userClub = "";
                PublicValues.location = "";
                PublicValues.director = "0";

                intent.putExtra("NetworkChk", NetworkChk);
                startActivity(intent);
                finish();
            }
        });

        old_time = System.currentTimeMillis();

        TimerTask TimeCal = new TimerTask() {
            @Override
            public void run() {
                SelectModeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        long now = System.currentTimeMillis();
                        Date mdate = new Date(now - old_time);
                        SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault());

                        Break_Time.setText(mDateFormat.format(mdate));

                        if (SerialRcvFlag == false) {
                            String hexString = "F2010F01000000000000000000000000000001FE";

                            byte[] datapacket = hexStringToByteArray(hexString);

                            //if (usbService != null && usbService.SERVICE_CONNECTED) { // if UsbService was correctly binded, Send data
                            //    usbService.write(datapacket);
                            //}
                        }
                    }
                });
            }
        };

        Timer TimeCalDisp = new Timer();
        TimeCalDisp.schedule(TimeCal, 0, 1000);

        //mHandler = new MyHandler(this);
    }


    @Override
    public void onResume() {
        super.onResume();
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

        //setFilters();  // Start listening notifications from UsbService
        //startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    public void onPause() {
        super.onPause();
        //unregisterReceiver(mUsbReceiver);
        //unbindService(usbConnection);
    }

/*

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
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


    private class MyHandler extends Handler {
        private final WeakReference<SelectModeActivity> mActivity;

        public MyHandler(SelectModeActivity activity) {
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

                    mActivity.get().SerialRcvFlag = true;

                    byte[] buffer = (byte[]) msg.obj; //"f1de3e44dcb2";//

                    break;
            }
        }

    }
*/


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

    public static String PacketbyteArrayToHexString(byte[] bytes) {

        StringBuilder sb = new StringBuilder();

        for (byte b : bytes) {

            sb.append(String.format("%02X", b & 0xff));
        }

        return sb.toString();
    }
}
