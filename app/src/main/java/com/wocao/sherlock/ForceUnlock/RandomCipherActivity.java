package com.wocao.sherlock.ForceUnlock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wocao.sherlock.AESUtils;
import com.wocao.sherlock.R;
import com.wocao.sherlock.MaterialDesign.StatusBarUtils;

public class RandomCipherActivity extends AppCompatActivity {

    Toolbar mToolBar;
    SwitchCompat switchCompat;
    TextView displayTV;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_cipher);
        new StatusBarUtils().setStatusBar(this);
        mToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        try {
            getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP, android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP);
        } catch (Exception e) {
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("解锁密码");

        sp = getSharedPreferences("data", MODE_PRIVATE);

        if(sp.contains("useInputCipher")){
            sp.edit().putBoolean("useInputCipher",false).commit();
        }
        initView();
        initCipher();

    }

    private void refreshCipher(int length) {
        String cipher = ForceUnlockManager.getRandomCipher(length);
        displayTV.setText(cipher);
        try {
            sp.edit().putString(AESUtils.encrypt("wocstudiosoftware", "cipher"), AESUtils.encrypt("wocstudiosoftware", cipher)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCipher() {
        try {

            if (!sp.contains(AESUtils.encrypt("wocstudiosoftware", "cipher"))) {
                refreshCipher(getCipherLength());
            } else if (!sp.contains("useInputCipher")) {
                refreshCipher(getCipherLength());
                sp.edit().putBoolean("useInputCipher", false).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        switchCompat = (SwitchCompat) findViewById(R.id.ActivityRandomCipherSwitch);
        displayTV = (TextView) findViewById(R.id.ActivityRandomCipherDisplayTV);
        try {
            switchCompat.setChecked(sp.getBoolean(AESUtils.encrypt("wocstudiosoftware", "useCipher"), true));
            sp.edit().putBoolean(AESUtils.encrypt("wocstudiosoftware", "useCipher"), sp.getBoolean(AESUtils.encrypt("wocstudiosoftware", "useCipher"), true)).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            displayTV.setText(AESUtils.decrypt("wocstudiosoftware", sp.getString(AESUtils.encrypt("wocstudiosoftware", "cipher"), null)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    sp.edit().putBoolean(AESUtils.encrypt("wocstudiosoftware", "useCipher"), isChecked).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isChecked) {
                    Toast.makeText(RandomCipherActivity.this, "已开启紧急解锁", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RandomCipherActivity.this, "已关闭紧急解锁", Toast.LENGTH_SHORT).show();
/*                    try {
                        sp.edit().remove(AESUtils.encrypt("wocstudiosoftware", "cipher")).commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/

                }

            }
        });
    }


    MenuItem displayCipher;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_forceunlock_random_menu, menu);
        displayCipher = menu.findItem(R.id.activity_forceUnlock_randomMenu_show_cipher);
        displayCipher.setCheckable(true);
        displayCipher.setChecked(sp.getBoolean(AESUtils.encrypt("SILEN","DisplayCipherInUnlock"), false));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.activity_forceUnlock_randomMenu_reset:
                refreshCipher(getCipherLength());
                break;
            case R.id.activity_forceUnlock_randomMenu_show_cipher:
                sp.edit().putBoolean(AESUtils.encrypt("SILEN","DisplayCipherInUnlock"), !displayCipher.isChecked()).commit();
                refreshCipher(getCipherLength());

                displayCipher.setChecked(!displayCipher.isChecked());
                break;
            case R.id.activity_forceUnlock_randomMenu_use_diy:
                try {
                    sp.edit().remove(AESUtils.encrypt("wocstudiosoftware", "cipher")).commit();
                    if (switchCompat.isChecked()) {
                        sp.edit().remove(AESUtils.encrypt("wocstudiosoftware", "useCipher")).commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sp.edit().putBoolean("useInputCipher", true).commit();
                Toast.makeText(RandomCipherActivity.this, "原随机密码已弃用，已关闭紧急解锁", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RandomCipherActivity.this, InputCipher.class).putExtra("ResetCipher", true));
                finish();
                break;
            case R.id.activity_forceUnlock_randomMenu_use_help:
                new RandomHelpDialog().show(getSupportFragmentManager(), null);
                break;
        }
        return true;
    }

    public int getCipherLength() {
        if (sp.getBoolean(AESUtils.encrypt("SILEN","DisplayCipherInUnlock"), false)) {
            return 30;
        } else {
            return 15;
        }
    }

}
