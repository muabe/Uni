package com.markjmind.uni;

import android.view.LayoutInflater;
import android.view.ViewGroup;

public class BindLayoutInfo {
    private LayoutInflater inflater;
    private ViewGroup container;

    public void setBindLayoutInfo(LayoutInflater inflater, ViewGroup container){
        this.inflater = inflater;
        this.container = container;
    }

    public LayoutInflater getLayoutInflater(){
        return inflater;
    }

    public ViewGroup getContainer(){
        return container;
    }
}
