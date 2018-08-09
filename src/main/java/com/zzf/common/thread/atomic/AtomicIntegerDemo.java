package com.zzf.common.thread.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDemo {
	static AtomicInteger i = new AtomicInteger();

	public static class AddThread implements Runnable {
		@Override
		public void run() {
			for (int k = 0; k < 10000; k++) {
				i.incrementAndGet();
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
		System.out.println(i);
	}

}
