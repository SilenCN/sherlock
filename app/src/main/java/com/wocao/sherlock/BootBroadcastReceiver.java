package com.wocao.sherlock;

import android.content.*;

import com.wocao.sherlock.Alarm.AlarmAutoStartCheck;
import com.wocao.sherlock.Alarm.Notification.AlarmNotificationAutoStartService;
import com.wocao.sherlock.CoreService.CoreService;

public class BootBroadcastReceiver extends BroadcastReceiver {
    String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {


        try {
            if (intent.getAction().equals(action_boot)) {
                Intent ootStartIntent = new Intent(context, CoreService.class);
                context.startService(ootStartIntent);
                AppConfig.haveBootStart = true;
            } else if (!AppConfig.haveBootStart) {
                Intent ootStartIntent = new Intent(context, CoreService.class);
                context.startService(ootStartIntent);
                AppConfig.haveBootStart = true;
            }

            if (AppConfig.alarmRestartToken) {
                AppConfig.alarmRestartToken = false;
                context.startService(new Intent(context, AlarmAutoStartCheck.class));
                context.startService(new Intent(context, AlarmNotificationAutoStartService.class));
            }

        }catch (Exception e){

        }
    }
}
