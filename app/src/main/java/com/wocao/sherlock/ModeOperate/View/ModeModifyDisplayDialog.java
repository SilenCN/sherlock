package com.wocao.sherlock.ModeOperate.View;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.wocao.sherlock.DataBaseOperate.AlarmDBTool;
import com.wocao.sherlock.DataBaseOperate.AppWhiteDBTool;
import com.wocao.sherlock.DataBaseOperate.ModeListDBTool;
import com.wocao.sherlock.ModeOperate.Activity.AppWhiteList;
import com.wocao.sherlock.ModeOperate.Model.LockMode;

/**
 * Created by silen on 16-9-11.
 */

public class ModeModifyDisplayDialog extends DialogFragment {
    private LockMode lockMode;
    private boolean isOperate;
    private DialogFragment ThisDialog;
    private ModeListDisplayDialog parentDialog;
    public ModeModifyDisplayDialog(LockMode lockMode, boolean isOperate,ModeListDisplayDialog parentDialog) {
        super();
        this.lockMode = lockMode;
        this.isOperate = isOperate;
        this.ThisDialog = this;
        this.parentDialog=parentDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(lockMode.getName());
        ListView listView = new ListView(getContext());
        listView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1);
        if (isOperate) {
            adapter.add("编辑");
        } else {
            adapter.add("白名单");
        }
        adapter.add("删除");
        listView.setAdapter(adapter);
        builder.setView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (isOperate) {
                            new ModeNameModifyDialog(parentDialog,lockMode).show(getActivity().getSupportFragmentManager(),null);
                        } else {
                            startActivity(new Intent(getContext(), AppWhiteList.class).putExtra("LockMode", lockMode));
                        }
                        ThisDialog.dismiss();
                        break;
                    case 1:
                        new ModeListDBTool(getContext()).deleteMode(lockMode.getId());
                        new AlarmDBTool(getContext()).updateAlarmIfModeDelete(lockMode.getId());
                        parentDialog.reflash();
                        ThisDialog.dismiss();
                        break;
                }
            }
        });
        builder.setNegativeButton("取消", null);

        return builder.create();
    }
}
