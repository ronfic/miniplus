package com.example.miniplus_dev;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
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

    public SendMail_Async(Context context, String recipients, String subject, String body, String filePath){
        this.context = context;
        this.recipients = recipients;
        this.subject = subject;
        this.body = body;
        this.filePath = "sdcard/Download/" + filePath;
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

            MimeBodyPart bodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body, "UTF-8", "html");
            multipart.addBodyPart(textPart);
            //message.setContent(body, "text/html;charset=EUC-KR");

            //message.setDataHandler(handler);

            FileDataSource fileDataSource = new FileDataSource(filePath);
            bodyPart.setDataHandler(new DataHandler(fileDataSource));
            bodyPart.setFileName(fileDataSource.getName());
            multipart.addBodyPart(bodyPart);

            message.setContent(multipart);

            //recipients = "11111@gmail.com,
            if (recipients.indexOf(',') > 0) //여러개 일때 이렇게 한다는데 그냥 for문 돌리는게 좋을듯
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));


            Transport.send(message);
        }catch (SendFailedException e) {
            Toast.makeText(context, "이메일을 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        }catch (MessagingException e) {
            Toast.makeText(context, "인터넷 연결을 확인해주십시오.", Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        File file = new File(this.filePath);
        file.delete();
        Log.e("비동기 인터넷", "ㅇㅋ");
    }
}
