package com.wocao.sherlock.Setting.DiyFloatView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wocao.sherlock.MaterialDesign.StatusBarUtils;
import com.wocao.sherlock.R;

public class DiyFloatViewDetailActivity extends AppCompatActivity {
    Toolbar mToolBar;
    private int viewId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewId=getIntent().getIntExtra("ViewId",0);

        setContentView(R.layout.activity_diy_float_view_detail);

        new StatusBarUtils().setStatusBar(this);

        mToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        try {
            getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP, android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP);
        } catch (Exception e) {
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("配置");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: Implement this method
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public int getViewId(){
        return viewId;
    }


}
