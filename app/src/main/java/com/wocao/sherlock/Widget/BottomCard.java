package com.wocao.sherlock.Widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;

import com.wocao.sherlock.R;

/**
 * TODO: document your custom view class.
 */
public class BottomCard extends RelativeLayout implements RelativeLayout.OnTouchListener{

    private BottomCardHandle handle;
    private int handleHeight = 50;
    private float offset = 0;
    private boolean isOpen = false;
    private int animDuration = 800;

    private float handleLastTouchY = 0;

    private float bottomHeight;

    private View contentView;

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
        contentView=(HorizontalScrollView)findViewById(R.id.BottomCardContent);
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
        if (offset + this.offset >= getBottomH()) {
            this.offsetTopAndBottom((int) (getBottomH() - this.offset));
            this.offset += (int) (getBottomH() - this.offset);
        } else if (offset + this.offset <= 0) {
            this.offsetTopAndBottom((int) (0 - this.offset));
            this.offset += (int) (0 - this.offset);
        } else {
            this.offsetTopAndBottom((int) offset);
            this.offset += (int) offset;
        }
        handle.setPercent(this.offset / getBottomH());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setRootMarginBottom(getBottomH());
        bottomHeight = getY();
        System.out.println(bottomHeight);
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
                setRootMarginBottom((float) animation.getAnimatedValue()-offset);
            }
        });
        animator.start();
        isOpen=!isOpen;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:break;
            case MotionEvent.ACTION_MOVE:break;
            case MotionEvent.ACTION_UP:break;
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

    public void openOrClose(){
        startAnim();
    }
}
