package com.communitymarketsoftapi.util;

import com.communitymarketsoftapi.model.Account;
import com.communitymarketsoftapi.repository.getOnlyRepo;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.shiro.SecurityUtils;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public final class Util {

    private Util() {

    }


    public static void SEND_MAIL(String recepient, String messageString, String subject) throws MessagingException {
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.zoho.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("aidan.stewart@communitymarketsoft.com",  System.getenv("MAIL_PASSWORD"));
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("aidan.stewart@communitymarketsoft.com"));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(recepient));
        message.setSubject(subject);
        message.setText(messageString);

        Transport.send(message);


    }

    public static int NEW_ID(){
        return ThreadLocalRandom.current().nextInt(8388607);
    }

    public static String DATE(boolean time){
        String format = time ? "MM-dd-yyyy HH:mm:ss" : "MM-dd-yyyy";
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(AccountUtil.TIME_ZONE()));
        Date currentDate = calendar.getTime();
        DateFormat df = new SimpleDateFormat(format);
        return  df.format(currentDate);
    }

}
