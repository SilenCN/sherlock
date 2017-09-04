package com.wocao.sherlock.ModeOperate.View;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wocao.sherlock.DataBaseOperate.ModeListDBTool;
import com.wocao.sherlock.ModeOperate.Activity.AppWhiteList;
import com.wocao.sherlock.ModeOperate.Model.LockMode;

/**
 * Created by silen on 16-9-9.
 */

public class NewModeDisplayDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("新增");
        final SharedPreferences sp=getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        final int id=sp.getInt("MaxLockModeID",2);

        final EditText editText = new EditText(getContext());
        editText.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setSingleLine(true);
        editText.setText("模式"+id);
        builder.setView(editText);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name=editText.getText().toString();
                if (name.equals("")) {
                    new NewModeDisplayDialog().show(getActivity().getSupportFragmentManager(),null);
                } else {
                    sp.edit().putInt("MaxLockModeID",id+1).commit();
                    LockMode lockMode=new LockMode();
                    lockMode.setId(id);
                    lockMode.setName(name);
                    lockMode.setCreateTime(System.currentTimeMillis());
                    lockMode.setNew(true);
                    ModeListDBTool modeListDBTool=new ModeListDBTool(getContext());
                    modeListDBTool.updateOrCreateMode(lockMode.getId(),lockMode.getName(),lockMode.getCreateTime());
                    startActivity(new Intent(getActivity(), AppWhiteList.class).putExtra("LockMode",lockMode));
                }
            }
        });
        builder.setNegativeButton("取消",null);
        return builder.create();
    }
}
