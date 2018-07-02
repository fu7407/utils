package com.zzf.common.classloader;

import java.lang.reflect.Method;

public class ClassLoaderTest {
	public static void main(String[] args) throws Exception {

		DecryptClassLoader classLoader = new DecryptClassLoader();
		Class<?> aClass = classLoader.loadClass("com.wangwenjun.concurrent.classloader.chapter3.MyObject");
		System.out.println(aClass);

		Object obj = aClass.newInstance();
		Method method = aClass.getMethod("hello", new Class<?>[] {});
		Object result = method.invoke(obj, new Object[] {});
		System.out.println(result);

	}
}