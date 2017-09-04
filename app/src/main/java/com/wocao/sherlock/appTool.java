package com.wocao.sherlock;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.wocao.sherlock.Accessibility.AccessbilityTool;
import com.wocao.sherlock.Permission.UsageStatsPermissionDialog;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 新宇 on 2015/11/22.
 */
public class appTool {

    public static Context context;

    public static String getLanguageCode(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        Boolean contain = false;

        AssetManager am = context.getAssets();
        try {
            for (String s : am.list("")) {
                if (s.equals(language)) {
                    contain = true;
                    break;
                }
            }
        } catch (IOException e) {
        }

        if (contain) {
            return language;
        } else {
            return "zh";
        }
    }

    public static boolean cipherIsIlligel(String cipher) {

        String regexString = "0123456789qwertyuiopasdfghjklzxcvbnm._QWERTYUIOPASDFGHJKLZXCVBNM";
        for (char ch : regexString.toCharArray()) {
            Pattern pattern = Pattern.compile("[" + ch + "]{" + AppConfig.cipherRepeatMaxLength + ",}");
            Matcher matcher = pattern.matcher(cipher);
            if (matcher.find()) {
                return false;
            }
        }
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        Matcher matcher = pattern.matcher(cipher);
        if (!matcher.find()) {
            return false;
        }

        pattern = Pattern.compile("[0-9]+");
        matcher = pattern.matcher(cipher);
        if (!matcher.find()) {
            return false;
        }

        return true;
    }

    public static String getLauncherPackageName(Context context) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            return null;
        }
        if (res.activityInfo.packageName.equals("android")) {
            // 有多个桌面程序存在，且未指定默认项时；
            return getHomes(context);
        } else {
            return res.activityInfo.packageName;
        }
    }

    public static String getHomes(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        AppConfig.homesPackage = resolveInfo.get(0).activityInfo.packageName;
        return AppConfig.homesPackage;
    }

    public static List<String> getAllHomes(Context context) {
        PackageManager packageManager = context.getPackageManager();
        //灞炴€?
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        List<String> list = new ArrayList<>();
        for (ResolveInfo info : resolveInfo) {
            list.add(info.activityInfo.packageName);
        }
        return list;
    }


    //该方法用于获取状态栏高度
    public static int getStatusHeight(Activity activity, Boolean Support, int defaultValue, Context context) {

        int statusHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT || Support) {
            Rect localRect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
            statusHeight = localRect.top;
            if (0 == statusHeight) {
                Class<?> localClass;
                try {
                    localClass = Class.forName("com.android.internal.R$dimen");
                    Object localObject = localClass.newInstance();
                    int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                    statusHeight = activity.getResources().getDimensionPixelSize(i5);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
        if (0 == statusHeight) {
            statusHeight = DipToPixels(context, defaultValue);
        }
        return statusHeight;
    }

    public static int DipToPixels(Context context, int dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        float valueDips = dip;
        int valuePixels = (int) (valueDips * SCALE + 0.5f);

        return valuePixels;
    }

    public static void disableWifiConnection(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        switch (manager.getWifiState()) {
            case WifiManager.WIFI_STATE_ENABLED:
                manager.setWifiEnabled(false);
                break;
        }
    }

    public static void disableBlueToothConnection(Context context) {
        BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
      /*  switch (manager.getAdapter().getState()){
            case
        }*/

    }

    public static void disableDataConnection(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Class<?> connectClass = Class.forName(manager.getClass().getName());
            Field iConnectivityManagerField = connectClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            Object connectivityManager = iConnectivityManagerField.get(manager);
            Class<?> iConnectivityManagerClass = Class.forName(connectivityManager.getClass().getName());
            Method method = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            method.setAccessible(true);
            method.invoke(connectivityManager, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
