package com.example.miniplus_dev;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.awt.font.TextAttribute;

public class initDialog {
    private Context context;
    Dialog dialog;
    LinearLayout initLay;



    public initDialog(Context context) {
        this.context = context;
    }

    public void initDialogStart() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = null;
        }
        ///////////////////Dialog Setting/////////////////////////////////////////////////
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.init_process);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        initLay = dialog.findViewById(R.id.init_dialog);

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

        initLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        //////////////////////////////////////////////////////////////////////////////////

        ////////Alert Dialog Full Screen Setting//////////////////////
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
        //////////////////////////////////////////////////////////////
/*
        final int AUTO_DISMISS_MILLIS = 5000;

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                new CountDownTimer(AUTO_DISMISS_MILLIS, 100){
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        dialog.dismiss();
                    }
                }.start();
            }
        });
        dialog.show();*/
        try {
            if(!dialog.isShowing()) dialog.show();
        }catch (Exception e){
            Log.e("initDia", "ERR = " + e);
        }

    }

    public void initDialogStop() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog = null;

    }
}
