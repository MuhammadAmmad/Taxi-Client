package com.kerer.taxiapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kerer.taxiapp.R;

/**
 * Created by ivan on 10.01.17.
 */

public class SplashFragment extends Fragment {
    public static final String TAG = "SplashFragment";

    public static SplashFragment newInstance(){
        return new SplashFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splash, container, false);

        return v;
    }

}
