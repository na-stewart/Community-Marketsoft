package com.communitymarketsoftapi.controller;

import com.google.gson.Gson;
import com.communitymarketsoftapi.model.Customer;
import com.communitymarketsoftapi.repository.CustomerRepo;
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
public class CustomerController {
    private Subject subject = SecurityUtils.getSubject();
    private CustomerRepo repo = new CustomerRepo();

    @GetMapping("/customer/all")
    public ResponseEntity getAll() throws SQLException {
        subject.checkPermission("customer:display");
        return new ResponseEntity<>(repo.getAll(), HttpStatus.OK);
    }


    @GetMapping("/customer")
    public ResponseEntity get(@RequestParam int id) throws SQLException {
        subject.checkPermission("customer:display");
        return new ResponseEntity<>(repo.get(id), HttpStatus.OK);
    }

    @DeleteMapping("/customer")
    public ResponseEntity delete(@RequestParam int id) throws SQLException {
        subject.checkPermission("customer:delete");
        repo.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/customer")
    public ResponseEntity post (@RequestParam("customer") String json) throws SQLException {
        subject.checkPermission("customer:display");
        repo.add(new Gson().fromJson(json, Customer.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/customer")
    public ResponseEntity<String> put(@RequestParam("customer") String json) throws SQLException {
        subject.checkPermission("customer:update");
        repo.update(new Gson().fromJson(json, Customer.class));
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
