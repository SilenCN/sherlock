package com.wocao.sherlock.CoreService;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.util.Log;

import com.wocao.sherlock.Accessibility.DetectionService;
import com.wocao.sherlock.AppWidget.ServiceState.ServiceStateStatic;
import com.wocao.sherlock.AppWidget.ServiceState.ServiceStateWidget;
import com.wocao.sherlock.DataBaseOperate.ModeListDBTool;
import com.wocao.sherlock.R;

public class CoreService extends Service {

    DataManager dataManager;
    FloatWindowManager floatWindowManager;
    PackageManager packageManager;
    ScreenBroadcastManager screenBroadcastManager;
    TimeManager timeManager;
    DetectionService detectionService;

    @Override
    public void onCreate() {
        super.onCreate();
        dataManager = new DataManager(this);
        screenBroadcastManager = new ScreenBroadcastManager(this);
        timeManager = new TimeManager(this);
        packageManager = new PackageManager(this, dataManager);
        if (!dataManager.isUseDesktop()) {
            floatWindowManager = new FloatWindowManager(this, timeManager.getDurationLeft(), packageManager.getModeId());
        } else {
            ServiceStateWidget.update(this,true);
        }
        if (dataManager.isUseAccessibility()) {
            detectionService = DetectionService.getInstance();
        }

        ServiceStateStatic.MODE_NAME = new ModeListDBTool(this).getModeNameById(packageManager.getModeId());
        setListener();
        start();

    }

    private void start() {

        timeManager.start();
        if (!dataManager.isUseDesktop())
            floatWindowManager.showFloatWindow();
        screenBroadcastManager.startScreenBroadcastReceiver();

    }

    private void setListener() {
        screenBroadcastManager.setOnScreenOnListener(new ScreenBroadcastManager.OnScreenOnListener() {
            @Override
            public void on() {
                Log.i("screenBroadcastManager","On");
                timeManager.reNewTime();
            }
        });
        if (dataManager.isUseAccessibility()) {
            detectionService.setOnWindowChangeListener(new DetectionService.OnWindowChangeListener() {
                @Override
                public void change(String packageName) {

                    boolean canRun = packageManager.checkPackage(packageName.equals(dataManager.getDefaultInputMethod()) ? null : packageName);
                    if (!canRun)
                        detectionService.back();
                    if (!dataManager.isUseDesktop()) {
                        floatWindowManager.canRun(canRun);
                    }
                }
            });
        }

        timeManager.setOnTimeAction(new TimeManager.OnTimeAction() {
            @Override
            public void update(long timeLeft) {
                if (dataManager.isUseDesktop()) {
                    ServiceStateStatic.TIME_DISPLAY = timeCalculate(timeLeft);
                   // if (ServiceStateManager.getHaveAppWidget(CoreService.this)) {
                        ServiceStateWidget.update(CoreService.this,true);
                       // CoreService.this.sendBroadcast(new Intent(ServiceStateWidget.APPWIDGET_UPDATE_ALL));
                  //  }
                } else {
                    floatWindowManager.updateView(timeLeft);
                }
            }

            @Override
            public void checkForPackage() {
                boolean canRun = packageManager.checkPackage(packageManager.getCurrentPkgName());
                if (!dataManager.isUseDesktop())
                    floatWindowManager.canRun(canRun);
            }

            @Override
            public void finish() {
                if (!timeManager.isFinishByAccident()) {
                    if (dataManager.isPlaySoundRing())
                        soundRing();
                }
                stopSelf();
            }
        });

        if (null!=floatWindowManager){
            floatWindowManager.setOnShouldReNewTimeListener(new FloatWindowManager.OnShouldReNewTimeListener() {
                @Override
                public void reNew() {
                    if (null!=timeManager){
                        timeManager.reNewTime();
                    }
                }
            });
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       /* if (null != screenBroadcastManager)
            screenBroadcastManager.finish();*/
        if (null != timeManager)
            timeManager.reNewTime();
        if (null != packageManager)
            packageManager.updateModeId();
        if (null != floatWindowManager)
            floatWindowManager.updateModeId(packageManager.getModeId());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != screenBroadcastManager)
            screenBroadcastManager.finish();
        if (null != timeManager)
            timeManager.finish();
        if (null != detectionService)
            detectionService.finish();
        if (null != floatWindowManager)
            floatWindowManager.distrory();

        if (dataManager.isUseDesktop()){
            ServiceStateWidget.update(this,false);
        }
    /*    ServiceStateStatic.isServiceRunning = false;
        // if (ServiceStateStatic.hasAppWidget) {
        sendBroadcast(new Intent(ServiceStateWidget.APPWIDGET_UPDATE_ALL));*/
        // }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String timeCalculate(long i) {
        long length = i;
        int countDay, countHour, countMinu, countSec;
        String TvStr = "";
        countDay = (int) length / (60 * 60 * 24);
        countHour = (int) (length % (60 * 60 * 24)) / (60 * 60);
        countMinu = (int) (length % (60 * 60)) / 60;
        countSec = (int) (length % 60);
        if (countDay > 0) {
            TvStr = countDay + getResources().getString(R.string.killpoccesserve_day);
        }
        if (countHour > 0) {
            TvStr = TvStr + countHour + getResources().getString(R.string.killpoccesserve_hour);
        }
        if (countMinu > 0) {
            TvStr = TvStr + countMinu + getResources().getString(R.string.killpoccesserve_min);
        }
        if (countSec >= 0) {
            TvStr = TvStr + countSec + getResources().getString(R.string.killpoccesserve_scend);
        }

        return TvStr;
    }

    private void soundRing() {
        try {
            MediaPlayer mp = new MediaPlayer();
            mp.reset();
            mp.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
