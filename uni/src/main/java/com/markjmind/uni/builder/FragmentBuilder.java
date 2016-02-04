package com.markjmind.uni.builder;

import com.markjmind.uni.UniFragment;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-01
 */
public class FragmentBuilder<T extends UniFragment> extends BaseBuilder<T> {
    private T uniFragment;

    public FragmentBuilder(BuildInterface buildInterface, T injectionObject) {
        super(buildInterface, injectionObject);
    }

    public void setUniFragment(T uniFragment){
        this.uniFragment = uniFragment;
    }

    public T build(){
        return uniFragment;
    }
}
