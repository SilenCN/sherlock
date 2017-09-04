package com.wocao.sherlock.Alarm.Notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by silen on 17-4-19.
 */

public class AlarmNotificationAutoStartService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (AlarmNotificationOperate.isTurnOn(this)){
            AlarmNotificationOperate.operateAllNotifition(this,false);
        }
        stopSelf();
    }
}
