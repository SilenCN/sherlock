package com.wocao.sherlock.Setting.LimitUnlock;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.wocao.sherlock.R;
import com.wocao.sherlock.Setting.SettingUtils;

/**
 * Created by silen on 17-4-26.
 */

public class LimitUnlockSettingFragment extends PreferenceFragment {
    PreferenceCategory dependencyPC;
    CheckBoxPreference switchCP;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.limit_unlock_preference_dependencies);
        switchCP=(CheckBoxPreference)findPreference(SettingUtils.getEncryptText("limit_unlock_switch"));

        dependencyPC=(PreferenceCategory)findPreference("limit_unlock_dependency");
        dependencyPC.setEnabled(switchCP.isChecked());
        switchCP.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                dependencyPC.setEnabled(switchCP.isChecked());
                return false;
            }
        });
    }
}
