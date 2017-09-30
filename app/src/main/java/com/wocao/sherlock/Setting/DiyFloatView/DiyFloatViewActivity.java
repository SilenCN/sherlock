package com.wocao.sherlock.Setting.DiyFloatView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.wocao.sherlock.R;

public class DiyFloatViewActivity extends AppCompatActivity {

    private TextView mottoTv, timeTv;
    private Button forceLockLeft, forceLockRight;

    int mottoTvLastX = 0, mottoTvLastY = 0,timeTvLastX = 0,timeTvLastY = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.float_view);

        findView();
        viewTest();

    }


    private void viewTest() {

        mottoTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mottoTvLastX = x;
                        mottoTvLastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //计算移动的距离
                        int offX = x - mottoTvLastX;
                        int offY = y - mottoTvLastY;
                        mottoTv.offsetLeftAndRight(offX);
                        mottoTv.offsetTopAndBottom(offY);

                        System.out.println(mottoTv.getX());
                        break;
                }
                return true;
            }
        });
        timeTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                event.getX();
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        timeTvLastX = x;
                        timeTvLastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //计算移动的距离
                        int offX = x - timeTvLastX;
                        int offY = y - timeTvLastY;
                        mottoTv.offsetLeftAndRight(offX);
                        mottoTv.offsetTopAndBottom(offY);

                        System.out.println(mottoTv.getX());
                        break;
                }
                return true;
            }
        });

    }

    private void findView() {

        timeTv = (TextView) findViewById(R.id.lockserverUICoutdownTV);
        mottoTv = (TextView) findViewById(R.id.lockserverTVMotto);
        forceLockLeft = (Button) findViewById(R.id.LockUIForceButtonLeft);
        forceLockRight = (Button) findViewById(R.id.LockUIForceButtonRight);
    }
}
