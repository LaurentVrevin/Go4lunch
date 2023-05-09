package ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.go4lunch.R;

public class Go4LunchLauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go4_lunch_launcher);
        //On cache l'actionbar
        getSupportActionBar().hide();


        // Redirige vers la page principale : main activity après 3 secondes
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // démarrer une page
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };
        // handler post delayed
        new Handler().postDelayed(runnable, 3000);
    }
}