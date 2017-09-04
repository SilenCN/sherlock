package com.wocao.sherlock.ForceUnlock;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by silen on 17-4-5.
 */

public class RandomHelpDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("帮助");
        builder.setMessage("强制解锁密码设置后可在锁定界面按住滑动条右滑即可，进入界面输入密码解锁。\n\n随机密码较复杂，建议用笔记下！");
        builder.setPositiveButton("知道了",null);
        return builder.create();
    }
}
