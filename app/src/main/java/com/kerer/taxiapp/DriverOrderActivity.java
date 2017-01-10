package com.kerer.taxiapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.kerer.taxiapp.fragment.DriverOrderFragment;

/**
 * Created by ivan on 10.01.17.
 */

public class DriverOrderActivity extends SingleFragmentActivity {
    private static final String EXTRA_ORDER_KEY = "orderKey";

    public static Intent getIntent(Context context, String orderKey){
        Intent i = new Intent(context, DriverOrderActivity.class);
        i.putExtra(EXTRA_ORDER_KEY, orderKey);

        return new Intent(context, DriverOrderActivity.class);
    }

    @Override
    public Fragment createFragment() {
        return DriverOrderFragment.newInstance(getIntent().getStringExtra(EXTRA_ORDER_KEY));
    }
}
