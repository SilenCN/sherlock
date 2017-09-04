package com.wocao.sherlock.ModeOperate.Server;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.wocao.sherlock.AppConfig;
import com.wocao.sherlock.DataBaseOperate.AppWhiteDBTool;
import com.wocao.sherlock.ModeOperate.Activity.AppWhiteList;
import com.wocao.sherlock.R;
import com.wocao.sherlock.appTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by silen on 16-9-17.
 */

public class InitMode extends AsyncTask<String, Integer, String> {
    int le = 0;
  //  ProgressDialog pdialog;
    Context context;
    List<PackageInfo> applist = new ArrayList<PackageInfo>();
    List<Map<String, Object>> listviewList = new ArrayList<Map<String, Object>>();
    ArrayList<Map<String, Object>> listviewListTemp = new ArrayList<Map<String, Object>>();
    AppWhiteDBTool appWhiteDBTool, appWhiteDBTool2;

    public InitMode(Context context) {
        this.context = context;
        appWhiteDBTool = new AppWhiteDBTool(context, 0);
        appWhiteDBTool2 = new AppWhiteDBTool(context, 1);

/*      pdialog = new ProgressDialog(context, 0);
        pdialog.setCanceledOnTouchOutside(false);

        pdialog.setTitle(R.string.AppCanOpen_loading);
        pdialog.setMessage(context.getResources().getString(R.string.AppCanOpen_loadingMessage));

        pdialog.setCancelable(true);

        pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pdialog.show();*/
    }

    @Override
    protected String doInBackground(String[] p1) {
        PackageManager pm = context.getPackageManager();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        applist = pm.getInstalledPackages(0);

        appTool.getHomes(context);

        le = applist.size();
     //   pdialog.setMax(le);
        le = 0;
        int listRunToken = 0, listOpenToken = 0, listNoneToken = 0;
        for (PackageInfo pi : applist) {
            ApplicationInfo ai;
            ai = pi.applicationInfo;

            for (String p : context.getResources().getStringArray(R.array.open_app_package)) {
                if (pi.packageName.indexOf(p) != -1) {
                    appWhiteDBTool.updateOrCreatePackageInfo(pi.packageName, true);
                    appWhiteDBTool2.updateOrCreatePackageInfo(pi.packageName, true);

                }
            }

            boolean isHomePackage = false;
            for (String p : appTool.getAllHomes(context)) {
                if (pi.packageName.indexOf(p) != -1) {
                    appWhiteDBTool.updateOrCreatePackageInfo(pi.packageName, false);
                    appWhiteDBTool2.updateOrCreatePackageInfo(pi.packageName, false);
                    isHomePackage = true;
                    break;
                }

            }
            if (!isHomePackage)
                if (pi.packageName.equals("com.wocao.sherlock") || pi.packageName.equals("com.wocao.sherlockassist") || pi.packageName.equals("com.android.settings")) {
                    appWhiteDBTool.updateOrCreatePackageInfo(pi.packageName, false);
                    appWhiteDBTool.updateOrCreatePackageInfo("com.wocao.sherlockassist", true);
                    appWhiteDBTool2.updateOrCreatePackageInfo(pi.packageName, false);
                    appWhiteDBTool2.updateOrCreatePackageInfo("com.wocao.sherlockassist", true);

                } else if (pi.packageName.equals(AppConfig.homesPackage)) {
                    appWhiteDBTool.updateOrCreatePackageInfo(AppConfig.homesPackage, false);
                    appWhiteDBTool2.updateOrCreatePackageInfo(AppConfig.homesPackage, false);
                }
            le++;
            publishProgress(le);
        }
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        sp.edit().putBoolean("HaveAppCanRun" + 0, false).commit();
        sp.edit().putBoolean("HaveAppCanRun" + 1, false).commit();

        return null;
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
      //  pdialog.dismiss();
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer[] values) {
        super.onProgressUpdate(values);
       // pdialog.setProgress(values[0]);
    }
}

