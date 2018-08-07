package com.zzf.common.thread;

import java.util.concurrent.locks.LockSupport;

/**
 * 线程阻塞工具类
 * @author zhangzengfu
 *
 */
public class LockSupportDemo {
	public static Object o = new Object();
	static ChangeObjectThread t1 = new ChangeObjectThread("t1");
	static ChangeObjectThread t2 = new ChangeObjectThread("t2");

	public static class ChangeObjectThread extends Thread {
		public ChangeObjectThread(String name) {
			super.setName(name);
		}

		@Override
		public void run() {
			synchronized (o) {
				System.out.println("in " + getName());
				LockSupport.park();// 可以阻塞当前线程
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		t1.start();
		Thread.sleep(100);
		t2.start();
		LockSupport.unpark(t1);
		LockSupport.unpark(t2);
		t1.join();
		t2.join();
	}

}
