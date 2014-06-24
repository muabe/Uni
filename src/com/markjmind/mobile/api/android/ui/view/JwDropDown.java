package com.markjmind.mobile.api.android.ui.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.markjmind.mobile.api.android.controller.Jwc;
import com.markjmind.mobile.api.android.ui.JwGroup;
import com.markjmind.mobile.api.android.ui.JwOnGroupSelect;

public class JwDropDown extends JwBaseDropDown{
	private ViewGroup boarder;
	private ViewGroup parentsView;
	private JwGroup jg;
	private JwOnGroupSelect groupSelect;
	private View onClickView;
	
	public JwDropDown(Context context) {
		super(context);
		parentsView = new LinearLayout(getContext());
		boarder = parentsView;
		jg = new JwGroup();
		jg.setReselected(true);
	}
	
	public JwDropDown(Context context,View onClickView) {
		super(context);
		this.onClickView = onClickView;
		this.onClickView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				show();
			}
		});
		parentsView = new LinearLayout(getContext());
		boarder = parentsView;
		jg = new JwGroup();
		jg.setReselected(false);
	}
	
	/**
	 * Row에 해당하는 View를 추가한다.
	 * @param view Row에 해당하는 View
	 * @param name Row명
	 * @param param select시 반환되는 param
	 */
	public void addRow(View view,String name, Object param){
		parentsView.addView(view);
		view.setClickable(true);
		jg.add(name, view);
	}
	/**
	 * Row에 해당하는 View를 추가한다.<br>
	 * select시 반환되는 param은 null이 된다.
	 * @param view Row에 해당하는 View
	 * @param name Row명
	 */
	public void addRow(View view,String name){
		this.addRow(view, name, null);
	}
	
	/**
	 * Row에 해당하는 View를 추가한다.<br>
	 * select시 반환되는 name은 null이 된다.<br>
	 * select시 반환되는 param은 null이 된다.
	 * @param view Row에 해당하는 View
	 */
	public void addRow(View view){
		this.addRow(view, null, null);
	}
	
	/**
	 * Row에 해당하는 View를 추가한다.
	 * @param R_layout_id Row에 해당하는 layout id
	 * @param name Row명
	 * @param param select시 반환되는 param
	 */
	public void addRow(int R_layout_id, String name, Object param){
		View view = Jwc.getViewInfalter(R_layout_id, getContext());
		this.addRow(view, name, param);
	}
	
	/**
	 * Row에 해당하는 View를 추가한다.<br>
	 * select시 반환되는 param은 null이 된다.
	 * @param R_layout_id Row에 해당하는 layout id
	 * @param name Row명
	 */
	public void addRow(int R_layout_id, String name) {
		this.addRow(R_layout_id, name, null);
	}
	
	/**
	 * Row에 해당하는 View를 추가한다.<br>
	 * select시 반환되는 name은 null이 된다.<br>
	 * select시 반환되는 param은 null이 된다.
	 * @param R_layout_id Row에 해당하는 layout id
	 */
	public void addRow(int R_layout_id) {
		this.addRow(R_layout_id, null, null);
	}
	
	/**
	 * 사용자가 선택한 Row에 대하여 select/deselect를 처리하는 이벤트 
	 * @param groupSelect row에 대한 select listener
	 */
	public void setOnSelected(JwOnGroupSelect groupSelect){
		this.groupSelect = groupSelect;
		jg.setOnGroupSelect(new JwOnGroupSelect() {
			@Override
			public void selected(View v, String name, int index, Object param) {
				if(JwDropDown.this.groupSelect!=null){
					JwDropDown.this.groupSelect.selected(v, name, index, param);
				}
				dismiss();
			}
			@Override
			public void deselected(View v, String name, int index, Object param) {
				if(JwDropDown.this.groupSelect!=null){
					JwDropDown.this.groupSelect.selected(v, name, index, param);
				}
			}
		});
	}
	
	/**
	 * BoarderLayout을 설정한다.
	 * @param R_layout_id boarder layout ID
	 * @param add_view_group_id boarder 하위에 Row를 추가할 ViewGourp ID 
	 */
	public void setBoarderLayout(ViewGroup boarder, int add_view_group_id){
		this.boarder = boarder;
		this.boarder.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		this.parentsView = (ViewGroup)this.boarder.findViewById(add_view_group_id);
	}
	
	/**
	 * BoarderLayout을 설정한다.
	 * @param R_layout_id boarder layout ID
	 * @param add_view_group_id boarder 하위에 Row를 추가할 ViewGourp ID 
	 */
	public void setBoarderLayout(int R_layout_id, int add_view_group_id){
		ViewGroup mainView = (ViewGroup)Jwc.getViewInfalter(R_layout_id, getContext());
		setBoarderLayout(mainView,add_view_group_id);
	}
	
	/**
	 * dropdown popup을 띄운다.
	 */
	public void show(){
		setMainContentView(boarder);
		super.show(onClickView);
	}
	
	/**
	 * onClickView를 기준으로
	 * dropdown popup을 띄운다.
	 * @param onClickView
	 */
	public void show(View onClickView){
		setMainContentView(boarder);
		super.show(onClickView);
	}
	
	/**
	 * 선택된 Row가 재선택될수 있는지 여부<br>
	 * isReselected의 defalut는 false이다
	 * @param isReselected
	 */
	public void setReselected(boolean isReselected){
		jg.setReselected(isReselected);
	}
}
