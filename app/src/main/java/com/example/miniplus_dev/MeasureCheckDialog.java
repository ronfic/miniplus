package com.example.miniplus_dev;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MeasureCheckDialog {
    private Context context;
    Dialog dialog;

    TextView txtText;


    public MeasureCheckDialog(Context context) {
        this.context = context;
    }


    public void DialogStart(int sel) {
        ///////////////////Dialog Setting/////////////////////////////////////////////////

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.measure_check_dialog);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //////////////////////////////////////////////////////////////////////////////////

        ////////Alert Dialog Full Screen Setting//////////////////////
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        //params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.9f;
        dialog.getWindow().setLayout(800, 300);
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

        //////////////////////////////////////////////////////////////////////////////////

        txtText = (TextView) dialog.findViewById(R.id.measure_txt_check_dialog);

        if (sel == 0 || sel == 3) {
            if(sel == 0) {
                txtText.setText("측정이 완료 되었습니다.\n다음 검사를 진행하여주세요.");
            } else if(sel == 3) {
                txtText.setText("데이터를 저장하지 못했습니다.\n인터넷 연결을 확인해주세요.");
            }
            txtText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();

                }
            }, 4000);
        } else if (sel == 1) {
            txtText.setText("금일 데이터가 존재하지 않습니다.\n금일 검사를 완료하여주세요.");
        } else {
            txtText.setText("데이터를 불러오는 중입니다..\n장비를 조작하지 마세요.");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }





/*

    long oldTime;
    Timer timeCalDisp = new Timer();
    Handler mHandlerCount;
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

                        if(secs == 8)   initDialogStop();
                    }
                });
            }
        };

        timeCalDisp.schedule(TimeCal,0,100);*/

        dialog.show();
    }

    public void DialogStop() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
