package com.markjmind.mobile.api.android.ui;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class JwAnimation {
//	 public static void drop(ViewGroup panel,int duration) {
//
//	       AnimationSet set = new AnimationSet(true);
//
//	       Animation animation = new AlphaAnimation(0.0f, 1.0f);
//	       animation.setDuration(duration);
//	       set.addAnimation(animation);
//
//	       
//	       animation = new TranslateAnimation(
//	           Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
//	           Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
//	       );
//	       animation.setDuration(duration);
//	       set.addAnimation(animation);
//	       
////	       ScaleAnimation scale = new ScaleAnimation(-1, 1, -1, 1, 
////	         ScaleAnimation.RELATIVE_TO_SELF, 1f, 
////	         ScaleAnimation.RELATIVE_TO_SELF, 1f); 
////	       set.addAnimation(scale);
//	       
//	       panel.startAnimation(set);
//	    }
	 
	 public static Animation down(View panel,int duration,AnimationListener al) {
	       AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, 0.0f, 
	           Animation.RELATIVE_TO_SELF, 0.0f,
	           Animation.RELATIVE_TO_SELF, 0.7f,
	           Animation.RELATIVE_TO_SELF, 0.0f
	       );
	       if(al!=null){
	    	   set.setAnimationListener(al);
	       }
	       animation.setDuration(duration);
	       set.addAnimation(animation);
	       panel.startAnimation(set);
	       return animation;
	 }
	 
	 public static AnimationSet getDisLeft(int duration) {
	       AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, 0.0f, 
	           Animation.RELATIVE_TO_SELF, -1.0f,
	           Animation.RELATIVE_TO_SELF, 0.0f,
	           Animation.RELATIVE_TO_SELF, 0.0f
	       );
	       
	       Animation animation2 = new AlphaAnimation(1.0f, 0.0f);
		   animation2.setDuration(duration);
		   set.addAnimation(animation2);
		     
	       animation.setDuration(duration);
	       set.addAnimation(animation);
	       return set;
	 }
	 public static AnimationSet getEnLeft(int duration) {
	       AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, 1.0f, 
	           Animation.RELATIVE_TO_SELF, 0.0f,
	           Animation.RELATIVE_TO_SELF, 0.0f,
	           Animation.RELATIVE_TO_SELF, 0.0f
	       );
	       Animation animation2 = new AlphaAnimation(0.0f, 1.0f);
		   animation2.setDuration(duration);
		   set.addAnimation(animation2);
		   
	       animation.setDuration(duration);
	       set.addAnimation(animation);
	       return set;
	 }
	 
	 public static AnimationSet getDisRight(int duration) {
	       AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, 0.0f, 
	           Animation.RELATIVE_TO_SELF, 1.0f,
	           Animation.RELATIVE_TO_SELF, 0.0f,
	           Animation.RELATIVE_TO_SELF, 0.0f
	       );
	       
	       Animation animation2 = new AlphaAnimation(1.0f, 0.0f);
		   animation2.setDuration(duration);
		   set.addAnimation(animation2);
		   
	       animation.setDuration(duration);
	       set.addAnimation(animation);
	       return set;
	 }
	 
	 public static AnimationSet getComeLeft(int duration) {
		   AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, 0.0f, 
	           Animation.RELATIVE_TO_SELF, -0.3f,
	           Animation.RELATIVE_TO_SELF, 0.0f,
	           Animation.RELATIVE_TO_SELF, 0.0f
	       );
	       animation.setDuration(duration/2);
	       set.addAnimation(animation);
	       
	       Animation animation2 = new TranslateAnimation(
		           Animation.RELATIVE_TO_SELF, 0.0f, 
		           Animation.RELATIVE_TO_SELF, 0.3f,
		           Animation.RELATIVE_TO_SELF, 0.0f,
		           Animation.RELATIVE_TO_SELF, 0.0f
		       );
		   animation2.setDuration(duration/2);
		   animation2.setStartOffset(duration/2);
	       set.addAnimation(animation2);
	       return set;
	 }
	 public static AnimationSet getComeRight(int duration) {
	       AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, 0.0f, 
	           Animation.RELATIVE_TO_SELF, 0.3f,
	           Animation.RELATIVE_TO_SELF, 0.0f,
	           Animation.RELATIVE_TO_SELF, 0.0f
	       );
	       animation.setDuration(duration/2);
	       set.addAnimation(animation);
	       
	       Animation animation2 = new TranslateAnimation(
		           Animation.RELATIVE_TO_SELF, 0.0f, 
		           Animation.RELATIVE_TO_SELF, -0.3f,
		           Animation.RELATIVE_TO_SELF, 0.0f,
		           Animation.RELATIVE_TO_SELF, 0.0f
		       );
		   animation2.setDuration(duration/2);
		   animation2.setStartOffset(duration/2);
	       set.addAnimation(animation2);
	       return set;
	 }

	 public static AnimationSet getEnRight(int duration) {
	       AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, -1.0f, 
	           Animation.RELATIVE_TO_SELF, 0.0f,
	           Animation.RELATIVE_TO_SELF, 0.0f,
	           Animation.RELATIVE_TO_SELF, 0.0f
	       );
	       Animation animation2 = new AlphaAnimation(0.0f, 1.0f);
		   animation2.setDuration(duration);
		   set.addAnimation(animation2);
		   
	       animation.setDuration(duration);
	       set.addAnimation(animation);
	       return set;
	 }
	 public static AnimationSet getEnUp(int duration) {
	       AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, 0.0f, 
	           Animation.RELATIVE_TO_SELF, 0.0f,
	           Animation.RELATIVE_TO_SELF, 1.0f,
	           Animation.RELATIVE_TO_SELF, 0.0f
	       );
	       Animation animation2 = new AlphaAnimation(0.0f, 1.0f);
		   animation2.setDuration(duration);
		   set.addAnimation(animation2);
		   
	       animation.setDuration(duration);
	       set.addAnimation(animation);
	       return set;
	 }
	 
	 public static Animation bottom(View panel,int duration,AnimationListener al) {
	       AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, 0.0f,	//x축 시작점
	           Animation.RELATIVE_TO_SELF, 0.0f,	//x축 종료점
	           Animation.RELATIVE_TO_SELF, 0.0f,	//y축 시작점
	           Animation.RELATIVE_TO_SELF, 1.0f		//y축 종료점
	       );
	       if(al!=null){
	    	   set.setAnimationListener(al);
	       }
	       animation.setDuration(duration);
	       set.addAnimation(animation);
	       panel.startAnimation(set);
	       return animation;
	 }
	 
	 public static Animation up(View panel,int duration,AnimationListener al) {
	       AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, 0.0f,	//x축 시작점
	           Animation.RELATIVE_TO_SELF, 0.0f,	//x축 종료점
	           Animation.RELATIVE_TO_SELF, 0.0f,	//y축 시작점
	           Animation.RELATIVE_TO_SELF, -1.0f	//y축 종료점
	       );
	       animation.setDuration(duration);
	       
	       set.addAnimation(animation);
	       
	       if(al!=null){
	    	   set.setAnimationListener(al);
	       }
	       panel.startAnimation(set);
	       return animation;	       
	 }
	 
	 
	 
	 public static Animation upFromDown(View panel,int duration,AnimationListener al) {
	       AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, 0.0f,	//x축 시작점
	           Animation.RELATIVE_TO_SELF, 0.0f,	//x축 종료점
	           Animation.RELATIVE_TO_SELF, -0.2f,	//y축 시작점
	           Animation.RELATIVE_TO_SELF, 0.2f	//y축 종료점
	       );
	       animation.setDuration(duration);
	       
	       set.addAnimation(animation);
	       
	       if(al!=null){
	    	   set.setAnimationListener(al);
	       }
	       panel.startAnimation(set);
	       return animation;	       
	 }
	 
	 public static Animation right(ViewGroup panel,int duration,AnimationListener al) {
	       AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, -0.1f,	//x축 시작점
	           Animation.RELATIVE_TO_SELF, 0.1f,	//x축 종료점
	           Animation.RELATIVE_TO_SELF, 0.0f,	//y축 시작점
	           Animation.RELATIVE_TO_SELF, 0.0f		//y축 종료점
	       );
	       animation.setDuration(duration);
	       set.addAnimation(animation);
	       
	              
	       if(al!=null){
	    	   set.setAnimationListener(al);
	       }
	       panel.startAnimation(set);
	       return animation;	       
	 }
	 
	 public static Animation rightOut(ViewGroup panel,int duration,AnimationListener al) {
	       AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, 0.0f,	//x축 시작점
	           Animation.RELATIVE_TO_SELF, 1.0f,	//x축 종료점
	           Animation.RELATIVE_TO_SELF, 0.0f,	//y축 시작점
	           Animation.RELATIVE_TO_SELF, 0.0f		//y축 종료점
	       );
	       animation.setDuration(duration);
	       set.addAnimation(animation);
	       
	              
	       if(al!=null){
	    	   set.setAnimationListener(al);
	       }
	       panel.startAnimation(set);
	       return animation;	       
	 }
	 
	 public static Animation left(ViewGroup panel,int duration,AnimationListener al) {
	       AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, 1.0f,	//x축 시작점
	           Animation.RELATIVE_TO_SELF, 0.0f,	//x축 종료점
	           Animation.RELATIVE_TO_SELF, 0.0f,	//y축 시작점
	           Animation.RELATIVE_TO_SELF, 0.0f		//y축 종료점
	       );
	       animation.setDuration(duration);
	       set.addAnimation(animation);
	       
	              
	       if(al!=null){
	    	   set.setAnimationListener(al);
	       }
	       panel.startAnimation(set);
	       return animation;	       
	 }
	 
	 public static Animation leftToRemove(ViewGroup panel,int duration) {
		 AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, 1.0f,	//x축 시작점
	           Animation.RELATIVE_TO_SELF, -1.0f,	//x축 종료점
	           Animation.RELATIVE_TO_SELF, 0.0f,	//y축 시작점
	           Animation.RELATIVE_TO_SELF, 0.0f		//y축 종료점
	       );
	       animation.setDuration(duration);
	       set.addAnimation(animation);
	       
    	   set.setAnimationListener(new RemoveFirst(panel));

    	   panel.startAnimation(set);
	       return animation;	       
	 }
	 public static Animation show(ViewGroup panel,int duration,AnimationListener al) {
		 AnimationSet set = new AnimationSet(true);
	     
	     Animation animation = new AlphaAnimation(0.0f, 1.0f);
	     animation.setDuration(duration);
	     set.addAnimation(animation);
	     
	     
	            
	     if(al!=null){
	  	   set.setAnimationListener(al);
	     }
	     panel.startAnimation(set);
	     return animation;	
	 }
	 
	 public static Animation showView(View panel,int duration,AnimationListener al) {
		 AnimationSet set = new AnimationSet(true);
	     
		 Animation animation = new TranslateAnimation(
		           Animation.RELATIVE_TO_SELF, -1.0f,	//x축 시작점
		           Animation.RELATIVE_TO_SELF, 0.0f,	//x축 종료점
		           Animation.RELATIVE_TO_SELF, -1.0f,	//y축 시작점
		           Animation.RELATIVE_TO_SELF, 0.0f		//y축 종료점
		       );
		 animation.setDuration(duration);
	     set.addAnimation(animation);
		 
	     animation = new AlphaAnimation(0.0f, 1.0f);
	     animation.setDuration(duration);
	     set.addAnimation(animation);
	     
	     ScaleAnimation scale = new ScaleAnimation(
										    		 -1.0f,	//x축 시작점
										    		 1.0f,//x축 종료점
										    		 -1.0f,	//y축 시작점
										    		 1.0f,//y축 종료점 	
	    		 ScaleAnimation.RELATIVE_TO_SELF, 0.7f, 
	    		 ScaleAnimation.RELATIVE_TO_SELF, 0.7f);
	     scale.setDuration(duration);
	     set.addAnimation(scale);
   
	     
	     
	            
	     if(al!=null){
	  	   set.setAnimationListener(al);
	     }
	     panel.startAnimation(set);
	     return null;	
	 }
	 
	 public static Animation hide(ViewGroup panel,int duration) {
		 AnimationSet set = new AnimationSet(true);
	     
	     Animation animation = new AlphaAnimation(1.0f,0.0f);
	     animation.setDuration(duration);
	     set.addAnimation(animation);
     
	            
	     set.setAnimationListener(new RemoveFirst(panel));
	     
	     panel.startAnimation(set);
	     return animation;	
	 }
	 
	 public static Animation remove(ViewGroup panel) {
		return hide(panel,100);
	 }
	 
	 public static Animation add(ViewGroup panel) {
		 return show(panel,300,null);	
	 }
	 
	 
	 public static Animation upToleft(ViewGroup panel,int duration,AnimationListener al) {
		 AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, -1.0f,	//x축 시작점
	           Animation.RELATIVE_TO_SELF, 0.0f,	//x축 종료점
	           Animation.RELATIVE_TO_SELF, 0.5f,	//y축 시작점
	           Animation.RELATIVE_TO_SELF, 0.0f		//y축 종료점
	       );
	       animation.setDuration(duration);
	       set.addAnimation(animation);
	       
	       if(al!=null){
	    	   set.setAnimationListener(al);
	       }

    	   panel.startAnimation(set);
	       return animation;	       
	 }
	 public static Animation downToright(ViewGroup panel,int duration,AnimationListener al) {
		 AnimationSet set = new AnimationSet(true);
	       
	       Animation animation = new TranslateAnimation(
	           Animation.RELATIVE_TO_SELF, -1.0f,	//x축 시작점
	           Animation.RELATIVE_TO_SELF, 0.0f,	//x축 종료점
	           Animation.RELATIVE_TO_SELF, -0.5f,	//y축 시작점
	           Animation.RELATIVE_TO_SELF, 0.0f		//y축 종료점
	       );
	       animation.setDuration(duration);
	       set.addAnimation(animation);
	       
	       if(al!=null){
	    	   set.setAnimationListener(al);
	       }

    	   panel.startAnimation(set);
	       return animation;	       
	 }
	 static class RemoveFirst implements AnimationListener{
		ViewGroup panel;
		public RemoveFirst(ViewGroup panel){
			 this.panel = panel;
		}
		public void onAnimationStart(Animation animation) {}	
		public void onAnimationRepeat(Animation animation) {}
		public void onAnimationEnd(Animation animation) {
			panel.removeViewAt(0);
		}
	 }
	 
	 

}
