package com.hung.ofastapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.hung.ofastapp.Fragment.Home_fragment_thuonghieu;
import com.hung.ofastapp.Home;
import com.hung.ofastapp.Objects.ThuongHieu;
import com.hung.ofastapp.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hung on 12/8/2015.
 */
public class Home_CustomGridviewAdapter extends ArrayAdapter<ThuongHieu> {
    Home_fragment_thuonghieu home_fragment_thuonghieu;
    Context context;
    int LayoutID;
    ArrayList<ThuongHieu> arrayList;
    ViewHolder holder;
    final int a = 0;
    final int  b = 0;
    public Home_CustomGridviewAdapter(Context context, int LayoutID, ArrayList<ThuongHieu> arrayList, Home_fragment_thuonghieu fragment_thuonghieu) {
        super(context, LayoutID, arrayList);
        this.context = context;
        this.LayoutID = LayoutID;
        this.arrayList = arrayList;
        this.home_fragment_thuonghieu = fragment_thuonghieu;
    }

    private class ViewHolder {
        TextView txtv_tenthuonghieu;
        ImageView img_anhthuonghieu;
        TextView txtv_idthuonghieu;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Declaramos el ImageView

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.home_content_custom_gridview,null);
            holder = new ViewHolder();
            holder.img_anhthuonghieu = (ImageView) convertView.findViewById(R.id.img_thuonghieu);
            holder.txtv_tenthuonghieu = (TextView) convertView.findViewById(R.id.txtv_tenthuonghieu);
            holder.txtv_idthuonghieu = (TextView)convertView.findViewById(R.id.txtv_idthuonghieu);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ThuongHieu thuongHieu = arrayList.get(position);
        holder.txtv_tenthuonghieu.setText(thuongHieu.name);
        holder.txtv_idthuonghieu.setText(thuongHieu.id);


        //Thư viện Glide
        Glide.with(this.context)
                .load(thuongHieu.img)
                .fitCenter()
                .centerCrop()
                .animate(R.anim.flyin_right_to_left)
                .override((home_fragment_thuonghieu.getSizeWidthScreen()/2),(home_fragment_thuonghieu.getSizeHeightScreen())/3)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_anhthuonghieu);
        //Thư viện Picasso
//        Picasso.with(this.context)
//                .load(thuongHieu.img)
//                .resize(300, 200)
//                .centerCrop()
//                .noFade()
//                .placeholder(R.drawable.logo)
//                .into(holder.img_anhthuonghieu);
        return convertView;
    }

}
