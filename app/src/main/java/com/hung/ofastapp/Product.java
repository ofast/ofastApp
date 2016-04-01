package com.hung.ofastapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.hung.ofastapp.Adapter.Product_ViewPagerAdapter;
import com.hung.ofastapp.CreateConnection.JSONParser;
import com.hung.ofastapp.CreateConnection.ofastURL;

import java.io.Serializable;
import java.util.ArrayList;

public class Product extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener{
    ViewPager viewPager;
    Product_ViewPagerAdapter adapter;
    ArrayList<com.hung.ofastapp.Objects.Product> arrayList = new ArrayList<com.hung.ofastapp.Objects.Product>();
    ArrayList<com.hung.ofastapp.Objects.Product> orderList = new ArrayList<com.hung.ofastapp.Objects.Product>();
    JSONParser image_par = new JSONParser();
    getInfo getInfo;
    public int pPostion = 0;
    ArrayList<String> image = new ArrayList<String>();
    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> price = new ArrayList<String>();
    ArrayList<Integer> number = new ArrayList<>();

    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    SearchView sv_findproduct;
    Typeface tf1;
    ProgressBar progress_loadproduct;

    LinearLayout lnlo_giohang;
    Button btn_addtocart;
    Button  btn_tru;
    Button btn_cong;
    TextView txtv_soluongsanpham;
    TextView txtv_soluong;

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
        txtv_soluong = (TextView) findViewById(R.id.txtv_soluong);

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
        viewPager.setOffscreenPageLimit(arrayList.size() - 1);

        //------------------------------------------------------------------------------------------
        //----------------------------S? ki?n khi nh?n gi? hàng----------------------------------------
        //------------------------------------------------------------------------------------------



        lnlo_giohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.hung.ofastapp.Objects.Product prod = new com.hung.ofastapp.Objects.Product("","","");
                Log.d("khang", "Loi gio hang 1");
                try {
//                    for(int i=0; i<arrayList.size();i++)
//                    {
//                        prod = arrayList.get(i);
//
//                        if(prod.isPicked())
//                        {
//                            image.add(prod.getImg_product());
//                            name.add(prod.getName_product());
//                            price.add(prod.getPrice_product());
//                            number.add(prod.getNum_order());
//                        }
//                    }
//                    Intent intent = new Intent(Product.this, Order.class);
//                    intent.putExtra("names", name);
//                    intent.putExtra("prices", price);
//                    intent.putExtra("images", image);
//                    intent.putExtra("numbers", number);
//                    startActivity(intent);

/*==============================================================================================
                                        TEST CODE
 =============================================================================================*/
                    Intent intent = new Intent(Product.this, Order.class);
                    intent.putExtra("LISTORDER", (Serializable) orderList);
                    startActivity(intent);

                } catch (Exception e){

                }

                }
        });

        //------------------------------------------------------------------------------------------
        //----------------------------S? ki?n khi nh?n AddtoCart--------------------------------------
        //------------------------------------------------------------------------------------------

        btn_addtocart = (Button) findViewById(R.id.btn_addtocart);
        btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.hung.ofastapp.Objects.Product mproduct = getProduct(pPostion);
                txtv_soluong.setVisibility(View.VISIBLE);


                //Add s?n ph?m
                if(!mproduct.isPicked()){
                    cartOrder += getProduct(pPostion).getNum_order();
                    btn_addtocart.setText("Cancel");
                    mproduct.setPicked(true);
                    orderList.add(mproduct);
                    txtv_soluong.setText(String.valueOf(cartOrder));
                    btn_tru.setEnabled(false);
                    btn_cong.setEnabled(false);

                }else{
                    //h?y s?n ph?m
                    cartOrder -= getProduct(pPostion).getNum_order();
                    mproduct.setPicked(false);
                    orderList.remove(mproduct);
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
            adapter = new Product_ViewPagerAdapter(getApplicationContext(),arrayList);
            viewPager.setAdapter(adapter);
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
}