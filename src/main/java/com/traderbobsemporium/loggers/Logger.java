package loggers;

import main.java.com.traderbobsemporium.model.Profile;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public interface Logger {
    public void log(Profile profile, ActivityType activityType);
}
