package com.example.movase.Repositories;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.movase.Activities.LoginActivity;
import com.example.movase.Activities.MainActivity;
import com.example.movase.Models.Login;
import com.example.movase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginRepository {
    //Este método faz o Login do usuário usando o authenthication
    public void tryLogin(Activity activity, Login login){
        try{
            UserFirebase userFb = new UserFirebase();
            userFb.getAuth().signInWithEmailAndPassword(login.getEmail(), login.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent toLogin = new Intent(activity, MainActivity.class);
                        activity.startActivity(toLogin);
                        activity.finish();
                    }else{
                        Log.d("ERRO LOGIN",task.getException().toString());
                       if(task.getException().toString().equalsIgnoreCase("com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted.")) {
                           Toast.makeText(activity.getApplicationContext(), "E-mail não cadastrado no sistema", Toast.LENGTH_LONG).show();
                       }else {
                           if(task.getException().toString().equalsIgnoreCase("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password.")) {
                               Toast.makeText(activity.getApplicationContext(), "Senha incorreta", Toast.LENGTH_LONG).show();
                           }else {
                               Toast.makeText(activity.getApplicationContext(), "E-mail não cadastrado", Toast.LENGTH_LONG).show();
                           }
                       }
                    }
                }
            });

        }catch (Exception e){
            Toast.makeText(activity, "Falha ao fazer login", Toast.LENGTH_LONG).show();
            Log.e("ERRO LOGIN", e.toString());
        }
    };
    public void tryLogout(Activity activity){
        try{
            //este método é responsável por fazer o logout do usuário
            UserFirebase userFb = new UserFirebase();
            userFb.getAuth().signOut();
            Intent toLogin = new Intent(activity, LoginActivity.class);
            activity.startActivity(toLogin);
            activity.finish();
        }catch (Exception e){
            Toast.makeText(activity, "Falha ao fazer logout", Toast.LENGTH_LONG).show();
            Log.e("Erro LOGOFF", e.toString());
        }
    };
}
