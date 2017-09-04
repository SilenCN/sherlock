package com.wocao.sherlock.Alarm.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.wocao.sherlock.DataBaseOperate.AlarmDBTool;
import com.wocao.sherlock.Setting.SettingUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by silen on 17-4-19.
 */

public class AlarmNotificationOperate {
    public static void operateAllNotifition(Context context, boolean isCancel) {
        List<Map<String, Object>> alarmList = new ArrayList<>();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        AlarmDBTool alarmDBTool = new AlarmDBTool(context);
        alarmList = alarmDBTool.getRegisterAlarmList();

        Calendar calendar = Calendar.getInstance();
        for (Map<String, Object> map : alarmList) {
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, (int) map.get("startTime") / 100);
            calendar.set(Calendar.MINUTE, ((int) map.get("startTime") % 100) - 10);//提前10分钟
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            //Toast.makeText(context, "时间"+calendar.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("Sherlock_Alarm_Notification_Action").putExtra("alarmID", (int) map.get("alarmID"));
            PendingIntent pi = PendingIntent.getBroadcast(context, (int) map.get("alarmID"), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pi);
            if (!isCancel)
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pi);
        }
    }

    public static boolean isTurnOn(Context context) {
        return SettingUtils.getBooleanValue(context,"setting_showNotification",false);
     //   return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("setting_showNotification", false);
    }
}
