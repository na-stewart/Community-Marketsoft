package main.java.com.traderbobsemporium.dao.loggers;

import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Logging.PurchasesActivity;
import main.java.com.traderbobsemporium.util.DatabaseUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class PurchasesActivityLogger implements Logger<PurchasesActivity> {
    @Override
    public void log(PurchasesActivity purchasesActivity) throws SQLException {

    }

    @Override
    public ArrayList<PurchasesActivity> getLogs() throws SQLException {
        ArrayList<PurchasesActivity> purchases = new ArrayList<>();
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("purchasesactivity");
        while (resultSet.next())
            purchases.add(new PurchasesActivity(resultSet));
        resultSet.close();
        return purchases;
    }

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
