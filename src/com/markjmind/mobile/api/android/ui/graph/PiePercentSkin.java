package com.markjmind.mobile.api.android.ui.graph;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 파이 그래프에서 원주위를 돌며 퍼센트를 나타내는 스킨
 * 
 * @author 오재웅
 * 
 */
public class PiePercentSkin implements IPieSkin {
	private PieLayout pl;

	private float fontSize;
	private float textSize;

	private MarginLayoutParams layoutParams;
	private LinearLayout layout;
	private TextView textView;

	public PiePercentSkin(float fontSize, float textSize) {
		this.fontSize = fontSize;
		this.textSize = textSize;
	}

	@Override
	public void init(PieLayout pieLayout) {
		this.pl = pieLayout;
		pl.setMarginArc((int) textSize);
		textSize = textSize * pl.density;

		layout = new LinearLayout(pl.getContext());
		layoutParams = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
		layout.setLayoutParams(layoutParams);
		layout.setOrientation(layout.HORIZONTAL);
		textView = new TextView(pl.getContext());
		textView.setLayoutParams(new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,
		        MarginLayoutParams.WRAP_CONTENT));
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(fontSize);
		TextView p = new TextView(pl.getContext());
		MarginLayoutParams marginParams = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,
		        MarginLayoutParams.MATCH_PARENT);
		p.setLayoutParams(marginParams);
		p.setPadding(0, 5, 0, 0);
		p.setText("%");
		p.setTextColor(Color.WHITE);
		p.setTextSize(fontSize / 2);
		layout.addView(textView, layout.getLayoutParams());
		layout.addView(p);
		textView.setVisibility(View.INVISIBLE);
		// pl.addView(layout);
	}

	@Override
	public void draw(PieLayout pieLayout, Canvas canvas, float startAngle, float maxAngle, float startRadians,
	        float radians, int arcIndex) {
		int value = (int) Math.round(radians * 100f / (maxAngle - startAngle));
		if (pl == null) {
			return;
		}
		String strValue = String.valueOf(value);

		float textWidth = fontSize * (strValue.length()) + pl.density * 2 + fontSize / 2;
		float lineLenth = 6f * pl.density;

		float centerX = pl.getWidth() / 2;
		float centerY = pl.getHeight() / 2;

		float r = pl.getHeight() / 2 - textSize;

		float rx = r + textWidth / 2 + lineLenth + lineLenth / 3;
		float ry = r + fontSize + lineLenth + lineLenth / 3;

		float start = startAngle - 90;
		float realRadians = (360 + (radians + start) % 360) % 360;

		float x = (float) (rx * Math.cos(Math.toRadians(realRadians)) + centerX);
		float y = (float) (ry * Math.sin(Math.toRadians(realRadians)) + centerY) - pl.density;

		x = x - textWidth / 2;
		y = y - fontSize - pl.density;

		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(fontSize * pl.density);
		textPaint.setTextAlign(Paint.Align.RIGHT);
		canvas.drawText(strValue, x + fontSize * strValue.length() + pl.density, y + fontSize / 2 * pl.density + 2
		        * pl.density, textPaint);
		textPaint.setTextSize(fontSize * pl.density / 2);
		textPaint.setTextAlign(Paint.Align.LEFT);
		canvas.drawText("%", x + fontSize * strValue.length() + pl.density, y + fontSize / 2 * pl.density + 2
		        * pl.density, textPaint);

		float x1 = (float) ((r) * Math.cos(Math.toRadians(realRadians)) + centerX);
		float y1 = (float) ((r) * Math.sin(Math.toRadians(realRadians)) + centerY);
		float x2 = (float) (x1 + lineLenth * Math.cos(Math.toRadians(realRadians + 30)));
		float y2 = (float) (y1 + lineLenth * Math.sin(Math.toRadians(realRadians + 30)));
		float x3 = (float) (x1 + lineLenth * Math.cos(Math.toRadians(realRadians - 30)));
		float y3 = (float) (y1 + lineLenth * Math.sin(Math.toRadians(realRadians - 30)));
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		Path path = new Path();
		path.reset();
		path.moveTo(x1, y1);

		path.lineTo(x2, y2);
		path.lineTo(x3, y3);

		path.close();
		canvas.drawPath(path, paint);
	}

	/*
	 * 레이 아웃 버전
	 * 
	 * @Override public void draw(PieLayout pieLayout, Canvas canvas, float
	 * startAngle, float maxAngle, float startRadians, float radians, int
	 * arcIndex) { int value
	 * =(int)Math.round(radians*100f/(maxAngle-startAngle)); String strValue =
	 * String.valueOf(value); textView.setText(strValue);
	 * 
	 * float textWidth = layout.getWidth(); float lineLenth = 6f*pl.density;
	 * 
	 * float centerX = pl.getWidth()/2; float centerY = pl.getHeight()/2;
	 * 
	 * 
	 * 
	 * float r = pl.getHeight()/2-textSize;
	 * 
	 * float rx = r+textWidth/2+lineLenth+lineLenth/3; float ry =
	 * r+fontSize+lineLenth+lineLenth/3;
	 * 
	 * float start = startAngle-90; float realRadians =
	 * (360+(radians+start)%360)%360;
	 * 
	 * float x = (float)(rx*Math.cos(Math.toRadians(realRadians))+ centerX);
	 * float y =(float)(ry*Math.sin(Math.toRadians(realRadians))+
	 * centerY)-pl.density;
	 * 
	 * x = x-textWidth/2; y = y-fontSize-pl.density; layout.setX(x);
	 * layout.setY(y);
	 * 
	 * 
	 * 
	 * float x1 = (float)((r)*Math.cos(Math.toRadians(realRadians))+ centerX);
	 * float y1 =(float)((r)*Math.sin(Math.toRadians(realRadians))+ centerY);
	 * float x2 =
	 * (float)(x1+lineLenth*Math.cos(Math.toRadians(realRadians+30))); float y2
	 * =(float)(y1+lineLenth*Math.sin(Math.toRadians(realRadians+30))); float x3
	 * = (float)(x1+lineLenth*Math.cos(Math.toRadians(realRadians-30))); float
	 * y3 =(float)(y1+lineLenth*Math.sin(Math.toRadians(realRadians-30))); Paint
	 * paint = new Paint(); paint.setAntiAlias(true);
	 * paint.setColor(Color.WHITE); paint.setStyle(Paint.Style.FILL); Path path
	 * = new Path(); path.reset(); path.moveTo(x1, y1);
	 * 
	 * path.lineTo(x2,y2); path.lineTo(x3,y3);
	 * 
	 * path.close(); canvas.drawPath(path, paint); }
	 */

}
