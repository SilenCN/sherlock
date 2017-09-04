package com.wocao.sherlock.CoreService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

import com.wocao.sherlock.Accessibility.AccessbilityTool;
import com.wocao.sherlock.AppWidget.ServiceState.ServiceStateStatic;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by silen on 17-5-1.
 */

public class TimeManager {

    private static final int TIME_FOR_UPDATE = 1000;
    private static final int TIME_FOR_GET_PACKAGE = 300;


    private Context context;
    private SharedPreferences sharedPreferences;
    private Timer timerForUpdate, timerForPackage;
    private Handler handler;


    private long durationLeft = 0;
    private int reNewTime = 0;

    private OnTimeAction onTimeAction;

    private boolean isFinishByAccident = true;

    public TimeManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        initTime();

        //TODO:删除
      //  finish();
    }

    private void initTime() {
        durationLeft = (sharedPreferences.getLong("date", 0) - System.currentTimeMillis()) / 1000;
    }

    public long getDurationLeft() {
        return durationLeft;
    }

    public void start() {
        init();
    }

    private void init() {
        if (durationLeft < 3 || durationLeft / (60 * 60 * 24 * 16) > 0) {
            finish();
            return;
        }

        ServiceStateStatic.isServiceRunning = true;

        isFinishByAccident = false;

        setHandler();

        timerForUpdate = new Timer();

        TimerTask taskForUpdate = new TimerTask() {

            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timerForUpdate.schedule(taskForUpdate, 0, 1000);

        //如果使用Accessibility，不启用刷新获取Package
        if (!AccessbilityTool.canUseAccessibility(context)) {
            timerForPackage = new Timer();
            TimerTask taskForPackage = new TimerTask() {

                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            };
            timerForUpdate.schedule(taskForPackage, 0, 250);
        }
    }

    @SuppressLint("HandlerLeak")
    private void setHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    //刷新时间信号
                    case 1:
                        if (durationLeft > 0) {
                            update(durationLeft);
                            durationLeft--;
                            reNewTime++;
                            if (reNewTime > 300) {
                                reNewTime = 0;
                                reNewTime();
                            }
                        } else {
                            finish();
                        }
                        break;
                    //获取顶层包名信号
                    case 2:
                        if (null != onTimeAction) {
                            onTimeAction.checkForPackage();
                        }
                        break;
                }
            }
        };
    }

    public void finish() {

        ServiceStateStatic.isServiceRunning = false;

        try {
            if (null != timerForPackage) {
                timerForPackage.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (null != timerForUpdate) {
                timerForUpdate.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (null != onTimeAction) {
                onTimeAction.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update(long durationLeft) {
        if (null != onTimeAction) {
            onTimeAction.update(durationLeft);
        }
    }

    public void reNewTime() {
        durationLeft = (sharedPreferences.getLong("date", durationLeft) - System.currentTimeMillis()) / 1000;
    }

    public void setOnTimeAction(OnTimeAction onTimeAction) {
        this.onTimeAction = onTimeAction;
    }

    public boolean isFinishByAccident() {
        return isFinishByAccident;
    }

    public interface OnTimeAction {
        void update(long timeLeft);

        void checkForPackage();

        void finish();
    }

}
