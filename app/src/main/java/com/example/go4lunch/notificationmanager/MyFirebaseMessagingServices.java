package com.example.go4lunch.notificationmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.go4lunch.R;
import com.example.go4lunch.ui.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

public class MyFirebaseMessagingServices extends FirebaseMessagingService {

    private static final String CANAL = "MyNotifCanal";

    private String userName;
    private String restaurantNameFromKey;
    private String restaurantIdFromKey;
    private String workmateNamesFromKey;
    private String userIdFromKey;
    private String restaurantAddress;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        // Step 1: Retrieve the currently logged-in Firebase user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        restaurantNameFromKey = sharedPreferences.getString("restaurant_name", "");
        restaurantIdFromKey = sharedPreferences.getString("restaurant_Id", "");
        restaurantAddress = sharedPreferences.getString("restaurant_address", "");
        userIdFromKey = sharedPreferences.getString("user_id", "");
        workmateNamesFromKey = sharedPreferences.getString("workmate_names", "");
        if (!restaurantIdFromKey.isEmpty()) {

            // Step 2: Check if a user is logged in
            if (currentUser != null && userIdFromKey.equals(currentUser.getUid())) {
                // You have the currently logged-in user here (currentUser)
                // You can access their ID, name, email, etc.
                userName = currentUser.getDisplayName();

                // Step 3: Display the notification with the selected restaurant
                String myNotif = Objects.requireNonNull(message.getNotification()).getBody();
                Log.d("FirebaseMessage", "You received a message: " + myNotif);

                // Optional Step 4: Handle notification click to open restaurant details activity
                // Create an intent to open MainActivity with the restaurant ID
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("restaurantId", restaurantIdFromKey);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // Create a BigTextStyle notification
                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                bigTextStyle.bigText(myNotif +
                        getString(R.string.notification_message_1) + userName +
                        getString(R.string.notification_message_2) + restaurantNameFromKey + ", " + restaurantAddress +
                        getString(R.string.notification_message_3) + workmateNamesFromKey);



                // Create the notification builder and set the expandable style
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CANAL);
                notificationBuilder.setContentTitle(getString(R.string.notification_message_title));
                notificationBuilder.setContentText(myNotif); // Text when notification is not expanded
                notificationBuilder.setStyle(bigTextStyle); // Set the expandable style
                notificationBuilder.setSmallIcon(R.drawable.ic_baseline_alarm_24);
                notificationBuilder.setContentIntent(pendingIntent); // Set the intent when notification is clicked
                notificationBuilder.setAutoCancel(true); // Close the notification when clicked

                // Send the notification
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // Check if the SDK version is supported and create a notification channel
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelId = getString(R.string.notification_channel_id);
                    String channelTitle = getString(R.string.notification_channel_title);
                    String channelDescription = getString(R.string.notification_channel_description);
                    NotificationChannel channel = new NotificationChannel(channelId, channelTitle, NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription(channelDescription);
                    notificationManager.createNotificationChannel(channel);
                    notificationBuilder.setChannelId(channelId);
                }

                notificationManager.notify(1, notificationBuilder.build());


            } else {
                // No user is logged in, handle accordingly
            }
        } else {
            Log.d("FirebaseMessage", "No restaurant selected, notification not shown.");

        }
    }
}
