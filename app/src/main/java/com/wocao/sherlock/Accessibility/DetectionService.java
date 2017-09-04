package com.wocao.sherlock.Accessibility;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

public class DetectionService extends AccessibilityService {
    final static String TAG = "DetectionService";

    public static String foregroundPackageName;

    private OnWindowChangeListener onWindowChangeListener;

    private static DetectionService detectionService=null;
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public void onInterrupt() {

    }

    public DetectionService() {
        super();
        AccessibilityStatic.detectionService = this;
        detectionService=this;
    }

    public static DetectionService getInstance(){
        return detectionService;
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            /*
             * 如果 与 DetectionService 相同进程，直接比较 foregroundPackageName 的值即可
             * 如果在不同进程，可以利用 Intent 或 bind service 进行通信
             */
            foregroundPackageName = event.getPackageName().toString();
            if (null != onWindowChangeListener) {
                onWindowChangeListener.change(foregroundPackageName);
            }

            //   Log.i("foregroundClassName", event.toString());

        }
    }

    public void back() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }

    public void setOnWindowChangeListener(OnWindowChangeListener onWindowChangeListener) {
        this.onWindowChangeListener = onWindowChangeListener;
    }


    public interface OnWindowChangeListener {
        void change(String packageName);
    }

    public void finish() {
        onWindowChangeListener = null;
    }
}

