package com.newland.mi.wrc.annotation;

import java.lang.annotation.*;

/**
 * @author Miracle
 * @date 2018/10/19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamsExist {
    String[] value()  default {};

    String[] file() default {};

    String type() default "POST";
}

