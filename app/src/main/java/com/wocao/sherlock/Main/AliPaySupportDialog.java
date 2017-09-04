package com.wocao.sherlock.Main;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.wocao.sherlock.R;

/**
 * Created by silen on 17-6-9.
 */

public class AliPaySupportDialog extends DialogFragment {
    Context context;

    public AliPaySupportDialog(Context context) {
        super();
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(getResources().getString(R.string.donate_alipay_count));
        builder.setTitle(R.string.donate_alipay_text);
        builder.setMessage(R.string.donate_alipay_dialogTV);
        builder.setPositiveButton(R.string.donate_alipay_openAlipay, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://ds.alipay.com/?from=mobilecodec&scheme=alipayqr%3A%2F%2Fplatformapi%2Fstartapp%3FsaId%3D10000007%26clientVersion%3D3.7.0.0718%26qrcode%3Dhttps%253A%252F%252Fqr.alipay.com%252Fapx05582lqz2lsejkj6r5c9%253F_s%253Dweb-other")));
            }
        });
        return builder.create();
    }
}
