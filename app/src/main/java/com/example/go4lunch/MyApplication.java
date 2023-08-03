package com.example.go4lunch;

import android.app.Application;

import com.google.firebase.firestore.FirebaseFirestore;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApplication extends Application {
    private static FirebaseFirestore firestoreInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        firestoreInstance = FirebaseFirestore.getInstance();
    }

    public static FirebaseFirestore getFirestoreInstance() {
        return firestoreInstance;
    }

    //pour rappel je pourrais récupérer cette instance avec :
    // "FirebaseFirestore firestore = ((MyApplication) getApplication()).getFirestore();"
}
