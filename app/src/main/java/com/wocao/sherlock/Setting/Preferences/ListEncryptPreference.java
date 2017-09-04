package com.wocao.sherlock.Setting.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.wocao.sherlock.Setting.SettingUtils;

/**
 * Created by silen on 17-4-28.
 */

public class ListEncryptPreference extends ListPreference {
    public ListEncryptPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setKeyTest();
    }

    public ListEncryptPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setKeyTest();
    }

    public ListEncryptPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setKeyTest();
    }

    public ListEncryptPreference(Context context) {
        super(context);
        setKeyTest();
    }

    private void setKeyTest() {
        setKey(SettingUtils.getEncryptText(getKey()));
    }

    @Override
    protected String getPersistedString(String defaultReturnValue) {
        if (!shouldPersist()) {
            return defaultReturnValue;
        }

        String saveString = getSharedPreferences().getString(getKey(), defaultReturnValue);
        if (null == saveString || saveString.equals("")) {
            return null;
        } else {
            return SettingUtils.getDecryptText(saveString);
        }
    }

    @Override
    protected boolean persistString(String value) {
        if (shouldPersist()) {
            // Shouldn't store null
            if (TextUtils.equals(value, getPersistedString(null))) {
                // It's already there, so the same as persisting
                return true;
            }

            SharedPreferences.Editor editor = getEditor();
            if (null == value || value.equals("")) {
                editor.putString(getKey(), null);
            } else
                editor.putString(getKey(), SettingUtils.getEncryptText(value));
            editor.commit();
            return true;
        }
        return false;
    }

}
