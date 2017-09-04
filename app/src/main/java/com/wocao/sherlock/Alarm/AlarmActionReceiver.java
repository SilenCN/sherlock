package com.wocao.sherlock.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.wocao.sherlock.AppConfig;
import com.wocao.sherlock.CoreService.CoreService;

/**
 * Created by 10397 on 2016/1/4.
 */
public class AlarmActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AppConfig.AlarmIDIntent = intent.getIntExtra("alarmID", 0);
        if (AppConfig.AlarmIDIntent != 65535) {
            context.startService(new Intent(context, AlarmStartService.class));
        }else {
            context.startService(new Intent(context,CoreService.class));
        }
    }
}
