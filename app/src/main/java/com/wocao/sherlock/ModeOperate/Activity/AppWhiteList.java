package com.wocao.sherlock.ModeOperate.Activity;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wocao.sherlock.AppConfig;
import com.wocao.sherlock.DataBaseOperate.AppWhiteDBTool;
import com.wocao.sherlock.ExitApplication;
import com.wocao.sherlock.ModeOperate.Model.LockMode;
import com.wocao.sherlock.ModeOperate.View.ModeHelpDialog;
import com.wocao.sherlock.R;
import com.wocao.sherlock.Setting.SettingUtils;
import com.wocao.sherlock.appTool;
import com.wocao.sherlock.MaterialDesign.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppWhiteList extends AppCompatActivity {

    AppWhiteDBTool appWhiteDBTool;
    MyAdapter myAdapter;
    ListView lv;
    //ImageButton iButton;
    PackageManager pm;
    ActivityManager am;
    List<PackageInfo> applist = new ArrayList<PackageInfo>();
    List<Map<String, Object>> listviewList = new ArrayList<Map<String, Object>>();
    ArrayList<Map<String, Object>> listviewListTemp = new ArrayList<Map<String, Object>>();

    SharedPreferences sp;

    Task task;
    Toolbar mToolBar;
    int CanOpenAppCount = 0;

    LockMode lockMode;
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_white_view);

        new StatusBarUtils().setStatusBar(this);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        } catch (Exception e) {
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lockMode = (LockMode) getIntent().getSerializableExtra("LockMode");

        getSupportActionBar().setTitle(getSupportActionBar().getTitle() + "-" + lockMode.getName());

        sp = getSharedPreferences("data", MODE_PRIVATE);

        appWhiteDBTool = new AppWhiteDBTool(AppWhiteList.this, lockMode.getId());

        lv = (ListView) findViewById(R.id.AppWhiteViewListView);
        lv.setDividerHeight(0);
        task = new Task(AppWhiteList.this);
        task.execute("");

        ExitApplication.getInstance().addActivity(this);

    }

    private class Task extends AsyncTask<String, Integer, String> {
        int le = 0;
        ProgressDialog pdialog;

        public Task(Context context) {
            pdialog = new ProgressDialog(context, 0);
            pdialog.setCanceledOnTouchOutside(false);

            pdialog.setTitle(R.string.AppCanOpen_loading);
            pdialog.setMessage(getResources().getString(R.string.AppCanOpen_loadingMessage));

            pdialog.setCancelable(true);

            pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pdialog.show();
        }

        @Override
        protected String doInBackground(String[] p1) {
            boolean showDesktopApp = SettingUtils.getBooleanValue(AppWhiteList.this, "setting_better_showDesktopApp", false);//
            // PreferenceManager.getDefaultSharedPreferences(AppWhiteList.this).getBoolean("setting_better_showDesktopApp", false);
            pm = getPackageManager();
            am = (ActivityManager) getApplication().getSystemService(getApplication().ACTIVITY_SERVICE);
            applist = pm.getInstalledPackages(0);

            appTool.getHomes(AppWhiteList.this);

            le = applist.size();

            pdialog.setMax(le);
            le = 0;
            int listRunToken = 0, listOpenToken = 0, listNoneToken = 0;
            int listRunTokenTemp = 0, listOpenTokenTemp = 0, listNoneTokenTemp = 0;

            for (PackageInfo pi : applist) {
                ApplicationInfo ai;
                ai = pi.applicationInfo;

                for (String p : getResources().getStringArray(R.array.open_app_package)) {
                    if (pi.packageName.indexOf(p) != -1) {
                        appWhiteDBTool.updateOrCreatePackageInfo(pi.packageName, true);
                    }
                }

                boolean isHomePackage = false;
                if (!showDesktopApp)
                    for (String p : appTool.getAllHomes(AppWhiteList.this)) {
                        if (pi.packageName.indexOf(p) != -1) {
                            appWhiteDBTool.updateOrCreatePackageInfo(pi.packageName, false);
                            isHomePackage = true;
                            break;
                        }
                    }
                if (!isHomePackage)
                    if (pi.packageName.equals("com.wocao.sherlock") || pi.packageName.equals("com.wocao.sherlockassist") || pi.packageName.equals("com.android.settings")) {
                        appWhiteDBTool.updateOrCreatePackageInfo(pi.packageName, false);
                        appWhiteDBTool.updateOrCreatePackageInfo("com.wocao.sherlockassist", true);

                    } else if (!showDesktopApp && pi.packageName.equals(AppConfig.homesPackage)) {
                        appWhiteDBTool.updateOrCreatePackageInfo(AppConfig.homesPackage, false);
                    } else {

                        String appname = pm.getApplicationLabel(ai).toString();

                        Drawable draw = ai.loadIcon(getPackageManager());
                        Map<String, Object> map = new HashMap<String, Object>();

                        map.put("AppIcon", draw);
                        map.put("AppName", appname);

                        map.put("AppPackage", pi.packageName);


                        boolean PackageInfo[] = appWhiteDBTool.quaryPackageInfo(pi.packageName);
                        map.put("AppCanRun", PackageInfo[0]);
                        map.put("AppCanOpen", PackageInfo[1]);

                        if (pm.getLaunchIntentForPackage(pi.packageName) != null) {

                            map.put("HasLaunch", true);

                            if (PackageInfo[1]) {
                                CanOpenAppCount++;
                                listviewList.add(listOpenToken, map);
                                listOpenToken++;
                            } else if (PackageInfo[0]) {
                                listviewList.add(listOpenToken + listRunToken, map);
                                listRunToken++;
                            } else {
                                listviewList.add(listNoneToken + listOpenToken + listRunToken, map);
                                listNoneToken++;
                            }
                        } else {
                            map.put("HasLaunch", false);
                            if (PackageInfo[1]) {
                                listviewListTemp.add(listOpenTokenTemp, map);
                                listOpenTokenTemp++;
                            } else if (PackageInfo[0]) {
                                listviewListTemp.add(listOpenTokenTemp + listRunTokenTemp, map);
                                listRunTokenTemp++;
                            } else {
                                listviewListTemp.add(listNoneTokenTemp + listOpenTokenTemp + listRunTokenTemp, map);
                                listNoneTokenTemp++;
                            }
                        }
                    }
                le++;
                publishProgress(le);
            }

            sp.edit().putBoolean("HaveAppCanRun" + lockMode.getId(), CanOpenAppCount > 0 ? true : false).commit();
            listviewList.addAll(listviewListTemp);
            listviewListTemp.clear();
            listviewListTemp.addAll(listviewList);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pdialog.dismiss();
            myAdapter = new MyAdapter(AppWhiteList.this);
            lv.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();

            if (sp.getBoolean("isAppWhiteListHelpFistShow", true)) {
                new ModeHelpDialog().show(getSupportFragmentManager(), null);
                sp.edit().putBoolean("isAppWhiteListHelpFistShow", false).commit();
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO: Implement this method
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer[] values) {
            super.onProgressUpdate(values);
            pdialog.setProgress(values[0]);
        }
    }

    public final class ViewHolder {
        public ImageView AppIcon;
        public TextView AppName;
        public AppCompatCheckBox canOpenCB;
        public AppCompatCheckBox canRunCB;
    }

    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return (null == listviewListTemp) ? 0 : listviewListTemp.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.listview_item_app, null);
                holder.AppIcon = (ImageView) convertView.findViewById(R.id.listviewitemappImageView);
                holder.AppName = (TextView) convertView.findViewById(R.id.listviewitemappTextView);
                holder.canOpenCB = (AppCompatCheckBox) convertView.findViewById(R.id.AppWhiteViewCanOpenCB);
                holder.canRunCB = (AppCompatCheckBox) convertView.findViewById(R.id.AppWhiteViewCanRunCB);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.AppIcon.setImageDrawable((Drawable) listviewListTemp.get(position).get("AppIcon"));
            holder.AppName.setText((String) listviewListTemp.get(position).get("AppName"));
            holder.canRunCB.setChecked((boolean) listviewListTemp.get(position).get("AppCanRun"));
            holder.canOpenCB.setChecked((boolean) listviewListTemp.get(position).get("AppCanOpen"));

            if ((boolean) listviewListTemp.get(position).get("HasLaunch")) {
                holder.canOpenCB.setVisibility(View.VISIBLE);
            } else {
                holder.canOpenCB.setVisibility(View.INVISIBLE);
            }
            final ViewHolder finalHolder = holder;
            holder.canOpenCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listviewListTemp.get(position).put("AppCanOpen", finalHolder.canOpenCB.isChecked());

                    if (finalHolder.canOpenCB.isChecked() == true) {
                        CanOpenAppCount++;
                        finalHolder.canRunCB.setChecked(true);
                        listviewListTemp.get(position).put("AppCanRun", true);
                    } else {
                        CanOpenAppCount--;
                    }
                    appWhiteDBTool.updateOrCreatePackageInfo((String) listviewListTemp.get(position).get("AppPackage"), finalHolder.canRunCB.isChecked(), finalHolder.canOpenCB.isChecked());
                    if (CanOpenAppCount > 0) {
                        sp.edit().putBoolean("HaveAppCanRun" + lockMode.getId(), true).commit();
                    } else {
                        sp.edit().putBoolean("HaveAppCanRun" + lockMode.getId(), false).commit();
                    }
                }
            });

            holder.canRunCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Toast.makeText(AppWhiteList.this, ""+(String)listviewList.get(position).get("AppPackage"), Toast.LENGTH_LONG).show();
                    sp.edit().putBoolean((String) listviewList.get(position).get("AppPackage"), finalHolder.canRunCB.isChecked()).commit();

                    if (finalHolder.canRunCB.isChecked() == false && finalHolder.canOpenCB.isChecked() == true) {
                        CanOpenAppCount--;
                        finalHolder.canOpenCB.setChecked(false);
                        listviewListTemp.get(position).put("AppCanOpen", false);
                    }
                    for (String p : getResources().getStringArray(R.array.open_app_package)) {
                        if (((String) listviewListTemp.get(position).get("AppPackage")).indexOf(p) != -1) {
                            finalHolder.canRunCB.setChecked(true);
                            Toast.makeText(AppWhiteList.this, "默认开启应用", Toast.LENGTH_SHORT).show();
                        }
                    }
                    listviewListTemp.get(position).put("AppCanRun", finalHolder.canRunCB.isChecked());
                    appWhiteDBTool.updateOrCreatePackageInfo((String) listviewListTemp.get(position).get("AppPackage"), finalHolder.canRunCB.isChecked(), finalHolder.canOpenCB.isChecked());

                    if (CanOpenAppCount > 0) {
                        sp.edit().putBoolean("HaveAppCanRun" + lockMode.getId(), true).commit();
                    } else {
                        sp.edit().putBoolean("HaveAppCanRun" + lockMode.getId(), false).commit();
                    }

                }
            });

            return convertView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.activity_applist_menu, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.activity_applist_menu_search));
        searchView.setIconified(true);
        searchView.setQueryHint("输入应用名");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listviewListTemp.clear();
                for (Map<String, Object> map : listviewList) {
                    if (((String) map.get("AppName")).toLowerCase().contains(query.toLowerCase()))
                        listviewListTemp.add(map);
                }
                myAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                listviewListTemp.clear();
                listviewListTemp.addAll(listviewList);
                myAdapter.notifyDataSetChanged();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onBackPressed() {
        backPress();
    }

    private void backPress() {
        if (searchView.isIconified()) {
            task.cancel(true);
            task = null;
            finish();
        } else {
            searchView.setIconified(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: Implement this method
        switch (item.getItemId()) {
            case android.R.id.home:
                backPress();
                break;
            case R.id.activity_applist_menu_help:
                new ModeHelpDialog().show(getSupportFragmentManager(), null);
                break;
            case R.id.activity_applist_menu_allcheck_run:
                new MenuTask(this, 1).execute("");
                break;
            case R.id.activity_applist_menu_allcheck_open:
                new MenuTask(this, 0).execute("");
                break;
            case R.id.activity_applist_menu_uncheck_open:
                new MenuTask(this, 3).execute("");
                break;
            case R.id.activity_applist_menu_uncheck_run:
                new MenuTask(this, 2).execute("");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private class MenuTask extends AsyncTask<String, Integer, String> {
        int le = 0;
        ProgressDialog pdialog;
        private int mode = 0;

        public MenuTask(Context context, int mode) {
            this.mode = mode;

            pdialog = new ProgressDialog(context, 0);
            pdialog.setCanceledOnTouchOutside(false);

            pdialog.setTitle("处理中");
            pdialog.setMessage("正在应用更改中，请等待。。。");

            pdialog.setCancelable(true);

            pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pdialog.show();
        }

        @Override
        protected String doInBackground(String[] p1) {

            le = listviewListTemp.size();

            pdialog.setMax(le);
            le = 0;

            for (Map<String, Object> pi : listviewListTemp) {

                switch (mode) {
                    case 0://全选可打开
                        if (!(boolean) pi.get("AppCanOpen")) {
                            if ((boolean) pi.get("HasLaunch")) {
                                CanOpenAppCount++;
                                pi.put("AppCanOpen", true);
                            }
                        }
                    case 1://全选可运行
                        if (!(boolean) pi.get("AppCanRun")) {
                            pi.put("AppCanRun", true);
                        }
                        break;
                    case 2://清空可运行
                        if ((boolean) pi.get("AppCanRun")) {
                            pi.put("AppCanRun", false);
                            for (String p : getResources().getStringArray(R.array.open_app_package)) {
                                if (((String) pi.get("AppPackage")).indexOf(p) != -1) {
                                    pi.put("AppCanRun", true);
                                }
                            }
                        }
                    case 3://清空可打开
                        if ((boolean) pi.get("AppCanOpen")) {
                            CanOpenAppCount--;
                            pi.put("AppCanOpen", false);
                        }
                        break;
                }
                appWhiteDBTool.updateOrCreatePackageInfo((String) pi.get("AppPackage"), (boolean) pi.get("AppCanRun"), (boolean) pi.get("AppCanOpen"));

                le++;
                publishProgress(le);
            }

            sp.edit().putBoolean("HaveAppCanRun" + lockMode.getId(), CanOpenAppCount > 0 ? true : false).commit();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pdialog.dismiss();
            myAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            // TODO: Implement this method
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer[] values) {
            super.onProgressUpdate(values);
            pdialog.setProgress(values[0]);
        }
    }

/*    public static class helpDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.AppWhiteList_help);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.updata_web, null);

            WebView web = (WebView) view.findViewById(R.id.updata_web_webview);
            web.clearCache(true);
            web.loadUrl("file:///android_asset/" + appTool.getLanguageCode(getContext()) + "/AppWhiteList_help.html");
            builder.setView(view);
            builder.setPositiveButton(R.string.AppWhileList_help_known, null);

            return builder.create();
        }
    }*/

}
