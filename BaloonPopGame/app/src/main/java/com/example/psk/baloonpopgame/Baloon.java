package com.example.psk.baloonpopgame;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import static android.util.TypedValue.applyDimension;

public class Baloon extends ImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private int mHeight, mColor;
    private boolean mIsPopped;
    private ValueAnimator mValueAnimator;
    private BaloonListener mBaloonPopListner;

    public Baloon(Context context) {
        super(context);
    }


    public Baloon(Context context, int color, int rawheight) {
        super(context);
        mColor = color;
        mHeight = rawheight;
        mIsPopped = false;
        int mWidth = mHeight / 2;
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(convertToDP(mHeight, context),
                        convertToDP(mWidth, context));
        this.setLayoutParams(layoutParams);
        this.setColorFilter(mColor);
        this.setImageResource(R.drawable.ic_balloon);
    }

    private int convertToDP(int ht, Context context) {
        return (int) applyDimension(TypedValue.COMPLEX_UNIT_DIP, ht, context.getResources().getDisplayMetrics());
    }

    public void releaseBaloon(int screenHeight, int duration, int delay) {
        mValueAnimator = new ValueAnimator();
        mValueAnimator.setDuration(duration);
        mValueAnimator.setStartDelay(delay);
        mValueAnimator.setFloatValues(screenHeight, 0f);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setTarget(this);
        mValueAnimator.addListener(this);
        mValueAnimator.addUpdateListener(this);
        mValueAnimator.start();
    }

    public void popBaloon() {
          /* Pop the baloon. */
        this.mIsPopped = true;
        this.mValueAnimator.cancel();
        this.setVisibility(INVISIBLE);
    }

    public void registerBaloonPopListener(BaloonListener baloonListener) {
        mBaloonPopListner = baloonListener;
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        //here
        if (mIsPopped == false) {
            setTranslationY((float) animation.getAnimatedValue());
            if ((float) animation.getAnimatedValue() == 0f) {
                //Reached top pop the baloon..
                popBaloon();
                mBaloonPopListner.callBackForBaloonPop(false);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /* Pop the baloon. */
        popBaloon();
        mBaloonPopListner.callBackForBaloonPop(true);
        
        return super.onTouchEvent(event);
    }
}
