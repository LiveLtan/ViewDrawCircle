package com.ltan.view.viewdrawcircle.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.ltan.view.viewdrawcircle.R;

/**
 * Desc: draw circle as water ripple
 * <p>
 * Modified list:
 *
 * created by ltan on 2018/9/9
 */
public class CircleView extends View {

    private static final String TAG = "TAG-CircleView";
    private int mWidth, mHeight;
    private Paint mRingPaint;
    private Paint mCenterPaint;
    private int mRingColor, mCenterColor;
    private float mRingWidth;
    private float mCenterRadius;
    private int mRingCount;

    private final static float DEFAULT_RADIUS = 90.F;
    private final static float DEFAULT_SPACING = 20.F;
    private final static int DEFAULT_COLOR = Color.BLUE;
    private final static int DEFAULT_RIPPLE_COUNT = 3;

    private ValueAnimator mValueAnimator;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyleAttr, 0);
        mRingColor = ta.getColor(R.styleable.CircleView_ringColor, DEFAULT_COLOR);
        mCenterColor = ta.getColor(R.styleable.CircleView_centerColor, DEFAULT_COLOR);
        mRingWidth = ta.getFloat(R.styleable.CircleView_ringWidth, DEFAULT_SPACING);
        mCenterRadius = ta.getFloat(R.styleable.CircleView_centerRadius, DEFAULT_RADIUS);
        mRingCount = ta.getInt(R.styleable.CircleView_ringCount, DEFAULT_RIPPLE_COUNT);
        ta.recycle();
        initPaint();
        initAnimation();
    }

    private void initPaint() {
        mRingPaint = new Paint();
        mRingPaint.setAlpha(255);
        mRingPaint.setAntiAlias(true);
        mRingPaint.setDither(true);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStrokeWidth(mRingWidth);

        mCenterPaint = new Paint();
        mCenterPaint.setAntiAlias(true);
        mCenterPaint.setDither(true);
        mCenterPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCenterPaint.setColor(mCenterColor);
    }

    private void initAnimation() {
        mValueAnimator = ValueAnimator.ofInt(0, mRingCount);
        mValueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRingCount = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mValueAnimator.setDuration(1000);
        mValueAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw the center circle
        canvas.drawCircle(mWidth / 2, mHeight / 2, mCenterRadius, mCenterPaint);

        // draw the outer circle stoke
        for(int i = 0; i < mRingCount; i++) {
            int alpha = 200 - i * 20;
            if(alpha <= 0) {
                alpha = 0;
            }
            mRingPaint.setAlpha(alpha);
            canvas.drawCircle(mWidth / 2, mHeight / 2, mCenterRadius + mRingWidth * (i + 0.5F) - 0.5F, mRingPaint);
            postInvalidateDelayed(10 * (i + 1));
            postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            }, 10);
        }
    }
}
