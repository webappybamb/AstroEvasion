package com.example.astroevasion;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Item {

//    private static final int[] COLORS = {
//            Color.RED,
//            Color.GREEN,
//            Color.BLUE
//    };


    private int mType;
    private int mSize;
    private int mSpeedX;
    private int mSpeedY;
    private int mX;
    private int mY;
    private boolean crossed;
    private Paint mPaint;
    private Rect mRect;

    public Item(int type, int size, int speedX, int speedY, int x, int y) {
        mType = type;
        mSize = size;
        mSpeedX = speedX;
        mSpeedY = speedY;
        mX = x;
        mY = y;
        mPaint = new Paint();
//        mPaint.setColor(COLORS[mType - 1]);

        mRect = new Rect(mX, mY, mX + mSize, mY + mSize);
        crossed = false;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);
    }

    public void update() {
        mX += mSpeedX;
        mY += mSpeedY;
        mRect.set(mX, mY, mX + mSize, mY + mSize);
    }

    public Rect getRect() {
        return mRect;
    }

    public int getSize() {
        return mSize;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public boolean getCrossed() {
        return crossed;
    }

    public void setCrossed(boolean v) {
        crossed = v;
    }

    public int getType() {
        return mType;
    }
}

