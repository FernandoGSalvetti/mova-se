package com.example.movase.Repositories;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireStore {
    // essa classe retorna a instância do Firestore para fazer a inserção, exclusão e atualização dos dados
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseFirestore getDb() {
        return db;
    }
}