package com.example.miniplus_dev;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


public class WifiSelectDialog extends Dialog {

    LinearLayout layout = (LinearLayout) findViewById(R.id.layout_custom);
    Context context;
    final int countt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*// 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();

        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);*/
        getWindow().setLayout(800, 600);

        setContentView(R.layout.wifi_select_dialog);
        layout = (LinearLayout) findViewById(R.id.layout_custom);
        for (int i = 0; i < 15; i++) {
            final Button btn = new Button(context);
            //btn.setBackgroundColor(Color.alpha(180));
            btn.setBackgroundColor(Color.BLACK);


            btn.setText("Button" + String.valueOf(i));

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //btn.setBackgroundColor(Color.alpha(0));
                    //btn.setBackgroundColor(Color.RED);
                    btn.setBackgroundColor(Color.YELLOW);
                    Log.i("BTT", "*************TOUCH*************" + btn.getText());
                    Toast.makeText(context, "TOUCH" + btn.getText(), Toast.LENGTH_SHORT).show();
                }
            });

            layout.addView(btn);
        }

        Button cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }


    public WifiSelectDialog(Context context) {
        super(context);
        this.context = context;
    }
}