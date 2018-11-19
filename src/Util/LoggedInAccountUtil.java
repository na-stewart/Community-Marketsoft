package Util;

import AccountTypes.AccountType;

/**
 * @Author Aidan Stewart
 * @Year 2018
 * Copyright (c)
 * All rights reserved.
 */
public class LoggedInAccountUtil {
    private static int ID;
    private static AccountType accountType;
    private static String Username;

    public static int getID() {
        return ID;
    }

    public static void setID(int ID) {
        LoggedInAccountUtil.ID = ID;
    }

    public static AccountType getAccountType() {
        return accountType;
    }

    public static void setAccountType(AccountType accountType) {
        LoggedInAccountUtil.accountType = accountType;
    }

    public static String getUsername() {
        return Username;
    }

    public static void setUsername(String username) {
        Username = username;
    }
}
