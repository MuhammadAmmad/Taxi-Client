package com.kerer.taxiapp;

import android.support.v4.app.Fragment;

public class ChooseRoleActivity extends SingleFragmentActivity {


    @Override
    public Fragment createFragment() {
        return ChooseRoleFragment.newInstance();
    }
}
