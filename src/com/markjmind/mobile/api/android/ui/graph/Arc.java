package com.markjmind.mobile.api.android.ui.graph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.RectF;
import android.view.View;

/**
 * * 원그래프의 기초 재료가 되는 클래스 이다.<br>
 * 그래프의 하나의 Arc 영역을 표시 하는 단위이다.<br>
 * 실제적으로 설정된 속성에따라 그래프가 화면에 표현되는 정보를 담았으며<br>
 * 그정보에 따라 canvas에 화면을 그려주는  알고리즘을 포함하고있다.<br>
 * 해당 영역에 대한 속성들을 변경할수 있다.
 * @author 오재웅
 */
public class Arc {
	private PieLayout pl;
	
	public float start=0;
	public float end=360;
	private int color=Color.BLUE;
	private int strokSize=1;
	private Paint paint;
	private RectF oval = new RectF();
	
	private float x=0;
	private float y=0;
	private float height;
	private float width;
	private float margin = 0;
	
	private boolean isCapRound = false; 
	private EmbossMaskFilter emboss = null;
	/**
	 * 생성자
	 * @param pl PieLayout 객체
	 */
	public Arc(PieLayout pl){
		this.pl = pl;
		init();
		setArc(start,end,color);
	}
	
	/**
	 * 생성자
	 * @param width 넓이
	 * @param height 높이
	 * @param pl PieLayout 객체
	 */
	public Arc(float width,float height,PieLayout pl){
		this.pl = pl;
		init();
		setSize(width,height);
		setArc(start,end,color);
	}
	
	private void init(){
		paint = new Paint();
//		pl.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
		if(isCapRound){
			paint.setStrokeCap(Cap.ROUND);
		}
		if(emboss!=null){
			paint.setMaskFilter(emboss);
		}
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		
	}
	
	/**
	 * 사이즈 설정
	 * @param width 넓이
	 * @param height 높이
	 */
	public void setSize(float width,float height, float margin){
		this.margin = margin;
		this.width = width-margin*2;
		this.height = height-margin*2;
		reSetOval();
	}
	
	/**
	 * 사이즈 설정
	 * @param width 넓이
	 * @param height 높이
	 */
	public void setSize(float width,float height){
		this.setSize(width, height,0);
	}
	
	/**
	 *  원그래프가 그려질 범위를 리셋한다.
	 */
	private void reSetOval(){
		float strok = strokSize/2;
		float jirm;
		if(width < height){
			this.x = strok+margin;
			this.y = (height-width)/2+strok+margin;
			jirm = width;
		}else{
			this.x = (width-height)/2+strok+margin;
			this.y = strok+margin;
			jirm = height;
		}
		width = jirm+x-strok*2;
		height = jirm+y-strok*2;
		oval.set(x,y,width,height);
		Math.sin(getRadian(20));
	}
	
	public double getRadian(double degree) {
		double radian = 0;
		radian = Math.PI * degree/180;
		return radian;
	}

	/**
	 * 시작점(각도)으로 부터 끝점(각도)까지의 호를<br>
	 * 정해진 컬러를 설정한다.
	 * @param start 시작점(각도)
	 * @param end 끝점(각도)
	 * @param color 컬러
	 */
	public void setArc(float start, float end, int color){
		this.start = start;
		this.end = end;
		this.color = color;
		setPaint(color);
	}
	
	/**
	 * 시작점과 끝점을 설정한다.
	 * @param start 시작점(각도)
	 * @param end 끝점(각도)
	 */
	public void setAngle(float start, float end){
		this.start = start;
		this.end = end;
	}
	
	/**
	 * 색,질감등의 paint객체를 설정한다.
	 * @param paint paint표현 객체
	 */
	public void setPaint(Paint paint){
		this.paint = paint;
	}
	
	/**
	 * 정해진 컬러로 strokSize 두께만큼 호를 설정한다.
	 * @param color 컬러
	 * @param strokSize 호두께
	 */
	public void setPaint(int color, int strokSize){
		setColor(color);
		setStrokSize(strokSize);
	}
	
	/**
	 * 정해진 컬러로 strokSize 두께만큼 호를 설정한다.
	 * @param color 컬러
	 * @param strokSize 호두께
	 */
	public void setPaint(int color){
		setPaint(color, 5);
	}
	
	/**
	 * 호의 컬러를 설정한다.
	 * @param color 컬러
	 */
	public void setColor(int color){
		this.color = color;
		paint.setColor(color);
	}
	
	/**
	 * 호의 두께를 설정한다.
	 * @param size 크기
	 */
	public void setStrokSize(int size){
		this.strokSize = size;
		paint.setStrokeWidth(strokSize);
	}
	
	/**
	 * 호를 그린다.
	 * @param canvas canvas 객체
	 */
	public void draw(Canvas canvas){
		oval = new RectF(x,y,width,height);
		canvas.drawArc(oval, start-90, end, false, paint);
	}
	
	/**
	 * 이전에 설정되었던 호를 기준으로 이어서 호를 추가한다. 
	 * @param end 끝점(각도)
	 * @param color 컬러
	 */
	public Arc addArcAngle(float end,int color){
		return pl.addArc(this.start+this.end, end, color);
	}
	
	/**
	 * 이전에 설정되었던 호를 기준으로 이어서 호를 추가한다. 
	 * @param end 끝점(각도)
	 * @param color 컬러
	 */
	public Arc addArcPercent(int addPercent,int color){
		float cap = Math.round(pl.getMaxAngle() - pl.getStartAngle());
		float curr = Math.round(cap*addPercent/100);
		return addArcAngle(curr, color);
	}
	
	
	public Arc setCapRound(boolean isCapRound){
		this.isCapRound = isCapRound;
		init();
		return this;
	}
	
	public Arc setMaskFilter(EmbossMaskFilter emboss){
		this.emboss = emboss;
		init();
		return this;
	}
	
	public EmbossMaskFilter getMaskFilter(){
		return emboss;
	}

}
