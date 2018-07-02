package com.zzf.common.classloader;

public class RuntimePackage {
	// RuntimePackage
	// com.wangwenjun.concurrent.classloader.chapter5
	// Boot.Ext.App.com.wangwenjun.concurrent.classloader.chapter5

	// Boot.Ext.App.com.wangwenjun.concurrent.classloader.chapter5.SimpleClassLoaderTest
	// Boot.Ext.App.SimpleClassLoader.com.wangwenjun.concurrent.classloader.chapter5.SimpleClassLoaderTest

	public static void main(String[] args)
			throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		SimpleClassLoader simpleClassLoader = new SimpleClassLoader();
		Class<?> aClass = simpleClassLoader.loadClass("com.zzf.common.classloader.SimpleObject");
		// sSystem.out.println(aClass.getClassLoader());
		SimpleObject simpleObject = (SimpleObject) aClass.newInstance();
	}
}
