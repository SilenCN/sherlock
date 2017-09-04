package com.wocao.sherlock.DataBaseOperate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by silen on 16-10-11.
 */

public class SqliteManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "Sherlock.db";
    private static final int DB_VERSION = 2;

    public SqliteManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE AlarmList(alarmID INTEGER PRIMARY KEY,isTurnOn BOOLEAN,singelWay BOOLEAN,startTime INTEGER,endTime INTEGER,Monday BOOLEAN,Tuesday BOOLEAN,Wednesday BOOLEAN,Thursday BOOLEAN,Friday BOOLEAN,Saturday BOOLEAN,Sunday BOOLEAN,Mode INTEGER)");
        
        db.execSQL("CREATE TABLE UNLOCK_HISTORY(id INTEGER PRIMARY KEY,time BIGINT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i=oldVersion;i<newVersion;i++)
        switch (i) {
            case 1:
                db.execSQL("CREATE TABLE UNLOCK_HISTORY(id INTEGER PRIMARY KEY,time BIGINT)");
                break;
        }
    }
}
