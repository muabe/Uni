package com.markjmind.mobile.api.android.controller;

import java.util.Stack;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * start : 2013.11.17<br>
 * <br>
 * 
 * @author 오재웅
 * @version 2013.11.17
 */
public class JwActivity extends Activity {
	private int MAX_HISTORY;
	private Stack<History> historyStack = new Stack<History>();
	

	public void setContentView(Class viewer_Class){
		JwViewer viewer = this.getCustomLayout(viewer_Class);
        setContentView(viewer);
	}
	public void setContentView(JwViewer viewer) {
		super.setContentView(viewer.getLayout());
		viewer.view_init();
	}

	public JwViewer getCustomLayout(Class cls) {
		JwViewer bl = null;
		try {
			bl = (JwViewer) cls.newInstance();
			bl.init(this, bl.getLayoutId());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return bl;
	}

	
	/*
	 * 컨트롤 영역
	 */
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void changeLayout(JwViewer viewer, ViewGroup parents, boolean isHistory) {
		View view = Jwc.changeLayout(viewer.getLayoutId(), parents);
		if(isHistory){
			addHistory(null, view, parents,History.CHANGE);
		}
		viewer.view_init();
	}
	public void changeLayout(JwViewer viewer, ViewGroup parents) {
		View view = Jwc.changeLayout(viewer.getLayoutId(), parents);
		viewer.view_init();
	}

	
	public void changeLayout(JwViewer viewer, int R_id_parents, boolean isHistory) {
		ViewGroup parents = (ViewGroup)Jwc.getView(R_id_parents, this);
		changeLayout(viewer, parents, isHistory);
	}
	public void changeLayout(JwViewer viewer, int R_id_parents) {
		changeLayout(viewer,R_id_parents,false);
	}
	
	public void addLayout(JwViewer viewer, int R_id_parents, boolean isHistory) {
		ViewGroup parents = (ViewGroup)Jwc.getView(R_id_parents, this);
		View view = Jwc.addLayout(viewer.getLayoutId(), parents);
		if(isHistory){
			addHistory(view, null, parents,History.CHANGE);
		}
		viewer.view_init();
	}
	public void addLayout(JwViewer viewer, int R_id_parents){
		addLayout(viewer, R_id_parents, false);
	}
	
	
	
	
	
	/*
	 * 히스토리 영역
	 */
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onBackPressed() {
		if(!back()){
			super.onBackPressed();
		}
	}
	
	public void addHistory(View front, View back, ViewGroup parents,int status){
		History history = new History(front,back,parents,status);
		historyStack.push(history);
	}
	
	public boolean back(){
		if(historyStack.size()>0){
			History history = historyStack.pop();
			history.back();
			return true;
		}
		return false;
	}
	
	class History{
		public ViewGroup parents;
		public View front;
		public View back;
		
		public static final int CHANGE =0;
		public static final int ADD = 1;
		
		int status=0;
		
		public History(View front, View back,ViewGroup parents, int status){
			this.front = front;
			this.back = back;
			this.parents = parents;
			this.status = status;
		}
		
		public void back(){
			if(status==CHANGE){
				Jwc.changeLayout(back,parents);
			}else if(status==ADD){
				parents.removeView(front);
			}
		}
		
		
	}
}
