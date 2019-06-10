package main.java.com.traderbobsemporium.dao.Loggers;
import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;
import main.java.com.traderbobsemporium.model.Logging.ActivityType;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountActivityLogger implements DAO<AccountActivity> {

    @Override
    public AccountActivity get(long id) throws SQLException {
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("accountactivity WHERE id =" + id);
        AccountActivity accountActivity = null;
        if (resultSet.next())
             accountActivity = new AccountActivity(resultSet);
        resultSet.close();
        return accountActivity;
    }

    @Override
    public List<AccountActivity> getAll() throws SQLException {
        List<AccountActivity> accountActivities = new ArrayList<>();
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("accountactivity");
        while (resultSet.next())
            accountActivities.add(new AccountActivity(resultSet));
        resultSet.close();
        return accountActivities;
    }

    @Override
    public void updateAll(AccountActivity accountActivity, String[] params) {
        if (!params[0].isEmpty())
            accountActivity.setName(params[0]);
        if (!params[1].isEmpty())
            accountActivity.setIp(params[1]);
        if (!params[2].isEmpty())
            accountActivity.setMac(params[2]);
        if (!params[3].isEmpty())
            accountActivity.setActivityType(ActivityType.valueOf(params[3]));
        if (!params[4].isEmpty())
            accountActivity.setAffectedId(Long.parseLong(params[4]));
        if (!params[5].isEmpty())
            accountActivity.setAffectedName(params[5]);
        if (!params[6].isEmpty())
            accountActivity.setDateTime(params[6]);
        if (!params[7].isEmpty())
            accountActivity.setSuccessful(Boolean.parseBoolean(params[7].toLowerCase()));
        update(accountActivity);
    }

    @Override
    public void update(AccountActivity updated) {
        DatabaseUtil.UPDATE("UPDATE accountactivity SET username = '" + updated.getName() + "'," +
                "ip = '" + updated.getIp() + "'," +
                "mac = '" + updated.getMac() + "'," +
                "activityType = '" + updated.getActivityType().name() + "'," +
                "affectedID = '" + updated.getAffectedId() + "'," +
                "affectedName = '" + updated.getAffectedName() + "'," +
                "dateTime = '" + updated.getDateTime() + "'," +
                "successful = '" +  booleanAsInt(updated.isSuccessful()) + "'" +
                " WHERE id =" + updated.getId() + ";");
    }


    @Override
    public void add(AccountActivity accountActivity) {
        DatabaseUtil.UPDATE("INSERT INTO accountactivity VALUES(' "+ accountActivity.getId() +  "','" +
                accountActivity.getName() + "','" + accountActivity.getIp() + "','" + accountActivity.getMac() +
                "','" + accountActivity.getActivityType() + "','" + accountActivity.getAffectedId() + "','" +
                accountActivity.getAffectedName() + "','" + Util.dateTime() + "','" + booleanAsInt(accountActivity.isSuccessful()) + "')");
    }

    @Override
    public void delete(long id) {
        DatabaseUtil.UPDATE("DELETE FROM accountactivity WHERE id = '" + id  + "'");
    }

    private int booleanAsInt(boolean bool){
         return bool ? 1 : 0;
    }
}
