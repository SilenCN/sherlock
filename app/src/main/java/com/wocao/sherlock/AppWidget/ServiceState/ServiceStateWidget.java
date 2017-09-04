package com.wocao.sherlock.AppWidget.ServiceState;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.wocao.sherlock.AESUtils;
import com.wocao.sherlock.CoreService.CoreStatic;
import com.wocao.sherlock.R;
import com.wocao.sherlock.Setting.SettingUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class ServiceStateWidget extends AppWidgetProvider {

    private static final String MODE_NAME_CLICK_ACTION = "com.wocao.sherlock.AppWidget.ServiceState.ModeName";
    private static final String Time_Display_CLICK_ACTION = "com.wocao.sherlock.AppWidget.ServiceState.TimeDisplay";
    private static final String UNLOCK_CLICK_ACTION = "com.wocao.sherlock.AppWidget.ServiceState.Unlock";
    public static final String APPWIDGET_UPDATE_ALL = "com.wocao.sherlock.AppWidget.ServiceState.UpdateAll";
    private static final String APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE";
    private static List<Integer> IdsSet = new ArrayList<>();

    private static boolean isShowMoto = false;
    private static int cout = 0;

    public static void update(Context context, boolean isRunning) {
        ServiceStateStatic.isServiceRunning=isRunning;
        updateAppWidget(context, AppWidgetManager.getInstance(context));
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager) {
       // Log.i("running", ServiceStateStatic.isServiceRunning + "");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.service_state_widget);

        if (null != ServiceStateStatic.TIME_DISPLAY && ServiceStateStatic.isServiceRunning) {
            views.setViewVisibility(R.id.AppWidgetStateServiceNotRunningLayout, View.GONE);

            views.setTextViewText(R.id.AppWidgetServiceStateModeName, ServiceStateStatic.MODE_NAME);

            if (ServiceStateStatic.hasMotoSet) {
                if (cout / 7 > 0) {
                    cout = 0;
                    isShowMoto = !isShowMoto;
                } else {
                    cout++;
                }
                if (isShowMoto) {
                    views.setTextViewText(R.id.AppWidgetServiceStateTimeDisplay, ServiceStateStatic.Moto);
                } else {
                    views.setTextViewText(R.id.AppWidgetServiceStateTimeDisplay, "预计还有" + ServiceStateStatic.TIME_DISPLAY + "结束");
                }
            } else {
                views.setTextViewText(R.id.AppWidgetServiceStateTimeDisplay, "预计还有" + ServiceStateStatic.TIME_DISPLAY + "结束");
            }
            if (ServiceStateStatic.useUnlock) {
                views.setViewVisibility(R.id.AppWidgetServiceStateUnlockButton, View.VISIBLE);
                views.setOnClickPendingIntent(R.id.AppWidgetServiceStateUnlockButton, PendingIntent.getBroadcast(context, R.id.AppWidgetServiceStateUnlockButton, new Intent(UNLOCK_CLICK_ACTION), PendingIntent.FLAG_UPDATE_CURRENT));
            } else {
                views.setViewVisibility(R.id.AppWidgetServiceStateUnlockButton, View.GONE);
            }
            views.setOnClickPendingIntent(R.id.AppWidgetServiceStateModeName, PendingIntent.getBroadcast(context, R.id.AppWidgetServiceStateModeName, new Intent(MODE_NAME_CLICK_ACTION), PendingIntent.FLAG_UPDATE_CURRENT));
            views.setOnClickPendingIntent(R.id.AppWidgetServiceStateTimeDisplay, PendingIntent.getBroadcast(context, R.id.AppWidgetServiceStateTimeDisplay, new Intent(Time_Display_CLICK_ACTION), PendingIntent.FLAG_UPDATE_CURRENT));

        } else {
            if (ServiceStateStatic.hasMotoSet) {
                views.setViewVisibility(R.id.AppWidgetStateServiceNotRunningLayout, View.GONE);
                views.setTextViewText(R.id.AppWidgetServiceStateModeName, context.getResources().getString(R.string.AppWidgetServiceStateNotRunning));
                views.setTextViewText(R.id.AppWidgetServiceStateTimeDisplay, ServiceStateStatic.Moto);
                views.setViewVisibility(R.id.AppWidgetServiceStateUnlockButton, View.GONE);
            } else {
                views.setViewVisibility(R.id.AppWidgetStateServiceNotRunningLayout, View.VISIBLE);
            }
        }
        appWidgetManager.updateAppWidget(new ComponentName(context, ServiceStateWidget.class), views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        switch (action) {
        //    case "android.appwidget.action.APPWIDGET_DISABLED":Log.i("dsfs","dsad");break;
            case APPWIDGET_UPDATE:
            case APPWIDGET_UPDATE_ALL:
        //        Log.i("APPWIDGET_UPDATE_ALL", "Receiver");
                updateAppWidget(context, AppWidgetManager.getInstance(context));
                break;
            case UNLOCK_CLICK_ACTION:
                Intent i = new Intent();
                i.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName("com.wocao.sherlockassist", "com.wocao.sherlockassist.InputCipher");
                i.setComponent(cn);
                i.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                CoreStatic.AccessibilityModeOnTheUnlock = true;
                break;
            case MODE_NAME_CLICK_ACTION:
                break;
            case Time_Display_CLICK_ACTION:
                break;
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

     //   ServiceStateStatic.hasAppWidget = true;
        ServiceStateStatic.useUnlock = context.getSharedPreferences("data", Context.MODE_PRIVATE).getBoolean(AESUtils.encrypt("wocstudiosoftware", "useCipher"), false);

        ServiceStateStatic.Moto = SettingUtils.getStringValue(context, "setting_display_motto", null);
        if (null != ServiceStateStatic.Moto && !ServiceStateStatic.Moto.equals("")) {
            ServiceStateStatic.hasMotoSet = true;
        } else {
            ServiceStateStatic.hasMotoSet = false;
        }

        updateAppWidget(context, appWidgetManager);
        /*    IdsSet.add((Integer) appWidgetId);
        }
*/
    }


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        ServiceStateManager.setHaveAppWidget(context,true);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
      //  Log.i("Disable","appwidget");
        ServiceStateManager.setHaveAppWidget(context,false);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            IdsSet.remove((Integer) appWidgetId);
        }
    }

}

