package Data.Customers;

import AccountTypes.AccountType;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class Employee {
    private int id;
    private String username;
    private String password;
    private AccountType accountType;
    private String macAddress;

    public Employee(int ID, String username, String password, AccountType accountType, String macAddress) {
        this.id = ID;
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        this.macAddress = macAddress;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getMacAddress() {
        return macAddress;
    }
}
