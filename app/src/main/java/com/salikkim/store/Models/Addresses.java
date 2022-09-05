package com.salikkim.store.Models;

public class Addresses {
    private String addr_id, address;

    public Addresses(String addr_id, String address) {
        this.addr_id = addr_id;
        this.address = address;
    }

    public String getAddr_id() {
        return addr_id;
    }

    public String getAddress() {
        return address;
    }
}
