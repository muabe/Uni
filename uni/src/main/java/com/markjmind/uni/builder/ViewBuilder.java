package com.markjmind.uni.builder;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-02-01
 */
public class ViewBuilder<T extends BuildInterface> extends BaseBuilder<T> {
    public ViewBuilder(BuildInterface buildInterface, T injectionObject) {
        super(buildInterface, injectionObject);
    }
}
