package main.java.com.traderbobsemporium.util;

import javafx.scene.control.Alert;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public final class Util {
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
