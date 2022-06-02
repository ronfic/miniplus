package com.example.miniplus_dev;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class LoginPadActivity extends Activity {

    private String TAG = "LoginPad";
    TextView Passtext;
    Button btn_one;
    Button btn_two;
    Button btn_three;
    Button btn_four;
    Button btn_five;
    Button btn_six;
    Button btn_seven;
    Button btn_eight;
    Button btn_nine;
    Button btn_zero;
    Button mbtn_cancel;
    Button mbtn_login;
    Button btn_delete;
    Button btn_delete_all;
    String delete;

    Button userGroupLoginBtn;
    boolean GroupLoginChk = false;
    boolean prevent = false;

    String txt_password;

    String myJSON;

    ArrayList<HashMap<String, String>> personList;

    Display windowSize;
    Point size;
    int win_width, win_height;
    float textSize;
    String SSID;

    private final String TAG_USERID = "id";
    private final String TAG_USERNAME = "user_name";
    private final String TAG_LOCATION = "club_name";
    private final String TAG_GENDER = "gender";
    private final String TAG_HEIGHT = "height";
    private final String TAG_WEIGHT = "weight";
    private final String TAG_RESULTS = "result";
    private final String TAG_DIRECTOR = "is_director";


    ListView userGroup_list;
    ArrayAdapter adapter;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        windowSize = getWindowManager().getDefaultDisplay();
        size = new Point();
        win_width = size.x;
        win_height = size.y;

        Intent intent = getIntent();
        SSID = intent.getStringExtra("SSID");

        if (win_width > 640 && win_height > 480) {
            setContentView(R.layout.activity_login_pad);
        } else {
            setContentView(R.layout.activity_login_pad2);
        }

        Passtext = (TextView) findViewById(R.id.password_box);
        btn_one = (Button) findViewById(R.id.one);
        btn_two = (Button) findViewById(R.id.two);
        btn_three = (Button) findViewById(R.id.three);
        btn_four = (Button) findViewById(R.id.four);
        btn_five = (Button) findViewById(R.id.five);
        btn_six = (Button) findViewById(R.id.six);
        btn_seven = (Button) findViewById(R.id.seven);
        btn_eight = (Button) findViewById(R.id.eight);
        btn_nine = (Button) findViewById(R.id.nine);
        btn_zero = (Button) findViewById(R.id.zero);
        mbtn_cancel = (Button) findViewById(R.id.btn_cancel);
        mbtn_login = (Button) findViewById(R.id.btn_login);
        btn_delete = (Button) findViewById(R.id.btn_Delete);
        btn_delete_all = (Button) findViewById(R.id.btn_Delete_all);

        userGroupLoginBtn = (Button) findViewById(R.id.userGroupLoginBtn);

        prevent = false;

        btn_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (Passtext.length() < 8) {
                        Passtext.append("1");
                    }
                }
            }
        });

        btn_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (Passtext.length() < 8) {
                        Passtext.append("2");
                    }
                }
            }
        });

        btn_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (Passtext.length() < 8) {
                        Passtext.append("3");
                    }
                }
            }
        });
        btn_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (Passtext.length() < 8) {
                        Passtext.append("4");
                    }
                }
            }
        });

        btn_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (Passtext.length() < 8) {
                        Passtext.append("5");
                    }
                }
            }
        });

        btn_six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (Passtext.length() < 8) {
                        Passtext.append("6");
                    }
                }
            }
        });

        btn_seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (Passtext.length() < 8) {
                        Passtext.append("7");
                    }
                }
            }
        });

        btn_eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (Passtext.length() < 8) {
                        Passtext.append("8");
                    }
                }
            }
        });

        btn_nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (Passtext.length() < 8) {
                        Passtext.append("9");
                    }
                }
            }
        });

        btn_zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (Passtext.length() < 8) {
                        Passtext.append("0");
                    }
                }
            }
        });
        mbtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    prevent = true;
                    Intent intent = new Intent(LoginPadActivity.this, SelectModeActivity.class);
                    intent.putExtra("SSID", SSID);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mbtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    prevent = true;
                    txt_password = Passtext.getText().toString();
                    GetData("http://211.253.30.245/equipment/kor/common/Login.php?txt_password=" + txt_password);
                    Log.e(TAG, "로그인버튼 PASSWORD : " + txt_password);
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    delete = Passtext.getText().toString();
                    if (delete.length() > 0)
                        Passtext.setText(delete.substring(0, delete.length() - 1));
                }
            }
        });

        btn_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    Passtext.setText(null);
                }
            }
        });

        userGroupLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    prevent = true;
                    if (!GroupLoginChk) GroupLoginChk = true;
                    else GroupLoginChk = false;
                }
            }
        });
        userGroup_list = (ListView) findViewById(R.id.userGroup_list);
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
    }

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
                inputIntent();
            }
        }
        GetDataJSON json = new GetDataJSON();
        json.execute(str);
    }

    InputNumberDialog input = new InputNumberDialog(this);

    protected void inputIntent() {

        try {
            JSONObject jsonObj = new JSONObject(new JSONObject(myJSON).getString(TAG_RESULTS));

            PublicValues.userId = jsonObj.getString(TAG_USERID);
            PublicValues.userName = jsonObj.getString(TAG_USERNAME);
            PublicValues.userGender = jsonObj.getString(TAG_GENDER);
            PublicValues.userHeight = jsonObj.getString(TAG_HEIGHT);
            PublicValues.userWeight = jsonObj.getString(TAG_WEIGHT);
            PublicValues.userBirth = jsonObj.getString("birthday");
            PublicValues.userClub = jsonObj.getString("clubs_id");
            PublicValues.director = jsonObj.getString(TAG_DIRECTOR);


            long now = System.currentTimeMillis();

            Date date = new Date(now);
            SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd");
            String timeNow = now_time.format(date);

            if (PublicValues.userBirth != null) {
                try{
                    PublicValues.userAge = Integer.valueOf(timeNow.substring(0, 4)) - Integer.valueOf(PublicValues.userBirth.substring(0, 4)) + 1;
                }catch (Exception e){
                    Log.e(TAG, "input Age Err : " + e);
                }

            }

            if (PublicValues.userId != null && PublicValues.userId.length() > 0 && !PublicValues.userId.equals("Guest")) {
                Log.i(TAG, "LOGIN Success");
                intent = new Intent(LoginPadActivity.this, MainActivity.class);

                if (PublicValues.userHeight == null || PublicValues.userHeight.length() < 1) {
                    insertH();

                } else if (PublicValues.userWeight == null || PublicValues.userWeight.length() < 1) {
                    insertW();
                }

                if ((PublicValues.userHeight != null && PublicValues.userHeight.length() > 0) && (PublicValues.userWeight != null && PublicValues.userWeight.length() > 0)) {
                    //insertBodyInfoData bodydata = new insertBodyInfoData();
                    //bodydata.execute();
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "비밀번호를 확인해 주세요.", Toast.LENGTH_LONG).show();
                Passtext.setText(null);
                prevent = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void insertH() {
        input.initDialogStart(false);
        input.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = input.inputNum.getText().toString().trim();

                if (str.charAt(0) == '0') {
                    Log.e("success", "0 input");
                    input.inputNum.setText(null);
                    input.inputNum.setHint("잘못 된 입력입니다.");
                    input.inputNum.setHintTextColor(Color.RED);
                } else {
                    PublicValues.userHeight = str;
                    Log.e("success", "height : " + PublicValues.userHeight);
                    if (PublicValues.userWeight == null || PublicValues.userWeight.length() < 1) {
                        input.initDialogStop();
                        insertW();
                    } else {
                        insertBodyInfoData bodydata = new insertBodyInfoData();
                        bodydata.execute();
                        input.initDialogStop();
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }


    private void insertW() {
        input.initDialogStart(true);
        input.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = input.inputNum.getText().toString().trim();

                if (str.charAt(0) == '0') {
                    Log.e("success", "0 input");
                    input.inputNum.setText(null);
                    input.inputNum.setHint("잘못 된 입력입니다.");
                    input.inputNum.setHintTextColor(Color.RED);
                } else {
                    PublicValues.userWeight = str;
                    Log.e("success", "weight : " + PublicValues.userWeight);
                    insertBodyInfoData bodydata = new insertBodyInfoData();
                    bodydata.execute();
                    input.initDialogStop();
                    startActivity(intent);
                    finish();
                }
            }
        });
    }



    public class insertBodyInfoData extends AsyncTask<Void, Integer, Void> {
        String data = "";

        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(Void... unused) {
            String param = "";
            String s_url;

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time_now = now_time.format(date);

            s_url = "http://211.253.30.245/equipment/kor/common/BodyInfoUpdate.php";

            param = param + "&users_id=" + PublicValues.userId +
                    "&height=" + PublicValues.userHeight +
                    "&weight=" + PublicValues.userWeight +
                    "&measure_date=" + time_now;

            Log.e("POST", param);

            try {
                // 서버연결
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

        }
    }

}
