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

    private static final String MOON_COLOR = "#FFEB3B";
    private static final String EARTH_COLOR = "#000000";
    private static final int RADIUS = 192;

    private Paint mMoonPaint;
    private Paint mEarthPaint;
    private PointF mEarthPoint = new PointF(RADIUS, RADIUS);

    private int mWidth = 0;
    private int mHeight = 0;
    private int mSkyRGB = 255;
    private int mDegree = 0;
    private boolean mIsTouchMode = false;

    public EclipseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mMoonPaint = new Paint();
        mMoonPaint.setColor(Color.parseColor(MOON_COLOR));
        mMoonPaint.setAntiAlias(true);
        mMoonPaint.setDither(true);

        mEarthPaint = new Paint();
        mEarthPaint.setColor(Color.parseColor(EARTH_COLOR));
        mEarthPaint.setAntiAlias(true);
        mEarthPaint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        drawSky(canvas);
        drawMoon(canvas);
        drawEarth(canvas);
    }

    private void drawSky(Canvas canvas) {
        canvas.drawColor(getSkyColorInt());
    }

    private void drawMoon(Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mHeight / 2, RADIUS, mMoonPaint);
    }

    private void drawEarth(Canvas canvas) {
        if (!mIsTouchMode) {
            if (mDegree > 360) {
                mDegree = 0;
            }
            mEarthPoint.x = (float) (Math.cos(mDegree) * RADIUS * 2 + mWidth / 2);
            mEarthPoint.y = (float) (Math.sin(mDegree) * RADIUS * 2 + mHeight / 2 - RADIUS * 2);
            mDegree++;
            calculateSkyRGB();
            postInvalidate();
        }
        canvas.drawCircle(mEarthPoint.x, mEarthPoint.y, RADIUS - 2, mEarthPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsTouchMode = true;
                break;
            case MotionEvent.ACTION_MOVE:
                mEarthPoint.x = event.getX();
                mEarthPoint.y = event.getY();
                calculateSkyRGB();
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                mIsTouchMode = false;
                postInvalidate();
                break;
        }
        return true;
    }

    private void calculateSkyRGB() {
        double totalOffset = RADIUS * 2;
        double nowOffset = Math.sqrt(Math.pow(mWidth / 2 - mEarthPoint.x, 2)
                + Math.pow(mHeight / 2 - mEarthPoint.y, 2));
        mSkyRGB = (int) (255 * nowOffset / totalOffset);
        if (mSkyRGB > 255)
            mSkyRGB = 255;
        else if (mSkyRGB < 24)
            mSkyRGB = 24;
    }

    private int getSkyColorInt() {
        return Color.rgb(mSkyRGB, mSkyRGB, mSkyRGB);
    }
}
