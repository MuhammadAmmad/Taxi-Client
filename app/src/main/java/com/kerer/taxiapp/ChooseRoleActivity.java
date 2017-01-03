package com.kerer.taxiapp;

import android.support.v4.app.Fragment;

import com.kerer.taxiapp.fragment.ChooseRoleFragment;
import com.kerer.taxiapp.SingleFragmentActivity;

public class ChooseRoleActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return ChooseRoleFragment.newInstance();
    }
}
