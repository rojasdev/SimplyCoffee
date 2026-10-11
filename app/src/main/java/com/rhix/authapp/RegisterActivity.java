package com.rhix.authapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.rhix.authapp.model.ApiResponse;
import com.rhix.authapp.model.RegisterRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editConfirmPassword;

    private Button btnRegister;

    private ProgressBar progressRegister;

    private TextView linkLogin;
    private TextView linkActivate;

    private ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_register
        );


        editFirstName =
                findViewById(R.id.editFirstName);

        editLastName =
                findViewById(R.id.editLastName);

        editEmail =
                findViewById(R.id.editEmail);

        editPassword =
                findViewById(R.id.editPassword);

        editConfirmPassword =
                findViewById(R.id.editConfirmPassword);

        btnRegister =
                findViewById(R.id.btnRegister);

        progressRegister =
                findViewById(R.id.progressRegister);

        linkLogin =
                findViewById(R.id.linkLogin);

        linkActivate =
                findViewById(R.id.linkActivate);


        apiService =
                ApiClient
                        .getClient(this)
                        .create(ApiService.class);


        btnRegister.setOnClickListener(
                v -> register()
        );


        linkLogin.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    RegisterActivity.this,
                                    LoginActivity.class
                            );

                    startActivity(intent);

                    finish();
                }
        );


        linkActivate.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    RegisterActivity.this,
                                    ActivateActivity.class
                            );

                    startActivity(intent);
                }
        );
    }

    private void register() {

        String firstName =
                editFirstName
                        .getText()
                        .toString()
                        .trim();

        String lastName =
                editLastName
                        .getText()
                        .toString()
                        .trim();

        String email =
                editEmail
                        .getText()
                        .toString()
                        .trim();

        String password =
                editPassword
                        .getText()
                        .toString();

        String confirmPassword =
                editConfirmPassword
                        .getText()
                        .toString();


        if (TextUtils.isEmpty(firstName)) {

            editFirstName.setError(
                    "First name is required"
            );

            editFirstName.requestFocus();

            return;
        }


        if (TextUtils.isEmpty(lastName)) {

            editLastName.setError(
                    "Last name is required"
            );

            editLastName.requestFocus();

            return;
        }


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


        if (TextUtils.isEmpty(password)) {

            editPassword.setError(
                    "Password is required"
            );

            editPassword.requestFocus();

            return;
        }


        if (password.length() < 8) {

            editPassword.setError(
                    "Password must be at least 8 characters"
            );

            editPassword.requestFocus();

            return;
        }


        if (!password.equals(confirmPassword)) {

            editConfirmPassword.setError(
                    "Passwords do not match"
            );

            editConfirmPassword.requestFocus();

            return;
        }


        setLoading(true);


        RegisterRequest request =
                new RegisterRequest(
                        firstName,
                        lastName,
                        email,
                        password
                );


        apiService
                .register(request)
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
                                            RegisterActivity.this,
                                            "Unable to connect to the server.",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    return;
                                }


                                ApiResponse result =
                                        response.body();


                                if (result.isSuccess()) {

                                    Toast.makeText(
                                            RegisterActivity.this,
                                            result.getMessage(),
                                            Toast.LENGTH_LONG
                                    ).show();

                                    Intent intent =
                                            new Intent(
                                                    RegisterActivity.this,
                                                    ActivateActivity.class
                                            );


                                    intent.putExtra(
                                            "email",
                                            email
                                    );


                                    startActivity(intent);

                                    finish();

                                } else {

                                    Toast.makeText(
                                            RegisterActivity.this,
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
                                        RegisterActivity.this,
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

            btnRegister.setEnabled(false);

            btnRegister.setText(
                    "Creating Account..."
            );

            progressRegister.setVisibility(
                    View.VISIBLE
            );

        } else {

            btnRegister.setEnabled(true);

            btnRegister.setText(
                    "CREATE ACCOUNT"
            );

            progressRegister.setVisibility(
                    View.GONE
            );
        }
    }
}