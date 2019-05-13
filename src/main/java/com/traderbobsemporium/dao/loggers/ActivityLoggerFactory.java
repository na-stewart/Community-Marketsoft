package main.java.com.traderbobsemporium.dao.loggers;

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
        }// else
            //return new PurchasesActivityLogger();
        return null;
    }
}
