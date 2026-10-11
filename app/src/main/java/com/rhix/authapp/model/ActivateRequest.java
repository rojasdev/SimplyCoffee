package com.rhix.authapp.model;

public class ActivateRequest {

    private String email;
    private String activation_code;


    public ActivateRequest(
            String email,
            String activation_code
    ) {

        this.email = email;
        this.activation_code = activation_code;
    }


    public String getEmail() {
        return email;
    }


    public String getActivation_code() {
        return activation_code;
    }
}