package com.englishtyping.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 管理员操作日志注解
 * 用于标记需要记录操作日志的管理员方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminOperation {
    
    /**
     * 操作类型（CREATE, UPDATE, DELETE, BAN, UNBAN, RESET_PASSWORD, etc.）
     */
    String type();
    
    /**
     * 操作对象类型（USER, CATEGORY, LEVEL, EXERCISE）
     */
    String targetType();
    
    /**
     * 操作描述
     */
    String description() default "";
}
