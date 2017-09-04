package com.wocao.sherlock.Shortcut.Presenter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wocao.sherlock.DataBaseOperate.ModeListDBTool;
import com.wocao.sherlock.ModeOperate.Model.LockMode;
import com.wocao.sherlock.Shortcut.Model.ShortCut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silen on 17-3-9.
 */

public class ShortcutListViewAdapter extends BaseAdapter {
    private Context context;
    private FragmentManager fragmentManager;
    private ShortcutManager shortcutManager;
    private List<ShortCut> list;
    private List<LockMode> modeList;
    private List<View> viewList;
    private OnNumberChangeListener onNumberChangeListener;
    private boolean isModify=false;
    public ShortcutListViewAdapter(Context context, AppCompatActivity activity) {
        super();

        this.fragmentManager = activity.getSupportFragmentManager();
        this.context = context;
        this.shortcutManager = new ShortcutManager(context, activity);
        list = shortcutManager.getList();
        modeList = new ModeListDBTool(context).getAllModeListData();
        viewList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        convertView = null;

        ShortcutItemViewHolder holder;
        //     if (null == convertView) {
        holder = new ShortcutItemViewHolder(context, list.get(position), shortcutManager, fragmentManager, modeList);
        //      convertView = holder.getView();
        //      convertView.setTag(holder);
        //  } else {
        //       holder = (ShortcutItemViewHolder) convertView.getTag();
        //   }

        holder.setOnItemDeleteListener(new ShortcutItemViewHolder.OnItemDeleteListener() {

            @Override
            public void delete(ShortCut shortCut) {

                notifyDataSetInvalidated();

                onNumberChangeListener.num(getCount());

            }
        });
        holder.setOnModifyListener(new ShortcutItemViewHolder.OnModifyListener() {
            @Override
            public void modify() {
                isModify=true;
            }
        });
        return holder.getView();
        // return convertView;
    }

    public void addNewShortcut() {
        shortcutManager.addShortcut();
        onNumberChangeListener.num(getCount());
        this.notifyDataSetChanged();
        isModify=true;
    }


    public interface OnNumberChangeListener {
        void num(int num);
    }

    public void setOnNumberChangeListener(OnNumberChangeListener onNumberChangeListener) {
        this.onNumberChangeListener = onNumberChangeListener;
    }

    public boolean isModify(){
        return isModify;
    }
    public boolean save(){

        return false;
    }
}
