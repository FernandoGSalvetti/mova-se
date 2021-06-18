package com.example.movase.Repositories;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireStore {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirebaseFirestore getDb() {
        return db;
    }
}