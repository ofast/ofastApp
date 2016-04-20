package com.hung.ofastapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hung.ofastapp.Adapter.MyFragmentPagerAdapter;
import com.hung.ofastapp.Fragment.Home_fragment_giamgia;
import com.hung.ofastapp.Fragment.Home_fragment_loai;
import com.hung.ofastapp.Fragment.Home_fragment_thuonghieu;
import com.hung.ofastapp.Objects.*;
import com.hung.ofastapp.Objects.Product;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class Home extends ActionBarActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    SearchView sv_findproduct;
    Typeface tf1;
    public boolean doubleBackToExitPressedOnce = false;
    ViewPager viewPager;
    TabHost tabHost;
    ViewStub base_content;
    LinearLayout lnlo_giohang;
    ArrayList<com.hung.ofastapp.Objects.Product> arrayList = new ArrayList<Product>();

    Context context =this;

    TextView txtv_soluong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        base_content = (ViewStub) findViewById(R.id.base_content);
        base_content.setLayoutResource(R.layout.home_tabhost);
        View stinflated = base_content.inflate();

        /*------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------
        //--------------------------------------Ánh xạ----------------------------------------------
         ------------------------------------------------------------------------------------------*/
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        sv_findproduct = (SearchView) findViewById(R.id.sv_findproduct);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        lnlo_giohang = (LinearLayout) findViewById(R.id.lnlo_cart);
        txtv_soluong = (TextView) findViewById(R.id.txtv_soluong);

/*----------------------------------------------------------------------------------------------------------------------------
-----------------**********************PHẦN TOOLBAR***************---------------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------*/
        /*------------------------------------------------------------------------------------------
        -----------------------Unfocus để ko hiện Keyboard all time-------------------------------
        ------------------------------------------------------------------------------------------*/
        setupUI(findViewById(R.id.layout_home));

        sv_findproduct.setIconified(false);


        /*------------------------------------------------------------------------------------------
        --------------------------------------------------------------------------------------------
        -----------------Khai báo ToolBar, và các thành phần cần thiết cho 1 Navi-------------------
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
        -----------------------------Bỏ gạch chân trong SearchView----------------------------------
        ------------------------------------------------------------------------------------------*/
        tf1 = Typeface.createFromAsset(getAssets(),"VNF-Futura Regular.ttf");
        int searchPlateId = sv_findproduct.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlateView = sv_findproduct.findViewById(searchPlateId);
        searchPlateView.setBackgroundColor(Color.TRANSPARENT);




 /*________________________________________________________________________________________________
 ___________________________________________________________________________________________________*/



  /*------------------------------------------------------------------------------------------
          --------------------------------------------------------------------------------------------
        -----------------Set Font cho Hint và sự kiện thi ấn vào SearchView-------------------------
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
        lnlo_giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Order.class);
                intent.putExtra("LISTORDER", (Serializable) arrayList);
                startActivity(intent);
            }
        });
        /*------------------------------------------------------------------------------------------
        --------------------------------------------------------------------------------------------
        -------------------------Sự kiện thay đổi khi Typing SearchView-----------------------------
        ------------------------------------------------------------------------------------------*/
        sv_findproduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //            Khi sumbit Searchview
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), "Đã search", Toast.LENGTH_SHORT).show();

                return false;
            }

            //            Khi Typing  Searchview
            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getApplicationContext(), "Đang gõ", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
