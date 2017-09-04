package com.wocao.sherlock.Permission;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.wocao.sherlock.Accessibility.AccessbilityTool;
import com.wocao.sherlock.R;

/**
 * Created by silen on 17-6-10.
 */

public class UsageStatsPermissionDialog extends DialogFragment {
    private Context context;

    public UsageStatsPermissionDialog(Context context) {
        this.context=context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.apptool_policyToAccessData);
        builder.setMessage(R.string.apptool_policyToAccessDataMessage);
        builder.setNegativeButton(R.string.apptool_policyToAccessDataAgree, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);

                context.startActivity(intent);
            }
        });
        builder.setNeutralButton(R.string.apptool_policyToAccessDataMessage_use_accessibility, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AccessbilityTool.checkedSetting(context);
            }
        });
        return builder.create();
    }
}
