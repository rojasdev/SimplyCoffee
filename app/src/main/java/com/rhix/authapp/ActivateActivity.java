package com.rhix.authapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rhix.authapp.api.ApiClient;
import com.rhix.authapp.api.ApiService;
import com.rhix.authapp.model.ActivateRequest;
import com.rhix.authapp.model.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivateActivity extends AppCompatActivity {
    private EditText editEmail;
    private EditText editActivationCode;
    private Button btnActivate;
    private ProgressBar progressActivate;
    private TextView linkLogin;
    private TextView linkRegister;
    private ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_activate
        );
        editEmail = findViewById(R.id.editEmail);
        editActivationCode = findViewById(R.id.editActivationCode);
        btnActivate = findViewById(R.id.btnActivate);
        progressActivate = findViewById(R.id.progressActivate);
        linkLogin = findViewById(R.id.linkLogin);
        linkRegister = findViewById(R.id.linkRegister);
        apiService =
                ApiClient
                        .getClient(this)
                        .create(ApiService.class);

        String registeredEmail =
                getIntent().getStringExtra(
                        "email"
                );

        if (registeredEmail != null
                && !registeredEmail.isEmpty()) {

            editEmail.setText(
                    registeredEmail
            );
        }

        editActivationCode.addTextChangedListener(
                new TextWatcher() {

                    private boolean editing = false;


                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }


                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {
                    }


                    @Override
                    public void afterTextChanged(
                            Editable editable
                    ) {

                        if (editing) {
                            return;
                        }


                        String value =
                                editable
                                        .toString()
                                        .toUpperCase()
                                        .replaceAll(
                                                "[^A-Z0-9]",
                                                ""
                                        );


                        if (value.length() > 10) {

                            value =
                                    value.substring(
                                            0,
                                            10
                                    );
                        }


                        editing = true;

                        editActivationCode
                                .setText(value);

                        editActivationCode
                                .setSelection(
                                        value.length()
                                );

                        editing = false;
                    }
                }
        );

        btnActivate.setOnClickListener(
                v -> activate()
        );

        linkLogin.setOnClickListener(
                v -> openLogin()
        );

        linkRegister.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    ActivateActivity.this,
                                    RegisterActivity.class
                            );

                    startActivity(intent);

                    finish();
                }
        );
    }

    private void activate() {

        String email =
                editEmail
                        .getText()
                        .toString()
                        .trim();

        String activationCode =
                editActivationCode
                        .getText()
                        .toString()
                        .trim()
                        .toUpperCase();

        if (TextUtils.isEmpty(email)) {

            editEmail.setError(
                    "Email is required"
            );

            editEmail.requestFocus();

            return;
        }


        if (!Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            editEmail.setError(
                    "Enter a valid email address"
            );

            editEmail.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(
                activationCode
        )) {

            editActivationCode.setError(
                    "Activation code is required"
            );

            editActivationCode.requestFocus();

            return;
        }


        if (activationCode.length() != 10) {

            editActivationCode.setError(
                    "Activation code must be 10 characters"
            );

            editActivationCode.requestFocus();

            return;
        }


        if (!activationCode.matches(
                "[A-Z0-9]{10}"
        )) {

            editActivationCode.setError(
                    "Invalid activation code format"
            );

            editActivationCode.requestFocus();

            return;
        }

        setLoading(true);

        ActivateRequest request =
                new ActivateRequest(
                        email,
                        activationCode
                );


        apiService
                .activate(request)
                .enqueue(
                        new Callback<ApiResponse>() {

                            @Override
                            public void onResponse(
                                    Call<ApiResponse> call,
                                    Response<ApiResponse> response
                            ) {

                                setLoading(false);


                                if (!response.isSuccessful()
                                        || response.body() == null) {

                                    Toast.makeText(
                                            ActivateActivity.this,
                                            "Unable to connect to the server.",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    return;
                                }


                                ApiResponse result =
                                        response.body();


                                if (result.isSuccess()) {

                                    Toast.makeText(
                                            ActivateActivity.this,
                                            result.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();

                                    openLogin();

                                } else {

                                    Toast.makeText(
                                            ActivateActivity.this,
                                            result.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                            }


                            @Override
                            public void onFailure(
                                    Call<ApiResponse> call,
                                    Throwable t
                            ) {

                                setLoading(false);

                                Toast.makeText(
                                        ActivateActivity.this,
                                        "Connection error: "
                                                + t.getMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
    }

    private void setLoading(
            boolean loading
    ) {

        if (loading) {

            btnActivate.setEnabled(false);

            btnActivate.setText(
                    "Activating..."
            );

            progressActivate.setVisibility(
                    View.VISIBLE
            );

        } else {

            btnActivate.setEnabled(true);

            btnActivate.setText(
                    "ACTIVATE ACCOUNT"
            );

            progressActivate.setVisibility(
                    View.GONE
            );
        }
    }

    private void openLogin() {

        Intent intent =
                new Intent(
                        ActivateActivity.this,
                        LoginActivity.class
                );


        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
        );


        startActivity(intent);

        finish();
    }
}