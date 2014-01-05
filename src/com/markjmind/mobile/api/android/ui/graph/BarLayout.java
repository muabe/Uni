package com.markjmind.mobile.api.android.ui.graph;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 원그래프의 표현하기 위해 직접적으로 사용하는 Layout 클래스 이다.
 * @author 오재웅
 *
 */
public class BarLayout extends LinearLayout{

	private int barColor=Color.BLUE;	
	
	public HeaderInfo header;
	public LeftInfo left;
	public BarInfo bar;
	public FootInfo foot;
	
	public float marginScale = 1f;
	private int max = 100;
	
	
	
	public LinearLayout main;
	public LinearLayout leftLyt;
	public LinearLayout rightLyt;
	public LinearLayout center;
	public LinearLayout headerLyt;
	public LineLayout body;
	public LinearLayout footLyt;
	
	private LayoutParams leftParams;
	private LayoutParams rightParams;
	private LayoutParams centerParams;
	private LayoutParams headerParams;
	private LayoutParams bodyParams;
	private LayoutParams footParams;
	
	
	
	private ArrayList<BarStyle> chartInfos = new ArrayList<BarStyle>();
	/**
	 * LinearLayout 오버라이딩 생성자
	 * @param context
	 * @param attrs
	 */
	public BarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		this.post(new Runnable(){
		    public void run(){
		    	bar.refreshMargin();
		    	initPadding();
		    }
		});
//		center.setBackgroundColor(Color.BLUE);
//		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.bar, 0, 0);
//
//		   try {
//		       int ok = a.getInteger(R.styleable.bar_figure, 0);
//		       if(ok>0){
//		    	   center.setBackgroundColor(Color.RED);
//		       }else{
//		    	   center.setBackgroundColor(Color.YELLOW);
//		       }
//		   } finally {
//		       a.recycle();
//		   }
		
	}
	
	private int figure;
	public int getFigure(){
		return figure;
	}
	public void setFigure(int figure){
		this.figure = figure;
	}
	
	
	/**
	 * 레이아웃을 초기화한다.
	 */
	public void init(){
		barColor=Color.BLUE;	
		header = new HeaderInfo();
		left = new LeftInfo();
		bar = new BarInfo();
		foot = new FootInfo();
		marginScale = 1f;
		max = 100;
		
		Context context = getContext();
		chartInfos.clear();
		this.removeAllViews();
		this.setOrientation(LinearLayout.HORIZONTAL);
		leftLyt = new LinearLayout(context);
		leftLyt.setOrientation(LinearLayout.VERTICAL);
		rightLyt = new LinearLayout(context);
		rightLyt.setOrientation(LinearLayout.VERTICAL);
		leftParams= new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		rightParams= new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		leftLyt.setLayoutParams(leftParams);
		rightLyt.setLayoutParams(rightParams);
		
		center = new LinearLayout(context);
		headerLyt = new LinearLayout(context);
		body = new LineLayout(context);
		footLyt = new LinearLayout(context);
		centerParams= new LayoutParams(0,LayoutParams.MATCH_PARENT,1);
		headerParams= new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		bodyParams= new LayoutParams(LayoutParams.MATCH_PARENT,0,1);
		footParams= new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		center.setLayoutParams(centerParams);
		headerLyt.setLayoutParams(headerParams);
		body.setLayoutParams(bodyParams);
		footLyt.setLayoutParams(footParams);
		center.setOrientation(LinearLayout.VERTICAL);
		headerLyt.setOrientation(LinearLayout.HORIZONTAL);
		body.setOrientation(LinearLayout.HORIZONTAL);
		footLyt.setOrientation(LinearLayout.HORIZONTAL);
		
		headerLyt.setLayoutParams(headerParams);
		body.setLayoutParams(bodyParams);
		footLyt.setLayoutParams(footParams);
		
		headerLyt.setGravity(Gravity.CENTER);
		footLyt.setGravity(Gravity.CENTER);
//		foot.setBackgroundColor(Color.BLUE);
		
		center.addView(headerLyt);
		center.addView(body);
		center.addView(footLyt);
			
		super.addView(leftLyt);
		super.addView(center);
		super.addView(rightLyt);
		
		headerLyt.setVisibility(View.GONE);
		leftLyt.setVisibility(View.GONE);
		footLyt.setVisibility(View.GONE);
		rightLyt.setVisibility(View.GONE);
		
	}
	
	public void initPadding(){
		if(leftLyt.getVisibility()==View.GONE){
			return;
		}
		int paddingSize = body.getHeight()/left.getViewList().size()/2;
		if(center.getPaddingTop()==0){
			if(headerLyt.getVisibility()==View.GONE){
				center.setPadding(center.getPaddingLeft(), paddingSize, center.getPaddingRight(), center.getPaddingBottom());
			}else{
				int h = headerLyt.getHeight()-paddingSize;
				 if(h>0){
					 leftLyt.setPadding(leftLyt.getPaddingLeft(), h, leftLyt.getPaddingRight(), leftLyt.getPaddingBottom());
				 }else if(h<0){
					 center.setPadding(center.getPaddingLeft(), Math.abs(h), center.getPaddingRight(), center.getPaddingBottom());
				 }
			}
		}
		
		if(center.getPaddingBottom()==0){
			if(footLyt.getVisibility()==View.GONE){
				center.setPadding(center.getPaddingLeft(), center.getPaddingTop(), center.getPaddingRight(), paddingSize);
			}else{
				int h = footLyt.getHeight()-paddingSize;
				 if(h>0){
					 leftLyt.setPadding(leftLyt.getPaddingLeft(), leftLyt.getPaddingTop(), leftLyt.getPaddingRight(), h);
				 }else if(h<0){
					 center.setPadding(center.getPaddingLeft(), center.getPaddingTop(), center.getPaddingRight(), Math.abs(h));
				 }
			}
		}
	}
	
	
	@Override
	public void addView(View child){
		if(child instanceof Bar){
			Bar b = (Bar)child;
			bar.add(b);
			return;
		}
		super.addView(child);
	}

	@Override
	public void addView(View child, int index) {
		if(child instanceof Bar){
			Bar b = (Bar)child;
			bar.add(b);
			return;
		}
		super.addView(child, index);
	}

	@Override
	public void addView(View child, ViewGroup.LayoutParams params) {
		if(child instanceof Bar){
			Bar b = (Bar)child;
			bar.add(b);
			return;
		}
		super.addView(child, params);
	}

	@Override
	public void addView(View child, int width, int height) {
		if(child instanceof Bar){
			Bar b = (Bar)child;
			bar.add(b);
			return;
		}
		super.addView(child, width, height);
	}

	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		if(child instanceof Bar){
			Bar b = (Bar)child;
			bar.add(b);
			return;
		}
		super.addView(child, index, params);
	}
	
