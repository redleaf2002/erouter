package com.leaf.erouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by suhong01 on 2018/7/31.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface EasyRegister {
}
