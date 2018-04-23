package com.example.test_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * created by liyunlei
 * email: YunLeiLi@sf-express.com
 * at: 2018年4月18日20:31:48
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Test {

    Class<?> type();

    String id();

}
