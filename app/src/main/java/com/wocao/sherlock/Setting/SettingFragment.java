package com.wocao.sherlock.Setting;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.wocao.sherlock.Accessibility.AccessbilityTool;
import com.wocao.sherlock.Alarm.Notification.AlarmNotificationOperate;
import com.wocao.sherlock.ControlByOther.ControlType;
import com.wocao.sherlock.ControlByOther.Presenter.ControlByOtherDialogManager;
import com.wocao.sherlock.Permission.PolicyAdminUtils;
import com.wocao.sherlock.R;
import com.wocao.sherlock.Setting.ControlByOther.ControlByOtherSettingActivity;
import com.wocao.sherlock.Setting.DiyFloatView.DiyFloatViewActivity;
import com.wocao.sherlock.Setting.LimitUnlock.LimitUnlockSettingActivity;
import com.wocao.sherlock.Setting.Uninstall.UninstallActivity;
import com.wocao.sherlock.Shortcut.Activity.ShortcutsActivity;

/**
 * Created by 10397 on 2016/3/24.
 */

public class SettingFragment extends PreferenceFragment {
    CheckBoxPreference accessibilityCP;
    CheckBoxPreference notificationCP;
    CheckBoxPreference showDesktopCP;
    CheckBoxPreference restartAfterUnlockCP;
    CheckBoxPreference usingTimerForRestartCP;
    Preference shortcutP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preference_dependencies);
        accessibilityCP = (CheckBoxPreference) findPreference(SettingUtils.getEncryptText("setting_better_accisibility_gettoppackage"));
        accessibilityCP.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (!AccessbilityTool.isAccessibilitySettingsOn(getActivity())) {
                    // 引导至辅助功能设置页面
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                } else {
                    // 执行辅助功能服务相关操作
                }
                return false;
            }
        });
        notificationCP = (CheckBoxPreference) findPreference(SettingUtils.getEncryptText("setting_showNotification"));
        notificationCP.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Toast.makeText(getActivity(), ""+!notificationCP.isChecked(), Toast.LENGTH_SHORT).show();
                AlarmNotificationOperate.operateAllNotifition(getActivity(), !notificationCP.isChecked());
                return false;
            }
        });

        showDesktopCP = ((CheckBoxPreference) findPreference(SettingUtils.getEncryptText("setting_better_showDesktopApp")));
        showDesktopCP.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (showDesktopCP.isChecked()) {
                    PolicyAdminUtils.checkDeviceAdmin(getActivity(), ((AppCompatActivity) getActivity()).getSupportFragmentManager(), true);
                }
                return false;
            }
        });


        restartAfterUnlockCP = ((CheckBoxPreference) findPreference(SettingUtils.getEncryptText("setting_better_startAfterUnlock")));
        usingTimerForRestartCP = (CheckBoxPreference) findPreference(SettingUtils.getEncryptText("setting_better_usingTimerToRestart"));
        restartAfterUnlockCP.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                usingTimerForRestartCP.setEnabled((boolean) newValue);
                return true;
            }
        });

        ((Preference) findPreference("setting_control_by_other")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ControlByOtherDialogManager manager = new ControlByOtherDialogManager(getActivity());
                manager.setOnSuccessListener(new ControlByOtherDialogManager.OnSuccessListener() {
                    @Override
                    public void success() {
                        startActivity(new Intent(getActivity(), ControlByOtherSettingActivity.class));
                    }
                });
                manager.test(SettingActivity.fragmentManager, ControlType.TYPE_GOTO_SETTING);

                return false;
            }
        });

        ((Preference) findPreference("setting_uninstall")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), UninstallActivity.class));
                return false;
            }
        });
        ((Preference) findPreference("setting_limit_unlock")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), LimitUnlockSettingActivity.class));
                return false;
            }
        });
        shortcutP = findPreference("setting_quick_shortcut");
        if (Build.VERSION.SDK_INT < 25) {
            shortcutP.setEnabled(false);
        }
        shortcutP.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent().setClass(getActivity(), ShortcutsActivity.class));
                return false;
            }
        });
        ((Preference) findPreference("setting_float_view_diy")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), DiyFloatViewActivity.class));
                return false;
            }
        });
    }
}
