package com.wocao.sherlock.Permission;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentManager;

import com.wocao.sherlock.Permission.FloatWindowPermission.FloatWindowManager;

/**
 * Created by silen on 17-6-10.
 */

public class OverlayPermissionUtils {

    public static boolean checkOverlayPermission(Context context, FragmentManager fragmentManager) {

        if (Build.VERSION.SDK_INT >= 19) {
            if (!FloatWindowManager.getInstance().checkPermission(context)) {
                new com.wocao.sherlock.Permission.OverlayPermissionDialog(context).show(fragmentManager, null);
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
}
