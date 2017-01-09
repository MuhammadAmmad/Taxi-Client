package com.kerer.taxiapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kerer.taxiapp.DriverWaitingOrderActivity;
import com.kerer.taxiapp.R;
import com.kerer.taxiapp.SignInActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ivan on 03.01.17.
 */

public class ChooseRoleFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.fragment_choose_role_driver_button)
    Button mDriverButton;
    @BindView(R.id.fragment_choose_role_client_button)
    Button mClientButton;

    public static ChooseRoleFragment newInstance(){
        return new ChooseRoleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_role, container, false);
        ButterKnife.bind(this, v);

        mClientButton.setOnClickListener(this);
        mDriverButton.setOnClickListener(this);

        startActivity(DriverWaitingOrderActivity.getIntent(getActivity()));

        return v;
    }

    @Override
    public void onClick(View v) {
        int role = 0;
        switch (v.getId()){
            case R.id.fragment_choose_role_client_button:
                role = 0;
                break;
            case R.id.fragment_choose_role_driver_button:
                role = 1;
                break;
        }
        startActivity(SignInActivity.createIntent(getActivity(), role));
    }
}
