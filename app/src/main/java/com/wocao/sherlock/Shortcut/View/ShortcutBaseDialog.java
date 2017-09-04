package com.wocao.sherlock.Shortcut.View;

import android.support.v4.app.DialogFragment;

/**
 * Created by silen on 17-3-9.
 */

public class ShortcutBaseDialog extends DialogFragment {
    private OnDialogResultListener onDialogResultListener;
    public void setOnDialogResultListener(OnDialogResultListener onDialogResultListener){
        this.onDialogResultListener=onDialogResultListener;
    }
    public void returnResult(Object result){
        onDialogResultListener.onResult(result);
    }
}
