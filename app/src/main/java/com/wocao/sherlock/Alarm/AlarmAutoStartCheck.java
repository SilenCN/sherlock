package com.wocao.sherlock.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.wocao.sherlock.CoreService.CoreService;
import com.wocao.sherlock.DataBaseOperate.AlarmDBTool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by 10397 on 2016/1/4.
 */
public class AlarmAutoStartCheck extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sp;
        sp = getSharedPreferences("data", MODE_PRIVATE);
        List<Map<String,Object>> alarmList=new ArrayList<>();
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        AlarmDBTool alarmDBTool=new AlarmDBTool(AlarmAutoStartCheck.this);
        alarmList=alarmDBTool.getRegisterAlarmList();

        Calendar calendar=Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minu=calendar.get(Calendar.MINUTE);
        for (Map<String,Object> map:alarmList){
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY,(int)map.get("startTime")/100);
            calendar.set(Calendar.MINUTE,(int)map.get("startTime")%100);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Intent intent=new Intent("Sherlock_Alarm_Action").putExtra("alarmID",(int)map.get("alarmID"));
            PendingIntent pi=PendingIntent.getBroadcast(AlarmAutoStartCheck.this,(int)map.get("alarmID"),intent,PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pi);
            alarmManager.setRepeating(AlarmManager.RTC,calendar.getTimeInMillis(),1000*60*60*24,pi);

            if (hour * 100 + minu < (int) map.get("endTime") && hour * 100 + minu >= (int) map.get("startTime") && (boolean) map.get("dayIsTurnOn")) {

                long duration = (((int) map.get("endTime") / 100 - hour) * 60 + (int) map.get("endTime") % 100 - minu) * 60 * 1000;

                if (sp.getLong("date", 0) < duration + System.currentTimeMillis()) {
                    sp.edit().putLong("date", duration + System.currentTimeMillis()).commit();
                    sp.edit().putInt("LockModeId",(int)map.get("Mode")).commit();
                    startService(new Intent(AlarmAutoStartCheck.this, CoreService.class));
                }
            } else if ((int) map.get("startTime") - (int) map.get("endTime") >= 0) {
                if (hour * 100 + minu < 2400 && hour * 100 + minu >= (int) map.get("startTime") && (boolean) map.get("dayIsTurnOn")) {
                    long duration = (((int) map.get("endTime") / 100 - hour + 24) * 60 + (int) map.get("endTime") % 100 - minu) * 60 * 1000;

                    if (sp.getLong("date", 0) < duration + System.currentTimeMillis()) {
                        sp.edit().putLong("date", duration + System.currentTimeMillis()).commit();
                        sp.edit().putInt("LockModeId",(int)map.get("Mode")).commit();
                        startService(new Intent(AlarmAutoStartCheck.this, CoreService.class));
                    }
                } else if (hour * 100 + minu < (int) map.get("endTime") && hour * 100 + minu <= (int) map.get("startTime") && (boolean) map.get("dayIsTurnOn")) {
                    long duration = (((int) map.get("endTime") / 100 - hour) * 60 + (int) map.get("endTime") % 100 - minu) * 60 * 1000;
                    if (sp.getLong("date", 0) < duration + System.currentTimeMillis()) {
                        sp.edit().putLong("date", duration + System.currentTimeMillis()).commit();
                        sp.edit().putInt("LockModeId",(int)map.get("Mode")).commit();
                        startService(new Intent(AlarmAutoStartCheck.this, CoreService.class));
                    }
                }
            }
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
