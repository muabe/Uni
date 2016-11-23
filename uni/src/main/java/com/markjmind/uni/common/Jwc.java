package com.markjmind.uni.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

/**
 * start : 2012.08.30<br>
 * <br>
 * 비동기 Layout Controll인 JwViewController를 확장한 클래스이다.<br>
 * Layout에 관련된 Util을 추가확장 했다.<br>
 * 
 * @author 오재웅      
 * @version 2013.11.17
 */
public class Jwc{
	
	
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

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public static void setVisible(View target, boolean isVisible, int falseVisible){
		if(isVisible){
			target.setVisibility(View.VISIBLE);
		}else{
			target.setVisibility(falseVisible);
		}
	}

	public static void setVisible(View target, boolean isVisible){
		Jwc.setVisible(target,isVisible, View.GONE);
	}


	public static boolean isActivityRunning(Context context){
		ActivityManager actMng = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = actMng.getRunningAppProcesses();

		for(ActivityManager.RunningAppProcessInfo info : list){
			if(info.processName.equals(context.getPackageName()))
			{
				return true;
			}

		}
		return false;
	}

	public static void toast(Context context, String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void hideKeyboard(Context context, EditText editText){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	public static void hideKeyboard(Activity activity){
		View view = activity.getCurrentFocus();
		if ( view != null ) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public static void showKeyboard(Context context, EditText editText){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, 0);

	}

	public static View lastChild(ViewGroup parantView){
		return parantView.getChildAt(parantView.getChildCount() - 1);
	}

	public static View getInfalterView(Context context, int layout_id){
//		return LayoutInflater.from(context).inflate(layout_id, null);
		return ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout_id, null);
//		return View.inflate(context, layout_id,null);
	}

	public static View getInfalterView(Context context, int layout_id, ViewGroup parents){
		return ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout_id, parents);
	}


	public static View findViewById(View view, int R_id){
		return view.findViewById(R_id);
	}


	public static View findViewWithTag(View parants, Object tag){
		return parants.findViewWithTag(tag);
	}
}
