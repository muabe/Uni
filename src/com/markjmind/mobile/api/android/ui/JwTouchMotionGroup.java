package com.markjmind.mobile.api.android.ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.markjmind.mobile.api.hub.Store;

/**
 * JwTouchMotionGroupLisener 정의할떄
 * MotionEvent me가 디폴트로 null일경우를 정의해야한다. 
 * @author win
 *
 */
public class JwTouchMotionGroup {
	public Store group = new Store();
	private String currView = null;
	
	public JwTouchMotionGroup(){
		currView = null;
	}
		
	public void add(View touch_view,JwTouchMotionGroupLisener jwTouchMotionGroupLisener){
		String name = ""+group.size();
		this.add(name,touch_view,jwTouchMotionGroupLisener);
	}
	public void add(String name,View touch_view,JwTouchMotionGroupLisener jwTouchMotionGroupLisener){
		this.add(name,touch_view,jwTouchMotionGroupLisener,null);
	}
	
	public void add(String name,View touch_view,JwTouchMotionGroupLisener jwTouchMotionGroupLisener, MotionEvent me){
		Store temp = new Store();
		temp.add("name", name);
		temp.add("touch_view", touch_view);
		temp.add("jwTouchMotionGroupLisener", jwTouchMotionGroupLisener);
		group.add(name, temp);
		jwTouchMotionGroupLisener.setTouch(false,touch_view,name,group.size()-1,me);
		touch_view.setOnTouchListener(new GroupTouchListener(name));
	}
	
	
	
	public boolean touch(String name, MotionEvent me){
		if(name.equals(currView)){
			return false;
		}
		currView = name;
		for(int i=0;i<group.size();i++){
			Store temp = (Store)group.getValue(i);
			String tempName =(String)temp.get("name");
			View view =(View)temp.get("touch_view");
			JwTouchMotionGroupLisener jwTouchMotionGroupLisener = (JwTouchMotionGroupLisener)temp.get("jwTouchMotionGroupLisener");
			if(name.equals(tempName)){
				jwTouchMotionGroupLisener.setTouch(true,view,name,i, me);
			}else{
				jwTouchMotionGroupLisener.setTouch(false,view,name,i, me);
			}
		}
		return true;
	}
	
	public boolean touch(int index){
		Store temp = (Store)group.getValue(index);
		return touch(temp.getString("name"),null);
	}
	
	public boolean touch(int index, MotionEvent me){
		Store temp = (Store)group.getValue(index);
		return touch(temp.getString("name"),me);
	}
	
	public int getCurrentIndex(){
		String[] keys = group.getKeys();
		int index = -1;
		for(int i=0;i<keys.length;i++){
			if(keys[i].equals(currView)){
				index = i;
				break;
			}
		}
		return index;
	}
		
	public String getCurrentName(){
		return currView;
	}
	
	class GroupTouchListener implements OnTouchListener{
		String name;
		
		public GroupTouchListener(String name){
			this.name = name;
		}
		public boolean onTouch(View arg0, MotionEvent me){
			return touch(name,me);
		}
	}
}
