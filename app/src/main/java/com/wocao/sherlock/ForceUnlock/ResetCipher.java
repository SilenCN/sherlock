package com.wocao.sherlock.ForceUnlock;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wocao.sherlock.AESUtils;
import com.wocao.sherlock.AppConfig;
import com.wocao.sherlock.ExitApplication;
import com.wocao.sherlock.R;
import com.wocao.sherlock.SystemBarTintManager;
import com.wocao.sherlock.appTool;

import java.io.File;
import java.io.FileOutputStream;

public class ResetCipher extends AppCompatActivity {

    TextInputLayout inputLayout1, inputLayout2;
    EditText editText1, editText2;
    TextView countTV1, countTV2, reviewRegTV;
    SharedPreferences sp;
    SwitchCompat useSwitch;
    boolean use;
    android.support.v7.widget.Toolbar mToolBar;
    MenuItem menuItem;
    final int minCipherLength = 15;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cipher_reset);
        setStatusBar();

        if (!getIntent().getBooleanExtra("DATA", false)) {
            toastText(R.string.CipherReset_IntentIlligel);
            finish();
        }

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        } catch (Exception e) {
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ExitApplication.getInstance().addActivity(this);
        sp = getSharedPreferences("data", MODE_PRIVATE);


        inputLayout1 = (TextInputLayout) findViewById(R.id.CipherInputEditLayout1);
        inputLayout2 = (TextInputLayout) findViewById(R.id.CipherInputEditLayout2);
        editText1 = (EditText) findViewById(R.id.CipherInputEditText1);
        editText2 = (EditText) findViewById(R.id.CipherInputEditText2);
        reviewRegTV = (TextView) findViewById(R.id.recetCipherReviewGegTV);
        countTV1 = (TextView) findViewById(R.id.CipherInputNnmCountTV1);
        countTV2 = (TextView) findViewById(R.id.CipherInputNnmCountTV2);
        useSwitch = (SwitchCompat) findViewById(R.id.CipherResetSwitchCompatUseSwitch);

        try {
            useSwitch.setChecked(sp.getBoolean(AESUtils.encrypt("wocstudiosoftware", "useCipher"), true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        countTV1.setVisibility(View.INVISIBLE);
        countTV2.setVisibility(View.INVISIBLE);

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputLayout1.setErrorEnabled(false);
                inputLayout2.setErrorEnabled(false);
                countTV1.setVisibility(View.VISIBLE);
                countTV1.setText(s.length() + "/" + minCipherLength);
                if (s.length() >= minCipherLength && editText2.getText() != null && editText2.getText().length() >= minCipherLength) {
                    menuItem.setVisible(true);
                } else if (s.length() == 0) {
                    countTV1.setVisibility(View.INVISIBLE);
                } else {
                    menuItem.setVisible(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputLayout2.setErrorEnabled(false);
                inputLayout1.setErrorEnabled(false);
                countTV2.setVisibility(View.VISIBLE);
                countTV2.setText(s.length() + "/" + minCipherLength);
                if (s.length() >= minCipherLength && editText1.getText() != null && editText1.getText().length() >= minCipherLength) {
                    menuItem.setVisible(true);
                } else if (s.length() == 0) {
                    countTV2.setVisibility(View.INVISIBLE);
                } else {
                    menuItem.setVisible(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        useSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    try {
                        sp.edit().putBoolean(AESUtils.encrypt("wocstudiosoftware", "useCipher"), false).commit();
                        sp.edit().remove(AESUtils.encrypt("wocstudiosoftware", "cipher")).commit();
                        sp.edit().putBoolean("useInputCipher",false).commit();
                        toastText(R.string.CipherReset_NotUseCipher);
                        File file = new File(AppConfig.cipherPath);
                        if (file.exists()) {
                            file.delete();
                        }
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        reviewRegTV.setClickable(true);
        reviewRegTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CipherReviewDialog().show(getSupportFragmentManager(),null);
                //new cipherReviewDialog().show(getSupportFragmentManager(), "");
            }
        });

        try {
            if (sp.getBoolean(AESUtils.encrypt("wocstudiosoftware", "theFirstTimeToShowCipherReg"), true)) {
                //cipherReviewDialog crd = new cipherReviewDialog();
                CipherReviewDialog crd=new CipherReviewDialog();
                crd.setCancelable(false);
                crd.show(getSupportFragmentManager(), null);
                sp.edit().putBoolean(AESUtils.encrypt("wocstudiosoftware", "theFirstTimeToShowCipherReg"), false).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!sp.getBoolean("FirstUse", true))
            toastText(R.string.CipherReset_ResetFailed);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO: Implement this method
        this.getMenuInflater().inflate(R.menu.activity_forceunlock_diy_menu, menu);
        menuItem = menu.add(1, 1, 1, R.string.CipherReset_Reset);
        menuItem.setTitle(R.string.CipherReset_Reset);
        //menuItem.setIcon(android.R.drawable.ic_media_play);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        menuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: Implement this method
        switch (item.getItemId()) {
            case android.R.id.home:
                toastText(R.string.CipherReset_ResetFailed);
                finish();
                break;
            case 1:
                if (editText1.getText().toString().equals(editText2.getText().toString())) {
                    if (appTool.cipherIsIlligel(editText1.getText().toString())) {
                        if (AppConfig.CipherRecovery)
                            try {
                                File file = new File(AppConfig.cipherPath);
                                if (file.exists()) {
                                    file.delete();
                                }
                                file.createNewFile();
                                FileOutputStream fp = new FileOutputStream(file);
                                fp.write(AESUtils.encrypt("wocstudiosoftware", editText1.getText().toString()).getBytes());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        try {
                            sp.edit().putString(AESUtils.encrypt("wocstudiosoftware", "cipher"), AESUtils.encrypt("wocstudiosoftware", editText1.getText().toString())).commit();
                            sp.edit().putBoolean(AESUtils.encrypt("wocstudiosoftware", "useCipher"), true).commit();
                            toastText(R.string.CipherReset_ResetSuccess);
                            finish();
                        } catch (Exception e) {

                        }
                    } else {
                        editText1.setText("");
                        editText2.setText("");
                        countTV1.setVisibility(View.GONE);
                        countTV2.setVisibility(View.GONE);
                        inputLayout1.setError(getResources().getString(R.string.resetCipher_checkCipherIsIlligel));
                        inputLayout2.setError(getResources().getString(R.string.resetCipher_checkCipherIsIlligel));
                        inputLayout1.setErrorEnabled(true);
                        inputLayout2.setErrorEnabled(true);
                        menuItem.setVisible(false);
                    }

                } else {
                    editText1.setText("");
                    editText2.setText("");
                    countTV1.setVisibility(View.GONE);
                    countTV2.setVisibility(View.GONE);
                    inputLayout1.setError(getResources().getString(R.string.CipherReset_CipherNotSame));
                    inputLayout2.setError(getResources().getString(R.string.CipherReset_CipherNotSame));
                    inputLayout1.setErrorEnabled(true);
                    inputLayout2.setErrorEnabled(true);
                    menuItem.setVisible(false);
                }
                break;
            case R.id.activity_forceUnlock_diyMenu_rule:new CipherReviewDialog().show(getSupportFragmentManager(),null);break;
            case R.id.activity_forceUnlock_diyMenu_use_random:
                try {
                    sp.edit().remove(AESUtils.encrypt("wocstudiosoftware", "cipher")).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sp.edit().putBoolean("useInputCipher",false).commit();
                Toast.makeText(ResetCipher.this, "原密码已弃用", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResetCipher.this,RandomCipherActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    public class cipherReviewDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.resetCipher_CipherRegTitle);
            builder.setMessage(R.string.resetCipher_CipherReg);
            builder.setNegativeButton(R.string.FirstUse_policyAlarmKnown, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            return builder.create();

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(0);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarAlpha(0);
        tintManager.setNavigationBarAlpha(0);
        tintManager.setTintAlpha(0);

    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void toastText(int stringID) {
        Toast.makeText(ResetCipher.this, stringID, Toast.LENGTH_SHORT).show();

    }
}