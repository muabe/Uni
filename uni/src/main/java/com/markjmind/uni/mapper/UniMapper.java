/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.markjmind.uni.mapper;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import com.markjmind.uni.mapper.annotiation.adapter.GetViewAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.LayoutAdapter;
import com.markjmind.uni.mapper.annotiation.adapter.OnClickAdapter;

/**
 * <br>捲土重來<br>
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-15
 */
public class UniMapper extends Mapper{

    public UniMapper(View finder){
        super(finder);
    }

    public UniMapper(Activity finder){
        super(finder);
    }

    public UniMapper(Dialog finder){
        super(finder);
    }

    public UniMapper(View finder, Object targetObject){
        super(finder, targetObject);
        initAdapter();
    }

    public UniMapper(Activity finder, Object targetObject){
        super(finder, targetObject);
        initAdapter();
    }

    public UniMapper(Dialog finder, Object targetObject){
        super(finder, targetObject);
        initAdapter();
    }

    private void initAdapter(){
        addAdapter(new LayoutAdapter());
        addAdapter(new GetViewAdapter());
        addAdapter(new OnClickAdapter());
    }

    public void clear(){
        viewHash.clear();
    }

    //	public String[] injectBox(){
//		if(targetClass.isAnnotationPresent(Box.class)){
//			Box par = targetClass.getAnnotation(Box.class);
//			return par.value();
//		}else{
//			throw new UinMapperException(ErrorMessage.Runtime.box(targetClass),null);
//		}
//	}

//    public int injectLayout(){
//        if(hasLayout()){
//            Layout lytId = targetClass.getAnnotation(Layout.class);
//            return lytId.value();
//        }else{
//            throw new UinMapperException(ErrorMessage.Runtime.injectLayout(targetClass),null);
//        }
//    }

//    public boolean hasLayout(){
//        return targetClass.isAnnotationPresent(Layout.class);
//    }
}
