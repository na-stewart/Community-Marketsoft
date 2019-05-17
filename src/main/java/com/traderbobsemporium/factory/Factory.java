package main.java.com.traderbobsemporium.factory;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public interface Factory<T> {
    T create(String param);
}
