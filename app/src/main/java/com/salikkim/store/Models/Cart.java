package com.salikkim.store.Models;

public class Cart {
    private String name, thumbnail, seller_name, color, size, available_adresses;
    private int cart_id, qnty, total_qnty, cod;
    private double price, sale_price, discount,shipping_charge;

    public Cart(String name, String thumbnail, String seller_name, String color, String size, String available_adresses, int cart_id, double price, double sale_price, int qnty, int total_qnty, int cod, double shipping_charge) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.seller_name = seller_name;
        this.color = color;
        this.size = size;
        this.available_adresses = available_adresses;
        this.cart_id = cart_id;
        this.price = price;
        this.sale_price = sale_price;
        this.total_qnty = total_qnty;
        this.cod = cod;
        this.shipping_charge = shipping_charge;
        this.discount = ((price - sale_price) * 100 / price);
        this.qnty = qnty;
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

    public String getAvailable_adresses() {
        return available_adresses;
    }

    public int getCart_id() {
        return cart_id;
    }

    public double getPrice() {
        return price;
    }

    public double getSale_price() {
        return sale_price;
    }

    public double getDiscount() {
        return discount;
    }


    public int getQnty() {
        return qnty;
    }

    public int getTotal_qnty() {
        return total_qnty;
    }

    public int getCod() {
        return cod;
    }

    public double getShipping_charge() {
        return shipping_charge;
    }
}
