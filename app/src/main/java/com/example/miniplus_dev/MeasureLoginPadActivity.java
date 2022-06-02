package com.example.miniplus_dev;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniplus_dev.setting.LoginPadFragment;

import org.json.JSONArray;
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
import java.util.Date;

public class MeasureLoginPadActivity extends Activity {


    private String TAG = "MeasureLoginPad";

    private Context context;

    //Seq_Exist seq_Exist;  //211124
    SelectFitt selectFitt;
    //MakeMember makeMember;    //211124
    //ChangeData changeData;    //211124

    TextView txtPass;

    String inputPassword;

    Button btnLogin, btnExit;
    Button btnCancle;

    Button[] btnNum = new Button[10];

    String f_seq;
    String f_name;
    String f_gender;
    String f_birth;
    String f_height;
    String f_weight;
    String f_phone;
    String f_phone_org;
    String f_email;


    Intent intent;


    private final String TAG_USERID = "id";
    private final String TAG_USERNAME = "user_name";
    private final String TAG_LOCATION = "club_name";
    private final String TAG_GENDER = "gender";
    private final String TAG_HEIGHT = "height";
    private final String TAG_WEIGHT = "weight";
    private final String TAG_DIRECTOR = "is_director";
    private final String TAG_RESULTS = "result";

    String myJSON;


    String where;

