package com.example.miniplus_dev;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail extends AppCompatActivity {
    String user = "ronfic.co@gmail.com";
    String password = "rf-00245";
    String sender = "론픽";
    //String[] file = {"sdcard/Download/screenshot.png", "sdcard/Download/data.ini"};

    public void sendSecurityCode(Context context, String subject, String body, String sendTo, String fileName) {
        try {
            Log.e("ㅋㅋㅋㅋㅋㅋㅋㅋ",fileName);
            GMailSender gMailSender = new GMailSender(user, password, sender);
            gMailSender.sendMailWithFile(subject, body, sendTo, "sdcard/Download/" + fileName);
            Toast.makeText(context, "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
            File file = new File("sdcard/Download/" + fileName);
            file.delete();
        } catch (SendFailedException e) {
            Toast.makeText(context, "이메일을 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            File file = new File("sdcard/Download/" + fileName);
            file.delete();
        } catch (MessagingException e) {
            Toast.makeText(context, "인터넷 연결을 확인해주십시오.", Toast.LENGTH_SHORT).show();
            File file = new File("sdcard/Download/" + fileName);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            File file = new File("sdcard/Download/" + fileName);
            file.delete();
        }
    }

    public void sendSecurityCode(Context context, String subject, String body, String sendTo, String sendRef, String fileName) {
        try {
            Log.e("ㅋㅋㅋㅋㅋㅋㅋㅋ","ㅋㅋㅋㅋㄹㅃㅃㅃㅃ");
            GMailSender gMailSender = new GMailSender(user, password, sender);
            gMailSender.sendMailWithFile(subject, body, sendTo, sendRef, "sdcard/Download/" + fileName);
            Toast.makeText(context, "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
        } catch (SendFailedException e) {
            Toast.makeText(context, "이메일을 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            Toast.makeText(context, "인터넷 연결을 확인해주십시오.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
