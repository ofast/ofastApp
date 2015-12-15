package com.hung.ofastapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.hung.ofastapp.Adapter.Product_ViewPagerAdapter;
import com.hung.ofastapp.CreateConnection.JSONParser;

import java.util.ArrayList;

public class Product extends ActionBarActivity {
    ViewPager viewPager;
    Product_ViewPagerAdapter adapter;
    ArrayList<String> arrayList = new ArrayList<>();
    JSONParser par_Image = new JSONParser();
    getInfo getInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.product);
        //Tạo Toolbar cho Giao diện
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Khai báo viewPager
        viewPager =(ViewPager)findViewById(R.id.view_pager);

        getInfo = new getInfo();
        getInfo.execute("http://o-fast.esy.es/frontend/web/index.php?r=catalog%2Fapplist");





    }
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Home.class);
        startActivityForResult(myIntent, 0);
        return true;

    }



    private class getInfo extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String data = JSONParser.getData(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            arrayList =par_Image.getImageProduct(s);
            Toast.makeText(getApplicationContext(),arrayList.toString(),Toast.LENGTH_SHORT).show();

            String[] myArray = new String[arrayList.size()];

            for(int j =0;j<arrayList.size();j++){
                myArray[j] = arrayList.get(j);
            }
            Toast.makeText(getApplicationContext(),myArray.toString(),Toast.LENGTH_SHORT).show();
//            adapter = new Product_ViewPagerAdapter(getApplicationContext(),myArray);
//            viewPager.setAdapter(adapter);


        }

    }
}
