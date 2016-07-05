package com.markjmind.uni.mapper;

import java.lang.annotation.Annotation;

/**
 * <br>捲土重來<br>
 *
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2016-07-04
 */
interface InjectListener<T extends Annotation, E> {
    void inject(T annotation, E element, Object targetObject);
    Class<T> getAnnotationType();
}
