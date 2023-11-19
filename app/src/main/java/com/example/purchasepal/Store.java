package com.example.purchasepal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Store {
    private Integer store_id;
    private String name;
    private String username;
    public Store(@JsonProperty("store_id")  String storeId, @JsonProperty("name")String name, @JsonProperty("username") String username) {
        this.store_id = Integer.parseInt(storeId);
        this.name = name;
        this.username = username;
    }

    public Integer getStoreId() {
        return store_id;
    }

    public void setStoreId(Integer storeId) {
        this.store_id = storeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @Override
    public String toString() {
        return "Store{" +
                "store_id=" + store_id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
