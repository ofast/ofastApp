package com.hung.ofastapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hung.ofastapp.Adapter.Product_ViewPagerAdapter;
import com.hung.ofastapp.CreateConnection.JSONParser;
import com.hung.ofastapp.CreateConnection.ofastURL;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class Product extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ViewPager.OnPageChangeListener {
    ViewPager viewPager;
    Product_ViewPagerAdapter adapter;
    ArrayList<com.hung.ofastapp.Objects.Product> arrayList = new ArrayList<com.hung.ofastapp.Objects.Product>();
    ArrayList<com.hung.ofastapp.Objects.Product> orderList = new ArrayList<com.hung.ofastapp.Objects.Product>();
    JSONParser image_par = new JSONParser();
    getInfo getInfo;
    public int pPostion = 0;

    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    SearchView sv_findproduct;
    Typeface tf1;
    ProgressBar progress_loadproduct;
    Context context = this;
    LinearLayout lnlo_giohang;
    Button btn_addtocart;
    Button  btn_tru;
    Button btn_cong;
    TextView txtv_soluongsanpham;
    TextView txtv_tongsoluongsanpham;

    String brand_id = "30";
    ViewStub base_content;
    int cartOrder =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        base_content = (ViewStub) findViewById(R.id.base_content);
        base_content.setLayoutResource(R.layout.product);
        View stinflated = base_content.inflate();

        //------------------------------------------------------------------------------------------
        //----------------------------T?o Toolbar cho Giao di?n----------------------------------------
        //------------------------------------------------------------------------------------------


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        sv_findproduct = (SearchView) findViewById(R.id.sv_findproduct);
        progress_loadproduct = (ProgressBar) findViewById(R.id.progress_loadproduct);
        /*----------------------------------------------------------------------------------------------------------------------------
-----------------**********************PH?N TOOLBAR***************---------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------*/
        /*------------------------------------------------------------------------------------------
        -----------------------Unfocus ?? ko hi?n Keyboard all time-------------------------------
        ------------------------------------------------------------------------------------------*/
        setupUI(findViewById(R.id.layout_home));
        sv_findproduct.setIconified(false);
        /*------------------------------------------------------------------------------------------
        --------------------------------------------------------------------------------------------
        -----------------Khai báo ToolBar, và các thành ph?n c?n thi?t cho 1 Navi-------------------
        ------------------------------------------------------------------------------------------*/
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
        /*------------------------------------------------------------------------------------------
        --------------------------------------------------------------------------------------------
        -----------------------------B? g?ch chân trong SearchView----------------------------------
        ------------------------------------------------------------------------------------------*/
        tf1 = Typeface.createFromAsset(getAssets(), "VNF-Futura Regular.ttf");
        int searchPlateId = sv_findproduct.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = sv_findproduct.findViewById(searchPlateId);
        searchPlateView.setBackgroundColor(Color.TRANSPARENT);
  /*------------------------------------------------------------------------------------------
          --------------------------------------------------------------------------------------------
        -----------------Set Font cho Hint và s? ki?n thi ?n vào SearchView-------------------------
        ------------------------------------------------------------------------------------------*/
        AutoCompleteTextView searchtext = (AutoCompleteTextView) sv_findproduct.findViewById(sv_findproduct.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        searchtext.setTextSize(20);
        searchtext.setTypeface(tf1);
        searchtext.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        searchtext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (CheckOpen(drawer) == true) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                return false;
            }
        });
        /*------------------------------------------------------------------------------------------
        --------------------------------------------------------------------------------------------
        ---------------------------------Click vào Icon SearchView----------------------------------
        ------------------------------------------------------------------------------------------*/
        sv_findproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckOpen(drawer) == true) {
                    drawer.closeDrawer(GravityCompat.START);

                }
            }
        });

        /*------------------------------------------------------------------------------------------
        --------------------------------------------------------------------------------------------
        -------------------------S? ki?n thay ??i khi Typing SearchView-----------------------------
        ------------------------------------------------------------------------------------------*/
        sv_findproduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //            Khi sumbit Searchview
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), "?ã search", Toast.LENGTH_SHORT).show();

                return false;
            }

            //            Khi Typing  Searchview
            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getApplicationContext(), "?ang gõ", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        //------------------------------------------------------------------------------------------
        //---------------------------Khai báo các thu?c tính----------------------------------------
        //------------------------------------------------------------------------------------------
        lnlo_giohang = (LinearLayout) findViewById(R.id.lnlo_cart);
        btn_cong = (Button) findViewById(R.id.btn_cong);
        btn_tru = (Button) findViewById(R.id.btn_tru);
        txtv_soluongsanpham = (TextView) findViewById(R.id.txtv_soluongsanpham);
        txtv_tongsoluongsanpham = (TextView) findViewById(R.id.txtv_tongsoluongsanpham);

        //------------------------------------------------------------------------------------------
        //---------------Set S? ki?n khi ?n vào nút C?ng tr? các th?--------------------------------
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
        //----------------------------Khai báo viewPager----------------------------------------
        //------------------------------------------------------------------------------------------
        viewPager =(ViewPager)findViewById(R.id.view_pager);

        //------------------------------------------------------------------------------------------
        //-------------------K?t n?i t?i server, l?y d? li?u r?i tr? v? Viewpager-------------------
        //------------------------------------------------------------------------------------------
        this.brand_id = getIntent().getStringExtra("brand_id");
        getInfo = new getInfo();
        getInfo.execute(ofastURL.brand_product + "id=" + brand_id);
