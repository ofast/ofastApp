package com.hung.ofastapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hung.ofastapp.Objects.ThuongHieu;
import com.hung.ofastapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hung on 12/8/2015.
 */
public class Home_CustomGridviewAdapter extends ArrayAdapter<ThuongHieu> {
    Context context;
    int LayoutID;
    ArrayList<ThuongHieu> arrayList;
    ViewHolder holder;
    public Home_CustomGridviewAdapter(Context context, int LayoutID, ArrayList<ThuongHieu> arrayList) {
        super(context, LayoutID, arrayList);
        this.context = context;
        this.LayoutID = LayoutID;
        this.arrayList = arrayList;
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

        Picasso.with(this.context)
                .load(thuongHieu.img)
                .resize(155, 155)
                .placeholder(R.drawable.avatar)
                .into(holder.img_anhthuonghieu);
        return convertView;
    }

}
