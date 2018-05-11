package com.markjmind.uni.mapper.annotiation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention( RetentionPolicy.RUNTIME )
@Target(ElementType.METHOD )
public @interface OnClick
{
    int value() default -1;
    int[] ids() default {};
}