package main.java.com.marketsoftcommunityapi.controller;

import com.google.gson.Gson;
import main.java.com.marketsoftcommunityapi.model.Customer;
import main.java.com.marketsoftcommunityapi.repository.CustomerRepo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.aspectj.apache.bcel.util.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.lang.reflect.Type;

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
    public ResponseEntity getAll() {
        subject.checkPermission("customer:display");
        return new ResponseEntity<>(repo.getAll(), HttpStatus.OK);
    }


    @GetMapping("/customer/all/whereparams")
    public ResponseEntity getAll(@RequestParam("where") String whereClause, @RequestParam("params") String[] params) {
        subject.checkPermission("customer:display");
        return new ResponseEntity<>(repo.getAll(whereClause, params), HttpStatus.OK);
    }

    @GetMapping("customer/all/where")
    public ResponseEntity getAll(@RequestParam("where") String whereClause, @RequestParam("param") String param) {
        subject.checkPermission("customer:display");
        return new ResponseEntity<>(repo.getAll(whereClause, param), HttpStatus.OK);
    }


    @GetMapping("/customer")
    public ResponseEntity get(@RequestParam int id) {
        subject.checkPermission("customer:display");
        return new ResponseEntity<>(repo.get(id), HttpStatus.OK);
    }

    @DeleteMapping("/customer")
    public ResponseEntity delete(@RequestParam int id){
        subject.checkPermission("customer:delete");
        repo.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/customer")
    public ResponseEntity post (@RequestParam("customer") String json) {
        subject.checkPermission("customer:add");
        repo.add(new Gson().fromJson(json, Customer.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/customer")
    public ResponseEntity<String> put(@RequestParam("customer") String json)  {
        subject.checkPermission("customer:update");
        repo.update(new Gson().fromJson(json, Customer.class));
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
