package main.java.com.traderbobsemporium.model.Logging;

import main.java.com.traderbobsemporium.model.Profile;
import main.java.com.traderbobsemporium.util.Util;
import org.apache.shiro.SecurityUtils;

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
    private String name;
    private ActivityType activityType;
    private long affectedId;
    private String affectedName;
    private String dateTime;

    public AccountActivity(ActivityType activityType, Profile profile) {
        this.id = Util.NEW_ID();
        this.name = String.valueOf(SecurityUtils.getSubject().getPrincipal());
        this.activityType = activityType;
        this.affectedName = profile.getName();
        this.affectedId = profile.getId();
        this.dateTime = Util.dateTime();
    }

    public AccountActivity(String name, ActivityType activityType, long affectedItemId, String affectedItemName, String dateTime) {
        this.id = Util.NEW_ID();
        this.name = name;
        this.activityType = activityType;
        this.affectedId = affectedItemId;
        this.affectedName = affectedItemName;
        this.dateTime = dateTime;
    }

    public AccountActivity(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong("id");
        this.name = resultSet.getString("username");
        this.activityType = ActivityType.valueOf(resultSet.getString("activityType"));
        this.affectedId = resultSet.getLong("affectedID");
        this.affectedName = resultSet.getString("affectedName");
        this.dateTime = resultSet.getString("dateTime");
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAffectedId() {
        return affectedId;
    }

    public void setAffectedId(long affectedId) {
        this.affectedId = affectedId;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public String getAffectedName() {
        return affectedName;
    }

    public void setAffectedName(String affectedName) {
        this.affectedName = affectedName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
