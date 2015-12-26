package com.hung.ofastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hung.ofastapp.Adapter.Product_CustomListviewDetail;

import java.util.ArrayList;

public class Order extends ActionBarActivity {
    ListView lv_dathang;
    TextView txtv_info;
    TextView txtv_tongtien;
    Button btn_dathang;
    ArrayList<com.hung.ofastapp.Objects.Product> arrayList = new ArrayList<>();
    Product_CustomListviewDetail adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.dathang);

//        Bundle bd = getIntent().getExtras();
//        arrayList = (ArrayList< com.hung.ofastapp.Objects.Product>)bd.getSerializable("key");


        /* Khai báo toolbar và set button Back */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


//        /* Ánh xạ các thuộc tính */
        txtv_info = (TextView) findViewById(R.id.txtv_info);
        lv_dathang = (ListView) findViewById(R.id.lv_dathang);
        txtv_tongtien = (TextView) findViewById(R.id.txtv_tongtien);
        btn_dathang = (Button) findViewById(R.id.btn_dathang);
//        adapter = new Product_CustomListviewDetail(this,R.layout.product_custom_listview_detail,arrayList);
//        lv_dathang.setAdapter(adapter);

    }


    /* Sự kiện khi ấn Back Button */
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Product.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
}
