package com.wocao.sherlock.Permission;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.wocao.sherlock.Permission.FloatWindowPermission.FloatWindowManager;
import com.wocao.sherlock.Setting.SettingUtils;

/**
 * Created by silen on 17-6-9.
 */

public class OverlayPermissionDialog extends DialogFragment {
    Context context;
    public OverlayPermissionDialog(Context context) {
        super();
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("悬浮窗权限");
        builder.setMessage("抱歉，应用未被赋予悬浮窗权限，无法显示锁定界面，需开启。非原生类系统可能需要到手机中的安全管理软件内开启。\n\n如果无法开启，可使用开放桌面启动器方案");
        builder.setPositiveButton("开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                   /* Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:" + context.getPackageName()));
                    startActivityForResult(intent,0);*/
                FloatWindowManager.getInstance().applyOrShowFloatWindow(context);
            }
        });
        builder.setNeutralButton("桌面启动器", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SettingUtils.putBooleanValue(context,"setting_better_showDesktopApp",true);
                PolicyAdminUtils.checkDeviceAdmin(context,((AppCompatActivity)getActivity()).getSupportFragmentManager(),true);
            }
        });
        return builder.create();
    }
}
