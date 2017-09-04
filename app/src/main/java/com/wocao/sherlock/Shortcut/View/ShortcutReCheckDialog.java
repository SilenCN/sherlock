package com.wocao.sherlock.Shortcut.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.wocao.sherlock.DataBaseOperate.ModeListDBTool;
import com.wocao.sherlock.Shortcut.Presenter.ShortcutItemViewHolder;

/**
 * Created by silen on 17-4-23.
 */

public class ShortcutReCheckDialog extends ShortcutBaseDialog {
    String duration;
    int modeID;

    public ShortcutReCheckDialog(String duration, int modeID) {
        super();
        this.duration = duration;
        this.modeID = modeID;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("任务确认");
        builder.setMessage("您选择的时间为" + duration + ",模式为" + new ModeListDBTool(getActivity()).getModeNameById(modeID)+",是否开启？");
        builder.setPositiveButton("开启", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                returnResult(null);
            }
        });
        builder.setNegativeButton("取消", null);
        return builder.create();
    }
}
