package com.communitymarketsoftapi.controller;

import com.communitymarketsoftapi.model.Account;
import com.communitymarketsoftapi.model.AccountRole;
import com.communitymarketsoftapi.repository.AccountRepo;
import com.communitymarketsoftapi.util.AccountUtil;
import com.communitymarketsoftapi.util.Util;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.*;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

@RestController
public class SubscriptionController {
    private AccountRepo accountRepo = new AccountRepo();
    private final SecureRandom random = new SecureRandom();
    Subject subject = SecurityUtils.getSubject();


    @GetMapping("/subscription/create")
    public ResponseEntity createSubscription(@RequestParam("subscriptionPlan") String subscriptionPlan, @RequestParam("email")
            String username, @RequestParam("password") String password, @RequestParam("timeZone") String timeZone) throws Exception {
        Account account = accountRepo.getByUsername(username);
        Map<String, Object> params = new HashMap<>();
        updateOrCreateSubscription(account, params, username, password);
        ArrayList<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        params.put("payment_method_types", paymentMethodTypes);
        HashMap<String, Object> subscriptionData = new HashMap<String, Object>();
        HashMap<String, Object> items = new HashMap<>();
        HashMap<String, Object> item = new HashMap<>();
        item.put("plan", subscriptionPlan);
        items.put("0", item);
        subscriptionData.put("items", items);
        subscriptionData.put("trial_period_days", 30);
        params.put("subscription_data", subscriptionData);
        params.put("success_url", "https://communitymarketsoft.com/success");
        params.put("cancel_url", "https://communitymarketsoft.com/fail");
        Session session = Session.create(params);
        if (account == null) {
            accountRepo.setRegister(true);
            accountRepo.add(new Account(username, password, AccountRole.DISABLED, null, null, timeZone));
        }
        return new ResponseEntity<>("https://api.communitymarketsoft.com/register.html?sessionId=" + session.getId(), HttpStatus.OK);
    }



    private void updateOrCreateSubscription(Account account, Map<String, Object> params, String email, String password)  {
        if (account != null) {
            subject.login(new UsernamePasswordToken(email, password));
            try {
                Customer customer = Customer.retrieve(account.getCommunityId());
                if (customer.getEmail().equals(account.getName()))
                    params.put("customer", account.getCommunityId());
                else
                    throw new RuntimeException("Only the community owner can update a subscription!");
            } catch (StripeException ignored) {
                params.put("customer_email", email);
            }
        }
        else
            params.put("customer_email", email);
    }

    @RequestMapping(value ="/subscription/webhook" ,method = RequestMethod.POST ,produces = "application/json")
    public  @ResponseBody ResponseEntity webhook(@RequestBody(required=true)String  request, @RequestHeader HttpHeaders headers) throws StripeException, MessagingException, SQLException {
        String signature = signatureToString(headers);
        Event event = Webhook.constructEvent(request, signature, System.getenv("STRIPE_WEBHOOK"));
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        if (deserializer.getObject().isPresent()) {
            Session session = (Session) deserializer.getObject().get();
            updateExistingAccountIfNeeded(session);
        }
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/subscription/cancel")
    public ResponseEntity cancel() throws StripeException {
        Subject subject = SecurityUtils.getSubject();
        subject.checkRole("ADMIN");
        Subscription.retrieve(AccountUtil.SUBSCRIPTION_ID()).cancel();
        subject.logout();
        return new ResponseEntity(HttpStatus.OK);
    }

    private String signatureToString(HttpHeaders httpHeaders){
        List<String> headers = httpHeaders.get("Stripe-Signature");
        StringBuilder stringBuilder = new StringBuilder();
        for (String header : headers)
            stringBuilder.append(header).append(",");
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    private ResponseEntity updateExistingAccountIfNeeded(Session hooksession) throws SQLException, StripeException {
        Account account = accountRepo.getByUsername(Customer.retrieve(hooksession.getCustomer()).getEmail());
        if (AccountUtil.COMMUNITY_ID() != null)
            updateSubscription(account, account.getSubscriptionId());
        else
            registerNewAccount(account, hooksession.getSubscription(), hooksession.getCustomer());


        subject.logout();
        return new ResponseEntity<>("account created, check email for information.", HttpStatus.OK);
    }

    private void updateSubscription(Account account, String subscription) throws SQLException, StripeException {
        account.setAccountRoles(AccountRole.ADMIN);
        accountRepo.updateAllAccountsWithSubscriptionId(account.getSubscriptionId(), subscription);
        accountRepo.updateSubscriptionFields(account);
        sendRegisterExistingAccountEmail(account.getName());
    }

    private void registerNewAccount(Account account, String subscriptionId, String communityId) throws SQLException {
        account.setCommunityId(communityId);
        account.setSubscriptionId(subscriptionId);
        account.setAccountRoles(AccountRole.ADMIN);
        accountRepo.updateSubscriptionFields(account);
        sendRegisterNewAccountEmail(account.getName());
    }

    private void sendRegisterExistingAccountEmail(String email) {
        String message = "Your account should now be registered and usable again! Thank you for resubscribing and for using " +
                "Community Marketsoft. If you have If you have any questions, issues, or comments feel free to reply to " +
                "this email or send an email to aidan.stewart@communitymarketsoft.com.";
        try {
            Util.SEND_MAIL(email, message, "Register Complete!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    //<3
    private void sendRegisterNewAccountEmail(String email) {
        String message = "Thanks for subscribing to Community Marketsoft. I really appreciate it. If you have any" +
                " questions, issues, or comments feel free to reply to this email or send an email to aidan.stewart@communitymarketsoft.com.";
        try {
            Util.SEND_MAIL(email, message, "Register Complete!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
