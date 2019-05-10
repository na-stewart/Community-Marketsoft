package main.java.com.traderbobsemporium.util;

import javafx.scene.control.Alert;

import java.util.Random;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public final class Util {
    public static final long NEW_ID = Math.abs(new Random().nextLong());

    private Util() {

    }

    public static void displayError(String dialogText, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText("Alert!");
        alert.setContentText(dialogText);
        alert.showAndWait();
    }
}
