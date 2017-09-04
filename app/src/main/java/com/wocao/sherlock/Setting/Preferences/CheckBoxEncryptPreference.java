package com.wocao.sherlock.Setting.Preferences;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import com.wocao.sherlock.Setting.SettingUtils;

/**
 * Created by silen on 17-4-28.
 */

public class CheckBoxEncryptPreference extends CheckBoxPreference {

    private Object mDefaultValue;

    public CheckBoxEncryptPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setKeyTest();
    }

    public CheckBoxEncryptPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setKeyTest();
    }

    public CheckBoxEncryptPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setKeyTest();
    }

    public CheckBoxEncryptPreference(Context context) {
        super(context);
    }

    private void setKeyTest(){
        try {
            setKey(SettingUtils.getEncryptText(getKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*    @Override
    protected boolean persistBoolean(boolean value) {
        if (shouldPersist()) {
            if (value == getPersistedBoolean(!value)) {
                // It's already there, so the same as persisting
                return true;
            }

            SharedPreferences.Editor editor = getEditor();
            try {
                editor.putBoolean(AESUtils.encrypt("SILEN", getKey()), value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                editor.apply();
            } catch (AbstractMethodError unused) {
                editor.commit();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        Log.i("+++++++++++++++++++++++", "____________________+++_______");
        final boolean shouldPersist = shouldPersist();
        try {
            if (!shouldPersist || !getSharedPreferences().contains(AESUtils.encrypt("SILEN", getKey()))) {
                if (mDefaultValue != null) {
                    setInitialValue(false, mDefaultValue);
                }
            } else {
                setInitialValue(true, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        Log.i("+++++++++++++++++++++++", "____________________+++_______");
        mDefaultValue=super.onGetDefaultValue(a,index);
        return super.onGetDefaultValue(a, index);
    }

    private void setInitialValue(boolean restoreValue, Object defaultValue){
        setChecked(restoreValue ? getPersistedBoolean(isChecked())
                : (Boolean) defaultValue);
    }


    @Override
    protected boolean getPersistedBoolean(boolean defaultReturnValue) {
        Log.i("+++++++++++++++++++++++", "____________________+++_______");
        if (!shouldPersist()) {
            return defaultReturnValue;
        }

        try {
            Log.i("+++++++++++++++++++++++", "_____________________________");

            return getSharedPreferences().getBoolean(AESUtils.encrypt("SILEN", getKey()), defaultReturnValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("+++++++++++++++++++++++", "++++++++++++++++++");
        return getSharedPreferences().getBoolean(getKey(), defaultReturnValue);
    }*/
}
