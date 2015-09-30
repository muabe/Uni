package com.markjmind.uni.ui.graph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 파이그래프의 스킨으로 홈화면의 삼각형
 * 
 * @author 오재웅
 * 
 */
public class PieArcRoundSkin implements IPieSkin {
	private PieLayout pl;
	float x, y;
	float rx, ry;
	float dx, dy;
	float dx2, dy2;
	float centerX;
	float centerY;
	float width;
	float height;
	float margin;
	float strokSize = 14f;
	boolean isFrist = true;
	float r;
	float realRadians;
	float startAngle;
	float maxAngle;

	public PieArcRoundSkin() {
	}

	@Override
	public void init(PieLayout pieLayout) {
		this.pl = pieLayout;
		strokSize = pl.getStrokSize();

	}

	@Override
	public void draw(PieLayout pieLayout, Canvas canvas, float startAngle, float maxAngle, float startRadians,
	        float radians, int arcIndex) {
		if (isFrist) {
			if (pl == null) {
				return;
			}
			setSize(pl.getWidth(), pl.getHeight(), pl.getMaginArc(), startAngle,maxAngle);
			isFrist = false;
		}
		if (radians > 0) {
			drawLeftRound(canvas);
		}
		
		if(radians>=maxAngle-startAngle){
			drawRightRound(canvas);
		}
	}

	public void drawLeftRound(Canvas canvas) {
		Paint p = new Paint();
		p.setStyle(Paint.Style.FILL);
		p.setColor(Color.WHITE);
		p.setAntiAlias(true);
		RectF oval = new RectF(dx, dy, dx + strokSize, dy + strokSize);
		
		canvas.drawArc(oval, startAngle + 90, 180, true, p);
	}
	
	public void drawRightRound(Canvas canvas) {
		Paint p = new Paint();
		p.setStyle(Paint.Style.FILL);
		p.setColor(Color.WHITE);
		p.setAntiAlias(true);
		RectF oval = new RectF(dx2, dy2, dx2 + strokSize, dy2 + strokSize);
		
		canvas.drawArc(oval, maxAngle-startAngle+90-180 , 180, true, p);

	}

	public void setSize(float width, float height, float margin, float startAngle, float maxAngle) {
		this.startAngle = startAngle;
		this.margin = margin;
		this.width = width - margin * 2;
		this.height = height - margin * 2;
		reSetOval();
		reSetOval2();
	}

	private void reSetOval() {
		centerX = pl.getWidth() / 2;
		centerY = pl.getHeight() / 2;

		r = pl.getHeight() / 2;
		rx = r - margin - strokSize;
		ry = r - margin - strokSize;
		realRadians = Math.round((360 + (startAngle - 90) % 360) % 360);
		x = (float) (rx * Math.cos(Math.toRadians(realRadians)) + centerX - strokSize / 2);
		y = (float) (ry * Math.sin(Math.toRadians(realRadians)) + centerY);

		float realRadians2 = Math.round((360 + (startAngle + 90) % 360) % 360);

		dx = Math.round(x - (strokSize / 2 * Math.cos(Math.toRadians(realRadians2))));
		dy = Math.round(y - (strokSize / 2 + (strokSize / 2 * Math.sin(Math.toRadians(realRadians2)))));
	}
	
	private void reSetOval2() {
		centerX = pl.getWidth() / 2;
		centerY = pl.getHeight() / 2;

		r = pl.getHeight() / 2;
		rx = r - margin - strokSize;
		ry = r - margin - strokSize;
		realRadians = Math.round((360 + (maxAngle-startAngle - 90) % 360) % 360);
		x = (float) (rx * Math.cos(Math.toRadians(realRadians)) + centerX - strokSize / 2);
		y = (float) (ry * Math.sin(Math.toRadians(realRadians)) + centerY);

		float realRadians2 = Math.round((360 + (maxAngle-startAngle + 90) % 360) % 360);

		dx2 = Math.round(x - (strokSize / 2 * Math.cos(Math.toRadians(realRadians2))));
		dy2 = Math.round(y - (strokSize / 2 + (strokSize / 2 * Math.sin(Math.toRadians(realRadians2)))));
	}
}
