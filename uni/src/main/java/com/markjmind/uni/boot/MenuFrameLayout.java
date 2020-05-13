package com.markjmind.uni.boot;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.GestureDetectorCompat;

import com.markjmind.uni.common.Jwc;

public class MenuFrameLayout extends FrameLayout implements GestureDetector.OnGestureListener {
    private GestureDetectorCompat detector;
    private float density;

    private float x = 0f;
    private float y = 0f;
    private float distDistance;
    private OnTouchListener touchListener;

    public MenuFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public MenuFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MenuFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        detector = new GestureDetectorCompat(getContext(), this);
        density = Jwc.getDensity(this);
        distDistance = 3f*density;
        this.setLongClickable(false);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.touchListener = l;
        super.setOnTouchListener(l);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        return detector.onTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void touchInit(){
        x = 0f;
        y = 0f;
    }
    @Override
    public boolean onDown(MotionEvent e) {
        touchInit();
        if(touchListener != null) {
            touchListener.onTouch(this, e);
        }
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float dx = Math.abs(x+distanceX);
        float dy = Math.abs(y+distanceY);
        if(dx > dy && dx > distDistance){
            return true;
        }else if(dx <= dy && dy > distDistance){
            return false;
        }else{
            x = x+distanceX;
            y = y+distanceY;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
