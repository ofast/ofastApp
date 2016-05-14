package com.hung.ofastapp.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hung.ofastapp.Objects.Product;
import com.hung.ofastapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hung on 12/14/2015.
 */
public class Product_ViewPagerAdapter extends PagerAdapter{

    private  Context context;
    private ArrayList<com.hung.ofastapp.Objects.Product> arrayList = new ArrayList<Product>();
    ViewHolder holder;

    public Product_ViewPagerAdapter(Context context, ArrayList<com.hung.ofastapp.Objects.Product> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }
    private class ViewHolder {
        ImageView img_product;
        TextView txtv_tensanpham;
        TextView txtv_giasanpham;
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
        View view = null;

        if (view == null) {
            view= inflater.inflate(R.layout.product_content,null);
            ((ViewPager) collection).addView(view);
             holder = new ViewHolder();
            holder.img_product = (ImageView) view.findViewById(R.id.img_product);
            holder.txtv_tensanpham = (TextView) view.findViewById(R.id.txtv_tensanpham);
            holder.txtv_giasanpham = (TextView) view.findViewById(R.id.txtv_giasanpham);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        final Product product = arrayList.get(position);
        holder.txtv_tensanpham.setText(product.name_product);
        holder.txtv_giasanpham.setText(product.price_product);
        Picasso.with(context)
                .load(product.img_product)
                .resize(dpToPx(360),dpToPx(360))
                .into(holder.img_product);

        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
