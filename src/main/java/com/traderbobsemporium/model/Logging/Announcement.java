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
public class Announcement extends Profile {
    private String title;
    private String dialog;
    private String dateTime;

    public Announcement(String title, String dialog) {
        super(Util.NEW_ID(), String.valueOf(SecurityUtils.getSubject()));
        this.title = title;
        this.dialog = dialog;
        this.dateTime = Util.dateTime();
    }

    public Announcement(ResultSet resultSet) throws SQLException {
        super(resultSet.getLong("id"), resultSet.getString("username"));
        title = resultSet.getString("title");
        dialog = resultSet.getString("dialog");
        dateTime = resultSet.getString("dateTime");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "id=" + getId() + '\'' +
                "title='" + title + '\'' +
                ", dialog='" + dialog + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}

