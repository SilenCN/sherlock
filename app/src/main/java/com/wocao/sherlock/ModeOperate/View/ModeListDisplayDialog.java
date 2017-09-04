package com.wocao.sherlock.ModeOperate.View;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wocao.sherlock.DataBaseOperate.ModeListDBTool;
import com.wocao.sherlock.ModeOperate.Activity.AppWhiteList;
import com.wocao.sherlock.ModeOperate.Model.LockMode;

import java.util.List;

/**
 * Created by silen on 16-8-30.
 */

public class ModeListDisplayDialog extends DialogFragment {
    ArrayAdapter<String> adapter;
    List<LockMode> listData;
    ModeListDisplayDialog ThisDialog=this;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("模式");
        ListView listView = new ListView(getContext());
        listView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        builder.setView(listView);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);

        getData();

        listView.setAdapter(adapter);
        builder.setPositiveButton("新增", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new NewModeDisplayDialog().show(getActivity().getSupportFragmentManager(), null);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listData.get(position).getId()!=1) {
                    startActivity(new Intent(getContext(), AppWhiteList.class).putExtra("LockMode", listData.get(position)));
                }else {
                    new StrengthModeAlarmDialog().show(getActivity().getSupportFragmentManager(),null);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 1) {
                    new ModeModifyDisplayDialog(listData.get(position), true,ThisDialog).show(getActivity().getSupportFragmentManager(), null);
                    return true;
                } else {
                    return false;
                }
            }
        });
        return builder.create();
    }

    public void reflash() {
        getData();
        adapter.notifyDataSetChanged();
    }

    private void getData() {
        ModeListDBTool modeListDBTool = new ModeListDBTool(getContext());
        listData = modeListDBTool.getAllModeListData();
        adapter.clear();
        for (LockMode lockMode : listData) {
            adapter.add(lockMode.getName());
        }
    }
}
