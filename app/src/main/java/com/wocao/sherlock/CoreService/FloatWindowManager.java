package com.wocao.sherlock.CoreService;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.wocao.sherlock.AESUtils;
import com.wocao.sherlock.AppWidget.ServiceState.ServiceStateStatic;
import com.wocao.sherlock.DataBaseOperate.ModeListDBTool;
import com.wocao.sherlock.R;
import com.wocao.sherlock.Setting.DiyFloatView.Widget.InitViewInterface;
import com.wocao.sherlock.Setting.DiyFloatView.Widget.MovableTextClock;
import com.wocao.sherlock.Setting.DiyFloatView.Widget.MovableTextView;
import com.wocao.sherlock.Setting.SettingUtils;
import com.wocao.sherlock.Widget.BottomCard;

import java.io.File;

/**
 * Created by silen on 17-10-3.
 */

public class FloatWindowManager {
    private Context context;

    private SharedPreferences sharedPreferences;

    private WindowManager windowManager;
    private WindowManager.LayoutParams params;

    private View floatView;
  /*  private SeekBar timeSeekBar;
    private TextView *//*unlock, whiteList,*//* mottoTv, timeTv;*/

    private MovableTextView mottoTv, timeTv;
    private MovableTextClock timeTC, dateTC;
    private BottomCard bottomCard;

    RelativeLayout layout;

    private Button forceLockLeft, forceLockRight;

    private long duration;
    private int lockModeId;
    private String lockModeName = null;
    private boolean disableSystemBar;


    private boolean seekBarIsTouch = false;
    private int systemBarHeight = 0;

    int windowHeight;
    int windowWidth;


    FloatWindowAppList floatWindowAppList;

    private FloatWindowManager.OnShouldReNewTimeListener onShouldReNewTimeListener;

    public FloatWindowManager(Context context, long duration, int lockModeId) {

        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        this.duration = duration;
        this.lockModeId = lockModeId;

    }

    public void showFloatWindow() {
        init();
        findView();
        initView();
        setListener();
        initData();
        windowManager.addView(floatView, params);
    }

    public void canRun(boolean canRun) {
        if (canRun) {
            pause();
        } else {
            restore();
        }
    }

