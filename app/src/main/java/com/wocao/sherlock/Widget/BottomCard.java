package com.wocao.sherlock.Widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

import com.wocao.sherlock.R;

/**
 * TODO: document your custom view class.
 */
public class BottomCard extends RelativeLayout implements RelativeLayout.OnTouchListener {

    private BottomCardHandle handle;
    private int handleHeight = 50;
    private float offset = 0;
    private boolean isOpen = false;
    private int animDuration = 800;

    private float handleLastTouchY = 0;


    private View contentView;

    private boolean isInit = false;

    public BottomCard(Context context) {
        super(context);
        init(context, null, 0);
    }

    public BottomCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);

    }

    public BottomCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        LayoutInflater.from(context).inflate(R.layout.bottom_card_view, this);
        handle = (BottomCardHandle) findViewById(R.id.BottomCardHandle);
        contentView = (HorizontalScrollView) findViewById(R.id.BottomCardContent);
        handleHeight = dip2px(context, 17);

        handle.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handleLastTouchY = event.getRawY();
                        lastY = handleLastTouchY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        setRootMarginBottom(event.getRawY() - lastY);
                        lastY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        startAnim();
                        break;
                }
                return true;
            }
        });

    }

    private void setRootMarginBottom(float offset) {
        //  offset=-offset;
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) getLayoutParams();

        if (-mlp.bottomMargin + offset >= getBottomH()) {
            mlp.bottomMargin = -getBottomH();
        } else if (-mlp.bottomMargin + offset <= 0) {
            mlp.bottomMargin = 0;
        } else {
            mlp.bottomMargin = mlp.bottomMargin - (int) offset;
        }
        this.offset = -mlp.bottomMargin;
        setLayoutParams(mlp);

        handle.setPercent(this.offset / getBottomH());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isInit) {
            setRootMarginBottom(getBottomH());
            isInit = true;
        }
    }


    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getBottomH() {
        return (getMeasuredHeight() - handleHeight);
    }

    private void startAnim() {
        int duration = 0;
        ValueAnimator animator;
        if (isOpen) {
            duration = (int) (animDuration * (1 - offset / getBottomH()));
            animator = ValueAnimator.ofFloat(offset, getBottomH());
        } else {
            duration = (int) (animDuration * (offset / getBottomH()));
            animator = ValueAnimator.ofFloat(offset, 0);
        }

        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setRootMarginBottom((float) animation.getAnimatedValue() - offset);
            }
        });
        animator.start();
        isOpen = !isOpen;
    }

    public void strengthClose(){
        setRootMarginBottom(getBottomH());
        isOpen=false;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    float lastY = 0;


    public View getContentView() {
        return contentView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public void openOrClose() {
        startAnim();
    }
}
