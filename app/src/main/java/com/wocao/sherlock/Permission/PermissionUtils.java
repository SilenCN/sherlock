package com.wocao.sherlock.Permission;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.wocao.sherlock.Accessibility.AccessbilityTool;
import com.wocao.sherlock.Assist.AssistUtils;
import com.wocao.sherlock.Setting.SettingUtils;

/**
 * Created by silen on 17-6-10.
 */

public class PermissionUtils {
    public static boolean checkPermission(Context context, FragmentManager fragmentManager, boolean isStrengthMode) {
        if (AssistUtils.checkAssistUpdate(context)) {
            if (SettingUtils.checkShowDesktopStats(context, fragmentManager)) {
                if (SettingUtils.isShowDesktop(context) || OverlayPermissionUtils.checkOverlayPermission(context, fragmentManager)) {
                    if (AccessbilityTool.checkAccessibilityWithDialog(context, fragmentManager) || (!AccessbilityTool.isUseAccessibility(context) && UsageStatsUtils.needPermissionForBlocking(context, fragmentManager))) {
                        if (PolicyAdminUtils.checkDeviceAdmin(context, fragmentManager, isStrengthMode)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
