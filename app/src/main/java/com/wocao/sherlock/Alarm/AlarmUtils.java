package com.wocao.sherlock.Alarm;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by silen on 17-12-18.
 */

public class AlarmUtils {
    public static int getTime(long time){
        return Integer.parseInt(new SimpleDateFormat("YYYYMMDD").format(new Date(time)));
    }
}