/*-------------------------------------------------------------------------------------------------------------------------------
-----------------**********************PHẦN CONTENT***************---------------------------------------------------------------
------------------------------------------------------------------------------------------------------------------------------*/
        initTabhost();
        initViewPager();


    }
    /*----------------------------------------------------------------------------------------------
    ------------------------------------------------------------------------------------------------
    --------------------------------Thêm Tab và Cài đặt tên cho chúng-------------------------------
    -----------------------------------------------------------------------------------------------*/
    private void initTabhost() {
        tabHost = (TabHost)findViewById(R.id.tabHost);
                                         /*Tab1 - Thương Hiệu*/
        tabHost.setup();
        //Tab Thương hiệu
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("Thương Hiệu");
    //    spec.setContent(new Intent(this, Home_fragment_loai.class));
        tabHost.addTab(tab1);
                                        /*Tab2 - Loại*/
        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2");
        //Tab Loại
        tab2 = tabHost.newTabSpec("tab2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("Loại");
//        spec.setContent(new Intent(this, Home_fragment_loai.class));
        tabHost.addTab(tab2);
        TabHost.TabSpec tab3 = tabHost.newTabSpec("tab3");
                                        /*Tab3 - Giảm giá*/
        tab3 = tabHost.newTabSpec("tab3");
        tab3.setContent(R.id.tab3);
        tab3.setIndicator("Giảm giá");
        tabHost.addTab(tab3);
                                /*Sự kiện khi ấn vào Tab*/
        tabHost.setOnTabChangedListener(this);

    }
    /*----------------------------------------------------------------------------------------------
    ------------------------------------------------------------------------------------------------
    -------------Set nội dung cho mỗi tab, tương ứng với 1 Fragment cho dễ quản lý -----------------
    -----------------------------------------------------------------------------------------------*/
    private void initViewPager() {

        List<Fragment> listFragments = new ArrayList<Fragment>();
        listFragments.add(new Home_fragment_thuonghieu());
        listFragments.add(new Home_fragment_loai());
        listFragments.add(new Home_fragment_giamgia());
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),listFragments);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(this);
    }


    /*----------------------------------------------------------------------------------------------
    ------------------------------------------------------------------------------------------------
    -----------------------------Kiểm tra xem Drawer có đang mở hay không---------------------------
    -----------------------------------------------------------------------------------------------*/
    public boolean CheckOpen(DrawerLayout drawer)
    {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            return true;
        }
        return false;
    }
    /*----------------------------------------------------------------------------------------------
    ------------------------------------------------------------------------------------------------
    --------------------Set sự kiện khi chọn các Items có trong Navigation -------------------------
    -----------------------------------------------------------------------------------------------*/
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
                Khi chọn Logout, thì Clear hết SharePreference rồi trở về trang Login & Register
            ---------------------------------------------------------------------------------------*/
            getSharedPreferences("LoginOneTimes",0).edit().clear().commit();
            Intent intent = new Intent(Home.this, Login_and_Register.class);
            startActivity(intent);
        }
        /*-----------------------Khi chọn xong thì đóng Navi-------------------*/
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /*----------------------------------------------------------------------------------------------
    ------------------------------------------------------------------------------------------------
    ------------------Set sự kiện khi chọn Tab, thay đổi Tab ---------------------------------------
    -----------------------------------------------------------------------------------------------*/
    @Override
    public void onTabChanged(String tabId) {
        int selectedItem = tabHost.getCurrentTab();
        viewPager.setCurrentItem(selectedItem);
        setupUI(findViewById(android.R.id.tabcontent));

    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        setTabColor(tabHost);
        setTextOfTabColor(tabHost);
    }

    @Override
    public void onPageSelected(int position) {

        tabHost.setCurrentTab(position);
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /*----------------------------------------------------------------------------------------------
     -----------------------------------------------------------------------------------------------
     ------------------Set màu sắc cho Tab và Thanh Toolbar khi tab nào được chọn ------------------
     -----------------------------------------------------------------------------------------------*/
    public void setTabColor(TabHost tabhost) {
//        tabhost.getTabWidget().setDividerDrawable();
//        tabhost.getTabWidget().getChildAt(tabHost.getCurrentTab()).getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE); //unselected
            /*-----------Tab1 - Thương HIệu được chọn----------*/
            if (tabhost.getCurrentTab() == 0) {
               //tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#e27070"));
                toolbar.setBackgroundColor(Color.parseColor("#e27070"));

            }
            /*-----------Tab2 - Loại được chọn----------*/
            else if (tabhost.getCurrentTab() == 1) {
                //tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#4FC3F7"));
                toolbar.setBackgroundColor(Color.parseColor("#039BE6"));

            }
            /*-----------Tab3 - Giảm giá được chọn----------*/
            else if (tabhost.getCurrentTab() == 2) {

               //  tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#00E676"));
                toolbar.setBackgroundColor(Color.parseColor("#689F39"));
            }
        }
    }
    /*----------------------------------------------------------------------------------------------
     -----------------------------------------------------------------------------------------------
     ------------------Set màu sắc cho Text khi Tab đó được chọn------------------------------------
     -----------------------------------------------------------------------------------------------*/
    public void setTextOfTabColor(TabHost tabhost) {

         /*--------Set màu #0000000 cho tất cả các tab---------------------------*/
        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#E0E0E0"));
            TextView tv1 = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title);
            if (tabhost.getCurrentTab() == 0) {

                tv1.setTextColor(Color.parseColor("#e27070"));
            }
            else if (tabhost.getCurrentTab() == 1) {
                tv1.setTextColor(Color.parseColor("#039BE6"));
            }
            else if (tabhost.getCurrentTab() == 2) {
                tv1.setTextColor(Color.parseColor("#689F39"));
            }
        }
         /*--------Set màu #FFFFFF cho tab được chọn---------------------------*/
