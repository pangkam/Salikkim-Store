package com.salikkim.store.Models;

public class Orders {
    private String name, thumbnail, seller_name, color, size, order_date, status_info;
    private int order_id, pay_mod, qnty, status;
    private double price;

    public Orders(String name, String thumbnail, String seller_name, String color, String size, String order_date, String status_info, int order_id, double price, int qnty, int status, int pay_mod) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.seller_name = seller_name;
        this.color = color;
        this.size = size;
        this.order_date = order_date;
        this.status_info = status_info;
        this.order_id = order_id;
        this.price = price;
        this.pay_mod = pay_mod;
        this.qnty = qnty;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public String getOrder_date() {
        return order_date;
    }

    public int getOrder_id() {
        return order_id;
    }

    public double getPrice() {
        return price;
    }

    public int getQnty() {
        return qnty;
    }

    public int getStatus() {
        return status;
    }

    public String getStatus_info() {
        return status_info;
    }

    public int getPay_mod() {
        return pay_mod;
    }
}
