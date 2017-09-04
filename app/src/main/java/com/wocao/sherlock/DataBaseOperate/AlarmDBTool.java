package com.wocao.sherlock.DataBaseOperate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 10397 on 2016/1/2.
 */
public class AlarmDBTool {
    private Context context;
    private String dbName = "Sherlock.db";
    SQLiteDatabase dbObject;

    public AlarmDBTool(Context context) {
        this.context = context;
        dbObject = new SqlObject(context).getSqLiteDatabase();
       // createTable();
    }

    public void createTable() {
        try {
            dbObject.rawQuery("select alarmID from AlarmList", null).close();
        } catch (Exception e) {
        }
    }

    public void insertAlarm(boolean isTurnOn, int startTime, int endTime, boolean Monday, boolean Tuesday, boolean Wednesday, boolean Thursday, boolean Friday, boolean Saturday, boolean Sunday, int mode) {
        ContentValues values = new ContentValues();
        values.put("isTurnOn", isTurnOn);
        values.put("startTime", startTime);
        values.put("endTime", endTime);
        values.put("Monday", Monday);
        values.put("Thursday", Thursday);
        values.put("Wednesday", Wednesday);
        values.put("Tuesday", Tuesday);
        values.put("Friday", Friday);
        values.put("Saturday", Saturday);
        values.put("Sunday", Sunday);
        values.put("Mode", mode);
        dbObject.insert("AlarmList", null, values);
    }

    public List<Map<String, Object>> getAllAlarm() {
        List<Map<String, Object>> alarmList = new ArrayList<>();
        Cursor cursor = dbObject.rawQuery("select alarmID,isTurnOn,startTime,endTime,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday,Mode from AlarmList", null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            Map<String, Object> values = new HashMap<>();
            values.put("alarmID", cursor.getInt(cursor.getColumnIndex("alarmID")));
            values.put("isTurnOn", cursor.getInt(cursor.getColumnIndex("isTurnOn")) == 1 ? true : false);
            values.put("startTime", cursor.getInt(cursor.getColumnIndex("startTime")));
            values.put("endTime", cursor.getInt(cursor.getColumnIndex("endTime")));
            values.put("Monday", cursor.getInt(cursor.getColumnIndex("Monday")) == 1 ? true : false);
            values.put("Thursday", cursor.getInt(cursor.getColumnIndex("Thursday")) == 1 ? true : false);
            values.put("Wednesday", cursor.getInt(cursor.getColumnIndex("Wednesday")) == 1 ? true : false);
            values.put("Tuesday", cursor.getInt(cursor.getColumnIndex("Tuesday")) == 1 ? true : false);
            values.put("Friday", cursor.getInt(cursor.getColumnIndex("Friday")) == 1 ? true : false);
            values.put("Saturday", cursor.getInt(cursor.getColumnIndex("Saturday")) == 1 ? true : false);

            values.put("Sunday", cursor.getInt(cursor.getColumnIndex("Sunday")) == 1 ? true : false);

            values.put("Mode", cursor.getInt(cursor.getColumnIndex("Mode")));
            alarmList.add(values);
        }
        cursor.close();

        return alarmList;
    }

    public List<Map<String, Object>> getRegisterAlarmList() {
        String dayOfWeek = null;
        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case 1:
                dayOfWeek = "Sunday";
                break;
            case 2:
                dayOfWeek = "Monday";
                break;
            case 3:
                dayOfWeek = "Tuesday";
                break;
            case 4:
                dayOfWeek = "Wednesday";
                break;
            case 5:
                dayOfWeek = "Thursday";
                break;
            case 6:
                dayOfWeek = "Friday";
                break;
            case 7:
                dayOfWeek = "Saturday";
                break;
        }

