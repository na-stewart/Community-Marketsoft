package main.java.com.traderbobsemporium.dao.loggers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public interface Logger<T> {
    void log(T t) throws SQLException;
    List<T> getLogs() throws SQLException;
}
