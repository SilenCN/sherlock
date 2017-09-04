package com.wocao.sherlock.DataBaseOperate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


public class AppWhiteDBTool {
    private Context context;
    private String dbName = "Sherlock.db";
    SQLiteDatabase dbObject;
    int modeID=0;
    public AppWhiteDBTool(Context context,int modeID) {
        this.context = context;
        this.modeID=modeID;
        dbObject = new SqlObject(context).getSqLiteDatabase();//context.openOrCreateDatabase(dbName, SQLiteDatabase.CREATE_IF_NECESSARY, null);
        createTable();
    }

    public void createTable() {
        try {
            dbObject.rawQuery("select Package from AppWhiteList"+modeID, null);
        } catch (Exception e) {
            dbObject.execSQL("CREATE TABLE AppWhiteList"+modeID+"(Package TEXT,canRun BOOLEAN,canOpen BOOLEAN,mode INTEGER)");
        }
    }

    /**
     * This method is to get all the packages that can open after the Sherlock Service running
     *
     * @return the list of packages.
     */
    public List quaryAllPackageCanOpen() {
        Cursor cursor = dbObject.query("AppWhiteList"+modeID, null, "canOpen=1", null, null, null, null);
        List list = new ArrayList();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex("Package")));
        }
        return list;
    }

    /**
     * This method is to update the package information,if not exist,it will insert a data.
     *
     * @param packageName The package to update.
     * @param canRun      whether this package can ran after the Sherlock Service running.
     * @param canOpen     Whether this package can open after the Sherlock Service running.
     */
    public void updateOrCreatePackageInfo(String packageName, boolean canRun, boolean canOpen) {
       // insertPackageInfo(packageName, canRun, canOpen);
        Cursor cursor;
        try {
            cursor = dbObject.query("AppWhiteList"+modeID, null, "Package=\"" + packageName+"\"", null, null, null, null);
            if( !(cursor != null && cursor.moveToFirst()) ){
                insertPackageInfo(packageName, canRun, canOpen);
                return;
            }else {
                if((canRun==(cursor.getInt(cursor.getColumnIndex("canRun")) == 1 ? true : false))&&(canOpen==(cursor.getInt(cursor.getColumnIndex("canOpen")) == 1 ? true : false))){
                   return;
                }
            }

        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        ContentValues values = new ContentValues();
        values.put("canRun", canRun);
        values.put("canOpen", canOpen);
        dbObject.update("AppWhiteList"+modeID, values, "Package=\"" + packageName+"\"", null);
    }
    public void updateOrCreatePackageInfo(String packageName, boolean canRun) {
        // insertPackageInfo(packageName, canRun, canOpen);
        Cursor cursor;
        try {
            cursor = dbObject.query("AppWhiteList"+modeID, null, "Package=\"" + packageName+"\"", null, null, null, null);
            if( !(cursor != null && cursor.moveToFirst()) ){
                insertPackageInfo(packageName, canRun, false);
                return;
            }else {
                if((canRun==(cursor.getInt(cursor.getColumnIndex("canRun")) == 1 ? true : false))){
                    return;
                }
            }

        } catch (Exception e) {
            System.out.println(e);
            return;
        }
        ContentValues values = new ContentValues();
        values.put("canRun", canRun);
        dbObject.update("AppWhiteList"+modeID, values, "Package=\"" + packageName+"\"", null);
    }
    /**
     * This method is to insert data.
     *
     * @param packageName The package to insert.
     * @param canRun      whether this package can ran after the Sherlock Service running.
     * @param canOpen     Whether this package can open after the Sherlock Service running.
     */
    private void insertPackageInfo(String packageName, boolean canRun, boolean canOpen) {
        ContentValues values = new ContentValues();
        values.put("Package", packageName);
        values.put("canRun", canRun);
        values.put("canOpen", canOpen);
        dbObject.insert("AppWhiteList"+modeID, null, values);
    }

    /**
     * This method is to get package information about whether can run and open after the Sherlock Service running according to package name.
     *
     * @param packageName Which package information to get.
     * @return a array of information.Index 0 is whether can run,index 1 is whether can open.
     */
    public boolean[] quaryPackageInfo(String packageName) {
        Cursor cursor = dbObject.query("AppWhiteList"+modeID, null, "Package=\"" + packageName+"\"", null, null, null, null);
        boolean[] bool = new boolean[2];
        if (cursor.getCount()>0) {
            cursor.moveToLast();

            bool[0] = cursor.getInt(cursor.getColumnIndex("canRun")) == 1 ? true : false;
            bool[1] = cursor.getInt(cursor.getColumnIndex("canOpen")) == 1 ? true : false;
        }else {
            bool[0]=false;
            bool[1]=false;
            insertPackageInfo(packageName,false,false);
        }
        cursor.close();
        return bool;
    }
    public void closeDB(){
        dbObject.close();
    }
}
