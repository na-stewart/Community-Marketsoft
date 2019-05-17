package main.java.com.traderbobsemporium.model.Logging;

import main.java.com.traderbobsemporium.model.Profile;
import main.java.com.traderbobsemporium.util.Util;

import java.net.*;
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

    public AccountActivity(long id, String username, ActivityType activityType, Profile profile) {
        this.id = id;
        this.username = username;
        this.activityType = activityType;
        this.dateTime = Util.dateTime();
        this.affectedItemName = profile.getName();
        this.affectedItemId = profile.getId();
        this.ip = ip();
        this.mac = mac();
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

    private String ip() {
        final DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            return socket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String mac() {
        byte[] mac;
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getByName(ip()));
            mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++)
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            return sb.toString();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
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

    @Override
    public String toString() {
        return "AccountActivity{" +
                "username='" + username + '\'' +
                ", ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                ", activityType=" + activityType +
                ", affectedItemId=" + affectedItemId +
                ", affectedItemName='" + affectedItemName + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
