package com.markjmind.mobile.api.android.ui.graph;

import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

/**
 * 막대그래프의 Bar 하나를 표현하는 클래스이다.<br>
 * 그래프의 기본단위인 Bar를 그리고 구성한다. <br>
 * 구조는 Head, Body, Bottom으로 구성되어있다.<br>
 * Head는 Bar위에 나타낼 문구나 이미지 또는 Layout을 넣을수 있다.<br>
 * Body는 막대그래프를 표현하는 부분이며 막대그래프가 수치에 따라 그려진다.<br>
 * Body에 필요한 View를 추가할수 있어서 이미지나 문구/Layout을 넣을수있다.<br>
 * Bottom은 Bar 하단에 필요한 이미지,문구,layout을 넣을수 있는 공간이나 <br>
 * 다른 Bar의 Bottom과 동일한 크기로 맞추기 않으면 그래프가 잘못표한될수 있으므로<br>
 * 일단 주석으로 막아 놓았다.<br>
 *  Bottom부분에 표현해야되는 내용은 BarLayout클래스 foot을 이용하여 설정할수있다. 
 * 
 * @author 오재웅
 *
 */
public class Bar extends LinearLayout{
	private LayoutParams thisParams;
	
	private LinearLayout headLyt;
	private LinearLayout bodyLyt;
	
	private LayoutParams headParams;
	private LayoutParams bodyParams;
	
	private BarBody barBody;
	
	private Animation animation;
	
//	private ArrayList<BarElement> figures = new ArrayList<BarElement>();
	
	/**
	 * LinearLayout 오버라이딩 생성자
	 * @param attrs
	 */
	public Bar(Context context, AttributeSet attrs) {
		super(context);
		init();
	}
	
	/**
	 * LinearLayout 오버라이딩 생성자
	 * @param context
	 */
	public Bar(Context context) {
		super(context);
		init();
	}
	
	/**
	 * Bar의 Layout구성을 초기화 한다.
	 */
	public void init(){
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.BOTTOM);
		thisParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		thisParams.weight = 1;
		this.setLayoutParams(thisParams);
		this.removeAllViews();
		
		headLyt = new LinearLayout(getContext());
		bodyLyt = new LinearLayout(getContext());
		
		headParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		bodyParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		bodyParams.weight=1;
		
		headLyt.setLayoutParams(headParams);
		bodyLyt.setLayoutParams(bodyParams);
		
		barBody = new BarBody(getContext());
		bodyLyt.addView(barBody);
		
