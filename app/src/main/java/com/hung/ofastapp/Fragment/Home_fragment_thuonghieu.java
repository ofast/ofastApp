package com.hung.ofastapp.Fragment;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.hung.ofastapp.Adapter.Home_CustomGridviewAdapter;
import com.hung.ofastapp.CreateConnection.JSONParser;
import com.hung.ofastapp.CreateConnection.ofastURL;
import com.hung.ofastapp.Objects.ThuongHieu;
import com.hung.ofastapp.Product;
import com.hung.ofastapp.R;

import java.util.ArrayList;


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
    GridView grv_thuonghieu;
    ImageView img_banner;
    ArrayList<ThuongHieu>  arrayList ;
    Home_CustomGridviewAdapter adapter;
    JSONParser parser = new JSONParser();
    String serverData;
    int myLastVisiblePos = 0;
    LinearLayout main_layout;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment_thuonghieu, container, false);
        img_banner = (ImageView) rootView.findViewById(R.id.img_banner);

        layout_thuonghieu_progress = (LinearLayout) rootView.findViewById(R.id.layout_thuonghieu_progress);
        main_layout = (LinearLayout) rootView.findViewById(R.id.main_layout);
        grv_thuonghieu = (GridView) rootView.findViewById(R.id.grv_thuonghieu);
        arrayList = new ArrayList<ThuongHieu>();

            fetcher = new Fetcher();
            fetcher.execute(ofastURL.brand);
            parser = new JSONParser();

        //------------------------------------------Testcode----------------------------------------
        //------------------------------------------------------------------------------------------
        //Tạo mờ cho gridview
        grv_thuonghieu.setFadingEdgeLength(150);




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
                    if (firstVisibleItem ==1) {
                        img_banner.animate().translationY(-img_banner.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                        grv_thuonghieu.animate().translationY(-img_banner.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                    }
                    if (firstVisibleItem + visibleItemCount >= totalItemCount) {
//                        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Loading..Wait..", true);
//                        dialog.show();
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            public void run() {
                                ArrayList<ThuongHieu> moreList;
                                moreList = parser.Parse(serverData);
                                arrayList.addAll(moreList);
                                adapter = new Home_CustomGridviewAdapter(getActivity().getApplicationContext(), R.layout.home_content_custom_gridview, arrayList);
                                grv_thuonghieu.setAdapter(adapter);
                                setMarksGridScrolling(currentFirstVisPos + 1, 0);
                                Log.d("Scroll END", "END END END");
//                                dialog.dismiss();
//                            }
//                        }, 1000); //3s

                    }
                    Log.d("Scroll DOWN", "DOWN DOWN DOWN");

                }
                if (currentFirstVisPos < myLastVisiblePos) {

                    if (firstVisibleItem ==0) {
                        img_banner.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                        grv_thuonghieu.animate().translationY(0).setInterpolator(new AccelerateInterpolator(2)).start();
                    }
                    Log.d("Scroll UP", "UP UP UP");
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
                grv_thuonghieu.smoothScrollToPositionFromTop(position, offset, 1);
            }
        }, 1);

    }

}

