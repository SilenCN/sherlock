package com.wocao.sherlock.Accessibility;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import com.wocao.sherlock.Setting.SettingUtils;

import static android.content.ContentValues.TAG;

/**
 * Created by silen on 17-4-1.
 */

public class AccessbilityTool {
    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.i(TAG, e.getMessage());
        }

        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }

        return false;
    }

    public static boolean isUseAccessibility(Context context) {
        return SettingUtils.getBooleanValue(context, "setting_better_accisibility_gettoppackage", false);
        //PreferenceManager.getDefaultSharedPreferences(context).getBoolean("setting_better_accisibility_gettoppackage", false);
    }

    public static void unCheckSetting(Context context) {
        SettingUtils.putBooleanValue(context, "setting_better_accisibility_gettoppackage", false);
        //PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("setting_better_accisibility_gettoppackage",false).commit();
    }

    public static void checkedSetting(Context context) {
        SettingUtils.putBooleanValue(context, "setting_better_accisibility_gettoppackage", true);
        //PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("setting_better_accisibility_gettoppackage",true).commit();
        context.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }


    public static boolean canUseAccessibility(Context context) {
        return isUseAccessibility(context) && isAccessibilitySettingsOn(context);
    }

    public static boolean checkAccessibilityWithDialog(Context context, android.support.v4.app.FragmentManager manager) {
        if (canUseAccessibility(context)) {
            return true;
        }
        if (isUseAccessibility(context) && !isAccessibilitySettingsOn(context)) {
            new AccessibilityCheckDialog().show(manager, null);
            return false;
        }
        return false;
    }
}
