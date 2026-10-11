package com.rhix.authapp.api;

import com.rhix.authapp.model.ActivateRequest;
import com.rhix.authapp.model.ApiResponse;
import com.rhix.authapp.model.LoginRequest;
import com.rhix.authapp.model.RegisterRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface ApiService {
    @POST("register.php")
    Call<ApiResponse> register(
            @Body RegisterRequest request
    );
    @POST("activate.php")
    Call<ApiResponse> activate(
            @Body ActivateRequest request
    );
    @POST("login.php")
    Call<ApiResponse> login(
            @Body LoginRequest request
    );
    @POST("logout.php")
    Call<ApiResponse> logout();
}