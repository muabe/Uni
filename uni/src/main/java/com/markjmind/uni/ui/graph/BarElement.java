/**
 * 
 */
package com.markjmind.uni.ui.graph;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 한 막대에 여러가지 색상을 지정할수 있는데
 * 그 하나하나의 색상을 BarElement라한다
 * @author 오재웅
 *
 */
public class BarElement extends LinearLayout{
	public LayoutParams barParam;
	
	public BarElement(Context context) {
		super(context);
	}
	
	public BarElement(Context context, AttributeSet attrs) {
		super(context,attrs);
	}
	

	/**
	 * BarElement 초기화
	 * @param figure 수치
	 * @param barColor 색상
	 */
	public void init(int figure, int barColor){
		barParam = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
		barParam.weight = figure;
		this.setLayoutParams(barParam);
		setBackgroundColor(barColor);
	}

	/**
	 * BarElement의 수치를 가져온다
	 * @return 수치
	 */
	public int getFigure(){
		return (int)barParam.weight;
	}
	
	/**
	 * BarElement의 수치를 설정한다
	 * @param figure 수치
	 */
	public BarElement setFigure(int figure){
		LinearLayout barMain = (LinearLayout)getParent();
		if(barMain!=null){
			LayoutParams barMainParam=(LayoutParams)barMain.getLayoutParams();
			int totalFigure =  (int)barMain.getWeightSum();
			if(totalFigure<0){
				totalFigure=0;
			}
			totalFigure = totalFigure+figure;
			barMain.setWeightSum(totalFigure);
			barMainParam.weight=totalFigure;
		}
		barParam.weight = figure;
		return this;
	}
	
}
