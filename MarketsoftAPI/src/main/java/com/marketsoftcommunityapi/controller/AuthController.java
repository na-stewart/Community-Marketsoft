package main.java.com.marketsoftcommunityapi.controller;

import main.java.com.marketsoftcommunityapi.repository.AccountRepo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountLockedException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

@RestController
public class AuthController {

    private Subject subject = SecurityUtils.getSubject();

    @RequestMapping(value = "/auth")
    private ResponseEntity retrieveCredentials(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) throws AccountLockedException {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        SecurityUtils.getSubject().getSession(true);
        usernamePasswordToken.setRememberMe(true);
        subject.login(usernamePasswordToken);
        authSubjectRole();
        return new ResponseEntity<>("Logged in" ,HttpStatus.OK);
    }

    private void authSubjectRole() throws AccountLockedException {
        if (subject.hasRole("UNCONFIRMED")) {
            subject.logout();
            throw new AccountLockedException();
        }
        if (subject.hasRole("DISABLED")) {
            subject.logout();
            throw new DisabledAccountException();
        }
    }

    @PostMapping(value = "/auth/logout")
    private ResponseEntity logout(){
        subject.logout();
        return new ResponseEntity<>("Logged out!", new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/auth/haspermission")
    private ResponseEntity hasPermission(@RequestParam("permission") String perm){
        boolean hasPermission = subject.isPermitted(perm);
        return new ResponseEntity<>(hasPermission, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/auth/hasrole")
    private ResponseEntity hasRole(@RequestParam("role") String role){
        boolean hasPermission = subject.hasRole(role);
        return new ResponseEntity<>(hasPermission, new HttpHeaders(), HttpStatus.OK);
    }
}
