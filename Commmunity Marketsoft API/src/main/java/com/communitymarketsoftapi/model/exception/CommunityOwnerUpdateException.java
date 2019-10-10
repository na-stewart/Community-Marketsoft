package com.communitymarketsoftapi.model.exception;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class CommunityOwnerUpdateException extends RuntimeException {
    public CommunityOwnerUpdateException() {
        super("This field cannot be updated because account being referenced is a community owner!");
    }
}
