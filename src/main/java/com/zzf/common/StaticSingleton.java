package com.zzf.common;

/**
 * 最好的单例模式实现方式（使用静态内部类）
 * @author zhangzengfu
 *
 */
public class StaticSingleton {

	private StaticSingleton() {
		System.out.println("StaticSingleton is create");
	}

	private static class SingletonHolder {
		private static StaticSingleton instance = new StaticSingleton();
	}

	public StaticSingleton getInstance() {
		return SingletonHolder.instance;
	}

}
