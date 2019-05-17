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
public class LoggerFactory implements Factory<Logger>{

    @Override
    public Logger create(String param) {
        switch (param.toLowerCase()){
            case "accountactivity":
                return new AccountActivityLogger();
            case "announcements":
                return new AnnouncementsLogger();
            case "purchasesactivity":
                return new PurchasesActivityLogger();
        }
      return null;
    }
}
