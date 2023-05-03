package com.example.go4lunch.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import models.User;
import viewmodels.UserViewModel;

public class SettingsActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName;
    private Button btnDelete;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private UserViewModel userViewModel;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Initialisation des vues
        etFirstName  = findViewById(R.id.editTextFirstName);
        etLastName  = findViewById(R.id.editTextLastName);
        btnDelete = findViewById(R.id.buttonDeleteAccount);


        // Récupération de l'utilisateur actuellement connecté
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // Initialisation du ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        userViewModel.getCurrentUserFromFirestore(mAuth.getCurrentUser().getUid());
        userViewModel.getUserLiveData().observe(this, user -> {
            currentUser = user;
            etFirstName.setText(user.getName().split(" ")[0]); // affiche le prénom
            etLastName.setText(user.getName().split(" ")[1]); // affiche le nom
        });

        btnDelete.setOnClickListener(view -> {
            deleteUser(currentUser.getUserId());
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Retour à l'activité précédente quand on clique sur le bouton de retour de la Toolbar
        onBackPressed();
        return true;
    }
    public void deleteUser(String userId) {
        // Supprimer l'utilisateur de Firebase Authentication
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_title)
                .setMessage(R.string.delete_message)
                .setPositiveButton(R.string.logout_positive_button, (dialog, which) -> {
                    userViewModel.deleteAccount(this);
                    mAuth.signOut();
                    Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton(R.string.logout_negative_button, null)
                .show();
    }

}