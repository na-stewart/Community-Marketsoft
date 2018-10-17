package Login;

import AccountPerms.AccountTypePerms;
import DataSource.DataSource;
import GUILoader.GUI;
import Main.Main;
import Manager.Database;
import Manager.DatabaseManager;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class LoginPanel implements Database {
    private DatabaseManager manager = new DatabaseManager();
    private String user;
    private String pass;

    public LoginPanel (String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    @Override
    public void receiveDatabaseData() throws SQLException {
        String query = "SELECT * FROM employee WHERE username ='" + user + "' AND password='" + pass + "'";
        ResultSet resultSet = manager.receiver(query);
        if (resultSet.next())
            if (!resultSet.getBoolean(5))
                Main.mainStage.close();
        else
            displayAlertDialog(Alert.AlertType.ERROR, alertContexts(true));
        resultSet.close();
    }

    @Override
    public void updateDatabaseData() throws SQLException {
        //String query = "";
      //  manager.update(query);
        displayAlertDialog(Alert.AlertType.INFORMATION,  alertContexts(false));
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
                    "\n-A user is already logged in!" +
                    "\n-your account is not registered!";
        else
            return "Your account is now registered. Please wait for an admin to enable you account.";
    }
}
