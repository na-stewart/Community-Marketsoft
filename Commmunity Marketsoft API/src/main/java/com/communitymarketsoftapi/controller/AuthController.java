package com.communitymarketsoftapi.controller;

import com.communitymarketsoftapi.model.Account;
import com.communitymarketsoftapi.model.AccountRole;
import com.communitymarketsoftapi.repository.AccountRepo;
import com.google.gson.JsonParser;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpHeaders;
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
public class AuthController {

    private Subject subject = SecurityUtils.getSubject();

    @PostMapping(value = "/auth")
    private ResponseEntity auth(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) throws SQLException, StripeException {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        usernamePasswordToken.setRememberMe(true);
        ensureUserIsLoggedOut();
        subject.login(usernamePasswordToken);
        checkValidSubscription();


        return new ResponseEntity<>("Logged in" ,HttpStatus.OK);
    }

    private void ensureUserIsLoggedOut() {
        try {
            if (subject == null)
                return;
            subject.logout();
            Session session = subject.getSession(false);
            if (session == null)
                return;
            session.stop();
        } catch (Exception e) {
        }
    }

    private void checkValidSubscription() throws StripeException, SQLException {
        AccountRepo accountRepo = new AccountRepo();
        Account loggedInAccount = accountRepo.getByUsername(subject.getPrincipal().toString());
        String status = Subscription.retrieve(loggedInAccount.getSubscriptionId()).getStatus();
        if (!status.equals("active") && !status.equals("trialing")){
            loggedInAccount.setAccountRoles(AccountRole.DISABLED);
            accountRepo.update(loggedInAccount);
            subject.logout();
            throw new DisabledAccountException("Current status is "+  status);

        }
    }


    @PostMapping(value = "auth/logout")
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
