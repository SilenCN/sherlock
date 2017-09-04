package com.wocao.sherlock.ForceUnlock;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.wocao.sherlock.AESUtils;
import com.wocao.sherlock.R;
import com.wocao.sherlock.SystemBarTintManager;

public class InputCipher extends AppCompatActivity {

    Toolbar mToolBar;
    TextInputLayout inputLayout;
    EditText editText;
    SharedPreferences sp;
    TextView textView;
    final int minCipherLength = 15;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cipher_input);
        setStatusBar();

        sp =getSharedPreferences("data", MODE_PRIVATE);
        try {
            if (!sp.getBoolean(AESUtils.encrypt("wocstudiosoftware","useCipher"),false)||!sp.contains(AESUtils.encrypt("wocstudiosoftware", "cipher"))){
                startActivity(new Intent(InputCipher.this,ResetCipher.class).putExtra("DATA",true));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        try {
            getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP, android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP);
        } catch (Exception e) {
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputLayout = (TextInputLayout) findViewById(R.id.CipherInputEditLayout);
        editText = (EditText) findViewById(R.id.CipherInputEditText);
        textView = (TextView) findViewById(R.id.CipherInputNnmCountTV);
        textView.setVisibility(View.INVISIBLE);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputLayout.setErrorEnabled(false);
                textView.setVisibility(View.VISIBLE);
                textView.setText(s.length() + "/" + minCipherLength);
                if (s.length() >= minCipherLength) {
                    menuItem.setVisible(true);
                } else if (s.length() == 0) {
                    textView.setVisibility(View.INVISIBLE);
                } else {
                    menuItem.setVisible(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO: Implement this method

        menuItem = menu.add(1, 1, 1, R.string.InputCipher_next);
        menuItem.setTitle(R.string.InputCipher_next);
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
                finish();
                break;
            case 1:
                try {
                    if (AESUtils.encrypt("wocstudiosoftware", editText.getText().toString()).equals(sp.getString(AESUtils.encrypt("wocstudiosoftware", "cipher"), "csdc"))) {
                        startActivity(new Intent(InputCipher.this, ResetCipher.class).putExtra("DATA",true));
                        overridePendingTransition(R.anim.fade, R.anim.hold);
                        finish();
                    } else {
                        editText.setText("");
                        textView.setVisibility(View.GONE);
                        inputLayout.setError(getResources().getString(R.string.InputCipher_inputError));
                        inputLayout.setErrorEnabled(true);
                        menuItem.setVisible(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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


}


