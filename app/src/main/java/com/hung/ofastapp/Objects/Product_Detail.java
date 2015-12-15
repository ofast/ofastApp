package com.hung.ofastapp.Objects;

/**
 * Created by Hung on 12/14/2015.
 */
public class Product_Detail {
    int detail_img;
    String detail_name;
    String detail_price;

    public Product_Detail(int detail_img, String detail_name, String detail_price) {
        this.detail_img = detail_img;
        this.detail_name = detail_name;
        this.detail_price = detail_price;
    }

    public int getDetail_img() {
        return detail_img;
    }

    public void setDetail_img(int detail_img) {
        this.detail_img = detail_img;
    }

    public String getDetail_name() {
        return detail_name;
    }

    public void setDetail_name(String detail_name) {
        this.detail_name = detail_name;
    }

    public String getDetail_price() {
        return detail_price;
    }

    public void setDetail_price(String detail_price) {
        this.detail_price = detail_price;
    }
}
