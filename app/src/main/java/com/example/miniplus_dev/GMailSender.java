package com.example.miniplus_dev;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class GMailSender extends javax.mail.Authenticator {
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    private String sender;
    private Session session;
    private String emailCode;

    public GMailSender(String user, String password, String sender) {
        this.user = user;
        this.password = password;
        this.sender = sender;
        //emailCode = createEmailCode();
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);

        MailcapCommandMap MailcapCmdMap = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        MailcapCmdMap.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        MailcapCmdMap.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        MailcapCmdMap.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        MailcapCmdMap.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        MailcapCmdMap.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(MailcapCmdMap);
    }

    public String getEmailCode() {
        return emailCode;
    }

    private String createEmailCode() {
        String[] str = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String newCode = new String();

        for(int x = 0; x < 8; x++) {
            int random = (int) (Math.random() * str.length);
            newCode += str[random];
        }
        return newCode;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String recipients) throws Exception {
        MimeMessage message = new MimeMessage(session);
        DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
        message.setSender(new InternetAddress(user));
        message.setSubject(subject);
        message.setDataHandler(handler);
        if(recipients.indexOf(',') > 0)
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        else
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
        Transport.send(message);
    }

    public synchronized void sendMailWithFile(String subject, String body, String recipients, String[] filePath) throws Exception {
        MimeMessage message = new MimeMessage(session);
        //DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
        message.setFrom(new InternetAddress(user, sender));
        message.setSubject(subject);
        message.setSentDate(new Date());

        MimeBodyPart bodyPart = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(body, "UTF-8", "html");
        multipart.addBodyPart(textPart);
        //message.setContent(body, "text/html;charset=EUC-KR");

        //message.setDataHandler(handler);

        for(int i = 0; i < filePath.length; i++) {
            FileDataSource fileDataSource = new FileDataSource(filePath[i]);
            bodyPart = new MimeBodyPart();
            bodyPart.setDataHandler(new DataHandler(fileDataSource));
            bodyPart.setFileName(fileDataSource.getName());
            multipart.addBodyPart(bodyPart);
        }
        message.setContent(multipart);

        //recipients = "11111@gmail.com,
        if(recipients.indexOf(',') > 0) //여러개 일때 이렇게 한다는데 그냥 for문 돌리는게 좋을듯
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        else
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));


        Transport.send(message);
    }

    public synchronized void sendMailWithFile(String subject, String body, String recipients, String filePath) throws Exception {
        MimeMessage message = new MimeMessage(session);
        //DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
        message.setFrom(new InternetAddress(user, sender));
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
        if(recipients.indexOf(',') > 0) //여러개 일때 이렇게 한다는데 그냥 for문 돌리는게 좋을듯
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        else
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));


        Transport.send(message);
    }

    public synchronized void sendMailWithFile(String subject, String body, String recipients, String references, String filePath) throws Exception {
        MimeMessage message = new MimeMessage(session);
        //DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
        message.setFrom(new InternetAddress(user, sender));
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
        if(recipients.indexOf(',') > 0) //여러개 일때 이렇게 한다는데 그냥 for문 돌리는게 좋을듯
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
        else
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));

        message.setRecipient(Message.RecipientType.CC, new InternetAddress(references));
        Transport.send(message);
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if(type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}
