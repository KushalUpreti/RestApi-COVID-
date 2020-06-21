package com.example.fragmentslearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Test extends AppCompatActivity {

    public void notif(){
        NotificationCompat.Builder alertNotify = new NotificationCompat.Builder(this,getString(R.string.channel1_id))
                .setContentTitle("Test Notif")
                .setContentText("Working fine")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.virus)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setAutoCancel(true); //removes notification when user clicks on it

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(28,alertNotify.build());
    }
}
