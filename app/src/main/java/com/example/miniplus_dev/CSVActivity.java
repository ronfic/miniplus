package com.example.miniplus_dev;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import org.apache.poi.hssf.usermodel.HSSFRow;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFScatterChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;


import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
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
import javax.mail.internet.MimeUtility;

import static com.example.miniplus_dev.PublicValues.lastVersionInfo;

public class CSVActivity extends Activity {

    //CalendarView calendarView;
    FrameLayout frame;
    com.prolificinteractive.materialcalendarview.MaterialCalendarView materialCalendar;
    Spinner spinner;
    Button btn1, btn2;
    boolean  failFlag = false;
    TextView phase1, phase2, phase3, phase4, phase5;
    EditText edtxt;

    ProgressDialog progressDialog = new ProgressDialog(this);
    ProgressDialog2 progressDialog2 = new ProgressDialog2(this);

    int caldate_y, caldate_m, caldate_d;

    String selectID;
    String selectName;
    String clubName;
    ArrayAdapter<String> mArrayadapter_member;
    ArrayList<String> item = new ArrayList<>();

    List<String> strDateList = new ArrayList<>();
    ArrayList<String> strDoWorkList = new ArrayList<>();

    String myJSON_sets;
    String myJSON_meas;
    ArrayList<ArrayList> Data_sets = new ArrayList<>();
    ArrayList<JSONObject> miniList = new ArrayList<>();
    ArrayList<JSONObject> Data_reps_user = new ArrayList<>();
    ArrayList<JSONObject> measList = new ArrayList<>();

    String myJSON_reps;
    ArrayList<JSONObject> Data_reps = new ArrayList<>();

    boolean oneDay, twoDay, mailFail = false;

    TodayDecorator todayDecorator;
    SundayDecorator sundayDecorator;
    SaturdayDecorator saturdayDecorator;
    MinMaxDecorator minMaxDecorator;
    BoldDecorator boldDecorator;
    EventDecorator eventDecorator;
    EventsDecorator eventsDecorator;
    NOEventsDecorator noEventsDecorator;
    NOEventDecoratorAll noEventDecoratorAll;

    MemberTodayDecorator memberTodayDecorator;
    DoWorkDecorator doWorkDecorator;
    DontWorkDecorator dontWorkDecorator;
    NoWorkDecorator noWorkDecorator;

