package com.example.manish.wpaper.pushNotif;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.manish.wpaper.activity.MainActivity;
import com.example.manish.wpaper.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessasingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage == null)
            return;

        if(remoteMessage.getNotification() !=null){
            handleNotification(remoteMessage.getNotification().getBody());
        }

    }

    private void handleNotification(String body) {

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this);
        notifBuilder.setContentTitle("Push Notification Test");
        notifBuilder.setContentText(body);
        notifBuilder.setAutoCancel(true);
        notifBuilder.setSmallIcon(R.drawable.wpaper);
        notifBuilder.setContentIntent(pi);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notifBuilder.build());

    }
}
