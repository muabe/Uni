package com.markjmind.mobile.api.android.ui;

import com.markjmind.mobile.api.hub.Store;

import android.view.MotionEvent;
import android.view.View;

public class JwTouchGroup implements JwTouchMotionGroupLisener{
	
	public Store group = new Store();
	public JwTouchMotionGroup jtmg = new JwTouchMotionGroup();
	
	public void add(View touch_view,JwTouchGroupListener jwTouchGroupListener){
		String name = ""+group.size();
		this.add(name, touch_view, jwTouchGroupListener);
	}
	
	public void add(String name,View touch_view,JwTouchGroupListener jwTouchGroupListener){
		group.add(name, jwTouchGroupListener);
		jtmg.add(name,touch_view,this);
	}
	
	public boolean touch(String name){
		return jtmg.touch(name, null);
	}
	
	public boolean touch(int index){
		return jtmg.touch(index);
	}
	
	public int getCurrentIndex(){
		return jtmg.getCurrentIndex();
	}
		
	public String getCurrentName(){
		return jtmg.getCurrentName();
	}
	
	public boolean setTouch(boolean enable, View v, String name, int index, MotionEvent me) {
		if(me==null || me.getAction()==MotionEvent.ACTION_DOWN){
			JwTouchGroupListener jwTouchGroupListener =(JwTouchGroupListener)group.get(name);
			return jwTouchGroupListener.setTouch(enable, v, name, index);
		}else{
			return false;
		}
		
	}
	
}
