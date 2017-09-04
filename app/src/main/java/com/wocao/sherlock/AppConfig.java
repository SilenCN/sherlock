package com.wocao.sherlock;


/**
 * Created by 新宇 on 2015/11/21.
 */
public class AppConfig {
    public static boolean haveBootStart=false;
    public static boolean alarmRestartToken=true;
    public static int AlarmIDIntent=0;
    public static int AlarmNotificationID=0;
    public static int cipherRepeatMaxLength=4;
    public static boolean ignoreDeviceAdmin=true;
    public static String assistAppVersion="3.0";
    public static String cipherPath="/storage/sdcard0/Android/data/.token";
    public static String homesPackage="";
    public static final int  START_AFTER_UNLOCK_DURATION= 10*60*1000;
    public static boolean debugForFlyme=false;

    public static boolean CipherRecovery=false;

}
