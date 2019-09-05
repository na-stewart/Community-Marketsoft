package main.java.com.marketsoftcommunityapi.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.AccountLockedException;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

@RestController
public class CredentialController {

    private Subject subject = SecurityUtils.getSubject();

    @RequestMapping(value = "/auth")
    private ResponseEntity<?> retrieveCredentials(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) throws AccountLockedException {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        SecurityUtils.getSubject().getSession(true);
        usernamePasswordToken.setRememberMe(true);
        subject.login(usernamePasswordToken);
        authSubjectRole();
        return new ResponseEntity<>("Logged in!", new HttpHeaders(), HttpStatus.OK);
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

}
