package com.example.movase.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.movase.Adapters.ListAmizadeAdapter;
import com.example.movase.Adapters.ListEventosAdapter;
import com.example.movase.Adapters.ListPessoasAdapter;
import com.example.movase.Models.AmizadeViewModel;
import com.example.movase.Models.EventoViewModel;
import com.example.movase.R;
import com.example.movase.Repositories.AmizadeRepository;
import com.example.movase.Repositories.FireStore;
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

public class AmigosFragment extends Fragment {
    private ListView meusAmigosListView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserFirebase userFirebase = new UserFirebase();
    private List<AmizadeViewModel> listMeusAmigos = new ArrayList<>(); // Obtenha sua lista de objetos aqui
    ListAmizadeAdapter adapterAmigos;
    private AmizadeRepository amizadeRep = new AmizadeRepository();

    public static AmigosFragment newInstance() {
        return new AmigosFragment();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        inicializaComponentes(view);
        observaListaDeAmigos();
        meusAmigosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = meusAmigosListView.getItemAtPosition(position);
                AmizadeViewModel amizade = (AmizadeViewModel) o; //As you are using Default String Adapter
                if(amizade.isAmigo()) {
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle("")
                            .setMessage(amizade.getNomeCompleto())
                            .setNegativeButton("Remover amigo", (dialog, which) -> {
                                    amizadeRep.removeAmizade(userFirebase.getCurrentUserUser().getUid(), amizade.getIdUsuario(), getActivity());
                            })
                            .show();
                }else{
                    if (amizade.getIdSolicitante().equalsIgnoreCase(userFirebase.getCurrentUserUser().getUid())) {
                        new MaterialAlertDialogBuilder(getContext())
                                .setTitle("")
                                .setMessage(amizade.getNomeCompleto())
                                .setNegativeButton("Cancelar solicitação", (dialog, which) -> {
                                        amizadeRep.solicitacaoDesfeita(userFirebase.getCurrentUserUser().getUid(), amizade.getIdUsuario(), getActivity());
                                })
                                .show();

                    }else{
                        new MaterialAlertDialogBuilder(getContext())
                                .setTitle("")
                                .setMessage(amizade.getNomeCompleto())
                                .setPositiveButton("Aceitar solicitação", (dialog, which) -> {
                                    amizadeRep.aceitarSolicitacaoDeAmizade(userFirebase.getCurrentUserUser().getUid(), amizade.getIdUsuario(), getActivity());
                                })
                                .setNegativeButton("Recusar solicitação", (dialog, which) -> {
                                    amizadeRep.recusarSolicitacaoDeAmizade(userFirebase.getCurrentUserUser().getUid(), amizade.getIdUsuario(), getActivity());
                                })
                                .show();

                    }
                }
            }
        });
    }
    private void inflateListViewMeusAmigos() {
        if(listMeusAmigos.size() > 0){
            adapterAmigos = new ListAmizadeAdapter(getActivity(), listMeusAmigos);
            meusAmigosListView.setAdapter(adapterAmigos);
        }else{
            meusAmigosListView.setVisibility(View.GONE);
        }
    }
    private void observaListaDeAmigos() {
        db.collection("usuarios")
                .document(userFirebase.getCurrentUserUser().getUid())
                .collection("amigos")
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
                            AmizadeViewModel amizade = new AmizadeViewModel();
                            amizade.setIdUsuario((String) dc.getDocument().get("id"));
                            amizade.setAmigo((Boolean) dc.getDocument().get("amigo"));
                            amizade.setIdSolicitante((String) dc.getDocument().get("idSolicitante"));
                            if(dc.getType() == DocumentChange.Type.REMOVED){
                                for(int i = 0; i < listMeusAmigos.size(); i++){
                                    AmizadeViewModel a = listMeusAmigos.get(i);
                                    if(a.getIdUsuario().equalsIgnoreCase(amizade.getIdUsuario())) {
                                        listMeusAmigos.remove(a);
                                        inflateListViewMeusAmigos();
                                    }
                                }
                            }else{
                                getDadosUsuario(amizade);
                            }
                        }

                    }
                });
    }

    private void getDadosUsuario(AmizadeViewModel amizade) {
        Log.d("TAG", "getDadosUsuario " + amizade.getIdSolicitante());
        final DocumentReference docRef = db.collection("usuarios").document(amizade.getIdUsuario());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    amizade.setFotoPerfil((String) task.getResult().get("fotoPerfil"));
                    amizade.setNomeCompleto((String) task.getResult().get("nomeCompleto"));
                    amizade.setApelido((String) task.getResult().get("apelido"));
                    amizade.setEsportePreferido((String) task.getResult().get("esportePreferido"));
                    if(listMeusAmigos.size() > 0) {
                        for (int i = 0; i < listMeusAmigos.size(); i++) {
                            AmizadeViewModel avm = listMeusAmigos.get(i);
                            if (avm.getIdUsuario().equals(task.getResult().getId())) {
                                listMeusAmigos.set(i, amizade);
                            } else {
                                listMeusAmigos.add(amizade);
                            }
                        }
                    }else{
                        listMeusAmigos.add(amizade);
                    }
                    inflateListViewMeusAmigos();
                }
            }
        });
    }

    private void inicializaComponentes(View view){
        meusAmigosListView = view.findViewById(R.id.fragments_amigos_list_view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_amigos, container, false);
    }
}