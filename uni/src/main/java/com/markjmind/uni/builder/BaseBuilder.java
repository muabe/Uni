package com.markjmind.uni.builder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.markjmind.uni.UniView;
import com.markjmind.uni.mapper.UniMapper;
import com.markjmind.uni.mapper.annotiation.adapter.LayoutAdapter;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-01
 */
public class BaseBuilder<T> {
    BuildInterface buildInterface;
    protected ViewGroup container;
    private T injectionObject;

    public BaseBuilder(BuildInterface buildInterface, T injectionObject){
        this.buildInterface = buildInterface;
        this.injectionObject = injectionObject;
    }

    public BaseBuilder setContainer(ViewGroup container){
        this.container = container;
        return this;
    }

    protected UniView mapping(){
        UniView uniView = buildInterface.getUniView();
        LayoutInflater inflater = ((LayoutInflater) uniView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        UniMapper mapper = new UniMapper(uniView, injectionObject);
        int layoutId = mapper.getAdapter(LayoutAdapter.class).getLayoutId();
//        uniView.setView((ViewGroup) inflater.inflate(layoutId, container, false));
//        uniView.setUniInterface(buildInterface.getUniInterface());
        mapper.inject();
        return uniView;
    }
}
