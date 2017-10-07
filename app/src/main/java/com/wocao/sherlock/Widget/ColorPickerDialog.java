package com.wocao.sherlock.Widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wocao.sherlock.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by silen on 17-10-5.
 */

public class ColorPickerDialog extends DialogFragment {
    private Context context;
    private OnColorReturnListener listener;

    public ColorPickerDialog(Context context) {
        super();
        this.context = context;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        int[] colors = context.getResources().getIntArray(R.array.color_array);
        String[] colorNames = context.getResources().getStringArray(R.array.color_name_array);

        final List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < colors.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("colorName", colorNames[i]);
            map.put("color", colors[i]);
            list.add(map);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_color);
        View layoutView = LayoutInflater.from(context).inflate(R.layout.color_picker_dialog, null);
        builder.setView(layoutView);
        GridView gridView = (GridView) layoutView.findViewById(R.id.ColorPickerDialogGridLayout);
        gridView.setAdapter(new CircleColorViewAdapter(context, list, R.layout.color_picker_dialog_gridview_child_view, new String[]{"colorName", "color"}, new int[]{R.id.ColorPickerColorNameTextView, R.id.ColorPickerCircleColorView}));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != listener) {
                    listener.onReturn((String) list.get(position).get("colorName"), (int) list.get(position).get("color"));
                }
                dismiss();
            }
        });

        builder.setNegativeButton("取消", null);
        builder.setNeutralButton("恢复默认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (null != listener) {
                    listener.onDefault();
                }
            }
        });
        return builder.create();
    }

    public void setListener(OnColorReturnListener listener) {
        this.listener = listener;
    }

    public interface OnColorReturnListener {
        void onReturn(String colorName, int color);

        void onDefault();
    }
}
