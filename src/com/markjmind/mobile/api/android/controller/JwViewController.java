package com.markjmind.mobile.api.android.controller;



import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * start : 2012.08.21<br>
 * <br>
 * 동적으로 화면을 교체하거나 특정부분만 <br>
 * 레이아웃을 바꾸는등의 비동기 Layout Controller 이다.<br>
 * 비동기 Layout을 쉬게 접근할수 있는 랩퍼 클래스이며 <br>
 * 기타 Layout에 관련된 Utill을 제공한다.<br>
 * 
 * @author 오재웅
 * @version 2012.08.30
 */
public class JwViewController {
	
	public static View lastChild(ViewGroup parantView){
		return parantView.getChildAt(parantView.getChildCount()-1);
	}
	
	public static View addLayout(View childView, ViewGroup parantView){
		parantView.addView(childView,parantView.getLayoutParams());	
		return lastChild(parantView);
	}
	public static View addLayout(int child_layout_id, ViewGroup parantView){
		
//		return lastChild((ViewGroup)View.inflate(parantView.getContext(),child_layout_id, parantView));
		return lastChild((ViewGroup)((LayoutInflater)parantView.getContext().getSystemService(parantView.getContext().LAYOUT_INFLATER_SERVICE)).inflate(child_layout_id, parantView));
	}
	
	
	public static View addLayout(View childView, int parant_gourp_id,Activity activity){
		ViewGroup parantView =(ViewGroup)getView(parant_gourp_id,activity);
		return addLayout(childView,parantView);
	}
	
	
	public static View addLayout(int child_layout_id, int parant_gourp_id, Activity activity){
		ViewGroup parantView =(ViewGroup)getView(parant_gourp_id, activity);
		return addLayout(child_layout_id,parantView);
	}
	
	
	public static View addLayout(int child_layout_id, ViewGroup parantView,int index){
		View addView = getViewInfalter(child_layout_id,parantView.getContext());
		parantView.addView(addView, index);
		return addView;
	}
	public static View changeLayout(int child_layout_id, ViewGroup parantView){
		parantView.removeAllViews();
		return addLayout(child_layout_id,parantView);		
	}
	public static View changeLayout(View childView, ViewGroup parantView){
		parantView.removeAllViews();
		return addLayout(childView,parantView);
	}
	
	
	public static View changeLayout(View childView, int parant_gourp_id, Activity activity){
		ViewGroup parantView =(ViewGroup)getView(parant_gourp_id,activity);
		parantView.removeAllViews();
		return addLayout(childView,parantView);
	}
	
	
	public static View changeLayout(int child_layout_id, int parant_gourp_id, Activity context){
		ViewGroup parantView =(ViewGroup)getView(parant_gourp_id,context);
		parantView.removeAllViews();
		return addLayout(child_layout_id,parantView);		
	}
	
	
	public static void addListView(int R_layout_listview_id,int R_id_listview_id,View tagetView,int R_id_tagetGroupview_id,BaseAdapter baseAdapter){
		View listview_layout	= View.inflate(tagetView.getContext(),R_layout_listview_id, null);
	    ListView listView = (ListView)listview_layout.findViewById(R_id_listview_id);
		listView.setAdapter(baseAdapter);	
		
	    ViewGroup parantView = (ViewGroup)tagetView.findViewById(R_id_tagetGroupview_id);
	    parantView.addView(listview_layout);
	}
	
	public static void addListView(int R_layout_listview_id,int R_id_listview_id,ViewGroup tagetViewGroup,BaseAdapter baseAdapter){
		View listview_layout	= View.inflate(tagetViewGroup.getContext(),R_layout_listview_id, null);
	    ListView listView = (ListView)listview_layout.findViewById(R_id_listview_id);
		listView.setAdapter(baseAdapter);	

		tagetViewGroup.addView(listview_layout);
	}
	
	public static void changeListView(int R_layout_listview_id,int R_id_listview_id,View tagetView,int R_id_tagetGroupview_id,BaseAdapter baseAdapter){
		ViewGroup parantView = (ViewGroup)tagetView.findViewById(R_id_tagetGroupview_id);
		parantView.removeAllViews(); 
		JwViewController.addListView(R_layout_listview_id, R_id_listview_id, tagetView, R_id_tagetGroupview_id, baseAdapter);
	}

	public static void changeListView(int R_layout_listview_id,int R_id_listview_id,ViewGroup tagetViewGroup,BaseAdapter baseAdapter){
		tagetViewGroup.removeAllViews(); 
		JwViewController.addListView(R_layout_listview_id, R_id_listview_id, tagetViewGroup, baseAdapter);
	}
	
	public static View getViewInfalter(int layout_id, Context context){
//		return LayoutInflater.from(context).inflate(layout_id, null);
		return ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout_id, null);
//		return View.inflate(context, layout_id,null);
	}
	
	public static View getView(int R_id, Activity activity){
		return activity.findViewById(R_id);
	}
	public static View getView(int R_id, Dialog dialog){
		return dialog.findViewById(R_id);
	}
	
	public static View getView(int R_id, View view){
		return view.findViewById(R_id);
	}
	
	public static View getViewTag(Object tag, int parants_id, Activity activity){
		return getTagView(tag, getView(parants_id,activity));
	}
	public static View getViewTag(Object tag, int parants_id, Dialog dialog){
		return getTagView(tag, getView(parants_id,dialog));
	}
	
	
	public static View getViewTag(Object tag, View parants){
		return parants.findViewWithTag(tag);
	}
	public static View getTagView(Object tag, View parants){
		return parants.findViewWithTag(tag);
	}
	
	
	
}
