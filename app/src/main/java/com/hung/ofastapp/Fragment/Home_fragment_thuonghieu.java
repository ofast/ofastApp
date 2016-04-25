package com.hung.ofastapp.Fragment;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.GridLayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hung.ofastapp.Adapter.Home_CustomGridviewAdapter;
import com.hung.ofastapp.CreateConnection.JSONParser;
import com.hung.ofastapp.CreateConnection.ofastURL;
import com.hung.ofastapp.Objects.ThuongHieu;
import com.hung.ofastapp.Product;
import com.hung.ofastapp.R;

import java.util.ArrayList;


import in.srain.cube.views.GridViewWithHeaderAndFooter;

import static android.widget.AbsListView.*;

/**
 * Created by Hung on 12/5/2015.
 */
public class Home_fragment_thuonghieu extends Fragment{
    Fetcher fetcher =  null;
    private int mPreviousTotal = 0;
    private boolean mLoading = true;
//    Button btn_loadmore;
    LinearLayout layout_thuonghieu_progress;
    GridViewWithHeaderAndFooter grv_thuonghieu;
    ImageView img_banner;
    ArrayList<ThuongHieu>  arrayList ;
    Home_CustomGridviewAdapter adapter;
    JSONParser parser = new JSONParser();
    String serverData;
    int myLastVisiblePos = 0;
    int lastTopValue =0;
    LinearLayout main_layout;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment_thuonghieu, container, false);
        img_banner = (ImageView) rootView.findViewById(R.id.img_thuonghieu);

        layout_thuonghieu_progress = (LinearLayout) rootView.findViewById(R.id.layout_thuonghieu_progress);
        main_layout = (LinearLayout) rootView.findViewById(R.id.main_layout);
        grv_thuonghieu = (GridViewWithHeaderAndFooter) rootView.findViewById(R.id.grv_thuonghieu);
        arrayList = new ArrayList<ThuongHieu>();
            fetcher = new Fetcher();
            fetcher.execute(ofastURL.brand);
            parser = new JSONParser();


        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View headerView = layoutInflater.inflate(R.layout.banner_header, null);
        grv_thuonghieu.addHeaderView(headerView);

        //------------------------------------------Testcode----------------------------------------
        //------------------------------------------------------------------------------------------
        //Tạo mờ cho gridview
        grv_thuonghieu.setFadingEdgeLength(150);
//        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.flyin_right_to_left);
//        final GridLayoutAnimationController controller = new GridLayoutAnimationController(animation, .2f, .2f);
//        grv_thuonghieu.setLayoutAnimation(controller);



        //------------------------------------------------------------------------------------------
        grv_thuonghieu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity().getApplicationContext(), Product.class);
                intent.putExtra("brand_id", arrayList.get(position).getId());
                startActivity(intent);
            }
        });



        grv_thuonghieu.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int currentFirstVisPos = view.getFirstVisiblePosition();
                if (currentFirstVisPos > myLastVisiblePos) {
                    if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                        ArrayList<ThuongHieu> moreList;
                        moreList = parser.Parse(serverData);
                        if(moreList.isEmpty() == false)
                        {
                            arrayList.addAll(moreList);
                            adapter = new Home_CustomGridviewAdapter(getActivity().getApplicationContext(), R.layout.home_content_custom_gridview, arrayList);
                            grv_thuonghieu.setAdapter(adapter);
                            setMarksGridScrolling(currentFirstVisPos + 2, 0);
                        }


                        Log.d("Scroll END", "END END END");
                    }
                    Log.d("Scroll DOWN", "DOWN DOWN DOWN");
                }
                myLastVisiblePos = currentFirstVisPos;
            }
        });


        return rootView;
    }
    private class Fetcher extends AsyncTask<String,String,String> {

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
            fetcher = null;
            arrayList = parser.Parse(s);
            adapter = new Home_CustomGridviewAdapter(getActivity().getApplicationContext(),R.layout.home_content_custom_gridview,arrayList);
            grv_thuonghieu.setAdapter(adapter);
            serverData = s;
            showProgress(false);
        }
    }
    ///Hàm ẩn hiện Progress load ảnh và
    private void showProgress(final boolean show) {

        if (show == true) {

            layout_thuonghieu_progress.setVisibility(View.VISIBLE);
        }
        else
        {
            layout_thuonghieu_progress.setVisibility(View.GONE);
//            btn_loadmore.setVisibility(View.VISIBLE);
        }
    }

    //Hàm khi loadmore, ScrollView sẽ chạy từ đâu tới đâu!
    public void setMarksGridScrolling(final int position, final int offset) {
        grv_thuonghieu.setSelection(position);
        grv_thuonghieu.postDelayed(new Runnable() {
            @Override
            public void run() {
                grv_thuonghieu.smoothScrollToPositionFromTop(position, offset, 0);
            }
        }, 1);

    }

    @Override
    public void onResume() {
        super.onResume();

    }
}

