package com.wocao.sherlock.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wocao.sherlock.Alarm.Notification.AlarmNotificationOperate;
import com.wocao.sherlock.ControlByOther.ControlType;
import com.wocao.sherlock.ControlByOther.Presenter.ControlByOtherDialogManager;
import com.wocao.sherlock.DataBaseOperate.AlarmDBTool;
import com.wocao.sherlock.DataBaseOperate.ModeListDBTool;
import com.wocao.sherlock.R;
import com.wocao.sherlock.MaterialDesign.StatusBarUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * +
 * Created by 10397 on 2016/1/2.
 * It was used for displaying the alarm Activity
 */
public class AlarmListActivity extends AppCompatActivity {

    Toolbar mToolBar;
    ListView listView;
    FloatingActionButton addAlarmButton;

    MyAdapter myAdapter;
    List<Map<String, Object>> listviewList = new ArrayList<Map<String, Object>>();

    AlarmDBTool alarmDBTool;

    AlarmManager alarmManager;
    ModeListDBTool modeListDBTool;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list);

        new StatusBarUtils().setStatusBar(this);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("定时任务");
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        } catch (Exception e) {
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmDBTool = new AlarmDBTool(AlarmListActivity.this);
        modeListDBTool = new ModeListDBTool(AlarmListActivity.this);
        listviewList = alarmDBTool.getAllAlarm();
        if (0 < listviewList.size()) {
            ((TextView) findViewById(R.id.alarmListNoItemShowTv)).setVisibility(View.GONE);
        }

        for (Map<String, Object> map : listviewList) {
           // Log.i("AlarmID_Has SEtting",(int) map.get("alarmID")+"");
            Intent intent = new Intent("Sherlock_Alarm_Action").putExtra("alarmID", (int) map.get("alarmID"));
            PendingIntent pi = PendingIntent.getBroadcast(AlarmListActivity.this, (int) map.get("alarmID"), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pi);

            if ((boolean) map.get("isTurnOn")) {
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, (int) map.get("startTime") / 100);
                calendar.set(Calendar.MINUTE, (int) map.get("startTime") % 100);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pi);

            }
        }

        if (AlarmNotificationOperate.isTurnOn(this)) {
            AlarmNotificationOperate.operateAllNotifition(this, false);
        }

        listView = (ListView) findViewById(R.id.alarmlistListview);
        listView.setDivider(null);
        addAlarmButton = (FloatingActionButton) findViewById(R.id.alarmListfloatActionButtonAdd);
        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AlarmListActivity.this, AlarmSettingActivity.class).putExtra("ActionAddAlarm", true));
                finish();
            }
        });

        myAdapter = new MyAdapter(AlarmListActivity.this);
        listView.setAdapter(myAdapter);


    }


    public final class ViewHolder {
        public TextView TimeDurationTV;
        public TextView DayOfAvaible;
        public SwitchCompat TurnOffSwitch;
        public RelativeLayout contain;
        public TextView NextDayTV;
        public TextView ModeTV;

    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listviewList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.alarm_list_item, null);
                holder.TimeDurationTV = (TextView) convertView.findViewById(R.id.ShortcutItemLabelTV);
                holder.DayOfAvaible = (TextView) convertView.findViewById(R.id.ShortcutItemTimeTV);
                holder.TurnOffSwitch = (SwitchCompat) convertView.findViewById(R.id.alarmListItemSwitch);
                holder.contain = (RelativeLayout) convertView.findViewById(R.id.alarmListItemContain);
                holder.NextDayTV = (TextView) convertView.findViewById(R.id.alarmlistitemNextDayTV);
                holder.ModeTV = (TextView) convertView.findViewById(R.id.alarmListItemModeTV);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.ModeTV.setText(modeListDBTool.quaryModeListDataById((int) listviewList.get(position).get("Mode")).getName());
            holder.TurnOffSwitch.setChecked((boolean) listviewList.get(position).get("isTurnOn"));
            holder.TimeDurationTV.setText(formatTime((int) listviewList.get(position).get("startTime")) + "--" + formatTime((int) listviewList.get(position).get("endTime")));
            holder.DayOfAvaible.setText(formatDayOfWeek(listviewList.get(position)));

            if ((int) listviewList.get(position).get("startTime") - (int) listviewList.get(position).get("endTime") >= 0) {
                holder.NextDayTV.setVisibility(View.VISIBLE);
            } else {
                holder.NextDayTV.setVisibility(View.INVISIBLE);
            }

            final ViewHolder finalHolder = holder;

            finalHolder.contain.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ControlByOtherDialogManager manager = new ControlByOtherDialogManager(AlarmListActivity.this);
                    manager.setOnSuccessListener(new ControlByOtherDialogManager.OnSuccessListener() {
                        @Override
                        public void success() {
                            startActivity(new Intent(AlarmListActivity.this, AlarmSettingActivity.class).putExtra("ActionAlarmModify", (int) listviewList.get(position).get("alarmID")));
                            finish();
                        }
                    });
                    manager.test(getSupportFragmentManager(), ControlType.TYPE_ALARM);

                }
            });
            finalHolder.TurnOffSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalHolder.TurnOffSwitch.setChecked(!finalHolder.TurnOffSwitch.isChecked());
                    ControlByOtherDialogManager manager = new ControlByOtherDialogManager(AlarmListActivity.this);
                    manager.setOnSuccessListener(new ControlByOtherDialogManager.OnSuccessListener() {
                        @Override
                        public void success() {
                            finalHolder.TurnOffSwitch.setChecked(!finalHolder.TurnOffSwitch.isChecked());
                            alarmDBTool.updataAlarm((int) listviewList.get(position).get("alarmID"), !(boolean) listviewList.get(position).get("isTurnOn"));
                            listviewList.get(position).put("isTurnOn", !(boolean) listviewList.get(position).get("isTurnOn"));

                            Intent intent = new Intent("Sherlock_Alarm_Action").putExtra("alarmID", (int) listviewList.get(position).get("alarmID"));
                            PendingIntent pi = PendingIntent.getBroadcast(AlarmListActivity.this, (int) listviewList.get(position).get("alarmID"), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                            Intent intentNotification = new Intent("Sherlock_Alarm_Notification_Action").putExtra("alarmID", (int) listviewList.get(position).get("alarmID"));
                            PendingIntent piNotification = PendingIntent.getBroadcast(AlarmListActivity.this, (int) listviewList.get(position).get("alarmID"), intentNotification, PendingIntent.FLAG_UPDATE_CURRENT);


                            alarmManager.cancel(pi);
                            if (AlarmNotificationOperate.isTurnOn(AlarmListActivity.this))
                                alarmManager.cancel(piNotification);

                            if (finalHolder.TurnOffSwitch.isChecked()) {
                                calendar.setTimeInMillis(System.currentTimeMillis());
                                calendar.set(Calendar.HOUR_OF_DAY, (int) listviewList.get(position).get("startTime") / 100);
                                calendar.set(Calendar.MINUTE, (int) listviewList.get(position).get("startTime") % 100);
                                calendar.set(Calendar.SECOND, 0);
                                calendar.set(Calendar.MILLISECOND, 0);
                                alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pi);
                                if (AlarmNotificationOperate.isTurnOn(AlarmListActivity.this)) {
                                    calendar.set(Calendar.MINUTE, (calendar.get(Calendar.MINUTE)) - 10);
                                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, piNotification);
                                }
                            }
                        }
                    });
                    manager.test(getSupportFragmentManager(), ControlType.TYPE_ALARM);

                }
            });
            return convertView;
        }
    }

    /**
     * this method is to format the time display
     *
     * @param time the integer of time
     * @return the string of time
     */
    private String formatTime(int time) {
        String returnString = "";
        int timeSuf = time / 100;
        time = time % 100;
        if (timeSuf > 9) {
            returnString = "" + timeSuf;
        } else {
            returnString = "0" + timeSuf;
        }
        if (time > 9) {
            returnString += ":" + time;
        } else {
            returnString += ":0" + time;
        }
        return returnString;
    }

    private String formatDayOfWeek(Map<String, Object> map) {
        String returnString = " ";
        boolean day[] = new boolean[7];
        day[0] = (Boolean) map.get("Monday");
        day[1] = (Boolean) map.get("Tuesday");
        day[2] = (Boolean) map.get("Wednesday");
        day[3] = (Boolean) map.get("Thursday");
        day[4] = (Boolean) map.get("Friday");
        day[5] = (Boolean) map.get("Saturday");
        day[6] = (Boolean) map.get("Sunday");
        boolean token = true;
        for (int i = 0; i < 7; i++) {
            if (!day[i]) {
                token = false;
                break;
            }
        }
        if (token) {
            returnString += " 每天";
        } else {
            if (day[6]) {
                returnString += " 周日";
            }
            if (day[0]) {
                returnString += " 周一";
            }
            if (day[1]) {
                returnString += " 周二";
            }
            if (day[2]) {
                returnString += " 周三";
            }
            if (day[3]) {
                returnString += " 周四";
            }
            if (day[4]) {
                returnString += " 周五";
            }
            if (day[5]) {
                returnString += " 周六";
            }
        }
        return returnString.substring(2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: Implement this method
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
