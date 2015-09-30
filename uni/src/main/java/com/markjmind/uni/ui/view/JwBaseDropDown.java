package com.markjmind.uni.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
/**
 * 
 * @author 오재웅
 * @email markjmind@gmail.com
 */
public class JwBaseDropDown extends Dialog implements OnClickListener{
	private FrameLayout frame;
	private View contentView;
	private Animation animIn;
	private Animation animOut;
	
	protected JwBaseDropDown(Context context){
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
	}
	
	public JwBaseDropDown(Context context, View contentView) {
		 super(context , android.R.style.Theme_Translucent_NoTitleBar);
		 this.contentView = contentView;
	}
	
	protected void setMainContentView(View contentView){
		this.contentView = contentView;
	}
	
	public void setAnimationIn(Animation animIn){
		this.animIn = animIn;
	}
	public void setAnimationOut(Animation animOut){
		this.animOut = animOut;
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		frame=new FrameLayout(getContext());
		frame.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		contentView.setClickable(true);
		if(animIn!=null){
			contentView.setAnimation(animIn);
		}
		frame.addView(contentView);
		super.setContentView(frame);
		frame.setOnClickListener(this);
	}

	
	public void show(View onClickView) {
		super.show();
		int width = onClickView.getWidth();
		int height = onClickView.getHeight();
		int[] location = new int[2];
		int[] flLoc = new int[2];
		frame.getLocationInWindow(flLoc);
		onClickView.getLocationInWindow(location);
		location[1] = location[1]-getStatusBarHeight()+height;
		frame.setPadding(location[0], location[1], 0, 0);
	}
	
	public void show(View v,float childWidth, float childHeight) {
		super.show();
		int width = v.getWidth();
		int height = v.getHeight();
		int[] location = new int[2];
		int[] flLoc = new int[2];
		frame.getLocationInWindow(flLoc);
		v.getLocationInWindow(location);
		location[1] = location[1]-getStatusBarHeight()+height;
		frame.setPadding(location[0], location[1], 0, 0);
	}

	@Override
	public void onClick(View v) {
		this.dismiss();
	}
	
	public int getStatusBarHeight() {
	      int result = 0;
	      int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result =  getContext().getResources().getDimensionPixelSize(resourceId);
	      }
	      return result;
	}
	
	@Override
	public void dismiss() {
		if(animOut!=null){
			animOut.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					JwBaseDropDown.super.dismiss();
				}
			});
			contentView.startAnimation(animOut);
		}else{
			super.dismiss();
		}
		
	}
	
	@Override
	public void onBackPressed() {
		dismiss();
	}
}