////////////////////////////////////////////////////////////LeftInfo Start ////////////////////////////////////////////////////////
	public class LeftInfo extends DefalutViewGroupSet<LeftInfo>{
		private int fontSize=10;
		private ArrayList<View> namesView= new ArrayList<View>();
		
		public int getNameViewSize(){
			return namesView.size();
		}

		public LeftInfo setNames(String[] names){
			leftLyt.removeAllViews();
			namesView.clear();
			addNames(names);
			return this;
		}
		
		public ArrayList<View> getViewList(){
			return namesView;
		}
		
		public LeftInfo addName(View view){
			LayoutParams leftTextLayoutParam= new LayoutParams(LayoutParams.WRAP_CONTENT,0);
			leftTextLayoutParam.weight=1;
			view.setLayoutParams(leftTextLayoutParam);
			namesView.add(0,view);
			leftLyt.setWeightSum(namesView.size());
			leftLyt.addView(view,0);
			
			if(leftLyt.getVisibility()!=View.VISIBLE){
				leftLyt.setVisibility(View.VISIBLE);
			}
			return this;
		}
		
		public LeftInfo setFontSize(){
			this.fontSize = fontSize;
			for(int i=0;i<namesView.size();i++){
				setTextViewTextSize(namesView.get(i),fontSize);
			}
			return this;
		}
		
		public LeftInfo addNames(String[] names){
			for(int i=0;i<names.length;i++){
				addName(names[i]);
			}
			return this;
		}
		
		public LeftInfo addName(String name){
			TextView text = new TextView(getContext());
			text.setGravity(Gravity.CENTER);
			text.setIncludeFontPadding( false );
			text.setTextSize(fontSize);
			text.setText(name);
			addName(text);
			return this;
		}

		@Override
		public ViewGroup getLayout() {
			return leftLyt;
		}
		@Override
		public LeftInfo getReturnInstance() {
			return this;
		}

		
		
		
	}
