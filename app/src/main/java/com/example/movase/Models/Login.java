package com.example.movase.Models;

import android.app.Activity;

import com.example.movase.Repositories.LoginRepository;

public class Login {

    private String email;
    private String password;
    private Activity activity;
    private LoginRepository loginRepository = new LoginRepository();

    public Login() {
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void TryLogin(){
        loginRepository.tryLogin(activity, this.email, this.password);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Login{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
