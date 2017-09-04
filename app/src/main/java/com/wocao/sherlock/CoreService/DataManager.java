package com.wocao.sherlock.CoreService;

import android.content.Context;
import android.provider.Settings;

import com.wocao.sherlock.AESUtils;
import com.wocao.sherlock.Accessibility.AccessbilityTool;
import com.wocao.sherlock.AppWidget.ServiceState.ServiceStateStatic;
import com.wocao.sherlock.Setting.SettingUtils;
import com.wocao.sherlock.appTool;

/**
 * Created by silen on 17-5-2.
 */

public class DataManager {

    private Context context;
    private boolean isUseDesktop;
    private String launcherPackageName;
    private boolean isDisableKeyGround;
    private boolean isUseAccessibility;
    private boolean isPlaySoundRing;
    private String defaultInputMethod;

    public DataManager(Context context) {
        this.context = context;
        init();
    }

    private void init() {

        launcherPackageName = appTool.getLauncherPackageName(context);
        isUseDesktop = SettingUtils.getBooleanValue(context, "setting_better_showDesktopApp", false);
        if (isUseDesktop){
            isDisableKeyGround=true;
        }else{
            isDisableKeyGround=SettingUtils.getBooleanValue(context,"setting_better_unlocksystem",false);
        }
        isUseAccessibility= AccessbilityTool.canUseAccessibility(context);
        isPlaySoundRing=SettingUtils.getBooleanValue(context,"setting_playEndSound",false);
        getInputMethod();

        ServiceStateStatic.useUnlock = context.getSharedPreferences("data",Context.MODE_PRIVATE).getBoolean(AESUtils.encrypt("wocstudiosoftware", "useCipher"), false);
        ServiceStateStatic.Moto = SettingUtils.getStringValue(context, "setting_display_motto", null);

    }

    public String getLauncherPackageName() {
        return launcherPackageName;
    }

    public boolean isUseDesktop() {
        return isUseDesktop;
    }

    public boolean isDisableKeyGround() {
        return isDisableKeyGround;
    }

    public boolean isUseAccessibility() {
        return isUseAccessibility;
    }

    public boolean isPlaySoundRing() {
        return isPlaySoundRing;
    }
    private void getInputMethod() {
        String inputComponent = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        if (!inputComponent.isEmpty()) {
            defaultInputMethod = inputComponent.substring(0, inputComponent.indexOf("/"));
        }
    }

    public String getDefaultInputMethod() {
        return defaultInputMethod;
    }
}
