package com.mqz.annotation;

import java.lang.annotation.*;

/**
 * 为了解决进行Bean拷贝的时候字段名名字不一一样的问题
 */
@Inherited
@Target({ElementType.ANNOTATION_TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CopySourceName {
    String value();
}
