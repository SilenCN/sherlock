package com.wocao.sherlock.Setting.Uninstall;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.wocao.sherlock.Assist.AssistUtils;
import com.wocao.sherlock.Permission.PolicyAdminUtils;
import com.wocao.sherlock.R;
import com.wocao.sherlock.adminReceiver;
import com.wocao.sherlock.appTool;
import com.wocao.sherlock.checkapp;


/**
 * Created by silen on 17-4-23.
 */

public class UninstallFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.uninstall_preference_dependencies);
        (findPreference("uninstall_cancel_policy")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (PolicyAdminUtils.isAdminActive(getActivity())) {
                    DevicePolicyManager policyManager = (DevicePolicyManager) getActivity().getBaseContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
                    policyManager.removeActiveAdmin(new ComponentName(getActivity(), adminReceiver.class));
                    Toast.makeText(getActivity(), "已取消设备管理器权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "该权限未开启", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        (findPreference("uninstall_assist")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (AssistUtils.checkAssistIsInstall(getActivity())) {
                    Uri uri = Uri.parse("package:com.wocao.sherlockassist");
                    Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "已卸载辅助程序", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
        (findPreference("uninstall_sherlock")).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });
    }
}
