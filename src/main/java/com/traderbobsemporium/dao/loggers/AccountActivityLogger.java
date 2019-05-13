package main.java.com.traderbobsemporium.dao.loggers;
import main.java.com.traderbobsemporium.model.AccountRole;
import main.java.com.traderbobsemporium.model.Logging.AccountActivity;
import main.java.com.traderbobsemporium.model.Profile;
import main.java.com.traderbobsemporium.util.DatabaseUtil;
import main.java.com.traderbobsemporium.util.Util;
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
    public void log(Profile profile, ActivityType activityType) {
        try {
            DatabaseUtil.UPDATE("INSERT INTO accountactivity VALUES('" + Util.NEW_ID +  "','" +
                    SecurityUtils.getSubject().getPrincipal() + "','" + ip() + "','" + mac() + "','" +
                    activityType.name() + "','" + profile.getId() + "','" + profile.getName() + "','" + dateTime() + "')");
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
    public AccountActivity get(long id) throws SQLException {
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("accountactivity WHERE id =" + id);
        AccountActivity accountActivity = new AccountActivity(resultSet);
        resultSet.close();
        return accountActivity;
    }

    @Override
    public List<AccountActivity> getAll() throws SQLException {
        List<AccountActivity> accountActivities = new ArrayList<>();
        ResultSet resultSet = DatabaseUtil.REQUEST_RESULT_SET("accountactivity");
        while (resultSet.next())
            accountActivities.add(new AccountActivity(resultSet));
        resultSet.close();
        return accountActivities;
    }

    @Override
    public void update(AccountActivity accountActivity, String[] params) throws SQLException, MalformedURLException {
        accountActivity.setUsername(params[0]);
        accountActivity.setIp(params[1]);
        accountActivity.setMac(params[2]);
        accountActivity.setActivityType(ActivityType.valueOf(params[3]));
        accountActivity.setAffectedItemId(Long.parseLong(params[4]));
        accountActivity.setAffectedItemName(params[5]);
        accountActivity.setDateTime(params[6]);
        DatabaseUtil.UPDATE("UPDATE account SET username = '" + accountActivity.getUsername() + "'," +
                "ip = '" + accountActivity.getIp() + "'," +
                "mac = '" + accountActivity.getMac() + "'," +
                "itemID = '" + accountActivity.getAffectedItemId() + "'," +
                "itemName = '" + accountActivity.getAffectedItemName() + "'," +
                "activityType = '" + accountActivity.getActivityType() + "'," +
                "dateTime = '" + accountActivity.getDateTime() + "','" +
                " WHERE id =" + accountActivity.getId() + ";");
    }

    @Override
    public void add(AccountActivity accountActivity) throws SQLException {
        throw new UnsupportedOperationException("Add method unsupported. Use Log instead.");
    }

    @Override
    public void delete(long id) throws SQLException {
        DatabaseUtil.UPDATE("DELETE FROM accountactivity WHERE id = '" + id  + "'");
    }
}
