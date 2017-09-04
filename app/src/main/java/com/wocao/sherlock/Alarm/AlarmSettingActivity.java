package com.wocao.sherlock.Alarm;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wocao.sherlock.Alarm.Notification.AlarmNotificationOperate;
import com.wocao.sherlock.DataBaseOperate.AlarmDBTool;
import com.wocao.sherlock.DataBaseOperate.ModeListDBTool;
import com.wocao.sherlock.ModeOperate.Model.LockMode;
import com.wocao.sherlock.Permission.PolicyAdminUtils;
import com.wocao.sherlock.R;
import com.wocao.sherlock.appTool;
import com.wocao.sherlock.MaterialDesign.StatusBarUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 10397 on 2016/1/2.
 */
public class AlarmSettingActivity extends AppCompatActivity {

    Toolbar mToolBar;
    AppCompatCheckBox compatCheckBox[] = new AppCompatCheckBox[8];
    TextView startTimeTV, endTimeTV, nextDayTV;
    AppCompatSpinner spinner;
    int token = 0;

    Intent intent;

    Map<String, Object> map = new HashMap<>();

    AlarmDBTool dbTool;
    ModeListDBTool modeListDBTool;
    MenuItem menuItemSave, menuItemDelete;

    List<LockMode> modeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_setting_view);

        new StatusBarUtils().setStatusBar(this);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        } catch (Exception e) {
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getModeListData();
        dbTool = new AlarmDBTool(AlarmSettingActivity.this);
        intent = getIntent();

        findView();
        initView();
        setListener();
    }

    private void getModeListData() {
        modeListDBTool = new ModeListDBTool(AlarmSettingActivity.this);
        modeList = modeListDBTool.getAllModeListData();
    }

    private void initView() {
        if (intent.getBooleanExtra("ActionAddAlarm", false)) {
            Calendar calender = Calendar.getInstance();
            map.put("startTime", calender.get(Calendar.HOUR_OF_DAY) * 100 + calender.get(Calendar.MINUTE));
            map.put("endTime", calender.get(Calendar.HOUR_OF_DAY) * 100 + calender.get(Calendar.MINUTE));
            map.put("isTurnOn", true);
            map.put("Monday", true);
            map.put("Thursday", true);
            map.put("Wednesday", true);
            map.put("Tuesday", true);
            map.put("Friday", true);
            map.put("Saturday", true);
            map.put("Sunday", true);
            map.put("Mode", 0);
            getSupportActionBar().setTitle("新建任务");


        } else {
            map = dbTool.quaryAlarm(intent.getIntExtra("ActionAlarmModify", 1));
            compatCheckBox[1].setChecked((boolean) map.get("Monday"));
            compatCheckBox[2].setChecked((boolean) map.get("Tuesday"));
            compatCheckBox[3].setChecked((boolean) map.get("Wednesday"));
            compatCheckBox[4].setChecked((boolean) map.get("Thursday"));
            compatCheckBox[5].setChecked((boolean) map.get("Friday"));
            compatCheckBox[6].setChecked((boolean) map.get("Saturday"));
            compatCheckBox[7].setChecked((boolean) map.get("Sunday"));

            compatCBOperate();
            getSupportActionBar().setTitle("修改任务");
        }

        spinnerOperate();

        startTimeTV.setText(formatTime((int) map.get("startTime")));
        endTimeTV.setText(formatTime((int) map.get("endTime")));

        if ((int) map.get("startTime") - (int) map.get("endTime") >= 0) {
            nextDayTV.setVisibility(View.VISIBLE);
        } else {
            nextDayTV.setVisibility(View.INVISIBLE);
        }
    }

    private void spinnerOperate() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AlarmSettingActivity.this, android.R.layout.simple_spinner_dropdown_item);
        for (LockMode lockMode : modeList) {
            adapter.add(lockMode.getName());
        }
        spinner.setAdapter(adapter);
        spinner.setSelection(getModePositionById((int) map.get("Mode")));
    }

    private int getModePositionById(int id) {
        for (int i = 0; i < modeList.size(); i++) {
            if (modeList.get(i).getId() == id) {
                return i;
            }
        }
        return 0;
    }

    private void findView() {
        for (int i = 0; i < 8; i++) {
            compatCheckBox[i] = (AppCompatCheckBox) findViewById(getResources().getIdentifier("alarmSettingCB" + i, "id", getPackageName()));
        }
        startTimeTV = (TextView) findViewById(R.id.alarmSettingStartTime);
        endTimeTV = (TextView) findViewById(R.id.ShortcutItemLabelTV);
        spinner = (AppCompatSpinner) findViewById(R.id.alarmSettingModeSelectSpinner);
        nextDayTV = (TextView) findViewById(R.id.alarmsettingviewNextDayTV);
    }

    private void setListener() {
        startTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token = 0;
                showTimePickerDialog();
                //new TimePickerDIalog().show(getSupportFragmentManager(), null);
            }
        });
        endTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token = 1;
                showTimePickerDialog();
               // new TimePickerDIalog().show(getSupportFragmentManager(), null);
            }
        });


        compatCheckBox[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compatCBOperate(compatCheckBox[0].isChecked());
            }
        });

        compatCheckBox[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    compatCBOperate();
                } else {
                    compatCheckBox[0].setChecked(false);
                }
            }
        });
        compatCheckBox[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    compatCBOperate();
                } else {
                    compatCheckBox[0].setChecked(false);
                }
            }
        });
        compatCheckBox[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    compatCBOperate();
                } else {
                    compatCheckBox[0].setChecked(false);
                }
            }
        });
        compatCheckBox[4].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    compatCBOperate();
                } else {
                    compatCheckBox[0].setChecked(false);
                }
            }
        });
        compatCheckBox[5].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    compatCBOperate();
                } else {
                    compatCheckBox[0].setChecked(false);
                }
            }
        });
        compatCheckBox[6].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    compatCBOperate();
                } else {
                    compatCheckBox[0].setChecked(false);
                }
            }
        });
        compatCheckBox[7].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    compatCBOperate();
                } else {
                    compatCheckBox[0].setChecked(false);
                }
            }
        });

    }

    private void compatCBOperate() {
        boolean CBtoken = true;
        for (int i = 1; i < 8; i++) {
            if (!compatCheckBox[i].isChecked()) {
                CBtoken = false;
                break;
            }
        }
        compatCheckBox[0].setChecked(CBtoken);
    }

    private void compatCBOperate(boolean isChecked) {
        for (int i = 1; i < 8; i++) {
            compatCheckBox[i].setChecked(isChecked);
        }
    }

    private void showTimePickerDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AlarmSettingActivity.this);
        View view = LayoutInflater.from(AlarmSettingActivity.this).inflate(R.layout.alarm_setting_timepicker_dialogview, null);
        final TimePicker picker = (TimePicker) view.findViewById(R.id.alarmSettingTimepickerDialogTimePicker);
        picker.setIs24HourView(true);
        if (token == 0) {
            if (Build.VERSION.SDK_INT > 22) {
                picker.setHour(((int) map.get("startTime")) / 100);
                picker.setMinute(((int) map.get("startTime")) % 100);
            } else {
                picker.setCurrentHour(((int) map.get("startTime")) / 100);
                picker.setCurrentMinute(((int) map.get("startTime")) % 100);
            }

        } else {
            if (Build.VERSION.SDK_INT > 22) {
                picker.setHour(((int) map.get("endTime")) / 100);
                picker.setMinute(((int) map.get("endTime")) % 100);
            } else {
                picker.setCurrentHour(((int) map.get("endTime")) / 100);
                picker.setCurrentMinute(((int) map.get("endTime")) % 100);
            }
        }
        builder.setView(view);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (token == 0) {
                    if (Build.VERSION.SDK_INT > 22) {
                        map.put("startTime", picker.getHour() * 100 + picker.getCurrentMinute());

                    } else {
                        map.put("startTime", picker.getCurrentHour() * 100 + picker.getCurrentMinute());
                    }
                    startTimeTV.setText(formatTime((int) map.get("startTime")));
                } else {
                    if (Build.VERSION.SDK_INT > 22) {
                        map.put("endTime", picker.getHour() * 100 + picker.getCurrentMinute());

                    } else {
                        map.put("endTime", picker.getCurrentHour() * 100 + picker.getCurrentMinute());
                    }
                    endTimeTV.setText(formatTime((int) map.get("endTime")));
                }
                if ((int) map.get("startTime") - (int) map.get("endTime") >= 0) {
                    nextDayTV.setVisibility(View.VISIBLE);
                } else {
                    nextDayTV.setVisibility(View.INVISIBLE);
                }

            }
        });
        builder.show();
    }
