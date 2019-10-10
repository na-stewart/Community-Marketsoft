package com.communitymarketsoftapi.model.logging;

import com.communitymarketsoftapi.model.Model;
import com.communitymarketsoftapi.util.Util;
import org.apache.shiro.SecurityUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountActivity extends Model {
    private ActivityType activityType;
    private int affectedId;
    private String affectedName;
    private String date;

    public AccountActivity(){
        super(null);   }


    public AccountActivity(ActivityType activityType, Model model) {
        super(String.valueOf(SecurityUtils.getSubject().getPrincipal()));
        this.activityType = activityType;
        this.affectedName = model.getName();
        this.affectedId = model.getId();
        this.date = Util.DATE(true);
    }

    public AccountActivity(String name, ActivityType activityType, int affectedItemId, String affectedItemName, String dateTime) {
        super(name);
        this.activityType = activityType;
        this.affectedId = affectedItemId;
        this.affectedName = affectedItemName;
        this.date = dateTime;
    }

    public AccountActivity(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("id"), resultSet.getString("username"));
        this.activityType = ActivityType.valueOf(resultSet.getString("activityType"));
        this.affectedId = resultSet.getInt("affectedID");
        this.affectedName = resultSet.getString("affectedName");
        this.date = resultSet.getString("DATE");
    }

    public int getAffectedId() {
        return affectedId;
    }

    public void setAffectedId(int affectedId) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "AccountActivity{" +
                "name='" + getName() + '\'' +
                ", activityType=" + activityType +
                ", affectedId=" + affectedId +
                ", affectedName='" + affectedName + '\'' +
                ", DATE='" + date + '\'' +
                '}';
    }
}
