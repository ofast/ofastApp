package com.hung.ofastapp.Objects;

import android.widget.CheckBox;

import java.io.Serializable;

/**
 * Created by Hung on 12/14/2015.
 */
public class Product implements Serializable {
    public int id_product;
    public int category_id;
    public String img_product;
    public String name_product;
    public String price_product;
    public boolean picked = false;
    public int num_order = 1;
    public String detail;
    public boolean checked = false;


    //Test Contructor
    public Product(String img_product, String name_product, String price_product, boolean checked) {
        this.img_product = img_product;
        this.name_product = name_product;
        this.price_product = price_product;
        this.checked = checked;
    }

    public Product(String img_product, String name_product, String price_product) {
        this.img_product = img_product;
        this.name_product = name_product;
        this.price_product = price_product;
    }

    public Product(String img_product, String name_product, int num_order, String price_product) {
        this.img_product = img_product;
        this.name_product = name_product;
        this.price_product = price_product;
        this.num_order = num_order;
    }
    //For Product
    public Product(int id_product, String img_product, String name_product, String price_product, String detail) {
        this.id_product = id_product;
        this.img_product = img_product;
        this.name_product = name_product;
        this.price_product = price_product;
        this.detail = detail;
    }
    //For detail of Product
    public Product(int id_product, String img_product, String name_product, String price_product) {
        this.id_product = id_product;
        this.img_product = img_product;
        this.name_product = name_product;
        this.price_product = price_product;
    }

    //For listview in Order.java
    public Product(int id_product, String img_product, String name_product, int num_order, String price_product) {
        this.id_product = id_product;
        this.img_product = img_product;
        this.name_product = name_product;
        this.price_product = price_product;
        this.num_order = num_order;
    }

    //For listview in Seach.java
    public Product(int id_product, String name_product, String price_product, String img_product, int category_id) {
        this.id_product = id_product;
        this.category_id = category_id;
        this.img_product = img_product;
        this.name_product = name_product;
        this.price_product = price_product;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
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

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public int getNum_order() {
        return num_order;
    }

    public void setNum_order(int num_order) {
        this.num_order = num_order;
    }

    public void addOrder() {
        this.num_order++;
    }

    public void subProduct() {
        if (num_order > 1) {
            this.num_order--;
        }

    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
