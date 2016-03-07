package com.markjmind.uni.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.markjmind.uni.common.JwViewController;

/**
 * start : 2012.08.30<br>
 * <br>
 * 비동기 Layout Controll인 JwViewController를 확장한 클래스이다.<br>
 * Layout에 관련된 Util을 추가확장 했다.<br>
 * 
 * @author 오재웅      
 * @version 2013.11.17
 */
public class Jwc extends JwViewController {
	
	
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

	public static int getDp(Context context, int pix){
		return (int)(pix/context.getResources().getDisplayMetrics().density);
	}

	public static int getPix(Context context, int dp){
		return (int)(dp*context.getResources().getDisplayMetrics().density);
	}

	public static int getWindowHeight(Context context){
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.y;
	}

	public static int getWindowWidth(Context context){
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.x;
	}

}
