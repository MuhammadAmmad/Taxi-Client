package com.kerer.taxiapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kerer.taxiapp.R;

/**
 * Created by ivan on 03.01.17.
 */

public class DriverSignInFragment extends Fragment {

    public static DriverSignInFragment newInstance(){
        return new DriverSignInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver_sign_in, container, false);

        return v;
    }
}
