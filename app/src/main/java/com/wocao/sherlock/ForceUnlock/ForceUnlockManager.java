package com.wocao.sherlock.ForceUnlock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.wocao.sherlock.AESUtils;

/**
 * Created by silen on 17-4-4.
 */

public class ForceUnlockManager {
    public static void gotoActivity(Context context) {
        SharedPreferences sp=context.getSharedPreferences("data",Context.MODE_PRIVATE);
        try {
            if (sp.getBoolean(AESUtils.encrypt("wocstudiosoftware", "theFirstTimeToShowCipherReg"), true)||!sp.getBoolean("useInputCipher",true)||!sp.getBoolean(AESUtils.encrypt("wocstudiosoftware", "useCipher"), true)){
                context.startActivity(new Intent(context,RandomCipherActivity.class)/*.putExtra("DATA",true)*/);
/*                sp.edit().putBoolean("useInputCipher",false).commit();*/
            }else{
                context.startActivity(new Intent(context,InputCipher.class).putExtra("ResetCipher", true));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getRandomCipher(int length){
        String cipher=new String();
        cipher= Base64.encodeToString((Math.random()*10000000+""+Math.random()*10000000).getBytes(),Base64.DEFAULT).substring(0,length);

        return cipher;
    }
}
