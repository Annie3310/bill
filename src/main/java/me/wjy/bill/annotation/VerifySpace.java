package me.wjy.bill.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用该注解的方法, 会在被执行前判断输入的密码是否包含空格, 如果包含则抛出异常
 * @author 王金义
 * @date 2021-08-11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifySpace {
}
