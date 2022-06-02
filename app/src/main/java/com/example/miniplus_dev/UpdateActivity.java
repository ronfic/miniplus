package com.example.miniplus_dev;

import android.Manifest;
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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
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
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.miniplus_dev.setting.LoginPadFragment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.miniplus_dev.PublicValues.needUpdateVersion;


public class UpdateActivity extends Activity {

    private String TAG = UpdateActivity.class.getSimpleName();


    private final String fileName = "mpconfig";




    String versionNum;

    Timer timer_main;



    NetworkInfo activeNetwork;

    public static String sStr_wifiSelItem = "";
    public static String sStr_wifiSelItemSecu = "";


    String str_wifiPass = "";

    public static ArrayList<String> sArr_wifiSecurity = new ArrayList<>();
    public static ArrayList<String> sArr_wifiName = new ArrayList<>();


    WifiManager wifiManager;


    ProgressBar updateStatusProgressBar;
    Button btn_yes, btn_no;
    TextView txt_nowVersion, txt_newVersion;



    //파일 저장
    public void saveItemsToFile() {
        File file = new File(getFilesDir(), fileName);
        FileWriter fw = null;
        BufferedWriter bufwr = null;

        Log.e(TAG, "AA1 + " + PublicValues.configValues.get(0));
        Log.e(TAG, "AA2 + " + PublicValues.configValues.get(1));


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
        File file = new File(getFilesDir(), fileName);
        FileReader fileReader = null;
        BufferedReader bufReader = null;
        String str;

        if (file.exists()) {
            try {
                fileReader = new FileReader(file);
                bufReader = new BufferedReader(fileReader);

                PublicValues.configValues.clear();

                while ((str = bufReader.readLine()) != null) {
                    PublicValues.configValues.add(str);
                    Log.e(TAG, "load File" + str);
                }


                bufReader.close();
                fileReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i < 2; i++){
            if(PublicValues.configValues.size() < i+1){
                PublicValues.configValues.add("0");
            }
        }
        Log.i(TAG, "load Config File");
        PublicValues.serialNumber = PublicValues.configValues.get(0);
        PublicValues.lastVersionInfo = PublicValues.configValues.get(1);
        PublicValues.stiction = PublicValues.configValues.get(2);
    }



    private ListView searchWifi() {

        final ListView WIFI_LIST = new ListView(UpdateActivity.this);
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
                        ArrayAdapter adapter = new ArrayAdapter(UpdateActivity.this, android.R.layout.simple_list_item_1, sArr_wifiName);
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


    private void initView() {

        updateStatusProgressBar = (ProgressBar) findViewById(R.id.progress);

        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_no  = (Button) findViewById(R.id.btn_no);

        txt_nowVersion  = (TextView) findViewById(R.id.txt_nowVersion);
        txt_newVersion  = (TextView) findViewById(R.id.txt_newVersion);

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
                if(versionNum.equals(result))   mUpdatePlease = false;
                else                            mUpdatePlease = true;
            }
        }
        GetDataJSON json = new GetDataJSON();
        json.execute("http://211.253.30.245/miniplus_update/versionInfo.php");
    }


    boolean mUpdatePlease = false;


    private void updateStart(String str) {
        class GetDataJSON extends AsyncTask<String, String, String> {



            @Override
            protected String doInBackground(String... sUrl) {
                File pathDevice = getFilesDir();
                //String path = "/sdcard/Download/miniplus_dev.apk";
                ///data/user/0/com.example.miniplus_dev_100/files
                Log.i(TAG, "pathDevice = " + getFilesDir());
                String path = getFilesDir() + "/miniplus_dev.apk";


                try {
                    URL url = new URL(sUrl[0]);
                    URLConnection connection = url.openConnection();
                    connection.connect();
                    int fileLength = connection.getContentLength();
                    // download the file

                    Log.i(TAG, "path : " + pathDevice);
                    outputFile = new File(path);
                    if (outputFile.exists()) { // 기존 파일 존재시 삭제하고 다운로드
                        Log.i(TAG, "del");
                        outputFile.delete();
                    }
                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(outputFile);


                    Log.i(TAG, "path : " + pathDevice);
                    byte data[] = new byte[1024];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        Log.i(TAG, "Now : " + (int) (total * 100 / fileLength));
                        updateStatusProgressBar.setProgress((int) (total * 100 / fileLength));
                        output.write(data, 0, count);
                    }

                    output.flush();
                    output.close();
                    input.close();
                } catch (Exception e) {
                    Log.e("YourApp", "Well that didn't work out so well...");
                    Log.e("YourApp", e.getMessage());
                }
                //return path;
                return null;
            }


            // begin the installation by opening the resulting file
            @Override
            protected void onPostExecute(String path) {

                MediaScannerConnection.scanFile(getApplicationContext(), new String[]{outputFile.getAbsolutePath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String s, Uri uri) {

                    }
                });

                updateStatusProgressBar.setProgress(0);
                installApk(outputFile);
            }
        }
        GetDataJSON json = new GetDataJSON();
        json.execute(str);
    }

    private File outputFile;

    public void installApk(File file) {
        Uri fileUri = FileProvider.getUriForFile(this.getApplicationContext(), this.getApplicationContext().getPackageName() + ".fileprovider",file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
        finish();
    }


    public void deleteApk() {
        Uri uri = Uri.fromParts("package", "com.example.miniplus_dev_100", null);
        Intent delIntent = new Intent(Intent.ACTION_DELETE, uri);
        startActivity(delIntent);
    }


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        PackageInfo pi = null;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versionNum = pi.versionName;
        initView();

        loadItemsFromFile();
        saveItemsToFile();


        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        if (wifiManager.isWifiEnabled() == false)
            wifiManager.setWifiEnabled(true);



        //ttttt 21.01.06 update test
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStart("http://211.253.30.245/miniplus_update/" + needUpdateVersion + "/miniplus_dev.apk");
            }
        });

        //ttttt 21.01.06 update test
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);

                if (timer_main != null) timer_main.cancel();
                timer_main = null;

                startActivity(intent);
                finishAffinity();
            }
        });





        TimerTask timeT_main = new TimerTask() {
            @Override
            public void run() {
                UpdateActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        txt_nowVersion.setText(versionNum);
                        txt_newVersion.setText(needUpdateVersion);


                    }
                });
            }
        };

        timer_main = new Timer();
        timer_main.schedule(timeT_main, 0, 250);
    }



    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
    }


    @Override
    public void onPause() {
        super.onPause();
    }


}
