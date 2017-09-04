package com.wocao.sherlock.ControlByOther.Presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.wocao.sherlock.ControlByOther.View.PasswordInputDialog;
import com.wocao.sherlock.Setting.SettingUtils;
import com.wocao.sherlock.Shortcut.View.OnDialogResultListener;

/**
 * Created by silen on 17-4-25.
 */

public class ControlByOtherDialogManager {
    Context context;
   // SharedPreferences sp;
    private OnSuccessListener onSuccessListener;
    public ControlByOtherDialogManager(Context context) {
        this.context = context;
    //    sp=PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void test(FragmentManager fragmentManager,String type){

        if (isTurnOn()&&getValue(type)){
            PasswordInputDialog passwordInputDialog=new PasswordInputDialog();
            passwordInputDialog.show(fragmentManager,null);
            passwordInputDialog.setOnDialogResultListener(new OnDialogResultListener() {
                @Override
                public void onResult(Object result) {
                    returnCheck();
                }
            });
        }else{
            returnCheck();
        }
    }

    public boolean isTurnOn(){
        return SettingUtils.getBooleanValue(context,"control_by_other_switch",false);
       // return sp.getBoolean("control_by_other_switch",false);
    }

    public boolean getValue(String type){
        return SettingUtils.getBooleanValue(context,type,true);
      //  return sp.getBoolean(type,true);
    }
    public String getPassword(){
        return SettingUtils.getStringValue(context,"control_by_other_password",null);
       // return sp.getString("control_by_other_password",null);
    }

    private void returnCheck(){
        if (null!=onSuccessListener){
            onSuccessListener.success();
        }
    }
    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    public interface OnSuccessListener{
        void success();
    }
}
