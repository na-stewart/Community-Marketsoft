package com.communitymarketsoftapi.controller;

import com.google.gson.Gson;
import com.communitymarketsoftapi.model.logging.AccountActivity;
import com.communitymarketsoftapi.repository.Loggers.AccountActivityRepo;
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
public class AccountActivityController{
        private Subject subject = SecurityUtils.getSubject();
        private AccountActivityRepo repo = new AccountActivityRepo();

        @GetMapping("/accountactivity/all")
        public ResponseEntity getAll() throws SQLException {
            subject.checkPermission("accountactivity:display");
            return new ResponseEntity<>(repo.getAll(), HttpStatus.OK);
        }


        @GetMapping("accountactivity/all/whereparams")
        public ResponseEntity getAll(@RequestParam("where") String whereClause, @RequestParam("params") String[] params) throws SQLException {
            subject.checkPermission("accountactivity:display");
            return new ResponseEntity<>(repo.getAll(whereClause, params), HttpStatus.OK);
        }

        @GetMapping("accountactivity/all/where")
        public ResponseEntity getAll(@RequestParam("clause") String whereClause, @RequestParam("param") String param) throws SQLException {
            subject.checkPermission("accountactivity:display");
            return new ResponseEntity<>(repo.getAll(whereClause, param), HttpStatus.OK);
        }


        @GetMapping("/accountactivity")
        public ResponseEntity get(@RequestParam int id) throws SQLException {
            subject.checkPermission("accountactivity:display");
            return new ResponseEntity<>(repo.get(id), HttpStatus.OK);
        }

        @DeleteMapping("/accountactivity")
        public ResponseEntity delete(@RequestParam int id) throws SQLException {
            subject.checkPermission("accountactivity:delete");
            repo.delete(id, false);
            return new ResponseEntity<>(HttpStatus.OK);
        }


        @PostMapping("/accountactivity")
        public ResponseEntity post (@RequestParam("accountactivity") String json) throws SQLException {
            subject.checkPermission("accountactivity:add");
            repo.add(new Gson().fromJson(json, AccountActivity.class));
            return new ResponseEntity<>(json , HttpStatus.OK);
        }

        @PutMapping("/accountactivity")
        public ResponseEntity<String> put(@RequestParam("accountactivity") String json) throws SQLException {
            subject.checkPermission("accountactivity:update");
            repo.update(new Gson().fromJson(json, AccountActivity.class));
            return new ResponseEntity<>(HttpStatus.OK);

        }
}
