package com.kerer.taxiapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.kerer.taxiapp.fragment.ClientSignInFragment;
import com.kerer.taxiapp.fragment.DriverSignInFragment;

/**
 * Created by ivan on 03.01.17.
 */

public class SignInActivity extends SingleFragmentActivity {

    private static final String EXTRA_ROLE = "role";

    /**
     * @param activity activity, from
     * @param role 0 - client, 1 - driver
     */
    public static Intent createIntent(Context activity, int role){
        Intent i = new Intent(activity, SignInActivity.class);
        i.putExtra(EXTRA_ROLE, role);

        return i;
    }

    @Override
    public Fragment createFragment() {
        Fragment fragment = null;

        switch (getIntent().getIntExtra(EXTRA_ROLE, 2)){
            case 0:
                fragment = ClientSignInFragment.newInstance();
                break;
            case 1:
                fragment = DriverSignInFragment.newInstance();
                break;
            case 2:
                Toast.makeText(this, getString(R.string.something_goes_wrong), Toast.LENGTH_SHORT)
                        .show();
                break;
        }

        return fragment;
    }
}
