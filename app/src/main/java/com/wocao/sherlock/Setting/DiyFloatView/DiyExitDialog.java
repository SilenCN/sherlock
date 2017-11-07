package com.wocao.sherlock.Setting.DiyFloatView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.File;

/**
 * Created by silen on 17-10-7.
 */

public class DiyExitDialog extends DialogFragment {

    public DiyExitDialog() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("退出");
        builder.setMessage("是否退出自定义界面？");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.setNeutralButton("恢复默认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DisplayConf", Context.MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "background.jpg");
                if (file.exists()) {
                    file.delete();
                }
                Toast.makeText(getActivity(), "已恢复默认，请重新打开界面！", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });
        return builder.create();
    }

}
