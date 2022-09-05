package com.salikkim.store.Models;

public class Products {
    private String name,thumbnail,seller_name, color, product_descriptions,available_adresses;
    private int product_id,seller_id, price, sale_price, discount;

    public Products(String name, String thumbnail, String seller_name, String color, String product_descriptions, String available_adresses, int product_id, int seller_id, int price, int sale_price) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.seller_name = seller_name;
        this.color = color;
        this.product_descriptions = product_descriptions;
        this.available_adresses = available_adresses;
        this.product_id = product_id;
        this.seller_id = seller_id;
        this.price = price;
        this.sale_price = sale_price;
        this.discount = ((price - sale_price) * 100 / price);
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

    public String getProduct_descriptions() {
        return product_descriptions;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public int getPrice() {
        return price;
    }

    public int getSale_price() {
        return sale_price;
    }

    public int getDiscount() {
        return discount;
    }

    public String getAvailable_adresses() {
        return available_adresses;
    }
}
