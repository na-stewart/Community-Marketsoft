package main.java.com.marketsoftcommunityapi.controller;

import com.google.gson.Gson;
import main.java.com.marketsoftcommunityapi.model.Account;
import main.java.com.marketsoftcommunityapi.model.AccountPermission;
import main.java.com.marketsoftcommunityapi.repository.AccountRepo;
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
@RestController
public class AccountController {
    private Subject subject = SecurityUtils.getSubject();
    private AccountRepo repo = new AccountRepo();

    @GetMapping("account/all")
    public ResponseEntity getAll() {
        subject.checkPermission("account:display");
        return new ResponseEntity<>(repo.getAll(), HttpStatus.OK);
    }

    @GetMapping("account/permissions/all")
    public ResponseEntity getAllPermissions(@RequestParam("username") String user){
        subject.checkPermission("account:display");
        return new ResponseEntity<>(repo.getAccountPermissionRepo().getAll("WHERE username = ?", user), HttpStatus.OK);
    }


    @GetMapping("account/all/whereparams")
    public ResponseEntity getAll(@RequestParam("where") String whereClause, @RequestParam("params") String[] params) {
        subject.checkPermission("account:display");
        return new ResponseEntity<>(repo.getAll(whereClause, params), HttpStatus.OK);
    }

    @GetMapping("account/all/where")
    public ResponseEntity getAll(@RequestParam("where") String whereClause, @RequestParam("param") String param) {
        subject.checkPermission("account:display");
        return new ResponseEntity<>(repo.getAll(whereClause, param), HttpStatus.OK);
    }


    @GetMapping("/account")
    public ResponseEntity get(@RequestParam int id) {
        subject.checkPermission("account:display");
        return new ResponseEntity<>(repo.get(id) ,HttpStatus.OK);
    }


    @DeleteMapping("/account")
    public ResponseEntity delete(@RequestParam int id){
        subject.checkPermission("account:delete");
        repo.delete(id);
        repo.deleteAccountPermissions(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/account/permissions")
    public ResponseEntity deletePerm(@RequestParam int id){
        subject.checkPermission("account:delete");
        repo.getAccountPermissionRepo().delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/account")
    public ResponseEntity post (@RequestParam("account") String json) {
        subject.checkPermission("account:add");
        repo.add(new Gson().fromJson(json, Account.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/account/permissions")
    public ResponseEntity postPerm (@RequestParam("permission") String json) {
        subject.checkPermission("account:add");
        repo.getAccountPermissionRepo().add(new Gson().fromJson(json, AccountPermission.class));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/account")
    public ResponseEntity<String> put(@RequestParam("account") String json)  {
        subject.checkPermission("account:update");
        repo.update(new Gson().fromJson(json, Account.class));
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
