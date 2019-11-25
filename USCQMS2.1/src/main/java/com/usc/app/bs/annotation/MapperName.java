package com.usc.app.bs.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @describe 定义注解，Service中使用该注解指定Mapper的namespace
 * @author caochengde
 * @data 2018年10月23日
 *
 */
@Documented
@Target (ElementType.TYPE)
@Retention (RetentionPolicy.RUNTIME)
public @interface MapperName {
    String value() default "";
}
