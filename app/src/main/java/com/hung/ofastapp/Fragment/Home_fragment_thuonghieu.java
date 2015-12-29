package com.hung.ofastapp.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.hung.ofastapp.Adapter.Home_CustomGridviewAdapter;
import com.hung.ofastapp.CreateConnection.JSONParser;
import com.hung.ofastapp.CreateConnection.ofastURL;
import com.hung.ofastapp.Objects.ThuongHieu;
import com.hung.ofastapp.Product;
import com.hung.ofastapp.R;

import java.util.ArrayList;

/**
 * Created by Hung on 12/5/2015.
 */
public class Home_fragment_thuonghieu extends Fragment{
    Fetcher fetcher =  null;
    Button btn_loadmore;
    LinearLayout layout_thuonghieu_progress;
    GridView grv_thuonghieu;
    ArrayList<ThuongHieu>  arrayList ;
    Home_CustomGridviewAdapter adapter;
    JSONParser parser = new JSONParser();
    String serverData;

    boolean isload = false;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment_thuonghieu, container, false);
        btn_loadmore = (Button) rootView.findViewById(R.id.btn_loadmore);
        layout_thuonghieu_progress = (LinearLayout) rootView.findViewById(R.id.layout_thuonghieu_progress);
        grv_thuonghieu = (GridView) rootView.findViewById(R.id.grv_thuonghieu);
        arrayList = new ArrayList<ThuongHieu>();

            fetcher = new Fetcher();
            fetcher.execute(ofastURL.brand);
            parser = new JSONParser();
            btn_loadmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgress(false);
                    ArrayList<ThuongHieu> moreList;
                    moreList = parser.Parse(serverData);
                    arrayList.addAll(moreList);
                    adapter = new Home_CustomGridviewAdapter(getActivity().getApplicationContext(), R.layout.home_content_custom_gridview, arrayList);
                    grv_thuonghieu.setAdapter(adapter);
                    setMarksGridScrolling(arrayList.size(), 0);
                    isload = true;
                }
            });
        grv_thuonghieu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity().getApplicationContext(), Product.class);
                intent.putExtra("brand_id",arrayList.get(position).getId());
                startActivity(intent);
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
            btn_loadmore.setVisibility(View.VISIBLE);
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

