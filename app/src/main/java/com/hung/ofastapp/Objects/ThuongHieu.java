package com.hung.ofastapp.Objects;

/**
 * Created by Hung on 12/8/2015.
 */
public class ThuongHieu {
    public String img;
    public String name;
    public String id;

    public ThuongHieu(String img, String name, String id) {
        this.img = img;
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTextview() {
        return name;
    }

    public void setTextview(String textview) {
        this.name = textview;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
