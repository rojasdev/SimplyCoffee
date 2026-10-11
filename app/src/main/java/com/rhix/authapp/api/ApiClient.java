package com.rhix.authapp.api;
import android.content.Context;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    private static final String BASE_URL =
            "http://pzf.22b.mytemp.website/authapi/";
    private static Retrofit retrofit;
    public static Retrofit getClient(
            Context context
    ) {
        if (retrofit == null) {
            HttpLoggingInterceptor logging =
                    new HttpLoggingInterceptor();
            logging.setLevel(
                    HttpLoggingInterceptor.Level.BODY
            );
            AuthInterceptor authInterceptor =
                    new AuthInterceptor(
                            context.getApplicationContext()
                    );
            OkHttpClient client =
                    new OkHttpClient.Builder()
                            .addInterceptor(
                                    authInterceptor
                            )
                            .addInterceptor(
                                    logging
                            )
                            .build();
            retrofit =
                    new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(client)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            )
                            .build();
        }
        return retrofit;
    }
}