//        TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
//        tv.setTextColor(Color.parseColor("#FFFFFF"));
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------Hàm ẩn Keyboard cho 1 Activity----------------------------------
    //----------------------------------------------------------------------------------------------
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    //----------------------------------------------------------------------------------------------
    //-------------Hàm ẩn Keyboard khi chạm vào màn hình chứa SearchView----------------------------
    //---------------------hoặc 1 layout gì đó------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof SearchView)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(Home.this);
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
    //------------------------------Khi quay lại App------------------------------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onResume() {
        setupUI(findViewById(R.id.layout_home));
        doubleBackToExitPressedOnce = false;
//        int soluongsanpham= 0;
//        SharedPreferences aaa = PreferenceManager.getDefaultSharedPreferences(context);
//        Gson gson = new Gson();
//        String json = aaa.getString("ListProduct", null);
//        if(json != null)
//        {
//            Type type = new TypeToken<ArrayList<Product>>() {}.getType();
//            arrayList = gson.fromJson(json, type);
//            for(int i = 0; i<arrayList.size(); i++)
//            {
//                soluongsanpham = soluongsanpham + arrayList.get(i).getNum_order();
//            }
//            Toast.makeText(getApplicationContext(),String.valueOf(arrayList.size()),Toast.LENGTH_SHORT).show();
//            txtv_soluong.setVisibility(View.VISIBLE);
//            txtv_soluong.setText(String.valueOf(soluongsanpham));
//        }

        super.onResume();
    }
    //----------------------------------------------------------------------------------------------
    //-----------------------------Sự kiện khi ấn Back của điện thoại-------------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {

        //-----------------------------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------
        //-----------------Khi nhấn Back, nếu Navi đang mở thì đóng nó lại--------------------------
        //------------------------------------------------------------------------------------------
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //------------------------------------------------------------------------------------------
            //------------------------------Lấy SharPre từ Login&Register-------------------------------
            //--------------Khi ấn Back, nếu từ TRYAPP thì sẽ trở về trang trước, xóa SharePre----------
            //-----------------------Nếu không thì thoát thạm thời khỏi appp----------------------------
            //==========================================================================================
            SharedPreferences settings = getSharedPreferences(Login_and_Register.TRYAPP, 0);
            boolean HasLoginByTryApp = settings.getBoolean("HasLoginByTryApp",false);
            if(HasLoginByTryApp==true)
            {
                getSharedPreferences("LoginByTryApp",0).edit().clear().commit();
                Intent intent = new Intent(Home.this, Login_and_Register.class);
                startActivity(intent);
            }
            else
            {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 1000);
                if (doubleBackToExitPressedOnce) {
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                }
                else {
                    Toast.makeText(this, "Press Again to Exit", Toast.LENGTH_SHORT).show();
                    this.doubleBackToExitPressedOnce = true;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        int soluongsanpham= 0;
//        SharedPreferences aaa = PreferenceManager.getDefaultSharedPreferences(context);
//        Gson gson = new Gson();
//        String json = aaa.getString("ListProduct", null);
//        if(json != null)
//        {
//            Type type = new TypeToken<ArrayList<Product>>() {}.getType();
//            arrayList = gson.fromJson(json, type);
//            for(int i = 0; i<arrayList.size(); i++)
//            {
//                soluongsanpham = soluongsanpham + arrayList.get(i).getNum_order();
//            }
//            Toast.makeText(getApplicationContext(),String.valueOf(arrayList.size()),Toast.LENGTH_SHORT).show();
//            txtv_soluong.setVisibility(View.VISIBLE);
//            txtv_soluong.setText(String.valueOf(soluongsanpham));
//        }

    }
}
