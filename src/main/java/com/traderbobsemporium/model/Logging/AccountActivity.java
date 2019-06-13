package main.java.com.traderbobsemporium.model.Logging;

import main.java.com.traderbobsemporium.model.DataObject;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.Util;
import org.apache.shiro.SecurityUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    private String name;
    private String ip;
    private String mac;
    private ActivityType activityType;
    private long affectedId;
    private String affectedName;
    private String dateTime;

    public AccountActivity(ActivityType activityType, DataObject profile) {
        this.id = Util.NEW_ID();
        this.name = String.valueOf(SecurityUtils.getSubject().getPrincipal());
        this.activityType = activityType;
        this.dateTime = Util.dateTime();
        this.affectedName = profile.getName();
        this.affectedId = profile.getId();
        this.ip = externalIp();
        this.mac = mac();
    }



    public AccountActivity(String name, String ip, String mac, ActivityType activityType, long affectedItemId, String affectedItemName, String dateTime) {
        this.id = Util.NEW_ID();
        this.name = name;
        this.ip = ip;
        this.mac = mac;
        this.activityType = activityType;
        this.affectedId = affectedItemId;
        this.affectedName = affectedItemName;
        this.dateTime = dateTime;
    }

    public AccountActivity(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong("id");
        this.name = resultSet.getString("username");
        this.ip = resultSet.getString("ip");
        this.mac = resultSet.getString("mac");
        this.activityType = ActivityType.valueOf(resultSet.getString("activityType"));
        this.affectedId = resultSet.getLong("affectedID");
        this.affectedName = resultSet.getString("affectedName");
        this.dateTime = resultSet.getString("dateTime");
    }

    private String externalIp(){
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            return in.readLine();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
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
                ", ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                ", activityType=" + activityType +
                ", affectedId=" + affectedId +
                ", affectedName='" + affectedName + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
