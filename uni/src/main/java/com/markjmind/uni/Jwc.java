package com.markjmind.uni;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * start : 2012.08.30<br>
 * <br>
 * 비동기 Layout Controll인 JwViewController를 확장한 클래스이다.<br>
 * Layout에 관련된 Util을 추가확장 했다.<br>
 * 
 * @author 오재웅      
 * @version 2013.11.17
 */
public class Jwc extends JwViewController{ 
	
	
	public static Drawable getDrawable(int R_drawable_id,View view){
		return view.getResources().getDrawable(R_drawable_id);
	}
	public static Drawable getDrawable(int R_drawable_id,Context context){
		return context.getResources().getDrawable(R_drawable_id);
	}
	public static void setBackgroundDrawable(int R_drawable_id,View view){
		Drawable d = getDrawable(R_drawable_id, view);
		if(d!=null){
			view.setBackgroundDrawable(d);
		}
	}
	public static void setBackgroundResouceTag(String tag, int R_drawable_id,  View parents){
		getViewTag(tag,parents).setBackgroundResource(R_drawable_id);
	}
	
//	public static void setBackground(int R_drawable_id,View view){
//		Drawable d = getDrawable(R_drawable_id, view);
//		if(d!=null){
//			view.setBackground(d); //상위버전에서만 사용
//		}
//	}
//	
	public static void setTextColor(TextView txt,int R_color_id){
		txt.setTextColor(txt.getContext().getResources().getColor(R_color_id));
	}
	public static void setTextTag(String tag,String text,View parants){
		TextView txt = (TextView)getViewTag(tag, parants);
		if(txt!=null){
			txt.setText(text);
		}
	}
	public static void setTextId(int R_id,String text,Activity activity){
		TextView txt = (TextView)getView(R_id, activity);
		if(txt!=null){
			txt.setText(text);
		}
	}
	public static void setTextHtmlTag(String tag,String text,View parents){
		TextView txt = (TextView)getViewTag(tag, parents);
		if(txt!=null){
			txt.setText(Html.fromHtml(text));
		}
	}
	public static void setImageResourceTag(String tag, int R_drawable_id, View parents){
		ImageView img = (ImageView)getViewTag(tag, parents);
		img.setImageResource(R_drawable_id);
	}
	
	public static View setVisible(String tag, View parants, int visiblility){
		View view = getTagView(tag, parants);
		view.setVisibility(visiblility);
		return view;
	}
	public static View gone(String tag, View parants){
		return setVisible(tag,parants,View.GONE);
	}
	public static View visible(String tag, View parants){
		return setVisible(tag,parants,View.VISIBLE);
	}
	public static View invisible(String tag, View parants){
		return setVisible(tag,parants,View.INVISIBLE);
	}
	
	public static int getColor(String color){
		return Color.parseColor(color);
	}

	public static float getDensity(Context context){
		return  context.getResources().getDisplayMetrics().density;
	}
	public static float getDensity(Activity context){
		return  context.getResources().getDisplayMetrics().density;
	}
	public static float getDensity(View view){
		return  view.getContext().getResources().getDisplayMetrics().density;
	}
	
}
