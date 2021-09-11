package com.example.movase.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.movase.Enum.Esportes;
import com.example.movase.Models.Evento;
import com.example.movase.Models.EventoViewModel;
import com.example.movase.Models.UsuarioRegister;
import com.example.movase.R;
import com.example.movase.Repositories.EventoRepository;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

public class CadastrarEventoActivity extends AppCompatActivity implements Validator.ValidationListener {
    private AutoCompleteTextView autoCompleteTextViewModadalidade;
    String[] esportes = new String[]{"Basquete",
            "Calistenia",
            "Corrida",
            "Caminhada",
            "Musculação",
            "Futebol",
            "Futsal",
            "Fut7",
            "Volêi"};
    @NotEmpty(message = "O campo data não pode estar vazio!")
    @Length(min = 5, message = "O campo data deve ser preenchido corretamente!")
    private TextInputEditText etData;
    @NotEmpty(message = "O campo horário não pode estar vazio!")
    @Length(min = 5, message = "O campo horário deve ser preenchido corretamente!")
    private TextInputEditText etHorario;
    @NotEmpty(message = "O campo endereço não pode estar vazio!")
    @Length(min = 5, message = "O campo endereço deve ser preenchido corretamente!")
    private TextInputEditText etEndereco;
    private MaterialButton btnCadastrarEvento;
    private MaterialButton btnVerParticipantes;
    @NotEmpty(message = "O campo nome não pode estar vazio!")
    @Length(min = 5, message = "O campo nome deve conter pelo menos 5 caracteres!")
    private TextInputEditText etNome;
    @Checked(message = "Você deve declarar o tipo de evento")
    private RadioGroup tipoDeEvento;
    private RadioButton tipoEvento;
    private MaterialToolbar materialToolbar;
    private EventoViewModel evento = new EventoViewModel();
    private String id = "";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayAdapter<String> adapterEsportesPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        id = intent.getStringExtra("idEvento");

        setContentView(R.layout.activity_cadastrar_evento);
        inicializaComponentes();

        Validator validator = new Validator(this);
        validator.setValidationListener(this);
        adapterEsportesPref = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, esportes);

        SimpleMaskFormatter mfDataNasc = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwDataNasc = new MaskTextWatcher(etData, mfDataNasc);
        etData.addTextChangedListener(mtwDataNasc);

        SimpleMaskFormatter mfHorario = new SimpleMaskFormatter("NN:NN");
        MaskTextWatcher mtwHorario = new MaskTextWatcher(etHorario, mfHorario);
        etHorario.addTextChangedListener(mtwHorario);
        onChangeField(etData, "data");
        onChangeField(etNome, "nome");
        onChangeField(etEndereco, "endereco");
        onChangeField(etHorario, "horario");

        tipoDeEvento.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                tipoEvento = findViewById(checkedId);
                evento.setTipoDeEvento(tipoEvento.getText().toString());
            }
        });
        autoCompleteTextViewModadalidade.setAdapter(adapterEsportesPref);


        autoCompleteTextViewModadalidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                evento.setModalidade(s.toString());
            }
        });


        materialToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goToMainActivity);
                finish();
            }
        });
        btnCadastrarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        btnVerParticipantes.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent irParaListagemDeParticipante = new Intent(getApplicationContext(), ListarParticipantesAcitivity.class);
                irParaListagemDeParticipante.putExtra("idEvento", id);
                irParaListagemDeParticipante.putExtra("nomeEvento", evento.getNome());
                startActivity(irParaListagemDeParticipante);
            }
        });
        if(!id.isEmpty()){
            btnVerParticipantes.setVisibility(View.VISIBLE);
            populaComponentes(id);
        }
    }
    private void selectValue(ArrayAdapter adapter, String value) {
        for (int i=0;i< esportes.length;i++) {
            if (esportes[i].equals(value)) {
                autoCompleteTextViewModadalidade.setText(autoCompleteTextViewModadalidade.getAdapter().getItem(i).toString(), false);
            }
        }
    }
    private void populaComponentes(String idEvento) {
        db.collection("eventos").document(idEvento).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null){
                    evento = documentSnapshot.toObject(EventoViewModel.class);
                    evento.setId(idEvento);
                    etNome.setText(evento.getNome());
                    selectValue(adapterEsportesPref, evento.getModalidade());
                    materialToolbar.setTitle("Editar Evento");
                    etData.setText(evento.getData());

                    if(evento.getTipoDeEvento().equalsIgnoreCase("aberto")){
                        tipoDeEvento.check(R.id.cadastrar_evento_activity_rbAberto);
                    }else{
                        tipoDeEvento.check(R.id.cadastrar_evento_activity_rbPrivado);
                    }
                    etEndereco.setText(evento.getEndereco());
                    etHorario.setText(evento.getHorario());
                    btnCadastrarEvento.setText("Salvar alterações");
                }
            }
        });

    }

    private void inicializaComponentes() {
        autoCompleteTextViewModadalidade = findViewById(R.id.activity_cadastrar_evento_select_modalidade);
        etNome = findViewById(R.id.activity_cadastrar_evento_etNome);
        materialToolbar = findViewById(R.id.activity_cadastrar_evento_topAppBar);
        btnCadastrarEvento = findViewById(R.id.cadastrar_evento_activity_btCriarEvento);
        tipoDeEvento = findViewById(R.id.cadastrar_evento_activity_rgTipoEvento);
        etData =  findViewById(R.id.cadastrar_evento_activity_etData);
        etEndereco = findViewById(R.id.cadastrar_evento_activity_etEndereco);
        etHorario = findViewById(R.id.cadastrar_evento_activity_etHorario);
        materialToolbar = findViewById(R.id.activity_cadastrar_evento_topAppBar);
        btnVerParticipantes = findViewById(R.id.cadastrar_evento_activity_listar_participantes);
    }
    private void onChangeField(TextInputEditText editText, String field){
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
                    case "nome":
                        evento.setNome(et.toString());
                        break;
                    case "horario":
                        evento.setHorario(et.toString());
                        break;
                    case "data":
                        evento.setData(et.toString());
                        Log.d("CRUD", evento.getData());
                        break;
                    case "endereco":
                        evento.setEndereco(et.toString());
                        break;
                    case "tipoDeEvento":
                        evento.setTipoDeEvento(et.toString());
                        break;
                    default:
                        break;
                }
            }
        });
    };
    @Override
    public void onValidationSucceeded() {
        EventoRepository eventoRepository = new EventoRepository();
        if(id.isEmpty()){
            eventoRepository.criarEvento(this, evento);
        }else{
            Log.d("CRUD", evento.getData());
            eventoRepository.atualizaEvento(this, evento);
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
