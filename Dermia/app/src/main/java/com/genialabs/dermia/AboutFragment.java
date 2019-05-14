package com.genialabs.dermia;

import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;


public class AboutFragment extends Fragment {

    private AboutViewModel mViewModel;
    private View rootView;
    private Switch switcher,switcher2;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.about_fragment, container, false);
        //Para onCrreatedoption y itemselected
        setHasOptionsMenu(true);
        switcher = rootView.findViewById(R.id.switch_drink_notif);
        switcher2 = rootView.findViewById(R.id.switchSunscreenNotif);

        if(((MainActivity)getContext()).getPrefs().isWaterChecked())
            switcher.setChecked(true);
        if(((MainActivity)getContext()).getPrefs().isSunChecked())
            switcher2.setChecked(true);
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switcher.isChecked()){

                    ((MainActivity)getContext()).getPrefs().setWaterChecked(true);
                    //Por si las he desctivado
                    ComponentName receiver = new ComponentName(getContext(), AlarmReceiver.class);
                    PackageManager pm = getActivity().getPackageManager();
                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                            PackageManager.DONT_KILL_APP);

                    //Notification
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    Intent notificationIntent = new Intent(getContext(), AlarmReceiver.class);
                    notificationIntent.putExtra("notificationId", 1);

                    PendingIntent broadcast = PendingIntent.getBroadcast(getContext(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Calendar cal = Calendar.getInstance();
                    int MIN=60*4;
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), MIN*60*1000,broadcast);
                    Snackbar.make(rootView, "Activado", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    ((MainActivity)getContext()).getPrefs().setWaterChecked(false);
                    //desactivar notificaciones
                    ComponentName receiver = new ComponentName(getContext(), AlarmReceiver.class);
                    PackageManager pm = getActivity().getPackageManager();
                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    Snackbar.make(rootView, "Desactivado", Snackbar.LENGTH_SHORT).show();

                }
            }
        });

        switcher2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switcher2.isChecked()){
                    ((MainActivity)getContext()).getPrefs().setSunChecked(true);
                    //Por si las he desctivado
                    ComponentName receiver = new ComponentName(getContext(), AlarmReceiver2.class);
                    PackageManager pm = getActivity().getPackageManager();
                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                            PackageManager.DONT_KILL_APP);

                    //Notification
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    Intent notificationIntent = new Intent(getContext(), AlarmReceiver2.class);
                    notificationIntent.putExtra("notificationId", 2);

                    PendingIntent broadcast = PendingIntent.getBroadcast(getContext(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Calendar cal = Calendar.getInstance();
                    int MIN=60;
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), MIN*60*1000,broadcast);
                    Snackbar.make(rootView, "Activado", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    ((MainActivity)getContext()).getPrefs().setSunChecked(false);

                        ComponentName receiver = new ComponentName(getContext(), AlarmReceiver2.class);
                        PackageManager pm = getActivity().getPackageManager();
                        pm.setComponentEnabledSetting(receiver,
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP);
                    Snackbar.make(rootView, "Desactivado", Snackbar.LENGTH_SHORT).show();

                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AboutViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onResume() {
        super.onResume();
        if(((MainActivity)getContext()).getPrefs().isWaterChecked())
            switcher.setChecked(true);
        else
            switcher.setChecked(false);
        if(((MainActivity)getContext()).getPrefs().isSunChecked())
            switcher2.setChecked(true);
        else
            switcher2.setChecked(false);
    }
}
