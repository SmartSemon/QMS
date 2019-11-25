package com.usc.app.bs.annotation;
import java.lang.annotation.*;

/**
 * e 自定义注解，拦截resource
 * @author yds
 *
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemResourceLog {

	String description() default "";
}
