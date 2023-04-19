package com.example.go4lunch.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.ui.manager.UserManager;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    // Définition de la constante RC_SIGN_IN qui sera utilisée pour identifier la requête de connexion
    private static final int RC_SIGN_IN = 123;
    private final UserManager userManager = UserManager.getInstance();
    // Création d'une instance de la classe ActivityMainBinding pour lier la vue avec l'activité
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Définition de la vue de l'activité
        setContentView(R.layout.activity_login);

        setupListener();
    }

    private void setupListener() {
        if (userManager.isCurrentUserLogged()) {
            startProfileActivity();
        } else {
            startSignInActivity();
        }
    }

    private void startSignInActivity() {
        // Définition de la liste des fournisseurs d'authentification disponibles
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Création d'une intention de connexion et lancement de l'activité correspondante
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.logo_go4lunch_launcher)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupListener();
    }

    // Launching Profile Activity
    private void startProfileActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Gestion de la réponse de l'activité de connexion
        handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // Affiche un message dans une barre de notification en bas de l'écran
    private void showSnackBar(String message) {
        if (binding != null && binding.getRoot() != null) {
            Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    // Gère la réponse de l'activité de connexion
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {

        // Récupération de la réponse de l'activité de connexion
        IdpResponse response = IdpResponse.fromResultIntent(data);

        // Vérification que la réponse concerne bien la requête de connexion
        if (requestCode == RC_SIGN_IN) {
            // CONNEXION RÉUSSIE
            if (resultCode == RESULT_OK) {
                //userManager.createUser();
                // Affichage d'un message de connexion réussie
                showSnackBar(getString(R.string.connection_succeed));
            } else {
                // ERREURS
                if (response == null) {
                    // Affichage d'un message indiquant que l'utilisateur a annulé la connexion
                    showSnackBar(getString(R.string.error_authentication_canceled));
                } else if (response.getError() != null) {
                    // Affichage d'un message d'erreur en fonction du code d'erreur retourné
                    if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                        showSnackBar(getString(R.string.error_no_internet));
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar(getString(R.string.error_unknown_error));
                    }
                }
            }
        }
    }
}