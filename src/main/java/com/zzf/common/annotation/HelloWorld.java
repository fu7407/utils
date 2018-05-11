package com.zzf.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME) //定义的这个注解是注解会在class字节码文件中存在，在运行时可以通过反射获取到。
@Target(ElementType.METHOD)
@Inherited
public @interface HelloWorld {
	public String name()default "";
}
