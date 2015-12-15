package com.hung.ofastapp.Objects;

/**
 * Created by Hung on 12/14/2015.
 */
public class Product {
    public String img_product;
    public String name_product;
    public String price_product;

    public Product(String img_product, String name_product, String price_product) {
        this.img_product = img_product;
        this.name_product = name_product;
        this.price_product = price_product;
    }

    public String getImg_product() {
        return img_product;
    }

    public void setImg_product(String img_product) {
        this.img_product = img_product;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public String getPrice_product() {
        return price_product;
    }

    public void setPrice_product(String price_product) {
        this.price_product = price_product;
    }
}
