package com.wocao.sherlock.Alarm;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.wocao.sherlock.AppConfig;
import com.wocao.sherlock.CoreService.CoreService;
import com.wocao.sherlock.DataBaseOperate.AlarmDBTool;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 10397 on 2016/1/6.
 */
public class AlarmStartService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sp;
        sp = getSharedPreferences("data", MODE_PRIVATE);
        Map<String, Object> map = new HashMap<>();
        AlarmDBTool alarmDBTool = new AlarmDBTool(AlarmStartService.this);
        map = alarmDBTool.quaryAlarmForWeek(AppConfig.AlarmIDIntent);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minu = calendar.get(Calendar.MINUTE);

        if (hour * 100 + minu < (int) map.get("endTime") && hour * 100 + minu >= (int) map.get("startTime") && (boolean) map.get("dayIsTurnOn")) {

            long duration = (((int) map.get("endTime") / 100 - hour) * 60 + (int) map.get("endTime") % 100 - minu) * 60 * 1000;

            if (sp.getLong("date", 0) < duration + System.currentTimeMillis()) {
                sp.edit().putLong("date", duration + System.currentTimeMillis()).commit();
                sp.edit().putInt("LockModeId",(int)map.get("Mode")).commit();
                startService(new Intent(AlarmStartService.this, CoreService.class));
            }
        } else if ((int) map.get("startTime") - (int) map.get("endTime") >= 0) {
            if (hour * 100 + minu <= 2400 && hour * 100 + minu >= (int) map.get("startTime") && (boolean) map.get("dayIsTurnOn")) {
                long duration = (((int) map.get("endTime") / 100 - hour + 24) * 60 + (int) map.get("endTime") % 100 - minu) * 60 * 1000;

                if (sp.getLong("date", 0) < duration + System.currentTimeMillis()) {
                    sp.edit().putLong("date", duration + System.currentTimeMillis()).commit();
                    sp.edit().putInt("LockModeId",(int)map.get("Mode")).commit();
                    startService(new Intent(AlarmStartService.this, CoreService.class));
                }
            } else if (hour * 100 + minu < (int) map.get("endTime") && hour * 100 + minu <= (int) map.get("startTime") && (boolean) map.get("dayIsTurnOn")) {
                long duration = (((int) map.get("endTime") / 100 - hour) * 60 + (int) map.get("endTime") % 100 - minu) * 60 * 1000;
                if (sp.getLong("date", 0) < duration + System.currentTimeMillis()) {
                    sp.edit().putLong("date", duration + System.currentTimeMillis()).commit();
                    sp.edit().putInt("LockModeId",(int)map.get("Mode")).commit();
                    startService(new Intent(AlarmStartService.this, CoreService.class));
                }
            }
        }
        stopSelf();
    }

 /*   @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        System.out.println("---------服务  restart---------");
        return 0;
    }*/
}
