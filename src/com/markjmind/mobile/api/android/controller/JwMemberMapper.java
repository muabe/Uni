package com.markjmind.mobile.api.android.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.util.Log;
import android.view.View;

public class JwMemberMapper {
	
	public void getFieldList(JwViewer obj){
		Class cls = obj.getClass();
		Field[] fields = cls.getDeclaredFields();
		Log.d("ddddddddddd", "--------------------------------------------------------------------------------------------------------------");
		for(int i=0;i<fields.length;i++){
			String modifiers = "";
			switch (fields[i].getModifiers()) {
				case 0:
					modifiers = "";
					break;
				case 1:
					modifiers = "public";
					break;
				case 2:
					modifiers = "private";
					break;
				case 4:
					modifiers = "protected";
					break;
			}
			Log.d("ddddddddddd", "("+fields[i].getType().getName()+")"+modifiers+" "+fields[i].getName());
			try {
				View view = obj.getViewTag(fields[i].getName());
				if(view!=null){
					fields[i].set(obj, fields[i].getType().cast(view));
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		Log.d("ddddddddddd", "--------------------------------------------------------------------------------------------------------------");
	}
}
