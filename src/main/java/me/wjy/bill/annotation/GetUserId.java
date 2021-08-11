package me.wjy.bill.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用该注解的方法, 会在进入方法前将在 token 中保存的 userId 赋值给入参对象
 * @author 王金义
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetUserId {
}
