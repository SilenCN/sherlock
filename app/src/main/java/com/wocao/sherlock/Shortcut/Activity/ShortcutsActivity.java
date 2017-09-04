package com.wocao.sherlock.Shortcut.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.wocao.sherlock.R;
import com.wocao.sherlock.Shortcut.Model.ShortCut;
import com.wocao.sherlock.Shortcut.Presenter.ShortcutListViewAdapter;
import com.wocao.sherlock.Shortcut.Presenter.ShortcutManager;
import com.wocao.sherlock.MaterialDesign.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

public class ShortcutsActivity extends AppCompatActivity {
    Toolbar mToolBar;
    FloatingActionButton addButton;
    List<ShortCut> shortCutList=new ArrayList<>();
    ListView listView;
    TextView noItemTV;
    ShortcutManager shortcutManager;
    ShortcutListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcuts);

        new StatusBarUtils().setStatusBar(this);


        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        } catch (Exception e) {
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Shortcuts");

        init();
    }

    private void init() {
        shortcutManager=new ShortcutManager(this,this);
        shortCutList=shortcutManager.getList();
        findView();
        initView();
        listener();
    }

    private void listener() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addNewShortcut();
            }
        });
        adapter.setOnNumberChangeListener(new ShortcutListViewAdapter.OnNumberChangeListener() {
            @Override
            public void num(int num) {
                viewChange(num);
            }
        });
    }

    private void initView() {

        listView.setDivider(null);

        adapter=new ShortcutListViewAdapter(this,this);

        viewChange(adapter.getCount());

        listView.setAdapter(adapter);
    }

    private void viewChange(int num){
        if(num==0){
            noItemTV.setVisibility(View.VISIBLE);
        }else{
            noItemTV.setVisibility(View.GONE);
        }
        if (num>=5){
            addButton.setVisibility(View.GONE);
        }else{
            addButton.setVisibility(View.VISIBLE);
        }
    }
    private void findView() {
        listView=(ListView)findViewById(R.id.ShortcutListview);
        noItemTV=(TextView)findViewById(R.id.ShortcutListNoItemShowTv);
        addButton=(FloatingActionButton)findViewById(R.id.ShortcutListfloatActionButtonAdd);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item=menu.add(0,0,0,"帮助");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:finish();break;
            case 0://TODO:帮助信息
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
