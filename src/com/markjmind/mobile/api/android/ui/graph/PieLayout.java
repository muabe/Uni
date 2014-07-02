/**
 * 
 */
package com.markjmind.mobile.api.android.ui.graph;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 원그래프를 그려주는 레이아웃 클래스이다.<br>
 * LinearLayout을 상속하여 확장하였으며 <br>
 * 원그래프의 표현하기 위해 직접적으로 사용하는 Layout 클래스 이다.<br>
 * Layout의 최하단 layer에 원그래프가 그려진다.<br>
 * 원그래프의 호를 add(추가)하는 방식으로 그래프를 설정할수있다.<br>
 * @author 오재웅
 *
 */
public class PieLayout extends FrameLayout{

	public float density;
	private boolean isSizeToScale = true;
	private float strokScale = 1.0f;
	private int strokSize = -1;
	
	private boolean isBackArc = false;
	private Arc backArc;
	private int arcMargin=0;
	
	private float startAngle=0;
	private float maxAngle=360;
	
	private boolean isCapRound = false;
	private boolean isEmboos = false;
	private EmbossMaskFilter emboss;
	
	public ArrayList<Arc> arcs = new ArrayList<Arc>();
	
	private ArrayList<IPieSkin> pieSkin = new ArrayList<IPieSkin>();
	private boolean isInitpieSkin = false;
	
	private int percent = 0;
	/**
	 * LinearLayout 오버라이딩 생성자
	 * @param context
	 */
	public PieLayout(Context context) {
		super(context);
		init();
	}
	
	/**
	 * LinearLayout 오버라이딩 생성자
	 * @param context
	 * @param attrs
	 */
	public PieLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	/**
	 * 그래프 초기화
	 */
	public void init(){
		super.setWillNotDraw(false);
		
		isSizeToScale = true;
		strokSize = -1;
		
		isBackArc = false;
		Arc backArc;
		arcMargin=0;
		
		startAngle=0;
		maxAngle=360;
		
		isCapRound = false;
		emboss =  new EmbossMaskFilter(new float[]{-1,-1,1},0.7f,3,4);
		
		isInitpieSkin = false;
		
		density = getContext().getResources().getDisplayMetrics().density;
		strokScale = 1.0f;
		
		if(arcs.size()>0 || pieSkin.size()>0){
			pieSkin.clear();
			arcs.clear();
			this.invalidate();
		}
	}
	
	
	public void isBackArc(boolean isBackArc){
		this.isBackArc = isBackArc; 
	}
	
	public Arc setBackArc(float start, float end, int color){
		if(backArc==null){
			backArc = new Arc(this);
		}
		backArc.setArc(start, end, color);
		backArc.setCapRound(isCapRound);
		if(isEmboos){
			backArc.setMaskFilter(emboss);
		}
		backArc.setColor(color);
		invalidate();
		return backArc;
	}
	
	public void isEmboos(boolean isEmboos){
		this.isEmboos = isEmboos;
	}
	public void setEmboss(EmbossMaskFilter emboss){
		this.emboss = emboss;
		
	}
	
	public Arc getBackArc(){
		return backArc;
	}
	
