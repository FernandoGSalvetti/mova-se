package com.example.movase.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movase.Models.Login;
import com.example.movase.R;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private Button btnRegistrar;
    private TextInputEditText etEmail;
    private TextInputEditText etSenha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onResume();
        super.onRestart();
        setContentView(R.layout.activity_login);
        Login login = new Login();
        login.setActivity(LoginActivity.this);
        inicializaComponentes();
        onChangeField(etEmail, login, "email");
        onChangeField(etSenha, login, "senha");
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.TryLogin();
            }
        });
    }

    private void onChangeField(TextInputEditText editText, Login login, String field) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable et) {
                switch (field){
                    case "email":
                        login.setEmail(et.toString());
                        break;
                    case "senha":
                        login.setPassword(et.toString());
                        break;
                    default:
                        break;
                }
            }
        });
    }
    private void inicializaComponentes() {
        btnLogin = findViewById(R.id.login_activity_btnLogin);
        btnRegistrar = findViewById(R.id.login_activity_btnRegistrar);
        etEmail = findViewById(R.id.login_activity_etEmail);
        etSenha = findViewById(R.id.login_activity_etSenha);
    }

}