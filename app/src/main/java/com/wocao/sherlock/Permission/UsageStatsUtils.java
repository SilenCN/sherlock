package com.wocao.sherlock.Permission;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Binder;
import android.os.Build;

import com.wocao.sherlock.AppConfig;

import java.util.List;

/**
 * Created by silen on 17-6-10.
 */

public class UsageStatsUtils {

    public static boolean needPermissionForBlocking(Context context, android.support.v4.app.FragmentManager fragmentManager) {
        try {
            if (AppConfig.debugForFlyme) return true;

            if (Build.VERSION.SDK_INT < 21) return true;

            boolean returnBool = false;
            returnBool = checkUsageStats(context);
            if (!returnBool) {
                new UsageStatsPermissionDialog(context).show(fragmentManager, null);
            }
            return returnBool;
        } catch (Exception e) {
            return true;
        }
    }


    private static boolean checkUsageStats(Context context) {
        if (AppConfig.debugForFlyme) return true;

        if (Build.VERSION.SDK_INT < 21) return true;

        AppOpsManager localAppOpsManager = (AppOpsManager) context.getSystemService("appops");
        int i = localAppOpsManager.checkOp("android:get_usage_stats", Binder.getCallingUid(), "com.wocao.sherlock");
        if (0 == i)
            return true;
        else if (3 == i) {
            List<PackageInfo> packageInfos = context.getPackageManager().getPackagesHoldingPermissions(new String[]{"android.permission.PACKAGE_USAGE_STATS"}, 0);
            if (null != packageInfos) {
                for (PackageInfo info : packageInfos) {
                    if ("com.wocao.sherlock".equals(info.packageName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
