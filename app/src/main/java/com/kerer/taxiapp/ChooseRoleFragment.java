package com.kerer.taxiapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ivan on 03.01.17.
 */

public class ChooseRoleFragment extends Fragment {

    public static ChooseRoleFragment newInstance(){
        return new ChooseRoleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_role, container, false);

        return v;
    }
}
