package com.wocao.sherlock.Permission;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.wocao.sherlock.R;

/**
 * Created by silen on 17-6-9.
 */

public class PolicyAdminDialog extends DialogFragment {
    private Context context;
    private ComponentName componentName;
    public PolicyAdminDialog(Context context,ComponentName componentName) {
        this.componentName=componentName;
        this.context=context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.apptool_relifeDeviceAdmin);
        builder.setMessage(R.string.apptool_relifeDeviceAdminMessage);
        builder.setNegativeButton(R.string.apptool_relifeDeviceAdminAgree, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               PolicyAdminUtils.activePolicyAdmin(context,componentName);
                //  activeManager(componentName);// 激活设备管理器获取权限
            }
        });
        return builder.create();
    }
}
