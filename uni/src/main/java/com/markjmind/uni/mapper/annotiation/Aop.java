package com.markjmind.uni.mapper.annotiation;

import com.markjmind.uni.thread.aop.AopListener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Muabe on 2018-05-11.
 */

@Inherited
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface Aop {
    Class<? extends AopListener> value();
}
