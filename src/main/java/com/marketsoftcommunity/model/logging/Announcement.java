package main.java.com.marketsoftcommunity.model.logging;

import main.java.com.marketsoftcommunity.model.Model;
import main.java.com.marketsoftcommunity.util.Util;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class Announcement extends Model implements Comparable<Announcement> {
    private String title;
    private String dialog;
    private String dateTime;

    public Announcement(String title, String dialog) {
        super("");
        this.title = title;
        this.dialog = dialog;
        this.dateTime = Util.date(false);
    }


    public Announcement(ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("id"), resultSet.getString("username"));
        title  = resultSet.getString("title");
        dialog = resultSet.getString("dialog");
        dateTime = resultSet.getString("date");
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
                "name='" + getName() + '\'' +
                ", title='" + title + '\'' +
                ", dialog='" + dialog + '\'' +
                ", date='" + dateTime + '\'' +
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

