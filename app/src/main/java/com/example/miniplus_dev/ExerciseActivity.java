package com.example.miniplus_dev;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.StrictMode;
import android.provider.ContactsContract;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniplus_dev.setting.DampingFrag;
import com.example.miniplus_dev.setting.EccentricFrag1;
import com.example.miniplus_dev.setting.EccentricFrag2;
import com.example.miniplus_dev.setting.EccentricFrag3;
import com.example.miniplus_dev.setting.IsokineticBiFrag;
import com.example.miniplus_dev.setting.IsokineticFrag;
import com.example.miniplus_dev.setting.LoginPadFragment;
import com.example.miniplus_dev.setting.ModeSelectFragment;
import com.example.miniplus_dev.setting.SpringFrag;
import com.example.miniplus_dev.setting.VibrationFrag;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.listener.OnDrawListener;
import com.github.mikephil.charting.utils.EntryXComparator;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class ExerciseActivity extends Activity implements LoginPadFragment.OnTimePickerSetListener{

    CRC16Modbus crc = new CRC16Modbus();

    private String TAG = ExerciseActivity.class.getSimpleName();

    private int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private String MOTER_START_SERIAL = "F2010FA1000000000000000000000000000001FE";
    private String MOTER_STOP_SERIAL = "F2010FA2000000000000000000000000000001FE";

    final String [] MODE_NAMES = new String[]{"ISOTONIC", "ISOKINETIC", "ISOKINETIC-BI", "ECCENTRIC", "ECCENTRIC", "ECCENTRIC", "SPRING", "DAMPING", "VIBRATION"};

    PublicFunctions publicFunctions = new PublicFunctions();

    public static FragmentManager fragmentManager;

    ProgressDialog progressDialog = new ProgressDialog(this);
    AppPopUpDialog appPopUpDialog = new AppPopUpDialog(this);

    LineChart chart_lineChart;
    BarChart chart_barChart;

    LinearLayout [] layout_values = new LinearLayout[3];

    Button btn_selectActibity;
    Button btn_mode, btn_login, btn_logout, btn_wifi, btn_guideVideo;
    Button btn_force, btn_speed, btn_power;
    Button btn_type;
    Button btn_reset;
    Button btn_screenShot;

    TextView txt_userName;
    TextView txt_mode, txt_wifiName;
    TextView txt_avgForce, txt_avgSpeed, txt_avgPower;
    TextView txt_maxForce, txt_maxSpeed, txt_maxPower;
    TextView txt_rep, txt_set;
    TextView txt_workTime, txt_breakTime;
    TextView txt_errorCode;
    TextView txt_set_avgforce, txt_set_avgspeed, txt_set_avgpower, txt_set_maxforce, txt_set_maxspeed, txt_set_maxpower;
    TextView txt_Worktype;

    Spinner workSelector;

    Spinner spWorkSelector; //glenn 추

    View rootView;
    int more = 0;
    int too = 0;
    int mSendRepsData = 0;
    String [] screenFile;

    boolean flag = false;

    boolean [] bool_valuesFlag = new boolean[3];
    boolean [] bool_valuesOldFlag = new boolean[3];
    boolean bool_networkConChk = true;
    boolean bool_breakStatus = false;
    boolean bool_changeModeFlag, bool_changeFragFlag;
    boolean bool_setFlag;
    boolean bool_resetFlag;


    byte b_errorBit = 0x00;
    byte b_statusBit = 0x00;
    byte b_workStatusBit = 0x00;
    byte b_workoutBit = 0x00;

    long l_nowTime;
    long l_oldTime;

    int i_selMode;
    int i_type = 0;

    int i_rep, i_set, i_rep_org = 0;
    int i_repsPosCnt, i_repsNegCnt = 0;

    int i_activeMoveCnt = 0;


    double d_velocity;
    double d_position;
    double d_loadcell;

    double d_avgForce, d_avgSpeed, d_avgPower = 0;
    double d_maxForce, d_maxSpeed, d_maxPower = 0;


    String str_fragSerial = "F2010FA1000000000000000000000000000001FE";
    String strBreak = "";

    ArrayList<BarEntry> arrE_barEntry = new ArrayList<>();

    List<Float> listF_avrForce = new ArrayList<>();
    List<Float> listF_avrSpeed = new ArrayList<>();
    List<Float> listF_avrPower = new ArrayList<>();

    List<Double> listD_avgForce = new ArrayList<Double>();
    List<Double> listD_avgSpeed = new ArrayList<Double>();
    List<Double> listD_avgPower = new ArrayList<Double>();

    ArrayList<Entry> listE_forceDataList = new ArrayList<>();
    ArrayList<Entry> listE_speedDataList = new ArrayList<>();
    ArrayList<Entry> listE_powerDataList = new ArrayList<>();

    List<ArrayList> listArr_forceEntry = new ArrayList<>();
    List<ArrayList> listArr_speedEntry = new ArrayList<>();
    List<ArrayList> listArr_powerEntry = new ArrayList<>();

    List<String> listArr_avgForceData = new ArrayList<>();
    List<Double> d_listArr_avgForceData = new ArrayList<>();
    List<String> listArr_avgSpeedData = new ArrayList<>();
    List<Double> d_listArr_avgSpeedData = new ArrayList<>();
    List<String> listArr_avgPowerData = new ArrayList<>();
    List<Double> d_listArr_avgPowerData = new ArrayList<>();

    List<String> listArr_maxForceData = new ArrayList<>();
    List<Double> d_listArr_maxForceData = new ArrayList<>();
    List<String> listArr_maxSpeedData = new ArrayList<>();
    List<Double> d_listArr_maxSpeedData = new ArrayList<>();
    List<String> listArr_maxPowerData = new ArrayList<>();
    List<Double> d_listArr_maxPowerData = new ArrayList<>();

    ///////////////////////////////선그래프 데이터
    ArrayList<Double> list_Position = new ArrayList<>();
    ArrayList<Double> list_Force = new ArrayList<>();
    ArrayList<Double> list_Speed = new ArrayList<>();
    ArrayList<Double> list_Power = new ArrayList<>();

    ArrayList<ArrayList> listRep_Position = new ArrayList<>();
    ArrayList<ArrayList> listRep_Force = new ArrayList<>();
    ArrayList<ArrayList> listRep_Speed = new ArrayList<>();
    ArrayList<ArrayList> listRep_Power = new ArrayList<>();

    List<ArrayList> listSet_Position = new ArrayList<>();
    List<ArrayList> listSet_Force = new ArrayList<>();
    List<ArrayList> listSet_Speed = new ArrayList<>();
    List<ArrayList> listSet_Power = new ArrayList<>();
    ///////////////////////////////

    ///////////////////////////////바그래프 데이터
    ArrayList<Float> listRep_BarForce = new ArrayList<>();
    ArrayList<Float> listRep_BarSpeed = new ArrayList<>();
    ArrayList<Float> listRep_BarPower = new ArrayList<>();

    List<ArrayList> listSet_BarForce = new ArrayList<>();
    List<ArrayList> listSet_BarSpeed = new ArrayList<>();
    List<ArrayList> listSet_BarPower = new ArrayList<>();
    ///////////////////////////////

    ///////////////////////////////전후 무게 세팅
    boolean befWorkoutFlag = false; //데이터 1번만 넣을수 있게 + 전 데이터 들어갔는지 확인
    int befWorkCnt = 0;
    int aftWorkCnt = 0;

    ArrayList<String> listWeight_bef = new ArrayList<>();
    ArrayList<String> listSpeed_bef = new ArrayList<>();
    ArrayList<String> listUpWeight_bef = new ArrayList<>();
    ArrayList<String> listDnWeight_bef = new ArrayList<>();
    ArrayList<String> listStart_bef = new ArrayList<>();
    ArrayList<String> listEnd_bef = new ArrayList<>();
    ArrayList<String> listConSpeed_bef = new ArrayList<>();
    ArrayList<String> listEccSpeed_bef = new ArrayList<>();
    ArrayList<String> listConstant_bef = new ArrayList<>();
    ArrayList<String> listSetpos_bef = new ArrayList<>();
    ArrayList<String> listNowpos_bef = new ArrayList<>();
    ArrayList<String> listFrequency_bef = new ArrayList<>();
    ArrayList<String> listVariation_bef = new ArrayList<>();

    ArrayList<String> listWeight_aft = new ArrayList<>();
    ArrayList<String> listSpeed_aft = new ArrayList<>();
    ArrayList<String> listUpWeight_aft = new ArrayList<>();
    ArrayList<String> listDnWeight_aft = new ArrayList<>();
    ArrayList<String> listStart_aft = new ArrayList<>();
    ArrayList<String> listEnd_aft = new ArrayList<>();
    ArrayList<String> listConSpeed_aft = new ArrayList<>();
    ArrayList<String> listEccSpeed_aft = new ArrayList<>();
    ArrayList<String> listConstant_aft = new ArrayList<>();
    ArrayList<String> listSetpos_aft = new ArrayList<>();
    ArrayList<String> listNowpos_aft = new ArrayList<>();
    ArrayList<String> listFrequency_aft = new ArrayList<>();
    ArrayList<String> listVariation_aft = new ArrayList<>();


    //데이터 전송용 temp 리스트
    ArrayList<String> listWeightDataValues_bef = new ArrayList<>();
    ArrayList<String> listSpeedDataValues_bef = new ArrayList<>();
    ArrayList<String> listUpWeightDataValues_bef = new ArrayList<>();
    ArrayList<String> listDnWeightDataValues_bef = new ArrayList<>();
    ArrayList<String> listStartDataValues_bef = new ArrayList<>();
    ArrayList<String> listEndDataValues_bef = new ArrayList<>();
    ArrayList<String> listConSpeedDataValues_bef = new ArrayList<>();
    ArrayList<String> listEccSpeedDataValues_bef = new ArrayList<>();
    ArrayList<String> listConstantDataValues_bef = new ArrayList<>();
    ArrayList<String> listSetposDataValues_bef = new ArrayList<>();
    ArrayList<String> listFrequencyDataValues_bef = new ArrayList<>();
    ArrayList<String> listVariationDataValues_bef = new ArrayList<>();


    ArrayList<String> listWeightDataValues_aft = new ArrayList<>();
    ArrayList<String> listSpeedDataValues_aft = new ArrayList<>();
    ArrayList<String> listUpWeightDataValues_aft = new ArrayList<>();
    ArrayList<String> listDnWeightDataValues_aft = new ArrayList<>();
    ArrayList<String> listStartDataValues_aft = new ArrayList<>();
    ArrayList<String> listEndDataValues_aft = new ArrayList<>();
    ArrayList<String> listConSpeedDataValues_aft = new ArrayList<>();
    ArrayList<String> listEccSpeedDataValues_aft = new ArrayList<>();
    ArrayList<String> listConstantDataValues_aft = new ArrayList<>();
    ArrayList<String> listSetposDataValues_aft = new ArrayList<>();
    ArrayList<String> listFrequencyDataValues_aft = new ArrayList<>();
    ArrayList<String> listVariationDataValues_aft = new ArrayList<>();


    ArrayList<List> listPosDataValues     = new ArrayList<>();
    ArrayList<List> listForceDataValues   = new ArrayList<>();
    ArrayList<List> listSpeedDataValues   = new ArrayList<>();
    ArrayList<List> listPowerDataValues   = new ArrayList<>();

/*
    존재이유 불명

    List<ArrayList> listSet_Weight_bef = new ArrayList<>();
    List<ArrayList> listSet_Speed_bef = new ArrayList<>();
    List<ArrayList> listSet_UpWeight_bef = new ArrayList<>();
    List<ArrayList> listSet_DnWeight_bef = new ArrayList<>();
    List<ArrayList> listSet_Start_bef = new ArrayList<>();
    List<ArrayList> listSet_End_bef = new ArrayList<>();
    List<ArrayList> listSet_ConSpeed_bef = new ArrayList<>();
    List<ArrayList> listSet_EccSpeed_bef = new ArrayList<>();
    List<ArrayList> listSet_Constant_bef = new ArrayList<>();
    List<ArrayList> listSet_Setpos_bef = new ArrayList<>();
    List<ArrayList> listSet_Nowpos_bef = new ArrayList<>();
    List<ArrayList> listSet_Frequency_bef = new ArrayList<>();
    List<ArrayList> listSet_Variation_bef = new ArrayList<>();

    List<ArrayList> listSet_Weight_aft = new ArrayList<>();
    List<ArrayList> listSet_Speed_aft = new ArrayList<>();
    List<ArrayList> listSet_UpWeight_aft = new ArrayList<>();
    List<ArrayList> listSet_DnWeight_aft = new ArrayList<>();
    List<ArrayList> listSet_Start_aft = new ArrayList<>();
    List<ArrayList> listSet_End_aft = new ArrayList<>();
    List<ArrayList> listSet_ConSpeed_aft = new ArrayList<>();
    List<ArrayList> listSet_EccSpeed_aft = new ArrayList<>();
    List<ArrayList> listSet_Constant_aft = new ArrayList<>();
    List<ArrayList> listSet_Setpos_aft = new ArrayList<>();
    List<ArrayList> listSet_Nowpos_aft = new ArrayList<>();
    List<ArrayList> listSet_Frequency_aft = new ArrayList<>();
    List<ArrayList> listSet_Variation_aft = new ArrayList<>();
*/

    ///////////////////////////////

    ///////////////////////////////평균최대 데이터

    ///////////////////////////////

    ///////////////////////////////휴식운동 시간

    ///////////////////////////////

    private UsbService usbService;
    Timer timer_main;
    private MainHandler mainHandler;
    private StringBuffer sb_packet = new StringBuffer();

    boolean screenFlag = false;
    boolean mailFlag = false;

    ModeSelectFragment modeSelectFragment = new ModeSelectFragment();

    IsokineticFrag isokineticFrag;
    IsokineticBiFrag isokineticBiFrag;
    EccentricFrag1 eccentricFrag1;
    EccentricFrag2 eccentricFrag2;
    EccentricFrag3 eccentricFrag3;
    SpringFrag springFrag;
    DampingFrag dampingFrag;
    VibrationFrag vibrationFrag;

    NetworkInfo activeNetwork;
    String str_wifiPass = "";

    WifiManager wifiManager;

    @Override
    public void onTimePickerSet(boolean LogonFlag) {
        Log.e("LogonFlag", LogonFlag + "");
        if(LogonFlag) {
            btn_logout.setVisibility(View.VISIBLE);
            txt_userName.setVisibility(View.VISIBLE);
            txt_userName.setText(PublicValues.userName);
            btn_login.setVisibility(View.GONE);
            //if(PublicValues.club_id.equals("1059") || PublicValues.club_id.equals("1000")) {
            if (PublicValues.director.equals("1")) {
                btn_screenShot.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void DoinBackLoading(boolean LoadinFlag) {
        if (LoadinFlag) {
            btn_login.setEnabled(false);
            btn_wifi.setEnabled(false);
        } else {
            btn_login.setEnabled(true);
            btn_wifi.setEnabled(true);
        }
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED:
                    break;
                case UsbService.ACTION_NO_USB:
                    break;
                case UsbService.ACTION_USB_DISCONNECTED:
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED:
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


    private ListView searchWifi() {

        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        final ListView WIFI_LIST = new ListView(ExerciseActivity.this);

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
                        String networkSSID = MainActivity.sStr_wifiSelItem;
                        WifiConfiguration conf = new WifiConfiguration();

                        List<ScanResult> results = wifiManager.getScanResults();
                        final int N = results.size();

                        Log.v(TAG, "Wi-Fi Scan Results ... Count:" + N + " " + networkSSID);
                        MainActivity.sArr_wifiName.clear();
                        MainActivity.sArr_wifiSecurity.clear();
                        for (int i = 0; i < N; ++i) {
                            MainActivity.sArr_wifiName.add(results.get(i).SSID.toString());
                            MainActivity.sArr_wifiSecurity.add(results.get(i).capabilities);
                         /*   if (networkSSID.equals(results.get(i).SSID)) {
                                connectWiFi(networkSSID, networkPass, results.get(i).capabilities);
                                Log.v(TAG, "  BSSID       =" + results.get(i).BSSID);
                                Log.v(TAG, "  SSID        =" + results.get(i).SSID);
                                Log.v(TAG, "  Capabilities=" + results.get(i).capabilities);
                                Log.v(TAG, "  Frequency   =" + results.get(i).frequency);
                                Log.v(TAG, "  Level       =" + results.get(i).level);
                                Log.v(TAG, "---------------");
                            }*/
                        }
                    }
                    if (MainActivity.sArr_wifiName.size() > 0) {
                        ArrayAdapter adapter = new ArrayAdapter(ExerciseActivity.this, android.R.layout.simple_list_item_1, MainActivity.sArr_wifiName);
                        WIFI_LIST.setAdapter(adapter);
                    }
                }
            }, filter);
            if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
                // start WiFi Scan
            }
            wifiManager.startScan();
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


    int mSetWeight, mSetWeightDn, mSetSpeed, mSetTention = 0;

    public class InsertFITTWorkout extends AsyncTask<Void, Integer, Void> {

        String data = "";

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... unused) {


            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            String timeNow = now_time.format(date);
            String sendWeightUp = "" + (mSetWeight/100);
            String sendWeightDn = "" + (mSetWeightDn/100);
            String sendSetSpeed = "" + (mSetSpeed/10);
            String sendSetTention = "" + mSetTention;
            String sendType = "MPTYPE00"+ i_type;
            String sendPart = "MPPART000";
            String sendMode = "MPMODE00"+ b_workoutBit;
            String avgForce = txt_avgForce.getText().toString();
            String maxForce = txt_maxForce.getText().toString();
            String avgSpeed = txt_avgSpeed.getText().toString();
            String maxSpeed = txt_maxSpeed.getText().toString();
            String reps         = txt_rep.getText().toString();
            String sets         = txt_set.getText().toString();
            String workTime     = txt_workTime.getText().toString();
            String breakTime    = strBreak;

            String param = "";
            String s_url = "";
            Log.e(TAG, PublicValues.userId);
            s_url = "http://211.253.30.245/php/fitt/fitt_miniplus.php";

            param = "users_id="            + PublicValues.userSecondId +
                    "&inputType="          + "exercise" +
                    "&workoutmode_code="   + sendMode +
                    "&workouttype_code="   + sendType +
                    "&reps="               + reps +
                    "&workouttime="        + workTime +
                    "&breaktime="          + breakTime +
                    "&weight_up="          + sendWeightUp +
                    "&weight_down="        + sendWeightDn;

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
                Log.e("RECV", data);

                if (data.contains("true")) {

                    Log.e("RESULT", "성공적으로 처리되었습니다!");
                } else {
                    Log.e("RESULT", "에러 발생!! ERRCODE = " + data);
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
            String sendWeightUp = "" + (mSetWeight/100);
            String sendWeightDn = "" + (mSetWeightDn/100);
            String sendSetSpeed = "" + (mSetSpeed/10);
            String sendSetTention = "" + mSetTention;
            String sendType = "MPTYPE00"+ i_type;
            String sendPart = "MPPART000";
            String sendMode = "MPMODE00"+ b_workoutBit;
            String avgForce = txt_avgForce.getText().toString();
            String maxForce = txt_maxForce.getText().toString();
            String avgSpeed = txt_avgSpeed.getText().toString();
            String maxSpeed = txt_maxSpeed.getText().toString();
            String reps         = txt_rep.getText().toString();
            String sets         = txt_set.getText().toString();
            String workTime     = txt_workTime.getText().toString();
            String breakTime    = strBreak;

            String param = "";
            String s_url = "";
            Log.e(TAG, PublicValues.userId);
            s_url = "http://211.253.30.245/php/iot/miniplus_iot.php";

            param = "method="              + "workout" +
                    "&users_id="           + PublicValues.userSecondId +
                    "&workout_datetime="   + timeNow +
                    "&workoutmode_code="   + sendMode +
                    "&workouttype_code="   + sendType +
                    "&reps="               + reps +
                    "&workouttime="        + workTime +
                    "&breaktime="          + breakTime +
                    "&weight_up="          + sendWeightUp +
                    "&weight_down="        + sendWeightDn;

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
                    Log.e("RESULT", "에러 발생!!! ERRCODE = " + data);
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

    public class InsertWorkoutData extends AsyncTask<Void, Integer, Void> {

        String data = "";

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... unused) {


            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            String timeNow = now_time.format(date);
            String sendWeightUp = "" + (mSetWeight/100);
            String sendWeightDn = "" + (mSetWeightDn/100);
            String sendSetSpeed = "" + (mSetSpeed/10);
            String sendSetTention = "" + mSetTention;
            String sendType = "MPTYPE00"+ i_type;
            String sendPart = "MPPART000";
            String sendMode = "MPMODE00"+ b_workoutBit;
            String avgForce = txt_avgForce.getText().toString();
            String maxForce = txt_maxForce.getText().toString();
            String avgSpeed = txt_avgSpeed.getText().toString();
            String maxSpeed = txt_maxSpeed.getText().toString();
            String reps         = txt_rep.getText().toString();
            String sets         = txt_set.getText().toString();
            String workTime     = txt_workTime.getText().toString();
            String breakTime    = strBreak;

            String param;
            String s_url;
            Log.e(TAG, PublicValues.userId);
            if(PublicValues.userId.length() > 0){
                s_url = "http://211.253.30.245/equipment/kor/miniplus/Miniplus_addSetData.php";

                param = "&users_id=" + PublicValues.userId +
                        "&workout_datetime="    +timeNow +
                        "&weight_up="   + sendWeightUp+
                        "&weight_dn="   + sendWeightDn+
                        "&set_speed="   + sendSetSpeed+
                        "&set_tension=" + sendSetTention+
                        "&avg_force="   + avgForce+
                        "&avg_speed="   + avgSpeed+
                        "&max_force="   + maxForce+
                        "&max_speed="   + maxSpeed+
                        "&set_seq="     + sets+
                        "&reps="        + reps+
                        "&workouttime=" + workTime+
                        "&breaktime="   + breakTime+
                        "&type="        + sendType+
                        "&part="        + sendPart+
                        "&mode="        + sendMode +
                        "&is_schedule="        + "1" +
                        "&is_measure="        + "0" +
                        "&equipment_serial=" + PublicValues.serialNumber +
                        "&club_id=" + PublicValues.club_id;

                InsertWorkoutOldData insertWorkoutOldData = new InsertWorkoutOldData();
                insertWorkoutOldData.execute();
            }
            else{


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
                    Log.e("RESULT", "에러 발생!!!! ERRCODE = " + data);
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
            Log.e("여기는 들어가니", "ㄴㄴ");


        }
    }

    String mSendSetResultJson = "";
    public class InsertWorkoutOldData extends AsyncTask<Void, Integer, Void> {

        String data = "";

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... unused) {


            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            String timeNow = now_time.format(date);
            String sendWeightUp = "" + (mSetWeight/100);
            String sendWeightDn = "" + (mSetWeightDn/100);
            String sendSetSpeed = "" + (mSetSpeed/10);
            String sendSetTention = "" + mSetTention;
            String sendType = "MPTYPE00"+ i_type;
            String sendPart = "MPPART000";
            String sendMode = "MPMODE00"+ b_workoutBit;
            String avgForce = String.format("%.1f", publicFunctions.calculateAverage(d_listArr_avgForceData));
            String maxForce = String.format("%.1f", publicFunctions.calculateMax(d_listArr_maxForceData));
            String avgSpeed = String.format("%.1f", publicFunctions.calculateAverage(d_listArr_avgSpeedData));
            String maxSpeed = String.format("%.1f", publicFunctions.calculateMax(d_listArr_maxSpeedData));
            String avgPower = String.format("%.1f", publicFunctions.calculateAverage(d_listArr_avgPowerData));
            String maxPower = String.format("%.1f", publicFunctions.calculateMax(d_listArr_maxPowerData));
            String reps         = txt_rep.getText().toString();
            String sets         = txt_set.getText().toString();
            String workTime     = txt_workTime.getText().toString();
            String breakTime    = strBreak;
            String part         = "0";
            if(PublicValues.club_id.equals("1144")) {
                //part = (workSelector.getSelectedItemPosition() + 1) +"";
            }
            if(flag) {
                part = (spWorkSelector.getSelectedItemPosition() + 1) + ""; //glenn 추가
            }
            String param;
            String s_url;
            Log.e(TAG, PublicValues.userId);

            s_url = "http://211.253.30.245/equipment/kor/miniplus/Miniplus_u_addWorkData.php";

            param = "&users_id=" + PublicValues.userId +
                    "&workout_datetime="    +timeNow +
                    "&weight_up="   + sendWeightUp+
                    "&weight_down="   + sendWeightDn+
                    "&set_speed="   + sendSetSpeed+
                    "&set_tension=" + sendSetTention+
                    "&avg_force="   + avgForce+
                    "&avg_speed="   + avgSpeed+
                    "&avg_power="   + avgPower+
                    "&max_force="   + maxForce+
                    "&max_speed="   + maxSpeed+
                    "&max_power="   + maxPower+
                    "&set_seq="     + sets+
                    "&reps="        + reps+
                    "&workouttime=" + workTime+
                    "&breaktime="   + breakTime+
                    "&type="        + sendType+
                    "&part="        + sendPart+
                    "&mode="        + sendMode +
                    "&workoutmode_code="        + sendMode+
                    "&workouttype_code="        + sendType +
                    "&is_schedule="        + "1" +
                    "&is_measure="        + "0" +
                    "&equipment_serial=" + PublicValues.serialNumber +
                    "&club_id=" + PublicValues.club_id +
                    "&workoutpart_code=" + part;

            Log.e("POST old", param);
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
                    Log.e("RESULT", "에러 발생! ERRCODE = " + data);
                }
                mSendSetResultJson = data;
                Log.e("제이쓴", data);
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

            if(PublicValues.club_id.equals("1121") && !PublicValues.userSecondId.equals("0")) {
                InsertESEWorkout insertESEWorkout = new InsertESEWorkout();
                insertESEWorkout.execute();
                Log.e("여기는 들어가니", "oo");
            } else {
                InsertWorkoutRepData insertWorkoutRepData = new InsertWorkoutRepData();
                insertWorkoutRepData.execute();
            }
        }
    }
    private void checkCount(ArrayList<String> a, int i) {
        boolean check = false;
        if (mSendRepsData != a.size()) {
            if(mSendRepsData != a.size() - 1) {
                check = true;
            }
            Log.e("갯수 차이", mSendRepsData + "   " + a.size());
            if(mSendRepsData > a.size()) {
                for (int j = a.size(); j < mSendRepsData; j++) {
                    a.add("--");
                }
            } else if(mSendRepsData < a.size()) {
                for (int j = a.size() - 1; j >= mSendRepsData; j--) {
                    a.remove(j);
                }
            }
            if(check) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (i == 0)
                            Toast.makeText(getApplicationContext(), "전 WEIGHT 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 1)
                            Toast.makeText(getApplicationContext(), "전 SPEED 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 2)
                            Toast.makeText(getApplicationContext(), "전 UP-WEIGHT 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 3)
                            Toast.makeText(getApplicationContext(), "전 DOWN-WEIGHT 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 4)
                            Toast.makeText(getApplicationContext(), "전 START-POS 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 5)
                            Toast.makeText(getApplicationContext(), "전 END-POS 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 6)
                            Toast.makeText(getApplicationContext(), "전 CON-SPEED 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 7)
                            Toast.makeText(getApplicationContext(), "전 ECC-SPEED 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 8)
                            Toast.makeText(getApplicationContext(), "전 CONSTANT 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 9)
                            Toast.makeText(getApplicationContext(), "전 SET POS 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 10)
                            Toast.makeText(getApplicationContext(), "전 FREQUENCY 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 11)
                            Toast.makeText(getApplicationContext(), "전 VARIATION 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 12)
                            Toast.makeText(getApplicationContext(), "후 WEIGHT 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 13)
                            Toast.makeText(getApplicationContext(), "후 SPEED 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 14)
                            Toast.makeText(getApplicationContext(), "후 UP-WEIGHT 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 15)
                            Toast.makeText(getApplicationContext(), "후 DOWN-WEIGHT 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 16)
                            Toast.makeText(getApplicationContext(), "후 START-POS 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 17)
                            Toast.makeText(getApplicationContext(), "후 END-POS 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 18)
                            Toast.makeText(getApplicationContext(), "후 CON-SPEED 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 19)
                            Toast.makeText(getApplicationContext(), "후 ECC-SPEED 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 20)
                            Toast.makeText(getApplicationContext(), "후 CONSTANT 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 21)
                            Toast.makeText(getApplicationContext(), "후 SET POS 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 22)
                            Toast.makeText(getApplicationContext(), "후 FREQUENCY 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                        else if (i == 23)
                            Toast.makeText(getApplicationContext(), "후 VARIATION 데이터가 정확하지 않습니다.", Toast.LENGTH_LONG).show();
                    }
                }, 0);
            }
        }
    }

    private void getFlag(String str) {
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
                    Log.e(TAG, "서버접속!!!!" + sb.toString().trim());
                    return sb.toString().trim();

                } catch (Exception e) {
                    return "Exception: " + e.getMessage();
                }
            }

            protected void onPostExecute(String result) {
                try {
                    if(new JSONObject(result).getString("result").toString().equals("1")) {
                        txt_Worktype.setVisibility(View.VISIBLE);
                        spWorkSelector.setVisibility(View.VISIBLE);
                        flag = true;
                    }
                    //Log.e("되지????", new JSONObject(result).getString("result"));
                } catch (JSONException e) {
                    Log.e("설마 에러?", e.getMessage());
                    //e.printStackTrace();
                } catch (IndexOutOfBoundsException iobe) {
                    //Toast.makeText(getApplicationContext(), "ㅋㅋㄹㅃㅃ", Toast.LENGTH_LONG).show();
                }

            }
        }
        GetDataJSON json = new GetDataJSON();
        json.execute(str);
    }

    public class InsertWorkoutRepData extends AsyncTask<Void, Integer, Void> {

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... unused) {


            String id = "";
            String data = "";



            Log.e("InsertWorkoutRepData", "" + i_selMode);



            try{ id = new JSONObject(mSendSetResultJson).getString("result"); }
            catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "Json Error!!!");
                id = "0";
            }

            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            String timeNow = now_time.format(date);



            for(int i = 0; i < mSendRepsData; i++){

                String textPosValues = "";
                String textForceValues = "";
                String textSpeedValues = "";
                String textPowerValues = "";
                String befSetting = "";
                String aftSetting = "";

                if(i_selMode == 0) {
                    befSetting = "WEIGHT,";
                    befSetting += listWeightDataValues_bef.get(i);
                    befSetting += "kg,,SPEED,";
                    befSetting += listSpeedDataValues_bef.get(i);
                    befSetting += "cm/s";

                    aftSetting = "WEIGHT,";
                    aftSetting += listWeightDataValues_aft.get(i);
                    aftSetting += "kg,,SPEED,";
                    aftSetting += listSpeedDataValues_aft.get(i);
                    aftSetting += "cm/s";
                } else if(i_selMode == 1) {
                    befSetting = "WEIGHT,";
                    befSetting += listWeightDataValues_bef.get(i);
                    befSetting += "kg,,SPEED,";
                    befSetting += listSpeedDataValues_bef.get(i);
                    befSetting += "cm/s";

                    aftSetting = "WEIGHT,";
                    aftSetting += listWeightDataValues_aft.get(i);
                    aftSetting += "kg,,SPEED,";
                    aftSetting += listSpeedDataValues_aft.get(i);   //여기서 튕김(이전 운동인데) 인덱스 넘어감 sizt7 머 7이래서 넘어갔음
                    aftSetting += "cm/s";
                } else if(i_selMode == 2) {
                    befSetting = "WEIGHT,";
                    befSetting += listWeightDataValues_bef.get(i);
                    befSetting += "kg,,START POS,";
                    befSetting += listStartDataValues_bef.get(i);
                    befSetting += "cm,,END POS,";
                    befSetting += listEndDataValues_bef.get(i);
                    befSetting += "cm,,CON SPEED,";
                    befSetting += listConSpeedDataValues_bef.get(i);
                    befSetting += "cm/s,,ECC SPEED,";
                    befSetting += listEccSpeedDataValues_bef.get(i);
                    befSetting += "cm/s";

                    aftSetting = "WEIGHT,";
                    aftSetting += listWeightDataValues_aft.get(i);
                    aftSetting += "kg,,START POS,";
                    aftSetting += listStartDataValues_aft.get(i);
                    aftSetting += "cm,,END POS,";
                    aftSetting += listEndDataValues_aft.get(i);
                    aftSetting += "cm,,CON SPEED,";
                    aftSetting += listConSpeedDataValues_aft.get(i);
                    aftSetting += "cm/s,,ECC SPEED,";
                    aftSetting += listEccSpeedDataValues_aft.get(i);
                    aftSetting += "cm/s";
                } else if(i_selMode == 3) {
                    befSetting = "UP WEIGHT,";
                    befSetting += listUpWeightDataValues_bef.get(i);
                    befSetting += "kg,,DOWN WEIGHT,";
                    befSetting += listDnWeightDataValues_bef.get(i);
                    befSetting += "kg";

                    aftSetting = "UP WEIGHT,";
                    aftSetting += listUpWeightDataValues_aft.get(i);
                    aftSetting += "kg,,DOWN WEIGHT,";
                    aftSetting += listDnWeightDataValues_aft.get(i);
                    aftSetting += "kg";
                } else if(i_selMode == 4) {
                    befSetting = "UP WEIGHT,";
                    befSetting += listUpWeightDataValues_bef.get(i);
                    befSetting += "kg,,DOWN WEIGHT,";
                    befSetting += listDnWeightDataValues_bef.get(i);
                    befSetting += "kg";

                    aftSetting = "UP WEIGHT,";
                    aftSetting += listUpWeightDataValues_aft.get(i);
                    aftSetting += "kg,,DOWN WEIGHT,";
                    aftSetting += listDnWeightDataValues_aft.get(i);
                    aftSetting += "kg";

                    for(int gg = 0; gg < listUpWeightDataValues_bef.size(); gg++){
                        Log.e("tt TEST listUpWeightDataValues_bef = ", "" + listUpWeightDataValues_bef.get(gg));
                    }
                    for(int gg = 0; gg < listUpWeightDataValues_aft.size(); gg++){
                        Log.e("tt TEST listUpWeightDataValues_aft = ", "" + listUpWeightDataValues_aft.get(gg));
                    }


                } else if(i_selMode == 5) {
                    befSetting = "UP WEIGHT,";
                    befSetting += listUpWeightDataValues_bef.get(i);
                    befSetting += "kg,,DOWN WEIGHT,";
                    befSetting += listDnWeightDataValues_bef.get(i);
                    befSetting += "kg";

                    aftSetting = "UP WEIGHT,";
                    aftSetting += listUpWeightDataValues_aft.get(i);
                    aftSetting += "kg,,DOWN WEIGHT,";
                    aftSetting += listDnWeightDataValues_aft.get(i);
                    aftSetting += "kg";
                } else if(i_selMode == 6) {
                    befSetting = "WEIGHT,";
                    befSetting += listWeightDataValues_bef.get(i);
                    befSetting += "kg,,CONSTANT,";
                    befSetting += listConstantDataValues_bef.get(i);
                    befSetting += ",,SET POS,";
                    befSetting += listSetposDataValues_bef.get(i);
                    befSetting += "cm";

                    aftSetting = "WEIGHT,";
                    aftSetting += listWeightDataValues_aft.get(i);
                    aftSetting += "kg,,CONSTANT,";
                    aftSetting += listConstantDataValues_aft.get(i);
                    aftSetting += ",,SET POS,";
                    aftSetting += listSetposDataValues_aft.get(i);
                    aftSetting += "cm";
                } else if(i_selMode == 7) {
                    befSetting = "WEIGHT,";
                    befSetting += listWeightDataValues_bef.get(i);
                    befSetting += "kg,,SPEED,";
                    befSetting += listSpeedDataValues_bef.get(i);
                    befSetting += "cm/s,,CONSTANT,";
                    befSetting += listConstantDataValues_bef.get(i);

                    aftSetting = "WEIGHT,";
                    aftSetting += listWeightDataValues_aft.get(i);
                    aftSetting += "kg,,SPEED,";
                    aftSetting += listSpeedDataValues_aft.get(i);
                    aftSetting += "cm/s,,CONSTANT,";
                    aftSetting += listConstantDataValues_aft.get(i);
                } else {
                    befSetting = "WEIGHT,";
                    befSetting += listWeightDataValues_bef.get(i);
                    befSetting += "kg,,SET POS,";
                    befSetting += listSetposDataValues_bef.get(i);
                    befSetting += "cm,,FREQUENCY,";
                    befSetting += listFrequencyDataValues_bef.get(i);
                    befSetting += ",,VARIATION,";
                    befSetting += listVariationDataValues_bef.get(i);

                    aftSetting = "WEIGHT,";
                    aftSetting += listWeightDataValues_aft.get(i);
                    aftSetting += "kg,,SET POS,";
                    aftSetting += listSetposDataValues_aft.get(i);
                    aftSetting += "cm,,FREQUENCY,";
                    aftSetting += listFrequencyDataValues_aft.get(i);
                    aftSetting += ",,VARIATION,";
                    aftSetting += listVariationDataValues_aft.get(i);
                }


                for(int j = 0; j < listForceDataValues.get(i).size(); j++){
                    textPosValues   += listPosDataValues.get(i).get(j);
                    textForceValues += listForceDataValues.get(i).get(j);
                    textSpeedValues += listSpeedDataValues.get(i).get(j);
                    textPowerValues += listPowerDataValues.get(i).get(j);

                    if(j+1 != listForceDataValues.get(i).size()){
                        textPosValues   += ",";
                        textForceValues += ",";
                        textSpeedValues += ",";
                        textPowerValues   += ",";

                    }
                }

                String avgForce = String.format("%.2f", publicFunctions.calculateAverage(listForceDataValues.get(i)));
                String avgSpeed = String.format("%.2f", publicFunctions.calculateAverage(listSpeedDataValues.get(i)));
                String avgPower = String.format("%.2f", publicFunctions.calculateAverage(listPowerDataValues.get(i)));
                String maxForce = String.format("%.2f", publicFunctions.calculateMax(listForceDataValues.get(i)));
                String maxSpeed = String.format("%.2f", publicFunctions.calculateMax(listSpeedDataValues.get(i)));
                String maxPower = String.format("%.2f", publicFunctions.calculateMax(listPowerDataValues.get(i)));

                String s_url = "http://211.253.30.245/equipment/kor/miniplus/Miniplus_addRepsData.php";
                String param = "&users_id=" + PublicValues.userId +
                        "&miniplus_id=" + id +
                        "&workout_datetime=" + timeNow +
                        "&sets=" + txt_set.getText().toString() +
                        "&reps=" + (i+1) +
                        "&pos_data=" + textPosValues +
                        "&force_data=" + textForceValues +
                        "&speed_data=" + textSpeedValues +
                        "&power_data=" + textPowerValues +
                        "&avg_force=" + avgForce +
                        "&avg_speed=" + avgSpeed +
                        "&avg_power=" + avgPower +
                        "&max_force=" + maxForce +
                        "&max_speed=" + maxSpeed +
                        "&max_power=" + maxPower +
                        "&bef_setting=" + befSetting +
                        "&aft_setting=" + aftSetting +
                        "&clubs_id=" + PublicValues.userClub;    //이부분에서 사람껄로 넣어라

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
                    Log.e("RECV DATA2", data);

                    if (data.contains("true")) {
                        Log.e("RESULT2", "성공적으로 처리되었습니다!");
                    } else {
                        Log.e("RESULT2", "에러 발생! ERRCODE = " + data);
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.e("Error1", e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Error2", e.getMessage());
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }





    public void writeExcel() {
        try {
            File file = new File("sdcard/Download/ExFile.xlsx");

            InputStream inputStream = new FileInputStream("sdcard/Download/frame_loading_01.png");
            FileOutputStream fileout = new FileOutputStream(file);
            byte[] bytes = IOUtils.toByteArray(inputStream);


            XSSFWorkbook xworkbook = new XSSFWorkbook();

            XSSFSheet xsheet = xworkbook.createSheet("ㅋ");
            XSSFRow curRow;

            CellStyle cellStyle1 = xworkbook.createCellStyle();
            cellStyle1.setAlignment(HorizontalAlignment.CENTER);
            cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle1.setBorderBottom(BorderStyle.MEDIUM_DASH_DOT_DOT);
            cellStyle1.setBorderRight(BorderStyle.THICK);
            cellStyle1.setRightBorderColor(IndexedColors.AQUA.getIndex());
            cellStyle1.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
            cellStyle1.setFillForegroundColor(IndexedColors.BROWN.getIndex());
            cellStyle1.setFillPattern(FillPatternType.DIAMONDS);


            int pictureIdx = xworkbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            inputStream.close();

            CreationHelper helper = xworkbook.getCreationHelper();
            //Creates the top-level drawing patriarch.
            Drawing drawing = xsheet.createDrawingPatriarch();

            //Create an anchor that is attached to the worksheet
            ClientAnchor anchor = helper.createClientAnchor();

            //create an anchor with upper left cell _and_ bottom right cell
            anchor.setCol1(1); //Column B
            anchor.setRow1(2); //Row 3
            anchor.setCol2(2); //Column C
            anchor.setRow2(3); //Row 4

            Picture pict = drawing.createPicture(anchor,pictureIdx);

            Cell cell = null;

            curRow = xsheet.createRow(0);
            cell = curRow.createCell(0);
            cell.setCellValue("ㅋㅋㄹㅃㅃ");
            cell.setCellStyle(cellStyle1);


            curRow = xsheet.createRow(2);
            curRow.setHeight((short)1000);
            cell = curRow.createCell(1);

            //xsheet.autoSizeColumn(1);

            xworkbook.write(fileout);
            fileout.close();
        } catch(FileNotFoundException ex) {
            ;
        } catch(IOException ex) {
            ;
        }
    }

    public void CreateCSV() {
        int resultCount = 0;
        String mode, type;
        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter("sdcard/Download/kkfQQ" + ".csv"));
            for(int i = 0; i < Data_sets.size(); i++) {
                JSONObject jsonObject_SET = (JSONObject) miniList.get(i);
                mode = searchModeType(jsonObject_SET.getString("workoutmode_code"));
                type = searchModeType(jsonObject_SET.getString("workouttype_code"));
                for(int j = 0; j < Data_sets.get(i).size(); j++) {
                    JSONObject jsonObject_REP = (JSONObject) Data_sets.get(i).get(j);
                    if(j == 0) {
                        if(i != 0) {
                            fw.newLine();
                            fw.newLine();
                        }
                        fw.write("SET," + jsonObject_REP.getString("sets") + ",,WORKOUT DATE," + jsonObject_SET.getString("workout_date"));
                        fw.newLine();
                        fw.write("WORKOUT MODE," + mode + ",,WORKOUT TYPE," + type);
                        fw.newLine();
                        fw.write("SET AVERAGE FROCE," + jsonObject_SET.getString("avg_force") + ",,SET AVERAGE SPEED," + jsonObject_SET.getString("avg_speed") + ",,SET AVERAGE POWER," + jsonObject_SET.getString("avg_power"));
                        fw.newLine();
                        fw.write("SET MAXIMUM FROCE," + jsonObject_SET.getString("max_force") + ",,SET MAXIMUM SPEED," + jsonObject_SET.getString("max_speed") + ",,SET MAXIMUM POWER," + jsonObject_SET.getString("max_power"));
                        fw.newLine();
                        fw.write("WORKOUT TIME," + jsonObject_SET.getString("workouttime") + ",,BREAK TIME," + jsonObject_SET.getString("breaktime"));
                        fw.newLine();
                        fw.newLine();
                    }
                    fw.write("REP," + jsonObject_REP.getString("reps"));
                    fw.newLine();
                    /*fw.write("BEFORE SETTING," + jsonObject_REP.getString("bef_setting"));
                    fw.newLine();
                    fw.write("AFTER SETTING," + jsonObject_REP.getString("aft_setting"));
                    fw.newLine();
                    */
                    fw.write("SETTING," + jsonObject_REP.getString("bef_setting"));
                    fw.newLine();
                    //fw.write("AFTER SETTING," + jsonObject_REP.getString("aft_setting"));
                    //fw.newLine();

                    fw.write("POSITION DATA");
                    fw.newLine();
                    fw.write(jsonObject_REP.getString("pos_data"));
                    fw.newLine();
                    fw.write("FORCE DATA");
                    fw.newLine();
                    fw.write(jsonObject_REP.getString("force_data"));
                    fw.newLine();
                    fw.write("SPEED DATA");
                    fw.newLine();
                    fw.write(jsonObject_REP.getString("speed_data"));
                    fw.newLine();
                    fw.write("POWER DATA");
                    fw.newLine();
                    fw.write(jsonObject_REP.getString("power_data"));
                    fw.newLine();
                    fw.write("AVERAGE FORCE," + jsonObject_REP.getString("avg_force") + ",,AVERAGE SPEED," + jsonObject_REP.getString("avg_speed") + ",,AVERAGE POWER," + jsonObject_REP.getString("avg_power"));
                    fw.newLine();
                    fw.write("MAXIMUM FORCE," + jsonObject_REP.getString("max_force") + ",,MAXIMUM SPEED," + jsonObject_REP.getString("max_speed") + ",,MAXIMUM POWER," + jsonObject_REP.getString("max_power"));
                    fw.newLine();
                    fw.newLine();
                }
            }
            fw.flush();
            fw.close();

            Data_sets.removeAll(Data_sets);
            miniList.removeAll(miniList);
        } catch (Exception e) {
            ;
        }
    }

    String myJSON_sets;
    ArrayList<JSONObject> tmpData_sets = new ArrayList<>();
    ArrayList<ArrayList> Data_sets = new ArrayList<>();
    ArrayList<JSONObject> miniList = new ArrayList<>();
    String ds_moduleID, ds_userID, ds_workoutDate, ds_avgF, ds_avgS, ds_avgP, ds_maxF, ds_maxS, ds_maxP, ds_workoutTime, ds_breakTime, ds_set, ds_rep, ds_mode, ds_type;
    private void GetData_sets(String str) {
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
                myJSON_sets = result;
                inputIntent_sets();
            }
        }
        GetDataJSON json = new GetDataJSON();
        json.execute(str);
    }

    protected void inputIntent_sets() {
        ds_moduleID = ds_userID = ds_workoutDate = ds_avgF = ds_avgS = ds_avgP = ds_maxF = ds_maxS = ds_maxP = ds_workoutTime = ds_breakTime = ds_set = ds_rep = ds_mode = ds_type = "";
        try {
            JSONArray jsonArray = new JSONArray(new JSONObject(myJSON_sets).getString("result"));
            for(int i = 0; i < jsonArray.length(); i++) {
                miniList.add(jsonArray.getJSONObject(i));
            }

            Log.e("튕긴 이유4", jsonArray.length() + "  ");
            //이거 분명 쓸곳 있다.
            /*
            ds_moduleID = jsonObj.getString("id");
            ds_userID = jsonObj.getString("users_id");
            ds_workoutDate = jsonObj.getString("workout_date");
            ds_avgF = jsonObj.getString("avg_force");
            ds_avgS = jsonObj.getString("avg_speed");
            ds_avgP = jsonObj.getString("avg_power");
            ds_maxF = jsonObj.getString("max_force");
            ds_maxS = jsonObj.getString("max_speed");
            ds_maxP = jsonObj.getString("max_power");
            ds_workoutTime = jsonObj.getString("workouttime");
            ds_breakTime = jsonObj.getString("breaktime");
            ds_set = jsonObj.getString("set_seq");
            ds_rep = jsonObj.getString("reps");
            ds_mode = jsonObj.getString("workoutmode_code");
            ds_type = jsonObj.getString("workouttype_code");
            */
            //

            long now = System.currentTimeMillis();

            Date date = new Date(now);
            SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd");
            String timeNow = now_time.format(date);
            for(int i = 0; i < miniList.size(); i++) {  //틀린값 들어간다.
                Log.e("여기가 문제", miniList.get(i).getString("id") + "   " + miniList.size());
                GetData_reps("http://211.253.30.245/equipment/kor/miniplus/Miniplus_RepData_SEL.php?miniplus_id=" + miniList.get(i).getString("id"));

            }
            //Log.e("ㅋㅋㄹㅃㅃ2", Data_sets.size() + "   " + Data_sets.get(0).size() + "   "+ Data_sets.get(1).size() + "   "+ Data_sets.get(2).size());
            JSONObject aaaaaa = (JSONObject)Data_sets.get(0).get(4);
            Log.e("ㅋㅋㄹㅃㅃ2", aaaaaa.getString("avg_force"));
            Log.e("완료", Data_sets.get(0).get(4).toString() + "   " + Data_sets.get(1).get(4).toString() + "   " + Data_sets.get(2).get(4).toString());
            CreateCSV();
            //writeExcel();
        } catch (JSONException e) {
            Log.e("튕긴 이유1", e.toString());
            e.printStackTrace();
        }
    }

    String myJSON_reps;
    ArrayList<JSONObject> Data_reps = new ArrayList<>();
    String dr_set, dr_rep, dr_positionData, dr_forceData, dr_speedData, dr_powerData, dr_avgF, dr_avgS, dr_avgP, dr_maxF, dr_maxS, dr_maxP, dr_befS, dr_aftS;
    private void GetData_reps(String str) {
        String uri = str;
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
            myJSON_reps = sb.toString().trim();
            inputIntent_reps();

        } catch (Exception e) {

        }

    }

    protected void inputIntent_reps() {
        dr_set = dr_rep = dr_positionData = dr_forceData = dr_speedData = dr_powerData = dr_avgF = dr_avgS = dr_avgP = dr_maxF = dr_maxS = dr_maxP = dr_befS = dr_aftS = "";
        try {
            JSONArray jsonArray = new JSONArray(new JSONObject(myJSON_reps).getString("result"));
            Log.e("ㅋㅋㄹㅃㅃ", jsonArray.length()+ "");
            for(int i = 0; i < jsonArray.length(); i++) {
                Data_reps.add(jsonArray.getJSONObject(i));
            }
            Data_sets.add((ArrayList) Data_reps.clone());
            Data_reps.removeAll(Data_reps);

            //이거 분명 쓸곳 있다.
            /*
            dr_set = jsonObj.getString("sets");
            dr_rep = jsonObj.getString("reps");
            dr_positionData = jsonObj.getString("pos_data");
            dr_forceData = jsonObj.getString("force_data");
            dr_speedData = jsonObj.getString("speed_data");
            dr_powerData = jsonObj.getString("power_data");
            dr_avgF = jsonObj.getString("avg_force");
            dr_avgS = jsonObj.getString("avg_speed");
            dr_avgP = jsonObj.getString("avg_power");
            dr_maxF = jsonObj.getString("max_force");
            dr_maxS = jsonObj.getString("max_speed");
            dr_maxP = jsonObj.getString("max_power");
            dr_befS = jsonObj.getString("bef_setting");
            dr_aftS = jsonObj.getString("aft_setting");
            */
            //
            Log.e("튕긴 이유3", dr_set + "ㅁㅁㅁ");

            long now = System.currentTimeMillis();

            Date date = new Date(now);
            SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd");
            String timeNow = now_time.format(date);



        } catch (JSONException e) {
            Log.e("튕긴 이유2", e.toString());
            e.printStackTrace();
        }
    }

    String myJSON_name;
    private void GetName(String str) {
        String uri = str;
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
            myJSON_name = sb.toString().trim();
            inputIntent_name();

        } catch (Exception e) {

        }

    }

    protected void inputIntent_name() {
        try {
            JSONArray jsonArray = new JSONArray(new JSONObject(myJSON_name).getString("result"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public class SendMail_Async extends AsyncTask {
        private String mailhost = "smtp.gmail.com";
        private Session session;

        String user = "ronfic.co@gmail.com";
        String password = "rf-00245";
        String sender = "론픽";

        private Context context;
        private String recipients;
        private String subject;
        private String body;
        private String[] filePath;

        public SendMail_Async(Context context, String recipients, String subject, String body, String[] filePath){
            this.context = context;
            this.recipients = recipients;
            this.subject = subject;
            this.body = body;
            this.filePath = filePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!ExerciseActivity.this.isFinishing()) {
                progressDialog.DialogStart();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", mailhost);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.quitwait", "false");

            session = Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            });

            MailcapCommandMap MailcapCmdMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            MailcapCmdMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            MailcapCmdMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            MailcapCmdMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            MailcapCmdMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            MailcapCmdMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(MailcapCmdMap);

            try {
                MimeMessage message = new MimeMessage(session);
                //DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));

                message.setFrom(new InternetAddress(user));
                message.setSubject(subject);
                message.setSentDate(new Date());

                MimeBodyPart bodyPart = new MimeBodyPart();
                Multipart multipart = new MimeMultipart();
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(body, "UTF-8", "html");
                multipart.addBodyPart(textPart);
                //message.setContent(body, "text/html;charset=EUC-KR");

                //message.setDataHandler(handler);

                for(int i = 0; i < filePath.length; i++) {
                    FileDataSource fileDataSource = new FileDataSource(filePath[i]);
                    bodyPart = new MimeBodyPart();
                    bodyPart.setDataHandler(new DataHandler(fileDataSource));
                    bodyPart.setFileName(fileDataSource.getName());
                    Log.e("파일이름", filePath[i]);
                    multipart.addBodyPart(bodyPart);
                }

                message.setContent(multipart);

                //recipients = "11111@gmail.com,
                if (recipients.indexOf(',') > 0) //여러개 일때 이렇게 한다는데 그냥 for문 돌리는게 좋을듯
                    message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(recipients));
                else
                    message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipients));
                Log.e("파일이름", "ㅋㅋㅋ");

                Transport.send(message);
            }catch (SendFailedException e) {
                Toast.makeText(context, "이메일을 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            }catch (MessagingException e) {
                Toast.makeText(context, "인터넷 연결을 확인해주십시오.", Toast.LENGTH_SHORT).show();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            setDirEmpty();
            mailFlag = false;
            if(!ExerciseActivity.this.isFinishing()) {
                progressDialog.DialogStop();
            }
            Log.e("비동기 인터넷", "ㅇㅋ");
        }

        public void setDirEmpty() {
            File dir = new File("sdcard/Download/screen");
            File[] childFileList = dir.listFiles();

            if (dir.exists()) {
                for (File childFile : childFileList) {
                    childFile.delete();
                }
            }
        }
    }

    public void settinValue_bef(String Weight, String Speed, String UpWeight, String DnWeight, String Start, String End, String ConSpeed, String EccSpeed, String Constant, String Setpos, String Frequency, String Variation) {
        listWeight_bef.add(Weight);
        listSpeed_bef.add(Speed);
        listUpWeight_bef.add(UpWeight);
        listDnWeight_bef.add(DnWeight);
        listStart_bef.add(Start);
        listEnd_bef.add(End);
        listConSpeed_bef.add(ConSpeed);
        listEccSpeed_bef.add(EccSpeed);
        listConstant_bef.add(Constant);
        listSetpos_bef.add(Setpos);
        listFrequency_bef.add(Frequency);
        listVariation_bef.add(Variation);
        befWorkCnt++;
    }

    public void settinValue_aft(String Weight, String Speed, String UpWeight, String DnWeight, String Start, String End, String ConSpeed, String EccSpeed, String Constant, String Setpos, String Frequency, String Variation) {
        listWeight_aft.add(Weight);
        listSpeed_aft.add(Speed);
        listUpWeight_aft.add(UpWeight);
        listDnWeight_aft.add(DnWeight);
        listStart_aft.add(Start);
        listEnd_aft.add(End);
        listConSpeed_aft.add(ConSpeed);
        listEccSpeed_aft.add(EccSpeed);
        listConstant_aft.add(Constant);
        listSetpos_aft.add(Setpos);
        listFrequency_aft.add(Frequency);
        listVariation_aft.add(Variation);
        aftWorkCnt++;
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



    private void controlBoard(String hexString) {
        byte[] datapacket = PublicFunctions.hexStrToByteArr(hexString);
        datapacket[18] = PublicFunctions.makeChkSum(datapacket);
        if (usbService != null) {
            usbService.write(datapacket);
        }
    }



    private void sendChangeValues(){
        switch (i_selMode){
            case 0:
                isokineticFrag.setFragValues(i_type);   //이건 무게 같은거 올릴때나 내릴때 버튼 텍스트 바꾸는 용도 인듯
                isokineticFrag.changeValues(false, 0);  //false는 값 내리고 ,0은 얼마나 내일 지 숫자크기
                break;
            case 1:
                isokineticFrag.setFragValues(i_type);
                isokineticFrag.changeValues(false, 0);
                break;
            case 2:
                isokineticBiFrag.setFragValues(i_type);
                isokineticBiFrag.changeValues(false, 0);
                break;
            case 3:
                eccentricFrag1.setFragValues(i_type);
                eccentricFrag1.changeValues(false, 0);
                break;
            case 4:
                eccentricFrag2.setFragValues(i_type);
                eccentricFrag2.changeValues(false, 0);
                break;
            case 5:
                eccentricFrag3.setFragValues(i_type);
                eccentricFrag3.changeValues(false, 0);
                break;
            case 6:
                springFrag.setFragValues(i_type);
                springFrag.changeValues(false, 0);
                break;
            case 7:
                dampingFrag.setFragValues(i_type);
                dampingFrag.changeValues(false, 0);
                break;
            case 8:
                vibrationFrag.setFragValues(i_type);
                vibrationFrag.changeValues(false, 0);
                break;
        }
    }

    private String searchModeType(String a) {
        String name = "";
        switch (a){
            case "MPMODE001":
                name = "ISOKINETIC";
                break;
            case "MPMODE002":
                name = "ISOKINETIC-BI";
                break;
            case "MPMODE003":
                name = "ECCENTRIC";
                break;
            case "MPMODE004":
                name = "ECCENTRIC";
                break;
            case "MPMODE005":
                name = "ECCENTRIC";
                break;
            case "MPMODE006":
                name = "SPRING";
                break;
            case "MPMODE007":
                name = "DAMPING";
                break;
            case "MPMODE008":
                name = "VIBRATION";
                break;
            case "MPMODE009":
                name = "ISOKINETIC";
                break;
            case "MPTYPE000":
                name = "BASE";
                break;
            case "MPTYPE001":
                name = "ONE HAND";
                break;
            case "MPTYPE002":
                name = "TWO HAND";
                break;
        }
        return name;
    }

    private void fragChange(){
        Log.i(TAG, "fragChange");
        fragmentManager = getFragmentManager();
        FragmentTransaction tran = fragmentManager.beginTransaction();

        switch (i_selMode) {
            case 0:
                tran.replace(R.id.modeFrag, isokineticFrag);    //화면전환 작은 걸로 봐서 버튼 누르는 부분만 바꾸는듯
                isokineticFrag.setFragValues(i_type);
                tran.commitAllowingStateLoss();
                break;
            case 1:
                tran.replace(R.id.modeFrag, isokineticFrag);
                isokineticFrag.setFragValues(i_type);
                tran.commitAllowingStateLoss();
                break;
            case 2:
                tran.replace(R.id.modeFrag, isokineticBiFrag);
                isokineticBiFrag.setFragValues(i_type);
                tran.commitAllowingStateLoss();
                break;
            case 3:
                tran.replace(R.id.modeFrag, eccentricFrag1);
                tran.commitAllowingStateLoss();
                break;
            case 4:
                tran.replace(R.id.modeFrag, eccentricFrag2);
                eccentricFrag2.setFragValues(i_type);
                tran.commitAllowingStateLoss();
                break;
            case 5:
                tran.replace(R.id.modeFrag, eccentricFrag3);
                tran.commitAllowingStateLoss();
                break;
            case 6:
                tran.replace(R.id.modeFrag, springFrag);
                springFrag.setFragValues(i_type);
                tran.commitAllowingStateLoss();
                break;
            case 7:
                tran.replace(R.id.modeFrag, dampingFrag);
                dampingFrag.setFragValues(i_type);
                tran.commitAllowingStateLoss();
                break;
            case 8:
                tran.replace(R.id.modeFrag, vibrationFrag);
                vibrationFrag.setFragValues(i_type);
                tran.commitAllowingStateLoss();
                break;
        }
        bool_changeFragFlag = false;
    }

    public void receiveFragNum(int modeSel) {
        Log.i(TAG, "mode select : " + modeSel + "  Machine Status : " + b_workoutBit);
        if(!bool_changeModeFlag && !bool_changeFragFlag && modeSel != b_workoutBit){

            progressDialog.DialogStart();
            bool_changeModeFlag = true;
            bool_changeFragFlag = true;
            i_selMode = modeSel;
        }
    }


    public void setFragValue(String serial) {   //질문 어떻게 쓰이는지 모르겠음

        Log.i(TAG, "setFragValue = " + serial);
        str_fragSerial = serial;
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        controlBoard(str_fragSerial);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        controlBoard(MOTER_START_SERIAL);

        progressDialog.DialogStop();


        bool_changeModeFlag = false;
    }


    private int[] getColors() {
        int[] colors;
        int colSize = 0;
        if(bool_valuesFlag[0])  colSize++;
        if(bool_valuesFlag[1])  colSize++;
        if(bool_valuesFlag[2])  colSize++;

        if(colSize > 0){
            colors = new int[colSize];
            colSize = 0;

            if(bool_valuesFlag[0]){
                colors[colSize] = Color.rgb(0, 166, 81);
                colSize++;
            }
            if(bool_valuesFlag[1]){
                colors[colSize] = Color.rgb(0, 174, 239);
                colSize++;
            }
            if(bool_valuesFlag[2]){
                colors[colSize] = Color.rgb(255, 112, 0);
                colSize++;
            }
        }
        else{   //질문 그냥 안나온다는 건지 봐야 알듯
            colors = new int[1];
            colors[0] = Color.rgb(0,0,0);
        }

        return colors;
    }

    private void setSelectModeBtn(){
        if(modeSelectFragment.btn_isokineticMode != null){
            modeSelectFragment.btn_isokineticMode.setBackgroundResource (R.drawable.btn_mode_sel);
            modeSelectFragment.btn_isokineticBiMode.setBackgroundResource (R.drawable.btn_mode_sel);
            modeSelectFragment.btn_eccentricMode.setBackgroundResource  (R.drawable.btn_mode_sel);
            modeSelectFragment.btn_eccentricMode.setBackgroundResource  (R.drawable.btn_mode_sel);
            modeSelectFragment.btn_eccentricMode.setBackgroundResource  (R.drawable.btn_mode_sel);
            modeSelectFragment.btn_springMode.setBackgroundResource     (R.drawable.btn_mode_sel);
            modeSelectFragment.btn_dampingMode.setBackgroundResource    (R.drawable.btn_mode_sel);
            modeSelectFragment.btn_vibrationMode.setBackgroundResource  (R.drawable.btn_mode_sel);
            modeSelectFragment.btn_isokineticMode.setBackgroundResource (R.drawable.btn_mode_sel);

            if(b_workoutBit == 1)       modeSelectFragment.btn_isokineticMode.setBackgroundResource (R.drawable.btn_mode_sel_able);
            else if(b_workoutBit == 2)  modeSelectFragment.btn_isokineticBiMode.setBackgroundResource (R.drawable.btn_mode_sel_able);   //질문 Bi가 뭔지
            else if(b_workoutBit == 3)  modeSelectFragment.btn_eccentricMode.setBackgroundResource  (R.drawable.btn_mode_sel_able);
            else if(b_workoutBit == 4)  modeSelectFragment.btn_eccentricMode.setBackgroundResource  (R.drawable.btn_mode_sel_able);
            else if(b_workoutBit == 5)  modeSelectFragment.btn_eccentricMode.setBackgroundResource  (R.drawable.btn_mode_sel_able);
            else if(b_workoutBit == 6)  modeSelectFragment.btn_springMode.setBackgroundResource     (R.drawable.btn_mode_sel_able);
            else if(b_workoutBit == 7)  modeSelectFragment.btn_dampingMode.setBackgroundResource    (R.drawable.btn_mode_sel_able);
            else if(b_workoutBit == 8)  modeSelectFragment.btn_vibrationMode.setBackgroundResource  (R.drawable.btn_mode_sel_able);
            else if(b_workoutBit == 9)  modeSelectFragment.btn_isokineticMode.setBackgroundResource (R.drawable.btn_mode_sel_able);
        }
    }

    private void initView() {

        chart_lineChart = (LineChart) findViewById(R.id.chart_lineChart);
        chart_barChart  = (BarChart) findViewById(R.id.chart_barChart);

        layout_values[0] = (LinearLayout) findViewById(R.id.layout_values1);
        layout_values[1] = (LinearLayout) findViewById(R.id.layout_values2);
        layout_values[2] = (LinearLayout) findViewById(R.id.layout_values3);
        ModeSelectFragment.layout_exercise = (FrameLayout) findViewById(R.id.layout_exercise);

        btn_selectActibity  = (Button) findViewById(R.id.btn_selectMode);

        btn_mode    = (Button) findViewById(R.id.btn_mode);
        btn_login   = (Button) findViewById(R.id.btn_login);
        btn_logout  = (Button) findViewById(R.id.btn_logout);
        btn_type    = (Button) findViewById(R.id.btn_type);
        btn_guideVideo  = (Button) findViewById(R.id.btn_guideVideo);

        btn_reset   = (Button) findViewById(R.id.btn_reset);

        btn_wifi    = (Button) findViewById(R.id.btn_wifi);

        btn_force   = (Button) findViewById(R.id.btn_force);
        btn_speed   = (Button) findViewById(R.id.btn_speed);
        btn_power   = (Button) findViewById(R.id.btn_power);

        btn_screenShot = (Button) findViewById(R.id.btn_screenShot);

        txt_userName    = (TextView) findViewById(R.id.txt_userName);
        txt_mode        = (TextView) findViewById(R.id.txt_mode);
        txt_avgForce    = (TextView) findViewById(R.id.txt_avgForce);
        txt_avgSpeed    = (TextView) findViewById(R.id.txt_avgSpeed);
        txt_avgPower    = (TextView) findViewById(R.id.txt_avgPower);
        txt_maxForce    = (TextView) findViewById(R.id.txt_maxForce);
        txt_maxSpeed    = (TextView) findViewById(R.id.txt_maxSpeed);
        txt_maxPower    = (TextView) findViewById(R.id.txt_maxPower);

        txt_wifiName    = (TextView) findViewById(R.id.txt_wifiName);
        txt_errorCode   = (TextView) findViewById(R.id.txt_errorCode);

        txt_set_avgforce = (TextView) findViewById(R.id.txt_set_avgforce);
        txt_set_avgspeed = (TextView) findViewById(R.id.txt_set_avgspeed);
        txt_set_avgpower = (TextView) findViewById(R.id.txt_set_avgpower);
        txt_set_maxforce = (TextView) findViewById(R.id.txt_set_maxforce);
        txt_set_maxspeed = (TextView) findViewById(R.id.txt_set_maxspeed);
        txt_set_maxpower = (TextView) findViewById(R.id.txt_set_maxpower);

        txt_rep = (TextView) findViewById(R.id.txt_rep);
        txt_set = (TextView) findViewById(R.id.txt_set);

        txt_workTime    = (TextView) findViewById(R.id.txt_workTime);
        txt_breakTime   = (TextView) findViewById(R.id.txt_breakTime);

        workSelector = (Spinner) findViewById(R.id.workSelector);
        txt_Worktype = (TextView) findViewById(R.id.txt_Worktype);
        spWorkSelector = (Spinner) findViewById(R.id.sp_worktype); //glenn 추가
        ArrayAdapter mArrayAdapter_work = ArrayAdapter.createFromResource(this, R.array.WorkArray, R.layout.spinner_item);
        workSelector.setAdapter(mArrayAdapter_work);

        if(PublicValues.club_id.equals("1144")) {
            //workSelector.setVisibility(View.VISIBLE);
        }
        txt_errorCode.setVisibility(View.GONE);
        btn_reset.setVisibility(View.GONE);



    }

    private void initSet(){

        bool_valuesFlag[0] = true;
        bool_valuesFlag[1] = true;
        bool_valuesFlag[2] = true;

        bool_valuesOldFlag[0] = true;
        bool_valuesOldFlag[1] = true;
        bool_valuesOldFlag[2] = true;

        modeSelectFragment = new ModeSelectFragment();

        isokineticFrag      = new IsokineticFrag();
        isokineticBiFrag    = new IsokineticBiFrag();
        eccentricFrag1      = new EccentricFrag1();
        eccentricFrag2      = new EccentricFrag2();
        eccentricFrag3      = new EccentricFrag3();
        springFrag          = new SpringFrag();
        dampingFrag         = new DampingFrag();
        vibrationFrag       = new VibrationFrag();
    }

    private void initValues(){
        d_avgForce = 0;
        d_avgSpeed = 0;
        d_avgPower = 0;

        d_maxForce = 0;
        d_maxSpeed = 0;
        d_maxPower = 0;

        chart_barChart.clear();
        chart_lineChart.clear();

        arrE_barEntry.clear();

        listD_avgForce.clear();
        listD_avgSpeed.clear();
        listD_avgPower.clear();

        listF_avrForce.clear();
        listF_avrSpeed.clear();
        listF_avrPower.clear();

        listArr_forceEntry.clear();
        listArr_speedEntry.clear();
        listArr_powerEntry.clear();

        listArr_avgForceData.clear();
        d_listArr_avgForceData.clear();
        listArr_avgSpeedData.clear();
        d_listArr_avgSpeedData.clear();
        listArr_avgPowerData.clear();
        d_listArr_avgPowerData.clear();

        listArr_maxForceData.clear();
        d_listArr_maxForceData.clear();
        listArr_maxSpeedData.clear();
        d_listArr_maxSpeedData.clear();
        listArr_maxPowerData.clear();
        d_listArr_maxSpeedData.clear();
    }

    private void lineChartInit(){
        try {
            chart_lineChart.getDescription().setText("");
            chart_lineChart.getLegend().setEnabled(false);
            chart_lineChart.setNoDataText("");

            YAxis leftAxis = chart_lineChart.getAxisLeft();
            YAxis rightAxis = chart_lineChart.getAxisRight();
            XAxis xAxis = chart_lineChart.getXAxis();

            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextSize(10f);
            xAxis.setTextColor(Color.rgb(255, 255, 255));
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(false);

            leftAxis.setTextSize(10f);
            leftAxis.setDrawLabels(false);
            leftAxis.setDrawAxisLine(false);
            leftAxis.setDrawGridLines(false);

            rightAxis.setTextColor(Color.rgb(255, 255, 255));
            rightAxis.setDrawAxisLine(false);
            rightAxis.setDrawGridLines(false);
            rightAxis.setDrawLabels(false);

            chart_lineChart.setScaleEnabled(true);
            chart_lineChart.setDoubleTapToZoomEnabled(false);
            chart_lineChart.setBackgroundColor(Color.argb(1, 0, 0, 0));
            chart_lineChart.setDrawBorders(false);

            chart_lineChart.getXAxis().setSpaceMin(0.2f);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e("Error1", e.getMessage());
        } catch (NegativeArraySizeException e) {
            e.printStackTrace();
            Log.e("Error2", e.getMessage());
        }
    }


    private void barChartInit(){

        chart_barChart.getLegend().setEnabled(false);
        chart_barChart.setNoDataText("");

        YAxis leftAxis = chart_barChart.getAxisLeft();
        YAxis rightAxis = chart_barChart.getAxisRight();
        XAxis xAxis = chart_barChart.getXAxis();

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return Integer.toString((int)value+1);
            }
        });

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.rgb(255, 255, 255));
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);


        leftAxis.setTextSize(10f);
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0);

        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);
    }


    private void selectLineChart(int sel){
        try {
            if (listArr_forceEntry.get(sel).size() > 0) {

                ArrayList<ILineDataSet> Linechart_dataSets = new ArrayList<>();
                LineDataSet data;

                chart_lineChart.clear();
                chart_lineChart.invalidate();

                Collections.sort(listArr_forceEntry.get(sel), new EntryXComparator());
                Collections.sort(listArr_speedEntry.get(sel), new EntryXComparator());
                Collections.sort(listArr_powerEntry.get(sel), new EntryXComparator());

                txt_avgForce.setText(String.valueOf(listArr_avgForceData.get(sel)));
                txt_avgSpeed.setText(String.valueOf(listArr_avgSpeedData.get(sel)));
                txt_avgPower.setText(String.valueOf(listArr_avgPowerData.get(sel)));

                txt_maxForce.setText(String.valueOf(listArr_maxForceData.get(sel)));
                txt_maxSpeed.setText(String.valueOf(listArr_maxSpeedData.get(sel)));
                txt_maxPower.setText(String.valueOf(listArr_maxPowerData.get(sel)));

                if(bool_valuesFlag[0]){
                    data = new LineDataSet(listArr_forceEntry.get(sel), "Force");
                    data.setColor(Color.rgb(0, 166, 81));
                    data.setDrawCircles(false);
                    data.setDrawCircleHole(false);
                    data.setDrawValues(true);
                    Linechart_dataSets.add(data);
                }
                if(bool_valuesFlag[1]){
                    data = new LineDataSet(listArr_speedEntry.get(sel), "Speed");
                    data.setColor(Color.rgb(0, 174, 239));
                    data.setDrawCircles(false);
                    data.setDrawCircleHole(false);
                    data.setDrawValues(true);
                    Linechart_dataSets.add(data);
                }
                if(bool_valuesFlag[2]){
                    data = new LineDataSet(listArr_powerEntry.get(sel), "Power");
                    data.setColor(Color.rgb(255, 112, 0));
                    data.setDrawCircles(false);
                    data.setDrawCircleHole(false);
                    data.setDrawValues(true);
                    Linechart_dataSets.add(data);
                }

                LineData lineData = new LineData(Linechart_dataSets);
                lineData.setValueTextSize(7f);
                lineData.setValueTextColor(Color.rgb(255, 255, 255));

                chart_lineChart.setData(lineData);
                chart_lineChart.animateY(500);
                chart_lineChart.setDescription(chart_lineChart.getDescription());
                chart_lineChart.setAutoScaleMinMaxEnabled(true);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e("Error1", e.getMessage());
        } catch (NegativeArraySizeException e) {
            e.printStackTrace();
            Log.e("Error2", e.getMessage());
        }
    }

    private void drawLineChart(){
        try {
            if (listE_forceDataList.size() > 0) {
                ArrayList<ILineDataSet> Linechart_dataSets = new ArrayList<>();
                LineDataSet data;

                chart_lineChart.clear();
                chart_lineChart.invalidate();

                Collections.sort(listE_forceDataList, new EntryXComparator());
                Collections.sort(listE_speedDataList, new EntryXComparator());
                Collections.sort(listE_powerDataList, new EntryXComparator());

                if(bool_valuesFlag[0]){
                    data = new LineDataSet(listE_forceDataList, "Force");
                    data.setColor(Color.rgb(0, 166, 81));
                    data.setDrawCircles(false);
                    data.setDrawCircleHole(false);
                    data.setDrawValues(true);
                    Linechart_dataSets.add(data);
                }
                if(bool_valuesFlag[1]){
                    data = new LineDataSet(listE_speedDataList, "Speed");
                    data.setColor(Color.rgb(0, 174, 239));
                    data.setDrawCircles(false);
                    data.setDrawCircleHole(false);
                    data.setDrawValues(true);
                    Linechart_dataSets.add(data);
                }
                if(bool_valuesFlag[2]){
                    data = new LineDataSet(listE_powerDataList, "Power");
                    data.setColor(Color.rgb(255, 112, 0));
                    data.setDrawCircles(false);
                    data.setDrawCircleHole(false);
                    data.setDrawValues(true);
                    Linechart_dataSets.add(data);
                }

                LineData lineData = new LineData(Linechart_dataSets);
                lineData.setValueTextSize(7f);
                lineData.setValueTextColor(Color.rgb(255, 255, 255));

                chart_lineChart.setData(lineData);
                chart_lineChart.setDescription(chart_lineChart.getDescription());
                chart_lineChart.setAutoScaleMinMaxEnabled(true);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.e("Error1", e.getMessage());
        } catch (NegativeArraySizeException e) {
            e.printStackTrace();
            Log.e("Error2", e.getMessage());
        }
    }
    int nameCount = 0;
    private void captLineChart() {
        Bitmap b = chart_lineChart.getChartBitmap();

        OutputStream stream = null;
        try {
            stream = new FileOutputStream("sdcard/Download/" + nameCount + ".png");


            /*
             * Write bitmap to file using JPEG or PNG and 40% quality hint for
             * JPEG.
             */
            b.compress(Bitmap.CompressFormat.PNG, 40, stream);

            stream.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        nameCount++;
        Log.e("chartsave", Environment.getExternalStorageDirectory().getPath() + "/Download/0.png");
    }


    private void changeBarChart(){

        float [] entryValue;
        int entrySize;
        arrE_barEntry.clear();

        for(int i = 0; i < listF_avrForce.size(); i++){
            entrySize = 0;
            if(bool_valuesFlag[0])  entrySize++;
            if(bool_valuesFlag[1])  entrySize++;
            if(bool_valuesFlag[2])  entrySize++;

            entryValue = new float[entrySize];
            entrySize = 0;
            if(bool_valuesFlag[0]){
                entryValue[entrySize] = listF_avrForce.get(i);
                entrySize++;
            }
            if(bool_valuesFlag[1]){
                entryValue[entrySize] = listF_avrSpeed.get(i);
                entrySize++;
            }
            if(bool_valuesFlag[2]){
                entryValue[entrySize] = listF_avrPower.get(i);
                entrySize++;
            }
            arrE_barEntry.add(new BarEntry(i, entryValue));
        }

    }


    private void addBarChart(){
        float [] entryValue;
        int entrySize= 0;

        listF_avrForce.add(Float.parseFloat(String.format("%.2f", d_avgForce)));
        listF_avrSpeed.add(Float.parseFloat(String.format("%.2f", d_avgSpeed)));
        listF_avrPower.add(Float.parseFloat(String.format("%.2f", d_avgPower)));

        if(bool_valuesFlag[0])  entrySize++;
        if(bool_valuesFlag[1])  entrySize++;
        if(bool_valuesFlag[2])  entrySize++;

        entryValue = new float[entrySize];
        entrySize = 0;

        if(bool_valuesFlag[0]){
            entryValue[entrySize] = Float.parseFloat(String.format("%.2f", d_avgForce));
            entrySize++;
        }
        if(bool_valuesFlag[1]){
            entryValue[entrySize] = Float.parseFloat(String.format("%.2f", d_avgSpeed));
            entrySize++;
        }
        if(bool_valuesFlag[2]){
            entryValue[entrySize] = Float.parseFloat(String.format("%.2f", d_avgPower));
        }
        arrE_barEntry.add(new BarEntry(i_rep, entryValue));

        if((bool_valuesFlag[0]^bool_valuesOldFlag[0]) ||
                (bool_valuesFlag[1]^bool_valuesOldFlag[1]) ||
                (bool_valuesFlag[2]^bool_valuesOldFlag[2])){
            changeBarChart();
        }

        bool_valuesOldFlag[0] = bool_valuesFlag[0];
        bool_valuesOldFlag[1] = bool_valuesFlag[1];
        bool_valuesOldFlag[2] = bool_valuesFlag[2];
    }

    private void drawBarChart() {
        Log.e(TAG, "TEST");
        chart_barChart.clear();
        chart_barChart.invalidate();
        chart_barChart.getDescription().setText("");

        BarDataSet data;
        data = new BarDataSet(arrE_barEntry, "");
        data.setColors(getColors());


        data.setDrawValues(false);
        ArrayList<IBarDataSet> Barchart_dataSets = new ArrayList<IBarDataSet>();
        Barchart_dataSets.add(data);


        BarData barData = new BarData(Barchart_dataSets);
        barData.setBarWidth(0.9f);
        barData.setValueTextSize(7f);
        barData.setValueTextColor(Color.rgb(255, 255, 255));

        barData.setDrawValues(true);

        chart_barChart.setData(barData);

        chart_barChart.setFitBars(true); // make the x-axis fit exactly all bars
        chart_barChart.animateY(300);
        chart_barChart.setScaleEnabled(false);
        chart_barChart.setDrawBorders(false);
        chart_barChart.setDescription(chart_barChart.getDescription());
        chart_barChart.setDrawValueAboveBar(true);
        chart_barChart.setVisibleXRange(0, 30);
        chart_barChart.moveViewToX(arrE_barEntry.size());
        chart_barChart.getXAxis().setSpaceMin(0.2f);
    }

    private void setValues(int data1, int data2, int data3){
        mSetWeightDn = data1;
        mSetSpeed = data2;
        mSetTention = data3;
    }

    public File ScreenShot(View view, int reps){
        view.setDrawingCacheEnabled(true);  //화면에 뿌릴때 캐시를 사용하게 한다

        Bitmap screenBitmap = view.getDrawingCache();   //캐시를 비트맵으로 변환

        String filename = PublicValues.userId + txt_mode.getText().toString() + txt_set.getText().toString() + "set" + reps + "rep.png";
        File file = new File("/sdcard/Download/screen/", filename);
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);   //비트맵을 PNG파일로 변환
            os.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        view.setDrawingCacheEnabled(false);
        return file;
    }

    public class MainHandler extends Handler {
        private final WeakReference<ExerciseActivity> mainActivity;

        private MainHandler(ExerciseActivity activity) {
            mainActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    break;
                case UsbService.CTS_CHANGE:
                    Toast.makeText(mainActivity.get(), "CTS_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case UsbService.DSR_CHANGE:
                    Toast.makeText(mainActivity.get(), "DSR_CHANGE", Toast.LENGTH_LONG).show();
                    break;
                case UsbService.SYNC_READ:
                    //Log.e("sync", "2");
                    byte[] buffer = (byte[]) msg.obj;
                    sb_packet.append(PublicFunctions.byteArrToHexStr(buffer));

                    if (sb_packet.length() >= 84) {
                        if (sb_packet.indexOf("F10124") >= 0 && sb_packet.lastIndexOf("FE") >= 0 && sb_packet.lastIndexOf("FE") - sb_packet.indexOf("F10124") > 84) {

                            int startIndex = sb_packet.indexOf("F10124");
                            int endIndex = sb_packet.indexOf("FE", startIndex + 78);


                            if (endIndex - startIndex == 82) {

                                //Log.e(TAG, "packet = " + sb_packet);
                                byte[] Bytebuf = PublicFunctions.hexStrToByteArr(sb_packet.substring(startIndex, endIndex + 2));
                                byte[] RecvChkSum = makeRecvChkSum(Bytebuf);

                                if (Bytebuf[39] == RecvChkSum[1] && Bytebuf[40] == RecvChkSum[0]) {

                                    if (Bytebuf[0] == -15 && Bytebuf[1] == 1) {


                                        int positionData = ((((int) Bytebuf[5]) << 8) | ((int) Bytebuf[6] & 0xff));
                                        int velocityData = ((((int) Bytebuf[7]) << 8) | ((int) Bytebuf[8] & 0xff));
                                        int loadcellData = ((((int) Bytebuf[9]) << 8) | ((int) Bytebuf[10] & 0xff));


                                        d_position  = (double) (positionData * -1) / 10.0;    // cm
                                        d_velocity  = (double) (velocityData * -1) / 10.0;    // cm/s
                                        d_loadcell  = (double) loadcellData / 10.0;         // N

                                        if (i_type == 1) {
                                            d_velocity = d_velocity * 4;
                                            d_loadcell = d_loadcell / 4;
                                            d_position = d_position * 4;
                                        } else if (i_type == 2) {
                                            d_velocity = d_velocity * 2;
                                            d_loadcell = d_loadcell / 2;
                                            d_position = d_position * 2;
                                        }


                                        //machine Status Chk
                                        mainActivity.get().b_statusBit = Bytebuf[30];
                                        if ((b_statusBit & 0xEE) == 0xEE) {
                                            mainActivity.get().b_errorBit = Bytebuf[31];
                                            mainActivity.get().b_workoutBit = (byte) 0x00;
                                        } else {
                                            if ((b_statusBit & 0x03) == 0x03) {
                                                PublicValues.initFlag = true;
                                            }
                                            mainActivity.get().b_errorBit = (byte) 0x00;
                                            mainActivity.get().b_workoutBit = Bytebuf[31];
                                        }
                                        mainActivity.get().b_workStatusBit = Bytebuf[32];

                                        if ((Bytebuf[4] & 0x04) == 0x04) {
                                            mainActivity.get().bool_breakStatus = false;
                                        } else if ((Bytebuf[4] & 0x04) == 0x00) {
                                            mainActivity.get().bool_breakStatus = true;
                                        }

                                        if(b_workoutBit > 0 && b_workoutBit < 9){

                                            mSetWeight = ((((int) Bytebuf[14]) << 8) | ((int) Bytebuf[15] & 0xff));
                                            int data1 = ((((int) Bytebuf[16]) << 8) | ((int) Bytebuf[17] & 0xff));
                                            int data2 = ((((int) Bytebuf[18]) << 8) | ((int) Bytebuf[19] & 0xff));
                                            //int data3 = ((((int) Bytebuf[21]) << 8) | ((int) Bytebuf[22] & 0xff));

                                            if(b_workoutBit == 1)       setValues(0, data1, 0);
                                            else if(b_workoutBit == 2)  setValues(0, data1, 0);
                                            else if(b_workoutBit == 3)  setValues(data1, 0, 0);
                                            else if(b_workoutBit == 4)  setValues(data1, 0, 0);
                                            else if(b_workoutBit == 5)  setValues(data1, 0, 0);
                                            else if(b_workoutBit == 6)  setValues(0, 0, data2);
                                            else if(b_workoutBit == 7)  setValues(0, data1, data2);
                                            else if(b_workoutBit == 8)  setValues(0, 0, 0);
                                            else                        setValues(0, 0, 0);
                                        }


                                        if(d_velocity > 2){
                                            i_repsPosCnt++;
                                            if(i_repsNegCnt > 5){
                                                mainActivity.get().listE_forceDataList.clear();
                                                mainActivity.get().listE_speedDataList.clear();
                                                mainActivity.get().listE_powerDataList.clear();
                                                chart_lineChart.clear();
                                                lineChartInit();

                                                mainActivity.get().d_maxForce = 0;
                                                mainActivity.get().d_maxSpeed = 0;
                                                mainActivity.get().d_maxPower = 0;

                                            }
                                            i_repsNegCnt = 0;

                                            if(!befWorkoutFlag && i_repsPosCnt > 5) {
                                                befWorkoutFlag = true;

                                                switch (i_selMode) {
                                                    case 0:
                                                        //Log.e("전데이터18", isokineticFrag.getWeight() + "   " + isokineticFrag.getSpeed());
                                                        settinValue_bef(isokineticFrag.getWeight(), isokineticFrag.getSpeed(), "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                                                        break;
                                                    case 1:
                                                        //Log.e("전데이터18", isokineticFrag.getWeight() + "   " + isokineticFrag.getSpeed());
                                                        settinValue_bef(isokineticFrag.getWeight(), isokineticFrag.getSpeed(), "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                                                        break;
                                                    case 2:
                                                        //Log.e("전데이터18", isokineticBiFrag.getWeight() + "   " + isokineticBiFrag.getStart() + "   " + isokineticBiFrag.getEnd() + "   " + isokineticBiFrag.getConSpeed() + "   " + isokineticBiFrag.getEccSpeed());
                                                        settinValue_bef(isokineticBiFrag.getWeight(), "0", "0", "0", isokineticBiFrag.getStart(), isokineticBiFrag.getEnd(), isokineticBiFrag.getConSpeed(), isokineticBiFrag.getEccSpeed(), "0", "0", "0", "0");
                                                        break;
                                                    case 3:
                                                        //Log.e("전데이터18", eccentricFrag1.getUpWeight() + "   " + eccentricFrag1.getDnWeight());
                                                        settinValue_bef("0", "0", eccentricFrag1.getUpWeight(), eccentricFrag1.getDnWeight(), "0", "0", "0", "0", "0", "0", "0", "0");
                                                        break;
                                                    case 4:
                                                        //Log.e("전데이터18", eccentricFrag2.getUpWeight() + "   " + eccentricFrag2.getDnWeight());
                                                        settinValue_bef("0", "0", eccentricFrag2.getUpWeight(), eccentricFrag2.getDnWeight(), "0", "0", "0", "0", "0", "0", "0", "0");
                                                        break;
                                                    case 5:
                                                        //Log.e("전데이터18", eccentricFrag3.getUpWeight() + "   " + eccentricFrag3.getDnWeight());
                                                        settinValue_bef("0", "0", eccentricFrag3.getUpWeight(), eccentricFrag3.getDnWeight(), "0", "0", "0", "0", "0", "0", "0", "0");
                                                        break;
                                                    case 6:
                                                        //Log.e("전데이터18", springFrag.getWeight() + "   " + springFrag.getConstant() + "   " + springFrag.getSetpos());
                                                        settinValue_bef(springFrag.getWeight(), "0", "0", "0", "0", "0", "0", "0", springFrag.getConstant(), springFrag.getSetpos(), "0", "0");
                                                        break;
                                                    case 7:
                                                        //Log.e("전데이터18", dampingFrag.getWeight() + "   " + dampingFrag.getSpeed() + "   " + dampingFrag.getConstant());
                                                        settinValue_bef(dampingFrag.getWeight(), dampingFrag.getSpeed(), "0", "0", "0", "0", "0", "0", dampingFrag.getConstant(), "0", "0", "0");
                                                        break;
                                                    case 8:
                                                        //Log.e("전데이터18", vibrationFrag.getWeight() + "   " + vibrationFrag.getFrequency() + "   " + vibrationFrag.getVariation() + "   " + vibrationFrag.getSetpos());
                                                        settinValue_bef(vibrationFrag.getWeight(), "0", "0", "0", "0", "0", "0", "0", "0", vibrationFrag.getSetpos(), vibrationFrag.getFrequency(), vibrationFrag.getVariation());
                                                        break;
                                                }

                                            }
                                            if(mainActivity.get().d_maxForce < d_loadcell)                      mainActivity.get().d_maxForce = d_loadcell;
                                            if(mainActivity.get().d_maxSpeed < d_velocity)                      mainActivity.get().d_maxSpeed = d_velocity;
                                            if(mainActivity.get().d_maxPower < d_loadcell * (d_velocity/100.0)) mainActivity.get().d_maxPower = d_loadcell * (d_velocity/100.0);


                                            mainActivity.get().listE_forceDataList.add(new Entry((float) d_position, (float) d_loadcell));
                                            mainActivity.get().listE_speedDataList.add(new Entry((float) d_position, (float) d_velocity));
                                            mainActivity.get().listE_powerDataList.add(new Entry((float) d_position, (float) (d_loadcell * (d_velocity/100.0))));

                                            mainActivity.get().list_Position.add(d_position);
                                            mainActivity.get().list_Force.add(d_loadcell);
                                            mainActivity.get().list_Speed.add(d_velocity);
                                            mainActivity.get().list_Power.add((double) Math.round(d_loadcell * (d_velocity/100.0) * 10) / 10);

                                            mainActivity.get().listD_avgForce.add(d_loadcell);
                                            mainActivity.get().listD_avgSpeed.add(d_velocity);
                                            mainActivity.get().listD_avgPower.add(d_loadcell * (d_velocity/100.0));

                                            mainActivity.get().d_avgForce = publicFunctions.calculateAverage(mainActivity.get().listD_avgForce);
                                            mainActivity.get().d_avgSpeed = publicFunctions.calculateAverage(mainActivity.get().listD_avgSpeed);
                                            mainActivity.get().d_avgPower = publicFunctions.calculateAverage(mainActivity.get().listD_avgPower);


                                            if(chart_lineChart != null){
                                                drawLineChart();
                                            }

                                        }
                                        if(d_velocity < -2 && i_repsPosCnt > 5){
                                            mainActivity.get().i_repsNegCnt++;

                                            if(i_repsNegCnt > 5){
                                                //LineChart Save
                                                listArr_forceEntry.add((ArrayList) listE_forceDataList.clone());
                                                listArr_speedEntry.add((ArrayList) listE_speedDataList.clone());
                                                listArr_powerEntry.add((ArrayList) listE_powerDataList.clone());

                                                listArr_avgForceData.add(String.format("%.1f", d_avgForce));
                                                d_listArr_avgForceData.add(Double.parseDouble(String.format("%.1f", d_avgForce)));
                                                listArr_avgSpeedData.add(String.format("%.1f", d_avgSpeed));
                                                d_listArr_avgSpeedData.add(Double.parseDouble(String.format("%.1f", d_avgSpeed)));
                                                listArr_avgPowerData.add(String.format("%.1f", d_avgPower));
                                                d_listArr_avgPowerData.add(Double.parseDouble(String.format("%.1f", d_avgPower)));

                                                listArr_maxForceData.add(String.format("%.1f", d_maxForce));
                                                d_listArr_maxForceData.add(Double.parseDouble(String.format("%.1f", d_maxForce)));
                                                listArr_maxSpeedData.add(String.format("%.1f", d_maxSpeed));
                                                d_listArr_maxSpeedData.add(Double.parseDouble(String.format("%.1f", d_maxSpeed)));
                                                listArr_maxPowerData.add(String.format("%.1f", d_maxPower));
                                                d_listArr_maxPowerData.add(Double.parseDouble(String.format("%.1f", d_maxPower)));

                                                //BarChart Drawing
                                                if(chart_barChart!= null){
                                                    addBarChart();
                                                    drawBarChart();

                                                    //captLineChart();
                                                    listRep_Position.add((ArrayList) list_Position.clone());
                                                    listRep_Force.add((ArrayList) list_Force.clone());
                                                    listRep_Speed.add((ArrayList) list_Speed.clone());
                                                    listRep_Power.add((ArrayList) list_Power.clone());

                                                    listRep_BarForce.add(Float.parseFloat(String.format("%.2f", d_avgForce)));
                                                    listRep_BarSpeed.add(Float.parseFloat(String.format("%.2f", d_avgSpeed)));
                                                    listRep_BarPower.add(Float.parseFloat(String.format("%.2f", d_avgPower)));

                                                    switch (i_selMode) {
                                                        case 0:
                                                            //Log.e("후데이터18", isokineticFrag.getWeight() + "   " + isokineticFrag.getSpeed());
                                                            settinValue_aft(isokineticFrag.getWeight(), isokineticFrag.getSpeed(), "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                                                            break;
                                                        case 1:
                                                            //Log.e("후데이터18", isokineticFrag.getWeight() + "   " + isokineticFrag.getSpeed());
                                                            settinValue_aft(isokineticFrag.getWeight(), isokineticFrag.getSpeed(), "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                                                            try {

                                                                //Log.e("후데이터19", listSet_Weight_bef.get(1).get(1)+ "   " + listSet_Speed_bef.get(1).get(1) + "   " + listSet_Weight_aft.get(1).get(1) + "   " + listSet_Speed_aft.get(1).get(1) + "   ");
                                                            } catch (IndexOutOfBoundsException ex) {
                                                                ;
                                                            }
                                                            break;
                                                        case 2:
                                                            //Log.e("후데이터18", isokineticBiFrag.getWeight() + "   " + isokineticBiFrag.getStart() + "   " + isokineticBiFrag.getEnd() + "   " + isokineticBiFrag.getConSpeed() + "   " + isokineticBiFrag.getEccSpeed());
                                                            settinValue_aft(isokineticBiFrag.getWeight(), "0", "0", "0", isokineticBiFrag.getStart(), isokineticBiFrag.getEnd(), isokineticBiFrag.getConSpeed(), isokineticBiFrag.getEccSpeed(), "0", "0", "0", "0");
                                                            break;
                                                        case 3:
                                                            //Log.e("후데이터18", eccentricFrag1.getUpWeight() + "   " + eccentricFrag1.getDnWeight());
                                                            settinValue_aft("0", "0", eccentricFrag1.getUpWeight(), eccentricFrag1.getDnWeight(), "0", "0", "0", "0", "0", "0", "0", "0");
                                                            break;
                                                        case 4:
                                                            //Log.e("후데이터18", eccentricFrag2.getUpWeight() + "   " + eccentricFrag2.getDnWeight());
                                                            settinValue_aft("0", "0", eccentricFrag2.getUpWeight(), eccentricFrag2.getDnWeight(), "0", "0", "0", "0", "0", "0", "0", "0");
                                                            break;
                                                        case 5:
                                                            //Log.e("후데이터18", eccentricFrag3.getUpWeight() + "   " + eccentricFrag3.getDnWeight());
                                                            settinValue_aft("0", "0", eccentricFrag3.getUpWeight(), eccentricFrag3.getDnWeight(), "0", "0", "0", "0", "0", "0", "0", "0");
                                                            break;
                                                        case 6:
                                                            //Log.e("후데이터18", springFrag.getWeight() + "   " + springFrag.getConstant() + "   " + springFrag.getSetpos());
                                                            settinValue_aft(springFrag.getWeight(), "0", "0", "0", "0", "0", "0", "0", springFrag.getConstant(), springFrag.getSetpos(), "0", "0");
                                                            break;
                                                        case 7:
                                                            //Log.e("후데이터18", dampingFrag.getWeight() + "   " + dampingFrag.getSpeed() + "   " + dampingFrag.getConstant());
                                                            settinValue_aft(dampingFrag.getWeight(), dampingFrag.getSpeed(), "0", "0", "0", "0", "0", "0", dampingFrag.getConstant(), "0", "0", "0");
                                                            break;
                                                        case 8:
                                                            //Log.e("후데이터18", vibrationFrag.getWeight() + "   " + vibrationFrag.getFrequency() + "   " + vibrationFrag.getVariation() + "   " + vibrationFrag.getSetpos());
                                                            settinValue_aft(vibrationFrag.getWeight(), "0", "0", "0", "0", "0", "0", "0", "0", vibrationFrag.getSetpos(), vibrationFrag.getFrequency(), vibrationFrag.getVariation());
                                                            break;
                                                    }
                                                    for(int i = 0 ; i < listRep_BarForce.size() ; i++) {
                                                        Log.e("횟수 데이터" + i, listRep_BarForce.size() + "   " + listRep_BarForce.get(i));
                                                    }
                                                    //
                                                }

                                                i_rep++;
                                                i_rep_org++;
                                                i_repsPosCnt = 0;
                                                //여기서 데이터 넣어주면되고

                                                befWorkoutFlag = false;

                                                mainActivity.get().listD_avgForce.clear();
                                                mainActivity.get().listD_avgSpeed.clear();
                                                mainActivity.get().listD_avgPower.clear();

                                                mainActivity.get().list_Position.removeAll(list_Position);
                                                mainActivity.get().list_Force.removeAll(list_Force);
                                                mainActivity.get().list_Speed.removeAll(list_Speed);
                                                mainActivity.get().list_Power.removeAll(list_Power);
                                            }
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
        setContentView(R.layout.activity_exercise);
        rootView = getWindow().getDecorView();
        initView();
        initSet();
        lineChartInit();
        barChartInit();


        getFlag("http://211.253.30.245/php/smartGym/center_flag.php?club_id=" + PublicValues.club_id);

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            ListPopupWindow window = (ListPopupWindow) popup.get(spWorkSelector);
            window.setModal(false);
            //window.setHeight(DisplayUtil.dpToPx(getContext(), 160));
        } catch (Exception e) {
            e.printStackTrace();
        }

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() .permitDiskReads() .permitDiskWrites() .permitNetwork().build());

        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        if (wifiManager.isWifiEnabled() == false)
            wifiManager.setWifiEnabled(true);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_REQUEST_COARSE_LOCATION);
        }

        PackageInfo pi = null;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        btn_selectActibity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExerciseActivity.this, MainActivity.class).setAction(Intent.ACTION_MAIN) .addCategory(Intent.CATEGORY_LAUNCHER) .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (timer_main != null) timer_main.cancel();
                timer_main = null;

                startActivity(intent);
                finishAffinity();
            }
        });

        btn_force.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bool_valuesFlag[0]){
                    bool_valuesFlag[0] = false;
                    btn_force.setBackgroundResource(R.drawable.btn_force_nopress);
                    layout_values[0].setVisibility(View.GONE);
                }
                else{
                    bool_valuesFlag[0] = true;
                    btn_force.setBackgroundResource(R.drawable.btn_force_press);
                    layout_values[0].setVisibility(View.VISIBLE);
                }
                drawLineChart();
                if(listF_avrForce.size() > 0){
                    changeBarChart();
                    drawBarChart();
                }
            }
        });
        btn_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bool_valuesFlag[1]){
                    bool_valuesFlag[1] = false;
                    btn_speed.setBackgroundResource(R.drawable.btn_speed_nopress);
                    layout_values[1].setVisibility(View.GONE);
                }
                else{
                    bool_valuesFlag[1] = true;
                    btn_speed.setBackgroundResource(R.drawable.btn_speed_press);
                    layout_values[1].setVisibility(View.VISIBLE);
                }
                drawLineChart();
                if(listF_avrSpeed.size() > 0){
                    changeBarChart();
                    drawBarChart();
                }
            }
        });
        btn_power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bool_valuesFlag[2]){
                    bool_valuesFlag[2] = false;
                    btn_power.setBackgroundResource(R.drawable.btn_power_nopress);
                    layout_values[2].setVisibility(View.GONE);
                }
                else{
                    bool_valuesFlag[2] = true;
                    btn_power.setBackgroundResource(R.drawable.btn_power_press);
                    layout_values[2].setVisibility(View.VISIBLE);
                }
                drawLineChart();
                if(listF_avrPower.size() > 0){
                    changeBarChart();
                    drawBarChart();
                }
            }
        });



        btn_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragmentManager = getFragmentManager();

                FragmentTransaction fragmentTransaction;
                fragmentTransaction = fragmentManager.beginTransaction();

                if(modeSelectFragment.bool_popLayoutStatus){
                    Log.e(TAG, "test true");
                    modeSelectFragment.bool_popLayoutStatus = false;
                    fragmentTransaction.remove(modeSelectFragment).commit();
                }
                else{
                    Log.e(TAG, "test false");
                    modeSelectFragment.bool_popLayoutStatus = true;
                    fragmentTransaction.replace(R.id.layout_popUp, modeSelectFragment);
                    fragmentTransaction.commit();
                }



            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager = getFragmentManager();

                FragmentTransaction fragmentTransaction;
                LoginPadFragment loginPadFragment = new LoginPadFragment();
                fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.layout_popUp, loginPadFragment);

                fragmentTransaction.commit();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
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

                listSet_Position.removeAll(listSet_Position);
                listSet_Force.removeAll(listSet_Force);
                listSet_Speed.removeAll(listSet_Speed);
                listSet_Power.removeAll(listSet_Power);

                listSet_BarForce.removeAll(listSet_BarForce);
                listSet_BarSpeed.removeAll(listSet_BarSpeed);
                listSet_BarPower.removeAll(listSet_BarPower);
