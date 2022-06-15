package com.example.miniplus_dev;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
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
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;

import com.example.miniplus_dev.setting.LoginPadFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.miniplus_dev.PublicValues.BootProcess;
import static com.example.miniplus_dev.PublicValues.ResetFlag;
import static com.example.miniplus_dev.PublicValues.lastVersionInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity implements LoginPadFragment.OnTimePickerSetListener {

    CRC16Modbus crc = new CRC16Modbus();

    private final String TAG = MainActivity.class.getSimpleName();

    private final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private final String MOTER_START_SERIAL = "F2010FA1000000000000000000000000000001FE";
    private final String MOTER_STOP_SERIAL = "F2010FA2000000000000000000000000000001FE";
    private final String TIME_ZONE = "Asia/Seoul";

    LinearLayout layout_selectMode, layout_admin, layout_update;

    TextView txt_userInfo;
    TextView txt_ipAddress, txt_wifiName;
    TextView txt_serialNum, txt_clubId, txt_loadCellValue, txt_loadCellValueMain, txt_stictionValue, txt_Email;
    TextView txt_versionInfo, txt_conversionInfo;
    TextView txt_errorCode;

    Button btn_exerciseMode, btn_login, btn_logOut, btn_admin, btn_measure, btn_back, btn_wifi, btn_reset, btn_update, btn_reboot;

    ImageView image_initial;

    private final String fileName = "mpconfig";

    private int i_stiction = 100;
    private int i_loadCell = 0;

    initDialog initDialog = new initDialog(this);


    boolean bool_breakStatus = false;
    boolean bool_measureFlag;
    boolean UsbServiceFlag = false;

    boolean mCSTFlag = false;
    boolean mFSTFlag = false;
    boolean mDoinFlag = false;
    boolean mFirstConnectionCheck = false;
    boolean restart = false;
    boolean readpack = false;

    byte b_errorBit = 0x00;
    byte b_statusBit = 0x00;
    byte b_workStatusBit = 0x00;
    byte b_workoutBit = 0x00;

    int i_readCnt = 0;

    int i_bootCnt = 0;

    int i_bootCST = 0;
    int i_cstCnt = 0;
    int i_bootFST = 0;
    int i_fstCnt = 0;

    int i_btnAdminCnt = 0;
    int i_btnAdminDelayCnt = 0;


    double velReal;
    double posReal;
    double loadReal;


    String versionNum;

    private UsbService usbService;
    Timer timer_main, mTimer_service;
    TimerTask timeT_main;
    private MainHandler mainHandler;
    private final StringBuffer sb_packet = new StringBuffer();


    NetworkInfo activeNetwork;

    public static String sStr_wifiSelItem = "";
    public static String sStr_wifiSelItemSecu = "";


    String str_wifiPass = "";

    public static ArrayList<String> sArr_wifiSecurity = new ArrayList<>();
    public static ArrayList<String> sArr_wifiName = new ArrayList<>();


    ProgressDialog progressDialog = new ProgressDialog(this);


    WifiManager wifiManager;

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            UsbServiceFlag = true;
                            Log.e("지금!!!", "지금!!!");
                        }
                    },7000);
                    Log.i(TAG, "*************USB READY*************");
                    //timerStart();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED:
                    Log.e("지금!!!", "pernotg");
                    break;
                case UsbService.ACTION_NO_USB:
                    Log.i(TAG, "*************ACTION_NO_USB*************");
                    Log.e("지금!!!", "no");
                    break;
                case UsbService.ACTION_USB_DISCONNECTED:
                    Log.i(TAG, "*************ACTION_USB_DISCONNECTED*************");
                    Log.e("지금!!!", "dis");
                    usbService.findSerialPortDevice();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED:
                    Log.e("지금!!!", "nots");
                    break;
            }
        }
    };

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mainHandler);

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    @Override
    public void onTimePickerSet(boolean LogonFlag) {
        Log.e("LogonFlag", LogonFlag + "");
        if(LogonFlag) {
            btn_logOut.setVisibility(View.VISIBLE);
            txt_userInfo.setVisibility(View.VISIBLE);
            txt_userInfo.setText(PublicValues.userName);
            btn_login.setVisibility(View.GONE);
            btn_measure.setVisibility(View.VISIBLE);
            btn_exerciseMode.setBackgroundResource(R.drawable.btn_exercise);
        }
    }

    @Override
    public void DoinBackLoading(boolean LoadinFlag) {
        if (LoadinFlag) {
            btn_login.setEnabled(false);
            btn_exerciseMode.setEnabled(false);
            btn_wifi.setEnabled(false);
        } else {
            btn_login.setEnabled(true);
            btn_exerciseMode.setEnabled(true);
            btn_wifi.setEnabled(true);
        }
    }

    private byte[] makeRecvChkSum(byte[] data) {
        crc.reset();
        for (int i = 3; i < 39; i++) {
            crc.update(data[i]);
        }

        byte[] byteStr = new byte[2];
        byteStr[0] = (byte) ((crc.getValue() & 0x000000ff));
        byteStr[1] = (byte) ((crc.getValue() & 0x0000ff00) >>> 8);

        return byteStr;
    }
    SharedPreferences sharedPreferences;


    //파일 저장
    public void saveItemsToFile() {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("serialNumber", PublicValues.configValues.get(0));
        editor.putString("stiction", PublicValues.configValues.get(1));
        editor.putString("lastVersionInfo", PublicValues.configValues.get(2));
        editor.putString("club_id", PublicValues.configValues.get(3));
        editor.putString("email", PublicValues.configValues.get(4));
        editor.apply();


        File file = new File("/sdcard/Download", fileName);
        FileWriter fw = null;
        BufferedWriter bufwr = null;

        try {
            fw = new FileWriter(file);
            bufwr = new BufferedWriter(fw);

            for (String str : PublicValues.configValues) {
                bufwr.write(str);
                bufwr.newLine();
            }
            bufwr.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (bufwr != null)  bufwr.close();
            if (fw != null)     fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //파일 호출
    private void loadItemsFromFile() {

        PublicValues.configValues.clear();
        PublicValues.configValues.add(sharedPreferences.getString("serialNumber", "0"));
        PublicValues.configValues.add(sharedPreferences.getString("stiction", "100"));
        PublicValues.configValues.add(sharedPreferences.getString("lastVersionInfo", "100"));
        PublicValues.configValues.add(sharedPreferences.getString("club_id", "0"));
        PublicValues.configValues.add(sharedPreferences.getString("email", "0"));

/*        Log.i(TAG, "PublicValues.configValues = " + PublicValues.configValues.get(0));
        Log.i(TAG, "PublicValues.configValues = " + PublicValues.configValues.get(1));
        Log.i(TAG, "PublicValues.configValues = " + PublicValues.configValues.get(2));
        Log.i(TAG, "PublicValues.configValues = " + PublicValues.configValues.get(3));*/


        File file = new File("/sdcard/Download", fileName);
        FileReader fileReader = null;
        BufferedReader bufReader = null;
        String str;

        if (file.exists()) {
            try {
                fileReader = new FileReader(file);
                bufReader = new BufferedReader(fileReader);

                int i = 0;
                while ((str = bufReader.readLine()) != null) {
                    if(i == 2){
                        PublicValues.configValues.remove(2);
                        PublicValues.configValues.add(2, str);
                        //Log.e(TAG, "load File 2 = " + str);
                    }
                    i++;
                }

                bufReader.close();
                fileReader.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "load File ERR!" + e);
            }
        }


        try{
            int i = Integer.parseInt(PublicValues.configValues.get(2));
        }catch (Exception e){
            Log.e(TAG, "ERR = " + e);
            PublicValues.configValues.remove(2);
            PublicValues.configValues.add(2, "100");
        }

        PublicValues.serialNumber = PublicValues.configValues.get(0);
        PublicValues.stiction = PublicValues.configValues.get(1);
        PublicValues.lastVersionInfo = PublicValues.configValues.get(2);
        PublicValues.club_id = PublicValues.configValues.get(3);
        PublicValues.email = PublicValues.configValues.get(4);
        Log.e("club_id", PublicValues.club_id);
    }


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
        registerReceiver(usbReceiver, filter);
    }



    private ListView searchWifi() {

        final ListView WIFI_LIST = new ListView(MainActivity.this);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        if (wifiManager.isWifiEnabled() == false)
            wifiManager.setWifiEnabled(true);

        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {

            // register WiFi scan results receiver
            IntentFilter filter = new IntentFilter();
            filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (wifiManager.disconnect()) {
                        String networkSSID = sStr_wifiSelItem;
                        WifiConfiguration conf = new WifiConfiguration();

                        List<ScanResult> results = wifiManager.getScanResults();
                        final int N = results.size();

                        Log.v(TAG, "Wi-Fi Scan Results ... Count:" + N + " " + networkSSID);
                        sArr_wifiName.clear();
                        sArr_wifiSecurity.clear();
                        for (int i = 0; i < N; ++i) {
                            sArr_wifiName.add(results.get(i).SSID);
                            sArr_wifiSecurity.add(results.get(i).capabilities);
                        }
                    }
                    if (sArr_wifiName.size() > 0) {
                        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, sArr_wifiName);
                        WIFI_LIST.setAdapter(adapter);
                    }
                }
            }, filter);
            if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
                // start WiFi Scan
            }
            wifiManager.startScan();    //질문
        }
        return WIFI_LIST;
    }


    private void connectWiFi(String SSID, String password, String Security) {
        try {
            Log.e(TAG, "Item clicked, SSID " + SSID + " Security : " + Security);
            Log.e(TAG, "PASS =  " + password);

            String networkSSID = SSID;
            String networkPass = password;
            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.priority = 40;
// Check if security type is WEP
            if (Security.toUpperCase().contains("WEP")) {
                Log.v("rht", "Configuring WEP");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                if (networkPass.matches("^[0-9a-fA-F]+$")) {
                    conf.wepKeys[0] = networkPass;
                } else {
                    conf.wepKeys[0] = "\"".concat(networkPass).concat("\"");
                }
                conf.wepTxKeyIndex = 0;
// Check if security type is WPA
            } else if (Security.toUpperCase().contains("WPA")) {
                Log.v(TAG, "Configuring WPA");
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                conf.preSharedKey = "\"" + networkPass + "\"";
// Check if network is open network
            } else {
                Log.v(TAG, "Configuring OPEN network");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.clear();
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            }
//Connect to the network
            wifiManager = null;
            wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            int networkId = wifiManager.addNetwork(conf);
            Log.v(TAG, "Add result " + networkId);
            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    Log.v(TAG, "WifiConfiguration SSID " + i.SSID);
                    boolean isDisconnected = wifiManager.disconnect();
                    Log.v(TAG, "isDisconnected : " + isDisconnected);
                    boolean isEnabled = wifiManager.enableNetwork(i.networkId, true);
                    Log.v(TAG, "isEnabled : " + isEnabled);
                    boolean isReconnected = wifiManager.reconnect();
                    Log.v(TAG, "isReconnected : " + isReconnected);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isConnected(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void controlBoard(String hexString) {
        byte[] datapacket = PublicFunctions.hexStrToByteArr(hexString);
        datapacket[18] = PublicFunctions.makeChkSum(datapacket);
        if (usbService != null) {
            usbService.write(datapacket);
        }
    }

    private void initView() {

        layout_selectMode   = findViewById(R.id.layout_selectMode);
        layout_admin        = findViewById(R.id.layout_admin);
        layout_update       = findViewById(R.id.layout_update);


        txt_userInfo    = findViewById(R.id.txt_userInfo);

        txt_ipAddress       = findViewById(R.id.txt_networkInfo);
        txt_wifiName        = findViewById(R.id.txt_wifiName);
        txt_serialNum       = findViewById(R.id.txt_serialNum);
        txt_clubId   = findViewById(R.id.txt_clubId);
        txt_loadCellValue   = findViewById(R.id.txt_loadCellValue);
        txt_loadCellValueMain   = findViewById(R.id.txt_loadCellValueMain);
        txt_stictionValue   = findViewById(R.id.txt_stictionValue);
        txt_Email = findViewById(R.id.txt_Email);

        txt_errorCode   = findViewById(R.id.txt_errorCode);

        txt_versionInfo = findViewById(R.id.txt_versionInfo);
        txt_conversionInfo  = findViewById(R.id.txt_conversionInfo);

        btn_wifi            = findViewById(R.id.btn_wifi);
        btn_logOut          = findViewById(R.id.btn_logOut);
        btn_exerciseMode    = findViewById(R.id.btn_exerciseMode);
        btn_login           = findViewById(R.id.btn_login);
        btn_measure         = findViewById(R.id.btn_measure);
        btn_admin           = findViewById(R.id.btn_admin);
        btn_back            = findViewById(R.id.btn_back);
        btn_reset           = findViewById(R.id.btn_reset);
        btn_update          = findViewById(R.id.btn_update);

        btn_reboot          = findViewById(R.id.btn_reboot);

        image_initial       = findViewById(R.id.image_initial);

        txt_errorCode.setVisibility(View.GONE);
        layout_update.setVisibility(View.GONE);
        btn_reset.setVisibility(View.GONE);

    }





    private void hideSystemUI() {
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

    private void GetVersionInfo() {
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
                    return "Exception: " + e.getMessage();
                }
            }

            protected void onPostExecute(String result) {
                //myJSON = result;
                Log.e(TAG, "UPDATE VERSION = " + result);
                PublicValues.needUpdateVersion = result;

                deleteApk(result);


                mUpdatePlease = !versionNum.equals(result);
            }
        }
        GetDataJSON json = new GetDataJSON();
        json.execute("http://211.253.30.245/miniplus_update/versionInfo.php");
    }


    boolean mUpdatePlease = false;



    public void deleteApk(String result) {

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

                if((result.equals(versionNum)) && Integer.parseInt(versionNum.replace(".", "")) > Integer.parseInt(lastVersionInfo.replace(".", ""))){
                    PublicValues.configValues.remove(2);
                    PublicValues.configValues.add(2, versionNum);
                    saveItemsToFile();
                    String pckName = lastVersionInfo.replace(".", "");
                    Log.e(TAG, "deleteApk : pckName = " + pckName);
                    Uri uri = Uri.fromParts("package", "com.example.miniplus_dev_" + pckName, null);
                    lastVersionInfo = versionNum;
                    Intent delIntent = new Intent(Intent.ACTION_DELETE, uri);
                    startActivity(delIntent);
                }
                else if(Integer.parseInt(versionNum.replace(".", "")) < Integer.parseInt(lastVersionInfo.replace(".", ""))){
                    String pckName = versionNum.replace(".", "");
                    Log.e(TAG, "deleteApk : pckName = " + pckName);
                    Uri uri = Uri.fromParts("package", "com.example.miniplus_dev_" + pckName, null);
                    Intent delIntent = new Intent(Intent.ACTION_DELETE, uri);
                    startActivity(delIntent);
                }

            }
        }
    }




    public class MainHandler extends Handler {
        private final WeakReference<MainActivity> mainActivity;

        private MainHandler(MainActivity activity) {
            mainActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    Toast.makeText(mainActivity.get(), "MESSAGE_FROM_SERIAL_PORT", Toast.LENGTH_LONG).show();
                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mainActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mainActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case UsbService.SYNC_READ:
                    Log.e("sync", "1");
                    byte[] buffer = (byte[]) msg.obj;
                    sb_packet.append(PublicFunctions.byteArrToHexStr(buffer));

                    if((PublicFunctions.byteArrToHexStr(buffer).equals("4F4F4F") || PublicFunctions.byteArrToHexStr(buffer).equals("4F4F") || PublicFunctions.byteArrToHexStr(buffer).equals("4F")) && timer_main != null && !mCSTFlag && !mFSTFlag) {
                        Log.e("씨에스티2222", "2222");
                        if(PublicFunctions.byteArrToHexStr(buffer).equals("4F4F4F")) {
                            i_bootCST += 3;
                        } else if(PublicFunctions.byteArrToHexStr(buffer).equals("4F4F")) {
                            i_bootCST += 2;
                        } else if(PublicFunctions.byteArrToHexStr(buffer).equals("4F")) {
                            i_bootCST += 1;
                        }

                        if(i_bootCST == 3) {
                            mCSTFlag = true;
                            Log.e("씨에스티1", PublicFunctions.byteArrToHexStr(buffer) + "   " + buffer.length);
                            buffer = null;
                            if (buffer == null) {
                                Log.e("씨에스티2", "ㅋㅋㄹㅃㅃ!!");
                            }
                            mDoinFlag = false;
                        } else {
                            mDoinFlag = true;
                        }
                    } else if((PublicFunctions.byteArrToHexStr(buffer).equals("4F4F4F") || PublicFunctions.byteArrToHexStr(buffer).equals("4F4F") || PublicFunctions.byteArrToHexStr(buffer).equals("4F")) && timer_main != null && mCSTFlag && !mFSTFlag) {
                        Log.e("에프에스티2222", "2222");
                        if(PublicFunctions.byteArrToHexStr(buffer).equals("4F4F4F")) {
                            i_bootFST += 3;
                        } else if(PublicFunctions.byteArrToHexStr(buffer).equals("4F4F")) {
                            i_bootFST += 2;
                        } else if(PublicFunctions.byteArrToHexStr(buffer).equals("4F")) {
                            i_bootFST += 1;
                        }

                        if(i_bootFST == 3) {
                            mFSTFlag = true;
                            Log.e("에프에스티1", PublicFunctions.byteArrToHexStr(buffer) + "   " + buffer.length);
                            mDoinFlag = false;
                        } else {
                            mDoinFlag = true;
                        }
                    } else if((!PublicFunctions.byteArrToHexStr(buffer).equals("4E4E4E") && !PublicFunctions.byteArrToHexStr(buffer).equals("4E4E") && !PublicFunctions.byteArrToHexStr(buffer).equals("4E")) && !mCSTFlag && !mFSTFlag) {
                        mCSTFlag = true;
                    } else if((!PublicFunctions.byteArrToHexStr(buffer).equals("4E4E4E") && !PublicFunctions.byteArrToHexStr(buffer).equals("4E4E") && !PublicFunctions.byteArrToHexStr(buffer).equals("4E")) &&mCSTFlag && !mFSTFlag) {
                        mFSTFlag = true;
                    }

                    if (sb_packet.length() >= 84) {
                        if (sb_packet.indexOf("F10124") >= 0 && sb_packet.lastIndexOf("FE") >= 0 && sb_packet.lastIndexOf("FE") - sb_packet.indexOf("F10124") > 84) {

                            int startIndex = sb_packet.indexOf("F10124");
                            int endIndex = sb_packet.indexOf("FE", startIndex + 78);


                            if (endIndex - startIndex == 82) {

                                byte[] Bytebuf = PublicFunctions.hexStrToByteArr(sb_packet.substring(startIndex, endIndex + 2));

                                byte[] RecvChkSum = makeRecvChkSum(Bytebuf);

                                if (Bytebuf[39] == RecvChkSum[1] && Bytebuf[40] == RecvChkSum[0]) {

                                    if (Bytebuf[0] == -15 && Bytebuf[1] == 1) {

                                        if (txt_conversionInfo.getText().toString().equals("") || txt_conversionInfo.getText().toString().equals(null))
                                            Log.e("bytebuffer", Bytebuf[0]+Bytebuf[1]+Bytebuf[2]+Bytebuf[3]+Bytebuf[4]+Bytebuf[5]+Bytebuf[6]+Bytebuf[7]+Bytebuf[8]+Bytebuf[9]+
                                                    Bytebuf[10]+Bytebuf[11]+Bytebuf[12]+Bytebuf[13]+Bytebuf[14]+Bytebuf[15]+Bytebuf[16]+Bytebuf[17]+Bytebuf[18]+Bytebuf[19]+Bytebuf[20]+Bytebuf[21]+
                                                    Bytebuf[22]+Bytebuf[23]+Bytebuf[24]+Bytebuf[25]+Bytebuf[26]+Bytebuf[27]+Bytebuf[28]+Bytebuf[29]+Bytebuf[30]+Bytebuf[31]+Bytebuf[32]+Bytebuf[33]+Bytebuf[34]+
                                                    Bytebuf[35]+Bytebuf[36]+Bytebuf[37]+Bytebuf[38]+"");
                                            txt_conversionInfo.setText("" + (int) Bytebuf[34] + "." + (int) Bytebuf[35]);


                                        int posData = ((((int) Bytebuf[5]) << 8) | ((int) Bytebuf[6] & 0xff));
                                        int velData = ((((int) Bytebuf[7]) << 8) | ((int) Bytebuf[8] & 0xff));
                                        int loadData = ((((int) Bytebuf[9]) << 8) | ((int) Bytebuf[10] & 0xff));
                                        int stiction = ((int) Bytebuf[38] & 0xff);
                                        int loadCell = ((Bytebuf[36]& 0xff) << 8) | (Bytebuf[37] & 0xff);
                                        Log.e("stiction", stiction +"");
                                        mainActivity.get().i_stiction = stiction;   //질문
                                        mainActivity.get().i_loadCell = loadCell;
                                        Log.e("스틱션", ((int) Bytebuf[38] & 0xff) + "");
                                        velReal = (double) (velData * -1) / 10.0;    // cm/s
                                        posReal = (double) (posData * -1) / 10.0;    // cm
                                        loadReal = (double) loadData / 10.0;         // N


                                        //machine Status Chk
                                        mainActivity.get().b_statusBit = Bytebuf[30];
                                        Log.e("b_statusBit", b_statusBit + "");
                                        if ((b_statusBit & 0xEE) == 0xEE) {
                                            mainActivity.get().b_errorBit = Bytebuf[31];
                                            mainActivity.get().b_workoutBit = (byte) 0x00;
                                        } else {
                                            if ((b_statusBit & 0x03) == 0x03 && !PublicValues.initFlag) {
                                                PublicValues.initFlag = true;
                                                initDialog.initDialogStop();
                                                Log.i(TAG, "initDialog.initDialogStop(); !!!");
                                            }
                                            mainActivity.get().b_errorBit = (byte) 0x00;
                                            mainActivity.get().b_workoutBit = Bytebuf[31];
                                        }
                                        mainActivity.get().b_workStatusBit = Bytebuf[32];


                                        if ((Bytebuf[4] & 0x04) == 0x04) {
                                            bool_breakStatus = false;
                                        } else if ((Bytebuf[4] & 0x04) == 0x00) {
                                            bool_breakStatus = true;
                                        }



                                    }
                                }
                            }
                            sb_packet.setLength(0);
                        }
                    }
                    break;
            }
        }
    }





    protected void onCreate(Bundle savedInstanceState) {

        Log.e(TAG, "onCreate ~!!!!!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        initView();

        hideSystemUI();

        //saveItemsToFile();
        Display display = getWindowManager().getDefaultDisplay();  // in Activity
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        Point size = new Point();
        display.getRealSize(size); // or getSize(size)
        int width = size.x;
        int height = size.y;

        Log.e("화면 크기", width + "   " + height);

        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        if (wifiManager.isWifiEnabled() == false)
            wifiManager.setWifiEnabled(true);

        if (!PublicValues.initFlag) {
            PublicValues.BootProcess = true;
            initDialog.initDialogStart();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        else if(PublicValues.ResetFlag){
            initDialog.initDialogStart();
        }


/*
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_COARSE_LOCATION);
        }

*/



        PackageInfo pi = null;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        versionNum = pi.versionName;
        txt_versionInfo.setText(versionNum);




        image_initial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicValues.initFlag = false;
                PublicValues.BootProcess = true;
                i_bootCnt = 0;
            }
        });

        //ttttt
        btn_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder Dialog = new AlertDialog.Builder(MainActivity.this);

                final EditText editText_pass = new EditText(MainActivity.this);
                final ListView AP_List = searchWifi();

                Dialog.setTitle("네트워크 선택");
                Dialog.setView(AP_List);

                AP_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        sStr_wifiSelItem = sArr_wifiName.get(position);
                        sStr_wifiSelItemSecu = sArr_wifiSecurity.get(position);
                    }
                });

                Dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder Dialog = new AlertDialog.Builder(MainActivity.this);

                        Dialog.setTitle("네트워크 이름 : " + sStr_wifiSelItem);
                        Dialog.setMessage("WIFI PASSWORD를 입력해 주세요.");
                        Dialog.setView(editText_pass);

                        Dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (editText_pass.getText().toString().length() > 0) {
                                    str_wifiPass = editText_pass.getText().toString();
                                } else {
                                    str_wifiPass = "";
                                }

                                connectWiFi(sStr_wifiSelItem, str_wifiPass, sStr_wifiSelItemSecu);
                                hideSystemUI();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hideSystemUI();
                            }
                        }).create().show();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hideSystemUI();
                    }
                }).create().show();

            }
        });

        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (PublicValues.director.equals("1")) {
                    i_btnAdminCnt++;
                    if (i_btnAdminCnt > 5) {
                        layout_selectMode.setVisibility(View.GONE);
                        layout_admin.setVisibility(View.VISIBLE);
                    }
                //}
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);

                if (timer_main != null) timer_main.cancel();
                timer_main = null;

                startActivity(intent);
                finishAffinity();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSystemUI();
                layout_admin.setVisibility(View.GONE);
                layout_selectMode.setVisibility(View.VISIBLE);
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((b_statusBit & 0xEE) == 0xEE) {
                    initDialog.initDialogStart();
                    PublicValues.ResetFlag = true;
                    PublicValues.BootProcess = true;
                    String hexString = "";

                    hexString = "F2010FEE" + String.format("%02X", b_errorBit) + "0000000000" + "000000000000000001FE";

                    byte[] datapacket = PublicFunctions.hexStrToByteArr(hexString);
                    datapacket[18] = PublicFunctions.makeChkSum(datapacket);
                    if (usbService != null) {
                        usbService.write(datapacket);
                    }
                }
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();

                FragmentTransaction fragmentTransaction;
                LoginPadFragment loginPadFragment = new LoginPadFragment();
                fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.loginPad, loginPadFragment);

                fragmentTransaction.commit();
            }
        });
        btn_logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PublicValues.userId = "";
                PublicValues.userName = "Guest";
                PublicValues.location = "";
                PublicValues.userGender = "Male";
                PublicValues.userHeight = "";
                PublicValues.userWeight = "";
                PublicValues.userBirth = "";
                PublicValues.userClub = "";
                PublicValues.director = "0";
                PublicValues.userAge = 0;

                btn_logOut.setVisibility(View.GONE);
                txt_userInfo.setVisibility(View.GONE);
                btn_measure.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);
                btn_exerciseMode.setBackgroundResource(R.drawable.btn_guest);
                if(layout_update.getVisibility() == View.VISIBLE)
                    layout_update.setVisibility(View.GONE);
            }
        });
        btn_exerciseMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, ExerciseActivity.class).setAction(Intent.ACTION_MAIN) .addCategory(Intent.CATEGORY_LAUNCHER) .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                if (timer_main != null) timer_main.cancel();
                timer_main = null;
                startActivity(intent);
                finishAffinity();
            }
        });

        btn_measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!bool_measureFlag && b_statusBit == 0x03) {

                    progressDialog.DialogStart();

                    bool_measureFlag = true;

                } else Toast.makeText(MainActivity.this, "운동종료 후 눌러주세요", Toast.LENGTH_SHORT).show();
            }
        });

        txt_serialNum.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                final EditText editText = new EditText(MainActivity.this);

                dialog.setTitle("제품코드 입력");
                dialog.setView(editText);

                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = editText.getText().toString();

                        if (str.length() > 0) {
                            txt_serialNum.setText(str);
                            PublicValues.configValues.remove(0);
                            PublicValues.configValues.add(0, str);

                            editText.setText("");

                            saveItemsToFile();
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, null).create().show();
                hideSystemUI();
            }
        });

        txt_stictionValue.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                final EditText editText = new EditText(MainActivity.this);

                dialog.setTitle("Stiction Tune");   //질문
                dialog.setView(editText);

                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = editText.getText().toString();

                        if (str.length() > 0) {
                            PublicValues.configValues.remove(1);
                            PublicValues.configValues.add(1, str);
                            editText.setText("");

                            controlBoard("F2010FF0" + PublicFunctions.intToOneByte(Integer.parseInt(PublicValues.configValues.get(1))) + "0000000000000000000000000000" + "FE");

                            saveItemsToFile();
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, null).create().show();
                hideSystemUI();
            }
        });


        txt_clubId.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                final EditText editText = new EditText(MainActivity.this);

                dialog.setTitle("Club");
                dialog.setView(editText);

                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = editText.getText().toString();

                        if (str.length() > 0) {
                            txt_clubId.setText(str);
                            PublicValues.club_id = str;
                            PublicValues.configValues.remove(3);
                            PublicValues.configValues.add(3, str);
                            editText.setText("");


                            saveItemsToFile();
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, null).create().show();
                hideSystemUI();
            }
        });

        txt_Email.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                final EditText editText = new EditText(MainActivity.this);

                dialog.setTitle("이메일 입력");
                dialog.setView(editText);

                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = editText.getText().toString();

                        if (str.length() > 0) {
                            txt_Email.setText(str);
                            PublicValues.configValues.remove(4);
                            PublicValues.configValues.add(4, str);

                            editText.setText("");

                            saveItemsToFile();
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, null).create().show();
                hideSystemUI();
            }
        });

        GetVersionInfo();

        if(PublicValues.userId != null && PublicValues.userId.length() > 0){
            btn_logOut.setVisibility(View.VISIBLE);
            txt_userInfo.setVisibility(View.VISIBLE);
            txt_userInfo.setText(PublicValues.userName);
            btn_login.setVisibility(View.GONE);
            btn_measure.setVisibility(View.VISIBLE);
            btn_exerciseMode.setBackgroundResource(R.drawable.btn_exercise);
        }
        else{
            btn_logOut.setVisibility(View.GONE);
            txt_userInfo.setVisibility(View.GONE);
            btn_measure.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
            btn_exerciseMode.setBackgroundResource(R.drawable.btn_guest);
            if(layout_update.getVisibility() == View.VISIBLE)
                layout_update.setVisibility(View.GONE);
        }











        timeT_main = new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("타이머", "" + bool_breakStatus + "   " + i_readCnt);

                        //Log.e("타이머", "" + ((ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).getAppTasks().size());
                        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);    //질문

                        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
                        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
                        while(deviceIterator.hasNext()){
                            UsbDevice device = deviceIterator.next();
                            Log.e(TAG, "QQQ = " + device.getProductName() + "\t111 " + device.getVendorId() + "\t     222 = " + device.getProductId());

                            //your code
                        }
                        Log.e(TAG, "=================================================== ");


                        //Log.i(TAG, "timer = " + b_statusBit);
                        if(mUpdatePlease && PublicValues.director.equals("1")){
                            //layout_update.setVisibility(View.VISIBLE);
                        }
                        else    layout_update.setVisibility(View.GONE);


                        if(i_btnAdminCnt > 0)       i_btnAdminDelayCnt++;
                        if(i_btnAdminDelayCnt > 12){
                            i_btnAdminCnt = 0;
                            i_btnAdminDelayCnt = 0;
                        }

                        txt_stictionValue.setText(""+i_stiction);
                        txt_loadCellValue.setText(""+i_loadCell);
                        txt_loadCellValueMain.setText(""+i_loadCell);



                        //if (isConnected(getApplicationContext())) {//네트워크 연결유무 표기
                        if (!Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()).contains("0.0.0.0")) {
                            btn_wifi.setBackgroundResource(R.drawable.btn_wifi_able);
                            txt_wifiName.setText(wifiManager.getConnectionInfo().getSSID());
                            txt_ipAddress.setText("Network : " + wifiManager.getConnectionInfo().getSSID() + "  " + "IP Address : " + Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()) + "\n" + PublicFunctions.getMacAddress("wlan0"));
                        } else {
                            txt_ipAddress.setText("");
                            btn_wifi.setBackgroundResource(R.drawable.btn_wifi_unable);
                        }



                        if(PublicValues.userId != null && PublicValues.userId.length() > 0){
                            btn_logOut.setVisibility(View.VISIBLE);
                            txt_userInfo.setVisibility(View.VISIBLE);
                            txt_userInfo.setText(PublicValues.userName);
                            btn_login.setVisibility(View.GONE);
                            btn_measure.setVisibility(View.VISIBLE);
                            btn_exerciseMode.setBackgroundResource(R.drawable.btn_exercise);
                        }
                        else{
                            btn_logOut.setVisibility(View.GONE);
                            txt_userInfo.setVisibility(View.GONE);
                            btn_measure.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                            btn_exerciseMode.setBackgroundResource(R.drawable.btn_guest);
                            if(layout_update.getVisibility() == View.VISIBLE)
                                layout_update.setVisibility(View.GONE);
                        }

                        if ((b_statusBit & 0xEE) == 0xEE) {
                            btn_reset.setVisibility(View.VISIBLE);
                            txt_errorCode.setVisibility(View.VISIBLE);
                            txt_errorCode.setText("ERROR : " + "0x" + String.format("%02X", b_errorBit));
                        } else {
                            btn_reset.setVisibility(View.GONE);
                            txt_errorCode.setVisibility(View.GONE);
                        }



                        if(bool_measureFlag){
                            if(b_workoutBit == 1 && !bool_breakStatus){ //질문
                                bool_measureFlag = false;
                                //if(PublicValues.club_id.equals("1144")) {
                                //    Intent intent = new Intent(MainActivity.this, MeasureTestActivity2.class);
                                //    startActivity(intent);
                                //} else {
                                    Intent intent = new Intent(MainActivity.this, MeasureTestActivity.class);
                                    startActivity(intent);
                                //}


                                if (timer_main != null) timer_main.cancel();
                                timer_main = null;
                                progressDialog.DialogStop();

                                finishAffinity();
                            }
                            else{
                                if(!bool_breakStatus){
                                    controlBoard(MOTER_STOP_SERIAL);
                                }
                                else if(b_workoutBit != 1){
                                    controlBoard("F2010F030101F407D000000000000000000001FE");
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    controlBoard(MOTER_START_SERIAL);
                                }
                                else {

                                }
                            }
                        }

                        if(usbService == null){
                            //setFilters();
                            startService(UsbService.class, usbConnection, null);
                        }
                        Log.e("bbot", PublicValues.initFlag + "  " + PublicValues.BootProcess + "  " + UsbServiceFlag + PublicValues.ResetFlag);
                        //장비 초기 부팅 설정
                        if(PublicValues.ResetFlag){
                            if(PublicValues.BootProcess && bool_breakStatus ){

                                controlBoard("F2010F030401F401F400000000000000000001FE");   //질문

                                controlBoard(MOTER_START_SERIAL);
                            }
                            else if(!bool_breakStatus && b_statusBit == 3){
                                PublicValues.ResetFlag = false;
                                PublicValues.BootProcess = false;
                                initDialog.initDialogStop();
                                Log.i(TAG, "initDialog.initDialogStop(); !!!");
                                hideSystemUI();
                            }
                        }
                        else{
                            if (!PublicValues.initFlag && PublicValues.BootProcess && UsbServiceFlag) {
                                i_bootCnt++;

                                if(i_bootCnt > 5 && i_bootCnt < 30){
                                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    loadItemsFromFile();
                                    saveItemsToFile();
                                    txt_serialNum.setText(PublicValues.configValues.get(0));
                                    txt_clubId.setText(PublicValues.configValues.get(3));
                                    txt_Email.setText(PublicValues.configValues.get(4));
                                }
                                else if(!mCSTFlag && !mFSTFlag && !mDoinFlag) {
                                    String hexString = "CST";
                                    Log.e(TAG, "CST");
                                    byte[] datapacket = hexString.getBytes();
                                    usbService.write(datapacket);
                                    i_cstCnt++;
                                    if(i_cstCnt == 30) {
                                        i_cstCnt = 0;
                                        mCSTFlag = true;
                                    }
                                    //mCSTFlag = true;
                                    /*
                                    i_cstCnt++;
                                    if(i_cstCnt == 30) {
                                        i_cstCnt = 0;
                                        mCSTFlag = true;
                                    }

                                     */
                                    //} else if (i_bootCnt < 10) {
                                } else if(mCSTFlag && !mFSTFlag && !mDoinFlag) {
                                    String hexString = "FST";
                                    Log.e(TAG, "FST");
                                    byte[] datapacket = hexString.getBytes();
                                    usbService.write(datapacket);
                                    i_fstCnt++;
                                    if(i_fstCnt == 30) {
                                        i_fstCnt = 0;
                                        mFSTFlag = true;
                                        restart = true;
                                    }
                                    //mFSTFlag = true;
                                    /*
                                    i_fstCnt++;
                                    if(i_fstCnt == 30) {
                                        i_fstCnt = 0;
                                        mFSTFlag = true;
                                        restart = true;
                                    }

                                     */
                                } else if(mCSTFlag && mFSTFlag){
                                    if(!mFirstConnectionCheck) {
                                        mFirstConnectionCheck = true;
                                        if(!restart) {
                                            restartApp();
                                        }
                                        try {
                                            Thread.sleep(5000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    Log.e(TAG, "i_bootCnt3 = " + i_bootCnt);
                                    controlBoard("F2010F01000000000000000000000000000001FE");
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    controlBoard(MOTER_START_SERIAL);
                                }
                                if(i_bootCnt > 300) {
                                    i_bootCnt = 0;
                                    timer_main.cancel();
                                    restartApp();
                                    return;
                                }
                            } else if(PublicValues.initFlag && PublicValues.BootProcess && bool_breakStatus){

                                initDialog.initDialogStop();


                                PublicValues.BootProcess = false;
                                i_bootCnt = 0;
                                controlBoard("F2010F030401F401F400000000000000000001FE");
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                controlBoard(MOTER_START_SERIAL);
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                controlBoard("F2010FF0" + PublicFunctions.intToOneByte(100) + "0000000000000000000000000000" + "FE");   //질문  이렇게하면 무게 1kg그램인지
                            }
                        }
                    }
                });
            }
        };


        timer_main = null;

        timer_main = new Timer();
        //if(!PublicValues.initFlag){
            //timer_main.schedule(timeT_main, 0, 250);
        //}
        //else{

        if(((ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).getAppTasks().size() != 1) {
            restartApp();
            ((ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE)).clearApplicationUserData();


        }
        else {

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()); //이거 2줄에서 에러 남
            //SharedPreferences in credential encrypted storage are not available until after user is unlocked
            loadItemsFromFile();    //처음 에러 날때
            saveItemsToFile();
            Log.e("섀어드", sharedPreferences.getAll().size() + "");
            txt_serialNum.setText(PublicValues.configValues.get(0));
            txt_clubId.setText(PublicValues.configValues.get(3));
            txt_Email.setText(PublicValues.configValues.get(4));
            timer_main.schedule(timeT_main, 0, 250);
        }
        //}


        mainHandler = new MainActivity.MainHandler(this);

    }




    @Override
    public void onResume() {
        Log.e(TAG, "onResume !!!");


        setFilters();
        startService(UsbService.class, usbConnection, null);


        super.onResume();



        hideSystemUI();

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);

        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
/*        while(deviceIterator.hasNext()){
            UsbDevice device = deviceIterator.next();
            Log.e(TAG, "QQQ = " + device.getProductName());
            Log.e(TAG, "QQQ = " + device.getVendorId());
            Log.e(TAG, "QQQ = " + device.getProductId());


            //your code
        }*/


    }

    public void restartApp() {
        this.finishAffinity();
        Intent miniplus = new Intent(this, MainActivity.class);
        startActivity(miniplus);
        System.exit(0);
    }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause !!!");
        progressDialog.DialogStop();

        super.onPause();

        unregisterReceiver(usbReceiver);
        unbindService(usbConnection);

    }

    @Override
    protected void onStop() {
        initDialog.initDialogStop();
        Log.i(TAG, "initDialog.initDialogStop(); !!!");
        if (timer_main != null) timer_main.cancel();
        timer_main = null;
        super.onStop();
    }

}
