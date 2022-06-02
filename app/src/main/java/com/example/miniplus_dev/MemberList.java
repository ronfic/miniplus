package com.example.miniplus_dev;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MemberList extends Activity {

    private final String TAG_USERNAME = "username";
    private final String TAG_USERID = "userid";
    private final String TAG_PASSWORD = "pw";
    private final String TAG_LOCATION = "location";
    private final String TAG_RESULTS = "result";

    String myJSON;

    JSONArray peoples = null;
    Context context;
    ArrayList<HashMap<String, String>> personList;
    ListAdapter listadapter;
    ListView memberlist;

    private ListView mListview = null;

    String user_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        personList = new ArrayList<HashMap<String, String>>();
        memberlist = (ListView) findViewById(R.id.memeberlist);

        Intent intent = getIntent();
        user_location = intent.getStringExtra("user_location");

        getDbData("http://211.253.30.245/php/miniplus_userList.php?location=" + user_location);

        Button BackBtn = (Button) findViewById(R.id.backbtn);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getDbData(String string) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                BufferedReader bufferedReader = null;

                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb1 = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb1.append(json + "\n");
                    }
                    return sb1.toString().trim();

                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }

            }

            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }

        GetDataJSON g = new GetDataJSON();
        g.execute(string);

    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < peoples.length(); i++) {
                JSONObject c = peoples.getJSONObject(i);
                String username = c.getString(TAG_USERNAME);
                String userid = c.getString(TAG_USERID);
                String password = c.getString(TAG_PASSWORD);
                String location = c.getString(TAG_LOCATION);

                HashMap<String, String> persons = new HashMap<String, String>();
                persons.clear();
                persons.put(TAG_USERNAME, username);
                persons.put(TAG_USERID, userid);
                persons.put(TAG_PASSWORD, password);
                persons.put(TAG_LOCATION, location);

                personList.add(persons);
            }

            // 어댑터 생성, R.layout.list_item : Layout ID
            listadapter = new SimpleAdapter(
                    this, personList, R.layout.list_item,
                    new String[]{TAG_USERNAME, TAG_USERID, TAG_PASSWORD, TAG_LOCATION},
                    new int[]{R.id.txt_userName, R.id.txt_userid, R.id.txt_password, R.id.txt_location}
            );

            memberlist.setAdapter(listadapter); // ListView 에 어댑터 설정


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