    int progressMax = 0;
    int progressPer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csvactivity);

        hideSystemUI();

        frame = findViewById(R.id.frame);
        frame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeypad();
                hideSystemUI();
                return false;
            }
        });
        materialCalendar = findViewById(R.id.materialCalendar);
        Calendar calendar = Calendar.getInstance();
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String caldate = df.format(date);
        caldate_y = Integer.parseInt(caldate.substring(0, 4));
        caldate_m = Integer.parseInt(caldate.substring(5, 7));
        caldate_d = Integer.parseInt(caldate.substring(8, 10));
        Log.e("이거 확인", caldate_y + " " + caldate_m + " " + caldate_d);
        materialCalendar.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                //.setMinimumDate(CalendarDay.from(2022, 2, 7))
                //.setMaximumDate(CalendarDay.from(caldate_y, caldate_m, caldate_d))
                .setCalendarDisplayMode(CalendarMode.MONTHS).commit();

        materialCalendar.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
        materialCalendar.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months))); //타이틀에서 월만 바꿈ㅋㅋ
        //materialCalendar.setTitleFormatter(new DateFormatTitleFormatter(new SimpleDateFormat("yyyy년 M월")));
        materialCalendar.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));

        Log.e("오늘", String.format("%04d" ,CalendarDay.today().getYear()) + "-" + String.format("%02d" ,(CalendarDay.today().getMonth() + 1)) + "-" + String.format("%02d" ,CalendarDay.today().getDay()));

        todayDecorator = new TodayDecorator(getApplicationContext());
        sundayDecorator = new SundayDecorator();
        saturdayDecorator = new SaturdayDecorator();
        minMaxDecorator = new MinMaxDecorator(caldate_y, caldate_m, caldate_d, 2022, 2, 7);
        boldDecorator = new BoldDecorator(caldate_y, caldate_m, caldate_d, 2022, 2, 7);
        //minMaxDecorator = new MinMaxDecorator(caldate_y, caldate_m, caldate_d, 2021, 12, 7);
        //boldDecorator = new BoldDecorator(caldate_y, caldate_m, caldate_d, 2021, 12, 7);
        //MinMaxDecorator minMaxDecorator = new MinMaxDecorator(2023, 4, 29, 2022, 3, 7);
        //BoldDecorator boldDecorator = new BoldDecorator(2023, 4, 29, 2022, 3, 7);
        //materialCalendar.addDecorators(todayDecorator, sundayDecorator, saturdayDecorator,minMaxDecorator, boldDecorator);
        materialCalendar.addDecorators(minMaxDecorator, boldDecorator);

        clubName = GetClubName("http://211.253.30.245/equipment/kor/common/CLUB_SEL.php?clubs_id=" + PublicValues.userClub);

        materialCalendar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeypad();
                hideSystemUI();
                return false;
            }
        });

        materialCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //Log.e("select_date", date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDay());
                hideKeypad();
                hideSystemUI();

                if(selected) {
                    oneDay = true;
                    twoDay = false;
                    strDateList.removeAll(strDateList);
                    strDateList.add(String.format("%04d" ,date.getYear()) + "-" + String.format("%02d" ,(date.getMonth() + 1)) + "-" + String.format("%02d" ,date.getDay()));
                    phase2.setTextColor(getColor(R.color.blue_overlay));
                    phase4.setTextColor(getColor(R.color.black_overlay));
                    phase5.setTextColor(getColor(R.color.black_overlay));

                    eventDecorator = new EventDecorator(getApplicationContext(), date);
                    noEventsDecorator = new NOEventsDecorator(getApplicationContext(), date, caldate_y, caldate_m, caldate_d, 2022, 2, 7);
                    materialCalendar.addDecorators(eventDecorator, noEventsDecorator);
                    Log.e("선택1", strDateList.get(0) + "   " + strDateList.size() + "   " + oneDay + "   " + twoDay);
                } else {
                    oneDay = false;
                    twoDay = false;
                    strDateList.removeAll(strDateList);
                    phase2.setTextColor(getColor(R.color.red_overlay));
                    phase4.setTextColor(getColor(R.color.black_overlay));
                    phase5.setTextColor(getColor(R.color.black_overlay));

                    noEventDecoratorAll = new NOEventDecoratorAll(getApplicationContext(), caldate_y, caldate_m, caldate_d, 2022, 2, 7);
                    materialCalendar.addDecorator(noEventDecoratorAll);
                    Log.e("선택1", strDateList.size() + "   " + oneDay + "   " + twoDay);
                }
            }
        });

        materialCalendar.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                hideKeypad();
                hideSystemUI();
                strDateList.removeAll(strDateList);
                eventsDecorator = new EventsDecorator(getApplicationContext(), dates);
                materialCalendar.addDecorators(eventsDecorator);
                if(dates.get(0).isBefore(dates.get(1))) {
                    strDateList.add(String.format("%04d" ,dates.get(0).getYear()) + "-" + String.format("%02d" ,(dates.get(0).getMonth() + 1)) + "-" + String.format("%02d" ,dates.get(0).getDay()));
                    strDateList.add(String.format("%04d" ,dates.get(dates.size() - 1).getYear()) + "-" + String.format("%02d" ,(dates.get(dates.size() - 1).getMonth() + 1)) + "-" + String.format("%02d" ,dates.get(dates.size() - 1).getDay()));

                } else {
                    strDateList.add(String.format("%04d" ,dates.get(dates.size() - 1).getYear()) + "-" + String.format("%02d" ,(dates.get(dates.size() - 1).getMonth() + 1)) + "-" + String.format("%02d" ,dates.get(dates.size() - 1).getDay()));
                    strDateList.add(String.format("%04d" ,dates.get(0).getYear()) + "-" + String.format("%02d" ,(dates.get(0).getMonth() + 1)) + "-" + String.format("%02d" ,dates.get(0).getDay()));
                }

                oneDay = false;
                twoDay = true;
                phase2.setTextColor(getColor(R.color.blue_overlay));
                phase4.setTextColor(getColor(R.color.black_overlay));
                phase5.setTextColor(getColor(R.color.black_overlay));
                Log.e("선택2", strDateList.get(0) + "   " + strDateList.get(1) + "   " + strDateList.size() + "   " + oneDay + "   " + twoDay);
            }
        });

        btn1 = findViewById(R.id.btn1);

        btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeypad();
                hideSystemUI();
                return false;
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CSVActivity.this.isFinishing()) {
                    progressPer = 0;
                    progressMax = 0;
                    //progressDialog2.DialogStart();
                    progressDialog.DialogStart();
                }
                phase4.setTextColor(getColor(R.color.black_overlay));
                phase5.setTextColor(getColor(R.color.black_overlay));
                /*
                if(edtxt.getText().toString().length() == 0) {
                    if (!CSVActivity.this.isFinishing()) {
                        //progressDialog2.DialogStop();
                        progressDialog.DialogStop();
                    }
                    Toast.makeText(getApplicationContext(), "이메일을 적어주세요.", Toast.LENGTH_LONG).show();
                } else if (!oneDay && !twoDay){
                    if (!CSVActivity.this.isFinishing()) {
                        //progressDialog2.DialogStop();
                        progressDialog.DialogStop();
                    }
                    Toast.makeText(getApplicationContext(), "날짜를 선택해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    if(oneDay)
                        GetData_reps_user("http://211.253.30.245/equipment/kor/miniplus/Miniplus_SetData_SEL3.php?clubs_id=" + PublicValues.userClub + "&workout_datetime1=" + strDateList.get(0).toString().trim() + "&workout_datetime2=" + strDateList.get(0).toString().trim());
                    else if(twoDay)
                        GetData_reps_user("http://211.253.30.245/equipment/kor/miniplus/Miniplus_SetData_SEL3.php?clubs_id=" + PublicValues.userClub + "&workout_datetime1=" + strDateList.get(0).toString().trim() + "&workout_datetime2=" + strDateList.get(1).toString().trim());
                    //u_miniplus_reps에서 miniplus_id를 중복제거해서 가져오지 그후에 u_miniplus에서 id로 검색해서 minilist를 완성시키지
                }
                */

                if (!oneDay && !twoDay){
                    if (!CSVActivity.this.isFinishing()) {
                        //progressDialog2.DialogStop();
                        progressDialog.DialogStop();
                    }
                    Toast.makeText(getApplicationContext(), "날짜를 선택해주세요.", Toast.LENGTH_LONG).show();
                } else {

                    if(edtxt.getText().length() == 0) {
                        Log.e("메일 보내기", PublicValues.configValues.get(4));
                        edtxt.setText(PublicValues.configValues.get(4).toString());

                    }
                    if(oneDay)
                        GetData_reps_user("http://211.253.30.245/equipment/kor/miniplus/Miniplus_SetData_SEL3.php?clubs_id=" + PublicValues.userClub + "&workout_datetime1=" + strDateList.get(0).toString().trim() + "&workout_datetime2=" + strDateList.get(0).toString().trim());
                    else if(twoDay)
                        GetData_reps_user("http://211.253.30.245/equipment/kor/miniplus/Miniplus_SetData_SEL3.php?clubs_id=" + PublicValues.userClub + "&workout_datetime1=" + strDateList.get(0).toString().trim() + "&workout_datetime2=" + strDateList.get(1).toString().trim());
                    //u_miniplus_reps에서 miniplus_id를 중복제거해서 가져오지 그후에 u_miniplus에서 id로 검색해서 minilist를 완성시키지
                }
            }
        });

        btn2 = findViewById(R.id.btn2);
        btn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeypad();
                return false;
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CSVActivity.this, ExerciseActivity.class).setAction(Intent.ACTION_MAIN) .addCategory(Intent.CATEGORY_LAUNCHER) .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            }
        });

        phase1 = findViewById(R.id.phase1);
        phase1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeypad();
                hideSystemUI();
                return false;
            }
        });

        phase2 = findViewById(R.id.phase2);
        phase2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeypad();
                hideSystemUI();
                return false;
            }
        });

        phase3 = findViewById(R.id.phase3);
        phase3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeypad();
                hideSystemUI();
                return false;
            }
        });

        phase4 = findViewById(R.id.phase4);
        phase4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeypad();
                hideSystemUI();
                return false;
            }
        });
        phase5 = findViewById(R.id.phase5);
        phase5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeypad();
                hideSystemUI();
                return false;
            }
        });

        int cy = calendar.get(Calendar.YEAR);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        edtxt = findViewById(R.id.edtxt);
        edtxt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //edtxt.setInputType(InputType.TYPE_CLASS_TEXT);
        edtxt.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        edtxt.setHint(PublicValues.configValues.get(4));
        edtxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    if(edtxt.isFocused()) {
                        edtxt.clearFocus();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideKeypad();
                                hideSystemUI();
                            }
                        },100);

                    }
                }
                return false;
            }
        });
        edtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0) {
                    phase3.setTextColor(getColor(R.color.red_overlay));
                    edtxt.setHint(PublicValues.configValues.get(4));
                } else {
                    phase3.setTextColor(getColor(R.color.blue_overlay));
                }
                phase4.setTextColor(getColor(R.color.black_overlay));
                phase5.setTextColor(getColor(R.color.black_overlay));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e("포커스", hasFocus + "");
                if (hasFocus) {

                }
            }
        });

        //calendarView = findViewById(R.id.calendarView);

        //calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));    //월말
        //calendarView.setMaxDate(System.currentTimeMillis());


        spinner = findViewById(R.id.spinner);
        mArrayadapter_member = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, item);

        //GetMemberList("http://211.253.30.245/equipment/kor/common/SameClubID_SEL.php?clubs_id=" + PublicValues.club_id);
        GetDoWorkDate("http://211.253.30.245/equipment/kor/miniplus/MiniplusSetDate_SEL2.php?clubs_id=" + PublicValues.userClub);
    }

    private void GetMemberList(String str) {
        class GetDataJSON extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
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
                    return sb.toString().trim();

                } catch (Exception e) {
                    return "Exception: " + e.getMessage();
                }
            }

            protected void onPostExecute(String result) {
                //myJSON = result;
                createMemberList(result);
            }
        }
        GetDataJSON json = new GetDataJSON();
        json.execute(str);
    }

    public void createMemberList(String result) {
        try {
            JSONArray jsonArr = new JSONArray(new JSONObject(result).getString("result"));
            item.add("---------------------------------------------------------------------");
            for (int i = 0; i < jsonArr.length(); i++) {
                item.add(jsonArr.getJSONObject(i).getString("id") + " - " + jsonArr.getJSONObject(i).getString("user_name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        spinner.setAdapter(mArrayadapter_member);
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeypad();

                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideSystemUI();
                if (position != 0) {
                    selectID = mArrayadapter_member.getItem(position).toString().substring(0, mArrayadapter_member.getItem(position).toString().indexOf(" - "));
                    selectName = mArrayadapter_member.getItem(position).toString().substring(12);
                    phase1.setTextColor(getColor(R.color.blue_overlay));
                    phase4.setTextColor(getColor(R.color.black_overlay));
                    phase5.setTextColor(getColor(R.color.black_overlay));
                    Log.e("아이템", selectID.length() + "   " + selectName.length());

                    //GetDoWorkDate("http://211.253.30.245/equipment/kor/miniplus/MiniplusSetDate_SEL.php?users_id=" + selectID);
                    GetDoWorkDate("http://211.253.30.245/equipment/kor/miniplus/MiniplusSetDate_SEL2.php?clubs_id=" + PublicValues.club_id);
                } else {
                    selectID = selectName = "";
                    phase1.setTextColor(getColor(R.color.red_overlay));
                    phase4.setTextColor(getColor(R.color.black_overlay));
                    phase5.setTextColor(getColor(R.color.black_overlay));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hideSystemUI();
                Log.e("순번", "a");
            }
        });
    }



    private void GetDoWorkDate(String str) {
        class GetDataJSON extends AsyncTask<String, String, String> {

            @Override
            protected void onPreExecute() {
                progressDialog.DialogStart();
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
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
                    return sb.toString().trim();

                } catch (Exception e) {
                    return "Exception: " + e.getMessage();
                }
            }

            protected void onPostExecute(String result) {
                //myJSON = result;
                DoWorkDate(result);
            }
        }
        GetDataJSON json = new GetDataJSON();
        json.execute(str);
    }

    public void DoWorkDate(String result) {
        try {
            int[] todayColors = new int[2];
            boolean todayexist = false;
            JSONArray jsonArr = new JSONArray(new JSONObject(result).getString("result"));
            for(int i = 0; i < jsonArr.length(); i++) {
                Log.e("빨강", jsonArr.getJSONObject(i).getString("workout_date"));
                if(!jsonArr.getJSONObject(i).getString("workout_date").equals(String.format("%04d" ,CalendarDay.today().getYear()) + "-" + String.format("%02d" ,(CalendarDay.today().getMonth() + 1)) + "-" + String.format("%02d" ,CalendarDay.today().getDay()))) {
                    strDoWorkList.add(jsonArr.getJSONObject(i).getString("workout_date"));  //오늘 아닌거
                    //todayexist = false;
                } else {    //오늘
                    todayexist = true;
                    if(jsonArr.getJSONObject(jsonArr.length() - 1).getString("workout_date").equals(String.format("%04d" ,CalendarDay.today().getYear()) + "-" + String.format("%02d" ,(CalendarDay.today().getMonth() + 1)) + "-" + String.format("%02d" ,CalendarDay.today().getDay()))) {
                        strDoWorkList.add(jsonArr.getJSONObject(i).getString("workout_date"));
                        todayColors[0] = Color.rgb(0, 0, 255);  //무조건 이거 들어가지
                    } else {
                        todayColors[0] = Color.rgb(255, 0, 0);

                    }
                    todayColors[1] = Color.rgb(0, 255, 0);
                }
            }
            if(!todayexist) {
                todayColors[0] = Color.rgb(255, 0, 0);
            } else {
                todayColors[0] = Color.rgb(0, 0, 255);
            }
            todayColors[1] = Color.rgb(0, 255, 0);
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String caldate = df.format(date);
            int caldate_y = Integer.parseInt(caldate.substring(0, 4));
            int caldate_m = Integer.parseInt(caldate.substring(5, 7));
            int caldate_d = Integer.parseInt(caldate.substring(8, 10));

            materialCalendar.removeDecorator(doWorkDecorator);
            materialCalendar.removeDecorator(dontWorkDecorator);
            doWorkDecorator = new DoWorkDecorator(caldate_y, caldate_m, caldate_d, 2022, 2, 7, (ArrayList) strDoWorkList.clone());
            dontWorkDecorator = new DontWorkDecorator(caldate_y, caldate_m, caldate_d, 2022, 2, 7, (ArrayList) strDoWorkList.clone());
            //doWorkDecorator = new DoWorkDecorator(caldate_y, caldate_m, caldate_d, 2021, 12, 7, (ArrayList) strDoWorkList.clone());
            //dontWorkDecorator = new DontWorkDecorator(caldate_y, caldate_m, caldate_d, 2021, 12, 7, (ArrayList) strDoWorkList.clone());

            strDoWorkList.removeAll(strDoWorkList);

            materialCalendar.removeDecorator(todayDecorator);
            memberTodayDecorator = new MemberTodayDecorator(3, todayColors);
            //materialCalendar.addDecorators(memberTodayDecorator, doWorkDecorator, dontWorkDecorator);
            materialCalendar.addDecorators(doWorkDecorator);

            progressDialog.DialogStop();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("제이슨", e.toString()); //아예 데이터가 없을때 혹은 이상할때

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String caldate = df.format(date);
            int caldate_y = Integer.parseInt(caldate.substring(0, 4));
            int caldate_m = Integer.parseInt(caldate.substring(5, 7));
            int caldate_d = Integer.parseInt(caldate.substring(8, 10));

            //materialCalendar.removeDecorator(doWorkDecorator);
            //materialCalendar.removeDecorator(dontWorkDecorator);
            noWorkDecorator = new NoWorkDecorator(caldate_y, caldate_m, caldate_d, 2022, 2, 7);
            int[] todayColors = {Color.RED, Color.GREEN};
            materialCalendar.removeDecorator(todayDecorator);
            memberTodayDecorator = new MemberTodayDecorator(3, todayColors);
            //materialCalendar.addDecorators(memberTodayDecorator, noWorkDecorator);

            progressDialog.DialogStop();
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
    BufferedWriter fw;
    public void CreateCSV() {
        String mode, type;

        File dir = new File("sdcard/Download/csvFolder");
        if(!dir.exists())
            dir.mkdirs();

        try {
            if(strDateList.size() == 1 || strDateList.size() == 2) {

                if(strDateList.size() == 1)    fw = new BufferedWriter(new FileWriter("sdcard/Download/csvFolder/" + selectName + "_" + strDateList.get(0) + ".csv"));
                else    fw = new BufferedWriter(new FileWriter("sdcard/Download/csvFolder/" + selectName + "_" + strDateList.get(0) + "_" + strDateList.get(1) + ".csv"));
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
                        fw.write("BEFORE SETTING," + jsonObject_REP.getString("bef_setting"));
                        fw.newLine();
                        fw.write("AFTER SETTING," + jsonObject_REP.getString("aft_setting"));
                        fw.newLine();
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

                Log.e("히얼","히얼");

                phase4.setTextColor(getColor(R.color.blue_overlay));
                if(strDateList.size() == 1) {
                    SendMail_Async mailServer = new SendMail_Async(getApplicationContext(), edtxt.getText().toString().trim(), selectName + "님의 " + strDateList.get(0).toString().trim() + "데이터입니다.", "론픽 미니플러스 운동 데이터입니다.", "sdcard/Download/csvFolder/" + selectName + "_" + strDateList.get(0) + ".csv");
                    mailServer.execute();
                } else if(strDateList.size() == 2) {
                    SendMail_Async mailServer = new SendMail_Async(getApplicationContext(), edtxt.getText().toString().trim(), selectName + "님의 " + strDateList.get(0).toString().trim() + " ~ " + strDateList.get(1).toString().trim() + "데이터입니다.", "론픽 미니플러스 운동 데이터입니다.", "sdcard/Download/csvFolder/" + selectName + "_" + strDateList.get(0) + "_" + strDateList.get(1) + ".csv");
                    mailServer.execute();
                }

            } else {
                phase4.setTextColor(getColor(R.color.red_overlay));
                Data_sets.removeAll(Data_sets);
                miniList.removeAll(miniList);
                Log.e("이유는", Data_sets.size() + "   " + strDateList.size());
            }

        } catch (Exception e) {
            Log.e("이유", e.toString());
            phase4.setTextColor(getColor(R.color.red_overlay));
            Data_sets.removeAll(Data_sets);
            miniList.removeAll(miniList);
        }
    }

    ArrayList<String> sheetList = new ArrayList<>();
    public void writeExcel() {
        Log.e("엑셀 시작", "시작");
        File dir = new File("sdcard/Download/excelFolder");
        if(!dir.exists())
            dir.mkdirs();
        String mode, type;
        int rowNum = 0;
        try {
            if(strDateList.size() == 1 || strDateList.size() == 2) {
                File file;
                if (strDateList.size() == 1)
                    file = new File("sdcard/Download/excelFolder/" + clubName + "_" + strDateList.get(0) + ".xlsx");
                else
                    file = new File("sdcard/Download/excelFolder/" + clubName + "_" + strDateList.get(0) + "_" + strDateList.get(1) + ".xlsx");
                FileOutputStream fileout = new FileOutputStream(file);

                XSSFWorkbook xworkbook = new XSSFWorkbook();

                XSSFFont defaultFont = xworkbook.createFont();
                defaultFont.setFontHeightInPoints((short) 13);
                defaultFont.setColor(IndexedColors.BLACK.getIndex());
                defaultFont.setBold(true);
                defaultFont.setFontName("Noto Sans KR");

                sheetList.removeAll(sheetList);

                boolean sameName = false;
                XSSFSheet xsheet;
                XSSFRow curRow;
                Cell cell;
                CellStyle cellStyle1 = xworkbook.createCellStyle();
                cellStyle1.setAlignment(HorizontalAlignment.CENTER);
                cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
                cellStyle1.setFont(defaultFont);
                xsheet = xworkbook.createSheet();
                try {
                    sheetList.add(miniList.get(0).getString("users_id"));
                    measList.removeAll(measList);
                    GetData_meas("http://211.253.30.245/php/get_miniTest2.php?user_id=" + miniList.get(0).getString("users_id"));
                    for (int i = 0; i < Data_sets.size(); i++) {
                        Log.e("중복아이디", miniList.get(i).getString("users_id") + "    " + i);
                        JSONObject jsonObject_SET = (JSONObject) miniList.get(i);
                        mode = searchModeType(jsonObject_SET.getString("workoutmode_code"));
                        type = searchModeType(jsonObject_SET.getString("workouttype_code"));
                        if(i != 0) {
                            if(!miniList.get(i).getString("users_id").equals(miniList.get(i - 1).getString("users_id"))) {  //앞에꺼랑 이름이 다르면
                                if (!sheetList.contains(miniList.get(i).getString("users_id"))) {    //겹치는거 없으면
                                    if (!failFlag) {
                                        rowNum = xsheet.getLastRowNum() + 4;
                                        curRow = xsheet.createRow(rowNum);
                                        cell = curRow.createCell(14);
                                        cell.setCellValue("TEST DATE");
                                        cell.setCellStyle(cellStyle1);
                                        cell = curRow.createCell(15);
                                        cell.setCellValue("TEST WORKOUT");
                                        cell.setCellStyle(cellStyle1);
                                        cell = curRow.createCell(16);
                                        cell.setCellValue("AVERAGE FORCE");
                                        cell.setCellStyle(cellStyle1);
                                        cell = curRow.createCell(17);
                                        cell.setCellValue("MAXIMUM FORCE");
                                        cell.setCellStyle(cellStyle1);
                                        cell = curRow.createCell(18);
                                        cell.setCellValue("AVERAGE SPEED");
                                        cell.setCellStyle(cellStyle1);
                                        cell = curRow.createCell(19);
                                        cell.setCellValue("MAXIMUM SPEED");
                                        cell.setCellStyle(cellStyle1);
                                        cell = curRow.createCell(20);
                                        cell.setCellValue("AVERAGE POWER");
                                        cell.setCellStyle(cellStyle1);
                                        cell = curRow.createCell(21);
                                        cell.setCellValue("MAXIMUM POWER");
                                        cell.setCellStyle(cellStyle1);
                                        cell = curRow.createCell(22);
                                        cell.setCellValue("POSITION DATA");
                                        cell.setCellStyle(cellStyle1);
                                        cell = curRow.createCell(23);
                                        cell.setCellValue("FORCE DATA");
                                        cell.setCellStyle(cellStyle1);
                                        cell = curRow.createCell(24);
                                        cell.setCellValue("SPEED DATA");
                                        cell.setCellStyle(cellStyle1);

                                        for(int k = 0; k < measList.size(); k++) {
                                            rowNum++;
                                            curRow = xsheet.createRow(rowNum);

                                            JSONObject jsonObject_MEAS = (JSONObject) measList.get(k);
                                            cell = curRow.createCell(14);
                                            cell.setCellValue(jsonObject_MEAS.getString("workoutdate").substring(0, 10));
                                            cell = curRow.createCell(15);
                                            if(jsonObject_MEAS.getString("test_code").equals("PUSH001")) {
                                                if(PublicValues.club_id.equals("1144")) {
                                                    cell.setCellValue("좌측1");
                                                } else {
                                                    cell.setCellValue("상지 전면 좌측");
                                                }
                                            } else if(jsonObject_MEAS.getString("test_code").equals("PUSH002")) {
                                                if(PublicValues.club_id.equals("1144")) {
                                                    cell.setCellValue("우측1");
                                                } else {
                                                    cell.setCellValue("상지 전면 우측");
                                                }
                                            } else if(jsonObject_MEAS.getString("test_code").equals("PULL001")) {
                                                if(PublicValues.club_id.equals("1144")) {
                                                    cell.setCellValue("좌측2");
                                                } else {
                                                    cell.setCellValue("상지 후면 좌측");
                                                }
                                            } else if(jsonObject_MEAS.getString("test_code").equals("PULL002")) {
                                                if(PublicValues.club_id.equals("1144")) {
                                                    cell.setCellValue("우측2");
                                                } else {
                                                    cell.setCellValue("상지 후면 우측");
                                                }
                                            } else if(jsonObject_MEAS.getString("test_code").equals("ROT001")) {
                                                if(PublicValues.club_id.equals("1144")) {
                                                    cell.setCellValue("좌측3");
                                                } else {
                                                    cell.setCellValue("몸통 좌측");
                                                }
                                            } else if(jsonObject_MEAS.getString("test_code").equals("ROT002")) {
                                                if(PublicValues.club_id.equals("1144")) {
                                                    cell.setCellValue("우측3");
                                                } else {
                                                    cell.setCellValue("몸통 우측");
                                                }
                                            } else if(jsonObject_MEAS.getString("test_code").equals("UPCODE001")) {
                                                if(PublicValues.club_id.equals("1144")) {
                                                    cell.setCellValue("운동4");
                                                } else {
                                                    cell.setCellValue("상지 전면");
                                                }
                                            } else if(jsonObject_MEAS.getString("test_code").equals("UPCODE002")) {
                                                if(PublicValues.club_id.equals("1144")) {
                                                    cell.setCellValue("운동5");
                                                } else {
                                                    cell.setCellValue("상지 후면");
                                                }
                                            } else if(jsonObject_MEAS.getString("test_code").equals("LOWCODE001")) {
                                                if(PublicValues.club_id.equals("1144")) {
                                                    cell.setCellValue("운동6");
                                                } else {
                                                    cell.setCellValue("하지 전면");
                                                }
                                            } else if(jsonObject_MEAS.getString("test_code").equals("LOWCODE002")) {
                                                if(PublicValues.club_id.equals("1144")) {
                                                    cell.setCellValue("운동7");
                                                } else {
                                                    cell.setCellValue("하지 후면");
                                                }
                                            }
                                            //cell.setCellValue(jsonObject_MEAS.getString("test_code"));
                                            cell = curRow.createCell(16);
                                            cell.setCellValue(Double.parseDouble(jsonObject_MEAS.getString("avg_force")));
                                            cell = curRow.createCell(17);
                                            cell.setCellValue(Double.parseDouble(jsonObject_MEAS.getString("max_force")));
                                            cell = curRow.createCell(18);
                                            cell.setCellValue(Double.parseDouble(jsonObject_MEAS.getString("avg_speed")));
                                            cell = curRow.createCell(19);
                                            cell.setCellValue(Double.parseDouble(jsonObject_MEAS.getString("max_speed")));
                                            cell = curRow.createCell(20);
                                            cell.setCellValue(Double.parseDouble(jsonObject_MEAS.getString("avg_power")));
                                            cell = curRow.createCell(21);
                                            cell.setCellValue(Double.parseDouble(jsonObject_MEAS.getString("max_power")));
                                            cell = curRow.createCell(22);
                                            cell.setCellValue(jsonObject_MEAS.getString("pos_data"));
                                            cell = curRow.createCell(23);
                                            cell.setCellValue(jsonObject_MEAS.getString("force_data"));
                                            cell = curRow.createCell(24);
                                            cell.setCellValue(jsonObject_MEAS.getString("speed_data"));
                                        }

                                    } else {
                                        failFlag = false;
                                    }

                                    measList.removeAll(measList);
                                    GetData_meas("http://211.253.30.245/php/get_miniTest2.php?user_id=" + miniList.get(i).getString("users_id"));

                                    sheetList.add(miniList.get(i).getString("users_id"));
                                    Log.e("시트수", sheetList.size() + "");
                                    xsheet = xworkbook.createSheet();
                                    sameName = false;
                                } else {
                                    xsheet = xsheet.getWorkbook().getSheetAt(sheetList.indexOf(miniList.get(i).getString("users_id")));
                                    rowNum = xsheet.getLastRowNum() + 1;
                                    Log.e("몇번째 줄", xsheet.getLastRowNum() + "");
                                    sameName = true;
                                }
                            }
                        }
                        if(!sameName) {
                            sameName = true;

                            Log.e("시트 수", miniList.get(i).getString("users_id") + "  ");
                            xworkbook.setSheetName(sheetList.size() - 1, miniList.get(i).getString("users_id"));

                            //xworkbook.setSheetName(i, miniList.get(i).getString("users_id"));



                            rowNum = 0;

                            curRow = xsheet.createRow(rowNum);
                            rowNum++;
                            cell = curRow.createCell(0);
                            cell.setCellValue("WORKOUT PART");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(1);
                            cell.setCellValue("SET");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(2);
                            cell.setCellValue("WORKOUT DATE");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(3);
                            cell.setCellValue("WORKOUT MODE");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(4);
                            cell.setCellValue("WORKOUT TYPE");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(5);
                            cell.setCellValue("SET AVERAGE FORCE");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(6);
                            cell.setCellValue("SET MAXIMUM FORCE");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(7);
                            cell.setCellValue("SET AVERAGE SPEED");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(8);
                            cell.setCellValue("SET MAXIMUM SPEED");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(9);
                            cell.setCellValue("SET AVERAGE POWER");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(10);
                            cell.setCellValue("SET MAXIMUM POWER");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(11);
                            cell.setCellValue("WORKOUT TIME");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(12);
                            cell.setCellValue("BREAK TIME");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(13);
                            cell.setCellValue("REP");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(14);
                            cell.setCellValue("BEFORE SETTING");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(15);
                            cell.setCellValue("AFTER SETTING");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(16);
                            cell.setCellValue("AVERAGE FORCE");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(17);
                            cell.setCellValue("MAXIMUM FORCE");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(18);
                            cell.setCellValue("AVERAGE SPEED");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(19);
                            cell.setCellValue("MAXIMUM SPEED");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(20);
                            cell.setCellValue("AVERAGE POWER");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(21);
                            cell.setCellValue("MAXIMUM POWER");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(22);
                            cell.setCellValue("POSITION DATA");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(23);
                            cell.setCellValue("FORCE DATA");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(24);
                            cell.setCellValue("SPEED DATA");
                            cell.setCellStyle(cellStyle1);
                            cell = curRow.createCell(25);
                            cell.setCellValue("POWER DATA");
                            cell.setCellStyle(cellStyle1);

                            xsheet.setColumnWidth(0, 32 * 160);
                            xsheet.setColumnWidth(2, 32 * 160);
                            xsheet.setColumnWidth(3, 32 * 166);
                            xsheet.setColumnWidth(4, 32 * 157);
                            xsheet.setColumnWidth(5, 32 * 198);
                            xsheet.setColumnWidth(6, 32 * 208);
                            xsheet.setColumnWidth(7, 32 * 196);
                            xsheet.setColumnWidth(8, 32 * 206);
                            xsheet.setColumnWidth(9, 32 * 205);
                            xsheet.setColumnWidth(10, 32 * 215);
                            xsheet.setColumnWidth(11, 32 * 157);
                            xsheet.setColumnWidth(12, 32 * 121);
                            xsheet.setColumnWidth(14, 32 * 500);
                            xsheet.setColumnWidth(15, 32 * 500);
                            xsheet.setColumnWidth(16, 32 * 158);
                            xsheet.setColumnWidth(17, 32 * 168);
                            xsheet.setColumnWidth(18, 32 * 156);
                            xsheet.setColumnWidth(19, 32 * 166);
                            xsheet.setColumnWidth(20, 32 * 165);
                            xsheet.setColumnWidth(21, 32 * 175);
                            xsheet.setColumnWidth(22, 32 * 1000);
                            xsheet.setColumnWidth(23, 32 * 1000);
                            xsheet.setColumnWidth(24, 32 * 1000);
                            xsheet.setColumnWidth(25, 32 * 1000);
                        }
                        for (int j = 0; j < Data_sets.get(i).size(); j++) {
                            JSONObject jsonObject_REP = (JSONObject) Data_sets.get(i).get(j);
                            curRow = xsheet.createRow(rowNum);
                            rowNum++;
                            if (j == 0) {
                                cell = curRow.createCell(0);
                                cell.setCellValue(jsonObject_SET.getString("workoutpart_code"));
                                cell = curRow.createCell(1);
                                cell.setCellValue(Integer.parseInt(jsonObject_REP.getString("sets")));
                                cell = curRow.createCell(2);
                                cell.setCellValue(jsonObject_SET.getString("workout_date"));
                                cell = curRow.createCell(3);
                                cell.setCellValue(mode);
                                cell = curRow.createCell(4);
                                cell.setCellValue(type);
                                cell = curRow.createCell(5);
                                cell.setCellValue(Double.parseDouble(jsonObject_SET.getString("avg_force")));
                                cell = curRow.createCell(6);
                                cell.setCellValue(Double.parseDouble(jsonObject_SET.getString("max_force")));
                                cell = curRow.createCell(7);
                                cell.setCellValue(Double.parseDouble(jsonObject_SET.getString("avg_speed")));
                                cell = curRow.createCell(8);
                                cell.setCellValue(Double.parseDouble(jsonObject_SET.getString("max_speed")));
                                cell = curRow.createCell(9);
                                cell.setCellValue(Double.parseDouble(jsonObject_SET.getString("avg_power")));
                                cell = curRow.createCell(10);
                                cell.setCellValue(Double.parseDouble(jsonObject_SET.getString("max_power")));
                                cell = curRow.createCell(11);
                                cell.setCellValue(jsonObject_SET.getString("workouttime"));
                                cell = curRow.createCell(12);
                                cell.setCellValue(jsonObject_SET.getString("breaktime"));
                            }
                            cell = curRow.createCell(13);
                            cell.setCellValue(Integer.parseInt(jsonObject_REP.getString("reps")));
                            cell = curRow.createCell(14);
                            cell.setCellValue(jsonObject_REP.getString("bef_setting").replaceAll(",,", " - ").replaceAll(",", ":"));
                            cell = curRow.createCell(15);
                            cell.setCellValue(jsonObject_REP.getString("aft_setting").replaceAll(",,", " - ").replaceAll(",", ":"));
                            cell = curRow.createCell(16);
                            cell.setCellValue(Double.parseDouble(jsonObject_REP.getString("avg_force")));
                            cell = curRow.createCell(17);
                            cell.setCellValue(Double.parseDouble(jsonObject_REP.getString("max_force")));
                            cell = curRow.createCell(18);
                            cell.setCellValue(Double.parseDouble(jsonObject_REP.getString("avg_speed")));
                            cell = curRow.createCell(19);
                            cell.setCellValue(Double.parseDouble(jsonObject_REP.getString("max_speed")));
                            cell = curRow.createCell(20);
                            cell.setCellValue(Double.parseDouble(jsonObject_REP.getString("avg_power")));
                            cell = curRow.createCell(21);
                            cell.setCellValue(Double.parseDouble(jsonObject_REP.getString("max_power")));
                            cell = curRow.createCell(22);
                            cell.setCellValue(jsonObject_REP.getString("pos_data"));
                            cell = curRow.createCell(23);
                            cell.setCellValue(jsonObject_REP.getString("force_data"));
                            cell = curRow.createCell(24);
                            cell.setCellValue(jsonObject_REP.getString("speed_data"));
                            cell = curRow.createCell(25);
                            cell.setCellValue(jsonObject_REP.getString("power_data"));
                            progressPer++;
                            progressDialog2.setData(progressPer);
                            Log.e("프로그래스 맥스", progressMax + "  " + progressPer);
                        }

                        if(i == Data_sets.size() - 1) {
                            if (!failFlag) {
                                rowNum = xsheet.getLastRowNum() + 4;
                                curRow = xsheet.createRow(rowNum);
                                cell = curRow.createCell(14);
                                cell.setCellValue("TEST DATE");
                                cell.setCellStyle(cellStyle1);
                                cell = curRow.createCell(15);
                                cell.setCellValue("TEST WORKOUT");
                                cell.setCellStyle(cellStyle1);
                                cell = curRow.createCell(16);
                                cell.setCellValue("AVERAGE FORCE");
                                cell.setCellStyle(cellStyle1);
                                cell = curRow.createCell(17);
                                cell.setCellValue("MAXIMUM FORCE");
                                cell.setCellStyle(cellStyle1);
                                cell = curRow.createCell(18);
                                cell.setCellValue("AVERAGE SPEED");
                                cell.setCellStyle(cellStyle1);
                                cell = curRow.createCell(19);
                                cell.setCellValue("MAXIMUM SPEED");
                                cell.setCellStyle(cellStyle1);
                                cell = curRow.createCell(20);
                                cell.setCellValue("AVERAGE POWER");
                                cell.setCellStyle(cellStyle1);
                                cell = curRow.createCell(21);
                                cell.setCellValue("MAXIMUM POWER");
                                cell.setCellStyle(cellStyle1);
                                cell = curRow.createCell(22);
                                cell.setCellValue("POSITION DATA");
                                cell.setCellStyle(cellStyle1);
                                cell = curRow.createCell(23);
                                cell.setCellValue("FORCE DATA");
                                cell.setCellStyle(cellStyle1);
                                cell = curRow.createCell(24);
                                cell.setCellValue("SPEED DATA");
                                cell.setCellStyle(cellStyle1);

                                for(int k = 0; k < measList.size(); k++) {
                                    //Log.e("측정숫자", measList.get(i).getString("pos_data"));
                                    rowNum++;
                                    curRow = xsheet.createRow(rowNum);

                                    JSONObject jsonObject_MEAS = (JSONObject) measList.get(k);
                                    cell = curRow.createCell(14);
                                    cell.setCellValue(jsonObject_MEAS.getString("workoutdate").substring(0, 10));
                                    cell = curRow.createCell(15);
                                    if(jsonObject_MEAS.getString("test_code").equals("PUSH001")) {
                                        if(PublicValues.club_id.equals("1144")) {
                                            cell.setCellValue("좌측1");
                                        } else {
                                            cell.setCellValue("상지 전면 좌측");
                                        }
                                    } else if(jsonObject_MEAS.getString("test_code").equals("PUSH002")) {
                                        if(PublicValues.club_id.equals("1144")) {
                                            cell.setCellValue("우측1");
                                        } else {
                                            cell.setCellValue("상지 전면 우측");
                                        }
                                    } else if(jsonObject_MEAS.getString("test_code").equals("PULL001")) {
                                        if(PublicValues.club_id.equals("1144")) {
                                            cell.setCellValue("좌측2");
                                        } else {
                                            cell.setCellValue("상지 후면 좌측");
                                        }
                                    } else if(jsonObject_MEAS.getString("test_code").equals("PULL002")) {
                                        if(PublicValues.club_id.equals("1144")) {
                                            cell.setCellValue("우측2");
                                        } else {
                                            cell.setCellValue("상지 후면 우측");
                                        }
                                    } else if(jsonObject_MEAS.getString("test_code").equals("ROT001")) {
                                        if(PublicValues.club_id.equals("1144")) {
                                            cell.setCellValue("좌측3");
                                        } else {
                                            cell.setCellValue("몸통 좌측");
                                        }
                                    } else if(jsonObject_MEAS.getString("test_code").equals("ROT002")) {
                                        if(PublicValues.club_id.equals("1144")) {
                                            cell.setCellValue("우측3");
                                        } else {
                                            cell.setCellValue("몸통 우측");
                                        }
                                    } else if(jsonObject_MEAS.getString("test_code").equals("UPCODE001")) {
                                        if(PublicValues.club_id.equals("1144")) {
                                            cell.setCellValue("운동4");
                                        } else {
                                            cell.setCellValue("상지 전면");
                                        }
                                    } else if(jsonObject_MEAS.getString("test_code").equals("UPCODE002")) {
                                        if(PublicValues.club_id.equals("1144")) {
                                            cell.setCellValue("운동5");
                                        } else {
                                            cell.setCellValue("상지 후면");
                                        }
                                    } else if(jsonObject_MEAS.getString("test_code").equals("LOWCODE001")) {
                                        if(PublicValues.club_id.equals("1144")) {
                                            cell.setCellValue("운동6");
                                        } else {
                                            cell.setCellValue("하지 전면");
                                        }
                                    } else if(jsonObject_MEAS.getString("test_code").equals("LOWCODE002")) {
                                        if(PublicValues.club_id.equals("1144")) {
                                            cell.setCellValue("운동7");
                                        } else {
                                            cell.setCellValue("하지 후면");
                                        }
                                    }
                                    //cell.setCellValue(jsonObject_MEAS.getString("test_code"));
                                    cell = curRow.createCell(16);
                                    cell.setCellValue(Double.parseDouble(jsonObject_MEAS.getString("avg_force")));
                                    cell = curRow.createCell(17);
                                    cell.setCellValue(Double.parseDouble(jsonObject_MEAS.getString("max_force")));
                                    cell = curRow.createCell(18);
                                    cell.setCellValue(Double.parseDouble(jsonObject_MEAS.getString("avg_speed")));
                                    cell = curRow.createCell(19);
                                    cell.setCellValue(Double.parseDouble(jsonObject_MEAS.getString("max_speed")));
                                    cell = curRow.createCell(20);
                                    cell.setCellValue(Double.parseDouble(jsonObject_MEAS.getString("avg_power")));
                                    cell = curRow.createCell(21);
                                    cell.setCellValue(Double.parseDouble(jsonObject_MEAS.getString("max_power")));
                                    cell = curRow.createCell(22);
                                    cell.setCellValue(jsonObject_MEAS.getString("pos_data"));
                                    cell = curRow.createCell(23);
                                    cell.setCellValue(jsonObject_MEAS.getString("force_data"));
                                    cell = curRow.createCell(24);
                                    cell.setCellValue(jsonObject_MEAS.getString("speed_data"));
                                }

                            } else {
                                failFlag = false;
                            }
                        }
                    }
                } catch (JSONException e) {
                    Log.e("측정숫자", e.toString());
                    ;
                }
            /*
            xsheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
            xsheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
            xsheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
            xsheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));   //set 병합
            */

            /*
            xsheet.addMergedRegion(new CellRangeAddress(0, 3, 15, 15));
            xsheet.addMergedRegion(new CellRangeAddress(0, 3, 16, 16));
            xsheet.addMergedRegion(new CellRangeAddress(0, 1, 17, 17));
            xsheet.addMergedRegion(new CellRangeAddress(2, 3, 17, 17));
            xsheet.addMergedRegion(new CellRangeAddress(0, 1, 18, 18));
            xsheet.addMergedRegion(new CellRangeAddress(2, 3, 18, 18));
            xsheet.addMergedRegion(new CellRangeAddress(0, 1, 19, 19));
            xsheet.addMergedRegion(new CellRangeAddress(2, 3, 19, 19));
            xsheet.addMergedRegion(new CellRangeAddress(0, 1, 20, 20));
            xsheet.addMergedRegion(new CellRangeAddress(2, 3, 20, 20));
            xsheet.addMergedRegion(new CellRangeAddress(0, 1, 21, 21));
            xsheet.addMergedRegion(new CellRangeAddress(2, 3, 21, 21));
            xsheet.addMergedRegion(new CellRangeAddress(0, 1, 22, 22));
            xsheet.addMergedRegion(new CellRangeAddress(2, 3, 22, 22));
            xsheet.addMergedRegion(new CellRangeAddress(0, 1, 23, 23));
            xsheet.addMergedRegion(new CellRangeAddress(2, 3, 23, 23));
            xsheet.addMergedRegion(new CellRangeAddress(0, 1, 24, 24));
            xsheet.addMergedRegion(new CellRangeAddress(2, 3, 24, 24)); //rep 병합
            */


            /*
            curRow = xsheet.createRow(0);
            cell = curRow.createCell(0);
            cell.setCellValue("SET");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(1);
            cell.setCellValue(1);

            cell = curRow.createCell(2);
            cell.setCellValue("WORKOUT DATE");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(3);
            cell.setCellValue("2022-01-10");

            cell = curRow.createCell(4);
            cell.setCellValue("WORKOUT MODE");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(5);
            cell.setCellValue("ECCENTRIC");

            cell = curRow.createCell(6);
            cell.setCellValue("SET AVERAGE FROCE");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(7);
            cell.setCellValue(60.1);

            cell = curRow.createCell(8);
            cell.setCellValue("SET AVERAGE SPEED");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(9);
            cell.setCellValue(76.4);

            cell = curRow.createCell(10);
            cell.setCellValue("SET AVERAGE POWER");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(11);
            cell.setCellValue(44.1);

            cell = curRow.createCell(12);
            cell.setCellValue("WORKOUT TIME");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(13);
            cell.setCellValue("0:00:06");

            cell = curRow.createCell(15);
            cell.setCellValue("REP");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(16);
            cell.setCellValue(1);

            cell = curRow.createCell(17);
            cell.setCellValue("BEFORE SETTING");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(18);
            cell.setCellValue("UP WEIGHT:5.0kg, DOWN WEIGHT:5.0kg");

            cell = curRow.createCell(19);
            cell.setCellValue("AVERAGE FORCE");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(20);
            cell.setCellValue(63.56);

            cell = curRow.createCell(21);
            cell.setCellValue("AVERAGE SPEED");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(22);
            cell.setCellValue(73.07);

            cell = curRow.createCell(23);
            cell.setCellValue("AVERAGE POWER");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(24);
            cell.setCellValue(41.44);

            cell = curRow.createCell(25);
            cell.setCellValue("POSITION DATA");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(26);
            cell.setCellValue(0.5);
            cell = curRow.createCell(27);
            cell.setCellValue(1.7);
            cell = curRow.createCell(28);
            cell.setCellValue(3.9);
            cell = curRow.createCell(29);
            cell.setCellValue(11.3);
            cell = curRow.createCell(30);
            cell.setCellValue(16.1);
            cell = curRow.createCell(31);
            cell.setCellValue(21.4);
            cell = curRow.createCell(32);
            cell.setCellValue(27);
            cell = curRow.createCell(33);
            cell.setCellValue(32.4);
            cell = curRow.createCell(34);
            cell.setCellValue(37.4);
            cell = curRow.createCell(35);
            cell.setCellValue(40.7);
            cell = curRow.createCell(36);
            cell.setCellValue(41.9);

            curRow = xsheet.createRow(1);
            cell = curRow.createCell(4);
            cell.setCellValue("WORKOUT TYPE");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(5);
            cell.setCellValue("BASE");

            cell = curRow.createCell(6);
            cell.setCellValue("SET MAXIMUM FROCE");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(7);
            cell.setCellValue(123.4);

            cell = curRow.createCell(8);
            cell.setCellValue("SET MAXIMUM SPEED");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(9);
            cell.setCellValue(122.6);

            cell = curRow.createCell(10);
            cell.setCellValue("SET MAXIMUM POWER");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(11);
            cell.setCellValue(94.1);

            cell = curRow.createCell(12);
            cell.setCellValue("BREAK TIME");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(13);
            cell.setCellValue("0:00:04");

            cell = curRow.createCell(25);
            cell.setCellValue("FORCE DATA");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(26);
            cell.setCellValue(100.9);
            cell = curRow.createCell(27);
            cell.setCellValue(111.4);
            cell = curRow.createCell(28);
            cell.setCellValue(111.3);
            cell = curRow.createCell(29);
            cell.setCellValue(89);
            cell = curRow.createCell(30);
            cell.setCellValue(77.3);
            cell = curRow.createCell(31);
            cell.setCellValue(69.4);
            cell = curRow.createCell(32);
            cell.setCellValue(55.5);
            cell = curRow.createCell(33);
            cell.setCellValue(39.9);
            cell = curRow.createCell(34);
            cell.setCellValue(18.1);
            cell = curRow.createCell(35);
            cell.setCellValue(15.5);
            cell = curRow.createCell(36);
            cell.setCellValue(10.9);

            curRow = xsheet.createRow(2);
            cell = curRow.createCell(17);
            cell.setCellValue("AFTER SETTING");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(18);
            cell.setCellValue("UP WEIGHT:5.0kg, DOWN WEIGHT:5.0kg");

            cell = curRow.createCell(19);
            cell.setCellValue("MAXIMUM FORCE");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(20);
            cell.setCellValue(111.4);

            cell = curRow.createCell(21);
            cell.setCellValue("MAXIMUM SPEED");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(22);
            cell.setCellValue(115.7);

            cell = curRow.createCell(23);
            cell.setCellValue("MAXIMUM POWER");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(24);
            cell.setCellValue(77.1);

            cell = curRow.createCell(25);
            cell.setCellValue("SPEED DATA");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(26);
            cell.setCellValue(9.4);
            cell = curRow.createCell(27);
            cell.setCellValue(23.7);
            cell = curRow.createCell(28);
            cell.setCellValue(43.6);
            cell = curRow.createCell(29);
            cell.setCellValue(84.5);
            cell = curRow.createCell(30);
            cell.setCellValue(99.7);
            cell = curRow.createCell(31);
            cell.setCellValue(110.1);
            cell = curRow.createCell(32);
            cell.setCellValue(115.7);
            cell = curRow.createCell(33);
            cell.setCellValue(114.7);
            cell = curRow.createCell(34);
            cell.setCellValue(103.9);
            cell = curRow.createCell(35);
            cell.setCellValue(69.7);
            cell = curRow.createCell(36);
            cell.setCellValue(28.8);

            curRow = xsheet.createRow(3);
            cell = curRow.createCell(25);
            cell.setCellValue("POWER DATA");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(26);
            cell.setCellValue(9.5);
            cell = curRow.createCell(27);
            cell.setCellValue(26.4);
            cell = curRow.createCell(28);
            cell.setCellValue(48.5);
            cell = curRow.createCell(29);
            cell.setCellValue(75.2);
            cell = curRow.createCell(30);
            cell.setCellValue(77.1);
            cell = curRow.createCell(31);
            cell.setCellValue(76.4);
            cell = curRow.createCell(32);
            cell.setCellValue(64.2);
            cell = curRow.createCell(33);
            cell.setCellValue(45.8);
            cell = curRow.createCell(34);
            cell.setCellValue(18.8);
            cell = curRow.createCell(35);
            cell.setCellValue(10.8);
            cell = curRow.createCell(36);
            cell.setCellValue(3.1);
            */
                for(int i = 0; i < xworkbook.getNumberOfSheets(); i++) {
                    xworkbook.setSheetName(i, GetMemberName("http://211.253.30.245/equipment/kor/common/NAME_SEL.php?id=", xworkbook.getSheetName(i)));
                }
                xworkbook.write(fileout);
                fileout.close();
                Log.e("엑셀 끝", "끝");
            /*
            File file = new File("sdcard/Download/ExFile.xlsx");

            InputStream inputStream = new FileInputStream("sdcard/Download/frame_loading_01.png");
            FileOutputStream fileout = new FileOutputStream(file);
            byte[] bytes = IOUtils.toByteArray(inputStream);


            XSSFWorkbook xworkbook = new XSSFWorkbook();

            XSSFFont defaultFont = xworkbook.createFont();
            defaultFont.setFontHeightInPoints((short)20);
            defaultFont.setColor(IndexedColors.WHITE.getIndex());
            defaultFont.setBold(true);
            defaultFont.setFontName("Noto Sans KR");



            XSSFSheet xsheet = xworkbook.createSheet("a");
            xsheet.setColumnWidth(4, 32*341);
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
            cellStyle1.setFont(defaultFont);


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

            xsheet.addMergedRegion(new CellRangeAddress(10, 13, 0, 0));

            curRow = xsheet.createRow(0);
            cell = curRow.createCell(0);
            cell.setCellValue("ㅋㅋㄹㅃㅃㅃㅃㅃㅃㅃㅃㅃa");
            cell.setCellStyle(cellStyle1);

            cell = curRow.createCell(4);
            cell.setCellValue("ㅋㅋㄹㅃㅃㅃㅃㅃㅃㅃㅃㅃ");
            cell.setCellStyle(cellStyle1);

            curRow = xsheet.createRow(5);
            cell = curRow.createCell(0);
            cell.setCellValue(1.22);
            cell = curRow.createCell(1);
            cell.setCellValue(2.31);
            cell = curRow.createCell(2);
            cell.setCellValue(4.11);
            cell = curRow.createCell(3);
            cell.setCellValue(7.11);

            curRow = xsheet.createRow(6);
            cell = curRow.createCell(0);
            cell.setCellValue(2.32);
            cell = curRow.createCell(1);
            cell.setCellValue(6.33);
            cell = curRow.createCell(2);
            cell.setCellValue(6.33);
            cell = curRow.createCell(3);
            cell.setCellValue(6.33);

            curRow = xsheet.createRow(7);
            cell = curRow.createCell(0);
            cell.setCellValue(4.12);
            cell = curRow.createCell(1);
            cell.setCellValue(4.12);
            cell = curRow.createCell(2);
            cell.setCellValue(4.12);
            cell = curRow.createCell(3);
            cell.setCellValue(4.12);

            curRow = xsheet.createRow(8);
            cell = curRow.createCell(0);
            cell.setCellValue(5.21);
            cell = curRow.createCell(1);
            cell.setCellValue(7.11);
            cell = curRow.createCell(2);
            cell.setCellValue(7.11);
            cell = curRow.createCell(3);
            cell.setCellValue(7.11);

            curRow = xsheet.createRow(9);
            cell = curRow.createCell(0);
            cell.setCellValue(6.12);
            cell = curRow.createCell(1);
            cell.setCellValue(5.25);
            cell = curRow.createCell(2);
            cell.setCellValue(6.12);
            cell = curRow.createCell(3);
            cell.setCellValue(1.22);

            curRow = xsheet.createRow(2);
            curRow.setHeight((short)1000);
            cell = curRow.createCell(1);


            XSSFDrawing drawing1 = xsheet.createDrawingPatriarch();
            XSSFClientAnchor anchor1 = drawing1.createAnchor(0, 0, 0, 0, 6, 5, 17, 19);


            XSSFChart chart = drawing1.createChart(anchor1);
            chart.setTitleText("1 rep");

            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.BOTTOM);

            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            bottomAxis.setTitle("position");
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);

            //XDDFDataSource<String> string = XDDFDataSourcesFactory.fromStringCellRange(xsheet, new CellRangeAddress(0, 0, 0, 6)); //글자는 이렇게 넣으면 된다.
            //XDDFNumericalDataSource<Double> positionD = XDDFDataSourcesFactory.fromNumericCellRange(xsheet, new CellRangeAddress(5, 9, 0, 0));
            //XDDFNumericalDataSource<Double> forceD = XDDFDataSourcesFactory.fromNumericCellRange(xsheet, new CellRangeAddress(5, 9, 1, 1));
            //XDDFNumericalDataSource<Double> speedD = XDDFDataSourcesFactory.fromNumericCellRange(xsheet, new CellRangeAddress(5, 9, 2, 2));
            //XDDFNumericalDataSource<Double> powerD = XDDFDataSourcesFactory.fromNumericCellRange(xsheet, new CellRangeAddress(5, 9, 3, 3));

            XDDFNumericalDataSource<Double> positionD = XDDFDataSourcesFactory.fromNumericCellRange(xsheet, new CellRangeAddress(5, 5, 0, 3));
            XDDFNumericalDataSource<Double> forceD = XDDFDataSourcesFactory.fromNumericCellRange(xsheet, new CellRangeAddress(6, 6, 0, 3));
            XDDFNumericalDataSource<Double> speedD = XDDFDataSourcesFactory.fromNumericCellRange(xsheet, new CellRangeAddress(7, 7, 0, 3));
            XDDFNumericalDataSource<Double> powerD = XDDFDataSourcesFactory.fromNumericCellRange(xsheet, new CellRangeAddress(8, 8, 0, 3));

            XDDFScatterChartData data = (XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, bottomAxis, leftAxis);

            XDDFScatterChartData.Series series1 = (XDDFScatterChartData.Series) data.addSeries(positionD, forceD);
            series1.setTitle("FORCE", null);
            series1.setSmooth(true);
            series1.setMarkerStyle(MarkerStyle.CIRCLE);

            XDDFScatterChartData.Series series2 = (XDDFScatterChartData.Series) data.addSeries(positionD, speedD);
            series2.setTitle("SPEED", null);
            series2.setSmooth(true);
            series2.setMarkerStyle(MarkerStyle.STAR);

            XDDFScatterChartData.Series series3 = (XDDFScatterChartData.Series) data.addSeries(positionD, powerD);
            series3.setTitle("POWER", null);
            series3.setSmooth(true);
            series3.setMarkerStyle(MarkerStyle.DIAMOND);

            chart.plot(data);
            //xsheet.autoSizeColumn(1);

            xworkbook.write(fileout);
            fileout.close();
            Log.e("엑셀 끝", "끝");
            */
            phase4.setTextColor(getColor(R.color.blue_overlay));
            if(strDateList.size() == 1) {
                SendMail_Async mailServer = new SendMail_Async(getApplicationContext(), edtxt.getText().toString().trim(), clubName + "의 " + strDateList.get(0).toString().trim() + "데이터입니다.", "론픽 미니플러스 운동 데이터입니다.", "sdcard/Download/excelFolder/" + clubName + "_" + strDateList.get(0) + ".xlsx");
                mailServer.execute();
            } else if(strDateList.size() == 2) {
                SendMail_Async mailServer = new SendMail_Async(getApplicationContext(), edtxt.getText().toString().trim(), clubName + "의 " + strDateList.get(0).toString().trim() + " ~ " + strDateList.get(1).toString().trim() + "데이터입니다.", "론픽 미니플러스 운동 데이터입니다.", "sdcard/Download/excelFolder/" + clubName + "_" + strDateList.get(0) + "_" + strDateList.get(1) + ".xlsx");
                mailServer.execute();
            }

            } else {
                phase4.setTextColor(getColor(R.color.red_overlay));
                Data_sets.removeAll(Data_sets);
                miniList.removeAll(miniList);
                Log.e("이유는", Data_sets.size() + "   " + strDateList.size());
            }
        } catch(FileNotFoundException ex) {
            phase4.setTextColor(getColor(R.color.red_overlay));
            Data_sets.removeAll(Data_sets);
            miniList.removeAll(miniList);
        } catch(IOException ex) {
            phase4.setTextColor(getColor(R.color.red_overlay));
            Data_sets.removeAll(Data_sets);
            miniList.removeAll(miniList);
        }
    }

    public void MatrixTime(int delayTime){
        long saveTime = System.currentTimeMillis();
        long currTime = 0;
        while( currTime - saveTime < delayTime){
            currTime = System.currentTimeMillis();
        }
    }

    public void hideKeypad(){

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        //MatrixTime(200);
    }

    class MyCustomDotSpan implements LineBackgroundSpan {

        private int radius;
        private int[] color = new int[0];

        MyCustomDotSpan(int radius, int[] color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void drawBackground(
                Canvas canvas, Paint paint,
                int left, int right, int top, int baseline, int bottom,
                CharSequence charSequence,
                int start, int end, int lineNum
        ) {

            int total = color.length > 5 ? 5 : color.length;
            int leftMost = (total - 1) * -10;

            //int pos = (left + right) / (color.length + 1);
            int pos = 30;
            for (int i = 0; i < total; i++) {
                int oldColor = paint.getColor();
                if (color[i] != 0) {
                    paint.setColor(color[i]);
                }
                //canvas.drawCircle((left + right) / 2 - leftMost, bottom + radius, radius, paint);
                canvas.drawCircle(pos, bottom + radius, radius, paint);
                paint.setColor(oldColor);
                Log.e("그림 위치", left + "  " + right);
                pos = pos + 7;
            }
        }
    }


    class TodayDecorator implements DayViewDecorator {

        Context context;
        private int radius;
        private int[] colors;
        private CalendarDay date = CalendarDay.today();

        TodayDecorator() {

        }

        TodayDecorator(Context context) {
            this.context = context;
        }

        public boolean shouldDecorate(@Nullable CalendarDay day) {
            return day.equals(date);
        }

        public void decorate(@Nullable DayViewFacade view) {
            //view.setBackgroundDrawable(context.getDrawable(R.drawable.circle));
            //view.addSpan(new MyCustomDotSpan(radius, colors)); //빨간점
            view.addSpan(new DotSpan(3, Color.GREEN));
        }
    }

    class EventDecorator implements DayViewDecorator{

        Context context;
        Drawable drawable;
        CalendarDay date;
        int yearNum;
        int monthNum;
        int dayNum;

        EventDecorator() {

        }

        EventDecorator(Context context, CalendarDay date) {
            Log.e("날짜!", monthNum + "");
            this.context = context;
            drawable = this.context.getDrawable(R.drawable.calendar_date_selector);
            this.date = date;
        }

        public boolean shouldDecorate(@Nullable CalendarDay day) {
            return day.equals(date);
        }

        public void decorate(@Nullable DayViewFacade view) {
            //view.setBackgroundDrawable(context.getDrawable(R.drawable.circle));
            //view.addSpan(new MyCustomDotSpan(radius, colors)); //빨간점
            if (drawable != null) {
                view.setSelectionDrawable(drawable);
                view.addSpan(new ForegroundColorSpan(Color.parseColor("#FF01E6F8")));
            } else {
                Log.d("event_decorator","is null");
            }
        }
    }

    class EventsDecorator implements DayViewDecorator{

        Context context;
        Drawable drawable;
        List<CalendarDay> dates;

        EventsDecorator() {

        }

        EventsDecorator(Context context, List<CalendarDay> dates) {
            this.context = context;
            drawable = this.context.getDrawable(R.drawable.calendar_date_selector);
            this.dates = dates;
        }

        public boolean shouldDecorate(@Nullable CalendarDay day) {
            return dates.contains(day);
        }

        public void decorate(@Nullable DayViewFacade view) {
            //view.setBackgroundDrawable(context.getDrawable(R.drawable.circle));
            //view.addSpan(new MyCustomDotSpan(radius, colors)); //빨간점
            if (drawable != null) {
                view.setSelectionDrawable(drawable);
                view.addSpan(new ForegroundColorSpan(Color.parseColor("#FF01E6F8")));
            } else {
                Log.d("event_decorator","is null");
            }
        }
    }

    class NOEventsDecorator implements DayViewDecorator{

        Context context;
        Drawable drawable;
        CalendarDay date;
        int maxY, maxM, maxD, minY, minM, minD;

        NOEventsDecorator() {

        }

        NOEventsDecorator(Context context, CalendarDay date, int maxY, int maxM, int maxD, int minY, int minM, int minD) {
            this.context = context;
            this.date = date;
            this.maxY = maxY;
            this.maxM = maxM;
            this.maxD = maxD;
            this.minY = minY;
            this.minM = minM;
            this.minD = minD;
        }

        public boolean shouldDecorate(@Nullable CalendarDay day) {
            if((day.getYear() == maxY && day.getMonth() == maxM - 1 && day.getDay() > maxD) || (day.getYear() == minY && day.getMonth() == minM - 1 && day.getDay() < minD)
                    || (day.getYear() > maxY) || (day.getYear() < minY) || (day.getYear() == maxY && day.getMonth() > maxM - 1) || (day.getYear() == minY && day.getMonth() < minM - 1)) {
                Log.e("달", day.getMonth() + "   " + maxM + "   " + minM);
                return false;
            } else {
                if(day.equals(date))    return false;
                else    return true;    //트루가 색칠
            }
        }

        public void decorate(@Nullable DayViewFacade view) {
            //view.setBackgroundDrawable(context.getDrawable(R.drawable.circle));
            //view.addSpan(new MyCustomDotSpan(radius, colors)); //빨간점
            view.addSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFFFF")));
        }
    }

    class NOEventDecoratorAll implements DayViewDecorator{

        Context context;
        int maxY, maxM, maxD, minY, minM, minD;

        NOEventDecoratorAll() {

        }

        NOEventDecoratorAll(Context context, int maxY, int maxM, int maxD, int minY, int minM, int minD) {
            this.context = context;
            this.maxY = maxY;
            this.maxM = maxM;
            this.maxD = maxD;
            this.minY = minY;
            this.minM = minM;
            this.minD = minD;
        }

        public boolean shouldDecorate(@Nullable CalendarDay day) {
            if((day.getYear() == maxY && day.getMonth() == maxM - 1 && day.getDay() > maxD) || (day.getYear() == minY && day.getMonth() == minM - 1 && day.getDay() < minD)
                    || (day.getYear() > maxY) || (day.getYear() < minY) || (day.getYear() == maxY && day.getMonth() > maxM - 1) || (day.getYear() == minY && day.getMonth() < minM - 1)) {
                Log.e("달", day.getMonth() + "   " + maxM + "   " + minM);
                return false;
            } else {
                return true;    //트루가 색칠
            }
        }

        public void decorate(@Nullable DayViewFacade view) {
            //view.setBackgroundDrawable(context.getDrawable(R.drawable.circle));
            //view.addSpan(new MyCustomDotSpan(radius, colors)); //빨간점
            view.addSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFFFF")));
        }
    }

    class MemberTodayDecorator implements DayViewDecorator {

        Context context;
        private int radius;
        private int[] colors;
        private CalendarDay date = CalendarDay.today();

        MemberTodayDecorator() {

        }

        public MemberTodayDecorator(int radius, int[] colors) {
            this.radius = radius;
            this.colors = colors;
        }

        MemberTodayDecorator(Context context) {
            this.context = context;
        }

        public boolean shouldDecorate(@Nullable CalendarDay day) {
            return day.equals(date);
        }

        public void decorate(@Nullable DayViewFacade view) {
            //view.setBackgroundDrawable(context.getDrawable(R.drawable.circle));
            view.addSpan(new MyCustomDotSpan(radius, colors)); //빨간점
            //view.addSpan(new DotSpan(1, Color.BLUE));
        }
    }

    class SundayDecorator implements DayViewDecorator {
        private Calendar calendar = Calendar.getInstance();

        public boolean shouldDecorate(@Nullable CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        public void decorate(@Nullable DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    class SaturdayDecorator implements DayViewDecorator {
        private Calendar calendar = Calendar.getInstance();

        public boolean shouldDecorate(@Nullable CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        public void decorate(@Nullable DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    class MinMaxDecorator implements DayViewDecorator {

        int maxY, maxM, maxD, minY, minM, minD;

        MinMaxDecorator() {

        }

        MinMaxDecorator(int maxYear, int maxMonth, int maxDay, int minYear, int minMonth, int minDay) {
            maxY = maxYear;
            maxM = maxMonth;
            maxD = maxDay;
            minY = minYear;
            minM = minMonth;
            minD = minDay;
        }

        public boolean shouldDecorate(@Nullable CalendarDay day) {
            if((day.getYear() == maxY && day.getMonth() == maxM - 1 && day.getDay() > maxD) || (day.getYear() == minY && day.getMonth() == minM - 1 && day.getDay() < minD)
                    || (day.getYear() > maxY) || (day.getYear() < minY) || (day.getYear() == maxY && day.getMonth() > maxM - 1) || (day.getYear() == minY && day.getMonth() < minM - 1)) {
                Log.e("달", day.getMonth() + "   " + maxM + "   " + minM);
                return true;
            } else {
                return false;    //트루가 색칠
            }
        }

        public void decorate(@Nullable DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.parseColor("#66D2D2D2")));
            view.setDaysDisabled(true);
        }
    }

    class BoldDecorator implements DayViewDecorator {

        int maxY, maxM, maxD, minY, minM, minD;

        BoldDecorator() {

        }

        BoldDecorator(int maxYear, int maxMonth, int maxDay, int minYear, int minMonth, int minDay) {
            maxY = maxYear;
            maxM = maxMonth;
            maxD = maxDay;
            minY = minYear;
            minM = minMonth;
            minD = minDay;
        }

        public boolean shouldDecorate(@Nullable CalendarDay day) {
            if((day.getYear() == maxY && day.getMonth() == maxM - 1 && day.getDay() > maxD) || (day.getYear() == minY && day.getMonth() == minM - 1 && day.getDay() < minD)
                    || (day.getYear() > maxY) || (day.getYear() < minY) || (day.getYear() == maxY && day.getMonth() > maxM - 1) || (day.getYear() == minY && day.getMonth() < minM - 1)) {
                Log.e("달", day.getMonth() + "   " + maxM + "   " + minM);
                return false;
            } else {
                return true;    //트루가 색칠
            }
        }

        public void decorate(@Nullable DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.2f));
        }
    }

    class DoWorkDecorator implements DayViewDecorator {

        int maxY, maxM, maxD, minY, minM, minD;
        List<String> workList = new ArrayList<>();

        DoWorkDecorator() {

        }

        DoWorkDecorator(int maxYear, int maxMonth, int maxDay, int minYear, int minMonth, int minDay, List<String> workList) {
            maxY = maxYear;
            maxM = maxMonth;
            maxD = maxDay;
            minY = minYear;
            minM = minMonth;
            minD = minDay;
            this.workList = workList;
        }

        public boolean shouldDecorate(@Nullable CalendarDay day) {
            if((day.getYear() == maxY && day.getMonth() == maxM - 1 && day.getDay() > maxD) || (day.getYear() == minY && day.getMonth() == minM - 1 && day.getDay() < minD)
                    || (day.getYear() > maxY) || (day.getYear() < minY) || (day.getYear() == maxY && day.getMonth() > maxM - 1) || (day.getYear() == minY && day.getMonth() < minM - 1)) {
                Log.e("달", day.getMonth() + "   " + maxM + "   " + minM);
                return false;
            } else {
                if(workList.contains(String.format("%04d" ,day.getYear()) + "-" + String.format("%02d" ,(day.getMonth() + 1)) + "-" + String.format("%02d" ,day.getDay()))) {
                    return true;
                } else {
                    return false;
                }   //트루가 색칠
            }
        }

        public void decorate(@Nullable DayViewFacade view) {
            view.addSpan(new DotSpan(3, Color.parseColor("#DBFF00")));
        }
    }

    class DontWorkDecorator implements DayViewDecorator {

        int maxY, maxM, maxD, minY, minM, minD;
        List<String> workList = new ArrayList<>();

        DontWorkDecorator() {

        }

        DontWorkDecorator(int maxYear, int maxMonth, int maxDay, int minYear, int minMonth, int minDay, List<String> workList) {
            maxY = maxYear;
            maxM = maxMonth;
            maxD = maxDay - 1;
            minY = minYear;
            minM = minMonth;
            minD = minDay;
            this.workList = workList;
        }

        public boolean shouldDecorate(@Nullable CalendarDay day) {
            Log.e("사이즈", workList.size() + "");
            if((day.getYear() == maxY && day.getMonth() == maxM - 1 && day.getDay() > maxD) || (day.getYear() == minY && day.getMonth() == minM - 1 && day.getDay() < minD)
                    || (day.getYear() > maxY) || (day.getYear() < minY) || (day.getYear() == maxY && day.getMonth() > maxM - 1) || (day.getYear() == minY && day.getMonth() < minM - 1)) {
                Log.e("달", day.getMonth() + "   " + maxM + "   " + minM);
                return false;
            } else {
                if(workList.contains(String.format("%04d" ,day.getYear()) + "-" + String.format("%02d" ,(day.getMonth() + 1)) + "-" + String.format("%02d" ,day.getDay()))) {
                    return false;
                } else {
                    return true;
                }   //트루가 색칠
            }
        }

        public void decorate(@Nullable DayViewFacade view) {
            view.addSpan(new DotSpan(3, Color.RED));
        }
    }

    class NoWorkDecorator implements DayViewDecorator {

        int maxY, maxM, maxD, minY, minM, minD;

        NoWorkDecorator() {

        }

        NoWorkDecorator(int maxYear, int maxMonth, int maxDay, int minYear, int minMonth, int minDay) {
            maxY = maxYear;
            maxM = maxMonth;
            maxD = maxDay - 1;
            minY = minYear;
            minM = minMonth;
            minD = minDay;
        }

        public boolean shouldDecorate(@Nullable CalendarDay day) {
            if((day.getYear() == maxY && day.getMonth() == maxM - 1 && day.getDay() > maxD) || (day.getYear() == minY && day.getMonth() == minM - 1 && day.getDay() < minD)
                    || (day.getYear() > maxY) || (day.getYear() < minY) || (day.getYear() == maxY && day.getMonth() > maxM - 1) || (day.getYear() == minY && day.getMonth() < minM - 1)) {
                Log.e("달", day.getMonth() + "   " + maxM + "   " + minM);
                return false;
            } else {
                return true;    //트루가 색칠
            }
        }

        public void decorate(@Nullable DayViewFacade view) {
            view.addSpan(new DotSpan(3, Color.RED));
        }
    }

    private void GetData_meas(String str) {

        String uri = str;
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
            myJSON_meas = sb.toString().trim();
            inputIntent_meas();
        } catch (Exception e) {
            failFlag = true;
            e.printStackTrace();
        }
    }

    protected void inputIntent_meas() {
        try {
            JSONArray jsonArray = new JSONArray(new JSONObject(myJSON_meas).getString("result"));

            for(int i  = 0; i < jsonArray.length(); i++) {
                Log.e("측정데이터", jsonArray.getJSONObject(i).toString());
                measList.add(jsonArray.getJSONObject(i));
            }
            /*
            for(int i = 0; i < miniList.size(); i++) {  //틀린값 들어간다.
                //Data_sets.add((ArrayList) miniList.clone());    //이거 없애고 사람수에 따라서 나눠서 넣어라
                if(oneDay)
                    GetData_reps("http://211.253.30.245/equipment/kor/miniplus/Miniplus_RepData_SEL2.php?users_id=" + miniList.get(i).getString("users_id") + "&workout_datetime1=" + strDateList.get(0).toString().trim() + "&workout_datetime2=" + strDateList.get(0).toString().trim());   //31128
                else if(twoDay)
                    GetData_reps("http://211.253.30.245/equipment/kor/miniplus/Miniplus_RepData_SEL2.php?users_id=" + miniList.get(i).getString("users_id") + "&workout_datetime1=" + strDateList.get(0).toString().trim() + "&workout_datetime2=" + strDateList.get(1).toString().trim());   //31128

            }

            if(miniList.size() == 0) {
                phase4.setTextColor(getColor(R.color.red_overlay));
                if (!CSVActivity.this.isFinishing()) {
                    progressDialog.DialogStop();
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }, 0);
            }
            else {
                if(Data_sets.size() != 0) {
                    //CreateCSV();
                    writeExcel();
                } else {
                    if (!CSVActivity.this.isFinishing()) {
                        progressDialog.DialogStop();
                    }
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }, 0);
                }
            }
            */
        } catch (JSONException e) {
            Log.e("튕긴 이유11", e.toString());
            failFlag = true;
            /*
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(miniList.size() == 0)    Toast.makeText(getApplicationContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    else    Toast.makeText(getApplicationContext(), "파일을 생성할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }, 0);

            miniList.removeAll(miniList);
            if (!CSVActivity.this.isFinishing()) {
                //progressDialog2.DialogStop();
                progressDialog.DialogStop();
            }
            phase4.setTextColor(getColor(R.color.red_overlay));
            e.printStackTrace();
            */
        }
    }

    private void GetData_sets(String str) {

        String uri = str;
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
            myJSON_sets = sb.toString().trim();
            inputIntent_sets();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void inputIntent_sets() {
        try {
            JSONArray jsonArray = new JSONArray(new JSONObject(myJSON_sets).getString("result"));

            miniList.add(jsonArray.getJSONObject(0));
            Log.e("미니양", jsonArray.getJSONObject(0).getString("reps"));
            progressMax += Integer.parseInt(jsonArray.getJSONObject(0).getString("reps")) * 2;
            /*
            for(int i = 0; i < miniList.size(); i++) {  //틀린값 들어간다.
                //Data_sets.add((ArrayList) miniList.clone());    //이거 없애고 사람수에 따라서 나눠서 넣어라
                if(oneDay)
                    GetData_reps("http://211.253.30.245/equipment/kor/miniplus/Miniplus_RepData_SEL2.php?users_id=" + miniList.get(i).getString("users_id") + "&workout_datetime1=" + strDateList.get(0).toString().trim() + "&workout_datetime2=" + strDateList.get(0).toString().trim());   //31128
                else if(twoDay)
                    GetData_reps("http://211.253.30.245/equipment/kor/miniplus/Miniplus_RepData_SEL2.php?users_id=" + miniList.get(i).getString("users_id") + "&workout_datetime1=" + strDateList.get(0).toString().trim() + "&workout_datetime2=" + strDateList.get(1).toString().trim());   //31128

            }

            if(miniList.size() == 0) {
                phase4.setTextColor(getColor(R.color.red_overlay));
                if (!CSVActivity.this.isFinishing()) {
                    progressDialog.DialogStop();
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }, 0);
            }
            else {
                if(Data_sets.size() != 0) {
                    //CreateCSV();
                    writeExcel();
                } else {
                    if (!CSVActivity.this.isFinishing()) {
                        progressDialog.DialogStop();
                    }
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }, 0);
                }
            }
            */
        } catch (JSONException e) {
            Log.e("튕긴 이유1", e.toString());

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(miniList.size() == 0)    Toast.makeText(getApplicationContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    else    Toast.makeText(getApplicationContext(), "파일을 생성할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }, 0);

            miniList.removeAll(miniList);
            if (!CSVActivity.this.isFinishing()) {
                //progressDialog2.DialogStop();
                progressDialog.DialogStop();
            }
            phase4.setTextColor(getColor(R.color.red_overlay));
            e.printStackTrace();
        }
    }


    private void GetData_reps_user(String str) {
        class GetDataJSON_user extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... params) {
                String uri = str;
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
                    return sb.toString().trim();
                } catch (Exception e) {
                    e.printStackTrace();
                    return new String("Exception: " + e.getMessage());
                }
            }
            protected void onPostExecute(String result) {
                myJSON_reps = result;
                inputIntent_reps_user();
            }
        }

        GetDataJSON_user getDataJSON_user = new GetDataJSON_user();
        getDataJSON_user.execute(str);
    }

    protected void inputIntent_reps_user() {
        try {
            JSONArray jsonArray = new JSONArray(new JSONObject(myJSON_reps).getString("result"));
            for(int i = 0; i < jsonArray.length(); i++) {
                Data_reps_user.add(jsonArray.getJSONObject(i));
            }
            for(int i = 0; i <Data_reps_user.size(); i++) {
                Log.e("미니미니미니", Data_reps_user.get(i).toString());
                GetData_sets("http://211.253.30.245/equipment/kor/miniplus/Miniplus_SetData_SEL2.php?id=" + Data_reps_user.get(i).getString("miniplus_id"));
            }
            progressDialog2.setMaxprogress(progressMax);

            Data_reps_user.removeAll(Data_reps_user);
            Log.e("미니양2", miniList.size()+"");
            for(int i = 0; i < miniList.size(); i++) {  //틀린값 들어간다.
                //Data_sets.add((ArrayList) miniList.clone());    //이거 없애고 사람수에 따라서 나눠서 넣어라
                GetData_reps("http://211.253.30.245/equipment/kor/miniplus/Miniplus_RepData_SEL2.php?miniplus_id=" + miniList.get(i).getString("id"));   //31128
            }

            if(miniList.size() == 0) {
                phase4.setTextColor(getColor(R.color.red_overlay));
                if (!CSVActivity.this.isFinishing()) {
                    //progressDialog2.DialogStop();
                    progressDialog.DialogStop();
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }, 0);
                Data_sets.removeAll(Data_sets);
            }
            else {
                if(Data_sets.size() != 0) {
                    //CreateCSV();
                    writeExcel();
                } else {
                    if (!CSVActivity.this.isFinishing()) {
                        //progressDialog2.DialogStop();
                        progressDialog.DialogStop();
                    }
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "데이터가 없습니다", Toast.LENGTH_SHORT).show();
                        }
                    }, 0);
                    miniList.removeAll(miniList);
                }
            }
        } catch (JSONException e) {
            Data_sets.removeAll(Data_sets);
            Data_reps.removeAll(Data_reps);
            miniList.removeAll(miniList);
            Data_reps_user.removeAll(Data_reps_user);
            if (!CSVActivity.this.isFinishing()) {
                //progressDialog2.DialogStop();
                progressDialog.DialogStop();
            }
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "데이터를 읽어올 수 없습니다", Toast.LENGTH_SHORT).show();
                }
            }, 0);
            Log.e("여기서 에러 맞쟈!!", "ㅋㅋㄹㅃㅃ");
            e.printStackTrace();
        }
    }

    private void GetData_reps(String str) {
        String uri = str;
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
            myJSON_reps = sb.toString().trim();
            inputIntent_reps();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void inputIntent_reps() {
        try {
            JSONArray jsonArray = new JSONArray(new JSONObject(myJSON_reps).getString("result"));
            for(int i = 0; i < jsonArray.length(); i++) {
                Data_reps.add(jsonArray.getJSONObject(i));
                progressPer++;
                progressDialog2.setData(progressPer);
            }
            Log.e("미니미니", Data_reps.size()+"");
            Data_sets.add((ArrayList) Data_reps.clone());
            Data_reps.removeAll(Data_reps);
        } catch (JSONException e) {
            Data_sets.removeAll(Data_sets);
            Data_reps.removeAll(Data_reps);
            if (!CSVActivity.this.isFinishing()) {
                //progressDialog2.DialogStop();
                progressDialog.DialogStop();
            }
            Log.e("여기서 에러 맞쟈", "ㅋㅋㄹㅃㅃ");
            e.printStackTrace();
        }
    }

    private String GetClubName(String str) {

        String uri = str;
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
            return createClubName(sb.toString().trim());

        } catch (Exception e) {
            e.printStackTrace();
            return "ronfic";
        }
    }
    protected String createClubName(String str) {
        try {
            JSONArray jsonArray = new JSONArray(new JSONObject(str).getString("result"));
            return jsonArray.getJSONObject(0).getString("club_name");
        } catch (JSONException e) {
            return "론픽";
        }
    }

    private String GetMemberName(String str, String value) {

        String uri = str + value;
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
            return createMemberName(sb.toString().trim(), value);

        } catch (Exception e) {
            e.printStackTrace();
            return value;
        }
    }
    protected String createMemberName(String str, String value) {
        try {
            JSONArray jsonArray = new JSONArray(new JSONObject(str).getString("result"));
            return jsonArray.getJSONObject(0).getString("user_name") + "(" + value + ")";
        } catch (JSONException e) {
            return value;
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
        private String filePath;

        public SendMail_Async(Context context, String recipients, String subject, String body, String filePath) {
            this.context = context;
            this.recipients = recipients;
            this.subject = subject;
            this.body = body;
            this.filePath = filePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

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
                /*
                MimeBodyPart bodyPart = new MimeBodyPart();
                Multipart multipart = new MimeMultipart();
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(body, "UTF-8", "html");
                multipart.addBodyPart(textPart);
                */

                //message.setContent(body, "text/html;charset=EUC-KR");

                //message.setDataHandler(handler);
                /*
                for (int i = 0; i < filePath.length; i++) {
                    FileDataSource fileDataSource = new FileDataSource(filePath[i]);
                    bodyPart = new MimeBodyPart();
                    bodyPart.setDataHandler(new DataHandler(fileDataSource));
                    bodyPart.setFileName(fileDataSource.getName());
                    Log.e("파일이름", filePath[i]);
                    multipart.addBodyPart(bodyPart);
                }
                */
                MimeBodyPart bodyPart = new MimeBodyPart();
                Multipart multipart = new MimeMultipart();
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(body, "UTF-8", "html");
                multipart.addBodyPart(textPart);
                //message.setContent(body, "text/html;charset=EUC-KR");

                //message.setDataHandler(handler);

                FileDataSource fileDataSource = new FileDataSource(filePath);
                bodyPart.setDataHandler(new DataHandler(fileDataSource));
                //bodyPart.setFileName(fileDataSource.getName());
                bodyPart.setFileName(MimeUtility.encodeText(fileDataSource.getName(), "euc-kr", "B"));
                multipart.addBodyPart(bodyPart);


                message.setContent(multipart);

                //recipients = "11111@gmail.com,
                if (recipients.indexOf(',') > 0) //여러개 일때 이렇게 한다는데 그냥 for문 돌리는게 좋을듯
                    message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(recipients));
                else
                    message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipients));
                Log.e("파일이름", "ㅋㅋㅋ");

                Transport.send(message);
            } catch (SendFailedException e) {
                mailFail = true;
                Data_sets.removeAll(Data_sets);
                miniList.removeAll(miniList);
                phase4.setTextColor(getColor(R.color.black_overlay));
                phase5.setTextColor(getColor(R.color.red_overlay));
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "이메일을 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }, 0);
            } catch (MessagingException e) {
                mailFail = true;
                Data_sets.removeAll(Data_sets);
                miniList.removeAll(miniList);
                phase4.setTextColor(getColor(R.color.black_overlay));
                phase5.setTextColor(getColor(R.color.red_overlay));
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "인터넷 연결을 확인해주십시오.", Toast.LENGTH_SHORT).show();
                    }
                }, 0);
            } catch (Exception e) {
                mailFail = true;
                Data_sets.removeAll(Data_sets);
                miniList.removeAll(miniList);
                phase4.setTextColor(getColor(R.color.black_overlay));
                phase5.setTextColor(getColor(R.color.red_overlay));
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Data_sets.removeAll(Data_sets);
            miniList.removeAll(miniList);

            setDirEmpty();

            if (!CSVActivity.this.isFinishing()) {
                if(!mailFail) {
                    phase5.setTextColor(getColor(R.color.blue_overlay));
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "메일 전송 완료.", Toast.LENGTH_SHORT).show();
                            PublicValues.configValues.remove(4);
                            PublicValues.configValues.add(4, edtxt.getText().toString());

                            saveItemsToFile();
                        }
                    }, 0);
                } else {
                    mailFail = false;
                }
                if (!CSVActivity.this.isFinishing()) {
                    //progressDialog2.DialogStop();
                    progressDialog.DialogStop();
                }
                phase4.setTextColor(getColor(R.color.black_overlay));
                phase5.setTextColor(getColor(R.color.black_overlay));
            }
            Log.e("비동기 인터넷", "ㅇㅋ");
        }

        public void setDirEmpty() {
            //File dir = new File("sdcard/Download/csvFolder");
            File dir = new File("sdcard/Download/excelFolder");
            File[] childFileList = dir.listFiles();

            if (dir.exists()) {
                for (File childFile : childFileList) {
                    childFile.delete();
                }
            }
        }

        public void saveItemsToFile() {
            SharedPreferences sharedPreferences;
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("serialNumber", PublicValues.configValues.get(0));
            editor.putString("stiction", PublicValues.configValues.get(1));
            editor.putString("lastVersionInfo", PublicValues.configValues.get(2));
            editor.putString("club_id", PublicValues.configValues.get(3));
            editor.putString("email", PublicValues.configValues.get(4));
            editor.apply();


            File file = new File("/sdcard/Download", "mpconfig");
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
    }
}