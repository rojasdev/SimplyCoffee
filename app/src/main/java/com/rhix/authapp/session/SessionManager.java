package com.rhix.authapp.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.rhix.authapp.model.User;

public class SessionManager {

    private static final String PREF_NAME =
            "auth_session";

    private static final String KEY_TOKEN =
            "auth_token";

    private static final String KEY_USER_ID =
            "user_id";

    private static final String KEY_FIRST_NAME =
            "first_name";

    private static final String KEY_LAST_NAME =
            "last_name";

    private static final String KEY_EMAIL =
            "email";


    private final SharedPreferences preferences;


    public SessionManager(Context context) {

        preferences =
                context.getSharedPreferences(
                        PREF_NAME,
                        Context.MODE_PRIVATE
                );
    }


    public void saveSession(
            String token,
            User user
    ) {

        SharedPreferences.Editor editor =
                preferences.edit();

        editor.putString(
                KEY_TOKEN,
                token
        );

        if (user != null) {

            editor.putInt(
                    KEY_USER_ID,
                    user.getUserId()
            );

            editor.putString(
                    KEY_FIRST_NAME,
                    user.getFirstName()
            );

            editor.putString(
                    KEY_LAST_NAME,
                    user.getLastName()
            );

            editor.putString(
                    KEY_EMAIL,
                    user.getEmail()
            );
        }

        editor.apply();
    }


    public String getToken() {

        return preferences.getString(
                KEY_TOKEN,
                null
        );
    }


    public boolean isLoggedIn() {

        String token = getToken();

        return token != null &&
                !token.isEmpty();
    }


    public String getFirstName() {

        return preferences.getString(
                KEY_FIRST_NAME,
                ""
        );
    }


    public String getLastName() {

        return preferences.getString(
                KEY_LAST_NAME,
                ""
        );
    }


    public String getEmail() {

        return preferences.getString(
                KEY_EMAIL,
                ""
        );
    }


    public void clearSession() {

        preferences
                .edit()
                .clear()
                .apply();
    }
}