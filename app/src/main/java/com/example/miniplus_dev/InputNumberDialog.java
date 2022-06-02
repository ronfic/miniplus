package com.example.miniplus_dev;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InputNumberDialog {
    private Context context;
    Dialog dialog;

    public InputNumberDialog(Context context) {
        this.context = context;
    }

    TextView txtTitle;

    public Button btnOk, btnCancle;

    public EditText inputNum;

    //public static int num;


    public void initDialogStart(boolean sel) {
        ///////////////////Dialog Setting/////////////////////////////////////////////////
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.input_number_dialog);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //////////////////////////////////////////////////////////////////////////////////

        ////////Alert Dialog Full Screen Setting//////////////////////
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        //params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.8f;
        dialog.getWindow().setLayout(800, 300);
        dialog.getWindow().setAttributes(params);

        //////////////////////////////////////////////////////////////////////////////////

        txtTitle = (TextView) dialog.findViewById(R.id.txt_input_dialog_title);
        btnOk = (Button) dialog.findViewById(R.id.btn_input_ok);
        btnCancle = (Button) dialog.findViewById(R.id.btn_input_cancle);

        inputNum = (EditText) dialog.findViewById(R.id.input_number);


        if (sel) {
            txtTitle.setText("본인의 몸무게를 입력해 주세요.");
            inputNum.setHint("(ex: 62, 103, 77");
        } else {
            txtTitle.setText("본인의 키를 입력해 주세요.");
            inputNum.setHint("(ex: 169, 156, 183");
        }

/*        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = inputNum.getText().toString().trim();
                Log.e("success", "올바른입력 : " + str);
                Log.e("success", "올바른입력 index of(0): " + str.charAt(0));

                if(str.charAt(0) == '0'){
                    Log.e("success", "조건 true");
                    inputNum.setText(null);
                    inputNum.setHint("잘못 된 입력입니다.");
                    inputNum.setHintTextColor(Color.RED);
                }
                else{
                    Log.e("success", "조건 false");
                    num = Integer.parseInt(str);
                    initDialogStop();
                }
            }
        });*/
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDialogStop();
            }
        });

        dialog.show();
    }

    public void initDialogStop() {
        dialog.dismiss();
    }
}
