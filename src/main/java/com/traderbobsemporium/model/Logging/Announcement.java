package main.java.com.traderbobsemporium.model.Logging;

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

// TODO: 6/13/2019 Turn into logger or make name title. 
public class Announcement implements Comparable<Announcement> {
    private long id;
    private String name;
    private String title;
    private String dialog;
    private String dateTime;

    public Announcement(String title, String dialog) {
        this.id = Util.NEW_ID();
        this.name = String.valueOf(SecurityUtils.getSubject().getPrincipal());
        this.title = title;
        this.dialog = dialog;
        this.dateTime = Util.dateTime();
    }


    public Announcement(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong("id");
        this.name = resultSet.getString("username");
        title  = resultSet.getString("title");;
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

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "id=" + id + '\'' +
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

