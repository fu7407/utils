package com.zzf.common.designpatterns.singleton;

/**
 * 线程安全，延迟加载（调用方法getInstance时创建实例对象）,性能最好
 * @author zhangzengfu
 *
 */
public class Singleton5 {

	private Singleton5() {

	}

	private static class LazyHolder {
		private static final Singleton5 instance = new Singleton5();
	}

	public static Singleton5 getInstance() {
		return LazyHolder.instance;
	}

}
