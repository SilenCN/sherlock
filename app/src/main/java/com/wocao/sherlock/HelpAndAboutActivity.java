package com.wocao.sherlock;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.webkit.*;
import android.content.*;
import android.widget.Toast;

import com.wocao.sherlock.MaterialDesign.StatusBarUtils;
import com.wocao.sherlock.Update.UpdateUtils;


public class HelpAndAboutActivity extends AppCompatActivity {

    WebView web;

    Intent intent;

    public Toolbar mToolBar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.help_about_view);

        new StatusBarUtils().setStatusBar(this);

        mToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        try {
            getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP, android.support.v7.app.ActionBar.DISPLAY_HOME_AS_UP);
        } catch (Exception e) {
        }


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        web = (WebView) super.findViewById(R.id.helpaboutviewwebview);

        intent = this.getIntent();

        web.getSettings().setLoadsImagesAutomatically(true);
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setDatabaseEnabled(true);
        String databasePath = this.getApplicationContext()
                .getDir("database", Context.MODE_PRIVATE).getPath();
        web.getSettings().setDatabasePath(databasePath);
        web.getSettings().setAppCacheEnabled(true);
        String appCaceDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        web.getSettings().setAppCachePath(appCaceDir);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        web.getSettings().getJavaScriptEnabled();
        web.getSettings().getJavaScriptCanOpenWindowsAutomatically();
        /*

		 web.setWebViewClient(new WebViewClient(){
		 public void onPageFinished(WebView view,String url){

		 }

		 });
		 */
        web.setWebChromeClient(new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//构建一个Builder来显示网页中的对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(HelpAndAboutActivity.this);
                builder.setTitle("Alert");
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok,
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }

            //处理javascript中的confirm
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HelpAndAboutActivity.this);
                builder.setTitle("confirm");
                builder.setMessage(message);
                builder.setPositiveButton(android.R.string.ok,
                        new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        });
                builder.setCancelable(false);
                builder.create();
                builder.show();
                return true;
            }
        });

        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.toLowerCase().startsWith("update")) {
                    web.stopLoading();
                    new UpdateUtils(HelpAndAboutActivity.this,getSupportFragmentManager()).setOnNoNewVersionListener(new UpdateUtils.OnNoNewVersionListener() {
                        @Override
                        public void onNoNewVersion() {
                            Toast.makeText(HelpAndAboutActivity.this,getResources().getString(R.string.update_check_no_new_version),Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (url.toLowerCase().startsWith("file") || url.toLowerCase().startsWith("action"))
                    super.onPageStarted(view, url, favicon);
                else {
                    web.stopLoading();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                getSupportActionBar().setTitle(web.getTitle());
            }
        });
        if (intent.getStringExtra("Type").equals("关于")) {
            web.loadUrl("file:///android_asset/" + appTool.getLanguageCode(HelpAndAboutActivity.this) + "/about.html");
            getSupportActionBar().setTitle("关于");
        } else if (intent.getStringExtra("Type").equals("帮助")) {
            web.loadUrl("file:///android_asset/" + appTool.getLanguageCode(HelpAndAboutActivity.this) + "/help.html");
            getSupportActionBar().setTitle("帮助");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: Implement this method
        switch (item.getItemId()) {
            case android.R.id.home:
                backAction();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        backAction();
    }

    private void backAction(){
        if (web.canGoBack()){
            web.goBack();
        }else{
            finish();
        }
    }
}
