package com.markjmind.mobile.api.android.ui.wrapper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
/**
 * 
 * @author 오재웅
 * @email markjmind@gmail.com
 */
public class JwAlertDialog {

	public static void button(String title, String msg, String[] btnNames, Context context, OnClickListener onClickListener){
		AlertDialog alertDlg = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
//		builder.setIcon(R.drawable.ic_launcher);
		
		if(btnNames.length>=1){
			builder.setPositiveButton(btnNames[0], onClickListener);
		}
		if(btnNames.length>=2){
			builder.setNegativeButton(btnNames[1], onClickListener);
		}
		if(btnNames.length>=3){
			builder.setNeutralButton(btnNames[2], onClickListener);
		}
		
		alertDlg = builder.create();
		alertDlg.show();
	}
	
	public static AlertDialog getAlertDialog(String title, String msg, Context context){
		AlertDialog alertDlg = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(msg);
		alertDlg = builder.create();
		return alertDlg;
	}
}
