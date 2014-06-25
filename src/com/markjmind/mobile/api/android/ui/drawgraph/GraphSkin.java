package com.markjmind.mobile.api.android.ui.drawgraph;

import java.util.ArrayList;

import android.graphics.Canvas;

public abstract class GraphSkin {
	protected int topMargin;
	protected int bottomMargin;
	protected ArrayList<SimpleStyle> list;
	private int height;
	private int width;
	private int maxMeasure;
	
	public GraphSkin(int maxMeasure){
		this.topMargin=0;
		this.bottomMargin=0;
		this.maxMeasure=maxMeasure;
		list = new ArrayList<SimpleStyle>();
	}
	
	public abstract void draw(Canvas canvas); 
	
	public void add(int meansure, SimpleStyle style){
		SimpleStyle clone =new SimpleStyle(style.color, style.width, style.size, style.margin, style.align);
		clone.setMeansure(meansure);
		list.add(clone);
	}

	public int getTopMargin() {
		return topMargin;
	}

	public void setTopMargin(int topMargin) {
		this.topMargin = topMargin;
	}

	public int getBottomMargin() {
		return bottomMargin;
	}

	public void setBottomMargin(int bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getMaxMeasure() {
		return maxMeasure;
	}

	public void setMaxMeasure(int maxMeasure) {
		this.maxMeasure = maxMeasure;
	}
	
	public int getBoradTopMargin(){
		return 0;
	}
	public int getBoradBottomMargin(){
		return 0;
	}
	
	
}
