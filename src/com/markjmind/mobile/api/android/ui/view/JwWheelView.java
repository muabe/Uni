package com.markjmind.mobile.api.android.ui.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewParent;
import android.widget.LinearLayout;

import com.markjmind.mobile.api.android.ui.view.textview.JwTextViewFonts;

public class JwWheelView extends LinearLayout  implements OnTouchListener{
	
	int measure;			//사이 간격 수치
	int maxCount=10;		//눈금 갯수
	
	int currDistance=0;	
	int currMeasureCount=0;
	boolean isFling =false;
	boolean isMoving =false;
	
	float speedX;
    int goX;
    boolean isPostInt = false;
	public float density;
	
	boolean isHorizontal = false;
	LinearLayout mainLyt;
	ArrayList<LineInfo> lines = new ArrayList<LineInfo>();
	
	private float accelerator = 1.0f;
	private float speed = 1.0f;
	
	protected OnChangeValueListener mOnChangeValueListener;
	
	
	public JwWheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public void init(int maxCount, int measure, boolean isHorizontal){
		this.maxCount = maxCount; 
		this.isHorizontal = isHorizontal;
		currDistance=0;	
		currMeasureCount=0;
		isFling =false;
		isMoving =false;
		if(mainLyt!=null){
			this.removeViewAt(0);
		}
		lines.clear();
		density = getContext().getResources().getDisplayMetrics().density;
		this.measure=(int)(measure*density);
		mainLyt = new LinearLayout(getContext());
		if(isHorizontal){
			mainLyt.setOrientation(LinearLayout.VERTICAL);
		}else{
			mainLyt.setOrientation(LinearLayout.HORIZONTAL);
		}
		this.addView(mainLyt, 0);

		/*
		//센터 빨간색 라인
		LinearLayout centerLine = new LinearLayout(getContext());
		centerLine.setBackgroundColor(Color.RED);
		if(isHorizontal){
			FrameLayout.LayoutParams lp3 = new FrameLayout.LayoutParams((int)density, LayoutParams.MATCH_PARENT);
			lp3.gravity= Gravity.CENTER;
			centerLine.setLayoutParams(lp3);
			this.addView(centerLine);
		}else{
			FrameLayout.LayoutParams lp3 = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)density);
			lp3.gravity= Gravity.CENTER;
			centerLine.setLayoutParams(lp3);
			this.addView(centerLine);	
		}
		*/
		
		this.setOnTouchListener(this);
		gestureDetector = new GestureDetector(getContext(), gestureListener);
        gestureDetector.setIsLongpressEnabled(false);
		this.setClickable(true);
		
		this.post(new Runnable(){
		    public void run(){
		    	postInit();
		    }
		});
	}
	public void postInit(){
		isPostInt = true;
		LinearLayout lineLayout=null;
		CellDisplay child=null;
		int measureCount=0;
		if(isHorizontal){
			mainLyt.setOrientation(LinearLayout.VERTICAL);
			LayoutParams lp2 = new LayoutParams(measure*(maxCount-1)+getWidth(),LinearLayout.LayoutParams.MATCH_PARENT);
			mainLyt.setLayoutParams(lp2);
			for(int i=0;i<lines.size();i++){
				LineInfo info = lines.get(i);
				lineLayout = info.layout;
				child = info.child;
				measureCount = info.measureCount;
				int leftMargin = (int) (getWidth()/2 - measure*info.measureCount/2);
				lineLayout.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.leftMargin = leftMargin;
				lineLayout.setLayoutParams(lp);
			}
		}else{
			mainLyt.setOrientation(LinearLayout.HORIZONTAL);
			LayoutParams lp2 = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, measure*(maxCount-1)+getHeight());
			mainLyt.setLayoutParams(lp2);
			for(int i=0;i<lines.size();i++){
				LineInfo info = lines.get(i);
				lineLayout = info.layout;
				child = info.child;
				measureCount = info.measureCount;
				int topMargin = (int) (getHeight()/2 - measure*info.measureCount/2);
				lineLayout.setOrientation(LinearLayout.VERTICAL);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.topMargin = topMargin;
				lineLayout.setLayoutParams(lp);
			}
		}
		for(int i=0;i<maxCount;i=i+measureCount){
			MeasureCellLyt mc = new MeasureCellLyt(getContext());
//			if(i%2==0){mc.setBackgroundColor(Color.GRAY);}
//			else{mc.setBackgroundColor(Color.GREEN);}
			mc.setMeasure((int)measure*measureCount, child.getView(i));
			lineLayout.addView(mc);
		}
	}
	
	
	public void setOnChangeValueListener(OnChangeValueListener l ){
		mOnChangeValueListener = l;
	}
	
	
	public LinearLayout addLine(int measureCount, CellDisplay child){
		LinearLayout lineLayout = new LinearLayout(getContext());
		lineLayout.setGravity(Gravity.CENTER);
		lines.add(new LineInfo(lineLayout,measureCount,child));
		mainLyt.addView(lineLayout);
		return lineLayout;
	}
	class LineInfo{
		public LinearLayout layout;
		public int measureCount;
		public CellDisplay child;
		public LineInfo(LinearLayout layout, int measureCount, CellDisplay child){
			this.layout = layout;
			this.measureCount = measureCount;
			this.child = child;
		}
	}
	
	class MeasureCellLyt extends LinearLayout{
		public void setMeasure(int measure, View child){
			if(isHorizontal){
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(measure,LinearLayout.LayoutParams.WRAP_CONTENT);
				this.setLayoutParams(lp);
				setOrientation(LinearLayout.HORIZONTAL);
				setGravity(Gravity.CENTER_HORIZONTAL);
			}else{
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,measure);
				this.setLayoutParams(lp);
				setOrientation(LinearLayout.VERTICAL);
				setGravity(Gravity.CENTER_VERTICAL);
			}
			addView(child);
		}
		public MeasureCellLyt(Context context){
			super(context);
		}
	}
	public DefaultCell getDefalutCell(int lineWidth,int lineHeight){
		return new DefaultCell(getContext(), lineWidth, lineHeight);
	}
	public DefaultTextCell getDefaultTextCell(String text){
		return new DefaultTextCell(getContext(), text);
	}
	
	public class DefaultCell extends LinearLayout{
		
		public DefaultCell(Context context,int lineWidth, int lineHeight){
			super(context);
			setBackgroundColor(Color.BLACK);
			if(isHorizontal){
				setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams((int)(lineWidth*density),(int)(lineHeight*density));
				setLayoutParams(lp2);
			}else{
				setOrientation(LinearLayout.VERTICAL);
				LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams((int)(lineHeight*density), (int)(lineWidth*density));
				setLayoutParams(lp2);
			}
		}
	}
	
	public class DefaultTextCell extends LinearLayout{
		JwTextViewFonts textView;
		public DefaultTextCell(Context context,String text){
			super(context);		
			if(isHorizontal){
				setOrientation(LinearLayout.HORIZONTAL);
			}else{
				setOrientation(LinearLayout.VERTICAL);
			}
			LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			setLayoutParams(lp2);
			textView = new JwTextViewFonts(this.getContext());
			textView.setTextSize(18);
			textView.setText(text);
			this.addView(textView);
		}
		
		public JwTextViewFonts getTextView(){
			return textView;
		}
	}
	public interface CellDisplay{
		public View getView(int count);
	}
	
	
	
	
	public void moveMeasure(int count){
		int distance = count*measure;
		move(distance);
	}
	
	public void move(int distance){
		int currTemp = currDistance + distance;
		int maxTemp = (maxCount-1)*measure;
		if(currTemp<0){
			distance = distance-currTemp;
			currDistance = 0;
		}else if(maxTemp<currTemp){
			distance = distance-(currTemp-maxTemp);
			currDistance = maxTemp;
		}else{
			currDistance +=distance;
		}
		
		if(isHorizontal){
			mainLyt.scrollBy(distance, 0);
		}else{
			mainLyt.scrollBy(0, distance);
		}
		if(currDistance/measure!=currMeasureCount){
			currMeasureCount = currDistance/measure;
			onChange(currMeasureCount);
		}
	}
	
	public void aniMove(int distance){
		final int d = distance;
		if(distance>=0){
			movehandler.post(new Runnable() {
				@Override
				public void run() {
					for(int i=0;i<d;i++){
						SystemClock.sleep(10);
						move(1);
					}
				}
			});
		}else{
			movehandler.post(new Runnable() {
				@Override
				public void run() {
					for(int i=d;i==0;i++){
						SystemClock.sleep(10);
						move(-1);
					}
				}
			});
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
	int breakFrame = 20;
	private void aniCheckMeasure(int distance, int sleep){
		int check = distance%measure;
		final int d;
		if(measure<=check*2){
			d = measure-check;
		}else{
			d = -check;
		}
		for(int i=0;i<breakFrame+1;i++){
			final int index = i;
			SystemClock.sleep(sleep);
			handler.post(new Runnable() {
				@Override
				public void run() {
					if(isFling){
						if(index==breakFrame){
							checkMeasure(currDistance);
						}else{
							move(d/breakFrame);
						}
					}
				}
			});
		}
	}
	 
	private void onChange(int currMeasureCount){
		if(mOnChangeValueListener !=null) {
			mOnChangeValueListener.onChangeValue(this, currMeasureCount);
		}
	}
	
	public int getSelectIndex(){
		return currMeasureCount;
	}
	
	
	Handler handler = new Handler();
	Handler movehandler = new Handler();
	private GestureDetector gestureDetector;
	private SimpleOnGestureListener gestureListener =  new SimpleOnGesture();
	class SimpleOnGesture extends SimpleOnGestureListener{
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			if(isHorizontal){
				move((int)(distanceX*speed));	
			}else{
				move((int)(distanceY*speed));
			}
			
            return true;
        }
		
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    		if(isHorizontal){
    			shotSpeed(velocityX*speed);
        	}else{
        		shotSpeed(velocityY*speed);
        	}
    		
            return true;
        }
	}
	
	
	public void shotDistance(int count, int time){
		if(isFling){
			isFling=false;
			checkMeasure(currDistance);
		}
		
		final int distance = count*measure;
		final int frame = time/10;
		final int moveFrame = distance/frame;
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i=0;i<frame+1;i++){
					final int index = i;
					SystemClock.sleep(10);
					if(isMoving){
						break;
					}
					handler.post(new Runnable() {
						@Override
						public void run() {
								if(frame==index){
									move(distance-moveFrame*frame);
								}else{
									move(moveFrame);
								}
						}
					});
				}
			}
		}).start();
		
	}
	
	public void setAccelerator(float accelerator){
		if(accelerator<=1.0f){
			this.accelerator = accelerator;
		}
	}
	
	public void setSpeed(float speed){
			this.speed = speed;
	}
	public void shotSpeed(float velocity){
		speedX = velocity;
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
						while(true){
			        		try {
			        			if(!isFling){
			        				break;
			        			}
			        			if(!frist){
			        				Thread.sleep(30);
			        			}
			        			frist = false;
			        			if(!isFling){
			        				break;
			        			}
			        			x = (int)(speedX/50);
								x = x*goX;
								sum += Math.abs(x);
								
								float accX = 0.95f*accelerator;
								if(speedX<80*10){
									accX = 0.98f*accelerator;
								}
								speedX=(speedX*accX);
								
								
								if(speedX<=10f || Math.abs(x)<measure/breakFrame/2+1){
									if(isFling){
										aniCheckMeasure(currDistance,10);
									}
									break;
								}else{
									final int dx=x;
									handler.post(new Runnable() {
										@Override
										public void run() {
											if(isFling){
 												move(dx);
											}
										}
									});
									int currTemp = currDistance + x;
									int maxTemp = (maxCount-1)*measure;
									if(currTemp<0){
										break;
									}else if(maxTemp<currTemp){
										break;
									}
								}
							} catch (InterruptedException e) {						
							}
			        	}						
						isMoving = false;							
					}
				}).start();
        		break;
        	}
    	}
	}
	
	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
	        ViewParent p = getParent();
	        if (p != null)
	            p.requestDisallowInterceptTouchEvent(true);
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
        void onChangeValue(View v, int val);
    }
	
    public void setGravity(int gravity){
    	mainLyt.setGravity(gravity);
    }
}
