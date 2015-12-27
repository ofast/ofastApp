package com.hung.ofastapp.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hung.ofastapp.Objects.Product;
import com.hung.ofastapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hung on 12/14/2015.
 */
public class Product_CustomListviewDetail extends ArrayAdapter<Product> {
    Context context;
    int LayoutID;
    ArrayList<Product> arrayList;
    ViewHolder holder;
    Typeface tf1;
    public Product_CustomListviewDetail(Context context, int LayoutID, ArrayList<Product> arrayList) {
        super(context, LayoutID, arrayList);
        this.context = context;
        this.LayoutID = LayoutID;
        this.arrayList = arrayList;
    }
    private class ViewHolder {
        ImageView img_image_product;
        TextView txtv_name_product;
        TextView txtv_price_product;
        TextView txtv_soluong_product;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Declaramos el ImageView

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.product_custom_listview_detail,null);
            holder = new ViewHolder();
            holder.img_image_product = (ImageView) convertView.findViewById(R.id.img_image_product);
            holder.txtv_name_product = (TextView) convertView.findViewById(R.id.txtv_name_product);
            holder.txtv_soluong_product = (TextView) convertView.findViewById(R.id.txtv_soluong_product);
            holder.txtv_price_product = (TextView)convertView.findViewById(R.id.txtv_price_product);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Product product = arrayList.get(position);
        holder.txtv_name_product.setText(product.name_product);
        holder.txtv_price_product.setText(product.price_product);

        tf1 = Typeface.createFromAsset(convertView.getContext().getAssets(),"VKORIN.TTF");
        holder.txtv_price_product.setTypeface(tf1);
        holder.txtv_soluong_product.setText(String.valueOf(product.num_order));

        Picasso.with(this.context)
                .load(product.img_product)
                .resize(155, 155)
                .placeholder(R.drawable.logo)
                .into(holder.img_image_product);
        return convertView;
    }
}
