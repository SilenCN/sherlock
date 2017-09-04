package com.wocao.sherlock.DataBaseOperate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wocao.sherlock.ModeOperate.Activity.AppWhiteList;
import com.wocao.sherlock.ModeOperate.Model.LockMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silen on 16-9-9.
 */

public class ModeListDBTool {
    private Context context;
    private String dbName = "Sherlock.db";
    SQLiteDatabase dbObject;

    public ModeListDBTool(Context context) {
        this.context = context;
        dbObject = new SqlObject(context).getSqLiteDatabase();//context.openOrCreateDatabase(dbName, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        createTable();
    }

    public void createTable() {
        try {
            dbObject.rawQuery("select id from ModeList where id=0", null);

        } catch (Exception e) {
            dbObject.execSQL("CREATE TABLE ModeList(id INTEGER,name TEXT,createTime BIGINT)");
            insertMode(0,"标准模式",System.currentTimeMillis());
            insertMode(1,"增强模式",System.currentTimeMillis());
        }
    }


    public List<LockMode> getAllModeListData() {
        Cursor cursor = dbObject.query("ModeList", null, null, null, null, null, null);
        List<LockMode> list = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            LockMode mode=new LockMode();
            mode.setId(cursor.getInt(cursor.getColumnIndex("id")));
            mode.setName(cursor.getString(cursor.getColumnIndex("name")));
            mode.setCreateTime(cursor.getLong(cursor.getColumnIndex("createTime")));
            list.add(mode);
        }
        Log.i("ListDataLength",list.size()+"");
        cursor.close();
        return list;
    }

    public void deleteMode(int modeId){
        dbObject.execSQL("DELETE FROM ModeList WHERE id="+modeId);
        dbObject.execSQL("DROP TABLE AppWhiteList"+modeId);
    }

    public void updateOrCreateMode(int id,String name, long createTime) {

        Cursor cursor;
        try {
            cursor = dbObject.query("ModeList", null, "id="+id, null, null, null, null);
            if( !(cursor != null && cursor.moveToFirst()) ){
                insertMode(id, name, createTime);
                return;
            }

        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("createTime", createTime);
        dbObject.update("ModeList", values, "id=" + id, null);
    }


    private void insertMode(int id,String name, long createTime) {
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("name", name);
        values.put("createTime", createTime);
        dbObject.insert("ModeList", null, values);
    }

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
    }
    public void closeDB(){
        dbObject.close();
    }

}
