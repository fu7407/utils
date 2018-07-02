package com.zzf.common.classloader;

public class BootClassLoader {

	public static void main(String[] args) throws ClassNotFoundException {
		System.out.println(System.getProperty("sun.boot.class.path"));
		System.out.println(System.getProperty("java.ext.dirs"));

		Class<?> klass = Class.forName("com.zzf.common.classloader.SimpleObject");
		System.out.println(klass.getClassLoader());
		System.out.println(klass.getClassLoader().getParent());
		System.out.println(klass.getClassLoader().getParent().getParent());

		Class<?> clazz = Class.forName("java.lang.String");
		System.out.println(clazz);
		System.out.println(clazz.getClassLoader());

	}
}
