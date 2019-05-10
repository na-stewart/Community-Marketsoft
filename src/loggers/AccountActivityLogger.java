package loggers;

import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import main.java.com.traderbobsemporium.model.Profile;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.controlsfx.dialog.ExceptionDialog;

import java.net.*;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
class AccountActivityLogger implements Logger {

    @Override
    public void log(Profile profile, ActivityType activityType) {
        try {
            DatabaseUtil.UPDATE("INSERT INTO accountactivity VALUES('" + SecurityUtils.getSubject().getPrincipal() +
                    "','" + ip() + "','" + mac() + "','" + activityType.name() + "','" + profile.getName() + ":" +
                    profile.getId() + "','" + dateTime() + "')");
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
}
