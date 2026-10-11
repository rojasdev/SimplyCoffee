package com.rhix.authapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.rhix.authapp.LoginActivity;
import com.rhix.authapp.R;
import com.rhix.authapp.api.ApiClient;
import com.rhix.authapp.api.ApiService;
import com.rhix.authapp.model.ApiResponse;
import com.rhix.authapp.session.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment {
    private SessionManager sessionManager;
    private ApiService apiService;
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view =
                inflater.inflate(
                        R.layout.fragment_settings,
                        container,
                        false
                );
        sessionManager =
                new SessionManager(
                        requireContext()
                );
        apiService =
                ApiClient
                        .getClient(requireContext())
                        .create(ApiService.class);
        Button btnLogout =
                view.findViewById(
                        R.id.btnLogout
                );
        btnLogout.setOnClickListener(
                v -> showLogoutConfirmation()
        );
        return view;
    }
    private void showLogoutConfirmation() {

        new AlertDialog.Builder(
                requireContext()
        )
                .setTitle("Logout")
                .setMessage(
                        "Are you sure you want to logout?"
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .setPositiveButton(
                        "Logout",
                        (dialog, which) ->
                                logout()
                )
                .show();
    }
    private void logout() {
        apiService
                .logout()
                .enqueue(
                        new Callback<ApiResponse>() {

                            @Override
                            public void onResponse(
                                    Call<ApiResponse> call,
                                    Response<ApiResponse> response
                            ) {
                                sessionManager
                                        .clearSession();
                                Toast.makeText(
                                        requireContext(),
                                        "Logged out successfully.",
                                        Toast.LENGTH_SHORT
                                ).show();
                                openLogin();
                            }
                            @Override
                            public void onFailure(
                                    Call<ApiResponse> call,
                                    Throwable t
                            ) {
                                sessionManager
                                        .clearSession();


                                Toast.makeText(
                                        requireContext(),
                                        "Session cleared.",
                                        Toast.LENGTH_SHORT
                                ).show();
                                openLogin();
                            }
                        }
                );
    }


    private void openLogin() {
        Intent intent =
                new Intent(
                        requireContext(),
                        LoginActivity.class
                );
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
        );
        startActivity(intent);
    }
}