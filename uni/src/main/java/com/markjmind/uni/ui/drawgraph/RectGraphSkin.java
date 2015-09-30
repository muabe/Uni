package com.markjmind.uni.ui.drawgraph;

import android.graphics.Canvas;
import android.graphics.Paint;

public class RectGraphSkin extends GraphSkin{
	
	public RectGraphSkin(int maxMeasure) {
		super(maxMeasure);
	}

	@Override
	public void draw(Canvas canvas) {
		drawRect(canvas);
	}
	
	private void drawRect(Canvas canvas){
		if(getMaxMeasure()<=0)
			return;
		Paint Pnt = new Paint();
		Pnt.setStyle(Paint.Style.STROKE);
		
		for(int i=0;i<list.size();i++){
			SimpleStyle style = list.get(i);
			Pnt.setColor(style.getColor());
			Pnt.setStrokeWidth(style.getWidth());	
			int height = getHeight();
			
			int x = getWidth()*i/list.size()+getWidth()/list.size()/2;
			int y = height*(getMaxMeasure()-style.meansure)/getMaxMeasure()+getTopMargin();
			int p = (int)((height+getTopMargin()-y)*(1f-percent));
			canvas.drawLine(x,y+p,x,height+getTopMargin(),Pnt);
		}
	}
	
	public void addAll(int[] meansures, SimpleStyle style){
		for(int i=0;i<meansures.length;i++){
			add(meansures[i],style);
		}
	}
	
	public static SimpleStyle getSimpleStyle(int color,int width){
		return new SimpleStyle(color, width,0,0);
	}
	
}
