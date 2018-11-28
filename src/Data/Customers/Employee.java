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

    public Employee(int ID, String username, String password, AccountType accountType) {
        this.id = ID;
        this.username = username;
        this.password = password;
        this.accountType = accountType;

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
}
