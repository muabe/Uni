package com.markjmind.uni.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class JwSlidingLayout extends LinearLayout{
	
	private View main;
	private int minSize;
	private GestureDetector gestureDetector;
	private boolean isFling;
	private boolean isDrag;
	
	private int curr_value; 
	private Handler anim = new Handler();
	private int  vel;
	private int maxSize;
	private Position position;
	private boolean isOpened;
	private float density;
	private boolean isPost;
	private int gravity;
	private SlidingListener sListener;
	
	public static enum Position{
		LEFT,RIGHT,TOP,BOTTOM
	}

	public JwSlidingLayout(Context context) {
		super(context);
		gravity = Gravity.LEFT;
		init(gravity);
	}

	public JwSlidingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		int[] attrsArray = new int[] {android.R.attr.gravity,android.R.attr.minHeight,android.R.attr.minWidth};
		this.density = getResources().getDisplayMetrics().density;
		TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
		gravity = ta.getInt(0, Gravity.LEFT);
		this.minSize = (int)(20*density);
		setMinimumHeight(ta.getDimensionPixelSize(1, (int)(20*density)));
   		init(gravity);
		ta.recycle();
	}
	@Override
	public void setMinimumHeight(int minHeight) {
		super.setMinimumHeight(minHeight);
		this.minSize = (int)(minHeight);

	}	
	
	public void init(int gravity){
		
		this.isFling = false;
		this.isOpened = true;
		this.curr_value = 0;
		this.isPost = true;
		
		setGravity(gravity);
		
		this.post(new Runnable() {
			@Override
			public void run() {
				isPost = true;
				((ViewGroup)getParent()).setFocusableInTouchMode(true);
				if(JwSlidingLayout.this.getChildCount()==0){
					return;
				}
				JwSlidingLayout.this.main = (ViewGroup) JwSlidingLayout.this.getChildAt(0);
				JwSlidingLayout.this.main.setClickable(true); 
				JwSlidingLayout.this.main.setOnTouchListener(new SimpleTouch());
				gestureDetector = new GestureDetector(JwSlidingLayout.this.getContext(), new SimpleOnGesture());
				gestureDetector.setIsLongpressEnabled(false);
				maxSize = getMeansure()-JwSlidingLayout.this.minSize;
				ViewGroup.LayoutParams lp = JwSlidingLayout.this.main.getLayoutParams();
				lp.width = JwSlidingLayout.this.main.getWidth();
				lp.height = JwSlidingLayout.this.main.getHeight();
				JwSlidingLayout.this.main.setLayoutParams(lp);
				JwSlidingLayout.this.main.invalidate();
				setVisible(isOpened);  
				isPost = false;
				
			}
		});
	}
	
	
	@Override
	public void setGravity(int gravity) {
		if(Gravity.LEFT==gravity){
			position = Position.LEFT;
		}else if(Gravity.RIGHT==gravity){
			position = Position.RIGHT;
		}else if(Gravity.TOP==gravity){
			position = Position.TOP;
		}else if(Gravity.BOTTOM==gravity){
			position = Position.BOTTOM;
		}else{
			position = Position.LEFT;
			super.setGravity(Gravity.LEFT);
			return;
		}
		super.setGravity(gravity);
	}
	
	public void setOnSlidingListener(SlidingListener sListener){
		this.sListener = sListener;
	}
	
	public JwSlidingLayout show(){
		if(!isPost && !isOpened){
			if(!isFling && !isDrag){
				int go=1;
				if(position==Position.RIGHT || position==Position.BOTTOM){
					go=go*-1;
				}
				isFling=true;
				aniMove(100*go,true);
			}
		}else{
			isOpened=true;
		}
		return this;
	}
	public JwSlidingLayout hide(){
		if(!isPost && isOpened ){
			if(!isFling && !isDrag){
				int go=-1;
				if(position==Position.RIGHT || position==Position.BOTTOM){
					go=go*-1;
				}
				isFling=true;
				aniMove(100*go,true);
			}
		}else{
			isOpened=false;
		}
		return this;
	}
	
	private JwSlidingLayout setVisible(boolean isVisible){
		if(isPost){
			if(isVisible){
				move(maxSize*-1);
			}else{ 
				move(maxSize);
			}
		}
		return this;
	}
	
	
	
	private void drag(int x){
		if(isFling){
			return;
		}
		if(position==Position.LEFT || position==Position.TOP ){
			x = x*-1;
		}
		isDrag = true;
		move(x);
	}
	
	private void move(int x){
		curr_value +=x;
		
		if(curr_value>=maxSize){
			curr_value = maxSize;
			if(isOpened){
				isOpened = !isOpened;
				if(sListener!=null){
					sListener.closed(this);
				}
			}
		}
		if(curr_value<=0){
			curr_value=0;
			if(!isOpened){
				isOpened = !isOpened;
				if(sListener!=null){
					sListener.opened(this);
				}
			}
		}
		int left = getPaddingLeft();
		int right = getPaddingRight();
		int top = getPaddingTop();
		int bottom = getPaddingBottom();
		if(position==Position.LEFT){
			setPadding(-curr_value, top, curr_value, bottom);
		}else if(position==Position.RIGHT){
			setPadding(curr_value, top, -curr_value, bottom);
		}else if(position==Position.TOP){
			setPadding(left, -curr_value, right, curr_value);
		}else if(position==Position.BOTTOM){
			setPadding(left, curr_value, right, -curr_value);
		}else{
			return;
		}
		invalidate();
	}
	
	
	public void aniMove(float velocity,boolean isAccel){
		if(position==Position.RIGHT || position==Position.BOTTOM ){
			velocity = velocity*-1;
		}
		final int go;
		if(velocity>=0){
			go = -1;
		}else{
			go =1;
		}
		vel = Math.abs((int)(velocity/100/density));
		if(vel>100*density){
			vel = (int)(100*density);
		}
		if(vel < 15*density*1.5f){
			vel = (int)(10*density*1.5f);
		}
		final boolean accel = isAccel;
		new Thread(new Runnable() {
			int accelVel = 0;
			
			@Override
			public void run() {
				if(!accel){
					accelVel=(int)(vel/density);
				}
				while(isFling){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
					anim.post(new Runnable() {
						@Override
						public void run() {
							if(accelVel<=vel){
								accelVel++;
								move(accelVel*go);
							}else{
								move(vel*go);
							}
							if(curr_value>=maxSize || curr_value<=0){
								isFling = false;
							}
						}
					});
					
				}
				isDrag = false;
			}
		}).start();
	}
	
	private int getMeansure(){
		int meansure = 0;
		if(position==Position.LEFT || position==Position.RIGHT){
			meansure = getWidth();
		}else if(position==Position.TOP || position==Position.BOTTOM){
			meansure = getHeight();
		}
		return meansure;
	}
	
	class SimpleOnGesture extends SimpleOnGestureListener{
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			isDrag = false;
			int go=1;
			if(isOpened){
				go=go*-1;
			}
			if(position==Position.RIGHT || position==Position.BOTTOM){
				go=go*-1;
			}
			isFling=true;
			aniMove(100*go,true);
			return false;
		}
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
			if(isFling){
				return false;
			}
			float x = e2.getX()-e1.getX();
			float y = e2.getY()-e1.getY();
			
			if((position==Position.LEFT || position==Position.RIGHT )){
				if(Math.abs(x)<0.3f){
					return false;
				}
//				Log.d("onFling","가로 velocityX:"+velocityX+" ,x:"+x+"("+(Math.abs(x)>=Math.abs(y))+")");
				isDrag = false;
				isFling = true;
				aniMove(x,false);
			}else if((position==Position.TOP || position==Position.BOTTOM)){
				if(Math.abs(y)<0.3f){
					return false;
				}
				isDrag = false;
				isFling = true;
				aniMove(y,false);
			}else{
				return false; 
			}
			return true;
		}
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			try{
				float x = e2.getX()-e1.getX();
				float y = e2.getY()-e1.getY();
				if(position==Position.LEFT || position==Position.RIGHT){
					if(Math.abs(x)<0.5f){
						return false;
					}
					drag((int)x);
				}else if(position==Position.TOP || position==Position.BOTTOM){
					if(Math.abs(y)<0.5f){
						return false;
					}
					drag((int)y);
				}else{
					return false;
				}
	            return true;
			}catch(NullPointerException e){
				return false;
			}
        }
	}
	
	private class SimpleTouch implements OnTouchListener{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			try{
				boolean gd = gestureDetector.onTouchEvent(event);
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_DOWN:
						break;
			    	case MotionEvent.ACTION_UP:
			    		if(isDrag && !isFling){
			    			int go=1;
		    				if(curr_value==0 ||maxSize/2<Math.abs(curr_value)){
		    					go = -1;
		    				}
		    				if(position==Position.RIGHT || position==Position.BOTTOM){
		    					go=go*-1;
		    				}
		    				isFling=true;
							aniMove(200*go,true);
							return true;
			    		}
			    		isDrag = false;
			    		return false;
				}
				return gd;
			}catch(NullPointerException e){
				return false;
			}
		}
	}
	public interface SlidingListener{
		public void opened(JwSlidingLayout slidingLayout);
		public void closed(JwSlidingLayout slidingLayout);
	}
	
}