	/**
	 * Layout이 표한되는 Canvas를 확장<br>
	 * 원그래프를 내부적으로 Layout에 그려줄수 있게한다.
	 */
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		if(isBackArc){
			backArc.setStrokSize(getStrokSize());
			backArc.setSize(this.getWidth(), this.getHeight(),this.arcMargin);
			backArc.draw(canvas);
		}
		for(int i=0;i<arcs.size();i++){
			Arc arc = (Arc)arcs.get(i);
			arc.setStrokSize(getStrokSize());
			if(!isInitpieSkin){
				for(int j=0;j<pieSkin.size();j++){
					pieSkin.get(j).init(this);
				}
				isInitpieSkin = true;
			}
			arc.setSize(this.getWidth(), this.getHeight(),this.arcMargin);
			arc.draw(canvas);
			for(int j=0;j<pieSkin.size();j++){
				pieSkin.get(j).draw(this, canvas, startAngle, maxAngle, arc.start, arc.end, i);
			}
		}
		
	}
	
	/**
	 * Layout이 표한되는 Canvas를 확장<br>
	 * 원그래프를 내부적으로 Layout에 그려줄수 있게한다.
	 */
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	};


	private ArrayList<Thread> threadPool = new ArrayList<Thread>();
	/**
	 * 선택한 호의 에니메이션 효과를 시작한다.
	 * @param arcIndex 선택할 호의 인덱스
	 */
	public void animStart(int arcIndex){
		for(int i=0;i<threadPool.size();i++){
			threadPool.get(i).interrupt();
		}
		threadPool.clear();
		Thread thread = new Thread(new AnimDraw(arcIndex));
		threadPool.add(thread);
		thread.start();
	}
	
	public void animStart(boolean syc){
		for(int i=0;i<threadPool.size();i++){
			threadPool.get(i).interrupt();
		}
		threadPool.clear();
		if(syc){
			Thread thread = new Thread(new AnimDraw());
			threadPool.add(thread);
			thread.start();
		}else{
			for(int i=0;i<arcs.size();i++){
				Thread thread = new Thread(new AnimDraw(i));
				threadPool.add(thread);
				thread.start();
			}
		}
	}
	
	public void animStart(){
		animStart(true);
	}
	

	
	/**
	 * 에니메이션이 동적으로 표현하게하는 
	 * 에니메이션 Thread 클래스 
	 * @author 오재웅
	 *
	 */
	private class AnimDraw implements Runnable{
		int frame = 150;
		int index=-1;
		public AnimDraw(int index){
			this.index = index;
		}
		public AnimDraw(){
		}
		public void run(){
			if(index>=0){
				animDraw(index);
			}else{
				animDraw();
			}
		}
		
		private void animDraw(int index){
			Arc arc = arcs.get(index);
			animDraw(frame,arc);
		}
		
		private void animDraw(){
			float allEnd=0;
			float preEnd = 0;
			float ends[] = new float[arcs.size()]; 
			for(int i=0;i<arcs.size();i++){
				Arc arc = arcs.get(i);
				ends[i]= arc.end;
				allEnd = allEnd + arc.end;
				arc.end=0;
			}
			postInvalidate();
			
			for(int i=0;i<arcs.size();i++){
				if(!Thread.currentThread().isInterrupted()){
					Arc arc = arcs.get(i);
					arc.end = ends[i];
					int currFrame = Math.round(frame*(arc.end/allEnd));
					animDraw(currFrame,arc);
				}else{
					return;
				}
			}
		}
		
		private void animDraw(int currFrame,Arc arc){
			float temp_end = arc.end;
			for(int j=0;j<currFrame;j++){
				if(!Thread.currentThread().isInterrupted()){
					arc.end =  temp_end*j/currFrame;
			        postInvalidate();
			         if(j!=0){
					      SystemClock.sleep(10);
						}
				}else{
					return;
				}
			}
			arc.end=temp_end;
			postInvalidate();
		}
	}
	
	public void setSizeToScale(boolean isSizeToScale){
		this.isSizeToScale = isSizeToScale;
	}
	
	/**
	 * 원그래프 호의 두께를 받는다.
	 * @return 호의 두께사이즈
	 */
	public int getStrokSize(){
		if(isSizeToScale){
			float width = getWidth();
			float height = getHeight();
			float strok;
			if(width < height){
				strok = width;
			}else{
				strok = height;
			}
			strok=strok/2*strokScale;
			return (int)strok;
		}else{
			return strokSize;
		}
	}
	
	public void setStrokSize(int size){
		strokSize=(int)(size*density);
	}
	

	/**
	 * 시작점(각도)에서 끝점(각도)까지<br>
	 * 해당 컬러로 설정한후 호를 추가한다.<br>
	 * EX) PieLayout.addArc(0,10,blue).addArc(10,20,red).addArc(20,50,gray); <--호를 3개추가
	 * @param start 시작점(각도)
	 * @param end 끝점(각도)
	 * @param color 컬러
	 * @param isInvalidate reFresh 여부
	 * @return 설정된 호의정보 Arc 객체
	 */
	public Arc addArc(float start, float angle, int color,boolean isInvalidate){
		Arc arc = new Arc(this);
		arc.setCapRound(isCapRound);
		if(isEmboos){
			arc.setMaskFilter(emboss);
		}
		arc.setArc(start, angle, color);
		arcs.add(arc);
		if(isInvalidate){
			invalidate();
		}
		
		return arc;
	}
	
	public Arc addArc(int percent, int color,boolean isInvalidate){
		float cap = Math.round(maxAngle - startAngle);
		float curr = Math.round(cap*percent/100) ;
		this.percent = percent;
		if(curr > cap ) {
			curr = cap;
		}
		return addArc(this.startAngle, curr, color, isInvalidate);
	}
	
	/**
	 * 시작점(각도)에서 끝점(각도)까지<br>
	 * 해당 컬러로 설정한후 호를 추가한다.<br>
	 * EX) PieLayout.addArc(0,10,blue).addArc(10,20,red).addArc(20,50,gray); <--호를 3개추가
	 * @param start 시작점(각도)
	 * @param end 끝점(각도)
	 * @param color 컬러
	 * @return 설정된 호의정보 Arc 객체
	 */
	public Arc addArc(float start, float angle, int color){
		Arc arc = new Arc(this);
		arc.setCapRound(isCapRound);
		arc.setArc(start, angle, color);
		arcs.add(arc);		
		invalidate();
		return arc;
	}
	
	public Arc addArc(int percent, int color){
		return addArc(percent, color ,true);
	}
	/**
	 * 원그래프 호두께의 스케일 설정
	 * @param strokScale 호 두께 스케일값
	 */
	public void setScale(float strokScale){
		this.strokScale = strokScale; 
	}
	
	/**
	 * 원그래프 호두꼐의 스케일값을 받는데
	 * @return 스케일값
	 */
	public float getStrokScale(){
		return this.strokScale;
	}
	
	/**
	 * 추가된 호의 정보객체(Arc)를 가져온다.
	 * @param index  
	 * @return Arc 객체
	 */
	public Arc getArc(int index){
		return arcs.get(index); 
	}
	
	/**
	 * 첫번째 Arc객체를 가져온다.
	 * @return Arc 객체
	 */
	public Arc getArc(){
		if(arcs.size()>0){
			return arcs.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 호객체의 갯수를 가져온다.
	 * @return
	 */
	public int getArcCount(){
		return arcs.size();
	}
	
	
	public void setMaxAngle(float startAngle, float maxAngle){
		this.startAngle = startAngle;
		this.maxAngle = maxAngle;
	}
	
	public float getStartAngle(){
		return this.startAngle;
	}
	
	public float getMaxAngle(){
		return this.maxAngle;
	}
	
	public void setMarginArc(int margin){
		arcMargin = (int)(margin*density);
	}
	public int getMaginArc(){
		return arcMargin;
	}
	
	public void addPieSkin(IPieSkin ps){
		pieSkin.add(ps);
	}
	
	public ArrayList getPieSkin(){
		return pieSkin;
	}
	
	public void setCapRound(boolean isCapRound){
		this.isCapRound = isCapRound;
		for(int i=0;i<arcs.size();i++){
			Arc arc = arcs.get(i);
			arc.setCapRound(isCapRound);
		}
	}
	
	public Arc setCapRound(boolean isCapRound, int index){
		Arc arc = arcs.get(index);
		return arc.setCapRound(isCapRound);
	}
	
	public void setMaskFilter(EmbossMaskFilter emboss){
		for(int i=0;i<arcs.size();i++){
			Arc arc = arcs.get(i);
			arc.setMaskFilter(emboss);
		}
	}
	
	public Arc  setMaskFilter(EmbossMaskFilter emboss, int index){
		Arc arc = arcs.get(index);
		return arc.setMaskFilter(emboss);
	}
}
