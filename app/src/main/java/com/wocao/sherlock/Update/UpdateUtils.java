package com.wocao.sherlock.Update;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.wocao.sherlock.R;
import com.wocao.sherlock.Statistics.StatisticsThead;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 新宇 on 2015/12/17.
 */
public class UpdateUtils {

    private Context context;
    private String fatherPath = "http://sherlock-1252168079.cossh.myqcloud.com/update/";
    private String newVersion = "";
    private String newLink = "";
    private String updateInfo;
    private android.support.v4.app.FragmentManager fragmentManager;
    private SharedPreferences sp;
    private View view;
    private OnNoNewVersionListener onNoNewVersionListener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != onNoNewVersionListener)
                onNoNewVersionListener.onNoNewVersion();
        }
    };

    public UpdateUtils(Context context, android.support.v4.app.FragmentManager fragmentManager) {
        this.context = context;

        this.fragmentManager = fragmentManager;
        sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);

        startThread();
    }

    public void startThread() {
        new httpThred().start();
        new StatisticsThead(context).start();
    }

    public String getResultForHttpGet(String item) {
        String path = fatherPath + item;
        String result = "";
        try {
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(false);
            con.setRequestMethod("GET");
            con.setConnectTimeout(3000);

            if (con.getResponseCode() == 200) {

                InputStream is = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));

                String line = "";
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

        // result;
    }

    class httpThred extends Thread {
        @Override
        public void run() {
            synchronized (this) {
                try {

                    if (!context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName.equals(newVersion = getResultForHttpGet("version.html"))) {

                        if (sp.getBoolean(newVersion, true) && !newVersion.equals("") && Double.parseDouble(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName) < Double.parseDouble(newVersion)) {
                            newLink = getResultForHttpGet("link.html");
                            updateInfo = getResultForHttpGet("updateInfo.html");
                            System.out.println(updateInfo);
                            new UpdateDialog(newVersion, updateInfo, newLink, sp);
                        } else {
                            handler.sendEmptyMessage(new Message().what);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface OnNoNewVersionListener {
        void onNoNewVersion();
    }

    public void setOnNoNewVersionListener(OnNoNewVersionListener onNoNewVersionListener) {
        this.onNoNewVersionListener = onNoNewVersionListener;
    }
}
