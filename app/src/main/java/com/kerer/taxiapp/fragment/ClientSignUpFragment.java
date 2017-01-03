package com.kerer.taxiapp.fragment;
/*
    @BindView(R.id.fragment_sign_up_car_model_ed)
    EditText mClientCarInfoEd;
    @BindView(R.id.fragment_sign_up_name_ed)
    EditText mClientCarNumberEd;
    @BindView(R.id.fragment_sign_up_name_ed)
    EditText mClientNameEd;
    @BindView(R.id.fragment_sign_up_surname_ed)
    EditText mClientSurNameEd;
    @BindView(R.id.fragment_sign_up_email_ed)
    EditText mClientEmailEd;
    @BindView(R.id.fragment_sign_up_password_ed)
    EditText mClientPasswordEd;
    @BindView(R.id.fragment_sign_up_phone_ed)
    Button mClientPhoneEd;*/
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kerer.taxiapp.R;
import com.kerer.taxiapp.model.Client;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */

public class ClientSignUpFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ClientSignUpFragment";

    public static ClientSignUpFragment newInstance() {
        return new ClientSignUpFragment();
    }

    @BindView(R.id.fragment_sign_up_name_ed)
    EditText mClientNameEd;
    @BindView(R.id.fragment_sign_up_surname_ed)
    EditText mClientSurNameEd;
    @BindView(R.id.fragment_sign_up_email_ed)
    EditText mClientEmailEd;
    @BindView(R.id.fragment_sign_up_password_ed)
    EditText mClientPasswordEd;
    @BindView(R.id.fragment_sign_up_phone_ed)
    EditText mClientPhoneEd;
    @BindView(R.id.fragment_sign_up_login_button)
    Button mClientSignUp;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    //List of all edit text`s in layout
    private List<EditText> mEdits;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    createUser();
                }
            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("driver");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_client_sign_up, container, false);
        ButterKnife.bind(this, v);

        mEdits = new ArrayList<>();
        mEdits.add(mClientNameEd);
        mEdits.add(mClientSurNameEd);
        mEdits.add(mClientEmailEd);
        mEdits.add(mClientPasswordEd);

        mClientSignUp.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (checkFields()) {
            String email = mClientEmailEd.getText().toString();
            String password = mClientPasswordEd.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Creating user succesful");
                            }
                        }
                    })
                    .addOnFailureListener(getActivity(), new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }

    /**
     * Check is all fields is not empty
     */
    private boolean checkFields() {
        boolean isFileed = true;

        if (mEdits == null) {
            return false;
        }

        if (mClientPasswordEd.getText().length() < 6) {
            mClientPasswordEd.setError(getString(R.string.minimal_length_6_symbols));
        }

        for (EditText item : mEdits) {
            if (item.getText().toString().length() == 0) {
                item.setError(getString(R.string.field_must_filled));
                isFileed = false;
            }
        }
        return isFileed;
    }

    private void createUser(){
        Client client = new Client();
        client.setmName(mClientNameEd.getText().toString());
        client.setmSurname(mClientSurNameEd.getText().toString());
        client.setmPhone(mClientPhoneEd.getText().toString());
        client.setuId(mAuth.getCurrentUser().getUid());
        client.setBanned(false);

        mDatabase.push().setValue(client);
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
