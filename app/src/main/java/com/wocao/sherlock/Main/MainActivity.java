package com.wocao.sherlock.Main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.wocao.sherlock.Alarm.AlarmListActivity;
import com.wocao.sherlock.ControlByOther.ControlType;
import com.wocao.sherlock.ControlByOther.Presenter.ControlByOtherDialogManager;
import com.wocao.sherlock.CoreService.CoreService;
import com.wocao.sherlock.ExitApplication;
import com.wocao.sherlock.ForceUnlock.ForceUnlockManager;
import com.wocao.sherlock.HelpAndAboutActivity;
import com.wocao.sherlock.ModeOperate.Model.LockMode;
import com.wocao.sherlock.ModeOperate.Server.InitMode;
import com.wocao.sherlock.ModeOperate.View.ModeListDisplayDialog;
import com.wocao.sherlock.ModeOperate.View.ModeSelectDialog;
import com.wocao.sherlock.Permission.PermissionUtils;
import com.wocao.sherlock.Permission.PolicyAdminUtils;
import com.wocao.sherlock.R;
import com.wocao.sherlock.Setting.SettingActivity;
import com.wocao.sherlock.Setting.SettingUtils;
import com.wocao.sherlock.Shortcut.View.OnDialogResultListener;
import com.wocao.sherlock.Shortcut.View.ShortcutReCheckDialog;
import com.wocao.sherlock.MaterialDesign.StatusBarUtils;
import com.wocao.sherlock.TestActivity;
import com.wocao.sherlock.Widget.CircleWidget;
import com.wocao.sherlock.appTool;
import com.wocao.sherlock.leading;
import com.wocao.sherlock.Update.UpdateUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolBar;
    Intent intent;
    Context context;
    SharedPreferences sp;
    long timeToFinish = 0l;
    CircleWidget BTStart;

    MenuItem menuItemAlarm;


    DataSetting dataSetting = new DataSetting();


    protected void onCreate(Bundle savedInstanceState) {

/*        if (BuildConfig.DEBUG) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }*/
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        new StatusBarUtils().setStatusBar(this);

//      appTool.disableDataConnection(this);

        //TODO:TEST
        //    Log.i("NotificationSwitch", AlarmNotificationOperate.isTurnOn(this) + "");

        new UpdateUtils(MainActivity.this, getSupportFragmentManager());

        mToolBar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolBar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        context = this;
        sp = getSharedPreferences("data", MODE_PRIVATE);

        sp.edit().putInt("SystemBarHeight", appTool.getStatusHeight(MainActivity.this, true, 25, MainActivity.this)).commit();


        intent = getIntent();

        ExitApplication.getInstance().addActivity(this);

        if (sp.getBoolean("FirstUse", true)) {
            startActivity(new Intent(MainActivity.this, leading.class));
            finish();
        }

        if (sp.getBoolean("FirstUseMode", true)) {
            new InitMode(MainActivity.this).execute("");
            sp.edit().putBoolean("FirstUseMode", false).commit();
        }

        try {
            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.versionTV)).setText("v" + getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        BTStart = (CircleWidget) super.findViewById(R.id.mainImageButton);
        BTStart.setOnClickListener(new CircleWidget.OnClickListener() {
            @Override
            public void onClick() {

                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(50);

                if (PermissionUtils.checkPermission(context, getSupportFragmentManager(), false)) {
                    ModeSelectDialog modeSelectDialog = new ModeSelectDialog();
                    modeSelectDialog.setOnLockModeSelectedListener(new OnLockModeSelectedListener());
                    modeSelectDialog.show(getSupportFragmentManager(), null);
                }
            }
        });

        if (((sp.getLong("date", timeToFinish) - System.currentTimeMillis()) / 1000) > 0) {
            StartService();
        }/*else {
            if (intent.getBooleanExtra("boot", false)) {
                finish();
            }
        }*/

        shortCutStart();

        SettingUtils.updatePreferences(this);

    //    startActivity(new Intent(this,TestActivity.class));

        sp.edit().putInt("height",getWindowManager().getDefaultDisplay().getHeight()).apply();
        sp.edit().putInt("width",getWindowManager().getDefaultDisplay().getWidth()).apply();

    }

    private void StartService() {
        stopService(new Intent(MainActivity.this, CoreService.class));
        startService(new Intent(MainActivity.this, CoreService.class));
        finish();
    }

    private void shortCutStart() {
        if (intent.getBooleanExtra("Shortcut", false)) {

            long time = intent.getLongExtra("time", 0);
            dataSetting.setDate(time * 1000 + System.currentTimeMillis());
            dataSetting.setModeId(intent.getIntExtra("modeId", 0));

            ShortcutReCheckDialog reCheckDialog = new ShortcutReCheckDialog(timeString(dataSetting).trim(), dataSetting.getModeId());
            reCheckDialog.show(getSupportFragmentManager(), null);
            reCheckDialog.setOnDialogResultListener(new OnShortcutCheckDialogListener());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menuItemAlarm = menu.add(1, 11, 11, R.string.MainActivity_AlarmMod_Dialog_title);
        menuItemAlarm.setIcon(R.mipmap.ic_alarm_check);
        menuItemAlarm.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                ExitApplication.getInstance().exit();
                finish();
                break;
            case 11:
                if (PermissionUtils.checkPermission(MainActivity.this, getSupportFragmentManager(), false))
                    startActivity(new Intent(MainActivity.this, AlarmListActivity.class));
                break;
        }
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        ControlByOtherDialogManager manager = new ControlByOtherDialogManager(this);
        switch (item.getItemId()) {
            case R.id.drawerIDForceUnlockCipher:
                manager.setOnSuccessListener(new ControlByOtherDialogManager.OnSuccessListener() {
                    @Override
                    public void success() {
                        ForceUnlockManager.gotoActivity(MainActivity.this);
                    }
                });

                manager.test(getSupportFragmentManager(), ControlType.TYPE_UNLOCK);
                break;

            case R.id.drawerIDSetting:
                manager.setOnSuccessListener(new ControlByOtherDialogManager.OnSuccessListener() {
                    @Override
                    public void success() {
                        startActivity(new Intent(MainActivity.this, SettingActivity.class));
                    }
                });
                manager.test(getSupportFragmentManager(), ControlType.TYPE_SETTING);

                break;
            case R.id.drawerIDModeOperate:
                manager.setOnSuccessListener(new ControlByOtherDialogManager.OnSuccessListener() {
                    @Override
                    public void success() {
                        new ModeListDisplayDialog().show(getSupportFragmentManager(), null);
                    }
                });
                manager.test(getSupportFragmentManager(), ControlType.TYPE_WHITE_LIST);

                break;
            case R.id.drawerIDDonate:
                new AliPaySupportDialog(context).show(getSupportFragmentManager(), null);
                break;
            case R.id.drawerIDAboutApp:
                startActivity(new Intent(MainActivity.this, HelpAndAboutActivity.class).putExtra("Type", "关于"));
                break;
            case R.id.drawerIDShareApp:
                PackageManager pm = getPackageManager();
                try {
                    ApplicationInfo ai = pm.getApplicationInfo("com.wocao.sherlock", 0);
                    startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND).putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(ai.sourceDir))).setType("*/*"), "Share"));
                } catch (PackageManager.NameNotFoundException e) {

                }
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            ExitApplication.getInstance().exit();
            finish();
        }
    }


    private String timeString(DataSetting dataSetting) {
        StringBuilder builder = new StringBuilder();
        if (dataSetting.getDay() > 0) {
            builder.append(dataSetting.getDay() + getResources().getString(R.string.killpoccesserve_day));
        }
        if (dataSetting.getHour() > 0) {
            builder.append(dataSetting.getHour() + getResources().getString(R.string.killpoccesserve_hour));
        }
        if (dataSetting.getMinute() > 0) {
            builder.append(dataSetting.getMinute() + getResources().getString(R.string.killpoccesserve_min));
        }
        if (dataSetting.getSec() > 0) {
            builder.append(dataSetting.getSec() + getResources().getString(R.string.killpoccesserve_scend));
        }
        return builder.toString().trim();
    }

    private class OnLockModeSelectedListener implements ModeSelectDialog.OnLockModeSelectedListener {

        @Override
        public void onSelected(LockMode lockMode) {
            dataSetting.setModeId(lockMode.getId());
            if (PolicyAdminUtils.checkDeviceAdmin(MainActivity.this, getSupportFragmentManager(), dataSetting.isStrengthMode()))
                new TimeSettingDialog(MainActivity.this)
                        .setOnTimeSettingListener(new OnTimeSettingListener())
                        .show(getSupportFragmentManager(), null);
        }
    }

    private class OnTimeSettingListener implements TimeSettingDialog.OnTimeSettingListener {

        @Override
        public void set(long time) {
            dataSetting.setDate(time);
            new TimeCheckDialog(timeString(dataSetting))
                    .setOnCheckedListener(new OnCheckedListener())
                    .show(getSupportFragmentManager(), null);
        }
    }

    private class OnCheckedListener implements TimeCheckDialog.OnCheckedListener {

        @Override
        public void checked() {
            if (dataSetting.getDay() * 24 + dataSetting.getHour() >= 1) {
                new TimeReCheckDialog(MainActivity.this, dataSetting.getDay(), dataSetting.getHour())
                        .setOnCheckedListener(new OnReCheckedListener())
                        .show(getSupportFragmentManager(), null);
            } else {
                startLockService();
            }
        }
    }

    private class OnReCheckedListener implements TimeReCheckDialog.OnCheckedListener {

        @Override
        public void onChecked() {
            if (dataSetting.isStrengthMode()) {
                showStrengthenAlarmDialog();
            } else {
                startLockService();
            }
        }
    }

    private class OnShortcutCheckDialogListener implements OnDialogResultListener {

        @Override
        public void onResult(Object result) {

            if (dataSetting.isStrengthMode()) {
                showStrengthenAlarmDialog();
            } else {
                startLockService();
            }
        }
    }


    private void showStrengthenAlarmDialog() {
        StrengthenModeAlarmDialog strengthenModeAlarmDialog = new StrengthenModeAlarmDialog();
        strengthenModeAlarmDialog.setCancelable(false);
        strengthenModeAlarmDialog.setOnResultListener(new StrengthenModeAlarmDialog.OnResultListener() {
            @Override
            public void onClick() {
                startLockService();
            }
        });
        strengthenModeAlarmDialog.show(getSupportFragmentManager(), null);
    }


    private void startLockService() {

        if (PermissionUtils.checkPermission(context, getSupportFragmentManager(), dataSetting.isStrengthMode())) {

            sp.edit().putInt("LockModeId", dataSetting.getModeId()).commit();
            sp.edit().putLong("date", dataSetting.getDate()).commit();
            intent = new Intent();
            intent.setClass(MainActivity.this, CoreService.class);
            startService(intent);

            ExitApplication.getInstance().exit();
            finish();
        }
    }

    private class DataSetting {
        private long date;
        private int day;
        private int hour;
        private int minute;
        private int sec;
        private int modeId;

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;

            long dateTemp = (date - System.currentTimeMillis()) / 1000;
            this.day = (int) (dateTemp / (60 * 60 * 24));
            this.hour = (int) (dateTemp % (60 * 60 * 24)) / (60 * 60);
            this.minute = (int) ((dateTemp % (60 * 60)) / 60);
            this.sec = (int) (dateTemp % 60);
        }

        public int getDay() {
            return day;
        }

        public int getHour() {
            return hour;
        }

        public int getMinute() {
            return minute;
        }

        public int getSec() {
            return sec;
        }

        public int getModeId() {
            return modeId;
        }

        public void setModeId(int modeId) {
            this.modeId = modeId;
        }

        public boolean isStrengthMode() {
            return getModeId() == 1 ? true : false;
        }
    }


}