package main.java.com.traderbobsemporium.dao;
import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;
import main.java.com.traderbobsemporium.model.Logging.ActivityType;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.Util;

import java.net.MalformedURLException;
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
    public void update(AccountActivity accountActivity, String[] params) throws SQLException {
        accountActivity.setName(params[0]);
        accountActivity.setIp(params[1]);
        accountActivity.setMac(params[2]);
        accountActivity.setActivityType(ActivityType.valueOf(params[3]));
        accountActivity.setAffectedId(Long.parseLong(params[4]));
        accountActivity.setAffectedName(params[5]);
        accountActivity.setDateTime(params[6]);
        DatabaseUtil.UPDATE("UPDATE account SET username = '" + accountActivity.getName() + "'," +
                "ip = '" + accountActivity.getIp() + "'," +
                "mac = '" + accountActivity.getMac() + "'," +
                "activityType = '" + accountActivity.getActivityType().name() + "'," +
                "affectedID = '" + accountActivity.getAffectedId() + "'," +
                "affectedName = '" + accountActivity.getAffectedName() + "'," +
                "dateTime = '" + accountActivity.getDateTime() + "'" +
                " WHERE id =" + accountActivity.getId() + ";");
    }


    @Override
    public void add(AccountActivity accountActivity) throws SQLException {
        DatabaseUtil.UPDATE("INSERT INTO accountactivity VALUES(' "+ accountActivity.getId() +  "','" +
                accountActivity.getName() + "','" + accountActivity.getIp() + "','" + accountActivity.getMac() +
                "','" + accountActivity.getActivityType() + "','" + accountActivity.getAffectedId() + "','" +
                accountActivity.getAffectedName() + "','" + Util.dateTime() + "')");
    }

    @Override
    public void delete(long id) throws SQLException {
        DatabaseUtil.UPDATE("DELETE FROM accountactivity WHERE id = '" + id  + "'");
    }
}
