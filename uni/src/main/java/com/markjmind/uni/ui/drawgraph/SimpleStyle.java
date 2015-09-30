package com.markjmind.uni.ui.drawgraph;
import android.graphics.Color;


public class SimpleStyle {
	public int color;
	public int backgroundColor;
	public int width;
	public int size;
	public int margin;
	public int meansure;
	public Align align;
	public static enum Align{
		NONE,LEFT,CENTER,RIGHT
	}
	
	public SimpleStyle(int color, int width, int size, int margin) {
		this.color = color;
		this.width = width;
		this.size = size;
		this.margin = margin;
		this.meansure=0;
		this.backgroundColor = Color.parseColor("#00000000");
	}
	
	public SimpleStyle(int color, int width, int size, int margin, Align align) {
		this.color = color;
		this.width = width;
		this.size = size;
		this.margin = margin;
		this.meansure=0;
		this.align = align;
		this.backgroundColor = Color.parseColor("#00000000");
	}

	public int getColor() {
		return color;
	}

	public SimpleStyle setColor(int color) {
		this.color = color;
		return this;
	}
	
	public int getBackgourndColor() {
		return backgroundColor;
	}

	public SimpleStyle setBackgourndColor(int color) {
		this.backgroundColor = backgroundColor;
		return this;
	}

	public int getWidth() {
		return width;
	}

	public SimpleStyle setWidth(int width) {
		this.width = width;
		return this;
	}

	public int getSize() {
		return size;
	}

	public SimpleStyle setSize(int size) {
		this.size = size;
		return this;
	}

	public int getMargin() {
		return margin;
	}

	public SimpleStyle setMargin(int margin) {
		this.margin = margin;
		return this;
	}

	public int getMeansure() {
		return meansure;
	}

	public SimpleStyle setMeansure(int meansure) {
		this.meansure = meansure;
		return this;
	}

	public Align getAlign() {
		return align;
	}

	public SimpleStyle setAlign(Align align) {
		this.align = align;
		return this;
	}
	
	
}
