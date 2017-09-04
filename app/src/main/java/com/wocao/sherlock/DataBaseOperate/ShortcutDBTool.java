package com.wocao.sherlock.DataBaseOperate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wocao.sherlock.Shortcut.Model.ShortCut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silen on 17-3-8.
 */

public class ShortcutDBTool {
    private String tableName = "Shortcut";
    private SQLiteDatabase dbObject;
    private Context context;

    public ShortcutDBTool(Context context) {
        this.context = context;
        dbObject = new SqlObject(context).getSqLiteDatabase();
        createTable();
    }

    public void createTable() {
        try {
            dbObject.rawQuery("select id from " + tableName, null);

        } catch (Exception e) {
            dbObject.execSQL("CREATE TABLE " + tableName + "(id INTEGER,label TEXT,time BIGINT,modeId INTEGER)");
        }
    }

    public List<ShortCut> getAllShortcutData() {
        Cursor cursor = dbObject.query(tableName, null, null, null, null, null, null);
        List<ShortCut> list = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            ShortCut shortCut = new ShortCut();
            shortCut.setId(cursor.getInt(cursor.getColumnIndex("id")));
            shortCut.setLabel(cursor.getString(cursor.getColumnIndex("label")));
            shortCut.setTime(cursor.getLong(cursor.getColumnIndex("time")));
            shortCut.setModeId(cursor.getInt(cursor.getColumnIndex("modeId")));
            list.add(shortCut);
        }
        Log.i("ListDataLength", list.size() + "");
        cursor.close();
        return list;
    }

    public void deleteShortcut(int id) {
        dbObject.execSQL("DELETE FROM " + tableName + " WHERE id=" + id);
    }

    public void updateOrCreateShortcut(ShortCut shortCut) {

        Cursor cursor;
        try {
            cursor = dbObject.query(tableName, null, "id=" + shortCut.getId(), null, null, null, null);
            if (!(cursor != null && cursor.moveToFirst())) {
                insertShortcut(shortCut);
                return;
            }

        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        ContentValues values = new ContentValues();
        values.put("label", shortCut.getLabel());
        values.put("time", shortCut.getTime());
        values.put("modeId", shortCut.getModeId());
        dbObject.update(tableName, values, "id=" + shortCut.getId(), null);
    }


    private void insertShortcut(ShortCut shortCut) {
        ContentValues values = new ContentValues();
        values.put("id",shortCut.getId());
        values.put("label", shortCut.getLabel());
        values.put("time", shortCut.getTime());
        values.put("modeId", shortCut.getModeId());
        dbObject.insert(tableName, null, values);
    }

    /*
        public LockMode quaryModeListDataById(int id) {
            Cursor cursor = dbObject.query("ModeList", null, "id="+id, null, null, null, null);
            LockMode mode = new LockMode();
            if (cursor.getCount()>0) {
                cursor.moveToLast();

                mode.setId(cursor.getInt(cursor.getColumnIndex("id")));
                mode.setName(cursor.getString(cursor.getColumnIndex("name")));
                mode.setCreateTime(cursor.getLong(cursor.getColumnIndex("createTime")));

                return mode;
            }else {
                return mode;
            }
        }
        public String getModeNameById(int id){
            Cursor cursor = dbObject.query("ModeList", null, "id="+id, null, null, null, null);
            if (cursor.getCount()>0) {
                cursor.moveToLast();
                return cursor.getString(cursor.getColumnIndex("name"));
            }else {
                return null;
            }
        }*/
    public void closeDB() {
        dbObject.close();
    }

}
