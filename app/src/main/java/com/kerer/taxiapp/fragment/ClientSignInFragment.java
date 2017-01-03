package com.kerer.taxiapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kerer.taxiapp.R;
import com.kerer.taxiapp.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ivan on 03.01.17.
 */

public class ClientSignInFragment extends Fragment {

    public static ClientSignInFragment newInstance(){
        return new ClientSignInFragment();
    }

    @BindView(R.id.fragment_sign_in_email_ed)
    EditText mEmailEditText;
    @BindView(R.id.fragment_sign_in_password_ed)
    EditText mPasswordEditText;
    @BindView(R.id.fragment_sign_in_login_button)
    Button mSignInButton;
    @BindView(R.id.fragment_sign_in_forgot_pass_tv)
    TextView mForgotPasswordTextView;
    @BindView(R.id.fragment_sign_in_create_acount_button)
    Button mCreateAcountButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.fragment_sign_in_create_acount_button)
    public void onCreateAcountClick(){
        startActivity(SignUpActivity.createIntent(getActivity(), 0));
    }
}
