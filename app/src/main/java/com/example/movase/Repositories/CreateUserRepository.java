package com.example.movase.Repositories;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.movase.Activities.LoginActivity;
import com.example.movase.Activities.MainActivity;
import com.example.movase.Models.Usuario;
import com.example.movase.Models.UsuarioRegister;
import com.example.movase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateUserRepository {
    FireStore fireStore = new FireStore();
    // Esse método é chamado para criar o usuário após ter criado a autenticação do mesmo
    private void createUserOnCollection(Activity activity, Usuario user, String id){
        ProgressBar pg = activity.findViewById(R.id.register_activity_pgRegister);

        //o addOnCompleteListener retorna quando a ação foi finalizada seja por erro ou por sucesso
        fireStore.db.collection("usuarios").document(id).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(activity, "Usuario Criado com sucesso, redirecionando para a tela de principal", Toast.LENGTH_LONG).show();
                    Intent mainActivityIntent = new Intent(activity, MainActivity.class);
                    activity.startActivity(mainActivityIntent);
                    activity.finish();
                    pg.setVisibility(View.GONE);
                }else{
                    Log.e("ERRO CRIAR USER", task.getException().toString());

                    Toast.makeText(activity, "Falha ao criar usuário, tente novamente mais tarde", Toast.LENGTH_LONG).show();
                    pg.setVisibility(View.GONE);
                }
            }
        });
    }
    //aqui cria uma autenticação por email e senha para o usuário
    public void createUser(Activity activity, UsuarioRegister usuarioRegister){
        UserFirebase userFb = new UserFirebase();
        ProgressBar pg = activity.findViewById(R.id.register_activity_pgRegister);
        //o addOnCompleteListener retorna quando a ação foi finalizada seja por erro ou por sucesso
        userFb.getAuth().createUserWithEmailAndPassword(usuarioRegister.getEmail(), usuarioRegister.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("UID CRIAR USER", task.getResult().getUser().getUid());
                    usuarioRegister.setSenha("");
                    createUserOnCollection(activity, usuarioRegister, task.getResult().getUser().getUid());
                }else{
                    Toast.makeText(activity, task.getException().toString(), Toast.LENGTH_LONG).show();
                    pg.setVisibility(View.GONE);
                }
            }
        });
    };
}
