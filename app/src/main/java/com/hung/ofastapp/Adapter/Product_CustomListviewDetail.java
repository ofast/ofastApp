package com.hung.ofastapp.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.hung.ofastapp.Objects.Product_Detail;

/**
 * Created by Hung on 12/14/2015.
 */
public class Product_CustomListviewDetail extends ArrayAdapter<Product_Detail> {
    public Product_CustomListviewDetail(Context context, int resource, Product_Detail[] objects) {
        super(context, resource, objects);
    }
}
