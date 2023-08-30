package com.example.go4lunch;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.go4lunch.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MyFirebaseMessagingServices extends FirebaseMessagingService {
    private static final String CANAL = "MyNotifCanal";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        String myMessage = Objects.requireNonNull(message.getNotification()).getBody();
        Log.d("FirebaseMessage", "Vous avez reçu un message"+ myMessage);

        //Créer une notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CANAL);
        notificationBuilder.setContentTitle("My notif");
        notificationBuilder.setContentText(myMessage);
        notificationBuilder.setSmallIcon(R.drawable.ic_baseline_alarm_24);

        //envoyer la notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //on vérifie que la version du SDK est ok et on crée un channel
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){

            String channelId = getString(R.string.notification_channel_id);
            String channelTitle= getString(R.string.notification_channel_title);
            String channelDescription= getString(R.string.notification_channel_description);
            NotificationChannel channel = new NotificationChannel(channelId, channelTitle, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
        }
        notificationManager.notify(1, notificationBuilder.build());
    }
}