/*
                listSet_Weight_bef.removeAll(listSet_Weight_bef);
                listSet_Speed_bef.removeAll(listSet_Speed_bef);
                listSet_UpWeight_bef.removeAll(listSet_UpWeight_bef);
                listSet_DnWeight_bef.removeAll(listSet_DnWeight_bef);
                listSet_Start_bef.removeAll(listSet_Start_bef);
                listSet_End_bef.removeAll(listSet_End_bef);
                listSet_ConSpeed_bef.removeAll(listSet_ConSpeed_bef);
                listSet_EccSpeed_bef.removeAll(listSet_EccSpeed_bef);
                listSet_Constant_bef.removeAll(listSet_Constant_bef);
                listSet_Setpos_bef.removeAll(listSet_Setpos_bef);
                listSet_Nowpos_bef.removeAll(listSet_Nowpos_bef);
                listSet_Frequency_bef.removeAll(listSet_Frequency_bef);
                listSet_Variation_bef.removeAll(listSet_Variation_bef);

                listSet_Weight_aft.removeAll(listSet_Weight_aft);
                listSet_Speed_aft.removeAll(listSet_Speed_aft);
                listSet_UpWeight_aft.removeAll(listSet_UpWeight_aft);
                listSet_DnWeight_aft.removeAll(listSet_DnWeight_aft);
                listSet_Start_aft.removeAll(listSet_Start_aft);
                listSet_End_aft.removeAll(listSet_End_aft);
                listSet_ConSpeed_aft.removeAll(listSet_ConSpeed_aft);
                listSet_EccSpeed_aft.removeAll(listSet_EccSpeed_aft);
                listSet_Constant_aft.removeAll(listSet_Constant_aft);
                listSet_Setpos_aft.removeAll(listSet_Setpos_aft);
                listSet_Nowpos_aft.removeAll(listSet_Nowpos_aft);
                listSet_Frequency_aft.removeAll(listSet_Frequency_aft);
                listSet_Variation_aft.removeAll(listSet_Variation_aft);*/
            }
        });

        btn_screenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if(b_workStatusBit == 2){
                    File dir = new File("sdcard/Download/screen");
                    if(!dir.exists())
                        dir.mkdirs();
                    more = 0;
                    too = 0;

                    if(i_rep > 2) {
                        screenFile = new String[i_rep];
                        screenFlag = true;
                        //mailServer.sendSecurityCode(getApplicationContext(), PublicValues.userName + "님의 " + txt_mode.getText().toString() + " " + txt_set.getText().toString() + "번째 set입니다.", "론픽 미니플러스 운동 스크린샷입니다.", PublicValues.configValues.get(4), PublicValues.userId + txt_mode.getText().toString() + txt_set.getText().toString() + "set.png");
                    } else {
                        Toast.makeText(ExerciseActivity.this, "3reps이상 운동해주세요.", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(ExerciseActivity.this, "운동종료 후 눌러주세요", Toast.LENGTH_SHORT).show();
                }
                */
                if(b_workStatusBit == 2) {
                    Intent intent = new Intent(ExerciseActivity.this, CSVActivity.class).setAction(Intent.ACTION_MAIN) .addCategory(Intent.CATEGORY_LAUNCHER) .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                    if (timer_main != null) timer_main.cancel();
                    timer_main = null;
                    startActivity(intent);
                    finishAffinity();
                    GetData_sets("http://211.253.30.245/equipment/kor/miniplus/Miniplus_SetData_SEL.php?users_id=" + "100000115" + "&workout_datetime=" + "2022-02-07");
                }
            }
        });

        btn_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(b_workStatusBit == 2){
                    i_type++;
                    if(i_type == 3) i_type = 0;

                    if(i_type == 0){
                        btn_type.setBackgroundResource(R.drawable.ic_base);
                    }
                    else if(i_type == 1){
                        btn_type.setBackgroundResource(R.drawable.ic_onehand);
                    }
                    else{
                        btn_type.setBackgroundResource(R.drawable.ic_twohand);
                    }

                    String hexString = "F2010F040" + i_type + "0000000000" + "000000000000000001FE";
                    controlBoard(hexString);

                    sendChangeValues();
                }
                else{
                    Toast.makeText(ExerciseActivity.this, "운동종료 후 눌러주세요", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((b_statusBit & 0xEE) == 0xEE) {
                    PublicValues.ResetFlag = true;
                    PublicValues.BootProcess = true;
                    String hexString = "";

                    hexString = "F2010FEE" + String.format("%02X", b_errorBit) + "0000000000" + "000000000000000001FE";

                    byte[] datapacket = PublicFunctions.hexStrToByteArr(hexString);
                    datapacket[18] = PublicFunctions.makeChkSum(datapacket);
                    if (usbService != null) {
                        usbService.write(datapacket);
                    }

                    Intent intent = new Intent(ExerciseActivity.this, MainActivity.class);

                    if (timer_main != null) timer_main.cancel();
                    timer_main = null;
                    startActivity(intent);
                    finishAffinity();


                }
            }
        });



        //ttttt
        btn_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*

                AlertDialog.Builder Dialog = new AlertDialog.Builder(ExerciseActivity.this);

                final EditText editText_pass = new EditText(ExerciseActivity.this);
                final ListView AP_List = searchWifi();

                Dialog.setTitle("네트워크 선택");
                Dialog.setView(AP_List);

                AP_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        MainActivity.sStr_wifiSelItem = MainActivity.sArr_wifiName.get(position).toString();
                        MainActivity.sStr_wifiSelItemSecu = MainActivity.sArr_wifiSecurity.get(position).toString();
                    }
                });

                Dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder Dialog = new AlertDialog.Builder(ExerciseActivity.this);

                        Dialog.setTitle("네트워크 이름 : " + MainActivity.sStr_wifiSelItem);
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

                                connectWiFi(MainActivity.sStr_wifiSelItem, str_wifiPass, MainActivity.sStr_wifiSelItemSecu);
                            }
                        }).setNegativeButton(android.R.string.cancel, null).create().show();
                    }
                }).setNegativeButton(android.R.string.cancel, null).create().show();
