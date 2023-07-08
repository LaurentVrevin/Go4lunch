package com.example.go4lunch.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityLoginBinding;

import com.example.go4lunch.viewmodels.UserViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private ActivityLoginBinding binding;
    private UserViewModel userViewModel;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Récupération des instances Firebase Authentication et Firestore
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Vérification si l'utilisateur est déjà connecté ou non
        if (mAuth.getCurrentUser() != null) {
            // Si oui, démarrage de l'activité principale
            startMainActivity();
        } else {
            // Sinon, démarrage de l'activité de connexion
            startLoginActivity();
        }
    }

    // Méthode pour démarrer l'activité de connexion
    private void startLoginActivity() {
        // Configuration des fournisseurs d'authentification
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());


        // Création de l'intent de connexion avec FirebaseUI
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.logo_go4lunch_launcher)
                        .build(),
                RC_SIGN_IN); // Ajout du code de demande de connexion
    }

    // Méthode pour démarrer l'activité principale
    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Méthode appelée après la fin de l'activité de connexion FirebaseUI
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Vérification si la demande de connexion provient de FirebaseUI
        if (requestCode == RC_SIGN_IN) {
            // Récupération de la réponse de connexion
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Vérification si la connexion a réussi ou non
            if (resultCode == RESULT_OK) {
                // Si oui, création de l'utilisateur dans Firestore et démarrage de l'activité principale
                userViewModel.createUserInFirestore();
                startMainActivity();
            } else {
                // Si non, affichage d'un message d'erreur dans un Snackbar
                if (response == null) {
                    showSnackBar(getString(R.string.error_authentication_canceled));
                } else if (response.getError() != null) {
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackBar(getString(R.string.error_no_internet));
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar(getString(R.string.error_unknown_error));
                    }
                }
            }
        }
    }


    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }
}