//////////////////////////////////////////////////////////// LeftInfo End ////////////////////////////////////////////////////////
	
	
	//////////////////////////////////////////////////////////// BarInfo Start ////////////////////////////////////////////////////////
	public class BarInfo extends DefalutViewGroupSet<Bar>{
		Bar bar;
		/**
		 * Bar를 추가하고 header와 foot을 설정한다.
		 * @param bar Bar
		 * @param headerView
		 * @param footView
		 * @return
		 */
		public Bar add(Bar bar,View headerView,View footView){
			if(headerView==null){
				headerView = new TextView(getContext());
			}
			if(footView==null){
				footView = new TextView(getContext());
			}
			BarStyle ci = new BarStyle(bar,headerView,footView);
			chartInfos.add(ci);
			
			headerLyt.addView(ci.headerLayout);
			body.addView(bar);
			footLyt.addView(ci.footLayout);
			
			headerLyt.setWeightSum(chartInfos.size());
			body.setWeightSum(chartInfos.size());
			footLyt.setWeightSum(chartInfos.size());
			refreshMargin();
			return ci.bar;
		}
		
		public BarInfo setMax(int max){
			BarLayout.this.max = max;
			for(int i=0;i<chartInfos.size();i++){
				BarStyle ci = getChartInfo(i);
				if(ci!=null){
					ci.bar.setMax(max);
				}
			}
			return this;
		}
		public int getMax(){
			return BarLayout.this.max;
		}
		
		public Bar add(Bar bar,View footView){
			return this.add(bar,null,footView);
		}
		public Bar add(Bar bar){
			return this.add(bar,null,null);
		}
		public Bar add(int figure){
			bar = new Bar(getContext());
			bar.setMax(max);
			bar.addFigure(figure,barColor);
			return add(bar);
			
		}
		public Bar add(int figure,int color){
			bar = new Bar(getContext());
			bar.setMax(max);
			bar.addFigure(figure, color);
			return add(bar);
		}
		public Bar add(int figure,String headerName,String footName){
			bar = new Bar(getContext());
			bar.setMax(max);
			bar.addFigure(figure,barColor);
			BarStyle ci = addBarStyle(bar,null,null);
			if(headerName!=null){
				setTextViewText(ci.headerView, headerName);
			}
			if(footName!=null){
				setTextViewText(ci.footView, footName);
			}
			return ci.bar;
		}
		public Bar add(int figure,String headerName,String footName,int color){
			bar = new Bar(getContext());
			bar.setMax(max);
			bar.addFigure(figure,barColor);
			BarStyle ci = addBarStyle(bar,null,null);
			if(headerName!=null){
				setTextViewText(ci.headerView, headerName);
			}
			if(footName!=null){
				setTextViewText(ci.footView, footName);
			}
			return ci.bar;
		}
		
		public Bar add(int figure,String footName){
			return add(figure,null,footName);
		}
		public Bar add(int figure,String footName,int color){
			return add(figure,null,footName,color);
		}
		public BarInfo setDefalutColor(int color){
			barColor = color;
			return this;
		}
		
		private BarStyle addBarStyle(Bar bar,View headerView,View footView){
			if(headerView==null){
				headerView = new TextView(getContext());
			}
			if(footView==null){
				footView = new TextView(getContext());
			}
			BarStyle ci = new BarStyle(bar,headerView,footView);
			chartInfos.add(ci);
			
			headerLyt.addView(ci.headerLayout);
			body.addView(bar);
			footLyt.addView(ci.footLayout);
			
			headerLyt.setWeightSum(chartInfos.size());
			body.setWeightSum(chartInfos.size());
			footLyt.setWeightSum(chartInfos.size());
			refreshMargin();
			return ci;
		}
		
		public void refreshMargin(){
			setMargin(marginScale);
		}
		
		public void setMargin(float scale){
			marginScale = scale;
			float margin = getWidth()/(chartInfos.size()+1)/4*scale;
			for(int i=0;i<chartInfos.size();i++){
				Bar bar = chartInfos.get(i).bar;
				bar.setMargin((int)margin, 0, (int)margin, 0);
			}
		}
		@Override
		public ViewGroup getLayout() {
			return bar;
		}
		@Override
		public Bar getReturnInstance() {
			return bar;
		}
	}
	//////////////////////////////////////////////////////////// BarInfo End ////////////////////////////////////////////////////////
	
	
	//////////////////////////////////////////////////////////// FootInfo Start ////////////////////////////////////////////////////////
	public class FootInfo{
		public void setVisibility(int visibility){
			footLyt.setVisibility(visibility);
			
		}
		public int getVisibility(){
			return footLyt.getVisibility();
		}
		
		public void setText(String name, int index){
			BarStyle ci = getChartInfo(index);
			if(ci!=null){
				setTextViewText(ci.footView,name);
			}
			setVisibility(View.VISIBLE);
		}
		public String getText(int index){
			BarStyle ci =  getChartInfo(index);
			if(ci!=null){
				return getTextViewText(ci.footView);
			}
			return null;
		}
		public void setTextColor(int index,int color){
			BarStyle ci =  getChartInfo(index);
			setTextViewColor(ci.footView,color);
		}
		public void setTextColor(int color){
			for(int i=0;i<chartInfos.size();i++){
				BarStyle ci =  getChartInfo(i);
				setTextViewColor(ci.footView,color);
			}
		
		}
		public void setTextSize(int index,float size){
			BarStyle ci =  getChartInfo(index);
			setTextViewSize(ci.footView,size);
		}
		public void setTextSize(float size){
			for(int i=0;i<chartInfos.size();i++){
				BarStyle ci =  getChartInfo(i);
				setTextViewSize(ci.footView,size);
			}
			setVisibility(View.VISIBLE);
		}
		
		public void setView(View view,int index){
			BarStyle ci =  getChartInfo(index);
			if(ci!=null){
				ci.footView = view;
			}
			setVisibility(View.VISIBLE);
		}
		public View getView(int index){
			BarStyle ci =  getChartInfo(index);
			if(ci!=null){
				return ci.footView;
			}
			return null;
		}
		public void setNames(String[] names){
			for(int i=0;i<names.length;i++){
				setText(names[i],i);
			}
			setVisibility(View.VISIBLE);
		}
		public String[] getNames(){
			String[] names = new String[chartInfos.size()];
			for(int i=0;i<names.length;i++){
				names[i]=getText(i);
			}
			return names;
		}
		public void setViews(View[] Views){
			for(int i=0;i<Views.length;i++){
				setView(Views[i],i);
			}
			setVisibility(View.VISIBLE);
		}
		public View[] getViews(){
			View[] views = new View[chartInfos.size()];
			for(int i=0;i<views.length;i++){
				views[i]=getView(i);
			}
			return views;
		}
	}
	//////////////////////////////////////////////////////////// FootInfo End ////////////////////////////////////////////////////////
	

	//////////////////////////////////////////////////////////// HeaderInfo Start ////////////////////////////////////////////////////////
	public class HeaderInfo{
		public void setText(String name, int index){
			BarStyle ci =  getChartInfo(index);
			if(ci!=null){
				setTextViewText(ci.headerView,name);
			}
			setVisibility(View.VISIBLE);
		}
		
		public String getText(int index){
			BarStyle ci =  getChartInfo(index);
			if(ci!=null){
				return getTextViewText(ci.headerView);
			}
			return null;
		}
		
		
		public void setView(View view,int index){
			BarStyle ci =  getChartInfo(index);
			if(ci!=null){
				ci.headerView = view;
			}
			setVisibility(View.VISIBLE);
		}
		public View getView(int index){
			BarStyle ci =  getChartInfo(index);
			if(ci!=null){
				return ci.headerView;
			}
			return null;
		}
		

		public void setTexts(String[] names){
			for(int i=0;i<names.length;i++){
				setText(names[i],i);
			}
			setVisibility(View.VISIBLE);
		}
		public String[] getTexts(){
			String[] names = new String[chartInfos.size()];
			for(int i=0;i<names.length;i++){
				names[i]=getText(i);
			}
			return names;
		}
		
		
		public void setViews(View[] Views){
			for(int i=0;i<Views.length;i++){
				setView(Views[i],i);
			}
			setVisibility(View.VISIBLE);
		}
		public View[] getViews(){
			View[] views = new View[chartInfos.size()];
			for(int i=0;i<views.length;i++){
				views[i]=getView(i);
			}
			setVisibility(View.VISIBLE);
			return views;
		}
	}
