package com.example.go4lunch.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.go4lunch.models.User;
import com.example.go4lunch.viewmodels.UserViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity {

    private TextView firstname, lastname;
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

        // Initialize views
        firstname = findViewById(R.id.txtviewFirstname);

        btnDelete = findViewById(R.id.buttonDeleteAccount);
                // Get the currently logged-in user
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Configure ViewModels
        configureViewModels();

        userViewModel.getUserLiveData().observe(this, user -> {
            currentUser = user;
            firstname.setText(user.getName().split(" ")[0]); // Display the first name

        });


        btnDelete.setOnClickListener(view -> {
            deleteUser(currentUser.getUserId());
        });
    }

    private void configureViewModels() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getCurrentUserFromFirestore(mAuth.getCurrentUser().getUid());
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Return to the previous activity when the Toolbar's back button is clicked
        onBackPressed();
        return true;
    }

    public void deleteUser(String userId) {
        // Delete the user from Firebase Authentication
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