        String yesterdayOfWeek = null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, (calendar.get(Calendar.DAY_OF_MONTH)) - 1);
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                yesterdayOfWeek = "Sunday";
                break;
            case 2:
                yesterdayOfWeek = "Monday";
                break;
            case 3:
                yesterdayOfWeek = "Tuesday";
                break;
            case 4:
                yesterdayOfWeek = "Wednesday";
                break;
            case 5:
                yesterdayOfWeek = "Thursday";
                break;
            case 6:
                yesterdayOfWeek = "Friday";
                break;
            case 7:
                yesterdayOfWeek = "Saturday";
                break;
        }


        List<Map<String, Object>> alarmList = new ArrayList<>();
        Cursor cursor = dbObject.rawQuery("select alarmID,isTurnOn,startTime,endTime," + dayOfWeek +","+yesterdayOfWeek+""+ ",Mode  from AlarmList", null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex("isTurnOn")) == 0) {
                continue;
            }
            Map<String, Object> values = new HashMap<>();
            values.put("alarmID", cursor.getInt(cursor.getColumnIndex("alarmID")));
            values.put("startTime", cursor.getInt(cursor.getColumnIndex("startTime")));
            values.put("endTime", cursor.getInt(cursor.getColumnIndex("endTime")));
            values.put("dayIsTurnOn", cursor.getInt(cursor.getColumnIndex(dayOfWeek)) == 1 ? true : false);
            calendar = Calendar.getInstance();
            int time = calendar.get(Calendar.HOUR) * 100 + calendar.get(Calendar.MINUTE);
            if ((int) values.get("endTime") - (int) values.get("startTime") < 0) {
                if (time < (int) values.get("endTime")) {
                    values.put("dayIsTurnOn", cursor.getInt(cursor.getColumnIndex(yesterdayOfWeek)) == 1 ? true : false);
                }
            }

            values.put("Mode", cursor.getInt(cursor.getColumnIndex("Mode")));
            alarmList.add(values);
        }
        cursor.close();
        return alarmList;
    }

    public void updataAlarm(int alarmID, boolean isTurnOn) {
        ContentValues values = new ContentValues();
        values.put("isTurnOn", isTurnOn);
        dbObject.update("AlarmList", values, "alarmID=" + alarmID, null);
    }

    public void updataAlarm(int alarmID, boolean isTurnOn, int startTime, int endTime, boolean Monday, boolean Tuesday, boolean Wednesday, boolean Thursday, boolean Friday, boolean Saturday, boolean Sunday, int mode) {

        ContentValues values = new ContentValues();
        values.put("isTurnOn", isTurnOn);
        values.put("startTime", startTime);
        values.put("endTime", endTime);
        values.put("Monday", Monday);
        values.put("Thursday", Thursday);
        values.put("Wednesday", Wednesday);
        values.put("Tuesday", Tuesday);
        values.put("Friday", Friday);
        values.put("Saturday", Saturday);
        values.put("Sunday", Sunday);
        values.put("Mode", mode);

        dbObject.update("AlarmList", values, "alarmID=" + alarmID, null);
    }

    public void deleteAlarm(int alarmID) {
        dbObject.delete("AlarmList", "alarmID=" + alarmID, null);
    }

    public Map<String, Object> quaryAlarm(int alarmID) {
        Cursor cursor = dbObject.query("AlarmList", null, "alarmID=" + alarmID, null, null, null, null);
        cursor.moveToLast();
        Map<String, Object> values = new HashMap<>();
        values.put("alarmID", cursor.getInt(cursor.getColumnIndex("alarmID")));
        values.put("isTurnOn", cursor.getInt(cursor.getColumnIndex("isTurnOn")) == 1 ? true : false);
        values.put("startTime", cursor.getInt(cursor.getColumnIndex("startTime")));
        values.put("endTime", cursor.getInt(cursor.getColumnIndex("endTime")));
        values.put("Monday", cursor.getInt(cursor.getColumnIndex("Monday")) == 1 ? true : false);
        values.put("Thursday", cursor.getInt(cursor.getColumnIndex("Thursday")) == 1 ? true : false);
        values.put("Wednesday", cursor.getInt(cursor.getColumnIndex("Wednesday")) == 1 ? true : false);
        values.put("Tuesday", cursor.getInt(cursor.getColumnIndex("Tuesday")) == 1 ? true : false);
        values.put("Friday", cursor.getInt(cursor.getColumnIndex("Friday")) == 1 ? true : false);
        values.put("Saturday", cursor.getInt(cursor.getColumnIndex("Saturday")) == 1 ? true : false);
        values.put("Sunday", cursor.getInt(cursor.getColumnIndex("Sunday")) == 1 ? true : false);
        values.put("Mode", cursor.getInt(cursor.getColumnIndex("Mode")));
        cursor.close();
        return values;
    }

    public Map<String, Object> quaryAlarmForWeek(int alarmID) {

        String dayOfWeek = null;
        switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            case 1:
                dayOfWeek = "Sunday";
                break;
            case 2:
                dayOfWeek = "Monday";
                break;
            case 3:
                dayOfWeek = "Tuesday";
                break;
            case 4:
                dayOfWeek = "Wednesday";
                break;
            case 5:
                dayOfWeek = "Thursday";
                break;
            case 6:
                dayOfWeek = "Friday";
                break;
            case 7:
                dayOfWeek = "Saturday";
                break;
        }
        String yesterdayOfWeek = null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, (calendar.get(Calendar.DAY_OF_MONTH)) - 1);
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                yesterdayOfWeek = "Sunday";
                break;
            case 2:
                yesterdayOfWeek = "Monday";
                break;
            case 3:
                yesterdayOfWeek = "Tuesday";
                break;
            case 4:
                yesterdayOfWeek = "Wednesday";
                break;
            case 5:
                yesterdayOfWeek = "Thursday";
                break;
            case 6:
                yesterdayOfWeek = "Friday";
                break;
            case 7:
                yesterdayOfWeek = "Saturday";
                break;
        }

        Log.i("AlarmID",alarmID+"");
        Cursor cursor = dbObject.query("AlarmList", null, "alarmID=" + alarmID, null, null, null, null);
        cursor.moveToLast();
        Map<String, Object> values = new HashMap<>();
        values.put("alarmID", cursor.getInt(cursor.getColumnIndex("alarmID")));
        values.put("startTime", cursor.getInt(cursor.getColumnIndex("startTime")));
        values.put("endTime", cursor.getInt(cursor.getColumnIndex("endTime")));
        values.put("dayIsTurnOn", cursor.getInt(cursor.getColumnIndex(dayOfWeek)) == 1 ? true : false);
        calendar = Calendar.getInstance();
        int time = calendar.get(Calendar.HOUR) * 100 + calendar.get(Calendar.MINUTE);
        if ((int) values.get("endTime") - (int) values.get("startTime") < 0) {
            if (time < (int) values.get("endTime")) {
                values.put("dayIsTurnOn", cursor.getInt(cursor.getColumnIndex(yesterdayOfWeek)) == 1 ? true : false);
            }
        }

        values.put("Mode", cursor.getInt(cursor.getColumnIndex("Mode")));
        cursor.close();
        return values;
    }

    public void updateAlarmIfModeDelete(int deleteModeId) {
        dbObject.execSQL("update AlarmList set Mode=0 where Mode=" + deleteModeId);
    }

    public void deleteDB() {
        context.deleteDatabase(dbName);
    }

    public void closeDB() {
        dbObject.close();
        dbObject = null;
    }
}
