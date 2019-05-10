package main.java.com.traderbobsemporium.loggers;
import main.java.com.traderbobsemporium.model.Account;
import main.java.com.traderbobsemporium.model.Camper;
import main.java.com.traderbobsemporium.model.Profile;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import org.apache.shiro.SecurityUtils;
import org.controlsfx.dialog.ExceptionDialog;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class PurchasesActivityLogger {

    /*
    @Override
    public void log(String[] params, ActivityType activityType) {
        try {
            DatabaseUtil.UPDATE("INSERT INTO purchasesactivity VALUES('" + params[0] + "','" + params[1] + "','"
                    + Integer.parseInt(params[2]) + "','" + Integer.parseInt(params[3]) + "','" + params[4] + "::" +
                    activityType.name() + "')");
        } catch (SQLException e) {
            new ExceptionDialog(e).showAndWait();
        }
    }
    */
}
