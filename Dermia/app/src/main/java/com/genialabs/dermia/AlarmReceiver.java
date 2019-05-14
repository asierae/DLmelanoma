package com.genialabs.dermia;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.android.material.snackbar.Snackbar;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "com.genialabs.medicaible.channelId";
    String tittle,text;
    Bitmap icon;
    @Override
    public void onReceive(Context context,Intent intent) {

        int notificationId = intent.getIntExtra("notificationId", 0);

        //// a que activity ir cuando pulse en la notificacion
        Intent repeatingIntent = new Intent(context,MainActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        repeatingIntent.putExtra("id", "10");
        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,repeatingIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        //boton de cancelar notificaciones
        final Intent allCitiesIntent = new Intent(context, CancelReceiver.class);
        allCitiesIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        allCitiesIntent.putExtra("cancel_id", "10");
        allCitiesIntent.putExtra("notificationID", notificationId);
        final PendingIntent allCitiesPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                allCitiesIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Action cancelAction = new NotificationCompat.Action(
                R.drawable.ic_favorite_black_24dp,
                context.getString(R.string.disable),
                allCitiesPendingIntent);

        if(notificationId==1) {//FROM REMEMBER DRINK WATER
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.waterdrop);
            tittle = context.getString(R.string.notif_tittle_water);
            text = context.getString(R.string.notif_text_water);
        }
        else{// = 2 FROM REMEMBER PUT sunsCREEN
            icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.sunscreen2);
            tittle = context.getString(R.string.notif_tittle_sun);
            text = context.getString(R.string.notif_text_sxun);
        }
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_all_inclusive_black_24dp)
                .setLargeIcon(icon)
                .setColor(Color.rgb(150,114,114))
                .setContentTitle(tittle)
                .setContentText(text)
                .addAction(cancelAction)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "NotificationDemo",
                    IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(100,builder.build());
    }
}
