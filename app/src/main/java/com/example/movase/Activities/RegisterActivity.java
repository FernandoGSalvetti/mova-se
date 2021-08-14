package com.example.movase.Activities;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.movase.Enum.Esportes;
import com.example.movase.Models.UsuarioRegister;
import com.example.movase.R;
import com.example.movase.Repositories.CreateUserRepository;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty(message = "O campo e-mail não pode estar vazio")
    @Email(message = "E-mail inválido")
    private TextInputEditText email;

    @NotEmpty(message = "O campo senha não pode estar vazio")
    @Password(min = 6, message = "Senha deve conter pelo menos 6 caracteres")
    private TextInputEditText senha;

    @NotEmpty(message = "O campo nome não pode estar vazio")
    @Length(min = 10, message = "O campo nome deve conter pelo menos 10 caracteres")
    private TextInputEditText nome;

    @NotEmpty(message = "O campo apelido não pode estar vazio")
    @Length(min = 2, message = "O campo nome deve conter pelo menos 2 caracteres")
    private TextInputEditText apelido;

    @NotEmpty(message = "O campo celular não pode estar vazio")
    @Length(min = 15, message = "O campo celular deve conter 11 caracteres")
    private TextInputEditText celular;

    @NotEmpty(message = "O campo endereço não pode estar vazio")
    @Length(min = 10, message = "O campo endereço deve conter pelo menos 10 caracteres")
    private TextInputEditText endereco;

    @NotEmpty(message = "O campo data de nascimento não pode estar vazio")
    private TextInputEditText dataNasc;

    @Checked(message = "Você deve declarar o seu sexo")
    private RadioGroup rgSexo;

    @Checked(message = "Você deve declarar o sexo preferencial para conexão")
    private RadioGroup rgSexoPref;

    @Checked(message = "Você deve marcar o checkbox de termos de uso e regras do aplicativo")
    private CheckBox checkBoxTermos;

    private Button btRegister;
    private AutoCompleteTextView spinnerEsportsPref;
    private ProgressBar pbRegister;
    private RadioButton sexo;
    private RadioButton sexoPref;
    private UsuarioRegister usuario = new UsuarioRegister();

    final Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day_of_month = calendar.get(Calendar.DAY_OF_MONTH);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Validator validator = new Validator(this);
        validator.setValidationListener(this);

        inicializaComponentes();

        //Mascara para o campo de celular
        SimpleMaskFormatter mfCelular = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtwCelular = new MaskTextWatcher(celular, mfCelular);
        celular.addTextChangedListener(mtwCelular);
        //Mascara para o campo de Data de Nascimento
        SimpleMaskFormatter mfDataNasc = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwDataNasc = new MaskTextWatcher(dataNasc, mfDataNasc);
        dataNasc.addTextChangedListener(mtwDataNasc);

        ArrayAdapter<Esportes> adapterEsportesPref = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Esportes.values());
        spinnerEsportsPref.setAdapter(adapterEsportesPref);
        onChangeField(email, usuario, "email");
        onChangeField(senha, usuario, "senha");
        onChangeField(nome, usuario, "nomeCompleto");
        onChangeField(apelido, usuario, "apelido");
        onChangeField(celular, usuario, "celular");
        onChangeField(endereco, usuario, "endereco");
        onChangeField(dataNasc, usuario, "dataNascimento");
        spinnerEsportsPref.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                usuario.setEsportePreferido(s.toString());
            }
        });

        dataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateDatePicker();
            }
        });
        rgSexo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                sexo = findViewById(checkedId);
                usuario.setSexo(sexo.getText().toString());
            }
        });
        rgSexoPref.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                sexoPref = findViewById(checkedId);
                usuario.setSexoPrefConexao(sexoPref.getText().toString());
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

    }

    private void inicializaComponentes() {
        email = findViewById(R.id.register_activity_etEmail);
        senha = findViewById(R.id.register_activity_etSenha);
        pbRegister = findViewById(R.id.register_activity_pgRegister);
        nome = findViewById(R.id.register_activity_etNome);
        apelido = findViewById(R.id.register_activity_etApelido);
        celular = findViewById(R.id.register_activity_etCelular);
        endereco = findViewById(R.id.register_activity_etEndereco);
        dataNasc = findViewById(R.id.register_activity_etDate);
        btRegister = findViewById(R.id.register_activity_btRegister);
        spinnerEsportsPref = findViewById(R.id.activity_editar_perfil_esporte_preferido);
        rgSexo = findViewById(R.id.register_activity_RGSexo);
        rgSexoPref = findViewById(R.id.register_activity_rgSexoPref);
        checkBoxTermos = findViewById(R.id.register_activity_cbTermos);
    }

    public String formatDate(int year, int month, int day){
        return day + "/" + month + "/" + year;
    }

    public void inflateDatePicker(){
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar upDate = Calendar.getInstance();
                upDate.set(year, month, dayOfMonth);
                dataNasc.setText(formatDate(year, month + 1, dayOfMonth));
                usuario.setDataNascimento(formatDate(year, month + 1, dayOfMonth));
                Log.d("CRUD", usuario.getDataNascimento());
            };
        }, year, month, day_of_month);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void onChangeField(TextInputEditText editText, UsuarioRegister usuarioRegister, String field){
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
                    case "email":
                        usuarioRegister.setEmail(et.toString());
                        break;
                    case "senha":
                        usuarioRegister.setSenha(et.toString());
                        break;
                    case "nomeCompleto":
                        usuarioRegister.setNomeCompleto(et.toString());
                        break;
                    case "apelido":
                        usuarioRegister.setApelido(et.toString());
                        break;
                    case "celular":
                        usuarioRegister.setCelular(et.toString());
                        break;
                    case "endereco":
                        usuarioRegister.setEndereco(et.toString());
                        break;
                    case "dataNascimento":
                        usuarioRegister.setDataNascimento(et.toString());
                        Log.d("CRUD", usuario.toString());
                        break;
                    default:
                        break;
                }
            }
        });
    };

    @Override
    public void onValidationSucceeded() {
        pbRegister.setVisibility(View.VISIBLE);
        CreateUserRepository userRepository = new CreateUserRepository();
        userRepository.createUser(RegisterActivity.this, this.usuario);
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