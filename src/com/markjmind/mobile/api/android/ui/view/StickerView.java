package com.markjmind.mobile.api.android.ui.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class StickerView extends LinearLayout implements OnClickListener{
	
	private StickerListener stickerListener;
	public float density; 
	private int minSize;
	private boolean isSelected;
	private SIZE_MODE min_size_mode;
	public static enum SIZE_MODE{
		WRAP_CONTENT,DP
	}
	
	public StickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void init(){
		density = getContext().getResources().getDisplayMetrics().density;
		minSize=0;
		min_size_mode = SIZE_MODE.WRAP_CONTENT;
		isSelected =false;
		super.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		select();
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {}
	
	public void setStickerListener(StickerListener stickerListener) {
		this.stickerListener = stickerListener;
	}
	
	public void select(){
		if(isSelected){
			return;
		}
		isSelected = true;
		ViewGroup parents = (ViewGroup)getParent();
		if(stickerListener==null){
			stickerListener = new StickerListener() {
				@Override
				public void onSelected(StickerView v, int index) {}
				@Override
				public void onDeSelected(StickerView v, int index) {}
			};
		}
		ArrayList<StickerView> stickerList = new ArrayList<StickerView>();
		
		StickerView preSelectView=null;
		StickerView postSelectView=null;
 		for(int i=0;i<parents.getChildCount();i++){
			if(parents.getChildAt(i) instanceof StickerView){
				StickerView child = (StickerView)parents.getChildAt(i);
				stickerList.add(child);
			}
		}
 		for(int i=0;i<stickerList.size();i++){
 			StickerView child = stickerList.get(i);
 			LayoutParams lp = (LayoutParams)child.getLayoutParams();
			if(this.equals(child)){
				lp.width=0;
				lp.weight=1;
				stickerListener.onSelected(child,i);
				postSelectView = child;
			}else{
				if(child.isSelected()){
					preSelectView = child;
				}
				if(min_size_mode==SIZE_MODE.DP){
					lp.width = minSize;
				}else{
					lp.width = LayoutParams.WRAP_CONTENT;
				}
				lp.weight=0;
				stickerListener.onDeSelected(child, i);
				child.setDeSelected();
			}
			
 		}
 		if(preSelectView!=null && postSelectView!=null){
			ResizeAnimation a = new ResizeAnimation(postSelectView);
			a.setFillBefore(false);
			postSelectView.setAnimation(a);
			ResizeAnimation a2 = new ResizeAnimation(preSelectView);
			a2.setFillBefore(false);
			preSelectView.setAnimation(a2);
			
 		}
	}
	
	public void setMinSize(int dp){
		this.minSize = (int)(dp);
		setMinSizeMode(SIZE_MODE.DP);
	}
	
	public void setMinSizeMode(SIZE_MODE mode){
		min_size_mode = mode;
	}
	
	public void setDeSelected(){
		isSelected = false;
	}
	
	public boolean isSelected(){
		ScaleAnimation s;
		TranslateAnimation t;
		AlphaAnimation a;
		return this.isSelected;
	}
	
	
	class ResizeAnimation extends Animation {
	    private View aniView;
	    private float preHeight;
	    private float postHeight;

	    private float preWidth;
	    private float postWidth;
	    
	    public ResizeAnimation(View v) {
	    	aniView = v;
	    	postWidth = aniView.getWidth();
	    	postHeight = aniView.getHeight();
	        setDuration(500);
	    }

	    @Override
	    protected void applyTransformation(float interpolatedTime, Transformation t) {
	        float height = (preHeight - postHeight) * interpolatedTime + postHeight;
	        float width = (preWidth - postWidth) * interpolatedTime + postWidth;
	        android.view.ViewGroup.LayoutParams p = aniView.getLayoutParams();
	        p.height = (int) height;
	        p.width = (int) width;
	        aniView.requestLayout();
	    }
	    
	    public void setFillBefore(boolean fillBefore) {
//	        aniView.getLayoutParams().width = (int)beforeWidth;
//	        aniView.getLayoutParams().height = (int)beforeHeight;
//	        aniView.requestLayout();
	    	super.setFillBefore(false);
	    }
	    
	    
	    
	    @Override
	    public void initialize(int width, int height, int parentWidth, int parentHeight) {
	        super.initialize(width, height, parentWidth, parentHeight);
	        preWidth = width;
	        preHeight = height;
	    }
	    
	}
	
}
