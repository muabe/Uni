package com.markjmind.mobile.api.android.ui;

import java.util.Stack;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.markjmind.mobile.api.hub.Store;

public class JwGroup {
	public Stack<HistoryInfo> history;
	
	public String currView = null;
	public Object currParam = null;
	
	protected Store group = new Store();
	protected int maxBackCount = 10;
	protected JwOnGroupSelect onGroupSelect;
	private boolean isHistory = false; 
	
	public static enum Motion{
		CLICK,
		TOUCH_DOWN
	}
	
	public JwGroup(){
		currView=null;
		history = new Stack<HistoryInfo>();
	}
	
	public JwGroup(JwOnGroupSelect onGroupSelect){
		currView=null;
		this.onGroupSelect = onGroupSelect;
		history = new Stack<HistoryInfo>();
	}
	
	public void setOnGroupSelect(JwOnGroupSelect onGroupSelect){
		this.onGroupSelect = onGroupSelect;
	}
	
	public void add(String name, View click_view){
		Store temp = new Store();
		temp.add("name", name);
		temp.add("view", click_view);
		group.add(name, temp);
		click_view.setOnClickListener(new GroupOnClickListener(name));
	}
	
	public void clear(){
		group.clear();
		history.clear();
	}
	
	public View getView(String name){
		if(name==null){
			return null;
		}
		Store temp = (Store)group.get(name);
		if(temp!=null){
			return (View)temp.get("view");
		}else{
			return null;
		}
	}
	
	public View getCurrentView(){
		return getView(getCurrentName());
	}
	
	public void setListenerMotion(Motion motion){
		String[] keys = group.getKeys();
		switch (motion) {
		case CLICK:
			for(int i=0;i<keys.length;i++){
				Store temp = (Store)group.get(keys[i]);
				View click_view = (View)temp.get("view");
				click_view.setOnClickListener(new GroupOnClickListener(keys[i]));
				click_view.setOnTouchListener(null);
			}
			break;
		case TOUCH_DOWN:
			for(int i=0;i<keys.length;i++){
				Store temp = (Store)group.get(keys[i]);
				View click_view = (View)temp.get("view");
				click_view.setOnClickListener(null);
				click_view.setOnTouchListener(new GroupOnTouchListener(keys[i]));
			}
			break;
		}
	}
	

	public void select(String name, Object param, boolean pushHistory){
		if(name.equals(currView)){
			return;
		}
		if(currView!=null){
			if(pushHistory){
				if(maxBackCount <= history.size()){
					history.remove(0);
				}
				history.push(new HistoryInfo(currView, currParam));
			}
		}
		currView = name;
		currParam = param;
		for(int i=0;i<group.size();i++){
			Store temp = (Store)group.getValue(i);
			String tempName =(String)temp.get("name");
			View view =(View)temp.get("view");
			if(onGroupSelect!=null){
				if(name.equals(tempName)){
					onGroupSelect.selected(view, name, i, param);
				}else{
					onGroupSelect.deselected(view, name, i, param);
				}
			}
		}
	}

	public void select(String name, Object param){
		select(name, param, isHistory);
	}
	public void select(String name){
		select(name,null, isHistory);
	}
	
	public void select(int index){
		select(index, null);
	}
	
	public void select(int index, Object param){
		Store temp = (Store)group.getValue(index);
		select(temp.getString("name"),param);
	}
	
	public void select(int index, Object param, boolean pushHistory){
		Store temp = (Store)group.getValue(index);
		select(temp.getString("name"), param, pushHistory);
	}
	
	public void nextSelect(){
		int index = this.nextIndex();
		select(index);
	}
	
	public void backSelect(){
		int index = this.backIndex();
		select(index);
	}
	
	
	public int size(){
		return group.size();
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
	
	public int nextIndex(){
		int nextIndex = getCurrentIndex()+1;
		if(nextIndex<size()){
			return nextIndex;
		}else{
			return 0;
		}
	}
	
	public int backIndex(){
		int backIndex = getCurrentIndex()-1;
		if(backIndex>=0){
			return backIndex;
		}else{
			return size()-1;
		}
	}
	
	
		
	public String getCurrentName(){
		return currView;
	}
	
	private class GroupOnClickListener implements OnClickListener{
		String name;
		
		public GroupOnClickListener(String name){
			this.name = name;
		}
		@Override
		public void onClick(View v) {
			select(name);
		}
	}
	private class GroupOnTouchListener implements OnTouchListener{
		String name;
		
		public GroupOnTouchListener(String name){
			this.name = name;
		}
		public void onClick(View v) {
			select(name);
		}
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			 switch (event.getAction()){
		        case MotionEvent.ACTION_DOWN:
		        	select(name);
		        break;
		        }
		        return false;

		}
	}
	
	public void setDefalutHistory(boolean isHistory){
		this.isHistory = isHistory;
	}
	
	class HistoryInfo{
		public String viewName;
		public Object param;
		public HistoryInfo(String viewName, Object param){
			this.viewName = viewName;
			this.param = param;
		}
	}
	public void setHistorySize(int size){
		this.maxBackCount=size;
	}
	public boolean back(){
		if(history.size()>0){
			HistoryInfo hInfo =history.pop();
			select(hInfo.viewName,hInfo.param, false);
			return true;
		}
		return false;
	}
	
}
