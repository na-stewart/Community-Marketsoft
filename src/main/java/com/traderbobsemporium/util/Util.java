package main.java.com.traderbobsemporium.util;

import javafx.scene.control.Alert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public final class Util {
    private static final Random random = new Random();

    private Util() {

    }

    public static long NEW_ID(){
        return Math.abs(random.nextLong());
    }

    public static String dateTime(){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        return  LocalDateTime.now().format(myFormatObj);
    }


    public static void displayError(String dialogText, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText("Alert!");
        alert.setContentText(dialogText);
        alert.showAndWait();
    }
}
