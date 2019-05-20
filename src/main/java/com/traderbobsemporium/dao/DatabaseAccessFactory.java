package main.java.com.traderbobsemporium.dao;

import java.security.InvalidParameterException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class DatabaseAccessFactory<T> {

    public DAO<T> getDAO(String param){
        DAO dao;
        switch (param.toLowerCase()) {
            case "account":
                dao = new AccountDAO();
                break;
            case "camper":
                dao = new CamperDAO();
                break;
            case "item":
                dao = new ItemDAO();
                break;
            default:
                throw new InvalidParameterException();

        }
        return dao;
    }

    public DAO<T> getLogger(String param) {
        DAO dao;
        switch (param.toLowerCase()) {
            case "accountactivity":
                dao = new AccountActivityLogger();
                break;
            case "announcements":
                dao = new AnnouncementsLogger();
                break;
            case "purchasesactivity":
                dao = new PurchasesActivityLogger();
                break;
            default:
                throw new InvalidParameterException();
        }
        return dao;
    }
}
