package com.wocao.sherlock.Setting.DiyFloatView;

import android.app.DialogFragment;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wocao.sherlock.AppWidget.ServiceState.ServiceStateStatic;
import com.wocao.sherlock.CoreService.FloatWindowAppList;
import com.wocao.sherlock.R;
import com.wocao.sherlock.Setting.DiyFloatView.Widget.InitViewInterface;
import com.wocao.sherlock.Setting.DiyFloatView.Widget.MovableTextClock;
import com.wocao.sherlock.Setting.DiyFloatView.Widget.MovableTextView;
import com.wocao.sherlock.Setting.SettingUtils;
import com.wocao.sherlock.Widget.BottomCard;

import java.io.File;
import java.io.FileNotFoundException;

public class DiyFloatViewActivity extends AppCompatActivity {

    private MovableTextView mottoTv, timeTv;
    private MovableTextClock timeTC, dateTC;
    private RelativeLayout layout;

    private int windowWidth = 0, windowHeight = 0;

    File BACKGROUND_FILE;//= Environment.getExternalStorageDirectory().getPath() + "/Sherlock/background.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.float_view);

        BACKGROUND_FILE = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "background.jpg");
        windowHeight = getWindowManager().getDefaultDisplay().getHeight();
        windowWidth = getWindowManager().getDefaultDisplay().getWidth();

        findView();
        viewTest();

        initView();

    }

    private void initView() {

        if (BACKGROUND_FILE.exists()) {
            try {
                layout.setBackground(new BitmapDrawable(BitmapFactory.decodeFile(BACKGROUND_FILE.getPath())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        timeTv.setInitViewInterface(new InitViewInterface() {
            @Override
            public int getPositionX(int width, int height) {
                return windowWidth / 2 - width / 2;
            }

            @Override
            public int getPositionY(int width, int height) {
                return windowHeight * 3 / 5 - height / 2;
            }
        });

        mottoTv.setInitViewInterface(new InitViewInterface() {
            @Override
            public int getPositionX(int width, int height) {
                return windowWidth / 2 - width / 2;
            }

            @Override
            public int getPositionY(int width, int height) {
                return windowHeight * 3 / 4 - height / 2;
            }
        });
        timeTC.setInitViewInterface(new InitViewInterface() {
            @Override
            public int getPositionX(int width, int height) {
                return windowWidth / 2 - width / 2;
            }

            @Override
            public int getPositionY(int width, int height) {
                return windowHeight / 4 - height / 2;
            }
        });
        dateTC.setInitViewInterface(new InitViewInterface() {
            @Override
            public int getPositionX(int width, int height) {
                return windowWidth / 2 - width / 2;
            }

            @Override
            public int getPositionY(int width, int height) {
                return windowHeight / 3 - height / 2;
            }
        });

        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DiyFloatViewActivity.this);
                builder.setTitle("背景");
                builder.setMessage("是否修改背景？");
                builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gotoPickAndCropSmallBitmap();
                    }
                });
                builder.setNegativeButton("否", null);
                builder.create();
                builder.show();

                return true;
            }
        });

        String motto = SettingUtils.getStringValue(this, "setting_display_motto", null);
        if (null != motto && !motto.equals("")) {
            mottoTv.setText(ServiceStateStatic.Moto);
        }
    }


    private void viewTest() {

    }

    private void findView() {

        ((BottomCard) findViewById(R.id.BottomCardTest)).setVisibility(View.GONE);
        timeTv = (MovableTextView) findViewById(R.id.lockserverUICoutdownTV);
        mottoTv = (MovableTextView) findViewById(R.id.lockserverTVMotto);
        timeTC = (MovableTextClock) findViewById(R.id.lockserverTCTime);
        dateTC = (MovableTextClock) findViewById(R.id.lockserverTCData);
        layout = (RelativeLayout) findViewById(R.id.FloatViewLayout);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                layout.setBackground(new BitmapDrawable(BitmapFactory.decodeFile(BACKGROUND_FILE.getPath())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void gotoPickAndCropSmallBitmap() {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
     /*   if (Build.VERSION.SDK_INT<19){
            intent=new Intent(Intent.ACTION_GET_CONTENT);
        }else{
            intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
        }*/



        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", windowWidth);
        intent.putExtra("aspectY", windowHeight);
        intent.putExtra("outputX", windowWidth);
        intent.putExtra("outputY", windowHeight);
        intent.putExtra("scale", true);
        intent.putExtra("output", Uri.fromFile(BACKGROUND_FILE));
      /*  intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection*/
        startActivityForResult(intent, 1);

    }

}
