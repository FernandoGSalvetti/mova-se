package com.example.movase.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movase.Adapters.ListEventosAdapter;
import com.example.movase.Adapters.ListPessoasAdapter;
import com.example.movase.Enum.Esportes;
import com.example.movase.Models.EventoViewModel;
import com.example.movase.Models.UsuarioViewModel;
import com.example.movase.R;
import com.example.movase.Repositories.AmizadeRepository;
import com.example.movase.Repositories.EventoRepository;
import com.example.movase.Repositories.FireStore;
import com.example.movase.Repositories.UserFirebase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PesquisarFragment extends Fragment {

    private AutoCompleteTextView autoCompleteTextViewTipoPesquisa;
    private AutoCompleteTextView autoCompleteTextViewTipoDeDado;
    private TextInputEditText etBusca;
    private TextView tvInstrucao;
    private String colecao = "";
    private String field = "";
    private MaterialButton btBusca;
    private ListEventosAdapter adapterMeusEventos;
    private ArrayAdapter<String> adapterDados;
    ListPessoasAdapter adapterPesquisaUsuario;
    private ArrayList<UsuarioViewModel> arrayListResultadoPesquisaUsuario = new ArrayList<UsuarioViewModel>();
    private ArrayList<EventoViewModel> arrayListResultadoPesquisaEvento = new ArrayList<EventoViewModel>();
    FireStore fireStore = new FireStore();
    String[] tipoDePesquisa = {"Evento", "Usuário"};
    String[] dadoEvento = {"Modalidade", "Nome do evento"};
    String[] dadoUsuario = {"Nome completo", "Apelido"};
    ListView listViewPesquisa;
    UserFirebase userFirebase = new UserFirebase();
    private EventoRepository eventoRepository = new EventoRepository();
    private AmizadeRepository amizadeRep = new AmizadeRepository();
    public static PesquisarFragment newInstance() {
        return new PesquisarFragment();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        super.onPause();
        super.onResume();
        inicializaComponentes(view);
        ArrayAdapter<String> adapterPesquisa = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, tipoDePesquisa);
        autoCompleteTextViewTipoDeDado.setVisibility(View.GONE);
        autoCompleteTextViewTipoPesquisa.setAdapter(adapterPesquisa);
        autoCompleteTextViewTipoPesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("Tipo pesquisa", s.toString());
                if(s.toString().equalsIgnoreCase("Evento")){
                    colecao = "eventos";
                    tvInstrucao.setText("Digite a modalidade ou nome do evento");
                    setVisibilitySelectTipoDeDado();
                    setAdapterDados(dadoEvento);
                }else{
                    colecao = "usuarios";
                    tvInstrucao.setText("Digite o nome do usuário ou apelido");
                    setVisibilitySelectTipoDeDado();
                    setAdapterDados(dadoUsuario);
                }
            }
        });
        autoCompleteTextViewTipoDeDado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equalsIgnoreCase("Nome do evento")){
                    field = "nome";
                }
                if(s.toString().equalsIgnoreCase("Modalidade")){
                    field = "modalidade";
                }
                if(s.toString().equalsIgnoreCase("Nome completo")){
                    field = "nomeCompleto";
                }
                if(s.toString().equalsIgnoreCase("Apelido")){
                    field = "apelido";
                }
            }
        });
        btBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(colecao.isEmpty() || field.isEmpty() || etBusca.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Preencha todos campos corretamente para realizar a pesquisa", Toast.LENGTH_SHORT).show();
                }else{
                    query(colecao, field, etBusca.getText().toString());
                }
            }
        });
        listViewPesquisa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(arrayListResultadoPesquisaUsuario.size() > 0){
                    UsuarioViewModel user = arrayListResultadoPesquisaUsuario.get(position);
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle(user.getNomeCompleto())
                            .setMessage("")
                            .setNegativeButton("Fechar", (dialog, which) -> {
                                dialog.cancel();
                            })
                            .setPositiveButton("Enviar solicitação de amizade", (dialog, which) -> {
                                amizadeRep.verificaSeEhAmigoOuJaEnviouSolicitacao(userFirebase.getCurrentUserUser().getUid(), user.getId(), getActivity());
                            })
                            .show();

                }else{
                    EventoViewModel evento = arrayListResultadoPesquisaEvento.get(position);
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle(evento.getNome())
                            .setMessage("")
                            .setNegativeButton("Fechar", (dialog, which) -> {
                                dialog.cancel();
                            })
                            .setPositiveButton("Participar deste evento", (dialog, which) -> {
                                eventoRepository.verificaSeJaEstaParticipandoDoEvento(evento, userFirebase.getCurrentUserUser().getUid(), getActivity());
                            })
                            .show();
                }
            }
        });
    }
    private void inflateListViewPesquisa() {
        if(colecao.equalsIgnoreCase("usuarios")){
            if(arrayListResultadoPesquisaUsuario.size() > 0){
                Log.d("TAG", "INFLATE USER");
                adapterPesquisaUsuario = new ListPessoasAdapter(getActivity(), arrayListResultadoPesquisaUsuario);
            }else{
                listViewPesquisa.setVisibility(View.GONE);
            }
            listViewPesquisa.setAdapter(adapterPesquisaUsuario);
        }else{
            if(arrayListResultadoPesquisaEvento.size() > 0){
                adapterMeusEventos = new ListEventosAdapter(getActivity(), arrayListResultadoPesquisaEvento);
            }else{
                listViewPesquisa.setVisibility(View.GONE);
            }
        listViewPesquisa.setAdapter(adapterMeusEventos);
        }
    }
    private void setAdapterDados(String [] dados){
        adapterDados = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, dados);
        autoCompleteTextViewTipoDeDado.setAdapter(adapterDados);
    }
    private void setVisibilitySelectTipoDeDado() {
        autoCompleteTextViewTipoDeDado.setVisibility(View.VISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pesquisar, container, false);
    }
    public void query(String colecao, String field, String pesquisa){
        CollectionReference queryRef = fireStore.getDb().collection(colecao);
        queryRef.whereEqualTo(field, pesquisa).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(value.size() > 0){
                    Log.d("TAG", "veio aqui");
                    if(colecao.equalsIgnoreCase("usuarios")){
                        arrayListResultadoPesquisaEvento.clear();
                        arrayListResultadoPesquisaUsuario.clear();
                        for(DocumentSnapshot ds : value.getDocuments()){
                            UsuarioViewModel usuarioViewModel = ds.toObject(UsuarioViewModel.class);
                            usuarioViewModel.setId(ds.getId());
                            if(usuarioViewModel.getId().equalsIgnoreCase(userFirebase.getCurrentUserUser().getUid()) == false){
                                arrayListResultadoPesquisaUsuario.add(usuarioViewModel);
                                inflateListViewPesquisa();
                            }
                        }
                    }else{
                        arrayListResultadoPesquisaUsuario.clear();
                        arrayListResultadoPesquisaEvento.clear();
                        for(DocumentSnapshot ds : value.getDocuments()){
                            EventoViewModel eventoViewModel = ds.toObject(EventoViewModel.class);
                            eventoViewModel.setId(ds.getId());
                            if(eventoViewModel.getIdCriadorDoEvento().equalsIgnoreCase(userFirebase.getCurrentUserUser().getUid()) == false){
                                Log.d("TAG", "Aqui");
                                arrayListResultadoPesquisaEvento.add(eventoViewModel);
                                inflateListViewPesquisa();
                            }
                        }
                    }
                }else{
                    Log.d("TAG", String.valueOf(value.size()));
                    arrayListResultadoPesquisaEvento.clear();
                    arrayListResultadoPesquisaUsuario.clear();
                    Toast.makeText(getActivity(), "Não foi encontrado nenhum resultado, verifique os dados da busca.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void inicializaComponentes(View view){
        etBusca = view.findViewById(R.id.fragment_pesquisar_etBusca);
        autoCompleteTextViewTipoPesquisa = view.findViewById(R.id.fragment_pesquisar_select_tipo_busca);
        tvInstrucao = view.findViewById(R.id.fragment_pesquisar_tvInstrucaoPesquisa);
        btBusca = view.findViewById(R.id.fragment_pesquisar_btn_buscar);
        autoCompleteTextViewTipoDeDado = view.findViewById(R.id.fragment_pesquisar_select_tipo_dado);
        listViewPesquisa = view.findViewById(R.id.fragment_pesquisar_list_view_result);
    }
}