package com.wocao.sherlock.ModeOperate.View;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.wocao.sherlock.R;

/**
 * Created by silen on 16-9-25.
 */

public class StrengthModeAlarmDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.setTime_strengthenDialogAlarm_title);
        builder.setMessage(R.string.modedisplay_strengthenDialogAlarm_message);

        builder.setNegativeButton(R.string.setTime_strengthenDialogAlarm_cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

}
