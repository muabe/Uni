package com.markjmind.uni.ui;

import android.view.MotionEvent;
import android.view.View;

public interface JwTouchMotionGroupLisener {
	public boolean setTouch(boolean enable,View v,String name,int index,MotionEvent me);
}
