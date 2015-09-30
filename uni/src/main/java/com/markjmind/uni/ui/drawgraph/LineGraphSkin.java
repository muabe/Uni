package com.markjmind.uni.ui.drawgraph;

import android.graphics.Canvas;
import android.graphics.Paint;

public class LineGraphSkin extends GraphSkin{
	
	private CircleGraphEl circle;
	private LineGraphEl line;
	
	private int maxMarginSize;
	
	public LineGraphSkin(int maxMeasure){
		super(maxMeasure);
		circle = new CircleGraphEl(maxMeasure);
		line = new LineGraphEl(maxMeasure);
	}
	
	@Override
	public void draw(Canvas canvas) {
		circle.draw(canvas);
		line.draw(canvas);
	}
	
	
	@Override
	public void add(int meansure, SimpleStyle lineStyle) {		
		this.add(meansure, lineStyle, null);
	}
	
	public void add(int meansure, SimpleStyle lineStyle, SimpleStyle cirleSytle){
		if(cirleSytle==null){
			cirleSytle = new SimpleStyle(0,0,0,0);
		}
		circle.add(meansure, cirleSytle);
		line.add(meansure, lineStyle);
	}
	public void addAll(int[] meansures, SimpleStyle lineStyle,SimpleStyle cirleSytle){
		for(int i=0;i<meansures.length;i++){
			add(meansures[i],lineStyle,cirleSytle);
		}
	}
	
	public void addAll(int[] meansures, SimpleStyle lineStyle){
		for(int i=0;i<meansures.length;i++){
			add(meansures[i],lineStyle,null);
		}
	}
	
	public static SimpleStyle getLineSimpleStyle(int color,int width){
		return new SimpleStyle(color, width,0,0);
	}
	public static SimpleStyle getCircleSimpleStyle(int color,int width, int circleSize){
		return new SimpleStyle(color, width,circleSize,0);
	}
	
	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		circle.setWidth(width);
		line.setWidth(width);
	}
	
	@Override
	public void setHeight(int height) {
		super.setHeight(height);
		circle.setHeight(height);
		line.setHeight(height);
	}
	
	@Override
	public void setTopMargin(int topMargin) {
		circle.setTopMargin(topMargin);
		line.setTopMargin(topMargin);
		super.setTopMargin(topMargin);
	}
	
	@Override
	public void setBottomMargin(int bottomMargin) {
		circle.setBottomMargin(bottomMargin);
		line.setBottomMargin(bottomMargin);
		super.setBottomMargin(bottomMargin);
	}
	
	private class LineGraphEl extends GraphSkin{
		
		public LineGraphEl(int maxMeasure) {
			super(maxMeasure);
		}

		@Override
		public void add(int meansure, SimpleStyle style) {			
			super.add(meansure, style);
		}
		
		
		@Override
		public void draw(Canvas canvas) {
			Paint Pnt = new Paint();
			int preX=0;
			int preY=0;
			
			for(int i=0;i<list.size();i++){
				SimpleStyle style = list.get(i);
				SimpleStyle cStyle = circle.list.get(i);
				Pnt.setColor(style.getColor());
				Pnt.setStrokeWidth(style.getWidth());
				int height = getHeight();
				int x = getWidth()*i/list.size()+getWidth()/list.size()/2;
				int y = height*(getMaxMeasure()-style.meansure)/getMaxMeasure()+getTopMargin();
				
				if(i!=0){
					int dx =x-preX;
					int dy = preY-y;
					float R = (float)Math.sqrt(dx*dx+dy*dy);
					float x0 =cStyle.size*dx/R;
					float y0 =cStyle.size*dy/R;
					canvas.drawLine(preX+x0,preY-y0,x-x0,y+y0,Pnt);
				}
				preX = x;
				preY = y;
			}
		}
	}
	
	@Override
	public int getBoradTopMargin() {
		return maxMarginSize;
	}
	
	@Override
	public int getBoradBottomMargin() {
		return maxMarginSize;
	}
	
	private class CircleGraphEl extends GraphSkin{
		
		public CircleGraphEl(int maxMeasure) {
			super(maxMeasure);
			maxMarginSize = 0;
		}

		@Override
		public void add(int meansure, SimpleStyle style) {	
			if(maxMarginSize<style.getWidth()+style.getSize()){
				maxMarginSize = style.getWidth()+style.getSize();
			}
			super.add(meansure, style);
		}
		
		@Override
		public void draw(Canvas canvas) {
			Paint Pnt = new Paint();
			Pnt.setStyle(Paint.Style.STROKE);
			
			for(int i=0;i<list.size();i++){
				SimpleStyle style = list.get(i);
				Pnt.setColor(style.getColor());
				Pnt.setStrokeWidth(style.getWidth());
				int height = getHeight();
				int x = getWidth()*i/list.size()+getWidth()/list.size()/2;
				int y = height*(getMaxMeasure()-style.meansure)/getMaxMeasure()+getTopMargin();
				canvas.drawCircle(x, y, style.size, Pnt);
			}
		}
	}
	

}
