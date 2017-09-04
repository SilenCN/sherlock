package com.wocao.sherlock.Accessibility;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.wocao.sherlock.R;

/**
 * Created by silen on 17-4-1.
 */

public class AccessibilityCheckDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.accessibility_dialog_title);
        builder.setMessage(R.string.accessibility_dialog_message);
        builder.setPositiveButton(R.string.accessibility_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
        });
        builder.setNegativeButton(R.string.accessibility_dialog_cancel,null);
        builder.setNeutralButton(R.string.accessibility_dialog_uncheck, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AccessbilityTool.unCheckSetting(getActivity());
            }
        });
        return builder.create();
    }
}
