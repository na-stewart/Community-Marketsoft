package main.java.com.traderbobsemporium.model.Logging;

import main.java.com.traderbobsemporium.model.Profile;
import main.java.com.traderbobsemporium.util.Util;
import org.apache.shiro.SecurityUtils;

import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AccountActivity extends Profile {
    private String ip;
    private String mac;
    private ActivityType activityType;
    private long affectedId;
    private String affectedName;
    private String dateTime;

    public AccountActivity(ActivityType activityType, Profile profile) {
        super(Util.NEW_ID(), String.valueOf(SecurityUtils.getSubject().getPrincipal()));
        this.activityType = activityType;
        this.dateTime = Util.dateTime();
        this.affectedName = profile.getName();
        this.affectedId = profile.getId();
        this.ip = ip();
        this.mac = mac();
    }

    public AccountActivity(String name, String ip, String mac, ActivityType activityType, long affectedItemId, String affectedItemName, String dateTime) {
        super(Util.NEW_ID(), name);
        this.ip = ip;
        this.mac = mac;
        this.activityType = activityType;
        this.affectedId = affectedItemId;
        this.affectedName = affectedItemName;
        this.dateTime = dateTime;
    }

    public AccountActivity(ResultSet resultSet) throws SQLException {
        super(resultSet.getLong("id"), resultSet.getString("username"));
        this.ip = resultSet.getString("ip");
        this.mac = resultSet.getString("mac");
        this.activityType = ActivityType.valueOf(resultSet.getString("activityType"));
        this.affectedId = resultSet.getLong("affectedID");
        this.affectedName = resultSet.getString("affectedName");
        this.dateTime = resultSet.getString("dateTime");
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


    public long getAffectedId() {
        return affectedId;
    }

    public void setAffectedId(long affectedId) {
        this.affectedId = affectedId;
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

    @Override
    public String toString() {
        return "AccountActivity{" +
                "id=" + getId() + '\'' +
                "username='" + getName() + '\'' +
                ", ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                ", activityType=" + activityType +
                ", affectedId=" + affectedId +
                ", affectedName='" + affectedName + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
