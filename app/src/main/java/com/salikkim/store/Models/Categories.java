package com.salikkim.store.Models;

public class Categories {
    private String title,url;

    public Categories(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
