package com.gjk.log.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * LogIgnore
 *
 * @author: gaojiankang
 * @date: 2023/4/11 9:28
 * @description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LogIgnore {
}
