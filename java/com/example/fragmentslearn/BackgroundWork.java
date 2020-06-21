package com.example.fragmentslearn;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


public class BackgroundWork extends Worker {
    private static final String TAG = "BackgroundWork";
    public static DataParser.OnDataAvailable context;
    public static Context context1;

    public BackgroundWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @NonNull
    @Override
    public Result doWork() {
        NotificationCompat.Builder alertNotify = new NotificationCompat.Builder(context1,"101")
                .setContentTitle("Test")
                .setContentText("Test notif")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.virus)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setAutoCancel(true); //removes notification when user clicks on it

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context1);
        notificationManager.notify(28,alertNotify.build());
        DataParser download = new DataParser(context);
        download.execute("https://api.covid19api.com/summary");
        return Result.success();
    }
}
