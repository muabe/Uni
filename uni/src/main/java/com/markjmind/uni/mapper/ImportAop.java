package com.markjmind.uni.mapper;

import com.markjmind.uni.UniLayout;
import com.markjmind.uni.thread.aop.AopOnPreAdapter;

import java.util.ArrayList;

/**
 * Created by Muabe on 2018-05-11.
 */

public class ImportAop extends AopOnPreAdapter{
    ArrayList<UniLayout> importor;
    public ImportAop(ArrayList<UniLayout> importor){
        this.importor = importor;
    }

    @Override
    public void beforeOnPre() {

    }

    @Override
    public void afterOnPre() {

    }
}
