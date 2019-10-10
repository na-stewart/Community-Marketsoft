package com.communitymarketsoftapi.controller;

import com.communitymarketsoftapi.model.Customer;
import com.communitymarketsoftapi.model.Item;
import com.communitymarketsoftapi.model.exception.InsufficientBalanceException;
import com.communitymarketsoftapi.model.exception.QuantityException;
import com.communitymarketsoftapi.model.logging.PurchasesActivity;
import com.communitymarketsoftapi.repository.CustomerRepo;
import com.communitymarketsoftapi.repository.ItemRepo;
import com.communitymarketsoftapi.repository.Loggers.PurchasesActivityRepo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author Aidan Stewart
 * @Year 2019
 * Copyright (c)
 * All rights reserved.
 */

@RestController
public class RetailerController {
    private Subject subject = SecurityUtils.getSubject();
    private PurchasesActivityRepo purchasesActivityRepo = new PurchasesActivityRepo();

    @GetMapping("retailer/customerpurchases")
    public ResponseEntity getAll(@RequestParam("customerName") String customerName, @RequestParam("date") String date) throws SQLException {
        subject.checkRole("RETAILER");
        return new ResponseEntity<>(purchasesActivityRepo.getPurchasesByCustomerAndDate(customerName, date), HttpStatus.OK);
    }

    @PostMapping("retailer/checkout")
    private ResponseEntity checkout(@RequestParam("customer") String customerParam, @RequestParam("itemList") String itemListParam) throws InsufficientBalanceException, QuantityException, SQLException {
        subject.checkRole("RETAILER");
        Gson gson = new Gson();
        List<Item> itemList = gson.fromJson(itemListParam, TypeToken.getParameterized(ArrayList.class, Item.class).getType());
        Customer customer = gson.fromJson(customerParam, Customer.class);
        subtractBalance(customer, itemList);
        for (Item item : itemList)
            purchasesActivityRepo.add(new PurchasesActivity(item, customer));
        return new ResponseEntity<>("Checkout successful!", HttpStatus.OK);
    }

    private void subtractBalance(Customer customer, List<Item> selectedItems) throws InsufficientBalanceException, QuantityException, SQLException {
        CustomerRepo customerRepo = new CustomerRepo();
        Customer syncedCustomer = customerRepo.get(customer.getId());
        BigDecimal sum = BigDecimal.ZERO;
        for (Item item : selectedItems) {
            if (item.getQuantity() <= 0)
                throw new QuantityException();
            sum = sum.add(item.getPrice());
        }
        if (sum.compareTo(syncedCustomer.getBalance()) <= 0)
            syncedCustomer.setBalance(syncedCustomer.getBalance().subtract(sum));
        else
            throw new InsufficientBalanceException();
        customerRepo.update(syncedCustomer);
        subtractQuantities(selectedItems);
    }


    private void subtractQuantities(List<Item> selectedItems) throws SQLException {
        ItemRepo itemRepo = new ItemRepo();
        Map<Item, Long> counts = selectedItems.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
        for (Item item : counts.keySet()) {
            item.setQuantity((int) (item.getQuantity() - counts.get(item)));
            itemRepo.update(item);
        }
    }
}
