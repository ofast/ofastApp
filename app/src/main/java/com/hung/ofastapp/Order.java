package com.hung.ofastapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Order extends ActionBarActivity {
    ListView lv_dathang;
    TextView txtv_info;
    TextView txtv_tongtien;
    Button btn_dathang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
        setContentView(R.layout.dathang);



        /* Khai báo toolbar và set button Back */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        /* Ánh xạ các thuộc tính */
//        txtv_info = (TextView) findViewById(R.id.txtv_info);
//        lv_dathang = (ListView) findViewById(R.id.lv_dathang);
//        txtv_tongtien = (TextView) findViewById(R.id.txtv_tongtien);
//        btn_dathang = (Button) findViewById(R.id.btn_dathang);


    }


    /* Sự kiện khi ấn Back Button */
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Product.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
}
