package main.java.com.traderbobsemporium.loggers;

import main.java.com.traderbobsemporium.model.Profile;

import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public interface Logger<T> {
    void log(String[] params, ActivityType activityType);
    List<T> getLogs();
}
