package com.rhix.authapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rhix.authapp.R;
import com.rhix.authapp.session.SessionManager;

public class HomeFragment extends Fragment {
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view =
                inflater.inflate(
                        R.layout.fragment_home,
                        container,
                        false
                );
        sessionManager =
                new SessionManager(
                        requireContext()
                );
        TextView textWelcome =
                view.findViewById(
                        R.id.textWelcome
                );
        String firstName =
                sessionManager.getFirstName();
        if (firstName == null ||
                firstName.isEmpty()) {
            firstName = "User";
        }
        textWelcome.setText(
                "Welcome, " + firstName + "!"
        );
        return view;
    }
}