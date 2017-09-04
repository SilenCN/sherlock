package com.wocao.sherlock.DataBaseOperate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by silen on 16-10-18.
 */

public class SqlObject {
    private static SQLiteDatabase sqLiteDatabase;
    private Context context;
    public SqlObject(Context context) {
        this.context=context;
    }
    public SQLiteDatabase getSqLiteDatabase(){
        if (null!=sqLiteDatabase&&sqLiteDatabase.isOpen()){
            return sqLiteDatabase;
        }else {
            sqLiteDatabase=new SqliteManager(context).getWritableDatabase();
            return sqLiteDatabase;
        }
    }
}
