package main.java.com.marketsoftcommunity.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import main.java.com.marketsoftcommunity.consumers.ApiConsumer;
import main.java.com.marketsoftcommunity.model.exceptions.ApiException;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.mail.Session;
import javax.mail.Transport;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public final class Util {

    private Util() {

    }

    public static void checkConnection() throws IOException, ApiException {
        new ApiConsumer<>().get("");
    }

    public static int NEW_ID(){
        return ThreadLocalRandom.current().nextInt(8388607);
    }

    public static String date(boolean time){
        String format = time ? "MM-dd-yyyy HH:mm:ss" : "MM-dd-yyyy";
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(format);
        return  LocalDateTime.now().format(myFormatObj);
    }


    public static void displayAlert(String dialogText, Alert.AlertType alertType) {
        displayAlert("Alert", dialogText, alertType);
    }

    public static String TIME_ZONE(){
        Calendar now = Calendar.getInstance();

        //get current TimeZone using getTimeZone method of Calendar class
        return now.getTimeZone().getID();
    }

    public static void displayAlert(String header, String dialogText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText(header);
        alert.setContentText(dialogText);
        if (alertType == Alert.AlertType.CONFIRMATION) {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK) {
                throw new RuntimeException("User Declined!");
            }
        } else
            alert.showAndWait();
    }

    public static String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
}
