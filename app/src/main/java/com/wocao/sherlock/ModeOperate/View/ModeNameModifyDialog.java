package com.wocao.sherlock.ModeOperate.View;

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

/**
 * Created by silen on 16-9-11.
 */

public class ModeNameModifyDialog extends DialogFragment {
    private ModeListDisplayDialog parentDialog;
    private LockMode lockMode;
    public ModeNameModifyDialog(ModeListDisplayDialog parentDialog,LockMode lockMode) {
        super();
        this.parentDialog=parentDialog;
        this.lockMode=lockMode;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(lockMode.getName());
        final EditText editText=new EditText(getContext());
        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setSingleLine(true);
        editText.setText(lockMode.getName());
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name=editText.getText().toString();
                if (name.equals("")) {
                    new ModeNameModifyDialog(parentDialog,lockMode).show(getActivity().getSupportFragmentManager(),null);
                } else {
                    lockMode.setName(name);
                    ModeListDBTool modeListDBTool=new ModeListDBTool(getContext());
                    modeListDBTool.updateOrCreateMode(lockMode.getId(),lockMode.getName(),lockMode.getCreateTime());
                    parentDialog.reflash();
                }
            }
        });
        builder.setNegativeButton("取消",null);
        return builder.create();
    }
}
