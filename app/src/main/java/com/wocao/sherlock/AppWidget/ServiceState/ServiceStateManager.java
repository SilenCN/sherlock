package com.wocao.sherlock.AppWidget.ServiceState;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by silen on 17-5-9.
 */

public class ServiceStateManager {
    private static Boolean isHaveAppWidget;


    public static Boolean getHaveAppWidget(Context context) {
        if (null == isHaveAppWidget) {
            isHaveAppWidget = context.getSharedPreferences("data", Context.MODE_PRIVATE).getBoolean("HaveAppWidget", false);
        }
      //  Log.i("isHaveAppWidget",isHaveAppWidget+"");
        return isHaveAppWidget;
    }

    public static void setHaveAppWidget(Context context, Boolean haveAppWidget) {
       // Log.i("isHaveAppWidget",isHaveAppWidget+"Setting");
        isHaveAppWidget = haveAppWidget;
        context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putBoolean("HaveAppWidget", haveAppWidget).commit();
    }
}
