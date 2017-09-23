package com.wocao.sherlock.CoreService;

import android.content.Context;
import android.content.Intent;
import android.content.pm.*;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    LinearLayout layout;
    AppWhiteDBTool appWhiteDBTool;
    List<Map<String, Object>> listviewList = new ArrayList<Map<String, Object>>();

    android.content.pm.PackageManager pm;
    int lockModeId = 0;

    public FloatWindowAppList(Context context, BottomCard bottomCard, int lockModeId) {
        this.bottomCard = bottomCard;
        this.context = context;
        this.lockModeId = lockModeId;
        initAppList();
    }

    private void initAppList() {
        pm = context.getPackageManager();
        layout = (LinearLayout) bottomCard.getContentView().findViewById(R.id.BottomCardLayout);

        updateLockModeId(lockModeId);

        //   updateLockModeId(lockModeId);
    }

    public void updateLockModeId(int lockModeId) {
        listviewList.clear();
        appWhiteDBTool = new AppWhiteDBTool(context, lockModeId);

        List list = appWhiteDBTool.quaryAllPackageCanOpen();

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
                            bottomCard.openOrClose();
                            context.startActivity(pm.getLaunchIntentForPackage((String) packageName).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    });
                    layout.addView(view);
                }
            } catch (Exception e) {
            }
        }
        appWhiteDBTool.closeDB();
    }

}
