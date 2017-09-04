package com.wocao.sherlock.Main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wocao.sherlock.Accessibility.AccessbilityTool;
import com.wocao.sherlock.AppConfig;
import com.wocao.sherlock.Permission.PolicyAdminUtils;
import com.wocao.sherlock.Permission.UsageStatsUtils;
import com.wocao.sherlock.R;

/**
 * Created by silen on 17-6-10.
 */

public class TimeReCheckDialog extends DialogFragment {
    private Context context;
    private int day;
    private int hour;

    private OnCheckedListener onCheckedListener;

    public TimeReCheckDialog(Context context,int day,int hour) {
        this.context=context;
        this.day=day;
        this.hour=hour;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // TODO: Implement this method
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.setTime_ReCheckTitle);
        builder.setMessage(R.string.setTime_ReCheckMessage);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.set_time_recheck_dialog_view, null);
        final EditText edit = (EditText) view.findViewById(R.id.setTimerecheckEditView);
        final TextView tv = (TextView) view.findViewById(R.id.setTimerecheckFocusTV);
        builder.setView(view);
        builder.setNegativeButton(R.string.setTime_GoOnCancel,null);

        builder.setPositiveButton(R.string.setTime_GoOnOK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface p1, int p2) {
                        tv.requestFocus();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        imm.showSoftInput(tv, InputMethodManager.SHOW_FORCED);

                        if (edit.getText() != null & !edit.getText().toString().equals("")) {
                            if (day + hour == Integer.parseInt(edit.getText().toString())) {
                                if (null!=onCheckedListener){
                                    onCheckedListener.onChecked();
                                }
                            } else {
                                Toast.makeText(getActivity(), R.string.setTime_Input_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.setTime_Input_void, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        return builder.create();
    }

    public TimeReCheckDialog setOnCheckedListener(OnCheckedListener onCheckedListener) {
        this.onCheckedListener = onCheckedListener;
        return this;
    }

    public interface OnCheckedListener{
        void onChecked();
    }

}
