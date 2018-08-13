package com.zzf.common.designpatterns.singleton;

/**
 * 非线程安全，延迟加载（调用方法getInstance时创建实例对象）
 * @author zhangzengfu
 *
 */
public class Singleton2 {

	private static Singleton2 instance = null;

	public static Singleton2 getInstance() {
		if (instance == null) {
			instance = new Singleton2();
		}
		return instance;
	}

}