    boolean prevent = false;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_login_pad);

        context = getApplicationContext();

        //seq_Exist = new Seq_Exist();  //211124
        //makeMember = new MakeMember();    //211124
        //changeData = new ChangeData();    //211124

        initIntent();
        init();

        hideSystemUI();

        prevent = false;

        btnNum[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txtPass.length() < 11) txtPass.append("0");
                }
            }
        });
        btnNum[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txtPass.length() < 11) txtPass.append("1");
                }
            }
        });
        btnNum[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txtPass.length() < 11) txtPass.append("2");
                }
            }
        });
        btnNum[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txtPass.length() < 11) txtPass.append("3");
                }
            }
        });
        btnNum[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txtPass.length() < 11) txtPass.append("4");
                }
            }
        });
        btnNum[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txtPass.length() < 11) txtPass.append("5");
                }
            }
        });
        btnNum[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txtPass.length() < 11) txtPass.append("6");
                }
            }
        });
        btnNum[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txtPass.length() < 11) txtPass.append("7");
                }
            }
        });
        btnNum[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txtPass.length() < 11) txtPass.append("8");
                }
            }
        });
        btnNum[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txtPass.length() < 11) txtPass.append("9");
                }
            }
        });


        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    String delete = txtPass.getText().toString();
                    if (delete.length() > 0)
                        txtPass.setText(delete.substring(0, delete.length() - 1));
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    prevent = true;
                    PublicValues.MeasureResultFlag = true;
                    inputPassword = txtPass.getText().toString();
                    Log.e(TAG, "로그인버튼 PASSWORD : " + inputPassword);
                    if (PublicValues.configValues.get(3).equals("1121")) {
                        selectFitt = new SelectFitt();
                        selectFitt.execute("http://211.253.30.245/php/fitt/fitt_getMember2.php?phoneNum=" + txtPass.getText().toString());  //211124
                    }
                    //if(where.equals("MeasureTestActivity")){
                    else {
                        GetData("http://211.253.30.245/equipment/kor/common/Login.php?txt_password=" + inputPassword);
                    }
                }
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    prevent = true;
                    PublicValues.MeasureResultFlag = false;
                    if (where.equals("MeasureTestActivity")) {
                        Intent intent = new Intent(MeasureLoginPadActivity.this, MeasureTestActivity.class);

                        startActivity(intent);

                        finishAffinity();
                    } else {
                        GetData("http://211.253.30.245/backend/index.php/MiniplusTest/getTestData?users_id=" + PublicValues.userId);
                    }
                }
            }
        });
    }


    private void initIntent() {
        intent = getIntent();

        where = intent.getStringExtra("whereFrom");


    }


    private void init() {

        txtPass = (TextView) findViewById(R.id.password_box);

        btnLogin = (Button) findViewById(R.id.btn_enter);
        btnExit = (Button) findViewById(R.id.btn_exit);

        btnCancle = (Button) findViewById(R.id.btn_cancel);
        //btnDeleteAll = (Button) findViewById(R.id.measure_login_btn_deleteall);


        btnNum[0] = (Button) findViewById(R.id.btn_num0);
        btnNum[1] = (Button) findViewById(R.id.btn_num1);
        btnNum[2] = (Button) findViewById(R.id.btn_num2);
        btnNum[3] = (Button) findViewById(R.id.btn_num3);
        btnNum[4] = (Button) findViewById(R.id.btn_num4);
        btnNum[5] = (Button) findViewById(R.id.btn_num5);
        btnNum[6] = (Button) findViewById(R.id.btn_num6);
        btnNum[7] = (Button) findViewById(R.id.btn_num7);
        btnNum[8] = (Button) findViewById(R.id.btn_num8);
        btnNum[9] = (Button) findViewById(R.id.btn_num9);

    }

    class SelectFitt extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            String uri = params[0];
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder(); //담을수 있는 틀

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));   //연결해주고


                String json;
                while ((json = bufferedReader.readLine()) != null) {   //여러줄의 제이슨을 넣어줌
                    sb.append(json + "\n"); //붙여주고
                }
                Log.e("sb", sb.toString());
                return sb.toString().trim();    //빈칸 없애고

            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        protected void onPostExecute(String result) {
            //if(!valueClass.RFID_no.equals("00000000")) RFID_READ_FLAG = true;
            try {
                JSONObject jsonObj = new JSONObject(result);
                f_seq = String.valueOf(jsonObj.getInt("seq"));
                f_name = jsonObj.getString("name");  //이부분 고쳐라
                if(jsonObj.getInt("gender") == 0) {
                    f_gender = "Male";
                } else {
                    f_gender = "Female";
                }
                f_birth = jsonObj.getString("birth");
                f_height = jsonObj.getString("height");
                f_weight = jsonObj.getString("weight");
                f_phone_org = jsonObj.getString("phoneNum");
                f_phone  = (jsonObj.getString("phoneNum")).replaceAll("-", "");
                f_email = jsonObj.getString("email");
                //seq_Exist.execute("http://211.253.30.245/php/TEST/seq_EXIST.php?seq=" + f_seq);   //211124
                Log.e("fitt", f_seq + "  " + f_name + "  " + f_gender + "  " + f_birth + "  " + f_height + "  " + f_weight + "  " + f_phone);
                GetData("http://211.253.30.245/equipment/kor/common/Login.php?txt_password=" + txtPass.getText().toString());   //211124
            } catch (JSONException e) {
                GetData("http://211.253.30.245/equipment/kor/common/Login.php?txt_password=" + txtPass.getText().toString());
                Log.e("fitt", e.getMessage());
            }

            //seq확인해라

            //int array[] = new int[3];  //배열 만듦
            //array[0] = 1;   //값 대충 넣음
            //array[1] = 2;   //값 대충 넣음

            //Log.e("myJSON", String.valueOf(array instanceof int[]));    //int 배열인지 확인하는 코드 이걸로 나중에 배열 들어오면 걸러내라
        }
    }