//        viewPager.setCurrentItem(0);

        viewPager.setOffscreenPageLimit(arrayList.size() - 1);

        //------------------------------------------------------------------------------------------
        //----------------------------Sự kiện khi ấn giỏ hàng----------------------------------------
        //------------------------------------------------------------------------------------------
        lnlo_giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Sự kiện giỏ hàng: ", "True");
                try {
                    Intent intent = new Intent(Product.this, Order.class);
                    startActivity(intent);
                } catch (Exception e){

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
                Log.d("Đậu cô ve","Đậu xanh");
            }
            txtv_tongsoluongsanpham.setVisibility(View.VISIBLE);
            txtv_tongsoluongsanpham.setText(String.valueOf(prd_soluong));
            //Xóa brraylist để đảm bảo là brraylist hoàn toàn trống, đễ chứa lại những object sau này.
            brrayList.clear();

        }
        //------------------------------------------------------------------------------------------
        //----------------------------Sự kiện nhấn AddtoCart--------------------------------------
        //------------------------------------------------------------------------------------------
        btn_addtocart = (Button) findViewById(R.id.btn_addtocart);
        btn_addtocart.setOnClickListener(this);
        //------------------------------------------------------------------------------------------
        //---------------------------Sự kiện khi vuốt item trong Viewpager--------------------------
        //------------------------------------------------------------------------------------------

        viewPager.setOnPageChangeListener(this);
    }




    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), Home.class);
        startActivityForResult(myIntent, 0);
        return true;

    }

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
            /*--------------------------------------------------------------------------------------
                Khi ch?n Logout, thì Clear h?t SharePreference r?i tr? v? trang Login & Register
            ---------------------------------------------------------------------------------------*/
            getSharedPreferences("LoginOneTimes",0).edit().clear().commit();
            Intent intent = new Intent(Product.this, Login_and_Register.class);
            startActivity(intent);
        }
        /*-----------------------Khi ch?n xong thì ?óng Navi-------------------*/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




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
                viewPager.setCurrentItem(0);
            }
            adapter = new Product_ViewPagerAdapter(getApplicationContext(),arrayList);
            viewPager.setAdapter(adapter);
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            viewPager.setClipChildren(false);

        }
    }

    public com.hung.ofastapp.Objects.Product getProduct(int position) {
        return arrayList.get(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /*----------------------------------------------------------------------------------------------
   ------------------------------------------------------------------------------------------------
   -----------------------------Ki?m tra xem Drawer có ?ang m? hay không---------------------------
   -----------------------------------------------------------------------------------------------*/
    public boolean CheckOpen(DrawerLayout drawer)
    {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            return true;
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------
    //-------------Hàm ?n Keyboard khi ch?m vào màn hình ch?a SearchView----------------------------
    //---------------------ho?c 1 layout gì ?ó------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof SearchView)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Product.this);
                    sv_findproduct.setFocusable(false);
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

    //----------------------------------------------------------------------------------------------
    //------------------------------Hàm ?n Keyboard cho 1 Activity----------------------------------
    //----------------------------------------------------------------------------------------------
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    //----------------------------------------------------------------------------------------------
    //------------------------Hàm ?n + hi?n Progress for ViewPager----------------------------------
    //----------------------------------------------------------------------------------------------
    private void showProgress(final boolean show) {

        if (show == true) {

            progress_loadproduct.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
        }
        else
        {
            progress_loadproduct.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
        }
    }
    private boolean CheckContainShare() {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("ListProduct", "");
        if(json.isEmpty() == false)
        {
            return true;
        }
        else return false;
    }
    public boolean CheckAndAdd(ArrayList<com.hung.ofastapp.Objects.Product> aaa, com.hung.ofastapp.Objects.Product bbb, com.hung.ofastapp.Objects.Product ccc)
    {

        if(CheckContainShare()==true)
        {
            for (int i = 0; i<aaa.size(); i++)
            {
                if(aaa.get(i).getId_product() == bbb.getId_product())
                {
                    return true;
                }
            }
        }
        return false;
    }
    public void CheckContainProduct()
    {
        for (int i = 0; i<orderList.size(); i++)
        {
            Log.d("orderlist.id:", String.valueOf(orderList.get(i).getId_product()));
            for(int h = 0; h<arrayList.size(); h = h+1)
            {
                Log.d("arraylist.id:", String.valueOf(arrayList.get(h).getId_product()));
                if(orderList.get(i).getId_product() == arrayList.get(h).getId_product())
                {
                    arrayList.set(h,orderList.get(i));



//                    arrayList.get(h).setNum_order(orderList.get(i).getNum_order());
//                    Log.d("soluongsanpham:", String.valueOf(arrayList.get(h).getNum_order()));
//
//                        arrayList.get(h).setPicked(true);
////
////
////                    txtv_soluongsanpham.setText(String.valueOf(arrayList.get(h).getNum_order()));
////                    Log.d("CheckContainProduct:", String.valueOf(arrayList.get(h).getNum_order()));
//                    if(arrayList.get(h).isPicked() == true){
//                        btn_addtocart.setText("Cancel");
//                        btn_tru.setEnabled(false);
//                        btn_cong.setEnabled(false);
//
//                    }
//                    else{
//                        btn_addtocart.setText("Add to CART");
//                        btn_tru.setEnabled(true);
//                        btn_cong.setEnabled(true);
//                    }
                }




            }
        }

    }
    public void CheckAndRemove(ArrayList<com.hung.ofastapp.Objects.Product> aaa, com.hung.ofastapp.Objects.Product bbb)
    {

        if(CheckContainShare()==true)
        {
            for (int i = 0; i<aaa.size(); i++)
            {
                if(aaa.get(i).getId_product() == bbb.getId_product())
                {
                    aaa.remove(i);
                }
            }
        }

    }


    ///On Click Listener of btn_addtoCart
    @Override
    public void onClick(View v) {
        Log.d("Sự kiện thêm sản phẩm: ", "True");
        com.hung.ofastapp.Objects.Product mproduct = getProduct(pPostion);
        int tongsoluongsanpham = 0;
        for(int i=0; i<orderList.size(); i++)
        {
            tongsoluongsanpham = tongsoluongsanpham + orderList.get(i).getNum_order();

        }

        //Add s?n ph?m
        if(!mproduct.isPicked()){
            com.hung.ofastapp.Objects.Product ccc = new com.hung.ofastapp.Objects.Product(0,"","",0,"");
            Log.d("Sự kiện ấn Add: ", "True");
            cartOrder = getProduct(pPostion).getNum_order() + tongsoluongsanpham;

            if(CheckAndAdd(orderList,mproduct,ccc) == true)
            {
                Log.d("Không addproduct ", "True");
            }
            else
            {
                Log.d("Đã addproduct ", "True");
                orderList.add(mproduct);
            }

            txtv_tongsoluongsanpham.setText(String.valueOf(cartOrder));
            btn_addtocart.setText("Cancel");
            mproduct.setPicked(true);
            btn_tru.setEnabled(false);
            btn_cong.setEnabled(false);

        }else{
            com.hung.ofastapp.Objects.Product ccc = new com.hung.ofastapp.Objects.Product(0,"","",0,"");
            //h?y s?n ph?m
            Log.d("Sự kiện ấn Cancel: ", "True");
            cartOrder = -getProduct(pPostion).getNum_order() + tongsoluongsanpham;
            mproduct.setPicked(false);
            CheckAndRemove(orderList,mproduct);
            btn_addtocart.setText("Add to CART");
            txtv_tongsoluongsanpham.setText(String.valueOf(cartOrder));
            btn_tru.setEnabled(true);
            btn_cong.setEnabled(true);
        }
        //------------------------------------------------------------------------------------------
        //-----------------Khi đã có arraylist<Product> được chọn thì thêm vào Share-----------------
        //------------------------------------------------------------------------------------------
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(orderList);
        Log.d("ORDERLIST NUMBER: ",String.valueOf(orderList.size()));
        prefsEditor.putString("ListProduct", json);
        prefsEditor.commit();
    }


    //----------------------------------------------------------------------------------------------
    //--------------------------------Viewpager.SetOnPageChange-------------------------------------
    //----------------------------------------------------------------------------------------------
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

    //----------------------------------------------------------------------------------------------
    //--------------Lượm được hàng, đây là hiệu ứng khi vuốt Viewpager------------------------------
    //----------------------------------------------------------------------------------------------
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
}