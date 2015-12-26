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

public class Product extends ActionBarActivity implements ViewPager.OnPageChangeListener {
    ViewPager viewPager;
    Product_ViewPagerAdapter adapter;
    ArrayList<com.hung.ofastapp.Objects.Product> arrayList = new ArrayList<com.hung.ofastapp.Objects.Product>();
    ArrayList<com.hung.ofastapp.Objects.Product> lProdcutPicked = new ArrayList<com.hung.ofastapp.Objects.Product>();
    ArrayList<com.hung.ofastapp.Objects.Product> intentPro = new ArrayList<>();
    JSONParser image_par = new JSONParser();
    getInfo getInfo;
    public int pPostion = 1;
    com.hung.ofastapp.Objects.Product mproduct;


    LinearLayout lnlo_giohang;
    Button btn_addtocart;
    Button  btn_tru;
    Button btn_cong;
    TextView txtv_soluongsanpham;
    TextView txtv_soluong;
    int soluongsanpham = 0;
    int tongsoluongsanpham =0;


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
        //----------------------------Tạo Toolbar cho Giao diện----------------------------------------
        //------------------------------------------------------------------------------------------
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //------------------------------------------------------------------------------------------
        //----------------------------Khai báo viewPager----------------------------------------
        //------------------------------------------------------------------------------------------
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        //------------------------------------------------------------------------------------------
        //-------------------Kết nối tới server, lấy dữ liệu rồi trả về Viewpager-------------------
        //------------------------------------------------------------------------------------------
        getInfo = new getInfo();
        getInfo.execute("http://o-fast.esy.es/frontend/web/index.php?r=catalog%2Fapplist");
        viewPager.setOffscreenPageLimit(arrayList.size() - 1);
        viewPager.setOnPageChangeListener(this);
        //------------------------------------------------------------------------------------------
        //----------------------------Sự kiện khi nhấn giỏ hàng----------------------------------------
        //------------------------------------------------------------------------------------------
        lnlo_giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Product.this, Order.class);
//                intent.putExtra("key",  arrayList);
//                startActivity(intent);
            }
        });


        //------------------------------------------------------------------------------------------
        //---------------Set Sự kiện khi ấn vào nút Cộng trừ các thứ--------------------------------
        //------------------------------------------------------------------------------------------

        btn_cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soluongsanpham = Integer.parseInt(txtv_soluongsanpham.getText().toString());
                soluongsanpham = soluongsanpham + 1;
                txtv_soluongsanpham.setText(String.valueOf(soluongsanpham));
            }
        });
        btn_tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soluongsanpham = Integer.parseInt(txtv_soluongsanpham.getText().toString());
                if (soluongsanpham == 0) {
                    Toast.makeText(getApplicationContext(), "Số lượng tối thiểu, không thể trừ!", Toast.LENGTH_SHORT).show();
                } else {
                    soluongsanpham = soluongsanpham - 1;
                    txtv_soluongsanpham.setText(String.valueOf(soluongsanpham));

                }

            }
        });

        //------------------------------------------------------------------------------------------
        //----------------------------Sự kiện khi nhấn AddtoCart--------------------------------------
        //------------------------------------------------------------------------------------------
        btn_addtocart = (Button) findViewById(R.id.btn_addtocart);
        btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mproduct = getProduct(pPostion);
                txtv_soluong.setVisibility(View.VISIBLE);


                //Add sản phẩm
                if (btn_addtocart.getText() == "Add to CART" && !mproduct.isPicked()) {

                    tongsoluongsanpham =  mproduct.getSoluong_product() +soluongsanpham;
                    txtv_soluong.setText(String.valueOf(tongsoluongsanpham));
                    mproduct.setSoluong_product(soluongsanpham);


                    Toast.makeText(getApplicationContext(),String.valueOf(mproduct.getSoluong_product()),Toast.LENGTH_SHORT).show();

                    mproduct.setPicked(true);
                    btn_addtocart.setText("Cancel");
                    btn_cong.setEnabled(false);
                    btn_tru.setEnabled(false);


                } else  {
                    tongsoluongsanpham = mproduct.getSoluong_product() - soluongsanpham;
                    txtv_soluong.setText(String.valueOf(tongsoluongsanpham));
                    mproduct.setSoluong_product(soluongsanpham);


                    mproduct.setPicked(false);
                    btn_addtocart.setText("Add to CART");
                    btn_cong.setEnabled(true);
                    btn_tru.setEnabled(true);
                }
            }
        });





    }
    //----------------------------------------------------------------------------------------------
    //--------------------------Sự kiện của Viewpager --------------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
        this.viewPager.setCurrentItem(position);


        pPostion = position;
        if(getProduct(position).isPicked()){

            btn_addtocart.setText("Cancel");
            btn_cong.setEnabled(false);
            btn_tru.setEnabled(false);

        }else{
            btn_addtocart.setText("Add to CART");
            btn_cong.setEnabled(true);
            btn_tru.setEnabled(true);

        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }
    //----------------------------------------------------------------------------------------------
    //---------------Sự kiện khi ấn nut Back trên thanh Toolbar-------------------------------------
    //----------------------------------------------------------------------------------------------
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Home.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
    //----------------------------------------------------------------------------------------------
    //---------------Lấy đối tượng ProJect từ 1 vị trí cụ thể---------------------------------------
    //----------------------------------------------------------------------------------------------
    public com.hung.ofastapp.Objects.Product getProduct(int position) {
        return arrayList.get(position);
    }

    //----------------------------------------------------------------------------------------------
    //--------------------------Lấy thông tin từ trên Server trả về --------------------------------
    //----------------------------------------------------------------------------------------------
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
}