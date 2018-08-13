package com.zzf.common.designpatterns.singleton;

/**
 * 线程安全，延迟加载（调用方法getInstance时创建实例对象）,但性能大不如前
 * @author zhangzengfu
 *
 */
public class Singleton3 {

	private static Singleton3 instance = null;

	public static synchronized Singleton3 getInstance() {
		if (instance == null) {
			instance = new Singleton3();
		}
		return instance;
	}

}
