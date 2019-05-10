package main.java.com.traderbobsemporium.model;

import main.java.com.traderbobsemporium.loggers.ActivityType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountActivity {
    private String username;
    private String ip;
    private String mac;
    private ActivityType activityType;
    private String affectedItemName;
    private String dateTime;

    public AccountActivity(String username, String ip, String mac, ActivityType activityType, String affectedItemName, String dateTime) {
        this.username = username;
        this.ip = ip;
        this.mac = mac;
        this.activityType = activityType;
        this.dateTime = dateTime;
        this.affectedItemName = affectedItemName;
    }

    public AccountActivity(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            this.username = resultSet.getString("username");
            this.ip = resultSet.getString("ip");
            this.mac = resultSet.getString("mac");
            this.activityType = ActivityType.valueOf(resultSet.getString("activityType"));
            this.affectedItemName = resultSet.getString("affectedItem");
            this.dateTime = resultSet.getString("dateTime");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public String getAffectedItemName() {
        return affectedItemName;
    }

    public void setAffectedItemName(String affectedItemName) {
        this.affectedItemName = affectedItemName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
