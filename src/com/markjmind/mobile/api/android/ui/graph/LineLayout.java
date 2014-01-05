package com.markjmind.mobile.api.android.ui.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 막대그래프의 배경의 라인을 표현하기위한 클래스이다.<br>
 * 직선 라인을 필요한 간격으로 Canvas를 이용하여 그려준다.<br>
 * 클래스를 확장하여 점선, 라인의 두께 등 새로운 라인을 커스텀하여 표한할수 있다. 
 * @author 오재웅
 *
 */
public class LineLayout extends LinearLayout{
	private int linecount = 0;
	private int lineColor = Color.parseColor("#aaf6f7db");
	
	/**
	 * LinearLayout 오버라이딩 생성자.
	 * @param context
	 */
	public LineLayout(Context context) {
		super(context);
		this.setWillNotDraw(false);
		
	}

	/**
	 * LinearLayout 오버라이딩 생성자.
	 * @param context
	 * @param attrs
	 */
	public LineLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setWillNotDraw(false);
	}
	
	@Override
	/**
	 * 라인을 그려준다.
	 */
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		drawLines(linecount,canvas);
	}
	
	/**
	 * 라인 색을 설정한다.
	 * @param lineColor 컬러
	 */
	public void setLineColor(int lineColor){
		this.lineColor = lineColor;
	}
	
	/**
	 * 라인의 컬러를 가져온다.
	 * @return 컬러
	 */
	public int getLineColor(){
		return lineColor;
	}
	
	/**
	 * 라인 갯수를 설정하고 Canvas의 라인을 그려준다.
	 * @param lineNum 라인수
	 * @param canvas Canvas 객체
	 */
	public void drawLines(int lineNum,Canvas canvas){
		
		int width = getWidth();
		Paint paint = new Paint();
		paint.setColor(lineColor);
//		paint.setStrokeWidth(2);
		if(lineNum==1){
			
		}else{
			for(int i=0;i<lineNum;i++){
				if(i==0){
					canvas.drawLine(0, 0, width, 1, paint);
				}else if(lineNum-1==i){
					canvas.drawLine(0, getHeight()-1, width, getHeight(), paint);
				}else{
					int height = getHeight()/(lineNum-1)*i;
					canvas.drawLine(0, height, width, height+1, paint);
				}
			}
		}
		
		
	}
	
	public void setLineCount(int lineCount){
		this.linecount = lineCount;
	}
	public int getLineCount(){
		return linecount;
	}
}
