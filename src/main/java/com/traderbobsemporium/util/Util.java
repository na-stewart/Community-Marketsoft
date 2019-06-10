package main.java.com.traderbobsemporium.util;

import com.sun.deploy.security.UserDeclinedException;
import com.sun.org.apache.regexp.internal.RE;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
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

    public static void displayAlert(String dialogText, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText("Alert!");
        alert.setContentText(dialogText);
        if (alertType == Alert.AlertType.CONFIRMATION){
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK){
                throw new UserDeclinedException("Transaction canceled by user!");
            }
        } else
            alert.showAndWait();

    }
}
