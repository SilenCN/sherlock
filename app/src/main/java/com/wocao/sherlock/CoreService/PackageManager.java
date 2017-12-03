package com.wocao.sherlock.CoreService;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.silen.applicationAnalyse.writeInforToSdCard;
import com.wocao.sherlock.AppConfig;
import com.wocao.sherlock.DataBaseOperate.AppWhiteDBTool;
import com.wocao.sherlock.Permission.PolicyAdminUtils;
import com.wocao.sherlock.appTool;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * Created by silen on 17-5-3.
 */

public class PackageManager {

    private Context context;
    private boolean isApiUpThan21 = false;
    private ActivityManager activityManager;
    private KeyguardManager keyguardManager;

    private DataManager dataManager;
    //private int modeId;
    private AppWhiteDBTool dbTool;
    private String lastPackageName;
    boolean canRun = false;

    private int modeId;

    DevicePolicyManager policyManager;
    boolean isAdminMode = false;

    public PackageManager(Context context, DataManager dataManager) {
        this.context = context;
        isApiUpThan21 = (Build.VERSION.SDK_INT >= 21);
        activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        this.dataManager = dataManager;
        updateModeId();

    }

    public void updateModeId() {
        modeId = context.getSharedPreferences("data", Context.MODE_PRIVATE).getInt("LockModeId", 0);
        setModeId(modeId);

        if (modeId == 1 && PolicyAdminUtils.isAdminActive(context)) {
            isAdminMode = true;
        } else {
            isAdminMode = false;
        }
    }

    public int getModeId() {
        return this.modeId;
    }

    private void setModeId(int modeId) {
        dbTool = new AppWhiteDBTool(context, modeId);
    }

    public String getCurrentPkgName() {
        if (isApiUpThan21 && !AppConfig.debugForFlyme) {
            return getTopPackage();
        } else {
            ComponentName topActivity = activityManager.getRunningTasks(1).get(0).topActivity;
            return topActivity.getPackageName();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private String getTopPackage() {
        long ts = System.currentTimeMillis();
        /*private UsageStatsManager mUsageStatsManager;
        mUsageStatsManager =;*/
        List<UsageStats> usageStats = ((UsageStatsManager) context.getSystemService("usagestats")).queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 4000, ts);
        if (usageStats == null || usageStats.size() == 0) {
            return null;
        }

        RecentUseComparator mRecentComp = new RecentUseComparator();
        Collections.sort(usageStats, mRecentComp);

        return usageStats.get(0).getPackageName();
    }

    public boolean checkPackage(String packageName) {



        boolean returnResult = false;

        if (null != packageName) {
            canRun = dbTool.quaryPackageInfo(packageName)[0];
        }

        if ((!canRun) && !keyguardManager.inKeyguardRestrictedInputMode()) {
            CoreStatic.AccessibilityModeOnTheUnlock = false;
        }
        if (canRun || CoreStatic.AccessibilityModeOnTheUnlock || (dataManager.isDisableKeyGround() && keyguardManager.inKeyguardRestrictedInputMode())) {
            returnResult = true;
        } else if (dataManager.isUseDesktop() && null != packageName && packageName.equals(dataManager.getLauncherPackageName())) {
            returnResult = true;
        } else {
            returnResult = false;

            // Log.i("returnResult", returnResult + "" + packageName);

            if (null != packageName && !packageName.equals(dataManager.getLauncherPackageName()) && !packageName.equals("com.wocao.sherlock")) {

                // if (!packageName.equals(lastPackageName)) {
                //    Log.i("returnResult", returnResult + "//");
                backToDesktop();
                // }
                activityManager.killBackgroundProcesses(packageName);
            }
        }

        if (packageName != null)
            lastPackageName = packageName;

        //TimeTv.setText(packageName+"\t"+canRun);


        //   Log.i("lockservice+package+run", packageName + canRun);
        if (isAdminMode) {
            policyManager.lockNow();
        }


    //    writeInforToSdCard.write(new SimpleDateFormat("HH:mm:ss SSS ").format(new Date())+packageName+"\t"+returnResult+"\n");
        return returnResult;
    }

    private void backToDesktop() {
        context.startActivity(new Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_HOME).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED));
    }

    static class RecentUseComparator implements Comparator<UsageStats> {

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public int compare(UsageStats lhs, UsageStats rhs) {
            return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
        }
    }
}