		super.addView(headLyt);
		super.addView(bodyLyt);
		
	}
	
	
	/**
	 * Bar의 Max 수치를 설정한다.
	 * @param max 수치
	 */
	public void setMax(int max){
		barBody.setMax(max);
	}
	
	/**
	 * Bar의 Max 수치를 가져온다.
	 * @return
	 */
	public int getMax(){
		return barBody.getMax();
	}
	
	/**
	 * Bar의 수치를 설정하고 bar를 추가한다.
	 * @param figure
	 */
	public Bar addFigure(int figure,int color){
		barBody.addFigure(figure,color);
		return this;
	}
	
	/**
	 * Bar의 수치를 설정하고 bar를 추가한다.
	 * @param figure
	 */
	public Bar add(int figure,int color){
		barBody.addFigure(figure,color);
		return this;
	}
	
	/**
	 * Bar의 수치를 설정한다.
	 * @param figure
	 */
	public BarElement setFigure(int figure,int element_index){
		return getElement(element_index).setFigure(figure);
	}
	
	/**
	 * Bar의 컬러를 설정한다.
	 * @param color 컬러
	 */
	public void setBackgroundColor(int color){
		barBody.setBackgroundColor(color);
	}
	
	
	/**
	 * 리소스 Id로 Bar의 컬러를 설정한다.
	 * @param id 리소스 Id
	 */
	public void setBackgroundResource(int id){
		barBody.setBackgroundResource(id);
	}
	
	/**
	 * Bar 사이의 간격을 조절한다.
	 * @param left 왼쪽여백
	 * @param top 상단여백
	 * @param right 오른쪽여백
	 * @param bottom 아래여백
	 */
	public void setMargin(int left,int top,int right,int bottom){
		barBody.setPadding(left, top, right, bottom);
	}	
	
	public BarElement getElement(int index){
		if(barBody.barMain.getChildCount()>0){
			return (BarElement)barBody.barMain.getChildAt(barBody.barMain.getChildCount()-index-1);
		}else{
			return null;
		}
		
	}
	
	/**
	 * Bar의 막대가 그려지는 부분을 구성하며<br>
	 * Bar의 크기,수치등을 조절하는 클래스인다.<br>
	 * 하나가아닌 여러개의 Bar가 겹쳐서 그릴수 있어 <br>
	 * 다중 막대그래프를 표현할수 있다.
	 * @author 오재웅
	 *
	 */
	class BarBody extends LinearLayout{
		private LayoutParams stickParam;
//		private int totalFigure=0;
		public LinearLayout barMain;
		public LayoutParams barMainParam;
		
		private int max=100;
		
		/**
		 * LinearLayout 오버라이딩 생성자.
		 * @param attrs
		 */
		public BarBody(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
			
		}
		
		/**
		 * LinearLayout 오버라이딩 생성자.
		 * @param context
		 */
		public BarBody(Context context) {
			super(context);
			init();
			
		}
		
		/**
		 * Layout의 설정값과 구성을 초기화한다.
		 */
		public void init(){
			//parent
			setMax(max);
			this.setOrientation(LinearLayout.VERTICAL);
			this.setGravity(Gravity.BOTTOM);
			stickParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			stickParam.weight = 1;
			this.setLayoutParams(stickParam);
			this.removeAllViews();
			
			barMain = new LinearLayout(getContext());	
			barMain.setOrientation(LinearLayout.VERTICAL);
			barMainParam = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
			barMain.setLayoutParams(barMainParam);
			
			this.addView(barMain);
		}
		
		
		
		/**
		 * Bar의 Max 수치를 설정한다.
		 * @param max 수치
		 */
		public void setMax(int max){
			this.max = max;
			this.setWeightSum(this.max);
		}
		
		/**
		 * Bar의 Max 수치값을 가져온다.
		 * @return Max 수치값
		 */
		public int getMax(){
			return this.max;
		}
		
		/**
		 * Bar의 수치값을 설정하고 Bar를 추가한다.
		 * @param figure 수치값
		 */
		public void addFigure(int figure, int color){
			int totalFigure = getTotalFigure();
			if(totalFigure<0){
				totalFigure=0;
			}
			totalFigure = totalFigure+figure;
			barMain.setWeightSum(totalFigure);
			barMainParam.weight=totalFigure;
			barMain.setLayoutParams(barMainParam);
			
			BarElement barEl = new BarElement(getContext());
			barEl.init(figure,color);
//			figures.add(barEl);
			barMain.addView(barEl,0);
			
			Animation anim = animation;
			if(anim==null){
				anim =defalutAninmation();
			}
			anim.setDuration(totalFigure*150/max*10);
			barMain.setAnimation(anim);
			
		}
		
		public Animation defalutAninmation(){
			AnimationSet set = new AnimationSet(true);
		       
		       Animation animation = new TranslateAnimation(
		           Animation.RELATIVE_TO_SELF, 0.0f, 
		           Animation.RELATIVE_TO_SELF, 0.0f,
		           Animation.RELATIVE_TO_SELF, 0.9f,
		           Animation.RELATIVE_TO_SELF, 0.0f
		       );
		       Animation animation2 = new AlphaAnimation(0.1f, 1.0f);
			   set.addAnimation(animation2);
		       set.addAnimation(animation);
		       return set;
		}
		
		/**
		 * Bar의 수치값을 가져온다.
		 * @return 수치값
		 */
		public int getTotalFigure(){
			return (int)barMain.getWeightSum();
		}
		
		/**
		 * Bar의 컬러를 설정한다.
		 * @param color 컬러
		 */
		public void setBackgroundColor(int color){
			for(int i=0;i<barMain.getChildCount();i++){
				getElement(i).setBackgroundColor(color);
			}
			
		}
		
		/**
		 * 리소스Id로 Bar의 컬러를 설정한다. 
		 * @param id 리소스 Id
		 */
		public void setBackgroundResource(int id){
			for(int i=0;i<barMain.getChildCount();i++){
				getElement(i).setBackgroundResource(id);
			}
			getElement(0).setBackgroundResource(id);
		}
		
		/**
		 * Bar의 컬러를 설정한다.
		 * @param color 컬러
		 */
		public void setBackgroundColor(int color,int index){
			getElement(index).setBackgroundColor(color);
		}
		
		/**
		 * 리소스Id로 Bar의 컬러를 설정한다. 
		 * @param id 리소스 Id
		 */
		public void setColorResource(int id, int index){
			getElement(index).setBackgroundResource(id);
		}
		
		/**
		 * Bar의 사이간격을 조정한다.
		 * @param left 왼쪽 여백
		 * @param top 상단 여백
		 * @param right 오른쪽 여백
		 * @param bottom 하단 여백
		 */
		public void setMargin(int left,int top,int right,int bottom){
			this.setPadding(left, top, right, bottom);
		}	
	}
}
