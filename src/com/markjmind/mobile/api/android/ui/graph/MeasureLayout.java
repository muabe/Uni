package com.markjmind.mobile.api.android.ui.graph;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MeasureLayout extends FrameLayout  implements OnTouchListener{
	
	int measure;			//사이 간격 수치
	int maxCount=600;		//눈금 갯수
	
	
	int currDistance=0;	
	int currMeasureCount=0;
	float speedX;
    int goX;
    boolean isInit = false;
	public float density;
	float hLineStartY;
	HorizontalScrollView horizontalLyt;
	LinearLayout mainLyt;
	protected OnChangeValueListener mOnChangeValueListener;
	
	
	public MeasureLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.post(new Runnable(){
		    public void run(){
		    	init();
		    	addLine(1,new CellDisplay() {
					@Override
					public View getView(int count) {
						return getDefalutCell(1, 10);
					}
				});
				
		    	addLine(5,new CellDisplay() {
					@Override
					public View getView(int count) {
						return getDefalutCell(1, 10);
					}
				});
		    	
		    	addLine(10,new CellDisplay() {
					@Override
					public View getView(int count) {
						return getDefalutCell(1, 10);
					}
				});
				
				addLine(10,new CellDisplay() {
					@Override
					public View getView(int count) {
						return getDefaultTextCell(""+(40+count/10));
					}
				});
		    }
		});
	}
	
	public void setOnChangeValueListener(OnChangeValueListener l ){
		mOnChangeValueListener = l;
	}
	
	public void addLine(int measureCount, CellDisplay child){
		int leftMargin = (int) (getWidth()/2 - measure*measureCount/2);
		LinearLayout lineLayout = new LinearLayout(getContext()); 
		lineLayout.setOrientation(LinearLayout.HORIZONTAL); 
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = leftMargin;
		lineLayout.setLayoutParams(lp);
//		lineLayout.setPadding(leftMargin, 0, leftMargin, 0);
		mainLyt.addView(lineLayout);
		
		
//		LinearLayout paddingLyt = new LinearLayout(getContext());
//		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(leftMargin,LinearLayout.LayoutParams.MATCH_PARENT);
//		paddingLyt.setLayoutParams(lp2);
//		lineLayout.addView(paddingLyt);
		for(int i=0;i<=maxCount;i=i+measureCount){
			MeasureCellLyt mc = new MeasureCellLyt(getContext());
			if(i%2==0){
//				mc.setBackgroundColor(Color.GRAY);
			}else{
//				mc.setBackgroundColor(Color.GREEN);
			}
			mc.setMeasure((int)measure*measureCount, child.getView(i));
			lineLayout.addView(mc);
		}
	}
	
	class MeasureCellLyt extends LinearLayout{
		
		private int measure;
		
		public void setMeasure(int measure, View child){
			this.measure = measure;
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(measure,LinearLayout.LayoutParams.WRAP_CONTENT);
			this.setLayoutParams(lp);
			setGravity(Gravity.CENTER_HORIZONTAL);
			addView(child);
		}
		
		public MeasureCellLyt(Context context){
			super(context);
		}
	}
	public LinearLayout getDefalutCell(int lineWidth,int lineHeight){
		return new DefaultCell(getContext(), lineWidth, lineHeight);
	}
	public LinearLayout getDefaultTextCell(String text){
		return new DefaultTextCell(getContext(), text);
	}
	
	class DefaultCell extends LinearLayout{
		
		public DefaultCell(Context context,int lineWidth, int lineHeight){
			super(context);
			setBackgroundColor(Color.BLACK);
			LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams((int)(lineWidth*density),(int)(lineHeight*density));
			setLayoutParams(lp2);
		}
	}
	
	class DefaultTextCell extends LinearLayout{
		
		public DefaultTextCell(Context context,String text){
			super(context);			
			LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			setLayoutParams(lp2);
			TextView textView = new TextView(context);
			textView.setText(text);
			this.addView(textView);
		}
	}
	
	interface CellDisplay{
		public View getView(int count);
	}
	
	
	
	
	
	public void init(){
		density = getContext().getResources().getDisplayMetrics().density;
		measure=(int)(10*density);
		hLineStartY=0;
		isInit = true;
		
		horizontalLyt = new HorizontalScrollView(getContext());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
		horizontalLyt.setLayoutParams(lp);
		
		LinearLayout tempLayout = new LinearLayout(getContext());
		tempLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
		tempLayout.setLayoutParams(lp1);
		
		mainLyt = new LinearLayout(getContext());
		mainLyt.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(measure*maxCount+getWidth(),LinearLayout.LayoutParams.MATCH_PARENT);
		mainLyt.setLayoutParams(lp2);
		
//		tempLayout.addView(mainLyt);
//		horizontalLyt.addView(tempLayout);
		this.addView(mainLyt);

		
		LinearLayout centerLine = new LinearLayout(getContext());
		centerLine.setBackgroundColor(Color.RED);
		FrameLayout.LayoutParams lp3 = new FrameLayout.LayoutParams((int)density, LayoutParams.MATCH_PARENT);
		lp3.gravity= Gravity.CENTER;
		centerLine.setLayoutParams(lp3);
		this.addView(centerLine);
		
		
		this.setOnTouchListener(this);
		gestureDetector = new GestureDetector(getContext(), gestureListener);
        gestureDetector.setIsLongpressEnabled(false);
		this.setClickable(true);
//		horizontalLyt.setActivated(false);
		
		moveMeasure(30);
		this.invalidate();
	}
	
	public void moveMeasure(int count){
		int distance = count*measure;
		move(distance);
	}
	
	public void move(int distance){
		int currTemp = currDistance + distance;
		int maxTemp = maxCount*measure;
		if(currTemp<0){
			distance = distance-currTemp;
			currDistance = 0;
		}else if(maxTemp<currTemp){
			distance = distance-(currTemp-maxTemp);
			currDistance = maxTemp;
		}else{
			currDistance +=distance;
		}
		
		mainLyt.scrollBy(distance, 0);
		if(currDistance/measure!=currMeasureCount){
			currMeasureCount = currDistance/measure;
			onChange(currMeasureCount);
		}
		
	}
	
	private void checkMeasure(int distance){
		int check = distance%measure;
		if(measure<=check*2){
			move(measure-check);
		}else{
			move(-check);
		}
		
	}
	 
	private void onChange(int currMeasureCount){
		if(mOnChangeValueListener !=null) {
			mOnChangeValueListener.onChangeValue(this, currMeasureCount);
		}
	}
	
	
	Handler handler = new Handler();
	private GestureDetector gestureDetector;
	private SimpleOnGestureListener gestureListener =  new SimpleOnGesture();
	class SimpleOnGesture extends SimpleOnGestureListener{
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			int distance = (int)distanceX;
			move(distance);
            return true;
        }
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        	speedX = velocityX;
        	goX = -1;
        	if(speedX<0){
        		goX = 1;
        		speedX=speedX*-1;
        	}
        	for(int i=0;i<10;i++){
        		if(isMoving){
        			isFling=false;
	        		try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	        	}else{
	        		isFling = true;
	        		new Thread(new Runnable() {
		        		boolean frist = true;
						@Override
						public void run() {
							isMoving = true;
							int sum =0;
							int x ;
							while(speedX>1f){
								
				        		try {
				        			if(!isFling){
				        				break;
				        			}
				        			if(!frist){
				        				Thread.sleep(10);
				        			}
				        			frist = false;
				        			if(!isFling){
				        				break;
				        			}
				        			x = (int)(speedX/80);
									x = x*goX;
									sum += x % measure ;
									x =( x/measure) * measure + (sum /measure) * measure;
									final int dx = x;
									sum = sum%measure;
									handler.post(new Runnable() {
										@Override
										public void run() {
											if(isFling){
 												move(dx);
											}
										}
									});
									float accX = 0.95f;
									if(speedX<80*10){
										accX = 0.98f;
									}
									speedX=(speedX*accX);
								} catch (InterruptedException e) {						
								}
				        	}
						
							isMoving = false;							
						}
					}).start();
	        		break;
	        	}
        		
        	}
            return true;
        }
	}
	boolean isFling =false;
	boolean isMoving =false;
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			isFling = false;
			break;
	    case MotionEvent.ACTION_UP:
	    	isMoving = false;
	    	checkMeasure(currDistance);
	        break;
		}
		return gestureDetector.onTouchEvent(event);
	}
	
	
	/**
     * value 값의 변경을 감지하는 Listener
     */
    public interface OnChangeValueListener {
        /**
         * Called when the focus state of a view has changed.
         *
         * @param v The view whose state has changed.
         * @param 변경값.
         */
        void onChangeValue(View v, float val);
    }
	
}
