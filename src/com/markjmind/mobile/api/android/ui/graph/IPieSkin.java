package com.markjmind.mobile.api.android.ui.graph;

import android.graphics.Canvas;
/**
 * 
 * @author 오재웅
 * @email markjmind@gmail.com
 */
public interface IPieSkin {
	public void init(PieLayout pieLayout);
	public void draw(PieLayout pieLayout, Canvas canvas, float startAngle, float maxAngle,float startRadians, float radians, int arcIndex);
	
}
