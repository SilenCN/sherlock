package com.wocao.sherlock;

import android.app.Application;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Created by 新宇 on 2015/12/8.
 */
public class FunctionApplication extends Application {
    String languageToLoad="zh";

    @Override
    public void onCreate() {
        super.onCreate();
        Locale locale=new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config=getResources().getConfiguration();
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        config.locale=Locale.CHINESE;
        getResources().updateConfiguration(config,metrics);
    }
}
