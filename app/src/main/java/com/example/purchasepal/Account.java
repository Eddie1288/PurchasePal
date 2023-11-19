package com.example.purchasepal;

import java.util.ArrayList;

public class Account {
    private String username;
    private ArrayList<Store> stores;

    public Account(String username, ArrayList<Store> stores) {
        this.username = username;
        this.stores = stores;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }
}
