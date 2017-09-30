package com.wocao.sherlock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wocao.sherlock.CoreService.FloatWindowAppList;
import com.wocao.sherlock.Setting.DiyFloatView.Widget.MovableTextClock;
import com.wocao.sherlock.Widget.BottomCard;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        BottomCard bottomCard=(BottomCard)findViewById(R.id.BottomCardTest);
        new FloatWindowAppList(this,bottomCard,0);
        
        ((MovableTextClock)findViewById(R.id.movableTextClock)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TestActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
