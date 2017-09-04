package com.wocao.sherlock.Alarm.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.wocao.sherlock.Alarm.AlarmListActivity;
import com.wocao.sherlock.AppConfig;
import com.wocao.sherlock.DataBaseOperate.AlarmDBTool;
import com.wocao.sherlock.DataBaseOperate.ModeListDBTool;
import com.wocao.sherlock.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by silen on 17-4-19.
 */

public class AlarmNotificationStartService extends Service {

    Map<String, Object> map = new HashMap<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AlarmDBTool alarmDBTool = new AlarmDBTool(this);
        map = alarmDBTool.quaryAlarmForWeek(AppConfig.AlarmNotificationID);

        Calendar calendar = Calendar.getInstance();
        //  calendar.set(Calendar.MINUTE, (calendar.get(Calendar.MINUTE)) + 10);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minu = calendar.get(Calendar.MINUTE);
        // Log.i("AlarmNotificationStartService","收到");
        //  Toast.makeText(this, "Hour"+hour+"/Minu"+minu, Toast.LENGTH_LONG).show();

        int time = hour * 100 + minu;
        if (time < (int) map.get("startTime") && (boolean) map.get("dayIsTurnOn")) {
            showNotification();
        }
        /*
        if (hour * 100 + minu < (int) map.get("endTime") && hour * 100 + minu >= (int) map.get("startTime") && (boolean) map.get("dayIsTurnOn")) {
            showNotification();
        } else if ((int) map.get("startTime") - (int) map.get("endTime") >= 0) {
            if (hour * 100 + minu < 2400 && hour * 100 + minu >= (int) map.get("startTime") && (boolean) map.get("dayIsTurnOn")) {
                showNotification();
            } else if (hour * 100 + minu < (int) map.get("endTime") && hour * 100 + minu <= (int) map.get("startTime") && (boolean) map.get("dayIsTurnOn")) {
                showNotification();
            }
        }*/
        stopSelf();
    }

    private void showNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, AlarmListActivity.class), 0);
        int startTime = (int) map.get("startTime");
        int endTime = (int) map.get("endTime");
        ModeListDBTool modeListDBTool = new ModeListDBTool(this);
        android.support.v4.app.NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("有定时任务将开启")
                .setContentText((startTime / 100) + ":" + startTime % 100 + "到" + (endTime < startTime ? "次日" : "") + endTime / 100 + ":" + endTime % 100 + "  " + "" + modeListDBTool.getModeNameById((int) map.get("Mode")))
                .setContentIntent(contentIntent)
                .setAutoCancel(true);
        notificationManager.notify(AppConfig.AlarmIDIntent, builder.build());
    }
}
