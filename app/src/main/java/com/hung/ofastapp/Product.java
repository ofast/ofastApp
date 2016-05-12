package com.hung.ofastapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hung.ofastapp.Adapter.Product_ViewPagerAdapter;
import com.hung.ofastapp.CreateConnection.JSONParser;
import com.hung.ofastapp.CreateConnection.ofastURL;
import java.lang.reflect.Type;
import java.nio.DoubleBuffer;
import java.util.ArrayList;

public class Product extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener {
    //Phần giao diện
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    ProgressBar progress_loadproduct;
    ImageButton imgbtn_giohang;
    ImageButton imgbtn_search;
    Button btn_addtocart;
    Button  btn_tru;
    Button btn_cong;
    TextView txtv_soluongsanpham;
    TextView txtv_tongsoluongsanpham;
    //Phần ViewPager
    ViewPager viewPager;
    Context context = this;
    Product_ViewPagerAdapter adapter;
    ArrayList<com.hung.ofastapp.Objects.Product> arrayList = new ArrayList<com.hung.ofastapp.Objects.Product>();
    ArrayList<com.hung.ofastapp.Objects.Product> orderList = new ArrayList<com.hung.ofastapp.Objects.Product>();
    JSONParser image_par = new JSONParser();
    getInfo getInfo;
    //Phần biến thêm
    public int pPostion = 0;
    String brand_id = "30";
    int cartOrder =0;
    ViewStub base_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        base_content = (ViewStub) findViewById(R.id.base_content);
        base_content.setLayoutResource(R.layout.product);
        View stinflated = base_content.inflate();
        /*------------------------------------------------------------------------------------------
        -----------------------------Khai báo các thuộc tính----------------------------------------
        ------------------------------------------------------------------------------------------*/
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progress_loadproduct = (ProgressBar) findViewById(R.id.progress_loadproduct);
        viewPager =(ViewPager)findViewById(R.id.view_pager);
        imgbtn_giohang = (ImageButton) findViewById(R.id.imgbtn_giohang);
//        imgbtn_search = (ImageButton) findViewById(R.id.imgbtn_search);
        btn_cong = (Button) findViewById(R.id.btn_cong);
        btn_tru = (Button) findViewById(R.id.btn_tru);
        btn_addtocart = (Button) findViewById(R.id.btn_addtocart);
        txtv_soluongsanpham = (TextView) findViewById(R.id.txtv_soluongsanpham);
        txtv_tongsoluongsanpham = (TextView) findViewById(R.id.txtv_tongsoluongsanpham);
                                    /*Ẩn Keyboard trên layout */
        setupUI(findViewById(R.id.layout_home));
                                    /*Toolbar*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                                    /*DrawerLayout*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
                                    /*Navigation*/
        navigationView.setNavigationItemSelectedListener(this);
        /*-----------------------------------------------------------------------------------------|
        |---------------------Kết nối tới Server, lấy dữ liệu rồi trả về ViewPager-----------------|
        |-----------------------------------------------------------------------------------------*/
        this.brand_id = getIntent().getStringExtra("brand_id");
        getInfo = new getInfo();
        getInfo.execute(ofastURL.brand_product + "id=" + brand_id);
        viewPager.setOffscreenPageLimit(arrayList.size() - 1);
        /*-----------------------------------------------------------------------------------------|
        |-----------------------------Sự kiện khi ấn giỏ hàng--------------------------------------|
        |-----------------------------------------------------------------------------------------*/
        imgbtn_giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sự kiện giỏ hàng: ", "True");
                try {
                    Intent intent = new Intent(Product.this, Order.class);
                    startActivity(intent);
//                    finish();
                } catch (Exception e){

                }

            }
        });
         /*-----------------------------------------------------------------------------------------|
        |-----------Sự kiện khi nhấn vào Button + và - để thêm, bớt SP-----------------------------|
        |-----------------------------------------------------------------------------------------*/
        //button +
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
        //button -
        btn_tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getProduct(pPostion).getNum_order() == 1) {
                    btn_tru.setEnabled(false);
                } else {
                    com.hung.ofastapp.Objects.Product curProduct = getProduct(pPostion);
                    curProduct.subProduct();
                    txtv_soluongsanpham.setText(String.valueOf(curProduct.getNum_order()));
                }
            }
        });

        /*Kiểm tra có tồn tại cái SharePreference nào được lưu hay không, nếu có thì tạo 1 braylist chứa tất cả nội dung có trong
        * SharePreference, sau đó, add chúng vào mảng orderlist chứa nhưng Object đã được chọn từ lúc trước, rồi xóa brraylisr đi
        *  đồng thời, xóa luôn những gì có trong SharePreference*/
        if(CheckContainShare() == true)
        {
            int prd_soluong=0;
            ArrayList<com.hung.ofastapp.Objects.Product> brrayList = new ArrayList<com.hung.ofastapp.Objects.Product>();
            Log.d("Có tồn tại Share: ","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            // Get arraylít<object> có trong sharepreference
            SharedPreferences aaa = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = aaa.getString("ListProduct", null);
            Type type = new TypeToken<ArrayList<com.hung.ofastapp.Objects.Product>>() {}.getType();
            //Lưu vào brraylist
            brrayList = gson.fromJson(json, type);
            //Chuyển tất cả các object có trong brraylist qua orderlist
            orderList.addAll(brrayList);
            for(int i=0; i<orderList.size();i++)
            {
                prd_soluong = prd_soluong + orderList.get(i).getNum_order();
            }
            txtv_tongsoluongsanpham.setVisibility(View.VISIBLE);
            txtv_tongsoluongsanpham.setText(String.valueOf(prd_soluong));
            //Xóa brraylist để đảm bảo là brraylist hoàn toàn trống, đễ chứa lại những object sau này.
            brrayList.clear();
        }
        /*-----------------------------------------------------------------------------------------|
        |-----------------------------Sự kiện nhấn AddtoCart---------------------------------------|
        |-----------------------------------------------------------------------------------------*/
        btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sự kiện thêm sản phẩm: ", "True");
                com.hung.ofastapp.Objects.Product mproduct = getProduct(pPostion);
                int tongsoluongsanpham = 0;
                for(int i=0; i<orderList.size(); i++)
                {
                    tongsoluongsanpham = tongsoluongsanpham + orderList.get(i).getNum_order();
                }
                //Add sản phẩmm
                if(!mproduct.isPicked()){
                    Log.d("Sự kiện ấn Add: ", "True");
                    cartOrder = getProduct(pPostion).getNum_order() + tongsoluongsanpham;
                    if(CheckAndAdd(orderList,mproduct) == true)
                    {
                        Log.d("Không addproduct ", "True");
                    }
                    else
                    {
                        Log.d("Đã addproduct ", "True");
                        orderList.add(mproduct);
                    }
                    txtv_tongsoluongsanpham.setVisibility(View.VISIBLE);
                    txtv_tongsoluongsanpham.setText(String.valueOf(cartOrder));
                    btn_addtocart.setText("Cancel");
                    mproduct.setPicked(true);
                    btn_tru.setEnabled(false);
                    btn_cong.setEnabled(false);
                }else{
                    //hủy sản phẩm
                    Log.d("Sự kiện ấn Cancel: ", "True");
                    cartOrder = -getProduct(pPostion).getNum_order() + tongsoluongsanpham;
                    mproduct.setPicked(false);
                    CheckAndRemove(orderList,mproduct);
                    btn_addtocart.setText("Add to CART");
                    txtv_tongsoluongsanpham.setText(String.valueOf(cartOrder));
                    btn_tru.setEnabled(true);
                    btn_cong.setEnabled(true);
                }
                /*-----------------------------------------------------------------------------------------|
                |--------------Khi đã có arraylist<Product> được chọn thì thêm vào Share-------------------|
                |-----------------------------------------------------------------------------------------*/
                SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(orderList);
                Log.d("ORDERLIST NUMBER: ",String.valueOf(orderList.size()));
                prefsEditor.putString("ListProduct", json);
                prefsEditor.commit();
            }
        });
        /*-----------------------------------------------------------------------------------------|
        |--------------------Sự kiện khi vuốt item trong Viewpager---------------------------------|
        |-----------------------------------------------------------------------------------------*/
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                Log.d("ID_PRODUCT",String.valueOf(arrayList.get(position).getId_product()));
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


    /*---------------------------------------------------------------------------------------------|
    |-----------------------------Chọn item trong Navigation---------------------------------------|
    |---------------------------------------------------------------------------------------------*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_news) {

        } else if (id == R.id.nav_shop) {

        } else if (id == R.id.nav_store) {

        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_share) {

        }else if(id==R.id.nav_logout){
    /*---------------------------------------------------------------------------------------------|
    |--------------Khi chọnn Logout, thì Clear hết SharePreference của USERNAME + PASSWORD --------|
    |------------------------rồi trở về trang Login & Register------------------------------------*/
            getSharedPreferences("LoginOneTimes",0).edit().clear().commit();
            Intent intent = new Intent(Product.this, Login_and_Register.class);
            startActivity(intent);
        }
        //Đóng Navi khi chọn xong một Item, có thể set đóng lúc nào, mở lúc nào tùy vào item chọn
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /*---------------------------------------------------------------------------------------------|
   |--------------------AsyncTask đọc file JSON từ Server trả về Arraylist-------------------------|
   |----------------------------------------------------------------------------------------------*/
    private class getInfo extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            showProgress(true);
        }

        @Override
        protected String doInBackground(String... params) {
            String data = JSONParser.getData(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            showProgress(false);
            arrayList = image_par.getImageProduct(s);
            if(CheckContainShare() == true)
            {
                CheckContainProduct();
    /* --------------------------------------------------------------------------------------------|
    |---------------------Kiểm tra phần tử đầu tiên có nằm trong Sharepreference không-------------|
    |-----------------------------nếu có - nếu không thì chạy hàm bên dưới-------------------------|
    |---------------------------------------------------------------------------------------------*/
                    CheckPicked(getProduct(0));
                    txtv_soluongsanpham.setText(String.valueOf(getProduct(pPostion).getNum_order()));
            }
            adapter = new Product_ViewPagerAdapter(getApplicationContext(),arrayList);
            adapter.notifyDataSetChanged();
            adapter.getItemPosition(arrayList.get(0));
            viewPager.setAdapter(adapter);
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        }
    }
    /*---------------------------------------------------------------------------------------------|
    |--------------------Hàm lấy 1 Product từ một vị trí Position----------------------------------|
    |---------------------------------------------------------------------------------------------*/
    public com.hung.ofastapp.Objects.Product getProduct(int position) {
        return arrayList.get(position);
    }

    /*---------------------------------------------------------------------------------------------|
    |--------------------Hàm ẩn Keyboard khi chạm vào màn hình ------------------------------------|
    |---------------------------------------------------------------------------------------------*/
    public void setupUI(View view) {
        Log.d("setupUI ","Hide KeyBooard");
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof SearchView)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Product.this);
                    return false;
                }

            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
    /*---------------------------------------------------------------------------------------------|
    |-------------------------------Hàm ẩn Keyboard cho 1 Activity---------------------------------|
    |---------------------------------------------------------------------------------------------*/
    public static void hideSoftKeyboard(Activity activity) {
        Log.d("hideSoftKeyboard","Hide KeyBooard");
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    /*---------------------------------------------------------------------------------------------|
    |--------------------------Hàm ẩn - hiện Progress cho Product.java-----------------------------|
    |---------------------------------------------------------------------------------------------*/
    private void showProgress(final boolean show) {
        if (show == true) {
            Log.d("showProgress","TRUE");
            progress_loadproduct.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
        }
        else
        {
            Log.d("showProgress","FALSE");
            progress_loadproduct.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
        }
    }
    /*---------------------------------------------------------------------------------------------|
    |--------------------------Kiểm tra sự tồn tại của SharePreference-----------------------------|
    |---------------------------Đây là một Share chứa ArrayList<Product>----------------------------*/
    private boolean CheckContainShare() {
        Log.d("CheckContainShare","TRUE");
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("ListProduct", "");
        if(json.isEmpty() == false)
        {
            Log.d("Có tồn tại Share","TRUE");
            return true;
        }
        else {
            Log.d("Không tồn tại Share","TRUE");
            return false;
        }
    }
    /*---------------------------------------------------------------------------------------------|
    |---------------------Kiểm tra sự tồn tại của 1 Product trong một list-------------------------|
    |----------------------Nếu có thì thay thế product đó vào phần tử trong list------------------*/
    public void CheckContainProduct()
    {
        Log.d("CheckContainProduct","ON");
        for (int i = 0; i<orderList.size(); i++)
        {
            for(int h = 0; h<arrayList.size(); h = h+1)
            {
                if(orderList.get(i).getId_product() == arrayList.get(h).getId_product())
                {
                    Log.d("Change Value Arraylist","BY VALUE IN ORDERLIST");
                    arrayList.set(h,orderList.get(i));
                }
            }
        }
    }
    /*---------------------------------------------------------------------------------------------|
    |--------Kiểm tra sự tồn tại của 1 Product trong một list, nếu có rồi thì khỏi ADD-------------|
    |---------------------------------------------------------------------------------------------*/
    public boolean CheckAndAdd(ArrayList<com.hung.ofastapp.Objects.Product> aaa, com.hung.ofastapp.Objects.Product bbb)
    {
        if(CheckContainShare()==true)
        {
            for (int i = 0; i<aaa.size(); i++)
            {
                if(aaa.get(i).getId_product() == bbb.getId_product())
                {
                    Log.d("Có tồn tại trong Share","--> KHông ADD");
                    return true;
                }
            }
        }
        return false;
    }
    /*---------------------------------------------------------------------------------------------|
    |--------Kiểm tra sự tồn tại của 1 Product trong một list, nếu có rồi thì xóa Product----------|
    |---------------------------------------------------------------------------------------------*/
    public void CheckAndRemove(ArrayList<com.hung.ofastapp.Objects.Product> aaa, com.hung.ofastapp.Objects.Product bbb)
    {
        Log.d("CheckAndRemove","ON");
        if(CheckContainShare()==true)
        {
            for (int i = 0; i<aaa.size(); i++)
            {
                if(aaa.get(i).getId_product() == bbb.getId_product())
                {
                    aaa.remove(i);
                    Log.d("REMOVED PRODUCT","ON");
                }
            }
        }
    }
    /*---------------------------------------------------------------------------------------------|
    |---------------------Kiểm tra sự tồn tại của 1 Product trong một list-------------------------|
    |-------------------------(Trả về True hoặc False)---------------------------------------------*/
    public boolean CheckContainProductTF(ArrayList<com.hung.ofastapp.Objects.Product> abc, com.hung.ofastapp.Objects.Product xyz)
    {
        for (int i = 0; i<abc.size(); i++)
        {
            if(orderList.get(i).getId_product() == xyz.getId_product())
            {
                return true;
            }
        }
        return false;
    }
    /*---------------------------------------------------------------------------------------------|
    |------Kiểm tra 1 Product có được chọn hay không?, nếu có - nếu không thực hiện hàm dưới-------|
    |---------------------------------------------------------------------------------------------*/
    public void CheckPicked(com.hung.ofastapp.Objects.Product product)
    {
        if(product.isPicked()){
            btn_addtocart.setText("Cancel");
            btn_tru.setEnabled(false);
            btn_cong.setEnabled(false);
        }else{
            btn_addtocart.setText("Add to CART");
            btn_tru.setEnabled(true);
            btn_cong.setEnabled(true);
        }
    }
    /*---------------------------------------------------------------------------------------------|
    |-----------------------------Hiệu ứng khi vuốt ViewPager--------------------------------------|
    |----------------------------------------------------------------------------------------------*/
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;
        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);
            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }
                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
    /*---------------------------------------------------------------------------------------------|
     |-------------------------------------On Stop-------------------------------------------------|
     |----------------------------------------------------------------------------------------------*/
    @Override
    protected void onStop() {
        super.onStop();
        orderList.clear();
    }
    /*---------------------------------------------------------------------------------------------|
    |-------------------------------------On Resume-------------------------------------------------|
    |----------------------------------------------------------------------------------------------*/
    @Override
    protected void onResume() {
        Log.d("ON RESUME"," ");
        if(CheckContainShare()==true) {
            Log.d("Check Share in", "ON RESUME = TRUE");
            orderList.clear();
            int prd_soluong = 0;
            ArrayList<com.hung.ofastapp.Objects.Product> brrayList = new ArrayList<com.hung.ofastapp.Objects.Product>();
            // Get arraylít<object> có trong sharepreference
            SharedPreferences aaa = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = aaa.getString("ListProduct", null);
            Type type = new TypeToken<ArrayList<com.hung.ofastapp.Objects.Product>>() {
            }.getType();
            //Lưu vào brraylist
            brrayList = gson.fromJson(json, type);
            orderList.addAll(brrayList);
            for (int i = 0; i < orderList.size(); i++) {
                prd_soluong = prd_soluong + orderList.get(i).getNum_order();
            }
            txtv_tongsoluongsanpham.setVisibility(View.VISIBLE);
            txtv_tongsoluongsanpham.setText(String.valueOf(prd_soluong));
            //Xóa brraylist để đảm bảo là brraylist hoàn toàn trống, đễ chứa lại những object sau này.
            brrayList.clear();
            CheckContainProduct();
            if(arrayList.size() != 0)
            {
                //Kiểm tra toàn bộ phần tử  khi trở lại
                for(int i = 0; i<arrayList.size(); i++)
                {
                    if(CheckContainProductTF(orderList,arrayList.get(i)) == true)
                    {
                        arrayList.get(i).setPicked(true);
                        CheckPicked(arrayList.get(i));
                    } else {
                        arrayList.get(i).setPicked(false);
                        CheckPicked(arrayList.get(i));
                    }
                }
                //Kiểm tra phần tử đầu tiên khi trở lại
                if(CheckContainProductTF(orderList, arrayList.get(viewPager.getCurrentItem())) == false)
                {
                    arrayList.get(viewPager.getCurrentItem()).setPicked(false);
                    CheckPicked(arrayList.get(viewPager.getCurrentItem()));
                }
                else {
                    arrayList.get(viewPager.getCurrentItem()).setPicked(true);
                    CheckPicked(arrayList.get(viewPager.getCurrentItem()));
                }
                Log.d("Số lượng hiện tại: ", String.valueOf(arrayList.get(viewPager.getCurrentItem()).getNum_order()));
                Log.d("Số lượng hiện thực tế: ", String.valueOf(pPostion));
                txtv_soluongsanpham.setText(String.valueOf(arrayList.get(viewPager.getCurrentItem()).getNum_order()));
            }
        }
        super.onResume();
    }
}