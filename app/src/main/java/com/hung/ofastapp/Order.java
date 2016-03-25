package com.hung.ofastapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hung.ofastapp.Adapter.Product_CustomListviewDetail;

import java.util.ArrayList;

public class Order extends ActionBarActivity {
    ListView lv_dathang;
    TextView txtv_tongtien;
    TextView title;
    Button btn_dathang;
    ArrayList<com.hung.ofastapp.Objects.Product> arrayList = new ArrayList<>();
    Product_CustomListviewDetail adapter;
    ArrayList<Integer> soluong = new ArrayList<>();
    ArrayList<String>  ten= new ArrayList<>();
    ArrayList<String> hinh = new ArrayList<>();
    ArrayList<String> gia = new ArrayList<>();
    ArrayList<String> linkimage = new ArrayList<>();
    float tongtien = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dathang);
        Typeface  tf1 = Typeface.createFromAsset(getAssets(), "VKORIN.TTF");
          /* Khai báo toolbar và set button Back */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

            /* Ánh xạ các thuộc tính */
        lv_dathang = (ListView) findViewById(R.id.lv_dathang);
        lv_dathang = (ListView) findViewById(R.id.lv_dathang);
        txtv_tongtien = (TextView) findViewById(R.id.txtv_tongtien);
        btn_dathang = (Button) findViewById(R.id.btn_dathang);
        title = (TextView) findViewById(R.id.txtv_info);
        title.setTypeface(tf1);

                    /*Set font chữ các thứ*/


        Bundle bd = getIntent().getExtras();
//        ten = bd.getStringArrayList("names");
//        gia = bd.getStringArrayList("prices");
//        hinh = bd.getStringArrayList("images");
//        soluong = bd.getIntegerArrayList("numbers");

//        for (int i = 0; i < ten.size(); i++) {
//            com.hung.ofastapp.Objects.Product product =
//                    new com.hung.ofastapp.Objects.Product(hinh.get(i), ten.get(i), soluong.get(i), gia.get(i));
//            arrayList.add(product);
//        }

//        adapter = new Product_CustomListviewDetail(this, R.layout.product_custom_listview_detail, arrayList);
//        lv_dathang.setAdapter(adapter);

//        for(int i = 0; arr; i++)
//        {
//            tongtien = tongtien + Float.parseFloat(gia.get(i)) * soluong.get(i);
//        }

        /* =======================================================================================
                                                TEST CODE
        ========================================================================================*/

        arrayList = (ArrayList<com.hung.ofastapp.Objects.Product>) bd.getSerializable("LISTORDER");
        adapter = new Product_CustomListviewDetail(this, R.layout.product_custom_listview_detail, arrayList);
        lv_dathang.setAdapter(adapter);

        /*
            = Tong Tien =
         */

        for (com.hung.ofastapp.Objects.Product prod : arrayList
             ) {
            if(prod.isPicked()){
                tongtien = tongtien +  prod.getNum_order() * Float.parseFloat(prod.getPrice_product());
            }
        }


        txtv_tongtien.setText(String.valueOf(tongtien)+ "00VNĐ");
        btn_dathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Đã đặt hàng, Chúng tôi sẽ liên hệ bạn ngay bây giờ", Toast.LENGTH_LONG).show();
            }
        });
    }

    /* Sự kiện khi ấn Back Button */
    public boolean onOptionsItemSelected(MenuItem item){
     super .onBackPressed();
        return true;
    }
}