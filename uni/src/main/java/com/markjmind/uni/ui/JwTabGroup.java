package com.markjmind.uni.ui;

import java.util.ArrayList;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class JwTabGroup implements OnClickListener, OnTouchListener{
	ArrayList<ViewInfo> viewList = new ArrayList<ViewInfo>();
	
	public void drawSelect(boolean select, View v, int index){
		
	}
	
	public ViewInfo getViewInfo(View view){
		for(int i=0;i<viewList.size();i++){
			ViewInfo vInfo = viewList.get(i);
			if(viewList.equals(view)){
				return vInfo;
			}
		}
		return null;
	}
	
	private void add(View view){
		ViewInfo vi = setViewInfo(view,"");
		viewList.add(vi);
	}
	
	private ViewInfo setViewInfo(View view, String ListenerName){
		ViewInfo vi = new ViewInfo();
		vi.index = viewList.size();
		vi.view = view;
		vi.listener=ListenerName;
		return vi;
		
	}
	
	public void add(View view, OnClickListener onClickListener){
		ViewInfo vi = setViewInfo(view,"onClickListener");
		vi.onClickListener=onClickListener;
		view.setOnClickListener(this);
		viewList.add(vi);
	}	
	
	public void add(View view, OnTouchListener onTouchListener){
		ViewInfo vi = setViewInfo(view,"OnTouchListener");
		vi.onTouchListener=onTouchListener;
		view.setOnClickListener(this);
		viewList.add(vi);	
	}
	public void onClick(View v) {
		ViewInfo vInfo = getViewInfo(v);
		drawSelect(true,v,vInfo.index);
		
//		if("onClickListener".equals(vInfo.listener)){
			vInfo.onClickListener.onClick(v);
//		}else if
		
	}
	public boolean onTouch(View arg0, MotionEvent arg1) {
		return false;
	}
	class ViewInfo{
		View view=null;
		int index;
		String listener=null;
		OnClickListener onClickListener=null;
		OnTouchListener onTouchListener=null;
	}



	
}
