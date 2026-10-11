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

public class ProfileFragment extends Fragment {
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
                        R.layout.fragment_profile,
                        container,
                        false
                );
        sessionManager =
                new SessionManager(
                        requireContext()
                );
        TextView textName =
                view.findViewById(
                        R.id.textName
                );
        TextView textEmail =
                view.findViewById(
                        R.id.textEmail
                );
        String firstName =
                sessionManager.getFirstName();
        String lastName =
                sessionManager.getLastName();
        String email =
                sessionManager.getEmail();
        textName.setText(
                firstName + " " + lastName
        );
        textEmail.setText(email);
        return view;
    }
}