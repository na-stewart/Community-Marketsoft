package com.communitymarketsoftapi.repository;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class getOnlyRepo<T> extends Repo<T> {
    public getOnlyRepo(String tableBeingSelected) {
        super(tableBeingSelected);
    }

    @Override
    public void update(T updated) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(T t) {
        throw new UnsupportedOperationException();
    }
}
