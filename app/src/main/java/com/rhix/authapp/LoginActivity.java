package com.rhix.authapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rhix.authapp.api.ApiClient;
import com.rhix.authapp.api.ApiService;
import com.rhix.authapp.model.ApiResponse;
import com.rhix.authapp.model.LoginRequest;
import com.rhix.authapp.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;

    private Button btnLogin;

    private ProgressBar progressLogin;

    private TextView linkRegister;
    private TextView linkActivate;

    private ApiService apiService;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_login
        );

        sessionManager =
                new SessionManager(this);

        if (sessionManager.isLoggedIn()) {

            openMainActivity();

            return;
        }

        editEmail =
                findViewById(
                        R.id.editEmail
                );

        editPassword =
                findViewById(
                        R.id.editPassword
                );

        btnLogin =
                findViewById(
                        R.id.btnLogin
                );

        progressLogin =
                findViewById(
                        R.id.progressLogin
                );

        linkRegister =
                findViewById(
                        R.id.linkRegister
                );

        linkActivate =
                findViewById(
                        R.id.linkActivate
                );
        apiService =
                ApiClient
                        .getClient(this)
                        .create(ApiService.class);

        btnLogin.setOnClickListener(
                v -> login()
        );

        linkRegister.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    LoginActivity.this,
                                    RegisterActivity.class
                            );

                    startActivity(intent);
                }
        );

        linkActivate.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    LoginActivity.this,
                                    ActivateActivity.class
                            );

                    startActivity(intent);
                }
        );
    }

    private void login() {

        String email =
                editEmail
                        .getText()
                        .toString()
                        .trim();

        String password =
                editPassword
                        .getText()
                        .toString();

// validation part client side
        if (TextUtils.isEmpty(email)) {

            editEmail.setError(
                    "Email is required"
            );

            editEmail.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(password)) {

            editPassword.setError(
                    "Password is required"
            );

            editPassword.requestFocus();

            return;
        }

        setLoading(true);


        LoginRequest request =
                new LoginRequest(
                        email,
                        password
                );


        apiService
                .login(request)
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
                                            LoginActivity.this,
                                            "Wrong username or password.",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    return;
                                }


                                ApiResponse result =
                                        response.body();


                                if (result.isSuccess()) {


                                    sessionManager.saveSession(
                                            result.getToken(),
                                            result.getUser()
                                    );


                                    Toast.makeText(
                                            LoginActivity.this,
                                            "Login successful.",
                                            Toast.LENGTH_SHORT
                                    ).show();


                                    openMainActivity();

                                } else {

                                    Toast.makeText(
                                            LoginActivity.this,
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
                                        LoginActivity.this,
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

            btnLogin.setEnabled(false);

            btnLogin.setText(
                    "Signing In..."
            );

            progressLogin.setVisibility(
                    View.VISIBLE
            );

        } else {

            btnLogin.setEnabled(true);

            btnLogin.setText(
                    "SIGN IN"
            );

            progressLogin.setVisibility(
                    View.GONE
            );
        }
    }


    private void openMainActivity() {

        Intent intent =
                new Intent(
                        LoginActivity.this,
                        MainActivity.class
                );


        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );


        startActivity(intent);

        finish();
    }
}