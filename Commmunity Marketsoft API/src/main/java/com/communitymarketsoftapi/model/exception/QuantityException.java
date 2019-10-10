package com.communitymarketsoftapi.model.exception;

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
