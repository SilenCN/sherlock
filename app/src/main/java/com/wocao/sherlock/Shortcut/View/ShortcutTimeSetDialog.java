package com.wocao.sherlock.Shortcut.View;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.wocao.sherlock.R;

/**
 * Created by silen on 17-3-11.
 */

public class ShortcutTimeSetDialog extends ShortcutBaseDialog {
    private long time;
    public ShortcutTimeSetDialog(long time){
        this.time=time;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("设置时间");
        final View layout1 = LayoutInflater.from(getActivity()).inflate(R.layout.time_set_cutdown_view, null);

        final NumberPicker numberPickerDay = (NumberPicker) layout1.findViewById(R.id.TimeSetCutdownNumberPickerDay);
        final NumberPicker numberPickerHour = (NumberPicker) layout1.findViewById(R.id.TimeSetCutdownNumberPickerHour);
        final NumberPicker numberPickerMin = (NumberPicker) layout1.findViewById(R.id.TimeSetCutdownNumberPickerMin);
        numberPickerDay.setMinValue(0);
        numberPickerDay.setMaxValue(15);//15
        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(23);
        numberPickerMin.setMinValue(0);
        numberPickerMin.setMaxValue(59);

        numberPickerDay.setValue((int)time/(24*60*60));
        numberPickerHour.setValue((int) (time % (60 * 60 * 24)) / (60 * 60));
        numberPickerMin.setValue((int) (time % (60 * 60)) / 60);

        builder.setView(layout1);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long time=(numberPickerDay.getValue()*24*60+numberPickerHour.getValue()*60+numberPickerMin.getValue())*60;
                returnResult(time);
            }
        });
        builder.setNegativeButton("取消",null);


        return builder.create();
    }
}
