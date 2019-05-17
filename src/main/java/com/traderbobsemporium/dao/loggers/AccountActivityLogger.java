package main.java.com.traderbobsemporium.dao.loggers;
import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;
import main.java.com.traderbobsemporium.model.Logging.ActivityType;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.Util;
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
public class AccountActivityLogger implements Logger<AccountActivity> {

    @Override
    public void log(AccountActivity accountActivity) throws SQLException {
        DatabaseUtil.UPDATE("INSERT INTO accountactivity VALUES('" + accountActivity.getId() + "','" +
                accountActivity.getUsername() + "','" + accountActivity.getIp() + "','" + accountActivity.getId() + "','" +
                accountActivity.getActivityType() + "','" + accountActivity.getAffectedItemId() + "','" +
                accountActivity.getUsername() + "','" + Util.dateTime() + "')");
    }

    @Override
    public List<AccountActivity> getLogs() throws SQLException {
        List<AccountActivity> accountActivities = new ArrayList<>();
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("accountactivity");
        while (resultSet.next())
            accountActivities.add(new AccountActivity(resultSet));
        resultSet.close();
        return accountActivities;
    }
}
