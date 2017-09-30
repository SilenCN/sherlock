package com.wocao.sherlock.Setting.DiyFloatView.Widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by silen on 17-9-23.
 */

public class MovableTextView extends android.support.v7.widget.AppCompatTextView implements View.OnTouchListener {
    private Context context;
    private SharedPreferences sharedPreferences;
    private int offsetX, offsetY;
    private boolean movable = false;

    private float lastX, lastY;
    private boolean isClick = false;

    private boolean isInit = false;

    public MovableTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public MovableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public MovableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {

        sharedPreferences = context.getSharedPreferences("DisplayConf", Context.MODE_PRIVATE);
        if (sharedPreferences.contains(getId() + "IsFakeBoldText")) {
            this.getPaint().setFakeBoldText(sharedPreferences.getBoolean(getId() + "IsFakeBoldText", false));
        }
        if (sharedPreferences.contains(getId() + "TextColor")) {
            super.setTextColor(sharedPreferences.getInt(getId() + "TextColor", this.getCurrentHintTextColor()));
        }
        if (sharedPreferences.contains(getId() + "TextSize")) {
            super.setTextSize(sharedPreferences.getFloat(getId() + "TextSize", this.getTextSize()));
        }


        this.setOnTouchListener(this);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        sharedPreferences.edit().putInt(getId() + "TextColor", color).apply();
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        sharedPreferences.edit().putFloat(getId() + "TextSize", size).apply();
    }

    public void setFakeBoldText(boolean isFakeBold) {
        this.getPaint().setFakeBoldText(isFakeBold);
        sharedPreferences.edit().putBoolean(getId() + "IsFakeBoldText", isFakeBold).apply();
    }

    public boolean isFakeBoldText() {
        return this.getPaint().isFakeBoldText();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (movable) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                isClick = true;
                break;
            case MotionEvent.ACTION_MOVE:
                isClick = false;
                moveTo(event.getX() - lastX, event.getY() - lastY, false);
                break;
            case MotionEvent.ACTION_UP:
                if (isClick) {
                    performClick();
                    return true;
                }
                moveTo(0, 0, true);
                break;
        }
        return true;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    private void moveTo(float deltaX, float deltaY, boolean save) {
        ViewGroup.MarginLayoutParams mlp =
                (ViewGroup.MarginLayoutParams) getLayoutParams();
        mlp.leftMargin = mlp.leftMargin + (int) deltaX;
        mlp.topMargin = mlp.topMargin + (int) deltaY;
        setLayoutParams(mlp);

        offsetX += (int) deltaX;
        offsetY += (int) deltaY;
        if (save) {
            sharedPreferences.edit().putInt(getId() + "OffsetX", offsetX).apply();
            sharedPreferences.edit().putInt(getId() + "OffsetY", offsetY).apply();
        }

    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isInit) {
            if (sharedPreferences.contains(getId() + "OffsetX") || sharedPreferences.contains(getId() + "OffsetY")) {
                this.offsetX = sharedPreferences.getInt(getId() + "OffsetX", 0);
                this.offsetY = sharedPreferences.getInt(getId() + "OffsetY", 0);

                ViewGroup.MarginLayoutParams mlp =
                        (ViewGroup.MarginLayoutParams) getLayoutParams();
                mlp.leftMargin = mlp.leftMargin + (int) offsetX;
                mlp.topMargin = mlp.topMargin + (int) offsetY;
                setLayoutParams(mlp);
            }
            isInit = true;
        }
    }
}