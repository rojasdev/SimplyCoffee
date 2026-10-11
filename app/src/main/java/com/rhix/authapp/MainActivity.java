package com.rhix.authapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rhix.authapp.fragment.HomeFragment;
import com.rhix.authapp.fragment.ProfileFragment;
import com.rhix.authapp.fragment.SettingsFragment;
import com.rhix.authapp.session.SessionManager;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sessionManager =
                new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {

            openLogin();

            return;
        }



        bottomNavigation =
                findViewById(
                        R.id.bottomNavigation
                );


        bottomNavigation.setOnItemSelectedListener(
                item -> {

                    Fragment fragment = null;

                    int itemId = item.getItemId();


                    if (itemId == R.id.nav_home) {

                        fragment =
                                new HomeFragment();

                    } else if (itemId == R.id.nav_profile) {

                        fragment =
                                new ProfileFragment();

                    } else if (itemId == R.id.nav_settings) {

                        fragment =
                                new SettingsFragment();
                    }


                    if (fragment != null) {

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(
                                        R.id.fragmentContainer,
                                        fragment
                                )
                                .commit();

                        return true;
                    }

                    return false;
                }
        );


        if (savedInstanceState == null) {

            bottomNavigation
                    .setSelectedItemId(
                            R.id.nav_home
                    );
        }
    }


    private void openLogin() {

        android.content.Intent intent =
                new android.content.Intent(
                        this,
                        LoginActivity.class
                );

        intent.addFlags(
                android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                        |
                        android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);

        finish();
    }
}