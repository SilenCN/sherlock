package com.wocao.sherlock.Assist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;

import com.wocao.sherlock.AppConfig;
import com.wocao.sherlock.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by silen on 17-6-9.
 */

public class AssistUtils {

    public static final String ASSIST_PACKAGE_NAME = "com.wocao.sherlockassist";

    public static boolean checkAssistIsInstall(Context context) {
        return checkAPP(context, ASSIST_PACKAGE_NAME);
    }

    public static boolean checkAPP(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static float getAssistVersion(Context context) {
        if (ASSIST_PACKAGE_NAME == null || "".equals(ASSIST_PACKAGE_NAME))
            return 1;
        try {
            PackageInfo pkg = context.getPackageManager().getPackageInfo(ASSIST_PACKAGE_NAME, 0);
            return Float.parseFloat(pkg.versionName.intern());
        } catch (Exception e) {
            return 1;
        }
    }

    public static boolean checkAssistVersion(Context context) {
        if (ASSIST_PACKAGE_NAME == null || "".equals(ASSIST_PACKAGE_NAME))
            return false;
        try {
            PackageInfo pkg = context.getPackageManager().getPackageInfo(ASSIST_PACKAGE_NAME, 0);
            if (AppConfig.assistAppVersion.equals(pkg.versionName)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkAssistUpdate(final Context context) {
        if (AssistUtils.checkAssistVersion(context)) {
            return true;
        } else {
            android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.updata_assist_title);
            builder.setMessage(R.string.updata_assist_message);
            builder.setNegativeButton(R.string.updata_assist_install, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    installAssistWithoutDialog(context);
                }
            });
            builder.show();

            return false;
        }
    }


    public static void installAssist(final Context context) {
        android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.apptool_installAssist);
        builder.setMessage(R.string.apptool_installAssistMessage);
        builder.setNegativeButton(R.string.apptool_installAssistInstall, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AssistUtils.installAssistWithoutDialog(context);
            }
        });
        builder.create();

    }

    public static void installAssistWithoutDialog(Context context) {

        File filec = new File(Environment.getExternalStorageDirectory().getPath() + "/Sherlock");
        if (!filec.exists()) {
            filec.mkdirs();
        }
        try {
            InputStream fromFileIs = context.getResources().getAssets().open("app-release.apk");

            int length = fromFileIs.available();

            byte[] buffer = new byte[length];

            FileOutputStream fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/Sherlock/assist.apk");

            BufferedInputStream bufferedInputStream = new BufferedInputStream(fromFileIs);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            int len = bufferedInputStream.read(buffer);
            while (len != -1) {
                bufferedOutputStream.write(buffer, 0, len);
                len = bufferedInputStream.read(buffer);
            }
            bufferedInputStream.close();
            bufferedOutputStream.close();
            fromFileIs.close();
            fileOutputStream.close();

            Intent intentapp = new Intent(Intent.ACTION_VIEW);
            intentapp.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath() + "/Sherlock/assist.apk")), "application/vnd.android.package-archive");
            context.startActivity(intentapp);

        } catch (IOException e) {
        }
    }
}
