package com.genialabs.dermia;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.genialabs.dermia.Controllers.PrefManager;
import com.google.android.material.snackbar.Snackbar;

import static android.content.Context.NOTIFICATION_SERVICE;

public class CancelReceiver extends BroadcastReceiver {
    private PrefManager prefManager;
    private ComponentName receiver;
    @Override
    public void onReceive(Context context, Intent intent) {
        String id = intent.getStringExtra("cancel_id");
        prefManager = new PrefManager(context);
        if(id=="10") {
            prefManager.setWaterChecked(false);
             receiver = new ComponentName(context, AlarmReceiver.class);
        }
        else {
            prefManager.setSunChecked(false);
             receiver = new ComponentName(context, AlarmReceiver2.class);
        }
        //desactivar notificaciones
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        NotificationManager nm = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        //nm.cancel(intent.getExtras().getInt("notificationID"));
        nm.cancelAll();
    }
}
