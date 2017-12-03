package com.wocao.sherlock.ModeOperate.View;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.wocao.sherlock.R;
import com.wocao.sherlock.appTool;

/**
 * Created by silen on 17-12-3.
 */

public class ModeHelpDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.AppWhiteList_help);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.updata_web, null);

        WebView web = (WebView) view.findViewById(R.id.updata_web_webview);
        web.clearCache(true);
        web.loadUrl("file:///android_asset/" + appTool.getLanguageCode(getContext()) + "/AppWhiteList_help.html");
        builder.setView(view);
        builder.setPositiveButton(R.string.AppWhileList_help_known, null);

        return builder.create();
    }
}
