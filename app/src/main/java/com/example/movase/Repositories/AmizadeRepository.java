package com.example.movase.Repositories;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.movase.Models.Amizade;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

public class AmizadeRepository {
    FireStore fireStore = new FireStore();
    public void verificaSeEhAmigoOuJaEnviouSolicitacao(String idUsuario, String idAmigo, Activity activity){
        fireStore.db.collection("usuarios").document(idUsuario).collection("amigos").document(idAmigo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    boolean isAmigo = (boolean) task.getResult().get("amigo");
                    if(isAmigo){
                        Toast.makeText(activity, "Você já é amigo deste usuário!", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(activity, "Você já enviou uma solicitação de amizade para este usuário, aguarde ele aceitar!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    enviarSolicitacaoDeAmizade(idUsuario, idAmigo, activity);
                }
            }
        });
    }
    public void enviarSolicitacaoDeAmizade(String idUsuario, String idAmigo, Activity activity){
        Amizade amizadeFrom = new Amizade(idAmigo, false, idUsuario);
        Amizade amizadeTo = new Amizade(idUsuario, false, idUsuario);
        fireStore.db.collection("usuarios").document(idUsuario).collection("amigos").document(idAmigo).set(amizadeFrom);
        fireStore.db.collection("usuarios").document(idAmigo).collection("amigos").document(idUsuario).set(amizadeTo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(activity.getApplicationContext(), "Solicitação de amizade enviada com sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void aceitarSolicitacaoDeAmizade(String idUsuario, String idAmigo, Activity activity){
        Amizade amizadeFrom = new Amizade(idAmigo, true);
        Amizade amizadeTo = new Amizade(idUsuario, true);
        fireStore.db.collection("usuarios").document(idUsuario).collection("amigos").document(idAmigo).set(amizadeFrom, SetOptions.merge());
        fireStore.db.collection("usuarios").document(idAmigo).collection("amigos").document(idUsuario).set(amizadeTo, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(activity.getApplicationContext(), "Amigo adicionado com sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void recusarSolicitacaoDeAmizade(String idUsuario, String idAmigo, Activity activity){
        fireStore.db.collection("usuarios").document(idUsuario).collection("amigos").document(idAmigo).delete();
        fireStore.db.collection("usuarios").document(idAmigo).collection("amigos").document(idUsuario).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(activity.getApplicationContext(), "Solicitação de amizade recusada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void solicitacaoDesfeita(String idUsuario, String idAmigo, Activity activity){
        fireStore.db.collection("usuarios").document(idUsuario).collection("amigos").document(idAmigo).delete();
        fireStore.db.collection("usuarios").document(idAmigo).collection("amigos").document(idUsuario).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(activity.getApplicationContext(), "Solicitação de amizade cancelada", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void removeAmizade(String idUsuario, String idAmigo, Activity activity){
        fireStore.db.collection("usuarios").document(idUsuario).collection("amigos").document(idAmigo).delete();
        fireStore.db.collection("usuarios").document(idAmigo).collection("amigos").document(idUsuario).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(activity.getApplicationContext(), "Amizade desfeita com sucesso", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
