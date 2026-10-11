package com.rhix.authapp.api;

import android.content.Context;
import com.rhix.authapp.session.SessionManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
public class AuthInterceptor implements Interceptor {
    private final SessionManager sessionManager;
    public AuthInterceptor(Context context) {
        sessionManager =
                new SessionManager(context);
    }
    @Override
    public Response intercept(
            Chain chain
    ) throws IOException {
        Request originalRequest =
                chain.request();
        String token =
                sessionManager.getToken();
        if (token == null || token.isEmpty()) {

            return chain.proceed(
                    originalRequest
            );
        }
        Request authenticatedRequest =
                originalRequest
                        .newBuilder()
                        .header(
                                "Authorization",
                                "Bearer " + token
                        )
                        .build();
        return chain.proceed(
                authenticatedRequest
        );
    }
}