/*
    public class TimePickerDIalog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            AlertDialog.Builder builder = new AlertDialog.Builder(AlarmSettingActivity.this);
            View view = LayoutInflater.from(AlarmSettingActivity.this).inflate(R.layout.alarm_setting_timepicker_dialogview, null);
            final TimePicker picker = (TimePicker) view.findViewById(R.id.alarmSettingTimepickerDialogTimePicker);
            picker.setIs24HourView(true);
            if (token == 0) {
                if (Build.VERSION.SDK_INT > 22) {
                    picker.setHour(((int) map.get("startTime")) / 100);
                    picker.setMinute(((int) map.get("startTime")) % 100);
                } else {
                    picker.setCurrentHour(((int) map.get("startTime")) / 100);
                    picker.setCurrentMinute(((int) map.get("startTime")) % 100);
                }

            } else {
                if (Build.VERSION.SDK_INT > 22) {
                    picker.setHour(((int) map.get("endTime")) / 100);
                    picker.setMinute(((int) map.get("endTime")) % 100);
                } else {
                    picker.setCurrentHour(((int) map.get("endTime")) / 100);
                    picker.setCurrentMinute(((int) map.get("endTime")) % 100);
                }
            }
            builder.setView(view);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (token == 0) {
                        if (Build.VERSION.SDK_INT > 22) {
                            map.put("startTime", picker.getHour() * 100 + picker.getCurrentMinute());

                        } else {
                            map.put("startTime", picker.getCurrentHour() * 100 + picker.getCurrentMinute());
                        }
                        startTimeTV.setText(formatTime((int) map.get("startTime")));
                    } else {
                        if (Build.VERSION.SDK_INT > 22) {
                            map.put("endTime", picker.getHour() * 100 + picker.getCurrentMinute());

                        } else {
                            map.put("endTime", picker.getCurrentHour() * 100 + picker.getCurrentMinute());
                        }
                        endTimeTV.setText(formatTime((int) map.get("endTime")));
                    }
                    if ((int) map.get("startTime") - (int) map.get("endTime") >= 0) {
                        nextDayTV.setVisibility(View.VISIBLE);
                    } else {
                        nextDayTV.setVisibility(View.INVISIBLE);
                    }

                }
            });
            return builder.create();
        }
    }*/

    /**
     * this method is to format the time display
     *
     * @param time the integer of time
     * @return the string of time
     */
    private String formatTime(int time) {
        String returnString = "";
        int timeSuf = time / 100;
        time = time % 100;
        if (timeSuf > 9) {
            returnString = "" + timeSuf;
        } else {
            returnString = "0" + timeSuf;
        }
        if (time > 9) {
            returnString += ":" + time;
        } else {
            returnString += ":0" + time;
        }
        return returnString;
    }

    private boolean settingIsIlligel() {
        boolean CBToken = false;
        for (int i = 1; i < 8; i++) {
            if (compatCheckBox[i].isChecked()) {
                CBToken = true;
                break;
            }
        }
        if (!CBToken) {
            Toast.makeText(AlarmSettingActivity.this, "至少选择一天", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (modeList.get(spinner.getSelectedItemPosition()).getId() == 1) {
            if (!PolicyAdminUtils.checkDeviceAdmin(AlarmSettingActivity.this, getSupportFragmentManager(),true)) {
                return false;
            }

        }

      /*  if ((int)map.get("endTime")-(int)map.get("startTime")<1){
            Toast.makeText(AlarmSettingActivity.this, "结束时间必须大于开始时间", Toast.LENGTH_SHORT).show();
            return false;
        }*/
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO: Implement this method

        menuItemSave = menu.add(1, 1, 1, "保存");
        menuItemSave.setTitle("保存");
        menuItemSave.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menuItemDelete = menu.add(0, 0, 0, "删除");
        menuItemDelete.setTitle("删除");
        menuItemDelete.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: Implement this method
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(AlarmSettingActivity.this, AlarmListActivity.class));
                finish();
                break;
            case 1:
                if (settingIsIlligel()) {

                    if (intent.getBooleanExtra("ActionAddAlarm", false)) {
                        dbTool.insertAlarm((boolean) map.get("isTurnOn"), (int) map.get("startTime"), (int) map.get("endTime"), compatCheckBox[1].isChecked(), compatCheckBox[2].isChecked(), compatCheckBox[3].isChecked(), compatCheckBox[4].isChecked(), compatCheckBox[5].isChecked(), compatCheckBox[6].isChecked(), compatCheckBox[7].isChecked(), modeList.get(spinner.getSelectedItemPosition()).getId());

                    } else {
                        dbTool.updataAlarm((int) map.get("alarmID"), (boolean) map.get("isTurnOn"), (int) map.get("startTime"), (int) map.get("endTime"), compatCheckBox[1].isChecked(), compatCheckBox[2].isChecked(), compatCheckBox[3].isChecked(), compatCheckBox[4].isChecked(), compatCheckBox[5].isChecked(), compatCheckBox[6].isChecked(), compatCheckBox[7].isChecked(), modeList.get(spinner.getSelectedItemPosition()).getId());
                    }
                    startActivity(new Intent(AlarmSettingActivity.this, AlarmListActivity.class));
                    finish();
                }
                break;
            case 0:
                if (!intent.getBooleanExtra("ActionAddAlarm", false)) {
                    dbTool.deleteAlarm((int) map.get("alarmID"));
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent intent = new Intent("Sherlock_Alarm_Action").putExtra("alarmID", (int) map.get("alarmID"));
                    PendingIntent pi = PendingIntent.getBroadcast(AlarmSettingActivity.this, (int) map.get("alarmID"), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager.cancel(pi);
                    if (AlarmNotificationOperate.isTurnOn(this)) {
                        Intent notificationIntent = new Intent("Sherlock_Alarm_Notification_Action").putExtra("alarmID", (int) map.get("alarmID"));
                        PendingIntent notificationPi = PendingIntent.getBroadcast(AlarmSettingActivity.this, (int) map.get("alarmID"), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(notificationPi);
                    }

                }
                startActivity(new Intent(AlarmSettingActivity.this, AlarmListActivity.class));

                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AlarmSettingActivity.this, AlarmListActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
