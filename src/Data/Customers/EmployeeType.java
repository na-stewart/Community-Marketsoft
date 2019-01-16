package Data.Customers;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public enum EmployeeType {
    ADMIN, EMPLOYEE, VENDOR, UNCONFIRMED;

    public static EmployeeType intToEmployeeType(int accountTypeInt){
        switch (accountTypeInt){
            case 0:
                return ADMIN;
            case 1:
                return EMPLOYEE;
            case 2:
                return VENDOR;
            case 3:
                return UNCONFIRMED;
        }
        return UNCONFIRMED;
    }

    public static int employeeTypeToInt(EmployeeType accountType){
        switch (accountType) {
            case ADMIN:
                return 0;
            case EMPLOYEE:
                return 1;
            case VENDOR:
                return 2;
            case UNCONFIRMED:
                return 3;
        }
        return 3;
    }
}
