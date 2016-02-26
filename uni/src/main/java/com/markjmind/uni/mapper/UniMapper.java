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
import android.content.Context;
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

    public UniMapper(){

    }

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


    public void reset(View finder, Object targetObject){
        super.reset(finder, targetObject);
        initAdapter();
    }

    public void reset(Activity finder, Object targetObject){
        super.reset(finder, targetObject);
        initAdapter();
    }

    public void reset(Dialog finder, Object targetObject){
        super.reset(finder, targetObject);
        initAdapter();
    }


    public Context getContext(){
        return context;
    }

    public Object getTarget(){
        return targetObject;
    }


    //	public String[] injectBox(){
//		if(targetClass.isAnnotationPresent(Box.class)){
//			Box par = targetClass.getAnnotation(Box.class);
//			return par.value();
//		}else{
//			throw new UinMapperException(ErrorMessage.Runtime.box(targetClass),null);
//		}
//	}

}
