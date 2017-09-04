package com.wocao.sherlock.ModeOperate.View;

import android.app.Dialog;
import android.content.DialogInterface;
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
import com.wocao.sherlock.ModeOperate.Model.LockMode;

import java.util.List;

/**
 * Created by silen on 16-9-11.
 */

public class ModeSelectDialog extends DialogFragment {
    ArrayAdapter<String> adapter;
    List<LockMode> listData;
    ModeSelectDialog ThisDialog=this;
    OnLockModeSelectedListener onLockModeSelectedListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("选择模式");
        ListView listView = new ListView(getContext());
        listView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        builder.setView(listView);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);

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
                onLockModeSelectedListener.onSelected(listData.get(position));
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
    public interface OnLockModeSelectedListener {
        void onSelected(LockMode lockMode);
    }
    public void setOnLockModeSelectedListener(OnLockModeSelectedListener onLockModeSelectedListener){
        this.onLockModeSelectedListener=onLockModeSelectedListener;
    }
}
