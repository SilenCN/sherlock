package com.wocao.sherlock.ForceUnlock;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.wocao.sherlock.R;

/**
 * Created by silen on 17-6-20.
 */

public class CipherReviewDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.resetCipher_CipherRegTitle);
        builder.setMessage(R.string.resetCipher_CipherReg);
        builder.setNegativeButton(R.string.FirstUse_policyAlarmKnown,null);
        return builder.create();
    }
}
