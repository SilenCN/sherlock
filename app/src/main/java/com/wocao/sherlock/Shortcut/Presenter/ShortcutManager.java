package com.wocao.sherlock.Shortcut.Presenter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;

import com.wocao.sherlock.DataBaseOperate.ShortcutDBTool;
import com.wocao.sherlock.Main.MainActivity;
import com.wocao.sherlock.R;
import com.wocao.sherlock.Shortcut.Model.ShortCut;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by silen on 17-3-8.
 */
@TargetApi(Build.VERSION_CODES.N_MR1)
public class ShortcutManager {
    private ShortcutDBTool shortcutDBTool;
    private List<ShortCut> list=new ArrayList<>();
    private List<ShortcutInfo> infoList=new ArrayList<>();
    private android.content.pm.ShortcutManager shortcutManager;
    private Context context;
    public ShortcutManager(Context context, Activity activity) {
        this.context=context;
        shortcutDBTool=new ShortcutDBTool(context);
        list=shortcutDBTool.getAllShortcutData();
        this.shortcutManager=(android.content.pm.ShortcutManager) activity.getApplication().getSystemService(android.content.pm.ShortcutManager.class);
    }

    public List<ShortCut> getList(){
        return this.list;
    }
    private void updateShortcut(){
        convertShortcutInfo();
        shortcutManager.removeAllDynamicShortcuts();
        shortcutManager.addDynamicShortcuts(infoList);
    }
    public void deleteShortcut(ShortCut shortCut){
        shortcutDBTool.deleteShortcut(shortCut.getId());
        list.remove(shortCut);
        updateShortcut();
    }
    public void addShortcut(){
        ShortCut shortCut=new ShortCut();
        shortCut.setId(list.size()==0?0:list.get(list.size()-1).getId()+1);
        shortcutDBTool.updateOrCreateShortcut(shortCut);
        list.add(shortCut);
        updateShortcut();
    }
    public void updateShortcut(ShortCut shortCut){
/*        int position=0;
        for(;position<list.size();position++){
            if (list.get(position).getId()==shortCut.getId()){
                list.remove(position);
                list.add(position,shortCut);
                break;
            }
        }*/
        shortcutDBTool.updateOrCreateShortcut(shortCut);
        updateShortcut();
    }
    private void convertShortcutInfo(){
        infoList.clear();
        for (ShortCut shortCut:list){
            if (shortCut.getTime()==0){
                continue;
            }
            String label=shortCut.getLabel()==null||shortCut.getLabel().equals("")?"无标题":shortCut.getLabel();
            ShortcutInfo info=new ShortcutInfo.Builder(context, ""+shortCut.getId())
                    .setShortLabel(label)
                    .setLongLabel(label)
                    .setIcon(Icon.createWithResource(context, R.mipmap.shortcut_icon))
                    .setIntent(new Intent(Intent.ACTION_MAIN, Uri.EMPTY,context,MainActivity.class).putExtra("Shortcut",true).putExtra("time",shortCut.getTime()).putExtra("modeId",shortCut.getModeId()))
                    .build();
            infoList.add(info);
        }

    }
}
