package com.hung.ofastapp;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.hung.ofastapp.Adapter.Fragment_Adapter;
import com.hung.ofastapp.Fragment.Login_fragment;
import com.hung.ofastapp.Fragment.Register_fragment;

import java.util.ArrayList;
import java.util.List;

public class Login_And_Register extends ActionBarActivity implements ViewPager.OnPageChangeListener {

    Toolbar toolbar;
    ViewPager viewPager;
    Context context =  this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_and_register);


        //Ánh xạ
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        //Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Change color of BackButton in Toolbar
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

//        Intent mIntent = getIntent();
//        int intValue = mIntent.getIntExtra("CurrentTab", 0);


        //ViewPager
        initViewPager();
        int intValue = getIntent().getIntExtra("CurrentTab",0);
        Log.d("IntValue", String.valueOf(intValue));
        viewPager.setCurrentItem(intValue);
    }

    private void initViewPager() {
        List<Fragment> listFragments = new ArrayList<Fragment>();
        listFragments.add(new Login_fragment());
        listFragments.add(new Register_fragment());
        Fragment_Adapter myFragmentPagerAdapter = new Fragment_Adapter(getSupportFragmentManager(),listFragments);
        viewPager.setAdapter(myFragmentPagerAdapter);
        /*OffScreenPageLimit để đảm bảo không bị OnPause khi chuyển lướt qua số tab*/
        viewPager.setOffscreenPageLimit(1);
         /*Sự kiện khi vuốt Content Fragment*/
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        viewPager.getChildAt(position);
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //Hàm sử dụng cho Fragment_Register khi đăng ký thành công thì chuyển tới đây!
    public void selectFragment(int position){
        viewPager.setCurrentItem(position, true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
