package com.example.movase.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.movase.Activities.CadastrarEventoActivity;
import com.example.movase.Adapters.ListEventosAdapter;
import com.example.movase.Models.ClasseMultiuso;
import com.example.movase.Models.Evento;
import com.example.movase.Models.EventoViewModel;
import com.example.movase.Models.UsuarioViewModel;
import com.example.movase.R;
import com.example.movase.Repositories.EventoRepository;
import com.example.movase.Repositories.UserFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MeusEventosFragment extends Fragment {
    private List<EventoViewModel> listMeusEventos = new ArrayList<>(); // Obtenha sua lista de objetos aqui
    UserFirebase userFirebase = new UserFirebase();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView meusEventos;
    private EventoRepository eventoRepository = new EventoRepository();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_view_meus_eventos, container, false);
        inicializaComponentes(view);
        observaMeusEventos();
        meusEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = meusEventos.getItemAtPosition(position);
              EventoViewModel eventoViewModel = (EventoViewModel) o; //As you are using Default String Adapter
               new MaterialAlertDialogBuilder(getContext())
                    .setTitle("")
                  .setMessage(eventoViewModel.getNome())
                .setNegativeButton("Excluir evento", (dialog, which) -> {
                    eventoRepository.excluirEvento(eventoViewModel);
           })
            .setPositiveButton("Editar evento", (dialog, which) -> {
                Intent editarEvento = new Intent(getActivity(), CadastrarEventoActivity.class);
                   editarEvento.putExtra("idEvento", eventoViewModel.getId());
                    startActivity(editarEvento);
                 })
                .show();
         }
        });
        return view;
    }

    private void inflateListViewMeusEventos() {
        if(listMeusEventos.size() > 0){
            ListEventosAdapter adapterMeusEventos = new ListEventosAdapter(getActivity(), listMeusEventos, false);
            meusEventos.setAdapter(adapterMeusEventos);
        }else{
            meusEventos.setVisibility(View.GONE);
        }
    }
    private void inicializaComponentes(View view){
        meusEventos = view.findViewById(R.id.fragment_list_view_meus_eventos_list);
    }

    private void observaMeusEventos(){

        db.collection("usuarios")
                .document(userFirebase.getCurrentUserUser().getUid())
                .collection("meusEventos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            inflateListViewMeusEventos();
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            String idEvento = dc.getDocument().getString("id");
                            observaDadosDoEvento(idEvento);
                            if(dc.getType() == DocumentChange.Type.REMOVED){
                                for(int i = 0; i < listMeusEventos.size(); i++){
                                    EventoViewModel eve = listMeusEventos.get(i);
                                    if(eve.getId().equalsIgnoreCase(idEvento)){
                                        Log.d("TAG", "Evento removido: " + dc.getDocument().getData().get("id"));
                                        listMeusEventos.remove(eve);
                                        inflateListViewMeusEventos();
                                        Toast.makeText(getContext(), "Evento excluido com sucesso", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                    }
                });
    }

    private void observaDadosDoEvento(String idEvento){
        //Pega os dados em tempo real do evento fazendo com que pegue atualizações do mesmo
        final DocumentReference docRef = db.collection("eventos").document(idEvento);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("GETUSER", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    EventoViewModel eventoViewModel = snapshot.toObject(EventoViewModel.class);
                    eventoViewModel.setId(idEvento);
                    //Verifica se este evento já esta adicionado no list caso esteja ele apenas atualiza
                    if(listMeusEventos.size() > 0) {
                        for (int i = 0; i < listMeusEventos.size(); i++) {
                            EventoViewModel evM = listMeusEventos.get(i);
                            if (evM.getId().equals(idEvento)) {
                                listMeusEventos.set(i, eventoViewModel);
                            } else {
                                listMeusEventos.add(eventoViewModel);
                            }
                        }
                    }else{
                        listMeusEventos.add(eventoViewModel);
                    }
                    inflateListViewMeusEventos();
                    Log.d("TAG", eventoViewModel.getNome());
                } else {
                    Log.d("GETUSER", "Current data: null");
                }
            }
        });
    }
}
