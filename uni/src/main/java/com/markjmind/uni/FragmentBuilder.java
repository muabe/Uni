package com.markjmind.uni;

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

    protected void setUniFragment(T uniFragment){
        this.uniFragment = uniFragment;
    }

    public T build(){
        return uniFragment;
    }
}
