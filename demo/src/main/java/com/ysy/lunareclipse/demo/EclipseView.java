package com.ysy.lunareclipse.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class EclipseView extends View {

    private static final String SUN_COLOR = "#FFEB3B";
    private static final String EARTH_COLOR = "#000000";
    private static final int RADIUS = 128;

    private Paint mSunPaint;
    private Paint mEarthPaint;
    private PointF mEarthPoint = new PointF(RADIUS, RADIUS);

    private int mWidth = 0;
    private int mHeight = 0;
    private int mRGB = 255;

    public EclipseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mSunPaint = new Paint();
        mSunPaint.setColor(Color.parseColor(SUN_COLOR));
        mSunPaint.setAntiAlias(true);

        mEarthPaint = new Paint();
        mEarthPaint.setColor(Color.parseColor(EARTH_COLOR));
        mEarthPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        drawSky(canvas);
        drawSun(canvas);
        drawEarth(canvas);
    }

    private void drawSky(Canvas canvas) {
        canvas.drawColor(getSkyColorInt());
    }

    private void drawSun(Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mHeight / 2, RADIUS, mSunPaint);
    }

    private void drawEarth(Canvas canvas) {
        canvas.drawCircle(mEarthPoint.x, mEarthPoint.y, RADIUS - 2, mEarthPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                mEarthPoint.x = event.getX();
                mEarthPoint.y = event.getY();
                double totalOffset = RADIUS * 2;
                double nowOffset = Math.sqrt(Math.pow(mWidth / 2 - mEarthPoint.x, 2)
                        + Math.pow(mHeight / 2 - mEarthPoint.y, 2));
                mRGB = (int) (255 * nowOffset / totalOffset);
                if (mRGB > 255)
                    mRGB = 255;
                else if (mRGB < 24)
                    mRGB = 24;
                postInvalidate();
                break;
        }
        return true;
    }

    private int getSkyColorInt() {
        return Color.rgb(mRGB, mRGB, mRGB);
    }
}
