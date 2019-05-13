package main.java.com.traderbobsemporium.dao.loggers;

import main.java.com.traderbobsemporium.dao.DAO;
import main.java.com.traderbobsemporium.model.Logging.ActivityType;
import main.java.com.traderbobsemporium.model.Profile;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public interface Logger<T> extends DAO<T> {
    void log(Profile params, ActivityType activityType);
}
