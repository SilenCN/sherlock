package com.wocao.sherlock.Setting.ControlByOther;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.wocao.sherlock.Permission.PolicyAdminUtils;
import com.wocao.sherlock.R;
import com.wocao.sherlock.Setting.SettingUtils;
import com.wocao.sherlock.adminReceiver;
import com.wocao.sherlock.appTool;

/**
 * Created by silen on 17-4-25.
 */

public class ControlByOtherSettingFragment extends PreferenceFragment {
    CheckBoxPreference switchP;
    EditTextPreference passwordP;
    PreferenceCategory dependencyP;
    Preference adminP;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.control_by_other_preference_dependencies);
        init();

    }

    private void init() {
        findP();
        enableManage();
        setListener();
    }

    private void setListener() {

        switchP.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                enableManage();
                return false;
            }
        });
        adminP.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (!PolicyAdminUtils.isAdminActive(getActivity())) {
                    PolicyAdminUtils.activePolicyAdmin(getActivity(), new ComponentName(getActivity(), adminReceiver.class));
                } else {
                    Toast.makeText(getActivity(), "已开启设备管理器权限！", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void findP() {
        switchP = (CheckBoxPreference) findPreference(SettingUtils.getEncryptText("control_by_other_switch"));
        passwordP = (EditTextPreference) findPreference(SettingUtils.getEncryptText("control_by_other_password"));
        dependencyP = (PreferenceCategory) findPreference("control_by_other_dependency");
        adminP = findPreference("control_by_other_admin_policy");
    }

    private void enableManage() {
        passwordP.setEnabled(switchP.isChecked());
        dependencyP.setEnabled(switchP.isChecked());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (switchP.isChecked()) {
            if (!(null != passwordP.getText() && null != passwordP.getText().toString() && !passwordP.getText().toString().equals(""))) {
                switchP.setChecked(false);
                Toast.makeText(getActivity(), "未设置密码，已关闭他人控制功能！", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
