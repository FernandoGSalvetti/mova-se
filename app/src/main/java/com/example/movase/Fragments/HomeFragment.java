package com.example.movase.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movase.Activities.CadastrarEventoActivity;
import com.example.movase.Activities.EditarPerfilActivity;
import com.example.movase.Models.ClasseMultiuso;
import com.example.movase.Models.EventoViewModel;
import com.example.movase.Models.UsuarioViewModel;
import com.example.movase.R;
import com.example.movase.Repositories.EventoRepository;
import com.example.movase.Repositories.LoginRepository;
import com.example.movase.Repositories.UserFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class HomeFragment extends Fragment {
    private ImageView btnLogout;
    private ImageView fotoPerfil;
    private TextView tvEsportePreferido;
    private TextView tvApelido;
    private TextView tvNomeCompleto;

    private FragmentContainerView eventosQueVouParticipar;
    private FragmentContainerView fragmentListMeusEventos;

    private LoginRepository loginRepository = new LoginRepository();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserFirebase userFirebase = new UserFirebase();
    private UsuarioViewModel usuarioViewModel = new UsuarioViewModel();
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        super.onPause();
        super.onResume();
        observaDadosUsuario();
        inicializaComponentes(view);
        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EditarPerfilIntent = new Intent(getContext(), EditarPerfilActivity.class);
                EditarPerfilIntent.putExtra("usuario", usuarioViewModel);
                getContext().startActivity(EditarPerfilIntent);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loginRepository.tryLogout(getActivity());
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Sair")
                        .setMessage("Você realmente deseja sair?")
                        .setNegativeButton(R.string.nao, (dialog, which) -> {
                            dialog.cancel();
                        })
                        .setPositiveButton(R.string.sim, (dialog, which) -> {
                            loginRepository.tryLogout(getActivity());
                        })
                        .show();
            }
        });
    }

    private void populaComponentes() {
        try{
            tvApelido.setText(usuarioViewModel.getApelido());
            tvNomeCompleto.setText(usuarioViewModel.getNomeCompleto());
            tvEsportePreferido.setText(usuarioViewModel.getEsportePreferido());
            if(usuarioViewModel.getFotoPerfil().isEmpty()){
                fotoPerfil.setImageResource(R.drawable.ic_baseline_account_circle_24);
            }else{
                Picasso.with(getContext()).load(usuarioViewModel.getFotoPerfil()).transform(new CropCircleTransformation()).into(fotoPerfil);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void observaDadosUsuario() {
        //Este método é responsável por observar os dados do usuário e sempre atualizá-los na tela
        final DocumentReference docRef = db.collection("usuarios").document(userFirebase.getCurrentUserUser().getUid());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("GETUSER", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    usuarioViewModel = snapshot.toObject(UsuarioViewModel.class);
                    populaComponentes();
                } else {
                    Log.d("GETUSER", "Current data: null");
                }
            }
        });
    }


    private void inicializaComponentes(View view) {
        btnLogout = view.findViewById(R.id.frag_home_btn_sair);
        fotoPerfil = view.findViewById(R.id.frag_home_img_perfil);
        tvApelido = view.findViewById(R.id.frag_home_tvApelido);
        tvNomeCompleto = view.findViewById(R.id.frag_home_tvNome);
        tvEsportePreferido = view.findViewById(R.id.frag_home_tvEsportePreferido);
        fragmentListMeusEventos = view.findViewById(R.id.fragment_home_list_meus_eventos);
        eventosQueVouParticipar = view.findViewById(R.id.frag_home_list_view_eventos);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
}