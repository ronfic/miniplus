package com.example.miniplus_dev;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressDialog2 {
    private Context context;
    Dialog dialog;
    int dataCnt;
    LinearLayout initLay;
    ProgressBar progressBar;
    TextView txt;


    public ProgressDialog2(Context context) {
        this.context = context;
    }

    public void DialogStart() {
        ///////////////////Dialog Setting/////////////////////////////////////////////////
        if(dialog == null){
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_custom_progress2);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            initLay = dialog.findViewById(R.id.init_dialog);
            progressBar = (ProgressBar) dialog.findViewById(R.id.progress);

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

/*            initLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });*/
            //////////////////////////////////////////////////////////////////////////////////

            ////////Alert Dialog Full Screen Setting//////////////////////
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
            //////////////////////////////////////////////////////////////


            if(!dialog.isShowing()){
                dialog.show();
            }

        }

    }

    public void DialogStop() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }
        }

    }

    public void setData(int a) {
        if(dialog != null) {
            if(dialog.isShowing()) {
                progressBar.setProgress(a);
            }
        }
    }

    public void setMaxprogress(int a) {
        if(dialog != null) {
            if(dialog.isShowing()) {
                progressBar.setMax(a);
            }
        }
    }
}
