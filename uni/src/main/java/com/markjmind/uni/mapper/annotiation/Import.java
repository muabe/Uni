package com.markjmind.uni.mapper.annotiation;

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
public @interface Import {
    int value();
    int mode() default Import.replace;

    int replace = 0;
    int add = 1;
}
