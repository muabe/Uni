package com.markjmind.uni.mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-07-05
 */
public abstract class MethodInjectAdapter <T extends Annotation> extends InjectAdapter<T>{


    public InjectListener<T, Class> getClassInjector(){
        return null;
    }

    public InjectListener<T, Field> getFieldInjector(){
        return null;
    }

    public void injectClass(T annotation, Class targetClass, Object targetObject){}
    public void injectField(T annotation, Field field, Object targetObject){}
}