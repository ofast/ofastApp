package com.hung.ofastapp;

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
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hung.ofastapp.Adapter.Home_Tab_Fragment_Adapter;
import com.hung.ofastapp.Fragment.Home_fragment_giamgia;
import com.hung.ofastapp.Fragment.Home_fragment_loai;
import com.hung.ofastapp.Fragment.Home_fragment_thuonghieu;
import com.hung.ofastapp.Objects.Product;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
public class Home extends ActionBarActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    //Phần giao diện
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    ViewStub base_content;
    TabHost tabHost;
    ViewPager viewPager;
    Context context =this;
    //Phần giỏ hàng
    TextView txtv_tongsoluongsanpham;
    ImageButton imgbtn_giohang;
    ArrayList<com.hung.ofastapp.Objects.Product> arrayList = new ArrayList<Product>();
    //Phần xử lý
    Typeface tf1;
    public boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        /*------------------------------------------------------------------------------------------
        -------------------------Tạo Content Nội Dung từ home_tabhost.xml--------------------------------
        ------------------------------------------------------------------------------------------*/
        base_content = (ViewStub) findViewById(R.id.base_content);
        base_content.setLayoutResource(R.layout.home_tabhost);
        View stinflated = base_content.inflate();
        /*------------------------------------------------------------------------------------------
        -----------------------------Khai báo các thuộc tính----------------------------------------
        ------------------------------------------------------------------------------------------*/
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        imgbtn_giohang = (ImageButton) findViewById(R.id.imgbtn_giohang);
        txtv_tongsoluongsanpham = (TextView) findViewById(R.id.txtv_tongsoluongsanpham);
                                    /*Toolbar*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
                                    /*DrawerLayout*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
                                    /*Navigation*/
        navigationView.setNavigationItemSelectedListener(this);

        /*-----------------------------------------------------------------------------------------|
        |-----------------------------Sự kiện khi ấn giỏ hàng--------------------------------------|
        |-----------------------------------------------------------------------------------------*/
        imgbtn_giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Order.class);
                startActivity(intent);
                 /*Khi truyền 1 đối tượng, 1 arraylist<object> ta sử dụng:
                intent.putExtra("LISTORDER", (Serializable) arrayList);*/
            }
        });
        /*Tabhost*/
        initTabhost();
        /*ViewPager*/
        initViewPager();


    }
    /*----------------------------------------------------------------------------------------------|
    |-----------------Khai báo các TabHost và Setup thông tin cho chúng-----------------------------|
    |----------------------------------------------------------------------------------------------*/
    private void initTabhost() {
        tabHost = (TabHost)findViewById(R.id.tabHost);
                                         /*Tab1 - Thương Hiệu*/
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("Thương Hiệu");
        tabHost.addTab(tab1);
                                        /*Tab2 - Loại*/
        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2");
        tab2 = tabHost.newTabSpec("tab2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("Loại");
        tabHost.addTab(tab2);
                                        /*Tab3 - Giảm giá*/
        TabHost.TabSpec tab3 = tabHost.newTabSpec("tab3");
        tab3 = tabHost.newTabSpec("tab3");
        tab3.setContent(R.id.tab3);
        tab3.setIndicator("Giảm giá");
        tabHost.addTab(tab3);
                                /*Sự kiện khi ấn vào Tab*/
        tabHost.setOnTabChangedListener(this);
    }
    /*----------------------------------------------------------------------------------------------|
    |---------------Đối với mỗi Tab sẽ tồn tại 1 Fragment nội dung cho chúng------------------------|
    |----------------------------------------------------------------------------------------------*/
    private void initViewPager() {
        List<Fragment> listFragments = new ArrayList<Fragment>();
        listFragments.add(new Home_fragment_thuonghieu());
        listFragments.add(new Home_fragment_loai());
        listFragments.add(new Home_fragment_giamgia());
        Home_Tab_Fragment_Adapter myFragmentPagerAdapter = new Home_Tab_Fragment_Adapter(getSupportFragmentManager(),listFragments);
        viewPager.setAdapter(myFragmentPagerAdapter);
        /*OffScreenPageLimit để đảm bảo không bị OnPause khi chuyển lướt qua số tab*/
        viewPager.setOffscreenPageLimit(2);
         /*Sự kiện khi vuốt Content Fragment*/
        viewPager.setOnPageChangeListener(this);
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
            /*--------------------------------------------------------------------------------------
                Khi chọn Logout, thì Clear hết SharePreference rồi trở về trang Login & Register
            ---------------------------------------------------------------------------------------*/
            context.getSharedPreferences("ListProduct",context.MODE_PRIVATE).edit().clear().commit();
            getSharedPreferences("LoginOneTimes",0).edit().clear().commit();
            Intent intent = new Intent(Home.this, Login_and_Register.class);
            startActivity(intent);
        }
        //Đóng Navi khi chọn xong một Item, có thể set đóng lúc nào, mở lúc nào tùy vào item chọn
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /*---------------------------------------------------------------------------------------------|
   |-----------------------------Sự kiện khi chọn Tab và Change Tab--------------------------------|
   |----------------------------------------------------------------------------------------------*/
    @Override
    public void onTabChanged(String tabId) {
        int selectedItem = tabHost.getCurrentTab();
        viewPager.setCurrentItem(selectedItem);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
         /*Set màu sắc cho Tabhost khi Scroll Tab*/
        setTabColor(tabHost);
        /*Set màu sắc cho Chữ trong Tab khi Scroll Tab*/
        setTextOfTabColor(tabHost);
    }

    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /*---------------------------------------------------------------------------------------------|
   |------------------Set Color for Tab and Toolbar when Tab Selected------------------------------|
   |----------------------------------------------------------------------------------------------*/

    public void setTabColor(TabHost tabhost) {
        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE); //unselected
            /*-----------Tab1 - Thương HIệu được chọn----------*/
            if (tabhost.getCurrentTab() == 0) {
                toolbar.setBackgroundColor(Color.parseColor("#e91e63"));
            }
            /*-----------Tab2 - Loại được chọn----------*/
            else if (tabhost.getCurrentTab() == 1) {
                toolbar.setBackgroundColor(Color.parseColor("#039BE6"));
            }
            /*-----------Tab3 - Giảm giá được chọn----------*/
            else if (tabhost.getCurrentTab() == 2) {
                toolbar.setBackgroundColor(Color.parseColor("#689F39"));
            }
            /*Ở trên chỉ thay đổi màu sắc cho Toolbar, nếu muốn thay đổi màu sắc cho TabHost, ta sử dụng:
            tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundColor(Color.parseColor("#00E676"));*/
        }
    }
    /*---------------------------------------------------------------------------------------------|
   |------------------Set Color for Text of Tab when Tab Selected----------------------------------|
   |----------------------------------------------------------------------------------------------*/
    public void setTextOfTabColor(TabHost tabhost) {

         /*--------Set màu #0000000 cho tất cả các tab---------------------------*/
        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
            TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#E0E0E0"));
            TextView tv1 = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title);
            /*-----------Tab1 - Thương HIệu được chọn----------*/
            if (tabhost.getCurrentTab() == 0) {

                tv1.setTextColor(Color.parseColor("#e91e63"));
            }
            /*-----------Tab2 - Loại được chọn----------*/
            else if (tabhost.getCurrentTab() == 1) {
                tv1.setTextColor(Color.parseColor("#039BE6"));
            }
            /*-----------Tab3 - Giảm giá được chọn----------*/
            else if (tabhost.getCurrentTab() == 2) {
                tv1.setTextColor(Color.parseColor("#689F39"));
            }
        }
    }
    /*---------------------------------------------------------------------------------------------|
    |--------------------------Kiểm tra sự tồn tại của SharePreference-----------------------------|
    |---------------------------Đây là một Share chứa ArrayList<Product>----------------------------*/
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
    /*---------------------------------------------------------------------------------------------|
    |----------------Hàm set nội dung có trên thanh Toobar, được lấy từ menu-----------------------|
    |---------------------------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_home, menu);
        MenuItem searchbutton = menu.findItem(R.id.btn_search);
        ImageButton btn_search = (ImageButton) MenuItemCompat.getActionView(searchbutton);
        return true;
    }
    /*---------------------------------------------------------------------------------------------|
    |----------------Hàm set sự kiện khi ấn vào nội dung được set ở trên---------------------------|
    |---------------------------------------------------------------------------------------------*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_search) {
            Intent intent = new Intent(Home.this, Search.class);
            startActivity(intent);
            finish();
            return true;
        }
        /*Ở đây, nếu có nhiều nội dung thì ứng với mỗi nội dung ta làm tương tự như trên*/
        return super.onOptionsItemSelected(item);
    }
    /*---------------------------------------------------------------------------------------------|
    |---------------------Khi trở lại App, Chạy hàm onResume---------------------------------------|
    |---------------------------------------------------------------------------------------------*/
    @Override
    protected void onResume() {
        super.onResume();
        doubleBackToExitPressedOnce = false;
        if(CheckContainShare() == true)
        {
            int soluongsanpham= 0;
            SharedPreferences aaa = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = aaa.getString("ListProduct", "");
            Type type = new TypeToken<ArrayList<Product>>() {}.getType();
            arrayList = gson.fromJson(json, type);
            if(arrayList.isEmpty())
            {
                txtv_tongsoluongsanpham.setVisibility(View.GONE);
            }
            else {
                for(int i = 0; i<arrayList.size(); i++)
                {
                    soluongsanpham = soluongsanpham + arrayList.get(i).getNum_order();
                }
                Toast.makeText(getApplicationContext(),String.valueOf(arrayList.size()),Toast.LENGTH_SHORT).show();
                txtv_tongsoluongsanpham.setVisibility(View.VISIBLE);
                txtv_tongsoluongsanpham.setText(String.valueOf(soluongsanpham));
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"hihihih", Toast.LENGTH_SHORT).show();
            txtv_tongsoluongsanpham.setVisibility(View.GONE);
        }
    }
    /*---------------------------------------------------------------------------------------------|
    |---------------------------Khi Activity bị che một phần---------------------------------------|
    |---------------------------------------------------------------------------------------------*/
    @Override
    protected void onPause() {
        super.onPause();
        if(CheckContainShare() == true)
        {
            int soluongsanpham= 0;
            SharedPreferences aaa = PreferenceManager.getDefaultSharedPreferences(context);
            Gson gson = new Gson();
            String json = aaa.getString("ListProduct", "");
            Type type = new TypeToken<ArrayList<Product>>() {}.getType();
            arrayList = gson.fromJson(json, type);
            for(int i = 0; i<arrayList.size(); i++)
            {
                soluongsanpham = soluongsanpham + arrayList.get(i).getNum_order();
            }
            txtv_tongsoluongsanpham.setVisibility(View.VISIBLE);
            txtv_tongsoluongsanpham.setText(String.valueOf(soluongsanpham));
        }
        else {
            Toast.makeText(getApplicationContext(),"hihihih", Toast.LENGTH_SHORT).show();
            txtv_tongsoluongsanpham.setVisibility(View.GONE);
        }
    }
     /*--------------------------------------------------------------------------------------------|
    |---------------------------Khi ấn nút Back của Device-----------------------------------------|
    |---------------------------------------------------------------------------------------------*/
    @Override
    public void onBackPressed() {
        /*Đóng Navigation*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            /*--------------------------------------------------------------------------------------
            ------------------------------Lấy SharPre từ Login&Register---------------------------
            --------------Khi ấn Back, nếu từ TRYAPP thì sẽ trở về trang trước, xóa SharePre------
            -----------------------Nếu không thì thoát tạm thời khỏi appp------------------------
            ======================================================================================*/
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
}



     /*---------------------------------------------------------------------------------------------|
//    |-------------------------------Hàm ẩn Keyboard cho 1 Activity---------------------------------|
//    |---------------------------------------------------------------------------------------------*/
//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
//    }
//    /*---------------------------------------------------------------------------------------------|
//    |--------------------Hàm ẩn Keyboard khi chạm vào màn hình ------------------------------------|
//    |---------------------------------------------------------------------------------------------*/
//    public void setupUI(View view) {
//        if(!(view instanceof SearchView)) {
//            view.setOnTouchListener(new View.OnTouchListener() {
//
//                public boolean onTouch(View v, MotionEvent event) {
//                    hideSoftKeyboard(Home.this);
//                    return false;
//                }
//
//            });
//        }
//        if (view instanceof ViewGroup) {
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                View innerView = ((ViewGroup) view).getChildAt(i);
//                setupUI(innerView);
//            }
//        }
//    }