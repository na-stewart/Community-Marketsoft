package main.java.com.marketsoftcommunityapi.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Util {
    public static int NEW_ID(){
        return ThreadLocalRandom.current().nextInt(8388607);
    }

    public static String date(boolean time){
        String format = time ? "MM-dd-yyyy HH:mm:ss" : "MM-dd-yyyy";
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern(format);
        return  LocalDateTime.now().format(myFormatObj);
    }


    /*
    public static void displayAlert(String dialogText, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText("Alert!");
        alert.setContentText(dialogText);
        if (alertType == Alert.AlertType.CONFIRMATION){
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK){
                throw new RuntimeException("User Declined!");
            }
        } else
            alert.showAndWait();
    }
    */
}
