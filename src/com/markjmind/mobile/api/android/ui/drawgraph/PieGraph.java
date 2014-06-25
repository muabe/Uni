package com.markjmind.mobile.api.android.ui.drawgraph;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieGraph extends View{
	
	float mDensity;
	public int mRadius = 30;
	public int mBackColor = 0;
	Paint Pnt = new Paint();
	
	ArrayList<PieGraphInfo> pieInfo = new ArrayList<PieGraphInfo>();

	public PieGraph(Context context) {
		super(context);
		init();
	}
	
	public PieGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init(){
		mDensity = getContext().getResources().getDisplayMetrics().density;
		clear();
	}
	
	public void setRadius(int radius){
		this.mRadius = (int)(radius*mDensity);
	}

	public void setBackgroundColor(int color){
		mBackColor = color;
	}
	
	public void add(PieGraphInfo info){
		pieInfo.add(info);
	}
	
	public void add(int angle, int color){
		pieInfo.add(new PieGraphInfo(angle,color));
		invalidate();
		
	}
	
	public void clear(){
		pieInfo.clear();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBackCircle(canvas);
		drawCircle(canvas);
		
	}
	
	
	private void drawCircle(Canvas canvas){
		Pnt.setStrokeWidth(mRadius);	
		Pnt.setStyle(Paint.Style.STROKE);
		Pnt.setDither(true);
		int w = getWidth();
		int h = getHeight();
		int stW=0;
		int stH=0;
		if(w>h){
			stW = (w-h)/2+mRadius/2;
			stH = mRadius/2;
			w=h;
		}else{
			stW = mRadius/2;
			stH = (h-w)/2+mRadius/2;
			h=w;
		}
		w = w+stW-mRadius;
		h = h+stH-mRadius;

		RectF rect=new RectF(stW,stH,w,h);
		int pre_a = -90;
		
		for(int i=0;i<pieInfo.size();i++){
			Pnt.setColor(pieInfo.get(i).color);
			canvas.drawArc(rect,pre_a,pieInfo.get(i).angle,false,Pnt);
			pre_a = pre_a+pieInfo.get(i).angle;
		}
	}
	
	private void drawBackCircle(Canvas canvas){
		Paint Pnt = new Paint();
		Pnt.setColor(mBackColor);
		Pnt.setStrokeWidth(mRadius);	
		Pnt.setStyle(Paint.Style.STROKE);
		
		int w = getWidth();
		int h = getHeight();
		int stW=0;
		int stH=0;
		if(w>h){
			stW = (w-h)/2+mRadius/2;
			stH = mRadius/2;
			w=h;
		}else{
			stW = mRadius/2;
			stH = (h-w)/2+mRadius/2;
			h=w;
		}
		w = w+stW-mRadius;
		h = h+stH-mRadius;

		RectF rect=new RectF(stW,stH,w,h);
	    canvas.drawArc(rect,0,360,false,Pnt);
	}
	
	public class PieGraphInfo{
		public int angle;
		public int color;
		public PieGraphInfo(int angle, int color){
			this.angle = angle;
			this.color = color;
		}
	}
	
}
