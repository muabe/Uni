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
	public interface ItemInit{
		public void init(int index,String itemKey, View itemView, JwGroup jg);
	}
	
	private ViewGroup boarder;
	private ViewGroup parentsView;
	private JwGroup jg;
	private JwOnGroupSelect groupSelect;
	private View onClickView;
	private ItemInit itemInit;
	
	public JwDropDown(Context context) {
		super(context);
		parentsView = new LinearLayout(getContext());
		((LinearLayout)parentsView).setOrientation(LinearLayout.VERTICAL);
		parentsView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		boarder = parentsView;
		jg = new JwGroup();
		jg.setReselected(true);
		jg.setOnGroupSelect(new JwOnGroupSelect() {
			@Override
			public void selected(View v, String itemKey, int index, Object param) {
				dismiss();
			}
			@Override
			public void deselected(View v, String itemKey, int index, Object param) {
			}
		});
	}
	
	public JwDropDown(Context context,View onClickView) {
		this(context);
		this.onClickView = onClickView;
		this.onClickView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				show();
			}
		});
	}
	
	/**
	 * Item들을 초기화한다.
	 */
	private void initItems(){
		if(itemInit!=null){
			for(int i=0;i<jg.size();i++){
				String itemKey = jg.getName(i);
				View itemView = jg.getView(i);
				itemInit.init(i, itemKey, itemView, jg);
			}
		}
	}
	
	/**
	 * 각 Item의 layout에 대해 초기화할 내용을 설정한다. 
	 * @param ItemInit
	 */
	public void setItemsInit(ItemInit itemInit){
		this.itemInit = itemInit;
	}
	
	/**
	 * Item에 해당하는 View를 추가한다.
	 * @param view Item에 해당하는 View
	 * @param itemKey itemKey명
	 * @param param select시 반환되는 param
	 */
	public void addItem(View view,String itemKey, Object param){
		parentsView.addView(view);
		view.setClickable(true);
		jg.add(itemKey, view, param);
	}
	/**
	 * Item에 해당하는 View를 추가한다.<br>
	 * select시 반환되는 param은 null이 된다.
	 * @param view Item에 해당하는 View
	 * @param itemKey itemKey명
	 */
	public void addItem(View view,String itemKey){
		this.addItem(view, itemKey, null);
	}
	
	/**
	 * Item에 해당하는 View를 추가한다.
	 * @param R_layout_id Item에 해당하는 layout id
	 * @param itemKey itemKey명
	 * @param param select시 반환되는 param
	 */
	public void addItem(int R_layout_id, String itemKey, Object param){
		View view = Jwc.getViewInfalter(R_layout_id, getContext());
		this.addItem(view, itemKey, param);
	}
	
	/**
	 * Item에 해당하는 View를 추가한다.<br>
	 * select시 반환되는 param은 null이 된다.
	 * @param R_layout_id Item에 해당하는 layout id
	 * @param itemKey itemKey명
	 */
	public void addItem(int R_layout_id, String itemKey) {
		this.addItem(R_layout_id, itemKey, null);
	}
	
	/**
	 * Item에 해당하는 View를 추가한다.<br>
	 * select시 반환되는 itemKey는 null이 된다.<br>
	 * select시 반환되는 param은 null이 된다.
	 * @param R_layout_id Item에 해당하는 layout id
	 */
	public void addItem(int R_layout_id) {
		this.addItem(R_layout_id, null, null);
	}
	
	/**
	 * Item을 선택한다.
	 * @param itemKey 선택할 Item itemKey
	 */
	public void select(String itemKey){
		jg.select(itemKey);
	}
	
	/**
	 * Item을 선택한다.
	 * @param index 선택할 Item index
	 */
	public void select(int index){
		jg.select(index);
	}
	/**
	 * 사용자가 선택한 Item에 대하여 select/deselect를 처리하는 이벤트 
	 * @param groupSelect Item에 대한 select listener
	 */
	public void setOnSelected(JwOnGroupSelect groupSelect){
		this.groupSelect = groupSelect;
		jg.setOnGroupSelect(new JwOnGroupSelect() {
			@Override
			public void selected(View v, String itemKey, int index, Object param) {
				if(JwDropDown.this.groupSelect!=null){
					JwDropDown.this.groupSelect.selected(v, itemKey, index, param);
				}
				dismiss();
			}
			@Override
			public void deselected(View v, String itemKey, int index, Object param) {
				if(JwDropDown.this.groupSelect!=null){
					JwDropDown.this.groupSelect.deselected(v, itemKey, index, param);
				}
			}
		});
	}
	
	/**
	 * item의 size를 반환한다.
	 */
	public int getItemSize(){
		return jg.size(); 
	}
	/**
	 * BoarderLayout을 설정한다.
	 * @param R_layout_id boarder layout ID
	 * @param add_view_group_id boarder 하위에 Item를 추가할 ViewGourp ID 
	 */
	public void setBoarderLayout(ViewGroup boarder, int add_view_group_id){
		this.boarder = boarder;
		this.boarder.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		this.parentsView = (ViewGroup)this.boarder.findViewById(add_view_group_id);
	}
	
	/**
	 * BoarderLayout을 설정한다.
	 * @param R_layout_id boarder layout ID
	 * @param add_view_group_id boarder 하위에 Item를 추가할 ViewGourp ID 
	 */
	public void setBoarderLayout(int R_layout_id, int add_view_group_id){
		ViewGroup mainView = (ViewGroup)Jwc.getViewInfalter(R_layout_id, getContext());
		setBoarderLayout(mainView,add_view_group_id);
	}
		
	/**
	 * 설정된 Boarder의 Layout을 반환한다.
	 * @return Boarder ViewGroup
	 */
	public ViewGroup getBoarderLayout(){
		return boarder;
	}
	
	/**
	 * dropdown popup을 띄운다.
	 */
	public void show(){
		setMainContentView(boarder);
		initItems();
		super.show(onClickView);
	}
	
	/**
	 * onClickView를 기준으로
	 * dropdown popup을 띄운다.
	 * @param onClickView
	 */
	public void show(View onClickView){
		setMainContentView(boarder);
		initItems();
		super.show(onClickView);
	}
	
	/**
	 * 선택된 Item가 재선택될수 있는지 여부<br>
	 * isReselected의 defalut는 false이다
	 * @param isReselected
	 */
	public void setReselected(boolean isReselected){
		jg.setReselected(isReselected);
	}
}
