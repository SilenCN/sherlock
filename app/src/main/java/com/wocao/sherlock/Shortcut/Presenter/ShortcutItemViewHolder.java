package com.wocao.sherlock.Shortcut.Presenter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.wocao.sherlock.ModeOperate.Model.LockMode;
import com.wocao.sherlock.R;
import com.wocao.sherlock.Shortcut.Model.ShortCut;
import com.wocao.sherlock.Shortcut.View.OnDialogResultListener;
import com.wocao.sherlock.Shortcut.View.ShortcutDeleteDialog;
import com.wocao.sherlock.Shortcut.View.ShortcutNameModifyDialog;
import com.wocao.sherlock.Shortcut.View.ShortcutTimeSetDialog;

import java.util.List;

/**
 * Created by silen on 17-3-9.
 */

public class ShortcutItemViewHolder {
    private TextView label;
    private TextView time;
    private Spinner modeSpinner;
    private CardView cardView;
    private Context context;
    private ShortCut shortCut;
    private ShortcutManager shortcutManager;
    List<LockMode> modeList;
    private View itemView;
    private FragmentManager fragmentManager;

    private OnItemDeleteListener onItemDeleteListener;
    private OnModifyListener onModifyListener;

    public ShortcutItemViewHolder(Context context, ShortCut shortCut, ShortcutManager shortcutManager,FragmentManager fragmentManager, List<LockMode> modeList) {
        this.context = context;
        this.shortCut = shortCut;
        this.shortcutManager = shortcutManager;
        this.modeList = modeList;
        this.fragmentManager=fragmentManager;
        findView();
        initView();
    }

    private void initView() {
        time.setText(getTimeStr(shortCut.getTime()));
        label.setText(getLabelStr(shortCut.getLabel()));
        spinnerOperate();
        setListener();
    }

    private void setListener() {
        cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ShortcutDeleteDialog deleteDialog=new ShortcutDeleteDialog();
                deleteDialog.show(fragmentManager,null);
                deleteDialog.setOnDialogResultListener(new OnDialogResultListener() {
                    @Override
                    public void onResult(Object result) {
                        if ((boolean)result){
                            shortcutManager.deleteShortcut(shortCut);
                            onItemDeleteListener.delete(shortCut);
                            onModifyListener.modify();
                        }
                    }
                });
                return true;
            }
        });
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shortCut.setModeId(getModeIdByPosition(position));
                shortcutManager.updateShortcut(shortCut);
                onModifyListener.modify();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShortcutNameModifyDialog nameModifyDialog=new ShortcutNameModifyDialog(shortCut);
                nameModifyDialog.show(fragmentManager,null);
                nameModifyDialog.setOnDialogResultListener(new OnDialogResultListener() {
                    @Override
                    public void onResult(Object result) {
                        String labelStr=(String) result;
                        shortCut.setLabel(labelStr);
                        label.setText(getLabelStr(labelStr));
                        shortcutManager.updateShortcut(shortCut);
                        onModifyListener.modify();
                    }
                });
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShortcutTimeSetDialog timeSetDialog=new ShortcutTimeSetDialog(shortCut.getTime());
                timeSetDialog.show(fragmentManager,null);
                timeSetDialog.setOnDialogResultListener(new OnDialogResultListener() {
                    @Override
                    public void onResult(Object result) {
                        shortCut.setTime((long)result);
                        time.setText(getTimeStr(shortCut.getTime()));
                        shortcutManager.updateShortcut(shortCut);
                        onModifyListener.modify();
                    }
                });
            }
        });
    }

    private void findView() {
        itemView = LayoutInflater.from(context).inflate(R.layout.shortcuts_list_item, null);
        label = (TextView) itemView.findViewById(R.id.ShortcutItemLabelTV);
        time = (TextView) itemView.findViewById(R.id.ShortcutItemTimeTV);
        modeSpinner = (Spinner) itemView.findViewById(R.id.ShortcutItemModeSpinner);
        cardView = (CardView) itemView.findViewById(R.id.ShortcutItemContain);
    }

    public View getView(){
        return itemView;
    }
    private void spinnerOperate() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item);
        for (LockMode lockMode : modeList) {
            adapter.add(lockMode.getName());
        }
        modeSpinner.setAdapter(adapter);
        modeSpinner.setSelection(getModePositionById(shortCut.getModeId()));
    }
    private int getModePositionById(int id) {
        for (int i = 0; i < modeList.size(); i++) {
            if (modeList.get(i).getId() == id) {
                return i;
            }
        }
        return 0;
    }
    private int getModeIdByPosition(int position){
        return modeList.get(position).getId();
    }
    public interface OnItemDeleteListener{
        void delete(ShortCut shortCut);
    }
    public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener){
        this.onItemDeleteListener=onItemDeleteListener;
    }
    public interface OnModifyListener{
        void modify();
    }
    public void setOnModifyListener(OnModifyListener onModifyListener){
        this.onModifyListener=onModifyListener;
    }
    private String timeCalculate(long i) {
        long length = i;
        String TvStr = " ";
        TvStr = " ";
        if (((int) length / (60 * 60 * 24)) > 0) {
            TvStr = (int) length / (60 * 60 * 24) + context.getResources().getString(R.string.killpoccesserve_day);
        }
        if (((int) (length % (60 * 60 * 24)) / (60 * 60)) > 0) {
            TvStr = TvStr + (int) (length % (60 * 60 * 24)) / (60 * 60) + "小"+context.getResources().getString(R.string.killpoccesserve_hour);
        }
        if ((int) (length % (60 * 60)) / 60 > 0) {
            TvStr = TvStr + (int) (length % (60 * 60)) / 60 + context.getResources().getString(R.string.killpoccesserve_min);
        }

        return TvStr;
    }

    private String getTimeStr(long time) {
        if (time == 0) {
            return "点击设置时间";
        } else {
            return timeCalculate(time);
        }
    }

    private String getLabelStr(String str) {
        if (null == str || str.equals("")) {
            return "点击设置名称";
        } else {
            return str;
        }
    }

}
