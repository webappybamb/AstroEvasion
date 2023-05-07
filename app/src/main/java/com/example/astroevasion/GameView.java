package com.example.astroevasion;

import static java.lang.Math.sin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameView extends View {

    public static final int INITIAL_LIVES = 3;
    public static final int ITEM_SPAWN_INTERVAL = 1500; // in milliseconds
    public static final int FINGER_CIRCLE_RADIUS = 70;
    public static final int ITEM_MIN_SPEED = 50;
    public static final int ITEM_MAX_SPEED = 150;
    public static final int ITEM_TYPES = 3;
    public static final int[] ITEM_SIZES = {50, 100, 150, 200, 250, 300, 400, 500};
    private static final Bitmap[] IMGS = new Bitmap[(ITEM_TYPES*ITEM_SIZES.length)];
    private static boolean NOT_STARTED = true;


    private Paint mPaint;
    private float mX = getWidth()/2;
    private float mY = getHeight()/2;
    private boolean FIRST = true;
    private List<Item> mItems;
    private long mLastItemSpawnTime;
    private long mStartTime;
    private int mLives;
    private int mScore;
    private OnGameEventListener mOnGameEventListener;
    private Bitmap BG;
    private Bitmap XP;
    private Bitmap LIVES;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mItems = new ArrayList<>();
        mLives = INITIAL_LIVES;
        mStartTime = System.currentTimeMillis();
        mLastItemSpawnTime = System.currentTimeMillis();
        Log.println(Log.DEBUG, "IMGS LEN", ""+IMGS.length);
    }

    public void setOnGameEventListener(OnGameEventListener onGameEventListener) {
        mOnGameEventListener = onGameEventListener;
    }

    public void setThings() {
        BG = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bg), getWidth(), getHeight(), true);
        XP = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.xp), 100, 100, true);
        LIVES = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.heart), 100, 100, true);
        for (int i = 0; i < ITEM_SIZES.length; i++) {
            IMGS[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.planet1), ITEM_SIZES[i], ITEM_SIZES[i], true);
            IMGS[i+ITEM_SIZES.length] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.planet2), ITEM_SIZES[i], ITEM_SIZES[i], true);
            IMGS[i+2*ITEM_SIZES.length] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.planet3), ITEM_SIZES[i], ITEM_SIZES[i], true);
        }
    }

    public int getLives() {
        return mLives;
    }

    public void setLives(int lives) {
        mLives = lives;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (FIRST) {
            FIRST = false;
            setThings();
        }
        if (NOT_STARTED) { return; }
        Rect rectangle = new Rect(0, 0, getWidth(), getHeight());
        canvas.drawBitmap(BG, rectangle, rectangle, mPaint);
        mPaint.setAlpha(128);
        canvas.drawCircle(mX, mY, (float) (FINGER_CIRCLE_RADIUS + 10*sin((System.currentTimeMillis()-mStartTime)/60)), mPaint);
        mPaint.setAlpha(255);
        for (Item item : mItems) {
            int size = item.getSize();
            Rect rect1 = new Rect(0, 0, size, size);
            Rect rect2 = new Rect(item.getX(), item.getY(), item.getX()+size, item.getY()+size);
            canvas.drawBitmap(IMGS[item.getType()*ITEM_SIZES.length + Arrays.binarySearch(ITEM_SIZES, item.getSize())], rect1, rect2, mPaint);
            //item.draw(canvas);

        }

        mPaint.setTextSize(60);
        mPaint.setColor(Color.WHITE);
        canvas.drawBitmap(LIVES, new Rect(0, 0, 100, 100), new Rect(30, 30, 130, 130), mPaint);
        canvas.drawText(getContext().getString(R.string.lives, mLives), 160, 100, mPaint);
        canvas.drawBitmap(XP, new Rect(0, 0, 100, 100), new Rect(getWidth() - 350, 30, getWidth() - 250, 130), mPaint);
        canvas.drawText(getContext().getString(R.string.score, mScore),  getWidth() - 220, 100, mPaint);

        checkCollision();
        updateItems();
        spawnItem();
        invalidate();
        mScore += 1;
    }

    private void checkCollision() {
        List<Item> REMOVE = new ArrayList<>();
        for (Item item : mItems) {
            //if (item.getRect().intersect((int)mX - 50, (int)mY - 50, (int)mX + 50, (int)mY + 50)) {
            if (Math.pow(item.getSize()/2 + FINGER_CIRCLE_RADIUS, 2) > Math.pow(mX+FINGER_CIRCLE_RADIUS/2-item.getX()-item.getSize()/2, 2) + Math.pow(mY+FINGER_CIRCLE_RADIUS/2-item.getY()-item.getSize()/2, 2)) {
                mOnGameEventListener.onItemCollision();
                REMOVE.add(item);
            }
        }
        for (Item item: REMOVE) {
            mItems.remove(item);
        }
    }

    private void updateItems() {
        Iterator<Item> iterator = mItems.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            item.update();
            if ((item.getX() < - getWidth() || item.getX() > 2*getWidth() || item.getY() < -getHeight() || item.getY() > 2*getHeight()) && (item.getCrossed())) {
                iterator.remove();
                mOnGameEventListener.onItemCrossedScreen(item.getSize());
            } else if ((item.getX() < 0 || item.getX() > getWidth() || item.getY() < 0 || item.getY() > getHeight())){
                item.setCrossed(true);
            }
        }
    }

    private void spawnItem() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastItemSpawnTime >= ITEM_SPAWN_INTERVAL) {
            Random random = new Random();
            int width = getWidth();
            int height = getHeight();
            int directionX = 1;
            int directionY = 1;
            int type = random.nextInt(ITEM_TYPES);
            int size = ITEM_SIZES[random.nextInt(ITEM_SIZES.length)];
            int x;
            int y;

            int startingPoint = random.nextInt(4);
            if (startingPoint == 0) {
                // Top
                y = -size;
                x = random.nextInt(width);
            } else if (startingPoint == 1) {
                // Right
                x = width + size;
                y = random.nextInt(height);
            } else if (startingPoint == 2) {
                // Bottom
                y = height + size;
                x = random.nextInt(width);
            } else {
                // Left
                x = -size;
                y = random.nextInt(height);
            }
            int Mx = (int)(getWidth()/4) + random.nextInt((int)(getWidth()/2));
            int My = (int)(getHeight()/4) + random.nextInt((int)(getHeight()/2));

            int speedX = (Mx - x)/(ITEM_MAX_SPEED + (int)(Math.random() * ((ITEM_MIN_SPEED - ITEM_MAX_SPEED) + 1)));
            int speedY = (My - y)/(ITEM_MAX_SPEED + (int)(Math.random() * ((ITEM_MIN_SPEED - ITEM_MAX_SPEED) + 1)));

            mItems.add(new Item(type, size, directionX*speedX, directionY*speedY, x, y));
            mLastItemSpawnTime = currentTime;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                mY = event.getY();
                NOT_STARTED = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mX = event.getX();
                mY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                mOnGameEventListener.gameOver();
                break;
        }
        invalidate();
        return true;
    }

    public interface OnGameEventListener {
        void onItemCollision();
        void onItemCrossedScreen(int size);
        void gameOver();
    }
}