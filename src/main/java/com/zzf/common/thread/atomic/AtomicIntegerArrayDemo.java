package com.zzf.common.thread.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicIntegerArrayDemo {
	static AtomicIntegerArray arr = new AtomicIntegerArray(10);

	public static class AddThread implements Runnable {
		@Override
		public void run() {
			for (int k = 0; k < 10000; k++) {
				arr.getAndIncrement(k % arr.length());// 将第i个下标的元素加1
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Thread[] th = new Thread[10];
		for (int k = 0; k < 10; k++) {
			th[k] = new Thread(new AddThread());
		}
		for (int k = 0; k < 10; k++) {
			th[k].start();
		}
		for (int k = 0; k < 10; k++) {
			th[k].join();
		}
		System.out.println(arr);
	}

}
