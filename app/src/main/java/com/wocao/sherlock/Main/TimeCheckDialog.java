package com.wocao.sherlock.Main;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import com.wocao.sherlock.R;

/**
 * Created by silen on 17-6-10.
 */

public class TimeCheckDialog extends DialogFragment {
    private OnCheckedListener onCheckedListener;
    private String timeString;
    public TimeCheckDialog(String timeString) {
        this.timeString=timeString;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.setTime_GoOn);
        builder.setMessage(String.format(getResources().getString(R.string.setTime_GoNoMessage), new Object[]{timeString}));
        builder.setNegativeButton(R.string.setTime_GoOnCancel, null);

        builder.setPositiveButton(R.string.setTime_GoOnOK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        if (null!=onCheckedListener){
                            onCheckedListener.checked();
                        }
                    }
                }
        );
        return builder.create();
    }

    public TimeCheckDialog setOnCheckedListener(OnCheckedListener onCheckedListener) {
        this.onCheckedListener = onCheckedListener;
        return this;
    }


    public interface OnCheckedListener{
        void checked();
    }
}
