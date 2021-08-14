package com.example.movase.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movase.Adapters.ListAmizadeAdapter;
import com.example.movase.Adapters.ListPessoasAdapter;
import com.example.movase.Models.AmizadeViewModel;
import com.example.movase.Models.Usuario;
import com.example.movase.Models.UsuarioViewModel;
import com.example.movase.R;
import com.example.movase.Repositories.EventoRepository;
import com.example.movase.Repositories.UserFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class ListarParticipantesAcitivity extends AppCompatActivity {
    private ListView participantesListView;
    private TextView nomeEvento;
    private List<UsuarioViewModel> listParticipantes = new ArrayList<>(); // Obtenha sua lista de objetos aqui
    UserFirebase userFirebase = new UserFirebase();
    ListPessoasAdapter adapterParticipantes;
    private EventoRepository eventoRepository = new EventoRepository();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String id = "";
    private String nomeCompleto = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_participantes_acitivity);
        Intent intent = getIntent();
        id = intent.getStringExtra("idEvento");
        nomeCompleto = intent.getStringExtra("nomeEvento");
        inicializaComponentes();
        observaListaDeAmigos();
        nomeEvento.setText("Participantes do evento " + nomeCompleto);
        participantesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long idItem) {
                Object o = participantesListView.getItemAtPosition(position);
                UsuarioViewModel usuario = (UsuarioViewModel) o; //As you are using Default String Adapter
                    new MaterialAlertDialogBuilder(ListarParticipantesAcitivity.this)
                            .setTitle("")
                            .setMessage(usuario.getNomeCompleto())
                            .setNegativeButton("Remover usuário do evento", (dialog, which) -> {
                                if(userFirebase.getCurrentUserUser().getUid().equalsIgnoreCase(usuario.getId())){
                                    Toast.makeText(getApplicationContext(),"Você não remover você mesmo", Toast.LENGTH_SHORT).show();
                                }else{
                                    eventoRepository.removeParticipanteFromEvento(false, usuario.getId(), id);
                                    Toast.makeText(getApplicationContext(),"Participante removido com sucesso", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
            }
        });
    }

    private void inicializaComponentes() {
        nomeEvento = findViewById(R.id.actitivity_listar_participantes_nome_evento);
        participantesListView = findViewById(R.id.activity_listar_participantes_activity);
    }

    private void inflateListViewMeusAmigos() {
        if(listParticipantes.size() > 0){
            adapterParticipantes = new ListPessoasAdapter(this, listParticipantes);
            participantesListView.setAdapter(adapterParticipantes);
        }else{
            participantesListView.setVisibility(View.GONE);
        }
    }
    private void observaListaDeAmigos() {
        db.collection("eventos")
                .document(id)
                .collection("participantes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            for(DocumentSnapshot ds : snapshots.getDocuments()) {
                                inflateListViewMeusAmigos();
                            }
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            UsuarioViewModel usuarioViewModel = new UsuarioViewModel();
                            usuarioViewModel.setId((String) dc.getDocument().get("id"));
                            if(dc.getType() == DocumentChange.Type.REMOVED){
                                for(int i = 0; i < listParticipantes.size(); i++){
                                    UsuarioViewModel a = listParticipantes.get(i);
                                    if(a.getId().equalsIgnoreCase(usuarioViewModel.getId())) {
                                        listParticipantes.remove(a);
                                        inflateListViewMeusAmigos();
                                    }
                                }
                            }else{
                                getDadosUsuario(usuarioViewModel);
                            }
                        }

                    }
                });
    }

    private void getDadosUsuario(UsuarioViewModel usuario) {
        final DocumentReference docRef = db.collection("usuarios").document(usuario.getId());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    usuario.setFotoPerfil((String) task.getResult().get("fotoPerfil"));
                    usuario.setNomeCompleto((String) task.getResult().get("nomeCompleto"));
                    usuario.setApelido((String) task.getResult().get("apelido"));
                    usuario.setEsportePreferido((String) task.getResult().get("esportePreferido"));
                    if(listParticipantes.size() > 0) {
                        for (int i = 0; i < listParticipantes.size(); i++) {
                            UsuarioViewModel uvm = listParticipantes.get(i);
                            if (uvm.getId().equals(task.getResult().getId())) {
                                listParticipantes.set(i, usuario);
                            } else {
                                listParticipantes.add(usuario);
                            }
                        }
                    }else{
                        listParticipantes.add(usuario);
                    }
                    inflateListViewMeusAmigos();
                }
            }
        });
    }
}