package com.wocao.sherlock.ForceUnlock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.wocao.sherlock.AppConfig;
import com.wocao.sherlock.CoreService.CoreService;
import com.wocao.sherlock.Setting.SettingUtils;

import java.util.Timer;
import java.util.TimerTask;


public class forceStopService extends Service {

    SharedPreferences sp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sp = getSharedPreferences("data", MODE_PRIVATE);
        int dateDelta = intent.getIntExtra("date", 0);
        int type = intent.getIntExtra("type", 0);
        if (sp.getLong("date", System.currentTimeMillis()) > dateDelta + System.currentTimeMillis()) {
            if (SettingUtils.getBooleanValue(forceStopService.this, "setting_better_startAfterUnlock", false) && type == 0) {
                if (SettingUtils.getBooleanValue(forceStopService.this, "setting_better_usingTimerToRestart", false)) {
                    Timer timer=new Timer();
                    TimerTask task=new TimerTask() {
                        @Override
                        public void run() {
                            startService(new Intent(forceStopService.this,CoreService.class));
                        }
                    };
                    timer.schedule(task,10*60*1000);
                } else {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent alarmIntent = new Intent("Sherlock_Alarm_Action").putExtra("alarmID", 65535);
                    PendingIntent pi = PendingIntent.getBroadcast(forceStopService.this, 65535, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pi);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AppConfig.START_AFTER_UNLOCK_DURATION, pi);
                    stopService(new Intent(forceStopService.this, CoreService.class));
                }
            } else {
                sp.edit().putLong("date", dateDelta + System.currentTimeMillis()).commit();
                stopService(new Intent(forceStopService.this, CoreService.class));
                startService(new Intent(forceStopService.this, CoreService.class));
            }

        }
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
}
