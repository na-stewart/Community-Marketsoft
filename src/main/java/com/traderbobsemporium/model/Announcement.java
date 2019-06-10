package main.java.com.traderbobsemporium.model;

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
public class Announcement extends DataObject implements Comparable<Announcement> {
    private String title;
    private String dialog;
    private String dateTime;

    public Announcement(String title, String dialog) {
        super(Util.NEW_ID(), String.valueOf(SecurityUtils.getSubject().getPrincipal()));
        this.title = title;
        this.dialog = dialog;
        this.dateTime = Util.dateTime();
    }

    public Announcement(String name, String title, String dialog, String dateTime) {
        super(Util.NEW_ID(), name);
        this.title = title;
        this.dialog = dialog;
        this.dateTime = dateTime;
    }

    public Announcement(ResultSet resultSet) throws SQLException {
        super(resultSet.getLong("id"), resultSet.getString("username"));
        title  = resultSet.getString("title");
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

    @Override
    public int compareTo(Announcement o) {
        if (dateTime == null || o.dateTime == null) {
            return 0;
        }
        return getDateTime().compareTo(o.getDateTime());
    }
}

