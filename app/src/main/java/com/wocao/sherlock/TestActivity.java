package com.wocao.sherlock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wocao.sherlock.Widget.ColorPickerDialog;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        new ColorPickerDialog(this).show(getSupportFragmentManager(),null);
    }
}
