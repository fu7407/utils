package com.zzf.common.designpatterns.singleton;

/**
 * 线程安全，延迟加载（调用方法getInstance时创建实例对象）,双重检查，比Singleton3性能好很多
 * @author zhangzengfu
 *
 */
public class Singleton4 {

	private volatile static Singleton4 instance = null;

	public static Singleton4 getInstance() {
		if (instance == null) {
			synchronized (Singleton4.class) {
				if (instance == null) {
					instance = new Singleton4();
				}
			}
		}
		return instance;
	}

}
