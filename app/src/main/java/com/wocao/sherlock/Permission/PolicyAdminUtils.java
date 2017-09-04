package com.wocao.sherlock.Permission;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.wocao.sherlock.AppConfig;
import com.wocao.sherlock.adminReceiver;

/**
 * Created by silen on 17-6-9.
 */

public class PolicyAdminUtils {
    public static void activePolicyAdmin(Context context, ComponentName componentName) {
        // 使用隐式意图调用系统方法来激活指定的设备管理器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "锁屏");
        context.startActivity(intent);
    }

    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context, adminReceiver.class);
    }

    public boolean checkDeviceAdmin(Context context) {
        DevicePolicyManager policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context, adminReceiver.class);

        if (policyManager.isAdminActive(componentName) || AppConfig.ignoreDeviceAdmin) {
            return true;
        } else if (!policyManager.isAdminActive(componentName)) {
            return false;
        }
        return true;
    }
    public static boolean checkDeviceAdmin(Context context, android.support.v4.app.FragmentManager fragmentManager,boolean isStrengthMode) {
        DevicePolicyManager policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context, adminReceiver.class);

        if (policyManager.isAdminActive(componentName) || !isStrengthMode) {
            return true;
        } else if (!policyManager.isAdminActive(componentName)) {
            new PolicyAdminDialog(context, componentName).show(fragmentManager, null);
            return false;
        }
        return true;
    }

    public static boolean checkDeviceAdmin(Context context, android.support.v4.app.FragmentManager fragmentManager) {
        DevicePolicyManager policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(context, adminReceiver.class);

        if (policyManager.isAdminActive(componentName) || AppConfig.ignoreDeviceAdmin) {
            return true;
        } else if (!policyManager.isAdminActive(componentName)) {
            new PolicyAdminDialog(context, componentName).show(fragmentManager, null);
            return false;
        }
        return true;
    }
    public static boolean isAdminActive(Context context) {
        //  this.context = context;
       DevicePolicyManager  policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
       ComponentName componentName = new ComponentName(context, adminReceiver.class);

        if (policyManager.isAdminActive(componentName)) {
            return true;
        } else {
            return false;
        }

    }
}
