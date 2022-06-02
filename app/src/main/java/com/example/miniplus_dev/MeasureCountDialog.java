package com.example.miniplus_dev;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MeasureCountDialog {
    private Context context;
    Dialog dialog;
    public static boolean DialogClosed = false;

    public MeasureCountDialog(Context context) {
        this.context = context;
    }

    long oldTime;
    TextView txtCount;
    Handler mHandlerCount;
    int sel;
    Timer TimeCalDisp;




    public void initDialogStart(int seL) {
        ///////////////////Dialog Setting/////////////////////////////////////////////////
        DialogClosed = false;
        sel = seL;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.measure_count_dialog);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //////////////////////////////////////////////////////////////////////////////////

        ////////Alert Dialog Full Screen Setting//////////////////////
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.6f;
        //dialog.getWindow().setLayout(800, 600);
        dialog.getWindow().setAttributes(params);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);


        txtCount = (TextView) dialog.findViewById(R.id.measure_count_dialog);


        TimeCalDisp = new Timer();

        oldTime = System.currentTimeMillis();
        mHandlerCount = new Handler(Looper.getMainLooper());

        TimerTask TimeCal = new TimerTask() {
            @Override
            public void run() {
                mHandlerCount.post(new Runnable() {
                    @Override
                    public void run() {

                        long now = System.currentTimeMillis();

                        Date date = new Date(now);
                        Date oldDate = new Date(oldTime);
                        long diffTime = Math.abs(date.getTime() - oldDate.getTime());
                        long secs = (int) (diffTime / 1000) % 60;

                        if (sel == 0) {
                            if (secs < 7) txtCount.setText("" + (7 - secs));
                            else txtCount.setText("S T A R T");
                            if (secs == 8) initDialogStop();
                        }
                        if (sel == 1) {
                            if (secs <= 1) txtCount.setText("RIGHT\nReady");
                            else if (secs > 1 && secs < 7) txtCount.setText("Right\n" + (7 - secs));
                            else txtCount.setText("S T A R T");
                            if (secs == 8) initDialogStop();
                        }
                        if (sel == 2) {
                            if (secs <= 1) txtCount.setText("LEFT\nReady");
                            else if (secs > 1 && secs < 10)
                                txtCount.setText("Left\n" + (10 - secs));
                            else txtCount.setText("S T A R T");
                            if (secs == 11) initDialogStop();
                        }

                    }
                });
            }
        };

        TimeCalDisp.schedule(TimeCal, 0, 100);


        dialog.show();
    }



    public void initDialogStop() {
        DialogClosed = true;
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
        if(TimeCalDisp != null){
            TimeCalDisp.cancel();
            TimeCalDisp = null;
        }
    }
}
