package com.markjmind.mobile.api.android.ui.drawgraph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class VBackLineSkin extends GraphSkin{
	
	public VBackLineSkin(int maxMeasure) {
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
			int height = getHeight()+getTopMargin();
			if(style.align==SimpleStyle.Align.CENTER){
				if(i==0){
					canvas.drawLine(style.width,getTopMargin(),style.width,height,Pnt);
					continue;
				}else if(i==list.size()-1){
					canvas.drawLine(getWidth()-style.width,getTopMargin(),getWidth()-style.width,height,Pnt);
				}
				int gap = getWidth()*(index-1)/getMaxMeasure()+getWidth()/getMaxMeasure()/2;
				canvas.drawLine(gap,getTopMargin(),gap,height,Pnt);
			}else{
				int gap = getWidth()*index/getMaxMeasure();
				canvas.drawLine(gap,getTopMargin(),gap,height,Pnt);
			}
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
	
	public static SimpleStyle getSimpleStyle(int color,int width, SimpleStyle.Align align){
		return new SimpleStyle(color, width,0,0,align);
	}
}