//////////////////////////////////////////////////////////// HeaderInfo End ////////////////////////////////////////////////////////
	
	public BarStyle getChartInfo(int index){
		if(0<=index && index <chartInfos.size()){
			return chartInfos.get(index);
		}else{
			return null;
		}
	}
	
	private void setTextViewText(View view,String text){
		if(view instanceof TextView){
			((TextView)view).setText(text);
		}
	}
	private void setTextViewTextSize(View view,int size){
		if(view instanceof TextView){
			((TextView)view).setTextSize(size);
		}
	}
	private String getTextViewText(View view){
		if(view instanceof TextView){
			return ((TextView)view).getText().toString();
		}else{
			return null;
		}
	}
	private void setTextViewColor(View view,int color){
		if(view instanceof TextView){
			((TextView)view).setTextColor(color);
		}
	}
	private void setTextViewSize(View view,float size){
		if(view instanceof TextView){
			((TextView)view).setTextSize(size);
		}
	}
	
	
	
	/**
	 * 그래프 바디(Bar부분)의 Backgournd를 설정한다.
	 * @param background
	 */
	public void setBodyBackground(Drawable background){
		body.setBackgroundDrawable(background);
	}
	/**
	 * 그래프 바디(Bar부분)의 Backgournd를 리소스Id로 설정한다.
	 * @param resid 리소스Id
	 */
	public void setBodyBackgroundResource(int resid){
		body.setBackgroundResource(resid);
	}
	/**
	 * 그래프 바디(Bar부분)의 Backgournd의 컬러를 설정한다.
	 * @param color 컬러
	 */
	public void setBodyBackgroundColor(int color){
		body.setBackgroundColor(color);
	}
	
	/**
	 * 그래프 배경 라인의 갯수를 설정한다.
	 * @param count
	 */
	public void setLineCount(int count){
		body.setLineCount(count);
	}
	/**
	 * 그래프 배경 라인의 갯수를 가져온다.
	 * @return
	 */
	public int getLineCount(){
		return body.getLineCount(); 
	}
		
	class BarStyle{
		int index;
		public LinearLayout headerLayout;
		public LinearLayout footLayout;
		public View headerView;
		public Bar bar;
		public View footView;
		
		/**
		 * 생성자.<br>
		 * header, Bar, Foot의 View를 받아서 구성해준다.
		 * @param bar Bar
		 * @param headeriew Header View
		 * @param footView Foot View
		 */
		public BarStyle(Bar bar,View headerView,View footView){
			this.bar = bar;
			this.headerView = headerView;
			this.footView = footView;
			headerLayout = new LinearLayout(bar.getContext());
			headerLayout.setOrientation(LinearLayout.HORIZONTAL);
			headerLayout.setLayoutParams(new LayoutParams(0,LayoutParams.WRAP_CONTENT,1));
			headerLayout.setGravity(Gravity.CENTER);
			headerLayout.addView(headerView);
			footLayout = new LinearLayout(bar.getContext());
			footLayout.setOrientation(LinearLayout.HORIZONTAL);
			footLayout.setLayoutParams(new LayoutParams(0,LayoutParams.WRAP_CONTENT,1));
			footLayout.setGravity(Gravity.CENTER);
			footLayout.addView(footView);
		}
		
		public Bar getBar(int barIndex){
			return ((Bar)body.getChildAt(barIndex));
		}
		
		public BarStyle setBackgroundColor(int barIndex, int elementIndex, int color){
			getElement(barIndex,elementIndex).setBackgroundColor(color);
			return this;
		}
		
		public BarStyle setBackgroundColor(int color){
			for(int i=0;i<body.getChildCount();i++){
				((Bar)body.getChildAt(i)).setBackgroundColor(color);
			}
			return this;
		}
		public BarStyle setBodyBackgroundDrawable(int index, Drawable background){
			((Bar)body.getChildAt(index)).setBackgroundDrawable(background);
			return this;
		}
		public BarStyle setBodyBackgroundResource(int index, int resid){
			((Bar)body.getChildAt(index)).setBackgroundResource(resid);
			return this;
		}
		public BarStyle setBodyBackgroundResource(int resid){
			for(int i=0;i<body.getChildCount();i++){
				((Bar)body.getChildAt(i)).setBackgroundResource(resid);
			}
			return this;
		}
		public BarStyle setBackgroundDrawable(int index, Drawable background){
			((Bar)body.getChildAt(index)).setBackgroundDrawable(background);
			return this;
		}
		public BarStyle setBackgroundDrawable(Drawable background){
			for(int i=0;i<body.getChildCount();i++){
				((Bar)body.getChildAt(i)).setBackgroundDrawable(background);
			}
			return this;
		}
		public BarStyle setBackgroundResource(int index, int resid){
			((Bar)body.getChildAt(index)).setBackgroundResource(resid);
			return this;
		} 
		public BarStyle setBackgroundResource(int resid){
			for(int i=0;i<body.getChildCount();i++){
				((Bar)body.getChildAt(i)).setBackgroundResource(resid);
			}
			return this;
		}
		public BarElement getElement(Bar bar, int elementIndex){
			return bar.getElement(elementIndex);
		}
		public BarElement getElement(int barIndex, int elementIndex){
			return getElement(((Bar)body.getChildAt(barIndex)),elementIndex);
		}
		
	}
	
	private abstract class DefalutViewGroupSet<ReturnInstance>{
		public abstract ViewGroup getLayout();
		public abstract ReturnInstance getReturnInstance();
		
		public ReturnInstance setPadding(int left, int top, int right, int bottom){
			ViewGroup viewGroup = getLayout();
			viewGroup.setPadding(left, top, right, bottom);
			return getReturnInstance();
		}
		public ReturnInstance setPaddingLeft(int left){
			ViewGroup viewGroup = getLayout();
			viewGroup.setPadding(left, viewGroup.getPaddingTop(), viewGroup.getPaddingRight(), viewGroup.getPaddingBottom());
			return  getReturnInstance();
		}
		public ReturnInstance setPaddingTop(int top){
			ViewGroup viewGroup = getLayout();
			viewGroup.setPadding(viewGroup.getPaddingLeft(), top, viewGroup.getPaddingRight(), viewGroup.getPaddingBottom());
			return getReturnInstance();
		}
		public ReturnInstance setPaddingRight(int right){
			ViewGroup viewGroup = getLayout();
			viewGroup.setPadding(viewGroup.getPaddingLeft(), viewGroup.getPaddingTop(), right, viewGroup.getPaddingBottom());
			return  getReturnInstance();
		}
		public ReturnInstance setPaddingBottom(int bottom){
			ViewGroup viewGroup = getLayout();
			viewGroup.setPadding(viewGroup.getPaddingLeft(), viewGroup.getPaddingTop(), viewGroup.getPaddingRight(), bottom);
			return  getReturnInstance();
		}
		
//		public ReturnInstance setBackground(Drawable background){
//			getLayout().setBackground(background);
//			return getReturnInstance();
//		}
		public ReturnInstance setBackgroundColor(int color){
			getLayout().setBackgroundColor(color);
			return getReturnInstance();
		}
		public ReturnInstance setBackgroundDrawable(Drawable background){
			getLayout().setBackgroundDrawable(background);
			return getReturnInstance();
		}
		public ReturnInstance setBackgroundResource(int resid){
			getLayout().setBackgroundResource(resid);
			return getReturnInstance();
		}
		
		public ReturnInstance setVisibility(int visibility){
			getLayout().setVisibility(visibility);
			return getReturnInstance();
		}
		public int getVisibility(){
			return getLayout().getVisibility();
		}
	}
	
}
