package com.markjmind.uni.util;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-11-02
 */

public class ReflectionUtil {
    public static <T>T getInstance(Class<T> clz){
        T frag = null;
        try {
            frag = clz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return frag;
    }

    public static <T>T getSafeInstance(Class<T> clz){
        T frag = null;
        try {
            frag = clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return frag;
    }
}
