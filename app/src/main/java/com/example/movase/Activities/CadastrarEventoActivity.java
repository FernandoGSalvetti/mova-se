package com.example.movase.Activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.movase.Enum.Esportes;
import com.example.movase.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class CadastrarEventoActivity extends AppCompatActivity {
    private AutoCompleteTextView autoCompleteTextViewModadalidade;
    private TextInputEditText etNome;
    private MaterialToolbar materialToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_evento);
        inicializaComponentes();
        ArrayAdapter<Esportes> adapterEsportesPref = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Esportes.values());
        autoCompleteTextViewModadalidade.setAdapter(adapterEsportesPref);
        materialToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goToMainActivity);
                finish();
            }
        });
    }

    private void inicializaComponentes() {
        autoCompleteTextViewModadalidade = findViewById(R.id.cadastrar_evento_activity_spinner_modalidade);
        etNome = findViewById(R.id.cadastrar_evento_activity_etNome);
        materialToolbar = findViewById(R.id.cadastrar_evento_activity_topAppBar);
    }
}
