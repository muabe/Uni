package com.markjmind.uni;

import java.lang.reflect.Field;

import android.view.View;

import com.markjmind.uni.annotiation.Box;
import com.markjmind.uni.annotiation.GetView;
import com.markjmind.uni.annotiation.GetViewClick;
import com.markjmind.uni.annotiation.Layout;
/**
 * 
 * @author 오재웅
 * @email markjmind@gmail.com
 */

public class JwMemberMapper {
	
	public static String[] injectionBox(Class<?> viewerClass){
		if(viewerClass.isAnnotationPresent(Box.class)){
			Box par = viewerClass.getAnnotation(Box.class);
			return par.value();
		}else{
			throw new JwMapperException("\n["+viewerClass.getName()+"] 해당 Viewer에 @params을 지정하는 annotation의 value가 잘못되었습니다..",null);
		}
	}
	
	public static int injectionLayout(Class<?> viewerClass){
		if(viewerClass.isAnnotationPresent(Layout.class)){
			Layout lytId = viewerClass.getAnnotation(Layout.class);
			return lytId.value();
		}else{
			throw new JwMapperException("\n["+viewerClass.getName()+"] 해당 Viewer에 @layout을 지정하는 annotation의 value가 잘못되었습니다.",null);
		}
		
	}
	
	public static void injectField(Viewer obj){
		Field[] fields = obj.getClass().getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			if(fields[i].isAnnotationPresent(GetView.class)){
				GetView ab = fields[i].getAnnotation(GetView.class);
				int id = ab.value();
				id = setField(obj,id,fields[i]);
				String click = ab.click();
				if(!"".equals(click) && click.length()>0){
					obj.setOnClickListener(click, id);
				}
			}else if(fields[i].isAnnotationPresent(GetViewClick.class)){
				GetViewClick ab = fields[i].getAnnotation(GetViewClick.class);
				int id = ab.value();
				id = setField(obj,id,fields[i]);
				String click = ab.click();
				if(!"".equals(click) && click.length()>0){
				}else{
					click = fields[i].getName();
				}
				obj.setOnClickListener(click, id);
			}
		}
	}
	
	private static int setField(Viewer obj, int id, Field field){
		if(id==-1){
			id = JwStringID.getID(field.getName(), obj.getActivity().getApplication());
		}
		View v = obj.getView(id);
		if(v==null){
			throw new JwMapperException("\n["+obj.getClass().getName()+"] Field:"+field.getName()+", Filed에 해당하는 ID(Null)가 잘못 지정되었습니다.",null);
		}
		try {
			field.setAccessible(true);
			field.set(obj, v);
		} catch (IllegalArgumentException e) {
			throw new JwMapperException("\n["+obj.getClass().getName()+"] Field:"+field.getName()+", 잘못된 Field에 해당하는 ID를 찾을수 없습니다.",e);
		} catch (IllegalAccessException e) {
			throw new JwMapperException("\n["+obj.getClass().getName()+"] Field:"+field.getName()+", 접근권한이 없는 필드입니다.",e);
		}
		return id;
	}
	
	
	

}
