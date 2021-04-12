package com.jye.hiandroid.uikit.preview.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/**
 * 手势View
 *
 * @author jye
 * @since 1.0
 */
public class PreviewGestureView extends RelativeLayout {

    private int mHeight;
    private float mDisplacementX;
    private float mDisplacementY;
    private float mInitialTy;
    private float mInitialTx;
    private boolean mTracking;

    public PreviewGestureView(Context context) {
        this(context, null);
        post(new Runnable() {
            @Override
            public void run() {
                mHeight = getHeight();
            }
        });
    }

    public PreviewGestureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        post(new Runnable() {
            @Override
            public void run() {
                mHeight = getHeight();
            }
        });
    }

    public PreviewGestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        post(new Runnable() {
            @Override
            public void run() {
                mHeight = getHeight();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                mDisplacementX = event.getRawX();
                mDisplacementY = event.getRawY();

                mInitialTy = getTranslationY();
                mInitialTx = getTranslationX();

                break;

            case MotionEvent.ACTION_MOVE:
                // get the delta distance in X and Y direction
                float deltaX = event.getRawX() - mDisplacementX;
                float deltaY = event.getRawY() - mDisplacementY;

                //只有在不缩放的状态才能下滑
                if (onCanSwipeListener == null || !onCanSwipeListener.canSwipe()) {
                    break;
                }

                // set the touch and cancel event
                if (deltaY > 0 && (Math.abs(deltaY) > ViewConfiguration.get(getContext()).getScaledTouchSlop() * 2 && Math.abs(deltaX) < Math.abs(deltaY) / 2)
                        || mTracking) {

                    onSwipeListener.onSwiping(deltaY);

                    setBackgroundColor(Color.TRANSPARENT);

                    mTracking = true;

                    setTranslationY(mInitialTy + deltaY);
                    setTranslationX(mInitialTx + deltaX);

                    float mScale = 1 - deltaY / mHeight;
                    if (mScale < 0.3) {
                        mScale = 0.3f;
                    }
                    setScaleX(mScale);
                    setScaleY(mScale);

                }
                if (deltaY < 0) {
                    setViewDefault();
                }

                break;

            case MotionEvent.ACTION_UP:

                if (mTracking) {
                    mTracking = false;
                    float currentTranslateY = getTranslationY();

                    if (currentTranslateY > mHeight / 5) {
                        translateExit();
                        break;
                    }

                }
                setViewDefault();

                onSwipeListener.overSwipe();
                break;
        }
        return false;
    }

    private void setViewDefault() {
        //恢复默认
//        setAlpha(1);
        setTranslationX(0);
        setTranslationY(0);
        setScaleX(1);
        setScaleY(1);
        setBackgroundColor(Color.BLACK);
    }

    /**
     * 平移退出
     */
    private void translateExit() {
        TranslateAnimation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        translate.setDuration(200);
        translate.setFillAfter(true);
        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setTranslationY(mHeight);
                onSwipeListener.downSwipe();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.startAnimation(translate);//开始动画
    }

    public interface OnSwipeListener {
        //向下滑动
        void downSwipe();

        //结束
        void overSwipe();

        //正在滑动
        void onSwiping(float y);
    }


    private OnSwipeListener onSwipeListener;

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    public interface OnCanSwipeListener {
        //可不可以下滑关闭
        boolean canSwipe();
    }

    private OnCanSwipeListener onCanSwipeListener;

    public void setOnGestureListener(OnCanSwipeListener onCanSwipeListener) {
        this.onCanSwipeListener = onCanSwipeListener;
    }

}