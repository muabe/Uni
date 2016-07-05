package com.markjmind.uni.mapper.annotiation;


import com.markjmind.uni.mapper.ClassInjectAdapter;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-07-04
 */
public class LayoutInjector extends ClassInjectAdapter<Layout> {
    int layoutId = 0;

    public int getLayoutId(){
        return layoutId;
    }

    @Override
    public Class<Layout> getAnnotationType() {
        return Layout.class;
    }

    @Override
    public void injectClass(Layout annotation, Class targetClass, Object targetObject) {
        layoutId = annotation.value();
    }
}
