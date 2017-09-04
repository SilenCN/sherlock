package com.wocao.sherlock.Shortcut.View;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wocao.sherlock.DataBaseOperate.ModeListDBTool;
import com.wocao.sherlock.ModeOperate.Model.LockMode;
import com.wocao.sherlock.ModeOperate.View.ModeListDisplayDialog;
import com.wocao.sherlock.Shortcut.Model.ShortCut;

/**
 * Created by silen on 16-9-11.
 */

public class ShortcutNameModifyDialog extends ShortcutBaseDialog {
    ShortCut shortCut;

    public ShortcutNameModifyDialog(ShortCut shortCut) {
        super();
        this.shortCut=shortCut;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("修改名称");
        final EditText editText=new EditText(getContext());
        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setSingleLine(true);
        editText.setText(shortCut.getLabel());
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name=editText.getText().toString();
                if (name.equals("")) {
                    new ShortcutNameModifyDialog(shortCut).show(getActivity().getSupportFragmentManager(),null);
                } else {
                    returnResult(name);
                }
            }
        });
        builder.setNegativeButton("取消",null);
        return builder.create();
    }
}