/*  //211124
    class Seq_Exist extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            String uri = params[0];
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder(); //담을수 있는 틀

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));   //연결해주고


                String json;
                while ((json = bufferedReader.readLine()) != null) {   //여러줄의 제이슨을 넣어줌
                    sb.append(json + "\n"); //붙여주고
                }
                Log.e("sb", sb.toString());
                return sb.toString().trim();    //빈칸 없애고

            } catch (Exception e) {
                Log.e("sjfiejfkdlf", e.getMessage());
                return new String("Exception: " + e.getMessage());
            }

        }

        protected void onPostExecute(String result) {



            //if(!valueClass.RFID_no.equals("00000000")) RFID_READ_FLAG = true;
            //int array[] = new int[3];  //배열 만듦
            //array[0] = 1;   //값 대충 넣음
            //array[1] = 2;   //값 대충 넣음

            //Log.e("myJSON", String.valueOf(array instanceof int[]));    //int 배열인지 확인하는 코드 이걸로 나중에 배열 들어오면 걸러내라
            try {
                JSONArray jsonObj = new JSONArray(result);

                String seq_exist = jsonObj.getJSONObject(0).getString("SUCCESS");
                //prescribe2 = jsonObj.getJSONArray("bicycle");
                //is_bike = prescribe2.getString(0);
                if(seq_exist.equals("0")) {
                    makeMember.execute("http://211.253.30.245/php/TEST/register_TEST_ESE.php?userid=" + f_email + "&second_id=" + f_seq + "&user_name=" + f_name + "&password=" + f_phone + "&rfid=" + "" + "&email=" + f_email + "&phone=" + f_phone + "&birthday=" + f_birth + "&gender=" + f_gender + "&keypad=" + f_phone + "&height=" + f_height + "&weight=" + f_weight + "&clubs_id=" + "1121" + "&is_expert=" + "0");

                } else {
                    Log.e("seq", "okok");
                    GetData("http://211.253.30.245/equipment/kor/common/Login.php?txt_password=" + txtPass.getText().toString());
                    //비교해서 수정하는거 넣기

                }
//여기 조져라 바이크 하고 트레드밀도 해라 그럼 월요일에 끝내자 타이머 안에 종료되었을때 내부 저장하고 돌리면될듯
            } catch (JSONException e) {
                Log.i("????", e.toString());

            }
        }
    }

    class MakeMember extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            String uri = params[0];
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder(); //담을수 있는 틀

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));   //연결해주고


                String json;
                while ((json = bufferedReader.readLine()) != null) {   //여러줄의 제이슨을 넣어줌
                    sb.append(json + "\n"); //붙여주고
                }
                Log.e("sb", sb.toString());
                return sb.toString().trim();    //빈칸 없애고

            } catch (Exception e) {
                Log.e("make fail", e.getMessage());
                return new String("Exception: " + e.getMessage());
            }

        }

        protected void onPostExecute(String result) {
            Log.e("make ok", result);
            try {
                JSONObject jsonObj = new JSONObject(result);
                if(new JSONArray(jsonObj.getString("result")).getJSONObject(0).getString("is_success").equals("1")) {
                    Log.e("make ok", result);
                    GetData("http://211.253.30.245/equipment/kor/common/Login.php?txt_password=" + txtPass.getText().toString());
                } else {
                    Log.e("make fail2", "fail");
                }
            } catch(JSONException e) {
                ;
            }
            //if(!valueClass.RFID_no.equals("00000000")) RFID_READ_FLAG = true;

            //int array[] = new int[3];  //배열 만듦
            //array[0] = 1;   //값 대충 넣음
            //array[1] = 2;   //값 대충 넣음

            //Log.e("myJSON", String.valueOf(array instanceof int[]));    //int 배열인지 확인하는 코드 이걸로 나중에 배열 들어오면 걸러내라
        }
    }

    class ChangeData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            String uri = params[0];
            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder(); //담을수 있는 틀

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));   //연결해주고


                String json;
                while ((json = bufferedReader.readLine()) != null) {   //여러줄의 제이슨을 넣어줌
                    sb.append(json + "\n"); //붙여주고
                }
                Log.e("sb", sb.toString());
                return sb.toString().trim();    //빈칸 없애고

            } catch (Exception e) {
                Log.e("make fail", e.getMessage());
                return new String("Exception: " + e.getMessage());
            }

        }

        protected void onPostExecute(String result) {

            //if(!valueClass.RFID_no.equals("00000000")) RFID_READ_FLAG = true;

            //int array[] = new int[3];  //배열 만듦
            //array[0] = 1;   //값 대충 넣음
            //array[1] = 2;   //값 대충 넣음

            //Log.e("myJSON", String.valueOf(array instanceof int[]));    //int 배열인지 확인하는 코드 이걸로 나중에 배열 들어오면 걸러내라
        }
    }
*/

    private void GetData(final String str) {
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
                if (str.contains("password")) {
                    inputIntent();
                } else {
                    inputValues();
                }

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
            if(PublicValues.configValues.get(3).equals("1121")) {
                PublicValues.userSecondId = jsonObj.getString(PublicValues.USER_SECONDID);
            }
            PublicValues.userName = jsonObj.getString(TAG_USERNAME);
            PublicValues.userGender = jsonObj.getString(TAG_GENDER);
            PublicValues.userHeight = jsonObj.getString(TAG_HEIGHT);
            PublicValues.userWeight = jsonObj.getString(TAG_WEIGHT);
            PublicValues.userBirth = jsonObj.getString("birthday");
            PublicValues.userClub = jsonObj.getString("clubs_id");
            PublicValues.location = jsonObj.getString(PublicValues.LOCATION);
            PublicValues.director = jsonObj.getString(TAG_DIRECTOR);
            if(PublicValues.configValues.get(3).equals("1121")) {
                try {
                    if (Math.round(Double.parseDouble(f_height)) != Math.round(Double.parseDouble(PublicValues.userHeight)) || Math.round(Double.parseDouble(f_weight)) != Math.round(Double.parseDouble(PublicValues.userWeight)) || !f_phone.equals(jsonObj.getString("phone"))) {
                        Log.e("hsfwioejfsdkf", f_height + "-" + PublicValues.userHeight.substring(0, PublicValues.userHeight.indexOf(".")) + "   " + f_weight + "-" + PublicValues.userWeight.substring(0, PublicValues.userWeight.indexOf(".")) + "   " + f_phone + "-" + jsonObj.getString("phone"));
                        PublicValues.userHeight = f_height;
                        PublicValues.userWeight = f_weight;

                        //데이터 바꾸는거
                        //changeData.execute("http://211.253.30.245/php/TEST/changeData_TEST.php?userid=" + f_email + "&phone=" + f_phone + "&height=" + f_height + "&weight=" + f_weight); //211124
                    }
                } catch (Exception e) {
                    ;
                }
            }
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
                if (where.contains("MeasureTestActivity"))
                    intent = new Intent(MeasureLoginPadActivity.this, MeasureTestActivity.class);
                else
                    intent = new Intent(MeasureLoginPadActivity.this, MeasureLoginPadActivity.class);


                //height = null;  /////////////////////////TEST
                //weight = null;



                if (PublicValues.userHeight == null || PublicValues.userHeight.length() < 1) {
                    insertH();

                } else if (PublicValues.userWeight == null || PublicValues.userWeight.length() < 1) {
                    insertW();
                }

                if ((PublicValues.userHeight != null && PublicValues.userHeight.length() > 0) && (PublicValues.userWeight != null && PublicValues.userWeight.length() > 0)) {

                    if (where.contains("MeasureTestActivity")) {
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        GetData("http://211.253.30.245/backend/index.php/MiniplusTest/getTestData?users_id=" + PublicValues.userId);
                    }

                }
                prevent = false;
            } else {
                Toast.makeText(context, "비밀번호를 확인해 주세요.", Toast.LENGTH_LONG).show();
                txtPass.setText(null);
                prevent = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "비밀번호를 확인해 주세요.", Toast.LENGTH_LONG).show();
            txtPass.setText(null);
            prevent = false;
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
                        if (where.contains("MeasureTestActivity")) {
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            GetData("http://211.253.30.245/backend/index.php/MiniplusTest/getTestData?users_id=" + PublicValues.userId);
                        }
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


                    if (where.contains("MeasureTestActivity")) {
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        GetData("http://211.253.30.245/backend/index.php/MiniplusTest/getTestData?users_id=" + PublicValues.userId);
                    }
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


    protected void inputValues() {

        try {
            JSONObject jsonObj = new JSONObject(new JSONObject(myJSON).getString("result"));

            Log.e(TAG, "Json" + jsonObj);

            JSONObject jsonObjDet;

            long now = System.currentTimeMillis();

            Date date = new Date(now);
            SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd");

            String time_now = now_time.format(date);


            Intent intent = new Intent(MeasureLoginPadActivity.this, MeasureResultActivity.class);


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
                    values[0] = StringParseUnder2(jsonObjDet.getString("avg_power"));
                    values[1] = StringParseUnder2(jsonObjDet.getString("max_power"));
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
                    values[0] = StringParseUnder2(jsonObjDet.getString("avg_power"));
                    values[1] = StringParseUnder2(jsonObjDet.getString("max_power"));
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


            finishAffinity();
            //}


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Json Error!!!");
        }
    }

    String StringParseUnder2(String str) {
        return str.substring(0, str.lastIndexOf(".") + 4);
    }

}
