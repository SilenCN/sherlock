package com.wocao.sherlock.ControlByOther.View;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wocao.sherlock.ControlByOther.Presenter.ControlByOtherDialogManager;
import com.wocao.sherlock.R;
import com.wocao.sherlock.Shortcut.View.ShortcutBaseDialog;

/**
 * Created by silen on 17-4-25.
 */

public class PasswordInputDialog extends ShortcutBaseDialog {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("输入密码");
        builder.setMessage("已开启他人控制，请输入密码进入");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.control_by_other_password_input_view,null);

        final EditText editText = (EditText) view.findViewById(R.id.ControlByOtherPasswordET);
        final TextView focusTV = (TextView) view.findViewById(R.id.ControlByOtherFocusTV);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                focusTV.requestFocus();
                if (null != editText.getText() && null != editText.getText().toString() && !editText.getText().toString().equals("")) {
                    String inputResult = editText.getText().toString().trim();
                    if (inputResult.equals(new ControlByOtherDialogManager(getActivity()).getPassword())) {
                        returnResult(true);
                    } else {
                        Toast.makeText(getActivity(), "密码输入错误", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton("取消", null);

        return builder.create();
    }

    @Override
    public void returnResult(Object result) {
        super.returnResult(result);
    }
}
