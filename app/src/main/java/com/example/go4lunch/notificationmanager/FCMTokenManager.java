package com.example.go4lunch.notificationmanager;

import android.content.Context;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class FCMTokenManager {
    private static final String TAG = "FCMTokenManager";

    public interface TokenRefreshListener {
        void onTokenRefreshed(String token);
    }

    public static void initialize(Context context) {
        //Instance de firebase
        FirebaseApp.initializeApp(context);
    }

    public static void retrieveFCMToken(Context context, TokenRefreshListener listener) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        Log.d(TAG, "FCM Token: " + token);
                        if (listener != null) {
                            listener.onTokenRefreshed(token);
                        }
                    } else {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    }
                });
    }
}