package com.example.movase.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.movase.Fragments.AmigosFragment;
import com.example.movase.Fragments.ChatFragment;
import com.example.movase.Fragments.HomeFragment;
import com.example.movase.Fragments.PesquisarFragment;
import com.example.movase.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FloatingActionButton fabCriarEvento;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializaComponentes();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Fragment homeFragment = HomeFragment.newInstance();
        openFragment(homeFragment);
        fabCriarEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadastrarEvento = new Intent(getApplicationContext(), CadastrarEventoActivity.class);
                startActivity(cadastrarEvento);
                finish();
            }
        });
    }

    private void inicializaComponentes() {
        fabCriarEvento = findViewById(R.id.main_activity_fab_criar_evento);
        bottomNavigationView = findViewById(R.id.main_activity_bottom_navigation);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Fragment homeFragment = HomeFragment.newInstance();
                openFragment(homeFragment);
            break;
            case R.id.nav_chat:
                Fragment chatFragment = ChatFragment.newInstance();
                openFragment(chatFragment);
                break;
            case R.id.nav_pesquisar:
                Fragment pesquisarFragment = PesquisarFragment.newInstance();
                openFragment(pesquisarFragment);
                break;
            case R.id.nav_amigos:
                Fragment amigosFragment = AmigosFragment.newInstance();
                openFragment(amigosFragment);
                break;
        }
        return true;
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_activity_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}