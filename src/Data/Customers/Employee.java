package Data.Customers;

import Data.DataBaseManager;
import Util.LoggedInAccountUtil;
import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.SQLException;

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
    private EmployeeType employeeType;
    DataBaseManager databaseManager = new DataBaseManager();

    public Employee(int ID, String username, String password, EmployeeType accountType) {
        this.id = ID;
        this.password = password;
        this.username = username;
        this.employeeType = accountType;

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

    public EmployeeType getEmployeeType() {
        return employeeType;
    }


    public void setUsername(String username) throws SQLException {
        databaseManager.update("UPDATE employee SET username = '" + username + "' WHERE id =" + id + ";");
    }

    public void setPassword(String password) throws SQLException {
        String passwordHashed =  new StrongPasswordEncryptor().encryptPassword(password);
        databaseManager.update("UPDATE employee SET password = '" + passwordHashed +"' WHERE id =" + id + ";");
        this.password = password;
    }

    public void setEmployeeType(EmployeeType employeeType) throws SQLException {
        int employeeTypeInt = EmployeeType.employeeTypeToInt(employeeType);
        databaseManager.update("UPDATE employee SET employeetype = '" + employeeTypeInt +"' WHERE id =" + id + ";");
        this.employeeType = employeeType;
    }

    public void requestPermissionToModifyEmployees() throws SQLException {
        if (LoggedInAccountUtil.thisAccountType == EmployeeType.EMPLOYEE) {
            if (employeeType == EmployeeType.ADMIN || employeeType == EmployeeType.EMPLOYEE || employeeType == EmployeeType.UNCONFIRMED)
                throw new SQLException("Permission not granted to execute query.");
        }
    }
}

