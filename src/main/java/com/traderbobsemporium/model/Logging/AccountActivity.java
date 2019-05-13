package main.java.com.traderbobsemporium.model.Logging;

import main.java.com.traderbobsemporium.dao.loggers.ActivityType;
import main.java.com.traderbobsemporium.model.Profile;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountActivity {
    private long id;
    private String username;
    private String ip;
    private String mac;
    private ActivityType activityType;
    private long affectedItemId;
    private String affectedItemName;
    private String dateTime;

    public AccountActivity(long id, String username, String ip, String mac, ActivityType activityType, Profile profile, String dateTime) {
        this.id = id;
        this.username = username;
        this.ip = ip;
        this.mac = mac;
        this.activityType = activityType;
        this.dateTime = dateTime;
        this.affectedItemName = profile.getName();
        this.affectedItemId = profile.getId();
    }

    public AccountActivity(ResultSet resultSet) throws SQLException {
            this.username = resultSet.getString("username");
            this.ip = resultSet.getString("ip");
            this.mac = resultSet.getString("mac");
            this.activityType = ActivityType.valueOf(resultSet.getString("activityType"));
            this.affectedItemId = resultSet.getLong("itemID");
            this.affectedItemName = resultSet.getString("itemName");
            this.dateTime = resultSet.getString("dateTime");
            this.affectedItemId = resultSet.getLong("itemID");
            this.affectedItemName = resultSet.getString("itemName");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAffectedItemId() {
        return affectedItemId;
    }

    public void setAffectedItemId(long affectedItemId) {
        this.affectedItemId = affectedItemId;
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
