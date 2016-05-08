package com.hung.ofastapp.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hung.ofastapp.Objects.Product;
import com.hung.ofastapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hung on 12/14/2015.
 */
public class Product_ViewPagerAdapter extends PagerAdapter{

    private  Context context;
    private ArrayList<com.hung.ofastapp.Objects.Product> arrayList = new ArrayList<Product>();


    public Product_ViewPagerAdapter(Context context, ArrayList<com.hung.ofastapp.Objects.Product> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }


    @Override
    public Parcelable saveState() {
        return null;
    }


    @Override
    public Object instantiateItem(View collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.product_content,null);
        ((ViewPager) collection).addView(view);
        final ImageView img_product = (ImageView) view.findViewById(R.id.img_product);

        final TextView txtv_tensanpham = (TextView) view.findViewById(R.id.txtv_tensanpham);
        final TextView txtv_giasanpham = (TextView) view.findViewById(R.id.txtv_giasanpham);
        TextView txtv_picked = (TextView) view.findViewById(R.id.txtv_picked);
        TextView txtv_number = (TextView) view.findViewById(R.id.txtv_numorder);



        final Product product = arrayList.get(position);
        Picasso.with(context)
                .load(product.img_product)
                .placeholder(R.drawable.logo)
                .resize(150,150)
                .into(img_product);
        Handler mainHandler = new Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                txtv_tensanpham.setText(product.name_product);
                txtv_giasanpham.setText(product.price_product);} // This is your code
        };
        mainHandler.post(myRunnable);

        txtv_number.setText(String.valueOf(product.num_order));
        if(product.picked) {
            txtv_picked.setText("Picked true");
        }else{
            txtv_picked.setText("Picked false");
        }
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
