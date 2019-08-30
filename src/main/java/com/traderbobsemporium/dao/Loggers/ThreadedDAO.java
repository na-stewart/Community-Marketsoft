package main.java.com.traderbobsemporium.dao.Loggers;

import main.java.com.traderbobsemporium.dao.DAO;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public abstract class ThreadedDAO<T> extends DAO<T> {
    ThreadedDAO(String tableBeingSelected) {
        super(tableBeingSelected);
    }
    public abstract  void start();
    public abstract void stop();
}
