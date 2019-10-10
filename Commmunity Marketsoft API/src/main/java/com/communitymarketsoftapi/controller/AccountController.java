package com.communitymarketsoftapi.controller;

import com.communitymarketsoftapi.model.AccountRole;
import com.google.gson.Gson;
import com.communitymarketsoftapi.model.Account;
import com.communitymarketsoftapi.repository.AccountRepo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
@RestController
public class AccountController {
    private Subject subject = SecurityUtils.getSubject();
    private AccountRepo repo = new AccountRepo();

    @GetMapping("account/all")
    public ResponseEntity getAll() throws SQLException {
        subject.checkPermission("account:display");
        return new ResponseEntity<>(repo.getAll(), HttpStatus.OK);
    }



    @GetMapping("/account")
    public ResponseEntity get(@RequestParam int id) throws SQLException {
        subject.checkPermission("account:display");
        return new ResponseEntity<>(repo.get(id) ,HttpStatus.OK);
    }

    @DeleteMapping("/account")
    public ResponseEntity delete(@RequestParam int id) throws SQLException {
        subject.checkPermission("account:delete");
        repo.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/account")
    public ResponseEntity post (@RequestParam("account") String json) throws SQLException {
        subject.checkPermission("account:post");
        repo.add(new Gson().fromJson(json, Account.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/account")
    public ResponseEntity<String> put(@RequestParam("account") String json) throws SQLException {
        subject.checkPermission("account:update");
        repo.update(new Gson().fromJson(json, Account.class));
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
