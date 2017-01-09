package com.kerer.taxiapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.kerer.taxiapp.fragment.DriverWaitingOrderFragment;

/**
 * Created by ivan on 09.01.17.
 */

public class DriverWaitingOrderActivity extends SingleFragmentActivity {

    public static Intent getIntent(Context context) {
        return new Intent(context, DriverWaitingOrderActivity.class);
    }

    @Override
    public Fragment createFragment() {
        return DriverWaitingOrderFragment.newInstance();
    }
}
