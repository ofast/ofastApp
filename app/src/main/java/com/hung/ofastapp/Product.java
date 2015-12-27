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
    public int pPostion = 0;
    ArrayList<String> image = new ArrayList<String>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<Integer> number = new ArrayList<>();



    LinearLayout lnlo_giohang;
    Button btn_addtocart;
    Button  btn_tru;
    Button btn_cong;
    TextView txtv_soluongsanpham;
    TextView txtv_soluong;
    int cartOrder =0;

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
        btn_cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.hung.ofastapp.Objects.Product curProduct = getProduct(pPostion);
                curProduct.addOrder();
                txtv_soluongsanpham.setText(String.valueOf(curProduct.getNum_order()));
                if (getProduct(pPostion).getNum_order() != 1) {
                    btn_tru.setEnabled(true);
                }

            }
        });
        btn_tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getProduct(pPostion).getNum_order() == 1) {
                    btn_tru.setEnabled(false);
                } else {
                    com.hung.ofastapp.Objects.Product curProduct = getProduct(pPostion);
                    curProduct.subOrder();
                    txtv_soluongsanpham.setText(String.valueOf(curProduct.getNum_order()));
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
                com.hung.ofastapp.Objects.Product prod = new com.hung.ofastapp.Objects.Product("","","");

                    for(int i=0; i<arrayList.size();i++)
                    {
                        prod = arrayList.get(i);
                        if(prod.isPicked())
                        {
                            image.add(i,prod.getImg_product());
                            name.add(i,prod.getName_product());
                            price.add(i,prod.getPrice_product());
                            number.add(i,prod.getNum_order());
                        }
                    }
                    Intent intent = new Intent(Product.this, Order.class);
                    intent.putExtra("names",name);
                    intent.putExtra("prices",price);
                    intent.putExtra("images",image);
                    intent.putExtra("numbers",number);
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
                if(!mproduct.isPicked()){
                    cartOrder += getProduct(pPostion).getNum_order();
                    btn_addtocart.setText("Cancel");
                    mproduct.setPicked(true);
                    txtv_soluong.setText(String.valueOf(cartOrder));
                    btn_tru.setEnabled(false);
                    btn_cong.setEnabled(false);

                }else{
                    //hủy sản phẩm
                    cartOrder -= getProduct(pPostion).getNum_order();
                    mproduct.setPicked(false);
                    btn_addtocart.setText("Add to CART");
                    txtv_soluong.setText(String.valueOf(cartOrder));
                    btn_tru.setEnabled(true);
                    btn_cong.setEnabled(true);
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
                    btn_tru.setEnabled(false);
                    btn_cong.setEnabled(false);
                }else{
                    btn_addtocart.setText("Add to CART");
                    btn_tru.setEnabled(true);
                    btn_cong.setEnabled(true);
                }
                txtv_soluongsanpham.setText(String.valueOf(getProduct(pPostion).getNum_order()));
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