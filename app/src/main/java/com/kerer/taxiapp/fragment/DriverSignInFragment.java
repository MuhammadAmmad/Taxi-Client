package com.kerer.taxiapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kerer.taxiapp.R;
import com.kerer.taxiapp.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by ivan on 03.01.17.
 */

public class DriverSignInFragment extends Fragment {
    private static final String TAG = "DriverSignInFragment";

    public static DriverSignInFragment newInstance() {
        return new DriverSignInFragment();
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
    @BindView(R.id.fragment_sign_in_progress)
    SmoothProgressBar mProgress;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser != null){
            mAuth.signOut();
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if (mUser != null){
                    Log.d(TAG, "Auth is succesful");
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @OnClick(R.id.fragment_sign_in_create_acount_button)
    public void onCreateAcountClick() {
        startActivity(SignUpActivity.createIntent(getActivity(), 1));
    }

    @OnClick(R.id.fragment_sign_in_login_button)
    public void onTryAuth() {
        mProgress.setVisibility(View.VISIBLE);

        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if (email.length() < 0) {
            mEmailEditText.setError(getString(R.string.field_must_filled));
            return;
        }
        if (password.length() < 6) {
            mPasswordEditText.setError(getString(R.string.field_must_filled));
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "on complate");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                        mProgress.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
