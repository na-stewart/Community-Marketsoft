package com.communitymarketsoftapi.controller;

import com.google.gson.Gson;
import com.communitymarketsoftapi.model.logging.PurchasesActivity;
import com.communitymarketsoftapi.repository.Loggers.PurchasesActivityRepo;
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
public class PurchasesActivityController {
    private Subject subject = SecurityUtils.getSubject();
    private PurchasesActivityRepo repo = new PurchasesActivityRepo();

    @GetMapping("purchasesactivity/all")
    public ResponseEntity getAll() throws SQLException {
        subject.checkPermission("purchasesactivity:display");
        return new ResponseEntity<>(repo.getAll(), HttpStatus.OK);
    }


    @GetMapping("purchasesactivity/all/whereparams")
    public ResponseEntity getAll(@RequestParam("where") String whereClause, @RequestParam("params") String[] params) throws SQLException {
        subject.checkPermission("purchasesactivity:display");
        return new ResponseEntity<>(repo.getAll(whereClause, params), HttpStatus.OK);
    }

    @GetMapping("purchasesactivity/all/where")
    public ResponseEntity getAll(@RequestParam("where") String whereClause, @RequestParam("param") String param) throws SQLException {
        subject.checkPermission("purchasesactivity:display");
        return new ResponseEntity<>(repo.getAll(whereClause, param), HttpStatus.OK);
    }


    @GetMapping("/purchasesactivity")
    public ResponseEntity get(@RequestParam int id) throws SQLException {
        subject.checkPermission("purchasesactivity:display");
        return new ResponseEntity<>(repo.get(id), HttpStatus.OK);
    }

    @DeleteMapping("/purchasesactivity")
    public ResponseEntity delete(@RequestParam int id) throws SQLException {
        subject.checkPermission("purchasesactivity:delete");
        repo.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/purchasesactivity")
    public ResponseEntity post (@RequestParam("purchasesactivity") String json) throws SQLException {
        subject.checkPermission("purchasesactivity:add");
        repo.add(new Gson().fromJson(json, PurchasesActivity.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/purchasesactivity")
    public ResponseEntity<String> put(@RequestParam("purchasesactivity") String json) throws SQLException {
        subject.checkPermission("purchasesactivity:update");
        repo.update(new Gson().fromJson(json, PurchasesActivity.class));
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
