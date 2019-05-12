package main.java.com.traderbobsemporium.loggers;
import main.java.com.traderbobsemporium.model.AccountActivity;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import org.apache.shiro.SecurityUtils;
import org.controlsfx.dialog.ExceptionDialog;

import java.net.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
class AccountActivityLogger implements Logger<AccountActivity> {

    @Override
    public void log(String[] params, ActivityType activityType) {
        try {
            DatabaseUtil.UPDATE("INSERT INTO accountactivity VALUES('" + SecurityUtils.getSubject().getPrincipal()
                    + "','" + ip() + "','" + mac() + "','" + activityType.name() + "','" + params[0] + "::" +
                    params[1] + "','" + dateTime() + "')");
        } catch (SQLException | UnknownHostException | SocketException e) {
            new ExceptionDialog(e).showAndWait();
        }
    }
    private String ip() throws UnknownHostException, SocketException {
        final DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
        return socket.getLocalAddress().getHostAddress();
    }

    private String mac() throws SocketException, UnknownHostException {
        NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getByName(ip()));
        byte[] mac = network.getHardwareAddress();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++)
            sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        return sb.toString();
    }

    private String dateTime(){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        return  LocalDateTime.now().format(myFormatObj);
    }

    @Override
    public List<AccountActivity> getLogs() {
        List<AccountActivity> accountActivities = new ArrayList<>();
        try {
            ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("accountactivity");
            while (resultSet.next())
                accountActivities.add(new AccountActivity(resultSet));
            resultSet.close();
            return accountActivities;
        } catch (SQLException e) {
            new ExceptionDialog(e).showAndWait();
        }
        return null;
    }
}