*/

            }
        });

        chart_barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                if(b_workStatusBit == 2){
                    selectLineChart((int)e.getX());
                }
                else    Toast.makeText(ExerciseActivity.this, "운동종료 후 눌러주세요", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        txt_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i_rep != 0){
                    i_rep = 0;
                    i_rep_org = 0;
                    befWorkoutFlag = false;
                    i_set++;
                    initValues();
                }
            }
        });
        txt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i_set = 0;
                initValues();
            }
        });






        TimerTask timeT_main = new TimerTask() {
            @Override
            public void run() {
                ExerciseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(screenFlag) {
                            if(more == i_rep) {
                                Log.e("데이터 뭔지", PublicValues.configValues.get(4) + "   " + PublicValues.userName + "   " + txt_mode.getText().toString() + "   " + txt_set.getText().toString());
                                SendMail_Async mailServer = new SendMail_Async(getApplicationContext(), PublicValues.configValues.get(4), PublicValues.userName + "님의 " + txt_mode.getText().toString() + " " + txt_set.getText().toString() + "번째 set입니다.", "론픽 미니플러스 운동 스크린샷입니다.", screenFile);
                                mailServer.execute();
                                screenFlag = false;
                                mailFlag = true;
                            }
                            else {
                                if (too == 0) {
                                    selectLineChart(more);
                                }
                                too++;
                                if (too == 11) {
                                    ScreenShot(rootView, more + 1);
                                    screenFile[more] = "sdcard/Download/screen/" + PublicValues.userId + txt_mode.getText().toString() + txt_set.getText().toString() + "set" + (more + 1) + "rep.png";
                                    more++;
                                    too = 0;
                                }
                            }
                        }else if(mailFlag) {
                            ;
                        } else {
                            if(PublicValues.userId != null && PublicValues.userId.length() > 0){
                                txt_userName.setText(PublicValues.userName + " 님");
                                btn_login.setVisibility(View.GONE);
                                btn_logout.setVisibility(View.VISIBLE);
                                //if(PublicValues.club_id.equals("1059") || PublicValues.club_id.equals("1000")) {
                                if (PublicValues.director.equals("1")) {
                                    btn_screenShot.setVisibility(View.VISIBLE);
                                }
                            }
                            else{
                                txt_userName.setText("");
                                btn_login.setVisibility(View.VISIBLE);
                                btn_logout.setVisibility(View.GONE);
                                //if(PublicValues.club_id.equals("1059") || PublicValues.club_id.equals("1000")) {
                                    btn_screenShot.setVisibility(View.GONE);
                                //}
                            }

                            //time
                            l_nowTime = System.currentTimeMillis();

                            long stopwatch = Math.abs(new Date(l_nowTime).getTime() - new Date(l_oldTime).getTime());

                            int hours = (int) (stopwatch / (1000 * 60 * 60));
                            int mins = (int) (stopwatch / (1000 * 60)) % 60;
                            long secs = (int) (stopwatch / 1000) % 60;

                            if (hours > 24) l_oldTime = System.currentTimeMillis();

                            if(!bool_setFlag && mins > 9 && mins < 15){
                                if(b_workStatusBit == 2){
                                    progressDialog.DialogStart();
                                    publicFunctions.clearUserData();
                                    Intent intent = new Intent(ExerciseActivity.this, MainActivity.class);

                                    if (timer_main != null) timer_main.cancel();
                                    timer_main = null;
                                    //PublicValues.PopLogin = false;
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            }
                            if(b_workStatusBit != 2) {
                                txt_avgForce.setText(String.format("%.1f", d_avgForce));
                                txt_avgSpeed.setText(String.format("%.1f", d_avgSpeed));
                                txt_avgPower.setText(String.format("%.1f", d_avgPower));

                                txt_maxForce.setText(String.format("%.1f", d_maxForce));
                                txt_maxSpeed.setText(String.format("%.1f", d_maxSpeed));
                                txt_maxPower.setText(String.format("%.1f", d_maxPower));

                                txt_set_avgforce.setText("AVG FORCE\n" + String.format("%.1f", publicFunctions.calculateAverage(d_listArr_avgForceData)));
                                txt_set_avgspeed.setText("AVG SPEED\n" + String.format("%.1f", publicFunctions.calculateAverage(d_listArr_avgSpeedData)));
                                txt_set_avgpower.setText("AVG POWER\n" + String.format("%.1f", publicFunctions.calculateAverage(d_listArr_avgPowerData)));
                                txt_set_maxforce.setText("MAX FORCE\n" + String.format("%.1f", publicFunctions.calculateMax(d_listArr_maxForceData)));
                                txt_set_maxspeed.setText("MAX SPEED\n" + String.format("%.1f", publicFunctions.calculateMax(d_listArr_maxSpeedData)));
                                txt_set_maxpower.setText("MAX POWER\n" + String.format("%.1f", publicFunctions.calculateMax(d_listArr_maxPowerData)));
                            }

                            txt_rep.setText("" + i_rep);
                            txt_set.setText("" + i_set);

                            if(b_workoutBit < 9)    txt_mode.setText(MODE_NAMES[b_workoutBit]);
                            else                    txt_mode.setText("");
                            if(modeSelectFragment.bool_popLayoutStatus)    setSelectModeBtn();

                            if(b_workoutBit == 2)    isokineticBiFrag.receiveValues(d_position);
                            if(b_workoutBit == 6)    springFrag.receiveValues(d_position);
                            if(b_workoutBit == 8)    vibrationFrag.receiveValues(d_position);



                            if(bool_setFlag)    txt_workTime.setText(String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
                            else                txt_breakTime.setText(String.format("%02d", hours) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs));



                            if(!bool_setFlag && b_workStatusBit == 1 && d_velocity > 2){   // 운동 시작
                                l_oldTime = System.currentTimeMillis();
                                bool_setFlag = true;

                                i_rep = 0;

                                initValues();

                                txt_workTime.setTextColor(Color.rgb(255, 255, 255));
                                txt_breakTime.setTextColor(Color.rgb(100, 100, 100));
                                strBreak = txt_breakTime.getText().toString();
                            }
                            if(bool_setFlag && i_rep > 0 && b_workStatusBit == 2){  //Set증가
                                l_oldTime = System.currentTimeMillis();
                                i_activeMoveCnt = 0;
                                bool_setFlag = false;

                                txt_workTime.setTextColor(Color.rgb(100, 100, 100));
                                txt_breakTime.setTextColor(Color.rgb(255, 255, 255));

                                i_rep_org = 0;
                                befWorkoutFlag = false;


                                mSendRepsData = i_rep;


                                ////////////////////////////////////////////////////////////////////////////////////////////////////


                                listPosDataValues     = (ArrayList) listRep_Position.clone();
                                listForceDataValues   = (ArrayList) listRep_Force.clone();
                                listSpeedDataValues   = (ArrayList) listRep_Speed.clone();
                                listPowerDataValues   = (ArrayList) listRep_Power.clone();


                                if(i_selMode == 0) {
                                    listWeightDataValues_bef = (ArrayList) listWeight_bef.clone();
                                    listSpeedDataValues_bef = (ArrayList) listSpeed_bef.clone();
                                    checkCount(listWeightDataValues_bef, 0);
                                    checkCount(listSpeedDataValues_bef, 1);

                                    listWeightDataValues_aft = (ArrayList) listWeight_aft.clone();
                                    listSpeedDataValues_aft = (ArrayList) listSpeed_aft.clone();
                                    checkCount(listWeightDataValues_aft, 12);
                                    checkCount(listSpeedDataValues_aft, 13);
                                } else if(i_selMode == 1) {
                                    listWeightDataValues_bef = (ArrayList) listWeight_bef.clone();
                                    listSpeedDataValues_bef = (ArrayList) listSpeed_bef.clone();
                                    checkCount(listWeightDataValues_bef, 0);
                                    checkCount(listSpeedDataValues_bef, 1);

                                    listWeightDataValues_aft = (ArrayList) listWeight_aft.clone();
                                    listSpeedDataValues_aft = (ArrayList) listSpeed_aft.clone();
                                    checkCount(listWeightDataValues_aft, 12);
                                    checkCount(listSpeedDataValues_aft, 13);
                                } else if(i_selMode == 2) {
                                    listWeightDataValues_bef = (ArrayList) listWeight_bef.clone();
                                    listStartDataValues_bef = (ArrayList) listStart_bef.clone();
                                    listEndDataValues_bef = (ArrayList) listEnd_bef.clone();
                                    listConSpeedDataValues_bef = (ArrayList) listConSpeed_bef.clone();
                                    listEccSpeedDataValues_bef = (ArrayList) listEccSpeed_bef.clone();
                                    checkCount(listWeightDataValues_bef, 0);
                                    checkCount(listStartDataValues_bef, 4);
                                    checkCount(listEndDataValues_bef, 5);
                                    checkCount(listConSpeedDataValues_bef,6);
                                    checkCount(listEccSpeedDataValues_bef,7);

                                    listWeightDataValues_aft = (ArrayList) listWeight_aft.clone();
                                    listStartDataValues_aft = (ArrayList) listStart_aft.clone();
                                    listEndDataValues_aft = (ArrayList) listEnd_aft.clone();
                                    listConSpeedDataValues_aft = (ArrayList) listConSpeed_aft.clone();
                                    listEccSpeedDataValues_aft = (ArrayList) listEccSpeed_aft.clone();
                                    checkCount(listWeightDataValues_aft, 12);
                                    checkCount(listStartDataValues_aft, 16);
                                    checkCount(listEndDataValues_aft, 17);
                                    checkCount(listConSpeedDataValues_aft, 18);
                                    checkCount(listEccSpeedDataValues_aft, 19);
                                } else if(i_selMode == 3) {
                                    listUpWeightDataValues_bef = (ArrayList) listUpWeight_bef.clone();
                                    listDnWeightDataValues_bef = (ArrayList) listDnWeight_bef.clone();
                                    checkCount(listUpWeightDataValues_bef, 2);
                                    checkCount(listDnWeightDataValues_bef, 3);

                                    listUpWeightDataValues_aft = (ArrayList) listUpWeight_aft.clone();
                                    listDnWeightDataValues_aft = (ArrayList) listDnWeight_aft.clone();
                                    checkCount(listUpWeightDataValues_aft, 14);
                                    checkCount(listDnWeightDataValues_aft, 15);
                                } else if(i_selMode == 4) {
                                    listUpWeightDataValues_bef = (ArrayList) listUpWeight_bef.clone();
                                    listDnWeightDataValues_bef = (ArrayList) listDnWeight_bef.clone();
                                    checkCount(listUpWeightDataValues_bef, 2);  //여기서 아웃오브 메모리뜬다.
                                    checkCount(listDnWeightDataValues_bef, 3);

                                    listUpWeightDataValues_aft = (ArrayList) listUpWeight_aft.clone();
                                    listDnWeightDataValues_aft = (ArrayList) listDnWeight_aft.clone();
                                    checkCount(listUpWeightDataValues_aft, 14);
                                    checkCount(listDnWeightDataValues_aft, 15);
                                } else if(i_selMode == 5) {
                                    listUpWeightDataValues_bef = (ArrayList) listUpWeight_bef.clone();
                                    listDnWeightDataValues_bef = (ArrayList) listDnWeight_bef.clone();
                                    checkCount(listUpWeightDataValues_bef, 2);
                                    checkCount(listDnWeightDataValues_bef, 3);

                                    listUpWeightDataValues_aft = (ArrayList) listUpWeight_aft.clone();
                                    listDnWeightDataValues_aft = (ArrayList) listDnWeight_aft.clone();
                                    checkCount(listUpWeightDataValues_aft, 14);
                                    checkCount(listDnWeightDataValues_aft, 15);
                                } else if(i_selMode == 6) {
                                    listWeightDataValues_bef = (ArrayList) listWeight_bef.clone();
                                    listConstantDataValues_bef = (ArrayList) listConstant_bef.clone();
                                    listSetposDataValues_bef = (ArrayList) listSetpos_bef.clone();
                                    checkCount(listWeightDataValues_bef, 0);
                                    checkCount(listConstantDataValues_bef, 8);
                                    checkCount(listSetposDataValues_bef, 9);

                                    listWeightDataValues_aft = (ArrayList) listWeight_aft.clone();
                                    listConstantDataValues_aft = (ArrayList) listConstant_aft.clone();
                                    listSetposDataValues_aft = (ArrayList) listSetpos_aft.clone();
                                    checkCount(listWeightDataValues_aft, 12);
                                    checkCount(listConstantDataValues_aft, 20);
                                    checkCount(listSetposDataValues_aft, 21);
                                } else if(i_selMode == 7) {
                                    listWeightDataValues_bef = (ArrayList) listWeight_bef.clone();
                                    listSpeedDataValues_bef = (ArrayList) listSpeed_bef.clone();
                                    listConstantDataValues_bef = (ArrayList) listConstant_bef.clone();
                                    checkCount(listWeightDataValues_bef, 0);
                                    checkCount(listSpeedDataValues_bef, 1);
                                    checkCount(listConstantDataValues_bef, 8);

                                    listWeightDataValues_aft = (ArrayList) listWeight_aft.clone();
                                    listSpeedDataValues_aft = (ArrayList) listSpeed_aft.clone();
                                    listConstantDataValues_aft = (ArrayList) listConstant_aft.clone();
                                    checkCount(listWeightDataValues_aft, 12);
                                    checkCount(listSpeedDataValues_aft, 13);
                                    checkCount(listConstantDataValues_aft, 20);
                                } else {
                                    listWeightDataValues_bef = (ArrayList) listWeight_bef.clone();
                                    listSetposDataValues_bef = (ArrayList) listSetpos_bef.clone();
                                    listFrequencyDataValues_bef = (ArrayList) listFrequency_bef.clone();
                                    listVariationDataValues_bef = (ArrayList) listVariation_bef.clone();
                                    checkCount(listWeightDataValues_bef, 0);
                                    checkCount(listSetposDataValues_bef, 9);
                                    checkCount(listFrequencyDataValues_bef, 10);
                                    checkCount(listVariationDataValues_bef, 11);

                                    listWeightDataValues_aft = (ArrayList) listWeight_aft.clone();
                                    listSetposDataValues_aft = (ArrayList) listSetpos_aft.clone();
                                    listFrequencyDataValues_aft = (ArrayList) listFrequency_aft.clone();
                                    listVariationDataValues_aft = (ArrayList) listVariation_aft.clone();
                                    checkCount(listWeightDataValues_aft, 12);
                                    checkCount(listSetposDataValues_aft, 21);
                                    checkCount(listFrequencyDataValues_aft, 22);
                                    checkCount(listVariationDataValues_aft, 23);
                                }

                                listRep_Position.removeAll(listRep_Position);
                                listRep_Force.removeAll(listRep_Force);
                                listRep_Speed.removeAll(listRep_Speed);
                                listRep_Power.removeAll(listRep_Power);

                                listRep_BarForce.removeAll(listRep_BarForce);
                                listRep_BarSpeed.removeAll(listRep_BarSpeed);
                                listRep_BarPower.removeAll(listRep_BarPower);

                                listWeight_bef.removeAll(listWeight_bef);
                                listSpeed_bef.removeAll(listSpeed_bef);
                                listUpWeight_bef.removeAll(listUpWeight_bef);
                                listDnWeight_bef.removeAll(listDnWeight_bef);
                                listStart_bef.removeAll(listStart_bef);
                                listEnd_bef.removeAll(listEnd_bef);
                                listConSpeed_bef.removeAll(listConSpeed_bef);
                                listEccSpeed_bef.removeAll(listEccSpeed_bef);
                                listConstant_bef.removeAll(listConstant_bef);
                                listSetpos_bef.removeAll(listSetpos_bef);
                                listNowpos_bef.removeAll(listNowpos_bef);
                                listFrequency_bef.removeAll(listFrequency_bef);
                                listVariation_bef.removeAll(listVariation_bef);

                                listWeight_aft.removeAll(listWeight_aft);
                                listSpeed_aft.removeAll(listSpeed_aft);
                                listUpWeight_aft.removeAll(listUpWeight_aft);
                                listDnWeight_aft.removeAll(listDnWeight_aft);
                                listStart_aft.removeAll(listStart_aft);
                                listEnd_aft.removeAll(listEnd_aft);
                                listConSpeed_aft.removeAll(listConSpeed_aft);
                                listEccSpeed_aft.removeAll(listEccSpeed_aft);
                                listConstant_aft.removeAll(listConstant_aft);
                                listSetpos_aft.removeAll(listSetpos_aft);
                                listNowpos_aft.removeAll(listNowpos_aft);
                                listFrequency_aft.removeAll(listFrequency_aft);
                                listVariation_aft.removeAll(listVariation_aft);


                                ////////////////////////////////////////////////////////////////////////////////////////////////////

                                if(i_rep > 2){
                                    i_set++;


                                    //사용처 불명
                                    /*listSet_Position.add((ArrayList) listRep_Position.clone());
                                    listSet_Force.add((ArrayList) listRep_Force.clone());
                                    listSet_Speed.add((ArrayList) listRep_Speed.clone());
                                    listSet_Power.add((ArrayList) listRep_Power.clone());

                                    listSet_BarForce.add((ArrayList) listRep_BarForce.clone());
                                    listSet_BarSpeed.add((ArrayList) listRep_BarSpeed.clone());
                                    listSet_BarPower.add((ArrayList) listRep_BarPower.clone());

                                    listSet_Weight_bef.add((ArrayList) listWeight_bef.clone());
                                    listSet_Speed_bef.add((ArrayList) listSpeed_bef.clone());
                                    listSet_UpWeight_bef.add((ArrayList) listUpWeight_bef.clone());
                                    listSet_DnWeight_bef.add((ArrayList) listDnWeight_bef.clone());
                                    listSet_Start_bef.add((ArrayList) listStart_bef.clone());
                                    listSet_End_bef.add((ArrayList) listEnd_bef.clone());
                                    listSet_ConSpeed_bef.add((ArrayList) listConSpeed_bef.clone());
                                    listSet_EccSpeed_bef.add((ArrayList) listEccSpeed_bef.clone());
                                    listSet_Constant_bef.add((ArrayList) listConstant_bef.clone());
                                    listSet_Setpos_bef.add((ArrayList) listSetpos_bef.clone());
                                    listSet_Nowpos_bef.add((ArrayList) listNowpos_bef.clone());
                                    listSet_Frequency_bef.add((ArrayList) listFrequency_bef.clone());
                                    listSet_Variation_bef.add((ArrayList) listVariation_bef.clone());

                                    listSet_Weight_aft.add((ArrayList) listWeight_aft.clone());
                                    listSet_Speed_aft.add((ArrayList) listSpeed_aft.clone());
                                    listSet_UpWeight_aft.add((ArrayList) listUpWeight_aft.clone());
                                    listSet_DnWeight_aft.add((ArrayList) listDnWeight_aft.clone());
                                    listSet_Start_aft.add((ArrayList) listStart_aft.clone());
                                    listSet_End_aft.add((ArrayList) listEnd_aft.clone());
                                    listSet_ConSpeed_aft.add((ArrayList) listConSpeed_aft.clone());
                                    listSet_EccSpeed_aft.add((ArrayList) listEccSpeed_aft.clone());
                                    listSet_Constant_aft.add((ArrayList) listConstant_aft.clone());
                                    listSet_Setpos_aft.add((ArrayList) listSetpos_aft.clone());
                                    listSet_Nowpos_aft.add((ArrayList) listNowpos_aft.clone());
                                    listSet_Frequency_aft.add((ArrayList) listFrequency_aft.clone());
                                    listSet_Variation_aft.add((ArrayList) listVariation_aft.clone());*/
                                    //

                                    //for(int i = 0 ; i < listSet_BarForce.size() ; i++) {
                                    //    Log.e("2횟수 데이터" + i, listSet_BarForce.size() + "   " + listSet_BarForce.get(i));
                                    //}
                                    //Log.e("3횟수 데이터", listSet_BarForce.size() + "   " + listSet_BarForce.get(0).get(2));
                                    if(PublicValues.userId.length() == 0) {
                                        listWeight_bef.removeAll(listWeight_bef);
                                        listSpeed_bef.removeAll(listSpeed_bef);
                                        listUpWeight_bef.removeAll(listUpWeight_bef);
                                        listDnWeight_bef.removeAll(listDnWeight_bef);
                                        listStart_bef.removeAll(listStart_bef);
                                        listEnd_bef.removeAll(listEnd_bef);
                                        listConSpeed_bef.removeAll(listConSpeed_bef);
                                        listEccSpeed_bef.removeAll(listEccSpeed_bef);
                                        listConstant_bef.removeAll(listConstant_bef);
                                        listSetpos_bef.removeAll(listSetpos_bef);
                                        listNowpos_bef.removeAll(listNowpos_bef);
                                        listFrequency_bef.removeAll(listFrequency_bef);
                                        listVariation_bef.removeAll(listVariation_bef);

                                        listWeight_aft.removeAll(listWeight_aft);
                                        listSpeed_aft.removeAll(listSpeed_aft);
                                        listUpWeight_aft.removeAll(listUpWeight_aft);
                                        listDnWeight_aft.removeAll(listDnWeight_aft);
                                        listStart_aft.removeAll(listStart_aft);
                                        listEnd_aft.removeAll(listEnd_aft);
                                        listConSpeed_aft.removeAll(listConSpeed_aft);
                                        listEccSpeed_aft.removeAll(listEccSpeed_aft);
                                        listConstant_aft.removeAll(listConstant_aft);
                                        listSetpos_aft.removeAll(listSetpos_aft);
                                        listNowpos_aft.removeAll(listNowpos_aft);
                                        listFrequency_aft.removeAll(listFrequency_aft);
                                        listVariation_aft.removeAll(listVariation_aft);
                                    }
                                    if(i_set == 1 && PublicValues.userId.length() == 0) appPopUpDialog.DialogStart();

                                    //if(PublicValues.userId.length() > 0){
                                    InsertWorkoutData workoutData = new InsertWorkoutData();
                                    workoutData.execute();
                                    //}
                                }
                                //여기에 가야할듯



                            }
                            if(b_workStatusBit == 2){
                                if(i_activeMoveCnt < 30)    i_activeMoveCnt++;
                            }



                            if ((b_statusBit & 0xEE) == 0xEE) {
                                btn_reset.setVisibility(View.VISIBLE);
                                txt_errorCode.setVisibility(View.VISIBLE);
                                txt_errorCode.setText("ERROR : " + b_errorBit);
                            } else {
                                btn_reset.setVisibility(View.GONE);
                                txt_errorCode.setVisibility(View.GONE);
                            }
                            //if (isConnected(getApplicationContext())) {//네트워크 연결유무 표기
                            if (!Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress()).contains("0.0.0.0")) {
                                txt_wifiName.setText(wifiManager.getConnectionInfo().getSSID());
                                btn_wifi.setBackgroundResource(R.drawable.btn_wifi_able);
                            } else {
                                btn_wifi.setBackgroundResource(R.drawable.btn_wifi_unable);
                            }

                            if(bool_changeModeFlag){
                                if(!bool_breakStatus){
                                    controlBoard(MOTER_STOP_SERIAL);
                                    Log.i(TAG, "MOTER_STOP_SERIAL send ");
                                }
                                else if(bool_breakStatus && bool_changeFragFlag){
                                    fragChange();
                                }
                                else if(bool_breakStatus && !bool_changeFragFlag){
                                    Log.i(TAG, "MOTER_START_SERIAL");
                                }
                            }
                            else if(bool_breakStatus){
                                controlBoard(str_fragSerial);
                                controlBoard(MOTER_START_SERIAL);
                            }

                            if(bool_resetFlag){
                                if(PublicValues.BootProcess && bool_breakStatus ){
                                    setFragValue(str_fragSerial);
                                    controlBoard(MOTER_START_SERIAL);
                                }
                                else if(!bool_breakStatus && b_statusBit == 3){
                                    bool_resetFlag = false;
                                    PublicValues.BootProcess = false;
                                }
                            }
                        }
/*                        if(usbService == null){
                            try {
                                //setFilters();
                                startService(UsbService.class, usbConnection, null);
                            }catch (Exception e){
                                Log.e(TAG, "ERR!!! = " + e);
                            }

                        }*/




                    }
                });
            }
        };

        timer_main = new Timer();
        timer_main.schedule(timeT_main, 0, 100);

        mainHandler = new ExerciseActivity.MainHandler(this);

    }


    @Override
    public void onResume() {
        super.onResume();



        setFilters();
        startService(UsbService.class, usbConnection, null);


        receiveFragNum(4);



        hideSystemUI();
    }


    @Override
    public void onPause() {
        progressDialog.DialogStop();
        super.onPause();
        unregisterReceiver(usbReceiver);
        unbindService(usbConnection);

    }

    @Override
    protected void onStop() {
        progressDialog.DialogStop();
        super.onStop();
        if (timer_main != null) timer_main.cancel();
        timer_main = null;
    }

}