    public void distrory() {
        try {
            if (null != windowManager && null != floatView)
                windowManager.removeView(floatView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateView(long timeLeft) {
/*        if (!seekBarIsTouch)
            timeSeekBar.setProgress((int) (duration - timeLeft));*/
        timeTv.setText(lockModeName + " " + timeCalculate(timeLeft));
    }

    public void updateModeId(int lockModeId) {
        this.lockModeId = lockModeId;
        floatWindowAppList.updateLockModeId(lockModeId);
        initData();

    }

    public void pause() {
        params.height = disableSystemBar ? systemBarHeight : 1;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        floatView.setAlpha(0);
        windowManager.updateViewLayout(floatView, params);
    }

    public void restore() {
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        floatView.setAlpha(1);
        windowManager.updateViewLayout(floatView, params);

    }

    private void init() {
        windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.type = 2010;
        params.format = PixelFormat.RGBA_8888;

        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        params.gravity = Gravity.LEFT | Gravity.TOP;

        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

    }


    private void findView() {
        floatView = LayoutInflater.from(context).inflate(R.layout.float_view, null);

        timeTv = (MovableTextView) floatView.findViewById(R.id.lockserverUICoutdownTV);
        mottoTv = (MovableTextView) floatView.findViewById(R.id.lockserverTVMotto);

        timeTC = (MovableTextClock) floatView.findViewById(R.id.lockserverTCTime);
        dateTC = (MovableTextClock) floatView.findViewById(R.id.lockserverTCData);
        forceLockLeft = (Button) floatView.findViewById(R.id.LockUIForceButtonLeft);
        forceLockRight = (Button) floatView.findViewById(R.id.LockUIForceButtonRight);
        bottomCard = (BottomCard) floatView.findViewById(R.id.BottomCardTest);
        layout = (RelativeLayout) floatView.findViewById(R.id.FloatViewLayout);

    }

    private void initView() {
        windowHeight = sharedPreferences.getInt("height", 1920);
        windowWidth = sharedPreferences.getInt("width", 1080);

        floatWindowAppList = new FloatWindowAppList(context, bottomCard, lockModeId);

        timeTv.setInitViewInterface(new InitViewInterface() {
            @Override
            public int getPositionX(int width, int height) {
                return windowWidth / 2 - width / 2;
            }

            @Override
            public int getPositionY(int width, int height) {
                return windowHeight * 3 / 5 - height / 2;
            }
        });

        mottoTv.setInitViewInterface(new InitViewInterface() {
            @Override
            public int getPositionX(int width, int height) {
                return windowWidth / 2 - width / 2;
            }

            @Override
            public int getPositionY(int width, int height) {
                return windowHeight * 3 / 4 - height / 2;
            }
        });
        timeTC.setInitViewInterface(new InitViewInterface() {
            @Override
            public int getPositionX(int width, int height) {
                return windowWidth / 2 - width / 2;
            }

            @Override
            public int getPositionY(int width, int height) {
                return windowHeight / 4 - height / 2;
            }
        });
        dateTC.setInitViewInterface(new InitViewInterface() {
            @Override
            public int getPositionX(int width, int height) {
                return windowWidth / 2 - width / 2;
            }

            @Override
            public int getPositionY(int width, int height) {
                return windowHeight / 3 - height / 2;
            }
        });

        // android 4.2 修复时间显示时HH无法加载，强制使用hh

        if (Build.VERSION.SDK_INT == 17)
            timeTC.setFormat24Hour("hh:mm");

        //格言设置
        ServiceStateStatic.Moto = SettingUtils.getStringValue(context, "setting_display_motto", null);
        if (null != ServiceStateStatic.Moto && !ServiceStateStatic.Moto.equals("")) {
            ServiceStateStatic.hasMotoSet = true;
            mottoTv.setText(ServiceStateStatic.Moto);
        } else {
            ServiceStateStatic.hasMotoSet = false;
            mottoTv.setVisibility(View.INVISIBLE);
        }



/*        //背景透明设置

        if (SettingUtils.getBooleanValue(context, "setting_display_Alpha", false)) {
            //PreferenceManager.getDefaultSharedPreferences(CoreServiceBackup.this).getBoolean("setting_display_Alpha", false)) {
            (floatView.findViewById(R.id.lockserverUILayout)).setBackgroundColor(Color.alpha(0));
            (floatView.findViewById(R.id.lockserverUITimeDisplayLayout)).setVisibility(View.INVISIBLE);
        }*/

/*        //隐藏解锁和白名单按钮
        whiteList.setVisibility(View.INVISIBLE);
        unlock.setVisibility(View.INVISIBLE);*/

        //  timeSeekBar.setMax((int) duration);
        File BACKGROUND_FILE = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "background.jpg");
        if (BACKGROUND_FILE.exists()) {
            try {
                layout.setBackground(new BitmapDrawable(BitmapFactory.decodeFile(BACKGROUND_FILE.getPath())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }




        dateTC.setMovable(false);
        timeTC.setMovable(false);
        //timeTv.setMovable(false);
        mottoTv.setMovable(false);
    }

    private void initData() {
        lockModeName = new ModeListDBTool(context).getModeNameById(lockModeId);
        ServiceStateStatic.MODE_NAME = lockModeName;

        systemBarHeight = sharedPreferences.getInt("SystemBarHeight", 40);
        //完全覆盖掉状态栏
        systemBarHeight += 3;

        disableSystemBar = SettingUtils.getBooleanValue(context, "setting_better_disablesystembar", false);

        if (!sharedPreferences.getBoolean("HaveAppCanRun" + lockModeId, false) && !sharedPreferences.getBoolean(AESUtils.encrypt("wocstudiosoftware", "useCipher"), false)) {
            bottomCard.setVisibility(View.GONE);
        } else {
            bottomCard.setVisibility(View.VISIBLE);
        }

    }

    private void setListener() {

        forceLockLeft.setOnClickListener(new FloatWindowManager.ForceLockButtonListener());
        forceLockRight.setOnClickListener(new FloatWindowManager.ForceLockButtonListener());

/*        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && progress < 3 && whiteList.getVisibility() == View.VISIBLE) {
                    try {
                        Intent i = new Intent();
                        ComponentName cn = new ComponentName("com.wocao.sherlockassist", "com.wocao.sherlockassist.OpenApp");
                        i.setComponent(cn);
                        i.setAction("android.intent.action.MAIN");
                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        i.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        CoreStatic.AccessibilityModeOnTheUnlock = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "开启失败", Toast.LENGTH_LONG).show();
                    }
                } else if (fromUser && progress > duration - 3 && unlock.getVisibility() == 0) {

                    try {
                        Intent i = new Intent();
                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        ComponentName cn = new ComponentName("com.wocao.sherlockassist", "com.wocao.sherlockassist.InputCipher");
                        i.setComponent(cn);
                        i.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        CoreStatic.AccessibilityModeOnTheUnlock = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "开启失败", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (null != onShouldReNewTimeListener) {
                    onShouldReNewTimeListener.reNew();
                }
                seekBarIsTouch = true;
                if (sharedPreferences.getBoolean("HaveAppCanRun" + lockModeId, false))
                    whiteList.setVisibility(View.VISIBLE);
                try {
                    if (sharedPreferences.getBoolean(AESUtils.encrypt("wocstudiosoftware", "useCipher"), false))
                        unlock.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarIsTouch = false;
                whiteList.setVisibility(View.INVISIBLE);
                unlock.setVisibility(View.INVISIBLE);
            }
        });*/
    }

    public void setOnShouldReNewTimeListener(FloatWindowManager.OnShouldReNewTimeListener onShouldReNewTimeListener) {
        this.onShouldReNewTimeListener = onShouldReNewTimeListener;
    }


    public interface OnShouldReNewTimeListener {
        void reNew();
    }

    private int forceStep = 0;
    private long forceTime = 0;

    class ForceLockButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (System.currentTimeMillis() - forceTime > 2000) {
                forceStep = 0;
            }
            forceTime = System.currentTimeMillis();

            switch (forceStep) {
                case 0:
                case 1:
                    if (v.getId() == R.id.LockUIForceButtonLeft) {
                        forceStep++;
                    } else {
                        forceStep = 0;
                    }
                    break;
                case 2:
                case 3:
                    if (v.getId() == R.id.LockUIForceButtonRight) {
                        forceStep++;
                    } else {
                        forceStep = 0;
                    }
                    break;
                case 4:
                    if (v.getId() == R.id.LockUIForceButtonLeft) {
                        forceStep++;
                    } else {
                        forceStep = 0;
                    }
                    break;
                case 5:
                    if (v.getId() == R.id.LockUIForceButtonRight) {
                        forceStep = 0;
                        Intent i = new Intent();
                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        ComponentName cn = new ComponentName("com.wocao.sherlockassist", "com.wocao.sherlockassist.InputCipher");
                        i.setComponent(cn);
                        i.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("UNLOCK_DEVELOPER", true);
                        context.startActivity(i);
                        CoreStatic.AccessibilityModeOnTheUnlock = true;
                    } else {
                        forceStep = 0;
                    }
            }
        }
    }

    public String timeCalculate(long i) {
        long length = i;
        int countDay, countHour, countMinu, countSec;
        String TvStr = "";
        countDay = (int) length / (60 * 60 * 24);
        countHour = (int) (length % (60 * 60 * 24)) / (60 * 60);
        countMinu = (int) (length % (60 * 60)) / 60;
        countSec = (int) (length % 60);
        //TvStr = "";
        if (countDay > 0) {
            TvStr = countDay + context.getResources().getString(R.string.killpoccesserve_day);
        }
        if (countHour > 0) {
            TvStr = TvStr + countHour + context.getResources().getString(R.string.killpoccesserve_hour);
        }
        if (countMinu > 0) {
            TvStr = TvStr + countMinu + context.getResources().getString(R.string.killpoccesserve_min);
        }
        if (countSec >= 0) {
            TvStr = TvStr + countSec + context.getResources().getString(R.string.killpoccesserve_scend);
        }

        return TvStr;
    }

}
