package com.wocao.sherlock.CoreService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.*;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wocao.sherlock.AESUtils;
import com.wocao.sherlock.DataBaseOperate.AppWhiteDBTool;
import com.wocao.sherlock.R;
import com.wocao.sherlock.Widget.BottomCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by silen on 17-9-12.
 */

public class FloatWindowAppList {
    Context context;
    BottomCard bottomCard;
    LinearLayout layoutLine1, layoutLine2;
    AppWhiteDBTool appWhiteDBTool;
    List<Map<String, Object>> listviewList = new ArrayList<Map<String, Object>>();

    android.content.pm.PackageManager pm;
    int lockModeId = 0;

    SharedPreferences sharedPreferences;

    private int contentNumber = 0;

    private boolean doubleLineDisplay = false;

    public FloatWindowAppList(Context context, BottomCard bottomCard, int lockModeId) {
        this.bottomCard = bottomCard;
        this.context = context;
        this.lockModeId = lockModeId;
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        initAppList();
    }

    private void initAppList() {
        pm = context.getPackageManager();
        layoutLine1 = (LinearLayout) bottomCard.getContentView().findViewById(R.id.BottomCardLayoutLine1);
        layoutLine2 = (LinearLayout) bottomCard.getContentView().findViewById(R.id.BottomCardLayoutLine2);
        updateLockModeId(lockModeId);

    }

    public void updateLockModeId(int lockModeId) {
        listviewList.clear();
        contentNumber=0;
        layoutLine1.removeAllViews();
        layoutLine2.removeAllViews();
        try {
            if (sharedPreferences.getBoolean(AESUtils.encrypt("wocstudiosoftware", "useCipher"), false)) {
                View view = LayoutInflater.from(context).inflate(R.layout.float_view_applist_gradview_item, null);
                ((ImageView) view.findViewById(R.id.gridviewchildviewImageView)).setImageResource(R.mipmap.unlock_icon);
                ((TextView) view.findViewById(R.id.gridviewchildviewTextView)).setText("强制解锁");
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomCard.strengthClose();
                        Intent i = new Intent();
                        ComponentName cn = new ComponentName("com.wocao.sherlockassist", "com.wocao.sherlockassist.InputCipher");
                        i.setComponent(cn);
                        i.setAction("android.intent.action.MAIN");
                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        i.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        CoreStatic.AccessibilityModeOnTheUnlock = true;
                    }
                });
                layoutLine1.addView(view);
                contentNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        appWhiteDBTool = new AppWhiteDBTool(context, lockModeId);

        List list = appWhiteDBTool.quaryAllPackageCanOpen();

        System.out.println(list);

        if (contentNumber + list.size() > 7) {
            doubleLineDisplay = true;
            layoutLine2.setVisibility(View.VISIBLE);
        } else {
            doubleLineDisplay = false;
            layoutLine2.setVisibility(View.INVISIBLE);
        }

        for (final Object packageName : list) {
            try {
                ApplicationInfo ai = pm.getApplicationInfo((String) packageName, 0);
                Drawable draw = ai.loadIcon(context.getPackageManager());
                if ((pm.getLaunchIntentForPackage((String) packageName) != null)) {
                    View view = LayoutInflater.from(context).inflate(R.layout.float_view_applist_gradview_item, null);
                    ((ImageView) view.findViewById(R.id.gridviewchildviewImageView)).setImageDrawable(draw);
                    ((TextView) view.findViewById(R.id.gridviewchildviewTextView)).setText(ai.loadLabel(pm));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomCard.strengthClose();
                            context.startActivity(pm.getLaunchIntentForPackage((String) packageName)/*.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)*/);
                            CoreStatic.AccessibilityModeOnTheUnlock = true;
                        }
                    });
                    contentNumber++;
                    if (doubleLineDisplay) {
                        if (contentNumber % 2 == 0) {
                            layoutLine2.addView(view);
                        } else {
                            layoutLine1.addView(view);
                        }
                    } else {
                        layoutLine1.addView(view);
                    }

                }
            } catch (Exception e) {
            }
        }
    }

    public int getContentNumber() {
        return contentNumber;
    }
}
