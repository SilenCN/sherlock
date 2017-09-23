package com.wocao.sherlock.Widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wocao.sherlock.R;

/**
 * TODO: document your custom view class.
 */
public class BottomCardHandle extends View {

    private int color;
    private int alpha=255;
    private Paint paint;
    private float percent=0f;

    public BottomCardHandle(Context context) {
        super(context);
        init();
    }

    public BottomCardHandle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomCardHandle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        color=Color.BLACK;
        paint=new Paint();
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height=getMeasuredHeight();
        int width=getMeasuredWidth();

        Path path=new Path();

        path.moveTo(width*0.1f,height*0.1F+height*0.8f*(percent));
        path.lineTo(width/2,height*0.1F+height*0.8f*(1-percent));
        path.lineTo(width*0.9f,height*0.1F+height*0.8f*percent);
        canvas.drawPath(path,paint);

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
        invalidate();
    }

}
