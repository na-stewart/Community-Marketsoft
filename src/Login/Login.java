package Login;

import Data.ID;
import Encryption.Crypto;
import Main.Main;
import Manager.MonoQuery;
import Manager.DatabaseManager;
import javafx.scene.control.Alert;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class Login implements MonoQuery {
    private DatabaseManager manager = new DatabaseManager();
    private String user;
    private String pass;
    private Crypto crypto = new Crypto();

    public Login(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    private void displayAlertDialog(Alert.AlertType alertType, String context){
        Alert alert = new Alert(alertType);
        alert.setContentText(context);
        alert.showAndWait();
    }

    private String alertContexts(boolean loggingIn){
        if (loggingIn)
            return "Failure to login is due to one the reasons below:" +
                    "\n-Incorrect username or password!" +
                    "\n-Mac address of account does not match the one of current system!" +
                    "\n-your account had not been enabled yet!";
        else
            return "Your account is now registered. Please wait for an admin to enable your account.";
    }

    @Override
    public void updateDatabase() throws SQLException {
        String query = "INSERT INTO employee VALUES('" + new ID().getId() + "','" +
                user + "','" +
                crypto.tryToEncrypt("key", pass) + "','" +
                '3' + "','" +
                getMacAddress() +"')" ;
        manager.update(query);
        displayAlertDialog(Alert.AlertType.INFORMATION, alertContexts(false));
    }

    @Override
    public void retrieveDatabaseData() throws SQLException {
        String query = "SELECT * FROM employee WHERE username =" +
                "'" + user + "' AND password=" +
                "'" + crypto.tryToEncrypt("key", pass) + "'";
            ResultSet resultSet = manager.receiver(query);
            if (canLogin(resultSet)) {
                Main.mainStage.close();
            } else
                displayAlertDialog(Alert.AlertType.ERROR, alertContexts(true));
            resultSet.close();
    }

    private boolean canLogin(ResultSet resultSet) throws SQLException {
        return resultSet.next()
                && resultSet.getInt(4) != 3
                && resultSet.getString(5).equals(getMacAddress()) ;
    }

    private String getMacAddress() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++)
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
