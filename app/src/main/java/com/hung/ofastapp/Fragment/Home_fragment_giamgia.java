package com.hung.ofastapp.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hung.ofastapp.R;

/**
 * Created by Hung on 12/5/2015.
 */
public class Home_fragment_giamgia extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment_giamgia,container,false);
        return v;
    }
}

