package com.markjmind.mobile.api.android.ui;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class JwIntentController{
	public Activity activity;

	
	public JwIntentController(Activity con)
	{
		this.activity = activity;
	}    
	public View getView(int id){
		return activity.findViewById(id);
	}
	public ViewGroup getGroupView(int id){
		return (ViewGroup)getView(id);
	}
	
	public View addLayout(int child_layout_id, ViewGroup parantView){
		return activity.getLayoutInflater().inflate(child_layout_id, parantView);
	}
	
	public View addLayout(int child_layout_id, int parant_group_layout_id){
		ViewGroup parantView = (ViewGroup)getView(parant_group_layout_id);
		return activity.getLayoutInflater().inflate(child_layout_id, parantView);
	}
	
	public void addLayout(View childView, ViewGroup parantView){
		parantView.addView(childView,parantView.getLayoutParams());	
	}
	

}
