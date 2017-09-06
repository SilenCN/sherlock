package com.wocao.sherlock.Main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.wocao.sherlock.AESUtils;
import com.wocao.sherlock.Accessibility.AccessbilityTool;
import com.wocao.sherlock.Alarm.AlarmListActivity;
import com.wocao.sherlock.AppConfig;
import com.wocao.sherlock.Assist.AssistUtils;
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
import com.wocao.sherlock.Permission.OverlayPermissionUtils;
import com.wocao.sherlock.Permission.PermissionUtils;
import com.wocao.sherlock.Permission.PolicyAdminUtils;
import com.wocao.sherlock.Permission.UsageStatsUtils;
import com.wocao.sherlock.R;
import com.wocao.sherlock.Setting.SettingActivity;
import com.wocao.sherlock.Setting.SettingUtils;
import com.wocao.sherlock.Shortcut.Activity.ShortcutsActivity;
import com.wocao.sherlock.Shortcut.View.OnDialogResultListener;
import com.wocao.sherlock.Shortcut.View.ShortcutReCheckDialog;
import com.wocao.sherlock.MaterialDesign.StatusBarUtils;
import com.wocao.sherlock.Widget.CircleWidget;
import com.wocao.sherlock.Widget.WrapContentHeightViewPager;
import com.wocao.sherlock.appTool;
import com.wocao.sherlock.leading;
import com.wocao.sherlock.Update.UpdateUtils;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolBar;
    Intent intent;
    Context context;
    SharedPreferences sp;
    long timeToFinish = 0l;
    CircleWidget BTStart;
    boolean isService = false;
    long time = 0;
    View view;
    boolean SnackBarIsShowing = false;

    MenuItem menuItemStrengthen;
    MenuItem menuItemAlarm;

    long dateSetting = 0l, delta = 0l;
    int countDay = 0, countHour = 0;

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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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

    class strengthenModDialog extends DialogFragment {

        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.MainActivity_StrengthenMod_Dialog_message);
            builder.setTitle(R.string.MainActivity_StrengthenMod_Dialog_title);
            builder.setPositiveButton(R.string.MainActivity_StrengthenMod_Dialog_enter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        //   if (new appTool().checkDeviceAdmin(MainActivity.this, getSupportFragmentManager(), true)) {
                        //      Toast.makeText(getActivity(), getResources().getString(R.string.MainActivity_StrengthenMod_entered), Toast.LENGTH_SHORT).show();
                        //      menuItemStrengthen.setIcon(R.mipmap.strengthen_true);
                        //   }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });
            return builder.create();
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

/*
    class dialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // TODO: Implement this method
            super.onCreateDialog(savedInstanceState);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.setTime_GoOn);
            builder.setMessage(String.format(getResources().getString(R.string.setTime_GoNoMessage), new Object[]{timeCalculate(delta / 1000)}));
            builder.setNegativeButton(R.string.setTime_GoOnCancel, null);

            builder.setPositiveButton(R.string.setTime_GoOnOK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface p1, int p2) {
                            if (countDay * 24 + countHour >= 1) {
                                new dialogReCheck().show(getSupportFragmentManager(), null);
                            } else {
                                if (AccessbilityTool.checkAccessibilityWithDialog(MainActivity.this, getSupportFragmentManager()) || (!AccessbilityTool.isUseAccessibility(MainActivity.this) && UsageStatsUtils.needPermissionForBlocking(MainActivity.this, getSupportFragmentManager())))

                                    if (PolicyAdminUtils.checkDeviceAdmin(MainActivity.this, getSupportFragmentManager())) {// 判断是否有权限(激活了设备管理器)
                                        if (AppConfig.ignoreDeviceAdmin) {
                                            startLockService(dateSetting);
                                        } else {
                                            showStrengthenAlarmDialog();
                                        }
                                    }
                            }
                        }
                    }
            );
            return builder.create();
        }
    }

    class dialogReCheck extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // TODO: Implement this method
            super.onCreateDialog(savedInstanceState);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.setTime_ReCheckTitle);
            builder.setMessage(R.string.setTime_ReCheckMessage);
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.set_time_recheck_dialog_view, null);
            final EditText edit = (EditText) view.findViewById(R.id.setTimerecheckEditView);
            final TextView tv = (TextView) view.findViewById(R.id.setTimerecheckFocusTV);
            //  tv.requestFocus();
            builder.setView(view);
            builder.setNegativeButton(R.string.setTime_GoOnCancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface p1, int p2) {

                }
            });

            builder.setPositiveButton(R.string.setTime_GoOnOK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface p1, int p2) {
                            tv.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            imm.showSoftInput(tv, InputMethodManager.SHOW_FORCED);

                            if (edit.getText() != null & !edit.getText().toString().equals("")) {
                                if (countDay + countHour == Integer.parseInt(edit.getText().toString())) {
                                    if (AccessbilityTool.checkAccessibilityWithDialog(MainActivity.this, getSupportFragmentManager()) || (!AccessbilityTool.isUseAccessibility(MainActivity.this) && UsageStatsUtils.needPermissionForBlocking(MainActivity.this, getSupportFragmentManager())))

                                        if (PolicyAdminUtils.checkDeviceAdmin(MainActivity.this, getSupportFragmentManager())) {// 判断是否有权限(激活了设备管理器)
                                            if (AppConfig.ignoreDeviceAdmin) {
                                                startLockService(dateSetting);
                                            } else {
                                                showStrengthenAlarmDialog();
                                            }
                                        }
                                } else {
                                    Toast.makeText(MainActivity.this, R.string.setTime_Input_error, Toast.LENGTH_SHORT).show();
                                }

                            } else {

                                Toast.makeText(MainActivity.this, R.string.setTime_Input_void, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
            return builder.create();
        }
    }*/

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