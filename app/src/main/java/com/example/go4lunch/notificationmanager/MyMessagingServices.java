package com.example.go4lunch.notificationmanager;

import static androidx.core.app.NotificationCompat.PRIORITY_DEFAULT;

import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.go4lunch.R;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyMessagingServices extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "Go4Lunch Notification ID";
    private static final String CHANNEL_NAME = "Go4Lunch Notification";
    private static final String CHANNEL_DESCRIPTION = "Go4Lunch Description";

    private void displayNotification(){
        //Créer une notification
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setContentTitle("Titre")
                            .setContentText("myMessage")
                            .setSmallIcon(R.drawable.ic_baseline_alarm_24)
                            .setPriority(PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(1, notificationBuilder.build());
        }
    }


}
