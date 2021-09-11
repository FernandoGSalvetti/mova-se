package com.example.movase.Repositories;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.example.movase.Activities.MainActivity;
import com.example.movase.Models.ClasseMultiuso;
import com.example.movase.Models.Evento;
import com.example.movase.Models.EventoViewModel;
import com.example.movase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventoRepository {

    FireStore fireStore = new FireStore();

    //Adiciona evento ao usuariio
    private void adicionaEventoAoUsuario(boolean isDonoEvento,String idUsuario, String idEvento){
        if(isDonoEvento){
            //Firebase pede uma classe para ser adicionada por isso esta esta sendo usada
            ClasseMultiuso classeMultiuso = new ClasseMultiuso();
            classeMultiuso.setId(idEvento);
            fireStore.db.collection("usuarios").document(idUsuario).collection("meusEventos").document(idEvento).set(classeMultiuso).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
            }else{
            ClasseMultiuso classeMultiuso = new ClasseMultiuso();
            classeMultiuso.setId(idEvento);
            fireStore.db.collection("usuarios").document(idUsuario).collection("eventos").document(idEvento).set(classeMultiuso).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }
    }

    //Adiciona o usuario ao evento
    private void adicionaParticipanteAoEvento(String idEvento,String idUsuario){
        ClasseMultiuso classeMultiuso = new ClasseMultiuso();
        classeMultiuso.setId(idUsuario);
        fireStore.db.collection("eventos").document(idEvento).collection("participantes").document(idUsuario).set(classeMultiuso).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
    public void verificaSeJaEstaParticipandoDoEvento(EventoViewModel evento, String idUsuario, Activity activity){
        fireStore.db.collection("usuarios").document(idUsuario).collection("eventos").document(evento.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    Toast.makeText(activity, "Você já esta participando deste evento!", Toast.LENGTH_LONG).show();
                }else{
                    verificaSePodeParticiparDoEvento(evento, idUsuario, activity);
                }
            }
        });
    }    //Ve se é possível participar do evento
    public void verificaSePodeParticiparDoEvento(EventoViewModel evento, String idUsuario, Activity activity){
        if(evento.getTipoDeEvento().equalsIgnoreCase("aberto")){
            participarDeEvento(evento.getId(), idUsuario, activity);
        }else{
            //Aqui como o evento é privado tem que ver se o usuário que está querendo particpar do evento tem vínculo de amizade com o dono do evento.
            fireStore.db.collection("usuarios").document(idUsuario).collection("amigos").document(evento.getIdCriadorDoEvento()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if(task.getResult().exists()){
                        boolean isAmigo = (boolean) task.getResult().get("amigo");
                        if(isAmigo){
                            participarDeEvento(evento.getId(), idUsuario, activity);
                        }else{
                            Toast.makeText(activity.getApplicationContext(), "O dono do evento ainda não aceitou seu pedido de amizade, aguarde o aceite dele para poder participar deste evento", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(activity.getApplicationContext(), "Você não é amigo do dono do evento ainda, adicione ele, após ele aceitar o pedido de amizade você vai poder participar do evento", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    //Para algum usuário poder participar de um evento
    private void participarDeEvento(String idEvento, String idUsuario, Activity activity){
        adicionaEventoAoUsuario(false, idUsuario, idEvento);
        adicionaParticipanteAoEvento(idEvento, idUsuario);
        Toast.makeText(activity.getApplicationContext(), "Você está participando do evento!", Toast.LENGTH_SHORT).show();
    }
    //Remove o evento do dono
    private void removeEventoDono(String idUsuario, String idEvento){
        fireStore.db.collection("usuarios").document(idUsuario).collection("meusEventos").document(idEvento).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {

            }
        });
    }
    //Remopve o usuario da lista de participantes do evento
    private void removeUsuario(String idEvento, String idUsuario){
        fireStore.db.collection("eventos").document(idEvento).collection("participantes").document(idUsuario).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {

            }
        });
    }
    //Remove o Evento do usuário e remove o Usuário da Lista de participantes do evento
    public void removeParticipanteFromEvento(boolean isDono, String idUsuario, String idEvento){
        Log.d("Evento id", idEvento);
        if(isDono){
            removeEventoDono(idUsuario, idEvento);
        }else{
            removeEventoParticipante(idUsuario, idEvento);
        }
        removeUsuario(idEvento, idUsuario);
    }
    //Remove o evento do participante
    public void removeEventoParticipante(String idUsuario, String idEvento){
        Log.d("Evento id", idEvento);
        fireStore.db.collection("usuarios").document(idUsuario).collection("eventos").document(idEvento).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {

            }
        });
    }



    public void excluirEvento(EventoViewModel evento){
        //Pega os participantes do evento
        CollectionReference eventosParticipantes = fireStore.db.collection("eventos").document(evento.getId()).collection("participantes");
        eventosParticipantes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot ds : task.getResult()) {
                        String usuarioId = ds.getString("id");
                        if(usuarioId.equalsIgnoreCase(evento.getIdCriadorDoEvento())){
                            removeParticipanteFromEvento(true, usuarioId, evento.getId());
                        }else{
                            removeParticipanteFromEvento(false, usuarioId, evento.getId());
                        }
                    }
                }
            }
        });
        fireStore.db.collection("eventos").document(evento.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {

            }
        });
    }

    public void criarEvento(Activity activity, EventoViewModel evento){
        UserFirebase userFb = new UserFirebase();
        evento.setIdCriadorDoEvento(userFb.getCurrentUserUser().getUid());
        fireStore.db.collection("eventos").add(evento).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                if(task.isSuccessful()) {
                    String idEvento = task.getResult().getId();
                    adicionaEventoAoUsuario(true, evento.getIdCriadorDoEvento(), idEvento);
                    adicionaParticipanteAoEvento(idEvento, evento.getIdCriadorDoEvento());
                    Intent mainActivityIntent = new Intent(activity, MainActivity.class);
                    activity.startActivity(mainActivityIntent);
                    activity.finish();
                }else{
                    Toast.makeText(activity, "Ocorreu um erro ao criar o evento!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void atualizaEvento(Activity activity, EventoViewModel evento){
        UserFirebase userFb = new UserFirebase();
        evento.setIdCriadorDoEvento(userFb.getCurrentUserUser().getUid());
        Log.d("CRUD", evento.getData());
        fireStore.db.collection("eventos").document(evento.getId()).set(evento, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                Intent mainActivityIntent = new Intent(activity, MainActivity.class);
                Toast.makeText(activity, "Evento atualizado com sucesso!", Toast.LENGTH_LONG).show();
                activity.startActivity(mainActivityIntent);
                activity.finish();
            }else{
                Toast.makeText(activity, "Ocorreu um erro ao criar o evento!", Toast.LENGTH_LONG).show();
            }
        }});
    }
}
