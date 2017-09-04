package com.wocao.sherlock;

import android.content.*;
import android.content.pm.*;

import java.util.*;

public class checkapp {

    public static boolean checkAssitVersion(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            PackageInfo pkg = context.getPackageManager().getPackageInfo(packageName, 0);
            if (AppConfig.assistAppVersion.equals(pkg.versionName)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

}
