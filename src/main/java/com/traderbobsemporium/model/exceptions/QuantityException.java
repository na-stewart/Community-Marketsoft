package main.java.com.traderbobsemporium.model.exceptions;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class QuantityException extends Exception {
    public QuantityException() {
        super("Item attempted to be checked out is out of stock!");
    }
}
