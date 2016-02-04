package com.markjmind.uni.builder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.markjmind.uni.UniView;
import com.markjmind.uni.mapper.Mapper;

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
        Mapper mapper = new Mapper(uniView, injectionObject);
        int layoutId = mapper.injectionLayout();
        uniView.setLayout((ViewGroup) inflater.inflate(layoutId, container, false));
//        uniView.setUniInterface(buildInterface.getUniInterface());
        mapper.injectionView();
        return uniView;
    }
}
