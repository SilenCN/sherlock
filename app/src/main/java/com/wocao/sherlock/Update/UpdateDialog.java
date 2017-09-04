package com.wocao.sherlock.Update;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.wocao.sherlock.R;

/**
 * Created by silen on 17-6-9.
 */

public class UpdateDialog extends DialogFragment {
    private String newVersion;
    private String updateInfo;
    private String newLink;
    private SharedPreferences sharedPreferences;
    public UpdateDialog(String newVersion, String updateInfo, String newLink, SharedPreferences sharedPreferences) {
        this.newLink = newLink;
        this.newVersion = newVersion;
        this.updateInfo = updateInfo;
        this.sharedPreferences=sharedPreferences;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getActivity().getResources().getString(R.string.updata_title) + "-v" + newVersion);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.updata_web, null);
        WebView web = (WebView) view.findViewById(R.id.updata_web_webview);
        web.clearCache(true);
        //     web.loadUrl(fatherPath + "updateInfo.html");

        web.loadData(updateInfo, "text/html", "UTF-8");

        builder.setView(view);
        builder.setPositiveButton(R.string.updata_download, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(newLink)));
            }
        });
        builder.setNeutralButton(R.string.updata_ignore, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferences.edit().putBoolean(newVersion, false).commit();
            }
        });
        builder.setNegativeButton(R.string.updata_nextTime,null);

        return builder.create();
    }
}
