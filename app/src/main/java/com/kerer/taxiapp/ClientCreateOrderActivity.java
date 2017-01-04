package com.kerer.taxiapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.kerer.taxiapp.fragment.ClientCreateOrderFragment;

/**
 * Created by ivan on 04.01.17.
 */

public class ClientCreateOrderActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return ClientCreateOrderFragment.newInstance();
    }

    public static final Intent createIntent(Context activity){
        return new Intent(activity, ClientCreateOrderActivity.class);
    }

}
