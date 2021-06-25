package com.example.movase.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.movase.R;
import com.example.movase.Repositories.LoginRepository;
import com.google.android.material.button.MaterialButton;

public class HomeFragment extends Fragment {
    private MaterialButton btnLogout;
    private LoginRepository loginRepository = new LoginRepository();
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        btnLogout = view.findViewById(R.id.fragment_home_btnSair);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRepository.tryLogout(getActivity());
                Toast.makeText(v.getContext(), "Teste", Toast.LENGTH_SHORT).show();
            }
        });
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