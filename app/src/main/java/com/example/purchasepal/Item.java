package com.example.purchasepal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    private Integer item_id;
    private String item_name;
    private Integer store_id;

    public Item(@JsonProperty("item_id") Integer item_id, @JsonProperty("item_name") String item_name, @JsonProperty("store_id") Integer store_id) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.store_id = store_id;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public Integer getStore_id() {
        return store_id;
    }

    public void setStore_id(Integer store_id) {
        this.store_id = store_id;
    }
}
