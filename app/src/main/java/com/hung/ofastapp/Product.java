package com.hung.ofastapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hung.ofastapp.Adapter.Product_ViewPagerAdapter;
import com.hung.ofastapp.CreateConnection.JSONParser;

import java.util.ArrayList;

public class Product extends ActionBarActivity  {
    ViewPager viewPager;
    Product_ViewPagerAdapter adapter;
    ArrayList<com.hung.ofastapp.Objects.Product> arrayList = new ArrayList<com.hung.ofastapp.Objects.Product>();
    ArrayList<com.hung.ofastapp.Objects.Product> lProdcutPicked = new ArrayList<com.hung.ofastapp.Objects.Product>();
    JSONParser image_par = new JSONParser();
    getInfo getInfo;
    public int pPostion = 1;



    LinearLayout lnlo_giohang;
    Button btn_addtocart;
    Button  btn_tru;
    Button btn_cong;
    TextView txtv_soluongsanpham;
    TextView txtv_soluong;
    int a = 0;
    int c =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);
        //------------------------------------------------------------------------------------------
        //---------------------------Khai báo các thuộc tính----------------------------------------
        //------------------------------------------------------------------------------------------
       lnlo_giohang = (LinearLayout) findViewById(R.id.lnlo_giohang);
        btn_cong = (Button) findViewById(R.id.btn_cong);
        btn_tru = (Button) findViewById(R.id.btn_tru);
        txtv_soluongsanpham = (TextView) findViewById(R.id.txtv_soluongsanpham);
        txtv_soluong = (TextView) findViewById(R.id.txtv_soluong);
        //------------------------------------------------------------------------------------------
        //---------------Set Sự kiện khi ấn vào nút Cộng trừ các thứ--------------------------------
        //------------------------------------------------------------------------------------------
        String b = txtv_soluongsanpham.getText().toString();
        a = Integer.parseInt(b);
        btn_cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a = a+1;
                txtv_soluongsanpham.setText(String.valueOf(a));
            }
        });
        btn_tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a == 0) {
                    Toast.makeText(getApplicationContext(), "Số lượng tối thiểu, không thể trừ!", Toast.LENGTH_SHORT).show();
                } else {
                    a = a - 1;
                    txtv_soluongsanpham.setText(String.valueOf(a));
                }

            }
        });
        //------------------------------------------------------------------------------------------
        //----------------------------Tạo Toolbar cho Giao diện----------------------------------------
        //------------------------------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //------------------------------------------------------------------------------------------
        //----------------------------Khai báo viewPager----------------------------------------
        //------------------------------------------------------------------------------------------
        viewPager =(ViewPager)findViewById(R.id.view_pager);

        //------------------------------------------------------------------------------------------
        //-------------------Kết nối tới server, lấy dữ liệu rồi trả về Viewpager-------------------
        //------------------------------------------------------------------------------------------
        getInfo = new getInfo();
        getInfo.execute("http://o-fast.esy.es/frontend/web/index.php?r=catalog%2Fapplist");
        viewPager.setOffscreenPageLimit(arrayList.size()-1);
        //------------------------------------------------------------------------------------------
        //----------------------------Sự kiện khi nhấn giỏ hàng----------------------------------------
        //------------------------------------------------------------------------------------------
        lnlo_giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Product.this, Order.class);
                startActivity(intent);
            }
        });

        //------------------------------------------------------------------------------------------
        //----------------------------Sự kiện khi nhấn AddtoCart--------------------------------------
        //------------------------------------------------------------------------------------------

        btn_addtocart = (Button) findViewById(R.id.btn_addtocart);
        btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.hung.ofastapp.Objects.Product mproduct = getProduct(pPostion);
                txtv_soluong.setVisibility(View.VISIBLE);


                //Add sản phẩm
                if(btn_addtocart.getText() == "Add to CART" && !mproduct.isPicked()){
                    c+= a;
                    mproduct.setPicked(true);
                    btn_addtocart.setText("Cancel");
                    txtv_soluong.setText(String.valueOf(c));

                }else{
                    //hủy sản phẩm
                    c-= a;
                    mproduct.setPicked(false);
                    btn_addtocart.setText("Add to CART");
                    txtv_soluong.setText(String.valueOf(c));
                }
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                viewPager.getChildAt(position);
                pPostion = position;
                if(getProduct(position).isPicked()){
                    btn_addtocart.setText("Cancel");
                }else{
                    btn_addtocart.setText("Add to CART");
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Home.class);
        startActivityForResult(myIntent, 0);
        return true;

    }

    private class getInfo extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            String data = JSONParser.getData(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            arrayList = image_par.getImageProduct(s);
            adapter = new Product_ViewPagerAdapter(getApplicationContext(),arrayList);
            viewPager.setAdapter(adapter);
        }

    }

        public com.hung.ofastapp.Objects.Product getProduct(int position) {
            return arrayList.get(position);
        }

}