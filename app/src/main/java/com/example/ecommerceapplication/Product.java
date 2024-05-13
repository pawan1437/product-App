package com.example.ecommerceapplication;

import java.util.List;

public class Product {
    private int id;
    private String title;
    private int price;
    private String description;
    private List<String> images;
    private boolean inCart;

    public boolean inCart() {
        return inCart;
    }

    public void setCart(boolean favorite) {
        inCart = favorite;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
