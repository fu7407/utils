package com.zzf.common.classloader;

public class Singleton {

	private static Singleton instance = new Singleton();

	public static int x = 0;

	public static int y;

	/**
	 * int x = 0;
	 * int y = 0;
	 * instance = null;
	 */

	/**
	 * 1.instance = null;
	 * 2.x = 0;
	 * 3.y = 0;
	 * <p>
	 * <p>
	 * instance = new Singleton();
	 * x++=>x=1
	 * y++=>y=1
	 * <p>
	 * x = 0;
	 * y = 1;
	 */

	private Singleton() {
		x++;
		y++;
	}

	public static Singleton getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		Singleton singleton = getInstance();
		System.out.println(singleton.x);
		System.out.println(singleton.y);
	}
}
