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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kerer.taxiapp.Constants;
import com.kerer.taxiapp.R;
import com.kerer.taxiapp.model.Car;
import com.kerer.taxiapp.model.Driver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ivan on 03.01.17.
 */

public class DriverSignUpFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "DriverSignUpFragment";

    public static DriverSignUpFragment newInstance() {
        return new DriverSignUpFragment();
    }

    @BindView(R.id.fragment_sign_up_car_model_ed)
    EditText mDriverCarInfoEd;
    @BindView(R.id.fragment_sign_up_car_number_ed)
    EditText mDriverCarNumberEd;
    @BindView(R.id.fragment_sign_up_name_ed)
    EditText mDriverNameEd;
    @BindView(R.id.fragment_sign_up_surname_ed)
    EditText mDriverSurNameEd;
    @BindView(R.id.fragment_sign_up_email_ed)
    EditText mDriverEmailEd;
    @BindView(R.id.fragment_sign_up_password_ed)
    EditText mDriverPasswordEd;
    @BindView(R.id.fragment_sign_up_phone_ed)
    EditText mDriverPhoneEd;
    @BindView(R.id.fragment_sign_up_login_button)
    Button mSignUp;

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
                Log.d(TAG, firebaseAuth.getCurrentUser().getUid());

            }
        };

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver_sign_up, container, false);
        ButterKnife.bind(this, v);

        mEdits = new ArrayList<>();
        mEdits.add(mDriverCarInfoEd);
        mEdits.add(mDriverCarNumberEd);
        mEdits.add(mDriverNameEd);
        mEdits.add(mDriverSurNameEd);
        mEdits.add(mDriverEmailEd);

        mSignUp.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        if (checkFields()) {
            String email = mDriverEmailEd.getText().toString();
            String password = mDriverPasswordEd.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                createUser();
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

    private void createUser() {
        mDatabase.child(Constants.DB_DRIVERS).push().setValue(bindDriver())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, String.valueOf(task.isSuccessful()));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    private Driver bindDriver() {
        Driver driver = new Driver();
        driver.setmName(mDriverNameEd.getText().toString());
        driver.setmSurname(mDriverSurNameEd.getText().toString());
        driver.setmPhone(mDriverPhoneEd.getText().toString());
        driver.setuId(mAuth.getCurrentUser().getUid());
        driver.setBanned(false);

        Car car = new Car();
        car.setmCarInfo(mDriverCarInfoEd.getText().toString());
        car.setmCarNumber(mDriverCarNumberEd.getText().toString());

        driver.setmCar(car);

        return driver;
    }

    private boolean checkFields() {
        boolean isFileed = true;

        if (mEdits == null) {
            return false;
        }

        if (mDriverPasswordEd.getText().length() < 6) {
            mDriverPasswordEd.setError(getString(R.string.minimal_length_6_symbols));
            isFileed = false;
        }

        for (EditText item : mEdits) {
            if (item.getText().toString().length() == 0) {
                item.setError(getString(R.string.field_must_filled));
                isFileed = false;
            }
        }
        return isFileed;
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
