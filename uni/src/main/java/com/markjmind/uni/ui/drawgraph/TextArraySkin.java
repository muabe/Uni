package com.markjmind.uni.ui.drawgraph;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class TextArraySkin extends GraphSkin{

	private String[] textArray;
	private int maxBoradBottomMargin;
	
	public TextArraySkin(int maxMeasure) {
		super(maxMeasure);
		textArray=null;
		maxBoradBottomMargin=0;
	}

	@Override
	public void draw(Canvas canvas) {
		if(textArray==null)
			return;
		Paint Pnt = new Paint();
		Pnt.setAntiAlias(true);

		for(int i=0;i<list.size();i++){
			SimpleStyle style = list.get(i);
			
			int strX =  getWidth()*i/list.size();
			int endX =  getWidth()*i/list.size()+getWidth()/list.size();
			int strY = getHeight();
			int endY = getHeight()+getBottomMargin();
			Pnt.setColor(style.backgroundColor);
			canvas.drawRect(strX, strY, endX, endY, Pnt);
			
			Pnt.setColor(style.color);
			Pnt.setTextAlign(getPaintAlign(style.align));
			Pnt.setTextSize(style.getSize());
			int x = strX+(endX-strX)/2;
			int y = strY+style.size+style.margin;
			canvas.drawText(textArray[i], x, y, Pnt);
		}		
	}
	
	@Override
	public void add(int meansure, SimpleStyle style) {
		if(maxBoradBottomMargin<style.size+style.margin+style.size/5){
			maxBoradBottomMargin = style.size+style.margin+style.size/5;
		}
		super.add(meansure, style);
	}
	
	public void addAll(String[] textArray, SimpleStyle style){
		this.textArray = textArray;
		for(int i=0;i<textArray.length;i++){
			add(i, style);
		}
	}
	
	@Override
	public int getBoradBottomMargin() {
		return maxBoradBottomMargin;
	}
	
	public static SimpleStyle getSimpleStyle(int color, int textSize, int topMargin){
		return new SimpleStyle(color,0,textSize,topMargin);
	}
	
	private Align getPaintAlign(SimpleStyle.Align al){
		if(al==null){
			return Align.CENTER;
		}else if(SimpleStyle.Align.LEFT == al){
			return Align.LEFT;
		}else if(SimpleStyle.Align.CENTER == al){
			return Align.CENTER;
		}else if(SimpleStyle.Align.RIGHT == al){
			return Align.RIGHT;
		}else{
			return Align.CENTER;
		}
	}
}
