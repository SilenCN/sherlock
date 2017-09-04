package com.wocao.sherlock.Alarm.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.wocao.sherlock.AppConfig;

/**
 * Created by silen on 17-4-19.
 */

public class AlarmNotificationActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AppConfig.AlarmNotificationID = intent.getIntExtra("alarmID", 0);
       // Toast.makeText(context, ""+AppConfig.AlarmNotificationID, Toast.LENGTH_SHORT).show();
        context.startService(new Intent(context,AlarmNotificationStartService.class));
    }
}
