package com.markjmind.uni.ui.drawgraph;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

/**
 * Board형식으로 Board위에 그래프 스킨들을 하나하나 입혀나가는 방식이다.
 * @author 오재웅
 * @phone 010-2898-7850
 * @email markjmind@gmail.com
 * @date 2014. 6. 25.
 */
public class GraphBoard extends View{
	
	private int maxTopMargin;
	private int maxBottomMargin;
	private ArrayList<GraphSkin> graphSkinList= new ArrayList<GraphSkin>();

	public GraphBoard(Context context) {
		super(context);
		init();
	}
	
	public GraphBoard(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	/**
	 * board를 초기화한다.
	 */
	public void init(){
		maxTopMargin = 0;
		maxBottomMargin = 0;
		graphSkinList.clear();
	}
	
	/**
	 * board에 스킨을 추가한다.
	 * @param skin 추가할 스킨
	 */
	public void addSkin(GraphSkin skin){
		if(skin.getBoradTopMargin()>maxTopMargin){
			maxTopMargin = skin.getBoradTopMargin();
		}
		if(skin.getBoradBottomMargin()>maxBottomMargin){
			maxBottomMargin = skin.getBoradBottomMargin();
		}
		graphSkinList.add(skin);
		invalidate();
	}
	
	/**
	 * board에 스킨을 삭제한다.
	 * @param skin 삭제할 스킨
	 */
	public void removeSkin(GraphSkin skin){
		graphSkinList.remove(skin);
		invalidate();
	}
	
	/**
	 * 상단 Margin을 설정한다.
	 * @param topMargin
	 */
	public void setMaxTopMargin(int topMargin){
		maxTopMargin = topMargin;
	}
	/**
	 * 하단 Margin을 설정한다.
	 * @param bottomMargin
	 */
	public void setMaxBottomMargin(int bottomMargin){
		maxBottomMargin = bottomMargin;
	}
	
	private float percent = 1.0f;
	public void setDrawPercent(float percent){
		this.percent = percent;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for(int i=0;i<graphSkinList.size();i++){
			GraphSkin skin = graphSkinList.get(i);
			if(skin.isShow){
				skin.setHeight(getHeight()-maxTopMargin-maxBottomMargin);
				skin.setDrawPercent(percent);
				skin.setWidth(getWidth());
				skin.setTopMargin(maxTopMargin);
				skin.setBottomMargin(maxBottomMargin);
				skin.draw(canvas);
			}
		}
	}
}
