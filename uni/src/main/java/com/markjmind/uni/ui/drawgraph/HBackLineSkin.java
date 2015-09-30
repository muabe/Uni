package com.markjmind.uni.ui.drawgraph;

import android.graphics.Canvas;
import android.graphics.Paint;

public class HBackLineSkin extends GraphSkin{
	
	public HBackLineSkin(int maxMeasure) {
		super(maxMeasure);
	}

	@Override
	public void draw(Canvas canvas) {
		Paint Pnt = new Paint();
		
		for(int i=0;i<list.size();i++){
			SimpleStyle style = list.get(i);
			Pnt.setColor(style.getColor());
			Pnt.setStrokeWidth(style.getWidth());
			int index = style.getMeansure();
			int height = getHeight();
			int gap = height*index/getMaxMeasure()+getTopMargin();
			canvas.drawLine(0,gap,getWidth(),gap,Pnt);
			
			
		}
	}
	@Override
	public void add(int index, SimpleStyle style) {
		int loc = getMaxMeasure() - index;
		if(loc>=0){
			super.add(loc, style);
		}
	}
	
	public void addAll(SimpleStyle style){
		for(int i=0;i<getMaxMeasure()+1;i++){
			add(i,style);
		}
	}
	
	public static SimpleStyle getSimpleStyle(int color,int width){
		return new SimpleStyle(color, width,0,0);
	}
}
