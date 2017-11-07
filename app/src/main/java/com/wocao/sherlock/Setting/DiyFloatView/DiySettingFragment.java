package com.wocao.sherlock.Setting.DiyFloatView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.wocao.sherlock.R;
import com.wocao.sherlock.Setting.SettingUtils;
import com.wocao.sherlock.Widget.ColorPickerDialog;

/**
 * Created by silen on 17-4-26.
 */

public class DiySettingFragment extends PreferenceFragment {
    ListPreference textSizeLP, textAlphaLP;
    Preference textColorP;
    CheckBoxPreference boldCB;
    private int viewId = 0;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.diy_floatview_dependencies);
        viewId = ((DiyFloatViewDetailActivity) getActivity()).getViewId();
        sharedPreferences = getActivity().getSharedPreferences("DisplayConf", Context.MODE_PRIVATE);
        textAlphaLP = (ListPreference) findPreference("TextAlpha");
        textSizeLP = (ListPreference) findPreference("TextSize");
        textColorP = findPreference("TextColor");
        boldCB = (CheckBoxPreference) findPreference("IsFakeBoldText");
        textAlphaLP.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                textAlphaLP.setValue((String) newValue);
/*                if (textAlphaLP.getValue().equals("0")) {
                    sharedPreferences.edit().remove(viewId + "TextAlpha").apply();
                } else {*/
                sharedPreferences.edit().putInt(viewId + "TextAlpha", Integer.parseInt(textAlphaLP.getValue())).apply();
                //            }
                textAlphaLP.setSummary(textAlphaLP.getEntry());
                return true;
            }
        });
        textSizeLP.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                textSizeLP.setValue((String) newValue);
                System.out.println(newValue);
                System.out.println(textSizeLP.getValue());
                if (textSizeLP.getValue().equals("0")) {
                    sharedPreferences.edit().remove(viewId + "TextSize").apply();
                } else {
                    sharedPreferences.edit().putInt(viewId + "TextSize", Integer.parseInt(textSizeLP.getValue())).apply();
                }

                textSizeLP.setSummary(textSizeLP.getEntry());
                return true;
            }
        });
        boldCB.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                sharedPreferences.edit().putBoolean(viewId + "IsFakeBoldText", (boolean) newValue).apply();

                return true;
            }
        });
        textColorP.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getActivity());
                colorPickerDialog.setListener(new ColorPickerDialog.OnColorReturnListener() {
                    @Override
                    public void onReturn(String colorName, int color) {
                        sharedPreferences.edit().putInt(viewId + "TextColor", color).apply();
                        sharedPreferences.edit().putString(viewId + "TextColorName", colorName).apply();
                        textColorP.setSummary(colorName);
                    }

                    @Override
                    public void onDefault() {
                        sharedPreferences.edit().remove(viewId + "TextColor").apply();
                        textColorP.setSummary("默认");
                    }
                });
                colorPickerDialog.show(((DiyFloatViewDetailActivity) getActivity()).getSupportFragmentManager(),null);
                return true;
            }
        });

        initP();

    }

    private void initP() {
        if (sharedPreferences.contains(viewId + "TextSize")) {
            textSizeLP.setValue(sharedPreferences.getInt(viewId + "TextSize", 14) + "");
        } else {
            textSizeLP.setValueIndex(0);
        }
        textSizeLP.setSummary(textSizeLP.getEntry());

        if (sharedPreferences.contains(viewId + "TextAlpha")) {
            textAlphaLP.setValue(sharedPreferences.getInt(viewId + "TextAlpha", 0) + "");
        } else {
            textAlphaLP.setValueIndex(0);
        }

        textAlphaLP.setSummary(textAlphaLP.getEntry());

        boldCB.setChecked(sharedPreferences.getBoolean(viewId + "IsFakeBoldText", false));

        if (sharedPreferences.contains(viewId + "TextColor")) {
            textColorP.setSummary(sharedPreferences.getString(viewId + "TextColorName", "默认"));
        }


    }


}
