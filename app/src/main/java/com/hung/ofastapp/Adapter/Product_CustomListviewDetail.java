package com.hung.ofastapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StringLoader;
import com.hung.ofastapp.Objects.Product;
import com.hung.ofastapp.Order;
import com.hung.ofastapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hung on 12/14/2015.
 */
public class Product_CustomListviewDetail extends ArrayAdapter<Product>  {


    public static boolean flagadd;
    Order order = new Order();

    Product_CustomListviewDetail adapter;
    Context context;
    int LayoutID;
    ArrayList<Product> arrayList = new ArrayList<Product>();
    ArrayList<Product> brrayList = new ArrayList<Product>();
    ViewHolder holder;
    Typeface tf1;
    public Product_CustomListviewDetail(Context context, int LayoutID, ArrayList<Product> arrayList) {
        super(context, LayoutID, arrayList);
        this.context = context;
        this.LayoutID = LayoutID;
        this.arrayList = arrayList;
        this.adapter = this;
    }



    private class ViewHolder {
        ImageView img_image_product;
        TextView txtv_name_product;
        TextView txtv_price_product;
        TextView txtv_soluong_product;
        TextView txtv_soluong;
        Button btn_cong;
        Button btn_tru;

    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
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
            holder.btn_cong = (Button) convertView.findViewById(R.id.btn_cong);
            holder.btn_tru = (Button) convertView.findViewById(R.id.btn_tru);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Product product = arrayList.get(position);
        holder.txtv_name_product.setText(product.name_product);
        holder.txtv_price_product.setText(product.price_product);

        tf1 = Typeface.createFromAsset(convertView.getContext().getAssets(),"VKORIN.TTF");
        holder.txtv_price_product.setTypeface(tf1);
        holder.txtv_soluong_product.setText(String.valueOf(product.num_order));
        holder.btn_cong.setTag(position);
        //Set sự kiện khi nhấn Button +
        holder.btn_cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.addOrder();
                holder.txtv_soluong_product.setText(String.valueOf(product.getNum_order()));
                Toast.makeText(getContext(),"BTN + of:" +position,Toast.LENGTH_SHORT).show();
                arrayList.set(position,product);
                Log.d("AAAAAAA",String.valueOf(arrayList.get(position).getNum_order()));
                Product_CustomListviewDetail.this.notifyDataSetChanged();
            }
        });
        //Set sự kiện khi nhấn Button -
        holder.btn_tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.subProduct();
                Log.d(String.valueOf(position),String.valueOf(arrayList.get(position).getNum_order()));
                holder.txtv_soluong_product.setText(String.valueOf(product.getNum_order()));
                Toast.makeText(getContext(),"BTN - of:" +position,Toast.LENGTH_SHORT).show();
                Product_CustomListviewDetail.this.notifyDataSetChanged();

            }
        });
        if(product.getNum_order() == 1)
        {
            holder.btn_tru.setEnabled(false);
//                    arrayList.remove(product);
//                    ((Order)context).TinhTong(arrayList);
            Product_CustomListviewDetail.this.notifyDataSetChanged();
        }
        //Kiểm tra số lượng sản phẩm lớn hơn 1
        if(product.getNum_order() >1)
        {
            holder.btn_tru.setEnabled(true);
            Product_CustomListviewDetail.this.notifyDataSetChanged();
        }

        //Chạy lại hàm Tính TỔng từ Home
        ((Order)context).TinhTong(arrayList);

        //Lấy ảnh từ web về
//        Glide.with(this.context)
//                .load(product.img_product)
//                .fitCenter()
//                .centerCrop()
//                .override(300,200)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.img_image_product);
        Picasso.with(this.context)
                .load(product.img_product)
                .resize(dpToPx(90),dpToPx(90))
                .placeholder(R.drawable.logo)
                .into(holder.img_image_product);

        return convertView;
    }


    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
