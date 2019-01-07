package Data.Customers;

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
    private EmployeeType accountType;

    public Employee(int ID, String username, String password, EmployeeType accountType) {
        this.id = ID;
        this.password = password;
        this.username = username;
        this.accountType = accountType;

    }

    public int getId() {
        return id;
    }

    public String getPassword(){
        return password;
    }

    public String getUsername() {
        return username;
    }

    public EmployeeType getAccountType() {
        return accountType;
    }
}
