package main.java.com.marketsoftcommunityapi.repository.Loggers;

import main.java.com.marketsoftcommunityapi.repository.Repo;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public abstract class LogRepo<T> extends Repo<T> {
    LogRepo(String tableBeingSelected) {
        super(tableBeingSelected);
    }
    public abstract  void start();
    public abstract void stop();
}
