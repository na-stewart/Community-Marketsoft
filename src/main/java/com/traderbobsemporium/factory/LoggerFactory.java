package main.java.com.traderbobsemporium.factory;

import main.java.com.traderbobsemporium.dao.loggers.AccountActivityLogger;
import main.java.com.traderbobsemporium.dao.loggers.AnnouncementsLogger;
import main.java.com.traderbobsemporium.dao.loggers.Logger;
import main.java.com.traderbobsemporium.dao.loggers.PurchasesActivityLogger;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class LoggerFactory<T> implements Factory<Logger>{

    @Override
    public Logger<T> create(String param) {
        Logger logger = null;
        switch (param.toLowerCase()){
            case "accountactivity":
                logger = new AccountActivityLogger();
                break;
            case "announcements":
                logger = new AnnouncementsLogger();
                break;
            case "purchasesactivity":
                 logger = new PurchasesActivityLogger();
                 break;
        }
        return logger;
    }
}
