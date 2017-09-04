package com.wocao.sherlock.Setting;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wocao.sherlock.AESUtils;
import com.wocao.sherlock.Assist.AssistUtils;
import com.wocao.sherlock.Permission.PolicyAdminUtils;
import com.wocao.sherlock.appTool;
import com.wocao.sherlock.checkapp;

import java.util.Map;

/**
 * Created by silen on 17-4-28.
 */

public class SettingUtils {
    public static String getDecryptText(String encrypted) {
        if (null == encrypted || encrypted.equals("")) {
            return null;
        } else {
            return AESUtils.decrypt("SILEN", encrypted);
        }
    }

    public static String getEncryptText(String cleartext) {
        if (null == cleartext || cleartext.equals("")) {
            return null;
        } else {
            return AESUtils.encrypt("SILEN", cleartext);
        }
    }

    public static String getStringValue(Context context, String key, String defineValue) {
        return getDecryptText(PreferenceManager.getDefaultSharedPreferences(context).getString(getEncryptText(key), defineValue));
    }

    public static boolean getBooleanValue(Context context, String key, boolean defineValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(getEncryptText(key), defineValue);
    }

    public static void putBooleanValue(Context context, String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(getEncryptText(key), value).commit();
    }

    public static void putStringValue(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(getEncryptText(key), getEncryptText(value)).commit();
    }

    public static void updatePreferences(Context context) {
        if (AssistUtils.getAssistVersion(context) < 2.75 && !PreferenceManager.getDefaultSharedPreferences(context).contains(getEncryptText("Update"))) {
            for (Map.Entry<String, ?> entry : PreferenceManager.getDefaultSharedPreferences(context).getAll().entrySet()) {
                if (entry.getValue().getClass().getName().equals("java.lang.String")) {
                    putStringValue(context, entry.getKey(), (String) entry.getValue());
                } else {
                    putBooleanValue(context, entry.getKey(), (Boolean) entry.getValue());
                }
            }
            putBooleanValue(context, "Update", true);
        }
    }

    public static boolean isShowDesktop(Context context){
        return getBooleanValue(context, "setting_better_showDesktopApp", false);
    }
    public static boolean checkShowDesktopStats(Context context, android.support.v4.app.FragmentManager fragmentManager) {
        if (isShowDesktop(context)) {
            if (!PolicyAdminUtils.checkDeviceAdmin(context, fragmentManager, true)) {
                return false;
            }
        }
        return true;
    }
}
