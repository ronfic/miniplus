package com.example.miniplus_dev.setting;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniplus_dev.InputNumberDialog;
import com.example.miniplus_dev.PublicValues;
import com.example.miniplus_dev.R;

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
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginPadFragment extends Fragment {

    private String TAG = "LoginPad";

    View view;

    InputNumberDialog inputNumberDialog = new InputNumberDialog(this.getContext());
    OnTimePickerSetListener onTimePickerSetListener;

    //Seq_Exist seq_Exist;  //211124
    SelectFitt selectFitt;
    //MakeMember makeMember;    //211124
    //ChangeData changeData;    //211124

    String myJSON;

    TextView txt_passBox;

    Button btn_canel;
    Button btn_enter;
    Button btn_exit;


    Button btn_num0;
    Button btn_num1;
    Button btn_num2;
    Button btn_num3;
    Button btn_num4;
    Button btn_num5;
    Button btn_num6;
    Button btn_num7;
    Button btn_num8;
    Button btn_num9;

    String f_seq;
    String f_name;
    String f_gender;
    String f_birth;
    String f_height;
    String f_weight;
    String f_phone;
    String f_phone_org;
    String f_email;

    boolean prevent = false;

    public interface OnTimePickerSetListener {
        void onTimePickerSet(boolean LogonFlag);
        void DoinBackLoading(boolean LoadinFlag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        view = inflater.inflate(R.layout.fragment_loginpad, container, false);

        //seq_Exist = new Seq_Exist();  //211124
        //makeMember = new MakeMember();    //211124
        //changeData = new ChangeData();    //211124

        txt_passBox = view.findViewById(R.id.password_box);

        btn_canel   = view.findViewById(R.id.btn_cancel);
        btn_enter   = view.findViewById(R.id.btn_enter);
        btn_exit    = view.findViewById(R.id.btn_exit);

        btn_num0    = view.findViewById(R.id.btn_num0);
        btn_num1    = view.findViewById(R.id.btn_num1);
        btn_num2    = view.findViewById(R.id.btn_num2);
        btn_num3    = view.findViewById(R.id.btn_num3);
        btn_num4    = view.findViewById(R.id.btn_num4);
        btn_num5    = view.findViewById(R.id.btn_num5);
        btn_num6    = view.findViewById(R.id.btn_num6);
        btn_num7    = view.findViewById(R.id.btn_num7);
        btn_num8    = view.findViewById(R.id.btn_num8);
        btn_num9    = view.findViewById(R.id.btn_num9);

        prevent = false;

        view.setClickable(true);

        onTimePickerSetListener = (OnTimePickerSetListener) getActivity();

        btn_num0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txt_passBox.length() < 11) {
                        txt_passBox.append("0");
                    }
                }
            }
        });

        btn_num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txt_passBox.length() < 11) {
                        txt_passBox.append("1");
                    }
                }
            }
        });

        btn_num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txt_passBox.length() < 11) {
                        txt_passBox.append("2");
                    }
                }
            }
        });

        btn_num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txt_passBox.length() < 11) {
                        txt_passBox.append("3");
                    }
                }
            }
        });

        btn_num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txt_passBox.length() < 11) {
                        txt_passBox.append("4");
                    }
                }
            }
        });

        btn_num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txt_passBox.length() < 11) {
                        txt_passBox.append("5");
                    }
                }
            }
        });

        btn_num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txt_passBox.length() < 11) {
                        txt_passBox.append("6");
                    }
                }
            }
        });

        btn_num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txt_passBox.length() < 11) {
                        txt_passBox.append("7");
                    }
                }
            }
        });

        btn_num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txt_passBox.length() < 11) {
                        txt_passBox.append("8");
                    }
                }
            }
        });

        btn_num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    if (txt_passBox.length() < 11) {
                        txt_passBox.append("9");
                    }
                }
            }
        });

        btn_canel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    String tmp = txt_passBox.getText().toString();
                    if (tmp.length() > 0) txt_passBox.setText(tmp.substring(0, tmp.length() - 1));
                }
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!prevent) {
                    prevent = true;
                    PublicValues.MeasureResultFlag = false;
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().remove(LoginPadFragment.this).commit();
                    fragmentManager.popBackStack();
                    onTimePickerSetListener.DoinBackLoading(false);
                }
            }
        });

        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!prevent) {
                    prevent = true;
                    Log.e("버튼막음", "막음");
                    PublicValues.MeasureResultFlag = true;
                    String password = txt_passBox.getText().toString();
                    if (PublicValues.configValues.get(3).equals("1121")) {
                        selectFitt = new SelectFitt();
                        selectFitt.execute("http://211.253.30.245/php/fitt/fitt_getMember2.php?phoneNum=" + password);  //211124
                    } else {
                        GetData("http://211.253.30.245/equipment/kor/common/Login.php?txt_password=" + password);
                    }
                    Log.e(TAG, "로그인버튼 PASSWORD : " + password);
                }
            }
        });

        onTimePickerSetListener.DoinBackLoading(true);
        return view;
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
                GetData("http://211.253.30.245/equipment/kor/common/Login.php?txt_password=" + txt_passBox.getText().toString());   //211124
            } catch (JSONException e) {
                GetData("http://211.253.30.245/equipment/kor/common/Login.php?txt_password=" + txt_passBox.getText().toString());
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
                    GetData("http://211.253.30.245/equipment/kor/common/Login.php?txt_password=" + txt_passBox.getText().toString());
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
                    GetData("http://211.253.30.245/equipment/kor/common/Login.php?txt_password=" + txt_passBox.getText().toString());
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
*/
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
                    return "Exception: " + e.getMessage();
                }
            }

            protected void onPostExecute(String result) {
                myJSON = result;
                inputIntent();
                onTimePickerSetListener.DoinBackLoading(false);
            }
        }
        GetDataJSON json = new GetDataJSON();
        json.execute(str);
    }



    protected void inputIntent() {

        try {
            JSONObject jsonObj = new JSONObject(new JSONObject(myJSON).getString(PublicValues.RESULTS));

            PublicValues.userId = jsonObj.getString(PublicValues.USER_ID);
            if(PublicValues.configValues.get(3).equals("1121")) {
                PublicValues.userSecondId = jsonObj.getString(PublicValues.USER_SECONDID);
            }
            PublicValues.userName = jsonObj.getString(PublicValues.USER_NAME);
            PublicValues.userGender = jsonObj.getString(PublicValues.USER_GENDER);
            PublicValues.userHeight = jsonObj.getString(PublicValues.USER_HEIGHT);
            PublicValues.userWeight = jsonObj.getString(PublicValues.USER_WEIGHT);
            PublicValues.userBirth = jsonObj.getString(PublicValues.USER_BIRTHDAY);
            PublicValues.location = jsonObj.getString(PublicValues.LOCATION);
            PublicValues.director = jsonObj.getString(PublicValues.DIRECTOR);
            PublicValues.userClub = jsonObj.getString("clubs_id");

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

            //////////////////////////////////////////////////////////////////////////////////////210621
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd");
            String time_now = now_time.format(date);


            if (PublicValues.userBirth != null) {
                try{
                    PublicValues.userAge = Integer.valueOf(time_now.substring(0, 4)) - Integer.valueOf(PublicValues.userBirth.substring(0, 4)) + 1;
                }catch (Exception e){
                    Log.e(TAG, "input Age Err : " + e);
                }

            }
            /////////////////////////////////////////////////////////////////////////////////////
            if (PublicValues.userId != null && PublicValues.userId.length() > 0) {
                Log.i(TAG, "LOGIN Success");
                /*  //원래
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat now_time = new SimpleDateFormat("yyyy-MM-dd");
                String time_now = now_time.format(date);
                */
                updateLogin("http://211.253.30.245/php/android_update_login_workdate.php?id=" + PublicValues.userId + "&work_date=" + time_now);
                if (PublicValues.userHeight == null || PublicValues.userHeight.length() < 1) {
                    insertH();

                } else if (PublicValues.userWeight == null || PublicValues.userWeight.length() < 1) {
                    insertW();
                }

                if ((PublicValues.userHeight != null && PublicValues.userHeight.length() > 0) && (PublicValues.userWeight != null && PublicValues.userWeight.length() > 0)) {
                    super.onResume();
                    FragmentManager fragmentManager = getFragmentManager();
                    if (fragmentManager != null) {
                        fragmentManager.beginTransaction().remove(LoginPadFragment.this).commit();
                        fragmentManager.popBackStack();
                    }


                    //fragmentManager.beginTransaction().commit();
                }
                prevent = false;
                onTimePickerSetListener.onTimePickerSet(true);

            } else {
                try {

                    Toast.makeText(super.getContext(), "비밀번호를 확인해 주세요.", Toast.LENGTH_LONG).show();
                    txt_passBox.setText(null);
                    prevent = false;
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                Toast.makeText(super.getContext(), "비밀번호를 확인해 주세요.", Toast.LENGTH_LONG).show();
                txt_passBox.setText(null);
                prevent = false;
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void insertH() {
        inputNumberDialog.initDialogStart(false);
        inputNumberDialog.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = inputNumberDialog.inputNum.getText().toString().trim();

                if (str.charAt(0) == '0') {
                    Log.e("success", "0 input");
                    inputNumberDialog.inputNum.setText(null);
                    inputNumberDialog.inputNum.setHint("잘못 된 입력입니다.");
                    inputNumberDialog.inputNum.setHintTextColor(Color.RED);
                } else {
                    PublicValues.userHeight = str;
                    Log.e("success", "height : " + PublicValues.userHeight);

                    if (PublicValues.userWeight == null || PublicValues.userWeight.length() < 1) {
                        inputNumberDialog.initDialogStop();
                        insertW();
                    } else {
                        insertBodyInfoData bodydata = new insertBodyInfoData();
                        bodydata.execute();
                        inputNumberDialog.initDialogStop();
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().remove(LoginPadFragment.this).commit();
                        fragmentManager.popBackStack();
                        fragmentManager.beginTransaction().commit();
                    }
                }
            }
        });
    }


    private void insertW() {
        inputNumberDialog.initDialogStart(true);
        inputNumberDialog.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = inputNumberDialog.inputNum.getText().toString().trim();

                if (str.charAt(0) == '0') {
                    Log.e("success", "0 input");
                    inputNumberDialog.inputNum.setText(null);
                    inputNumberDialog.inputNum.setHint("잘못 된 입력입니다.");
                    inputNumberDialog.inputNum.setHintTextColor(Color.RED);
                } else {
                    PublicValues.userWeight = str;
                    Log.e("success", "weight : " + PublicValues.userWeight);
                    insertBodyInfoData bodydata = new insertBodyInfoData();
                    bodydata.execute();
                    inputNumberDialog.initDialogStop();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().remove(LoginPadFragment.this).commit();
                    fragmentManager.popBackStack();
                    LoginPadFragment.super.onResume();
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
                    "&height=" + PublicValues.userWeight +
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
                outs.write(param.getBytes(StandardCharsets.UTF_8));
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
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
/*  //211124
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
    private void updateLogin(String str) {
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
                    Log.e(TAG, "Log update : " + sb.toString().trim());
                    return sb.toString().trim();

                } catch (Exception e) {
                    Log.e(TAG, "update Date Err : " + e);
                    return "Exception: " + e.getMessage();
                }
            }

            protected void onPostExecute(String result) {
            }
        }
        GetDataJSON json = new GetDataJSON();
        json.execute(str);
    }

}
