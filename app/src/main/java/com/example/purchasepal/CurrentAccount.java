package com.example.purchasepal;

import java.util.ArrayList;

/**
 * Lazy Singleton
 * Holds the Account that is currently logged into the device
 */
public class CurrentAccount {
    private static CurrentAccount instance;
    private static Account account;

    /**
     * Lazy singleton constructor is private.
     */
    private CurrentAccount() {
    }


    /**
     * Returns the current account. Creates it if it does not exist
     * @return The current account
     */
    public static Account getAccount() {
        return account;
    }

    public static void setAccount(Account acc) {
        account = acc;
    }

    public Account getCurrentAccount() {
        return account;
    }


}