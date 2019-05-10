package main.java.com.traderbobsemporium.loggers;

import com.sun.org.apache.regexp.internal.RE;

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
