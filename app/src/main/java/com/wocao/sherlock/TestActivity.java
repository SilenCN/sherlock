package com.wocao.sherlock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wocao.sherlock.CoreService.FloatWindowAppList;
import com.wocao.sherlock.Widget.BottomCard;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        BottomCard bottomCard=(BottomCard)findViewById(R.id.BottomCardTest);
        new FloatWindowAppList(this,bottomCard,0);
    }
}
