package com.wocao.sherlock.Widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wocao.sherlock.R;


/**
 * Created by 10397 on 2016/2/26.
 */
public class CircleWidget extends View {

    private Paint paint, clearPaint, textPaint,shadowPaint;
    //默认大小
    private static int width = 200, height = 200;

    private Rect textRound;
    private OnClickListener onClickListener;
    private boolean isPressed = false;
    private int clickX = 0, clickY = 0;
    //点击之后绘制波浪效果初始化大小
    private float drawShadowToken=30;
    //圆圈颜色
    private int basicColor=getResources().getColor(R.color.toolbarColor);
    //字体颜色
    private int textColor=Color.WHITE;
    //文本
    private static String textToPaint="开启服务";

    public CircleWidget(Context context) {
        super(context);
        init();
    }

    public CircleWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CircleWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

   /* public CilcleWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }*/

    private void init() {

       // System.out.println(width + "++++++++++++++" + height);
        this.paint = new Paint();
        this.clearPaint = new Paint();
        paint.setAntiAlias(true);


        clearPaint.setColor(Color.WHITE);
        clearPaint.setStyle(Paint.Style.STROKE);

        textPaint = new TextPaint();
        textPaint.setTextSize((int)(16*this.getResources().getDisplayMetrics().scaledDensity));
        textPaint.setColor(Color.WHITE);

        textRound = new Rect();
        textPaint.getTextBounds(textToPaint.toCharArray(), 0, textToPaint.length(), textRound);
        textPaint.setShadowLayer(20, 1, 1, Color.argb(100, 0, 0, 0));
        textPaint.setAntiAlias(true);

        shadowPaint=new Paint();

        shadowPaint.setColor(Color.BLACK);

        shadowPaint.setAlpha(20);
        shadowPaint.setAntiAlias(true);

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               // System.out.println(event.getX()+"<<<<<<"+event.getY());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isPressInInsertCircle(event.getX(), event.getY())) {
                            isPressed = true;
                            clickX = (int) event.getX();
                            clickY = (int) event.getY();
                          //  System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>");
                        } else {
                            isPressed = false;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                      //  System.out.println(event.getX()+"<<<<<<"+event.getY());
                        if (!isPressInInsertCircle(event.getX(), event.getY()))
                            isPressed = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isPressed){
                            onClickListener.onClick();
                        }
                        isPressed = false;
                        drawShadowToken=20;

                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //this.setLayerType(LAYER_TYPE_HARDWARE,paint);
      //  canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);
        paint.setAlpha((int) (255 * 0.55));
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 * 0.55f, paint);

        canvas.drawText(textToPaint, getWidth() / 2 - textRound.width() / 2, getHeight() / 2 + textRound.height() / 2, textPaint);

        if (isPressed){
            canvas.drawCircle(clickX,clickY,drawShadowToken,shadowPaint);
            drawShadowToken*=1.05;
    //        System.out.println(",,,,,,,,");

        }

        clearPaint.setStrokeWidth(width);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2,  getWidth() / 2 * 0.55f+clearPaint.getStrokeWidth()/2,clearPaint);


        paint.setColor(basicColor);
        paint.setAlpha((int) (255 * 0.1));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getWidth() / 2 - getWidth() / 2 * 0.9f);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - paint.getStrokeWidth(), paint);

        paint.setAlpha((int) (255 * 0.33));
        paint.setStrokeWidth(getWidth() / 2 * 0.85f - getWidth() / 2 * 0.72f);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 * 0.85f - paint.getStrokeWidth(), paint);

        invalidate();
    }

    public interface OnClickListener {
        void onClick();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }

    private boolean isPressInInsertCircle(float pressX,float pressY) {
        if (Math.pow(width / 2 - pressX, 2) + Math.pow(height / 2 - pressY, 2) <= Math.pow(getWidth() / 2 * 0.55f, 2))
            return true;
        else
            return false;
    }
}
