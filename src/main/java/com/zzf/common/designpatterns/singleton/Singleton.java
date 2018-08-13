package com.zzf.common.designpatterns.singleton;

/**
 * 线程安全，JVM加载此类时即创建实例对象
 * @author zhangzengfu
 *
 */
public class Singleton {

	private static Singleton instance = new Singleton();

	private Singleton() {

	}

	public Singleton getInstance() {
		return instance;
	}

}
