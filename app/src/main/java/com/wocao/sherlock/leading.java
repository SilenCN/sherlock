package com.wocao.sherlock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wocao.sherlock.Assist.AssistUtils;
import com.wocao.sherlock.ForceUnlock.ForceUnlockManager;
import com.wocao.sherlock.Main.MainActivity;
import com.wocao.sherlock.ModeOperate.Server.InitMode;
import com.wocao.sherlock.ModeOperate.View.ModeListDisplayDialog;

import java.io.File;
import java.io.FileInputStream;


public class leading extends AppCompatActivity {

    RelativeLayout layout1, layout2, layout3;
    Button intall, whitelist, forceUnlock, intoMain, startSet;
    SharedPreferences sp;
    TextView tv1, tv2;

    int TopIntoToken = 0;
    int TopOutToken = 0;

    int staticToken = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.leading);

        sp = getSharedPreferences("data", MODE_PRIVATE);

        layout1 = (RelativeLayout) findViewById(R.id.loadingTopLayout);
        layout2 = (RelativeLayout) findViewById(R.id.loadingLayoutInstall);
        layout3 = (RelativeLayout) findViewById(R.id.loadingLayoutSetting);
        startSet = (Button) findViewById(R.id.loadingStartSet);
        intall = (Button) findViewById(R.id.loadingInstall);
        intoMain = (Button) findViewById(R.id.loadingIntoMain);
        whitelist = (Button) findViewById(R.id.loadingAppWhiteList);
        forceUnlock = (Button) findViewById(R.id.loadingForceUnLock);
        tv1 = (TextView) findViewById(R.id.loadingWelcomeTitle);
        tv2 = (TextView) findViewById(R.id.loadingMessage);
        forceUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForceUnlockManager.gotoActivity(leading.this);
                //startActivity(new Intent(leading.this, InputCipher.class).putExtra("ResetCipher", true));
            }
        });

        intall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssistUtils.installAssistWithoutDialog(leading.this);
            }
        });
        whitelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ModeListDisplayDialog().show(getSupportFragmentManager(), null);
            }
        });
        intoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp.edit().putBoolean("FirstUse", false).commit();
                startActivity(new Intent(leading.this, MainActivity.class));

                finish();
            }
        });

        startSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OperateTopLayoutOut(tv1);
            }
        });
        OperateTopLayoutInto(tv1);
        if (sp.getBoolean("FirstUseMode",true)){
            new InitMode(leading.this).execute("");
            sp.edit().putBoolean("FirstUseMode",false).commit();
        }
    }

    private void OperateTopLayoutInto(Object view) {
        Animation anim = AnimationUtils.loadAnimation(leading.this, R.anim.leading_toplayout_in);
        if (TopIntoToken == 2) {
            ((Button) view).startAnimation(anim);
            ((Button) view).setVisibility(View.VISIBLE);
        } else {
            ((View) view).startAnimation(anim);
            ((View) view).setVisibility(View.VISIBLE);
        }
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TopIntoToken++;
                switch (TopIntoToken) {
                    case 1:
                        OperateTopLayoutInto(tv2);
                        break;
                    case 2:
                        OperateTopLayoutInto(startSet);
                        break;
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void OperateTopLayoutOut(final Object view) {

        startSet.setClickable(false);

        Animation anim = AnimationUtils.loadAnimation(leading.this, R.anim.leading_toplayout_out);
        if (TopOutToken == 2) {
            ((Button) view).startAnimation(anim);
            ((Button) view).setVisibility(View.VISIBLE);
        } else {
            ((View) view).startAnimation(anim);
            ((View) view).setVisibility(View.VISIBLE);
        }
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TopOutToken++;
                switch (TopOutToken) {
                    case 1:
                        ((View) view).setVisibility(View.INVISIBLE);
                        OperateTopLayoutOut(tv2);
                        break;
                    case 2:
                        ((View) view).setVisibility(View.INVISIBLE);
                        OperateTopLayoutOut(startSet);
                        break;
                    case 3:
                        ((Button) view).setVisibility(View.INVISIBLE);
                        layout1.setVisibility(View.GONE);
                        OperateInstallLayoutIn(layout2);
                        staticToken = 2;
                        break;
                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void OperateInstallLayoutIn(Object view) {
        Animation anim = AnimationUtils.loadAnimation(leading.this, R.anim.leading_installlayout_in);
        ((RelativeLayout) view).startAnimation(anim);
        ((RelativeLayout) view).setVisibility(View.VISIBLE);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (AssistUtils.checkAssistIsInstall(leading.this)) {
                    OperateInstallLayoutOut1(layout2);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void OperateInstallLayoutOut1(Object view) {
        intall.setClickable(false);
        Animation anim = AnimationUtils.loadAnimation(leading.this, R.anim.leading_installlayout_out);
        anim.setFillAfter(true);
        ((RelativeLayout) view).startAnimation(anim);
        ((RelativeLayout) view).setVisibility(View.VISIBLE);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                staticToken = 0;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                OperateInstallLayoutOut2(layout2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void OperateInstallLayoutOut2(Object view) {
        Animation anim = AnimationUtils.loadAnimation(leading.this, R.anim.leading_installlayout_out1);
        anim.setFillAfter(true);
        ((RelativeLayout) view).startAnimation(anim);
        ((RelativeLayout) view).setVisibility(View.VISIBLE);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout2.setVisibility(View.GONE);
                OperateSettingLayoutIn(layout3);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void OperateSettingLayoutIn(Object view) {
        Animation anim = AnimationUtils.loadAnimation(leading.this, R.anim.leading_settinglayout_in);
        anim.setFillAfter(true);
        ((RelativeLayout) view).startAnimation(anim);
        ((RelativeLayout) view).setVisibility(View.VISIBLE);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                recoveryCipher();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    protected void onResume() {
        super.onResume();
        if (staticToken == 2 && AssistUtils.checkAssistIsInstall(leading.this)) {
            OperateInstallLayoutOut1(layout2);
        }
    }

    private void recoveryCipher() {
        if (AppConfig.CipherRecovery) {
            File file = new File(AppConfig.cipherPath);
            if (file.exists()) {
                try {
                    FileInputStream fi = new FileInputStream(file);
                    byte[] by = new byte[(int) (file.length())];
                    fi.read(by);
                    sp.edit().putString(AESUtils.encrypt("wocstudiosoftware", "cipher"), new String(by)).commit();
                    sp.edit().putBoolean(AESUtils.encrypt("wocstudiosoftware", "useCipher"), true).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

