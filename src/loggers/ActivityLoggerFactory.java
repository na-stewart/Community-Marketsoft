package loggers;

import loggers.AccountActivityLogger;
import loggers.ActivityType;
import loggers.Logger;
import loggers.PurchasesActivityLogger;
import main.java.com.traderbobsemporium.model.Profile;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class ActivityLoggerFactory {

    public Logger logger(String loggerType){
        if (loggerType.equals("AccountActivity")){
            return new AccountActivityLogger();
        } else
            return new PurchasesActivityLogger();
    }
}
