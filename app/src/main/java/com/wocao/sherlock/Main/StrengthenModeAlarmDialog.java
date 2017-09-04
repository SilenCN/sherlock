package com.wocao.sherlock.Main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.wocao.sherlock.R;

/**
 * Created by silen on 17-6-9.
 */

public class StrengthenModeAlarmDialog extends DialogFragment {
    private OnResultListener onResultListener;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.setTime_strengthenDialogAlarm_title);
        builder.setMessage(R.string.setTime_strengthenDialogAlarm_message);
        builder.setPositiveButton(R.string.setTime_strengthenDialogAlarm_known, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (null!=onResultListener)
                    onResultListener.onClick();
            }
        });
        builder.setNegativeButton(R.string.setTime_strengthenDialogAlarm_cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    public interface OnResultListener{
        void onClick();
    }

}
