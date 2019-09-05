package main.java.com.marketsoftcommunityapi.controller;

import com.google.gson.Gson;
import main.java.com.marketsoftcommunityapi.model.Customer;
import main.java.com.marketsoftcommunityapi.model.logging.Announcement;
import main.java.com.marketsoftcommunityapi.repository.CustomerRepo;
import main.java.com.marketsoftcommunityapi.repository.Loggers.AnnouncementRepo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */
public class AnnouncementController {
    private Subject subject = SecurityUtils.getSubject();
    private AnnouncementRepo repo = new AnnouncementRepo();

    @GetMapping("announcement/all")
    public ResponseEntity getAll() {
        subject.checkPermission("announcement:display");
        return new ResponseEntity<>(repo.getAll(), HttpStatus.OK);
    }


    @GetMapping("announcement/all/whereparams")
    public ResponseEntity getAll(@RequestParam("where") String whereClause, @RequestParam("params") String[] params) {
        subject.checkPermission("announcement:display");
        return new ResponseEntity<>(repo.getAll(whereClause, params), HttpStatus.OK);
    }

    @GetMapping("announcement/all/where")
    public ResponseEntity getAll(@RequestParam("where") String whereClause, @RequestParam("param") String param) {
        subject.checkPermission("announcement:display");
        return new ResponseEntity<>(repo.getAll(whereClause, param), HttpStatus.OK);
    }


    @GetMapping("/announcement")
    public ResponseEntity get(@RequestParam int id) {
        subject.checkPermission("announcement:display");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/announcement")
    public ResponseEntity delete(@RequestParam int id){
        subject.checkPermission("announcement:delete");
        repo.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/announcement")
    public ResponseEntity post (@RequestParam("announcement") String json) {
        subject.checkPermission("announcement:add");
        repo.add(new Gson().fromJson(json, Announcement.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/announcement")
    public ResponseEntity<String> put(@RequestParam("announcement") String json)  {
        subject.checkPermission("announcement:update");
        repo.update(new Gson().fromJson(json, Announcement.class));
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
