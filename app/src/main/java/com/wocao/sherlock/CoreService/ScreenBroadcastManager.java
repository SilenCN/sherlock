package com.wocao.sherlock.CoreService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by silen on 17-5-3.
 */

public class ScreenBroadcastManager {
    private OnScreenOnListener onScreenOnListener;
    private ScreenBroadcastReceiver receiver;

    private Context context;
    public ScreenBroadcastManager(Context context) {
        this.context=context;
    }

    public void setOnScreenOnListener(OnScreenOnListener onScreenOnListener) {
        this.onScreenOnListener = onScreenOnListener;
    }

    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                if (null != onScreenOnListener) {
                    onScreenOnListener.on();
                }
            }
        }
    }

    public interface OnScreenOnListener {
        void on();
    }

    public void startScreenBroadcastReceiver() {
        receiver = new ScreenBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(receiver, filter);
    }

    public void finish() {
        if (null != receiver) {
            context.unregisterReceiver(receiver);
        }
    }
}
