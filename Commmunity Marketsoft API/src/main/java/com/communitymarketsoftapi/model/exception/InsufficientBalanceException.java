package com.communitymarketsoftapi.model.exception;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException() {
        super("Customer has insufficient balance!");
    }
}
