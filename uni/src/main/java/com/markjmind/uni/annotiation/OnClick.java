package com.markjmind.uni.annotiation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target(ElementType.METHOD )
public @interface OnClick
{
    int value() default -1;
    int[] ids() default {};
}