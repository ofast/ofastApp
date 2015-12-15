package com.hung.ofastapp.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hung.ofastapp.R;

import java.util.ArrayList;

/**
 * Created by Hung on 12/14/2015.
 */
public class Product_ViewPagerAdapter extends PagerAdapter{

    ArrayList<String> arrayList = new ArrayList<>();

    private Context context;
    LayoutInflater inflater;
    String[] aaa ;




    public Product_ViewPagerAdapter(Context context, String[] aaa ){
        this.context = context;
        this.aaa = aaa;

    }
    @Override
    public int getCount() {

        return aaa.length;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
         return (view == (LinearLayout)object);
    }



    public Object instantiateItem(ViewGroup container, final int position) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.hahaha, container,
                false);




        Log.d("hahahahahhahaha", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");



        ImageView img_product = (ImageView) itemView.findViewById(R.id.img_product);
        TextView txtv_tensanpham = (TextView) itemView.findViewById(R.id.txtv_aaa);
        TextView txtv_giasanpham = (TextView) itemView.findViewById(R.id.txtv_giasanpham);
//        ListView lv_thongtinsanpham = (ListView) convertView.findViewById(R.id.lv_thongtinsanpham);
//        Button btn_tru = (Button) convertView.findViewById(R.id.btn_tru);
//        TextView txtv_soluongsanpham = (TextView) convertView.findViewById(R.id.txtv_soluongsanpham);
//        Button btn_cong = (Button) convertView.findViewById(R.id.btn_cong);
//        Button btn_addtocart = (Button) convertView.findViewById(R.id.btn_addtocart);
        txtv_tensanpham.setText(aaa[position]);
        Log.d("akjshdkjhasdasd", aaa.toString());
//       Product product = arrayList.get(position);
//
//        txtv_giasanpham.setText(product.price_product);
//        Picasso.with(this.context)
//                .load(product.img_product)
//                .placeholder(R.drawable.avatar)
//                .into(img_product);
        container.addView(itemView);

        return itemView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }




}
